package com.example.news_app.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.news_app.Adapters.ResultsAdapter;
import com.example.news_app.Models.Articles;
import com.example.news_app.Models.Result;
import com.example.news_app.Networks.ArticlesUtils;
import com.example.news_app.R;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Articles>>{

    // Create a var to store Articles
    private String param_Person;
    private String param_Topic;
    private String param_Location;
    private String BASE_NEWS_URL = "https://content.guardianapis.com/search?";
    private String param_api = "6a469cc7-fb5d-4611-a343-9eaba1eea651";

    private RecyclerView Pages;
    private TextView totalpageSize;
    private TextView currentPage;
    private TextView searchVal;
    private TextView Total;

    private SpeedDialView Paginate;
    private SpeedDialActionItem SpeedDialOne;
    private SpeedDialActionItem SpeedDialTwo;
    private SpeedDialActionItem SpeedDialThree;
    private SpeedDialActionItem SpeedDialFour;

    private String current_page ;
    private String total_pages;

    private int int_current_page ;
    private int int_page_size;

    MaterialSearchView searchView;
    private String param_q;

    private ProgressBar pg;
    private ConnectivityManager cm;
    private NetworkInfo activeNetwork;
    private boolean isConnected;
    private boolean isMetered;
    private TextView TextHex;

    private LinearLayout mainLayout;

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        LayoutInflater mLayoutInflater = getLayoutInflater().from(this);
        @SuppressLint("InflateParams") View mViewCustomActionBar = mLayoutInflater.inflate(R.layout.appbarlayout,null);

        getSupportActionBar().setCustomView(mViewCustomActionBar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        totalpageSize = findViewById(R.id.art_total_pages);
        currentPage = findViewById(R.id.art_current_pages);
        searchVal = findViewById(R.id.art_search_topics);
        Total = findViewById(R.id.art_total);

        Pages = findViewById(R.id.news);

        searchView = mViewCustomActionBar.findViewById(R.id.search_view);
        searchView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        //searchView.setDefaultFocusHighlightEnabled(true);
        searchView.setSoundEffectsEnabled(true);
        searchView.setSuggestionIcon(getResources().getDrawable(android.R.drawable.ic_dialog_info));
        searchView.setTextColor(Color.WHITE);
        mainLayout = findViewById(R.id.main_layout);

        pg = findViewById(R.id.progressBar);
        TextHex = findViewById(R.id.textHex);

        // functions for ui opertaions
        paginateUI();
        searchUI();

        // connectivity checker
        cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnected();
        isMetered = cm.isActiveNetworkMetered();

        Connecting();

        if(isConnected)
            LoadArticles(1,null);
        else
            NotConnecting(getString(R.string.attempting_connection),R.color.orange);
    }

    /**
     *  Adds all java docs that need no hiding during load times.
     */
     private void Connecting(){
         pg.setVisibility(View.GONE);
         TextHex.setVisibility(View.GONE);
         mainLayout.setVisibility(View.VISIBLE);
         Paginate.setVisibility(View.VISIBLE);
     }

    /**
     * hides all the ui elements that need hiding during load times.
     */
    private void NotConnecting(String text,int Color){
        pg.setVisibility(View.VISIBLE);
        pg.setProgressDrawable(getResources().getDrawable(R.drawable.two_state));
        TextHex.setVisibility(View.VISIBLE);
        TextHex.setTextColor(getResources().getColor(Color));
        TextHex.setText(text);
        mainLayout.setVisibility(View.GONE);
        Paginate.setVisibility(View.GONE);
     }

    /**
     *  This method sets up the searching UI.
     */
    private void searchUI() {
        searchView.setVoiceSearch(true);
        searchView.setHintTextColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            searchView.setTooltipText(getString(R.string.enter_vals));
        }
        searchView.setEllipsize(true);
        searchView.setSubmitOnClick(true);
        searchView.setHint(getString(R.string.prefer));
        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                LoadArticles(1,query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                LoadArticles(1,newText);//previous
                return false;
            }
        });
    }


    /**
     *  This methods sets up the pagination UI.
     */
    private void paginateUI() {
        SpeedDialOne  = new SpeedDialActionItem.Builder(R.id.fab_one_label,R.drawable.first)
                .setLabel(getString(R.string.first_page))
                .setLabelColor(getResources().getColor(R.color.strong_light_green))
                .setLabelBackgroundColor(Color.WHITE)
                .setFabBackgroundColor(Color.WHITE)
                .setLabelClickable(false)
                .create();

        SpeedDialTwo  = new SpeedDialActionItem.Builder(R.id.fab_two_label,R.drawable.next)
                .setLabel(getString(R.string.next_page))
                .setLabelColor(getResources().getColor(R.color.strong_light_green))
                .setLabelBackgroundColor(Color.WHITE)
                .setFabBackgroundColor(Color.WHITE)
                .setLabelClickable(false)
                .create();

        SpeedDialThree  = new SpeedDialActionItem.Builder(R.id.fab_three_label,R.drawable.previous)
                .setLabelColor(getResources().getColor(R.color.strong_light_green))
                .setLabelBackgroundColor(Color.WHITE)
                .setLabel(getString(R.string.previous_page))
                .setLabelClickable(false)
                .setFabBackgroundColor(Color.WHITE)
                .create();

        SpeedDialFour  = new SpeedDialActionItem.Builder(R.id.fab_four_label,R.drawable.last)
                .setLabelColor(getResources().getColor(R.color.strong_light_green))
                .setLabelBackgroundColor(Color.WHITE)
                .setLabel(getString(R.string.last_page))
                .setLabelClickable(false)
                .setFabBackgroundColor(Color.WHITE)
                .create();

        Paginate = findViewById(R.id.fab_paginate);
        Paginate.addActionItem(SpeedDialFour);
        Paginate.addActionItem(SpeedDialThree);
        Paginate.addActionItem(SpeedDialTwo);
        Paginate.addActionItem(SpeedDialOne);


        Paginate.setOnChangeListener(new SpeedDialView.OnChangeListener() {
            @Override
            public boolean onMainActionSelected() {
                return false;
            }

            @Override
            public void onToggleChanged(boolean isOpen) {

            }
        });

        Paginate.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem actionItem) {
                switch(actionItem.getId()) {
                    case R.id.fab_one_label:// first.
                        q_exists(1);
                        return false;

                    case R.id.fab_two_label:// next.
                        if (current_page.equals(total_pages)) {
                            Toast.makeText(getApplicationContext(), getString(R.string.on_last), Toast.LENGTH_LONG).show();
                        } else {
                            int_current_page  = int_current_page + 1;
                            if(int_current_page==Integer.parseInt(total_pages)) int_current_page=1;
                            q_exists(int_current_page);
                        }
                        return false;

                    case R.id.fab_three_label:// previous.
                        if(int_current_page==1) {
                            Toast.makeText(getApplicationContext(), getString(R.string.on_first), Toast.LENGTH_LONG).show();
                        }else
                        int_current_page  = int_current_page - 1;
                        if(int_current_page==0) int_current_page++;
                        q_exists(int_current_page);
                        return false;

                    case R.id.fab_four_label:// last.
                        q_exists(Integer.parseInt(total_pages));
                        return false;

                    default: return true;
                }
            }
        });
    }

    void q_exists(int val){
        if(param_q!=null)
            LoadArticles(val, param_q);
        else
            LoadArticles(val, null);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * This is the loader that gives way to all the queries to the aPI and all loading functionality.
     *
     * recives a loader and an id integer I use this for gettins textr into the loader
     * @param loader
     * @param value
     */
    void LoadArticles(int loader, @Nullable String value){
        String laodingText = getString(R.string.in_between) + current_page + "....";
        NotConnecting(laodingText,R.color.strong_light_orange);

        param_q = value;
        if(param_q!=null) {getSupportLoaderManager().restartLoader(loader,null,this);}
        getSupportLoaderManager().initLoader(loader,null,this).forceLoad();
    }


    /**
     * The loaders method
     *
     * @param id
     * @param args
     * @return
     */
    @NonNull
    public Loader<List<Articles>> onCreateLoader(int id, @Nullable Bundle args) {
        Uri newUri = Uri.parse(BASE_NEWS_URL);

        Uri.Builder uri = newUri.buildUpon();

        uri.appendQueryParameter("api-key", getString(R.string.api_key));
        uri.appendQueryParameter("page", String.valueOf(id));//pull from ui
//        for now
        if (param_q != null){
            //TODO: get this from prefers, the default values must be assigned and editable from prefs
            uri.appendQueryParameter("q", set_correctly(param_q));//pull from ui
            searchVal.setText(param_q);
        }else{
            uri.appendQueryParameter("q","celebrities OR politics");//pull from ui
            searchVal.setText(param_q);
        }
        uri.appendQueryParameter("show-fields","headline,thumbnail,body,publication,byline"); //pull from ui

        return new ArticlesUtils(this,uri.toString());
    }

    /**
     * This method is all about getting a list of srting seperated by commas and turing these values
     *  into searchable content
     *
     * @param param_q
     * @return
     */
    private String set_correctly(String param_q) {
        return param_q;
    }



    /**
     *
     * Loader has recieved some output
     *
     * @param loader
     * @param data
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onLoadFinished(@NonNull Loader<List<Articles>> loader, final List<Articles> data) {

        View.OnClickListener onItemClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
                Intent intHolder = new Intent(getApplicationContext(), SingleArticleActivity.class);
                int position = viewHolder.getAdapterPosition();
                intHolder.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intHolder.putExtra("pos",position);
                intHolder.putParcelableArrayListExtra("context", (ArrayList<Result>) data.get(0).getResults());
                startActivity(intHolder);
            }
        };

        if(data!=null && isConnected) {
            Connecting();
            ResultsAdapter ArticleResults = new ResultsAdapter(this,
                    data.get(0).getStatus(),
                    data.get(0).getResults());
            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
            // RecyclerView for Articles
            Pages.setLayoutManager(llm);
            Pages.setAdapter(ArticleResults);

            ArticleResults.msetOnItemClickLister(onItemClick);

            currentPage.setText(data.get(0).getCurrentPage().toString());
            Total.setText(data.get(0).getTotal().toString());
            current_page = data.get(0).getCurrentPage().toString();
            total_pages = data.get(0).getPageSize().toString();

            //set the integer values
            int_current_page = Integer.parseInt(current_page);

//        param_q = param_api;

            String PagesComparison = data.get(0).getCurrentPage().toString() + "/" + data.get(0).getPageSize().toString();
            totalpageSize.setText(PagesComparison);
        } else {
            TextHex.setTextColor(getResources().getColor(R.color.red));
            TextHex.setText(R.string.lost_connection);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Articles>> loader) {
        // TODO: no reset the ui
        NotConnecting(getString(R.string.reloading),R.color.blue);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                Intent Prefs = new Intent(this, SettingsActivity.class);
                Prefs.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Prefs);
                return true;

            default: return super.onOptionsItemSelected(item);
        }

    }
}
