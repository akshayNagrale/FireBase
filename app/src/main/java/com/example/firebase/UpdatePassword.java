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

public class UpdatePassword extends AppCompatActivity {
    private EditText txtPassword, txtUpdatePassword;
    private Button bntUpdatePassword;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ValidationInput validationInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        txtPassword = findViewById(R.id.txtPassword);
        txtUpdatePassword = findViewById(R.id.txtConfPassword);
        bntUpdatePassword = findViewById(R.id.btnUpdatePassword);
        validationInput = new ValidationInput(this);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        bntUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });
    }

    private void updatePassword() {
        String password = txtPassword.getText().toString();
        String confirmPassword = txtUpdatePassword.getText().toString();

        if (validationInput.passwordValidation(confirmPassword)) {

            if (password.equals(confirmPassword)) {

                user.updatePassword(confirmPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(UpdatePassword.this, "Password Updated Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UpdatePassword.this, "Something going wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else {
                Toast.makeText(this, "Password not match , please check the password", Toast.LENGTH_SHORT).show();
            }
        }
    }
}