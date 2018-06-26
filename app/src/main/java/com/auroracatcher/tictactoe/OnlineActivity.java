package com.auroracatcher.tictactoe;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class OnlineActivity extends AppCompatActivity {

    EditText userEmail;
    EditText friendEmail;
    Button loginButton;
    Button inviteButton;
    Button acceptButton;

    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;
    private String myEmail;
    private String myId;

    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);
        userEmail = (EditText) findViewById(R.id.userEmail);
        friendEmail = (EditText) findViewById(R.id.friendEmail);
        loginButton = (Button) findViewById(R.id.loginButton);
        inviteButton = (Button) findViewById(R.id.inviteButton);
        acceptButton = (Button) findViewById(R.id.acceptButton);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void signIn(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    public static final String TAG = "sign in / sign up";
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                myEmail = user.getEmail();
                                myId = user.getUid();
                                loginButton.setEnabled(false);
                                userEmail.setText(myEmail);

                                // because character @ wont be accepted
                                myRef.child("Users").child(getUserBeforeAt(myEmail)).child("Request").setValue(user.getUid());

                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private String getUserBeforeAt(String email) {
        String[] splits = email.split("@");
        return splits[0];
    }

    public void loginClick(View view) {
        Log.d("login", userEmail.getText().toString());
        signIn(userEmail.getText().toString(), "whatishardcode");
    }

    public void inviteClick(View view) {
        Log.d("invite", friendEmail.getText().toString());
        myRef.child("Users").child(getUserBeforeAt(friendEmail.getText().toString())).child("Request").push().setValue(myEmail);
    }

    public void incomingRequestListener() {
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                try {
                    HashMap<String, Object> td = (HashMap<String, Object>) dataSnapshot.getValue();
                    if (td != null) {
                        String value;
                        for (String key : td.keySet()) {
                            value = (String) td.get(key);
                            friendEmail.setText(value);
                            inviteButton.setBackgroundColor(Color.GREEN);
                            myRef.child("Users").child(getUserBeforeAt(myEmail)).child("Request").setValue(myId);
                            break; // TODO: why break?
                        }
                    }
                } catch (Exception ex) {

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("request", "Failed to read value.", error.toException());
            }
        });
    }

    public void acceptClick(View view) {
        Log.d("accept", friendEmail.getText().toString());
    }

    public void btnClick(View view) {
    }

    public void restart(View view) {
    }
}
