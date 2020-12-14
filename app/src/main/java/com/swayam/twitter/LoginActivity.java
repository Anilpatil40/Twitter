package com.swayam.twitter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameField,passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ParseInstallation.getCurrentInstallation().saveInBackground();

        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
    }

    public void login(View view){
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        if (username.equals("") || password.equals("") ){
            showErrorMessage("Username and Password can not be empty");
            return;
        }

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("please wait...");
        dialog.show();

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                dialog.dismiss();
                if (e == null){
                    startActivity(new Intent(LoginActivity.this,UserActivity.class));
                    finish();
                }else {
                    showErrorMessage(e.getMessage());
                }
            }
        });
    }

    public void showErrorMessage(String error){
        new MaterialAlertDialogBuilder(this)
                .setTitle("Error")
                .setMessage(error)
                .create()
                .show();
    }

    public void openRegistration(View view) {
        startActivity(new Intent(this,RegisterActivity.class));
        finish();
    }
}