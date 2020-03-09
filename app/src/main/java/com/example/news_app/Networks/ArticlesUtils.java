package com.example.news_app.Networks;

import android.content.Context;
import android.content.Loader;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;


import com.example.news_app.Models.Articles;
import com.example.news_app.Models.Fields;
import com.example.news_app.Models.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import static java.sql.DriverManager.println;

public class ArticlesUtils extends AsyncTaskLoader<List<Articles>> {

    private String UrlVal;

    public ArticlesUtils(@NonNull Context context,String UrlVal) {
        super(context);
        this.UrlVal=UrlVal;
    }

    @Nullable
    public ArrayList<Articles> loadInBackground() {
        String response=null;
        ArrayList<Articles> eqs = new ArrayList<>();
        String BASE_URL = UrlVal;
        try {
            URL base_url = new URL(BASE_URL);
            HttpURLConnection connect = (HttpURLConnection) base_url.openConnection();
            connect.setRequestMethod("GET");
            InputStream input = new BufferedInputStream(connect.getInputStream());
            response = convertStreamToString(input);
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Articles News = new Articles();
        ArrayList ResponseResults = new ArrayList<Result>();
        if(response!=null){
            try {
                //-- if One results

                //-- if 2 or more results
                JSONObject Root = new JSONObject(response);
                JSONObject Roo = Root.getJSONObject("response");

                News.setStatus(Roo.getString("status"));
                News.setStartIndex(Roo.getInt("startIndex"));
                News.setCurrentPage(Roo.getInt("currentPage"));
                News.setUserTier(Roo.getString("userTier"));
                News.setOrderBy(Roo.getString("orderBy"));
                News.setPages(Roo.getInt("pages"));
                News.setTotal(Roo.getInt("total"));
                News.setPageSize(Roo.getInt("pageSize"));

                JSONArray results = Roo.getJSONArray("results");

                for (int i=0;i<10;i++){
                    println("hi");
                }
                for (int count= 0;count < results.length();count++){
                    JSONObject result = results.getJSONObject(count);
                    JSONObject field = result.getJSONObject("fields");

                    Result currentResult = new Result(
                    result.getString("id"),
                    result.getString("type"),
                    result.getString("sectionId"),
                    result.getString("sectionName"),
                    result.getString("webPublicationDate"),
                    result.getString("webTitle"),
                    result.getString("webUrl"),
                    result.getString("apiUrl"),
                    new Fields(field.getString("thumbnail"),
                               field.getString("body"),
                               field.getString("headline"),
                               field.getString("publication")),
                    result.getBoolean("isHosted"),
                    result.getString("pillarId"),
                    result.getString("pillarName")
                    );

                    ResponseResults.add(count,currentResult);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        News.setResults(ResponseResults);
        eqs.add(News);
        return eqs;
    }

    private static String convertStreamToString(InputStream is){
        BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line =reader.readLine())!=null){
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private static String convertIntToString(int date){
        return (String) DateFormat.format("LLL, dd yyyy",date);
    }

    private static String formatTime(int dateObject) {
        return (String) DateFormat.format("h:mm a",dateObject);
    }

}
