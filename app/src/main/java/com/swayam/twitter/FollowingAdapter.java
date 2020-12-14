package com.swayam.twitter;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.nikartm.button.FitButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.Holder> {
    private static final String TAG = "TAG";
    private ArrayList list;
    private List followedUsers ;
    private Context mContext;
    private ProgressDialog dialog;

    public FollowingAdapter(Context context){
        mContext = context;
        refreshAdapter();

        dialog = new ProgressDialog(mContext);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait...");
    }

    public void refreshAdapter(){
        list = new ArrayList();
        followedUsers = null;

        ParseUser parseUser = ParseUser.getCurrentUser();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username",parseUser.getUsername());

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (objects != null){
                    Log.i(TAG, "done: "+objects.size());
                    ParseUser parseUser = objects.get(0);
                    List<String> users = parseUser.getList("followings");
                    if (users != null){
                        for (String user : users){
                            addUser(user);
                        }
                    }
                    followedUsers = users;
                }else {
                    Log.i(TAG, "done: "+null);
                }
                unFollowedUsers();
            }
        });

        notifyDataSetChanged();
    }

    private void unFollowedUsers(){
        ParseUser parseUser = ParseUser.getCurrentUser();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username",parseUser.getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (objects != null){
                    for (ParseUser user : objects){
                        addUser(user.getUsername());
                    }
                }else {
                    Log.i(TAG, "done: "+null);
                }
            }
        });
    }

    private void addUser(String user){
        if (followedUsers != null && followedUsers.contains(user))
            return;
        ParseUser parseUser = new ParseUser();
        parseUser.put("username",user);
        list.add(parseUser);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.following_item_view,parent,false);
        return new Holder(view);

    }

    private void onButtonClick(Holder holder){
        ParseUser parseUser = ParseUser.getCurrentUser();
        String username = holder.getUsername();
        if (holder.isFollowing){
            List list = parseUser.getList("followings");
            list.remove(username);
            parseUser.put("followings",list);
        }else {
            parseUser.add("followings",username);
        }

        dialog.show();

        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                dialog.dismiss();
                if (e == null){
                    holder.setFollowing(!holder.isFollowing);
                }else {
                    showUnSuccessMessage();
                }
            }
        });
    }

    private void showUnSuccessMessage(){
        new MaterialAlertDialogBuilder(mContext)
                .setTitle("Error")
                .setMessage("Something went wrong")
                .create()
                .show();
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        ParseUser parseUser = (ParseUser)list.get(position);
        String user = parseUser.getUsername();

        if (followedUsers != null && followedUsers.contains(user)){
            holder.setFollowing(true);
        }

        holder.fitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick(holder);
            }
        });
        holder.setUsername(user);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        private TextView username;
        private String user;
        private FitButton fitButton;
        private boolean isFollowing;

        public Holder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            fitButton = itemView.findViewById(R.id.fitButton);
        }

        public String getUsername(){
            return user;
        }

        public void setUsername(String username) {
            this.user = username;
            this.username.setText(user);
        }

        public void setFollowing(boolean following) {
            isFollowing = following;
            if (isFollowing){
                fitButton.setText("unfollow");
            }else {
                fitButton.setText("follow");
            }
        }
    }
}
