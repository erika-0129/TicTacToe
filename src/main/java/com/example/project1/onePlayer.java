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
import java.util.concurrent.TimeUnit;

public class onePlayer extends AppCompatActivity {

    private static final String TAG = "onePlayer";
    private Handler handler = new Handler();
    int userScore = 0, compScore = 0;
    ImageButton topLeftButton, topMiddleButton, topRightButton, middleLeftButton, middleMiddleButton,
            middleRightButton, bottomLeftButton, bottomMiddleButton, bottomRightButton;
    Button resetButton, homeButton;
    Random random;
    private boolean gameActive = true;              // checks if the game is over or not
    TextView scoreTextView;
    MediaPlayer winnerSound, loserSound;            // Plays sound for the user if they win or lose
    private char[] board;                           // Set up of the board as an array
    private boolean playerTurn = true;              // Needed to determine if the payer is currently playing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_one_player);
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

        random = new Random();

        winnerSound = MediaPlayer.create(onePlayer.this, R.raw.successaudio);
        loserSound = MediaPlayer.create(onePlayer.this, R.raw.failaudio);

        // Set up of the board as an array
        clearBoard();

        setupButtonListeners();
        resetButton(resetButton);
        homeButton(homeButton);

    }

    // NOTE: Majority of the following code will be from my Rock Paper Spear project which uses some
    // of the same logic

    // Setting up player move
    private void playerMove(int position){
        if (board[position] == ' ' && gameActive) {
            board[position] = 'X';          // setting up player 1 (current user) as X
            updateBoard();
            if (checkWin()) {
                userScore++;                // checks if the user has three in a row, updates score and calls gameOver
                setScoreTextView(userScore, compScore);
                gameOver();
            } else if (isBoardFull()) {         // There is a tie in the game
                Toast toast = Toast.makeText(getApplicationContext(), "TIE!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.END, 0,0);
                toast.show();
                gameActive = false;
                Log.i(TAG, "Tie");
            } else {                    // If none of the above, continue playing. Computer turn
                playerTurn = false;
                computerMove();
            }
        }
    }

    // Logic to track when player 1 has two in a row to block their move
    private int blockingMove(){
        // Winning positions
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
        // Potential blocks
        for (int[] winPosition : winPositions) {
            int pos1 = winPosition[0];
            int pos2 = winPosition[1];
            int pos3 = winPosition[2];

            // Check if the player has two in a row and the third is empty
            if (board[pos1] == 'X' && board[pos2] == 'X' && board[pos3] == ' ') {
                return pos3;
            }
            if (board[pos1] == 'X' && board[pos2] == ' ' && board[pos3] == 'X') {
                return pos2;
            }
            if (board[pos1] == ' ' && board[pos2] == 'X' && board[pos3] == 'X') {
                return pos1;
            }
        }
        return -1; // No blocking move was found
    }

    private void computerMove() {
        if (!gameActive) return;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int position = blockingMove();          // Checks if there is a move to block
                if (position == -1) {                   // If no blocking, places O in random location
                    position = random.nextInt(9);
                    while (board[position] != ' ') {
                        position = random.nextInt(9);
                    }
                }

                board[position] = 'O';                  // Set up Computer as O Uses similar logic to playerMove
                updateBoard();
                if (checkWin()) {
                    compScore++;
                    setScoreTextView(userScore, compScore);
                    gameOver();
                } else if (isBoardFull()){
                    Toast toast = Toast.makeText(getApplicationContext(), "TIE!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.END, 0,0);
                    toast.show();
                    gameActive = false;
                    Log.i(TAG, "Tie");
                } else
                    playerTurn = true;          // Player's turn
            }
        }, 1000); // Delay of 1000 milliseconds, so computer doesn't play immediately after player
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
                enableButtons(false);
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

    // Ends the game when one player has three in a row
    private void gameOver() {
        // we make the game "inactive" so it does not continue working
        gameActive = false;
        Log.i(TAG, "Game Over");

        // Check who won and play the appropriate sound
        if (checkWin()) {
            // disables the buttons so they do not work when the game is over.
            enableButtons(false);
            if (playerTurn) {
                winnerSound.start();
                Toast toast = Toast.makeText(getApplicationContext(), "Player One is the winner!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.END, 0,0);
                toast.show();
            } else {
                loserSound.start();
                Toast toast = Toast.makeText(getApplicationContext(), "Computer wins!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.END, 0,0);
                toast.show();
            }
        }
    }

    // Button to go back home
    public void homeButton(Button homeButton) {
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(onePlayer.this, MainActivity.class);
            startActivity(intent);
        });
    }

    // Setting the reset button
    public void resetButton(Button resetButton) {
        resetButton.setOnClickListener(v -> {

            clearBoard();
            updateBoard();

            // we set the game as active again
            gameActive = true;
            playerTurn = true;

            // Re-Enable player buttons
            enableButtons(true);
        });
    }

    private void clearBoard(){
        board = new char[9];
        for (int i = 0; i < 9; i++){
            board[i] = ' ';
        }
    }

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
