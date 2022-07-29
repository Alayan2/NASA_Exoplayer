package com.exoplayer.exoplayerapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyviewHolder>  {

    Context context;
    Collection movieList;

    public RecyclerAdapter(Context context, Collection movieList) {
        this.context = context;
        this.movieList = movieList;

    }

    public void setMovieList(Collection movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerAdapter.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_adapter,parent,false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.MyviewHolder holder, int position) {

        //Video Title
        holder.tvMovieName.setText(movieList.getCollection().getItems().get(position).getData().get(0).getTitle());
        System.out.println(movieList.getCollection().getItems().get(position).getData().get(0).getTitle());

        //Video Thumbnail Image
        Glide.with(context).load(movieList.getCollection().getItems().get(position).getLinks().get(0).getHref()).apply(RequestOptions.centerCropTransform()).into(holder.image);
        System.out.println(movieList.getCollection().getItems().get(position).getLinks().get(0).getHref());

        //Video Creation Date
        holder.dataCreated.setText("Date: " + movieList.getCollection().getItems().get(position).getData().get(0).getDateCreated().substring(0,10));

        //Video URL
        String videoURL = "https://images-assets.nasa.gov/video/" + movieList.getCollection().getItems().get(position).getData().get(0).getNasaId() + "/" + movieList.getCollection().getItems().get(position).getData().get(0).getNasaId() + "~orig.mp4";

        //Video Title
        String videoTitle = movieList.getCollection().getItems().get(position).getData().get(0).getTitle();

        //Video Description
        String videoDescription = movieList.getCollection().getItems().get(position).getData().get(0).getDescription();

        //Video keywords
        String keywords = movieList.getCollection().getItems().get(position).getData().get(0).getKeywords().get(0);

        System.out.println(videoURL);
        holder.tvMovieName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("button clicked");
                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtra("url", videoURL);
                Log.d("videourl", videoURL);
                intent.putExtra("desc", videoDescription);
                intent.putExtra("keywords", keywords);
                intent.putExtra("title", videoTitle);

                context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

    }

    @Override
    public int getItemCount() {
        if(movieList != null){
            return movieList.getCollection().getMetadata().getTotalHits();
        }
        return 0;

    }



    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView tvMovieName;
        ImageView image;
        TextView dataCreated;

        public MyviewHolder(View itemView) {
            super(itemView);
            tvMovieName = (TextView)itemView.findViewById(R.id.title);
            image = (ImageView)itemView.findViewById(R.id.image);
            dataCreated = (TextView)itemView.findViewById(R.id.videoDate);
        }
    }

}



