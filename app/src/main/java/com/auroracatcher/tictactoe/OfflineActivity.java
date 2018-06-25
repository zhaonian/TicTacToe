package com.auroracatcher.tictactoe;

import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OfflineActivity extends AppCompatActivity {

    private int playMode = 0;
    private int gameState = 1;
    private List<Integer> player1Moves = new ArrayList<>();
    private List<Integer> player2Moves = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
    }

    public void restart(View view) {
        player1Moves.clear();
        player2Moves.clear();
        gameState = 1;
        clearAllButtons();
    }

    private void clearAllButtons() {
        for (int i = 1; i <= 9; i++) {
            Button button = (Button) findViewById(getResources().getIdentifier("button" + i, "id",
                    this.getPackageName()));
            button.setBackgroundColor(Color.LTGRAY);
            button.setEnabled(true);
        }
    }

    public void btnClick(View view) {
        Button btnSelected = (Button) view;
        int cellId = -1;
        switch (btnSelected.getId()) {
            case R.id.button1:
                cellId = 1;
                break;
            case R.id.button2:
                cellId = 2;
                break;
            case R.id.button3:
                cellId = 3;
                break;
            case R.id.button4:
                cellId = 4;
                break;
            case R.id.button5:
                cellId = 5;
                break;
            case R.id.button6:
                cellId = 6;
                break;
            case R.id.button7:
                cellId = 7;
                break;
            case R.id.button8:
                cellId = 8;
                break;
            case R.id.button9:
                cellId = 9;
                break;
            default:
                cellId = -1;
                break;
        }
        playGame(cellId, btnSelected);
    }

    private void playGame(int cellId, Button btnSelected) {
        if (gameState == 1) {
            switch (playMode) {
                case 0: // weak AI
                    playWeakAI(cellId, btnSelected);
                    break;
                case 1: // strong AI
                    break;
                case 2: // online
                    break;
            }
        }
    }

    // start weak AI
    private void playWeakAI(int cellId, Button btnSelected) {
        if (gameState == 0) {
            return;
        }
        btnSelected.setBackgroundColor(Color.RED);
        player1Moves.add(cellId);
        Log.d("Player: ", player1Moves.toString());

        displayWinner(checkWinner());
        if (gameState == 0) {
            return;
        }
        randomAImove();
        btnSelected.setEnabled(false);
        displayWinner(checkWinner());
    }

    // get available cells to put new tiles
    private List<Integer> getAvailableCells() {
        // get the available cells
        List<Integer> availableCells = new ArrayList<>();
        for (int cellId = 1; cellId < 10; cellId++) {
            if (!(player1Moves.contains(cellId) || player2Moves.contains(cellId))) {
                availableCells.add(cellId);
            }
        }

        Log.d("available cells: ", availableCells.toString());

        return availableCells;
    }

    // random AI
    private void randomAImove() {
        // get available cells
        List<Integer> availableCells = getAvailableCells();
        // get the random position
        Random random = new Random();
        int randomIndex = random.nextInt(availableCells.size());
        int randomCell = availableCells.get(randomIndex);
        player2Moves.add(randomCell);
        Log.d("AI: ", player2Moves.toString());
        setAImoveColor(randomCell);
    }

    // AI puts the tile down on board
    private void setAImoveColor(int cellIndex) {
        Button btnSelected = null;
        switch (cellIndex) {
            case 1:
                btnSelected = (Button) findViewById(R.id.button1);
                break;
            case 2:
                btnSelected = (Button) findViewById(R.id.button2);
                break;
            case 3:
                btnSelected = (Button) findViewById(R.id.button3);
                break;
            case 4:
                btnSelected = (Button) findViewById(R.id.button4);
                break;
            case 5:
                btnSelected = (Button) findViewById(R.id.button5);
                break;
            case 6:
                btnSelected = (Button) findViewById(R.id.button6);
                break;
            case 7:
                btnSelected = (Button) findViewById(R.id.button7);
                break;
            case 8:
                btnSelected = (Button) findViewById(R.id.button8);
                break;
            case 9:
                btnSelected = (Button) findViewById(R.id.button9);
                break;
            default:
                btnSelected = null;
                break;
        }
        btnSelected.setBackgroundColor(Color.GREEN);
        btnSelected.setEnabled(false);
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
            if (winner == 0) {
                Toast.makeText(this, "You win!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "You lose!", Toast.LENGTH_LONG).show();
            }
            gameState = 0;
        } else if (player1Moves.size() + player2Moves.size() >= 9) {
            Toast.makeText(this, "Tie", Toast.LENGTH_LONG).show();
            gameState = 0;
        }
    }


    //TODO: make a good AI with minimax algorithm
}
