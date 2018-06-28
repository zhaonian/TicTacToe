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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

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
    private String gameSessionId;
    private int activePlayer = 1; // 1 - first, 2 - second
    private List<Integer> player1Moves = new ArrayList<>();
    private List<Integer> player2Moves = new ArrayList<>();


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
                                incomingRequestListener();
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
        startGame(getUserBeforeAt(friendEmail.getText().toString()) + ":" + getUserBeforeAt(myEmail));
    }

    public void incomingRequestListener() {
        // Read from the database
        myRef.child("Users").child(getUserBeforeAt(myEmail)).child("Request").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                try {
                    HashMap<String, Object> td = (HashMap<String, Object>) dataSnapshot.getValue();
                    if (td != null) {
                        String value;
                        for (String key : td.keySet()) {
                            Log.d("ha", td.get(key).toString());
                            value = td.get(key).toString();
                            friendEmail.setText(value);
                            friendEmail.setBackgroundColor(Color.GREEN);
                            myRef.child("Users").child(getUserBeforeAt(myEmail)).child("Request").setValue(myId);
                            break; // TODO: why break?
                        }
                    }
                } catch (Exception ex) {
                    Log.d("exc", ex.getMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("request", "Failed to read value.", error.toException());
            }
        });
    }

    public void startGame(String gameId) {
        gameSessionId = gameId;
        myRef.child("Playing").child(gameId).removeValue();

        myRef.child("Playing").child(getUserBeforeAt(gameId)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                try {
                    player1Moves.clear();
                    player2Moves.clear();
                    activePlayer = 2;
                    HashMap<String, Object> td = (HashMap<String, Object>) dataSnapshot.getValue();
                    if (td != null) {
                        String value;
                        String firstPlayer = getUserBeforeAt(myEmail);
                        for (String key : td.keySet()) {
                            value = td.get(key).toString();
                            if (!value.equals(firstPlayer)) {
                                activePlayer = activePlayer == 2 ? 1 : 2;
                            }
                            firstPlayer = value;
                            String[] splitId = key.split(":");
                            Log.d("wo", splitId[1]);
                            friendMove(Integer.parseInt(splitId[1]));
                        }
                    }
                } catch (Exception ex) {
                    Log.d("exc", ex.getMessage());
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
        myRef.child("Users").child(getUserBeforeAt(friendEmail.getText().toString())).child("Request").push().setValue(myEmail);
        startGame(getUserBeforeAt(myEmail) + ":" + getUserBeforeAt(friendEmail.getText().toString()));
    }

    public void btnClick(View view) {
        if (gameSessionId == null) { // no game started yet
            return;
        }
        Button btnSelected = (Button) view;
        int cellId;
        switch (btnSelected.getId()) {
            case R.id.button10:
                cellId = 1;
                break;
            case R.id.button11:
                cellId = 2;
                break;
            case R.id.button12:
                cellId = 3;
                break;
            case R.id.button13:
                cellId = 4;
                break;
            case R.id.button14:
                cellId = 5;
                break;
            case R.id.button15:
                cellId = 6;
                break;
            case R.id.button16:
                cellId = 7;
                break;
            case R.id.button17:
                cellId = 8;
                break;
            case R.id.button18:
                cellId = 9;
                break;
            default:
                cellId = -1;
                break;
        }
        myRef.child("Playing").child(gameSessionId).child("CellID:" + cellId).setValue(getUserBeforeAt(myEmail));
    }

    private void playGame(int cellId, Button selectedButton) {
        if (activePlayer == 1) {
            selectedButton.setBackgroundColor(Color.BLUE);
            player1Moves.add(cellId);
        } else if (activePlayer == 2) {
            selectedButton.setBackgroundColor(Color.RED);
            player2Moves.add(cellId);
        }
        selectedButton.setEnabled(false);
        displayWinner(checkWinner());
    }

    // friend makes move
    private void friendMove(int cellId) {
        setBtnColor(cellId);
    }

    private int checkWinner() {
        int winner = -1;

        if (player1Moves.contains(1) && player1Moves.contains(2) && player1Moves.contains(3))
            winner = 0;
        if (player1Moves.contains(4) && player1Moves.contains(5) && player1Moves.contains(6))
            winner = 0;
        if (player1Moves.contains(7) && player1Moves.contains(8) && player1Moves.contains(9))
            winner = 0;
        if (player1Moves.contains(1) && player1Moves.contains(5) && player1Moves.contains(9))
            winner = 0;
        if (player1Moves.contains(3) && player1Moves.contains(5) && player1Moves.contains(7))
            winner = 0;
        if (player1Moves.contains(1) && player1Moves.contains(4) && player1Moves.contains(7))
            winner = 0;
        if (player1Moves.contains(2) && player1Moves.contains(5) && player1Moves.contains(8))
            winner = 0;
        if (player1Moves.contains(3) && player1Moves.contains(6) && player1Moves.contains(9))
            winner = 0;


        if (player2Moves.contains(1) && player2Moves.contains(2) && player2Moves.contains(3))
            winner = 1;
        if (player2Moves.contains(4) && player2Moves.contains(5) && player2Moves.contains(6))
            winner = 1;
        if (player2Moves.contains(7) && player2Moves.contains(8) && player2Moves.contains(9))
            winner = 1;
        if (player2Moves.contains(1) && player2Moves.contains(5) && player2Moves.contains(9))
            winner = 1;
        if (player2Moves.contains(3) && player2Moves.contains(5) && player2Moves.contains(7))
            winner = 1;
        if (player2Moves.contains(1) && player2Moves.contains(4) && player2Moves.contains(7))
            winner = 1;
        if (player2Moves.contains(2) && player2Moves.contains(5) && player2Moves.contains(8))
            winner = 1;
        if (player2Moves.contains(3) && player2Moves.contains(6) && player2Moves.contains(9))
            winner = 1;

        return winner;
    }

    private void displayWinner(int winner) {
        if (winner != -1) {
            if (winner == 1) {
                Toast.makeText(this, "1 win!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "2 win!", Toast.LENGTH_LONG).show();
            }
        } else if (player1Moves.size() + player2Moves.size() >= 9) {
            Toast.makeText(this, "Tie", Toast.LENGTH_LONG).show();
        }
    }

    private void setBtnColor(int cellIndex) {
        Button btnSelected;
        switch (cellIndex) {
            case 1:
                btnSelected = (Button) findViewById(R.id.button10);
                break;
            case 2:
                btnSelected = (Button) findViewById(R.id.button11);
                break;
            case 3:
                btnSelected = (Button) findViewById(R.id.button12);
                break;
            case 4:
                btnSelected = (Button) findViewById(R.id.button13);
                break;
            case 5:
                btnSelected = (Button) findViewById(R.id.button14);
                break;
            case 6:
                btnSelected = (Button) findViewById(R.id.button15);
                break;
            case 7:
                btnSelected = (Button) findViewById(R.id.button16);
                break;
            case 8:
                btnSelected = (Button) findViewById(R.id.button17);
                break;
            case 9:
                btnSelected = (Button) findViewById(R.id.button18);
                break;
            default:
                btnSelected = null;
                break;
        }
        playGame(cellIndex, btnSelected);
    }

    public void restart(View view) {
    }
}
