package com.example.news_app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.icu.text.DateFormat;
import android.os.Build;
import android.os.Parcelable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.news_app.Activities.SingleArticleActivity;
import com.example.news_app.Models.Fields;
import com.example.news_app.Models.Result;
import com.example.news_app.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultsViewHolder> {

    private final List<Result> resultList;
    private final String mResource;
    private final Context mContext;
    private View.OnClickListener mOnItemClickListener;

    public void msetOnItemClickLister (View.OnClickListener listener){
        mOnItemClickListener = listener;
    }

    public ResultsAdapter(@NonNull Context context, String resource, @NonNull List<Result> results) {
        super();
        mContext = context;
        mResource = resource;
        resultList = results;
    }

    public class ResultsViewHolder extends RecyclerView.ViewHolder {
        private TextView Type;
        private TextView SectionName;
        private TextView PillName;
        private TextView Time;
        private TextView Date;
        private ImageView Thumbnail;
        private ImageView Banner;
        private TextView Headline;
        private Fields fields;
        private TextView Summary;
        private ImageView State;
        private ImageView banner;
        private ProgressBar glider;
        private CardView cardView;

        public ResultsViewHolder(@NonNull View itemView) {
            super(itemView);

            View Results = itemView;

            cardView = Results.findViewById(R.id.card_view);
            Type = Results.findViewById(R.id.art_type);
            SectionName = Results.findViewById(R.id.art_section);
            PillName = Results.findViewById(R.id.art_pillname);
            Time = Results.findViewById(R.id.art_time);
            Date = Results.findViewById(R.id.art_date);
            Thumbnail = Results.findViewById(R.id.imgThumbnail);
            banner = Results.findViewById(R.id.imgBanner);
            Headline = Results.findViewById(R.id.art_headline);
            Summary = Results.findViewById(R.id.art_summary);
            State = Results.findViewById(R.id.art_stat);

            glider = Results.findViewById(R.id.thumbnail_progressBar);
            Results.setTag(this);
            Results.setOnClickListener(mOnItemClickListener);
        }
    }


    @NonNull
    @Override
    public ResultsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_card_view, parent, false);
        return new ResultsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ResultsViewHolder holder, final int position) {
            holder.Type.setText(resultList.get(position).getType());
            holder.SectionName.setText(resultList.get(position).getSectionName());
            holder.PillName.setText(resultList.get(position).getPillarName());

            switch(resultList.get(position).getIsHosted().toString()){
                case "true": holder.State.setImageDrawable(mContext.getResources().getDrawable(R.drawable.one_state));break;
                case "false": holder.State.setImageDrawable(mContext.getResources().getDrawable(R.drawable.three_state));break;
            }

            holder.Headline.setText(resultList.get(position).getWebTitle());
            holder.Summary.setText(Html.fromHtml(resultList.get(position).getFields().getBody().substring(0,100).concat("... ")));
        try {
            holder.Time.setText(FormatTime(resultList.get(position).getWebPublicationDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            holder.Date.setText(FormatDate(resultList.get(position).getWebPublicationDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Glide.with(mContext).load(resultList.get(position).getFields().getThumbnail())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.glider.setVisibility(View.VISIBLE);
                            Glide.with(mContext).load(mContext.getResources().getDrawable(R.drawable.banner_red)).into(holder.banner);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.glider.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(holder.Thumbnail);
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    private String FormatTime(String Date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",Locale.getDefault());
        Date d = new Date();
        try {
            d = sdf.parse(Date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assert d != null;
        return new SimpleDateFormat("HH:mm:ss aa", Locale.getDefault()).format(d);
    }

    private String FormatDate(String Date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",Locale.getDefault());
        Date d = new Date();
        try {
            d = sdf.parse(Date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assert d != null;
        return new SimpleDateFormat("LLL, dd yyyy", Locale.getDefault()).format(d);
    }
}

