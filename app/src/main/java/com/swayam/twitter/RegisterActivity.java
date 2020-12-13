package com.swayam.twitter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameField,passwordField,emailField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        emailField = findViewById(R.id.email);

    }

    public void signUp(View view){
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        String email = emailField.getText().toString();

        if (username.equals("") || password.equals("") || email.equals("")){
            showErrorMessage("Username, Password and Email can not be empty");
            return;
        }

        ParseUser parseUser = new ParseUser();
        parseUser.put("email",email);
        parseUser.put("username",username);
        parseUser.put("password",password);

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("please wait...");
        dialog.show();

        parseUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                dialog.dismiss();
                if (e == null){
                    showSuccessMessage("user created");
                }else {
                    showErrorMessage(e.getMessage());
                }
            }
        });

    }

    public void showSuccessMessage(String message){
        new MaterialAlertDialogBuilder(this)
                .setCancelable(false)
                .setTitle("Successful")
                .setMessage(message)
                .setPositiveButton("Log In", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openLogin(null);
                        finish();
                    }
                })
                .create()
                .show();
    }

    public void showErrorMessage(String error){
        new MaterialAlertDialogBuilder(this)
                .setTitle("Error")
                .setMessage(error)
                .create()
                .show();
    }

    public void openLogin(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}