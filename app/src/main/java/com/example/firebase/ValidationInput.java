package com.example.firebase;

import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

import java.util.regex.Pattern;

public class ValidationInput {
    private Context context;

    public ValidationInput(Context context) {
        this.context = context;
    }

    boolean emailValidation(String email) {
        if (email.length() == 0) {
            Toast.makeText(context, "Please enter email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Please enter Valid Email Address", Toast.LENGTH_SHORT).show();
            return false;
        } else {

            return true;
        }

    }

    boolean passwordValidation(String password) {
        if (password.length() == 0) {
            Toast.makeText(context, "Please enter password", Toast.LENGTH_SHORT).show();
            return false;

        } else if (password.length() < 8) {
            Toast.makeText(context, "Please enter password at least 8 character", Toast.LENGTH_SHORT).show();
            return false;
        } else {

            return true;
        }
    }
}
