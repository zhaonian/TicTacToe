package com.auroracatcher.tictactoe;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int activePlayer = 1;
    private List<Integer> player1Moves = new ArrayList<>();
    private List<Integer> player2Moves = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        if (activePlayer == 1) {
            btnSelected.setBackgroundColor(Color.RED);
            player1Moves.add(cellId);
            activePlayer = 2;
            AImove();
        } else {
            btnSelected.setBackgroundColor(Color.GREEN);
            player2Moves.add(cellId);
            activePlayer = 1;
        }
        btnSelected.setEnabled(false);
        checkWinner();
    }

    private void checkWinner() {
        int winner = -1;

        if (player1Moves.contains(1) && player1Moves.contains(2) && player1Moves.contains(3))
            winner = 1;
        if (player2Moves.contains(1) && player2Moves.contains(2) && player2Moves.contains(3))
            winner = 2;

        if (player1Moves.contains(4) && player1Moves.contains(5) && player1Moves.contains(6))
            winner = 1;
        if (player2Moves.contains(4) && player2Moves.contains(5) && player2Moves.contains(6))
            winner = 2;

        if (player1Moves.contains(7) && player1Moves.contains(8) && player1Moves.contains(9))
            winner = 1;
        if (player2Moves.contains(7) && player2Moves.contains(8) && player2Moves.contains(9))
            winner = 2;

        if (player1Moves.contains(1) && player1Moves.contains(5) && player1Moves.contains(9))
            winner = 1;
        if (player2Moves.contains(1) && player2Moves.contains(5) && player2Moves.contains(9))
            winner = 2;

        if (player1Moves.contains(3) && player1Moves.contains(5) && player1Moves.contains(7))
            winner = 1;
        if (player2Moves.contains(3) && player2Moves.contains(5) && player2Moves.contains(7))
            winner = 2;

        if (winner != -1) {
            if (winner == 1) {
                Toast.makeText(this, "Player 1 wins!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Player 2 wins!", Toast.LENGTH_LONG).show();
            }
        }
    }

    // random AI
    private void AImove() {
        List<Integer> emptyCells = new ArrayList<>();
        for (int cellId = 1; cellId < 10; cellId++) {
            if (!(player1Moves.contains(cellId) || player2Moves.contains(cellId))) {
                emptyCells.add(cellId);
            }
        }
        Random random = new Random();
        int randomIndex = random.nextInt(emptyCells.size());
        int randomCell = emptyCells.get(randomIndex);
        Button btnSelected = null;
        switch (randomCell) {
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
        playGame(randomCell, btnSelected);
    }

    //TODO: make a good AI with minimax algorithm
}
