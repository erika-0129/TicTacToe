package com.example.project1;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class twoPlayer extends AppCompatActivity {
    private static final String TAG = "twoPlayer";
    int playerOneScore = 0, playerTwoScore = 0;
    ImageButton topLeftButton, topMiddleButton, topRightButton, middleLeftButton, middleMiddleButton,
            middleRightButton, bottomLeftButton, bottomMiddleButton, bottomRightButton;
    Button resetButton, homeButton;
    private boolean gameActive = true;              // Checks if the game is over or not
    private boolean playerTurn = true;              // Checks if a player is playing
    TextView scoreTextView;
    private char[] board;                           // Set up of the board as an array


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_two_player);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        scoreTextView = findViewById(R.id.scoreTextView);

        topLeftButton = findViewById(R.id.topLeftButton);
        topMiddleButton = findViewById(R.id.topMiddleButton);
        topRightButton = findViewById(R.id.topRightButton);
        middleLeftButton = findViewById(R.id.middleLeftButton);
        middleMiddleButton = findViewById(R.id.middleMiddleButton);
        middleRightButton = findViewById(R.id.middleRightButton);
        bottomLeftButton = findViewById(R.id.bottomLeftButton);
        bottomMiddleButton = findViewById(R.id.bottomMiddleButton);
        bottomRightButton = findViewById(R.id.bottomRightButton);
        resetButton = findViewById(R.id.resetButton);
        homeButton = findViewById(R.id.homeButton);


        // Set up of the board
        clearBoard();

        setupButtonListeners();
        resetButton(resetButton);
        homeButton(homeButton);
    }

    // Setting up player move for BOTH players
    private void playerMove(int position) {
        if (board[position] == ' ' && gameActive) {
            if (playerTurn) {
                board[position] = 'X';          // Sets up player 1 as X
            } else {
                board[position] = 'O';          // Sets up player 2 as O
            }
            updateBoard();
            if (checkWin()) {
                if (playerTurn) {
                    playerOneScore++;
                } else {
                    playerTwoScore++;
                }
                setScoreTextView(playerOneScore, playerTwoScore);
                gameOver();
            } else if (isBoardFull()) {
                Toast toast = Toast.makeText(getApplicationContext(), "TIE!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.END, 0,0);
                toast.show();
                gameActive = false;
                Log.i(TAG, "It's a tie!");
            } else {
                playerTurn = !playerTurn; // Switch turn
            }
        }
    }

    // Update the UI based on the board array
    private void updateBoard() {
        topLeftButton.setImageResource(board[0] == 'X' ? R.drawable.file_x : board[0] == 'O' ? R.drawable.file_o : R.drawable.fsu2);
        topMiddleButton.setImageResource(board[1] == 'X' ? R.drawable.file_x : board[1] == 'O' ? R.drawable.file_o : R.drawable.fsu2);
        topRightButton.setImageResource(board[2] == 'X' ? R.drawable.file_x : board[2] == 'O' ? R.drawable.file_o : R.drawable.fsu2);
        middleLeftButton.setImageResource(board[3] == 'X' ? R.drawable.file_x : board[3] == 'O' ? R.drawable.file_o : R.drawable.fsu2);
        middleMiddleButton.setImageResource(board[4] == 'X' ? R.drawable.file_x : board[4] == 'O' ? R.drawable.file_o : R.drawable.fsu2);
        middleRightButton.setImageResource(board[5] == 'X' ? R.drawable.file_x : board[5] == 'O' ? R.drawable.file_o : R.drawable.fsu2);
        bottomLeftButton.setImageResource(board[6] == 'X' ? R.drawable.file_x : board[6] == 'O' ? R.drawable.file_o : R.drawable.fsu2);
        bottomMiddleButton.setImageResource(board[7] == 'X' ? R.drawable.file_x : board[7] == 'O' ? R.drawable.file_o : R.drawable.fsu2);
        bottomRightButton.setImageResource(board[8] == 'X' ? R.drawable.file_x : board[8] == 'O' ? R.drawable.file_o : R.drawable.fsu2);
    }

    private boolean checkWin() {
        // Check rows, columns, and diagonals for a win
        int[][] winPositions = {
                {0, 1, 2}, // Top row
                {3, 4, 5}, // Middle row
                {6, 7, 8}, // Bottom row
                {0, 3, 6}, // Left column
                {1, 4, 7}, // Middle column
                {2, 5, 8}, // Right column
                {0, 4, 8}, // Top-left to bottom-right diagonal
                {2, 4, 6}  // Top-right to bottom-left diagonal
        };

        // Check if any of the winning combinations are met
        for (int[] winPosition : winPositions) {
            if (board[winPosition[0]] != ' ' &&
                    board[winPosition[0]] == board[winPosition[1]] &&
                    board[winPosition[1]] == board[winPosition[2]]) {
                return true;
            }
        }
        return false;
    }

    // Setting the score on the screen
    private void setScoreTextView(int userScore, int compScore){
        scoreTextView.setText(String.valueOf(userScore) + " : " + String.valueOf(compScore));
    }

    //Checks if the board has any entries left
    private boolean isBoardFull() {
        for (char c : board) {
            if (c == ' ') return false;
        }
        return true;
    }

    // Ends the game when player reached 2 out of 3
    private void gameOver() {
        // we make the game "inactive" so it does not continue working
        gameActive = false;
        Log.i(TAG, "Game Over");

        // Check who won and play the appropriate sound
        if (checkWin()) {
            enableButtons(false);
            if (playerTurn) {
                Toast toast = Toast.makeText(getApplicationContext(), "Player One is the Winner!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.END, 0,0);
                toast.show();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Player Two is the Winner!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.END, 0,0);
                toast.show();
            }
        }
    }

    // Button to go back home
    public void homeButton(Button homeButton) {
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(twoPlayer.this, MainActivity.class);
            startActivity(intent);
        });
    }

    // Setting the reset button
    public void resetButton(Button resetButton) {
        resetButton.setOnClickListener(v -> {
            // Clear board to restart the game
            clearBoard();
            updateBoard();

            // Reset the game state
            gameActive = true;
            playerTurn = true;

            // Re-enable player buttons
            enableButtons(true);
        });
    }

    private void clearBoard(){
        board = new char[9];
        for (int i = 0; i < 9; i++){
            board[i] = ' ';
        }
    }
    // Since there are several buttons to choose from, it was easier to separate them
    private void enableButtons(boolean enable) {
        topLeftButton.setEnabled(enable);
        topMiddleButton.setEnabled(enable);
        topRightButton.setEnabled(enable);
        middleLeftButton.setEnabled(enable);
        middleMiddleButton.setEnabled(enable);
        middleRightButton.setEnabled(enable);
        bottomLeftButton.setEnabled(enable);
        bottomMiddleButton.setEnabled(enable);
        bottomRightButton.setEnabled(enable);
    }

    // Button listeners were more neat outside of onCreate
    private void setupButtonListeners() {
        topLeftButton.setOnClickListener(v -> playerMove(0));
        topMiddleButton.setOnClickListener(v -> playerMove(1));
        topRightButton.setOnClickListener(v -> playerMove(2));
        middleLeftButton.setOnClickListener(v -> playerMove(3));
        middleMiddleButton.setOnClickListener(v -> playerMove(4));
        middleRightButton.setOnClickListener(v -> playerMove(5));
        bottomLeftButton.setOnClickListener(v -> playerMove(6));
        bottomMiddleButton.setOnClickListener(v -> playerMove(7));
        bottomRightButton.setOnClickListener(v -> playerMove(8));
    }

}

