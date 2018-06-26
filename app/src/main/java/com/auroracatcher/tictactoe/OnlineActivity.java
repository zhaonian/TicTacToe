package com.auroracatcher.tictactoe;

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

public class OnlineActivity extends AppCompatActivity {

    EditText userEmail;
    EditText friendEmail;
    Button loginButton;

    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;
    private String myEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);
        userEmail = (EditText) findViewById(R.id.userEmail);
        friendEmail = (EditText) findViewById(R.id.friendEmail);
        loginButton = (Button) findViewById(R.id.loginButton);
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
                                loginButton.setEnabled(false);
                                userEmail.setText(myEmail);
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

    public void loginClick(View view) {
        Log.d("login", userEmail.getText().toString());
        signIn(userEmail.getText().toString(), "whatishardcode");
    }

    public void inviteClick(View view) {
        Log.d("invite", friendEmail.getText().toString());
    }

    public void acceptClick(View view) {
        Log.d("accept", friendEmail.getText().toString());
    }

    public void btnClick(View view) {
    }

    public void restart(View view) {
    }
}
