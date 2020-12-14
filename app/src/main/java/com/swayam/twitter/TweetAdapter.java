package com.swayam.twitter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.Holder> {
    private ArrayList<ParseObject> list;
    private Context mContext;

    public TweetAdapter(Context context){
        mContext = context;
        list = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tweets");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects != null && objects.size()>0){
                    for (ParseObject object : objects){
                        addTweet(object);
                    }
                }
            }
        });

    }

    public void addTweet(ParseObject parseObject){
        list.add(0,parseObject);
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tweet_item_view,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        ParseObject parseObject = list.get(position);
        holder.username.setText(""+parseObject.get("username"));
        holder.tweet.setText(""+parseObject.get("tweet"));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        private TextView username;
        private TextView tweet;

        public Holder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            tweet = itemView.findViewById(R.id.tweetText);
        }
    }
}
