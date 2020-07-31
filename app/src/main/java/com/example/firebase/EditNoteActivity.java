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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditNoteActivity extends AppCompatActivity {
    private EditText editTitle, editDescription;
    private Button btnEdit;
    String noteTitle, noteDescription, noteId;
    private EditNoteActivity mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        mContext = EditNoteActivity.this;

        if (getIntent().getExtras() != null){
            noteTitle = getIntent().getStringExtra("noteTitle");
            noteDescription = getIntent().getStringExtra("noteDescription");
            noteId = getIntent().getStringExtra("noteId");

            editTitle.setText(noteTitle);
            editDescription.setText(noteDescription);
        }

        btnEdit = findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editNoteInFirebase();
            }
        });



    }

    private void editNoteInFirebase() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootReference = database.getReference();
        DatabaseReference noteReference = rootReference.child("Users").child(currentUser.getUid()).child("Notes");
        DatabaseReference particularNoteReference =noteReference.child(noteId);
        particularNoteReference.child("noteTitle").setValue(editTitle.getText().toString());
        particularNoteReference.child("noteDescription").setValue(editDescription.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(EditNoteActivity.this, "Note updated Successful", Toast.LENGTH_SHORT).show();
                    mContext.finish();
                }else{
                    Toast.makeText(EditNoteActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}