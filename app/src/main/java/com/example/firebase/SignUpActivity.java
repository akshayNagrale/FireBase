package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private EditText etEmail, etPassword, etConfirmPassword, etName;
    private Button btnRegister;
    private ValidationInput validationInput;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference rootReference, nameReference;
    private String name;
    private String email;
    private String password;
    private String confPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        validationInput = new ValidationInput(this);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar2);
        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpBtn();
            }
        });
    }

    private void signUpBtn() {
         name = etName.getText().toString();
         email = etEmail.getText().toString();
         password = etPassword.getText().toString();
         confPassword = etConfirmPassword.getText().toString();

        if (!name.isEmpty()) {
            showProgressBar();
            if (validationInput.emailValidation(email) && validationInput.passwordValidation(password)) {


                if (password.equals(confPassword)) {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                hideProgressBar();
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(SignUpActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                saveNameInFireBase(user);
                            } else {
                                hideProgressBar();
                                Toast.makeText(SignUpActivity.this, "Something went Wrong" + task.getException(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                }
                else {
                    hideProgressBar();
                    Toast.makeText(this, "Password Doesn't match", Toast.LENGTH_SHORT).show();
                }
            }
        }else
        {
            hideProgressBar();
            Toast.makeText(this, "Name should not be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveNameInFireBase(FirebaseUser user) {
        database = FirebaseDatabase.getInstance();
        rootReference = database.getReference();
        nameReference = rootReference.child("Users").child(user.getUid()).child("name");
        nameReference.setValue(name);
    }


    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

}