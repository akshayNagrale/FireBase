package com.example.firebase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView welcomeTxt;
    private Button btnLogout, btnUpdatePassword, btnUpdateEmail;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Context context;
    private UserActivity userActivity;

    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        welcomeTxt = findViewById(R.id.welcomeMessage);
        btnLogout = findViewById(R.id.btnLogout);
        btnUpdatePassword = findViewById(R.id.btnUpdate);
        btnUpdateEmail = findViewById(R.id.btnUpdateEmail);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        context = UserActivity.this;



        welcomeTxt.setText("Hi," + user.getEmail() + "!");


        btnLogout.setOnClickListener(this);
        btnUpdatePassword.setOnClickListener(this);
        btnUpdateEmail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnLogout:
                showLogoutDialog();
                break;
            case R.id.btnUpdate:
                showUpdateBtn();
                break;
            case R.id.btnUpdateEmail:
                showUpdateEmailBtn();
                break;
        }
    }

    private void showUpdateEmailBtn() {
        Intent intent = new Intent(UserActivity.this, UpdateEmail.class);
        startActivity(intent);
    }

    private void showUpdateBtn() {
        Intent intent = new Intent(UserActivity.this, UpdatePassword.class);
        startActivity(intent);
    }

    private void showLogoutDialog() {
        builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.signOut();
                ((Activity) context).finish();

            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}