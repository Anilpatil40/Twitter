package com.swayam.twitter;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.nikartm.button.FitButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class TweetsFragment extends Fragment {
    private FitButton fitButton;
    private EditText tweetText;
    private RecyclerView recyclerView;
    private TweetAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tweets,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fitButton = getView().findViewById(R.id.fitButton);
        tweetText = getView().findViewById(R.id.tweetText);
        recyclerView = getView().findViewById(R.id.recyclerview);
        adapter = new TweetAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTweet();
            }
        });

    }

    private void sendTweet() {
        ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);
        dialog.setMessage("Please wait...");
        dialog.show();

        String tweet = tweetText.getText().toString();

        if (tweet.equals("")){
            showUnSuccessMessage("Please write something");
        }

        ParseObject object = new ParseObject("Tweets");
        object.put("username", ParseUser.getCurrentUser().getUsername());
        object.put("tweet",tweet);

        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                dialog.dismiss();
                if (e == null){
                    ParseObject parseObject = new ParseObject("Tweets");
                    parseObject.put("username",ParseUser.getCurrentUser().getUsername());
                    parseObject.put("tweet",tweet);
                    adapter.addTweet(parseObject);
                    FancyToast.makeText(getContext(),"sent",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                    tweetText.setText("");
                }else {
                    showUnSuccessMessage(e.getMessage());
                }
            }
        });

    }

    private void showUnSuccessMessage(String message){
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Error")
                .setMessage(message)
                .create()
                .show();
    }

}
