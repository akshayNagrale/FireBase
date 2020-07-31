package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdateEmail extends AppCompatActivity {
    private EditText txtOldEmail, txtNewEmail;
    private Button btnUpdateEmail;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    ValidationInput validationInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);
        txtOldEmail = findViewById(R.id.txtOldEmail);
        txtNewEmail = findViewById(R.id.txtNewEmail);
        btnUpdateEmail = findViewById(R.id.txtUpdateEmail);
        validationInput = new ValidationInput(this);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        btnUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmailBtn();

            }
        });
    }

    private void updateEmailBtn() {
        String oldEmail = txtOldEmail.getText().toString();
        String newEmail = txtNewEmail.getText().toString();
        if (validationInput.emailValidation(newEmail)) {
            if (oldEmail.equals(newEmail)) {

                Toast.makeText(this, "new email is not same as old email", Toast.LENGTH_SHORT).show();

            } else {
                user.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UpdateEmail.this, "Email Updated Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UpdateEmail.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }
    }
}