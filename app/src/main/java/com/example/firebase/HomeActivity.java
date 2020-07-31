package com.example.firebase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private TextView welcomeMessageTV;
    private Button createNoteBtn;
    private RecyclerView recyclerView;

    ArrayList<Note> noteArrayList = new ArrayList<>();

    NoteAdapter adapter;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mContext =this;
        //get the name of the user in the welcome message

        welcomeMessageTV = findViewById(R.id.welcome_tv);
        createNoteBtn = findViewById(R.id.btnCreateNote);

        //get the name from Firebase

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference rootReference = firebaseDatabase.getReference(); //app root in firebase database.

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference nameReference = rootReference.child("Users").child(currentUser.getUid()).child("name");


        nameReference.addListenerForSingleValueEvent(new ValueEventListener() { //this will get triggered at least once.
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                welcomeMessageTV.setText("Welcome, "+dataSnapshot.getValue().toString()+"!");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        createNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateNoteActivity();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);

        //TODO : create and then set adapter after getting the data.



        //get the notes from Firebase
        readNotesFromFirebase();


    }

    private void readNotesFromFirebase() {

        //read the notes in firebase database

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference notesReference = firebaseDatabase.getReference().child("Users").child(currentUser.getUid()).child("Notes");

        notesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //for this example, this datasnapshot will contain. datasnapshot : {NoteID1 : {noteTitle :"some title", noteContent:"some content"}, NoteID2 :{noteTitle :"some title", noteContent:"some content"}}

                noteArrayList.clear();
                Note note;

                for(DataSnapshot noteSnapshot : dataSnapshot.getChildren())
                {
                    note = noteSnapshot.getValue(Note.class);
                    note.setNoteId(noteSnapshot.getKey());




//                    Toast.makeText(HomeActivity.this, "note : title : "+note.getNoteTitle() + " content "+note.getNoteDescription() + " note key/ID "+note.getNoteId(), Toast.LENGTH_SHORT).show();
//                     Log.i("mynote", "note : title : "+note.getNoteTitle() + " content "+note.getNoteDescription() + " note key/ID "+note.getNoteId());

                    //add note the arraylist of Notes.
                    noteArrayList.add(note);

                }

                printNoteList(noteArrayList);


                //TODO : SetUp Layout.

                adapter = new NoteAdapter(mContext, noteArrayList);
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, "Some error occurred.", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void printNoteList(ArrayList<Note> noteArrayList) {
        for(Note note : noteArrayList)
        {
            Log.i("mynote ", " title "+note.getNoteTitle()+" content "+note.getNoteDescription());
        }
    }


    private void openCreateNoteActivity() {

        //TODO : Open CreateNoteActivity here.
        Intent createNoteIntent = new Intent(HomeActivity.this,CreateNoteActivity.class);
        startActivity(createNoteIntent);

    }


    public void deleteFromFirebase(String noteId) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference noteReference = database.getReference().child("Users").child(currentUser.getUid()).child("Notes");
        DatabaseReference particularReference = noteReference.child(noteId);

        particularReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(mContext, "Successfully deleted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mContext, "Something went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.menuManageAccount:
                Intent userAccount = new Intent(HomeActivity.this, UserActivity.class);
                startActivity(userAccount);
                break;

            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();
                this.finish();
                Intent logoutIntent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(logoutIntent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}

