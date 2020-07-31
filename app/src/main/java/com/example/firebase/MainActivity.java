package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edEmail, edPassword;
    private Button btnLogin;
    private TextView txtSignUp;
    private ValidationInput validationInput1;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        validationInput1 = new ValidationInput(this);
        edEmail = findViewById(R.id.etEmail);
        edPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtSignUp = findViewById(R.id.txtSignUp);
        btnLogin.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    public void onClick(View view) {
        //int id = view.getId();
        switch (view.getId()) {
            case R.id.btnLogin:
                handleLoginBtn();
                break;
            case R.id.txtSignUp:
                handleSignUp();
                break;
        }
    }

    private void handleLoginBtn() {
        showProgressBar();
        String email = edEmail.getText().toString();
        String password = edPassword.getText().toString();
        if (validationInput1.emailValidation(email) && validationInput1.passwordValidation(password)) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        hideProgressBar();

                        Toast.makeText(MainActivity.this, "SignIn Successfully", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Something went Wrong" + task.getException(), Toast.LENGTH_SHORT).show();
                        hideProgressBar();
                    }
                }
            });

        }
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        
    }

    private void handleSignUp() {
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
}