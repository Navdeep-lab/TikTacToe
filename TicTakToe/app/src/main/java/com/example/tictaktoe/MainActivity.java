package com.example.tictaktoe;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Tracks the state of the game
    int activePlayer = 1; // 1 for Player 1 (X), 2 for Player 2 (O)
    int[] gameState = {0, 0, 0, 0, 0, 0, 0, 0, 0}; // 0: Empty, 1: X, 2: O
    boolean gameActive = true;
    int[][] winningPositions = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Rows
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Columns
            {0, 4, 8}, {2, 4, 6}              // Diagonals
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView winnerTextView = findViewById(R.id.winnerTextView);
        final Button resetButton = findViewById(R.id.resetButton);
        final GridLayout gridLayout = findViewById(R.id.gridLayout);

        // Setup onClickListeners for the buttons
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            final Button button = (Button) gridLayout.getChildAt(i);
            final int finalI = i;

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (gameState[finalI] == 0 && gameActive) {
                        // Update the button text (X or O) based on active player
                        if (activePlayer == 1) {
                            button.setText("X");
                            gameState[finalI] = 1;
                            activePlayer = 2; // Switch to Player 2
                        } else {
                            button.setText("O");
                            gameState[finalI] = 2;
                            activePlayer = 1; // Switch to Player 1
                        }

                        // Check if there is a winner
                        checkWinner(winnerTextView, gridLayout);
                    }
                }
            });
        }

        // Reset the game
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame(winnerTextView, gridLayout);
            }
        });
    }

    private void checkWinner(TextView winnerTextView, GridLayout gridLayout) {
        for (int[] winningPosition : winningPositions) {
            if (gameState[winningPosition[0]] != 0 &&
                    gameState[winningPosition[0]] == gameState[winningPosition[1]] &&
                    gameState[winningPosition[1]] == gameState[winningPosition[2]]) {

                // A player has won
                gameActive = false;
                String winner = "";
                if (gameState[winningPosition[0]] == 1) {
                    winner = "Player 1 (X)";
                } else {
                    winner = "Player 2 (O)";
                }

                // Display the winner and disable buttons
                winnerTextView.setText(winner + " has won!");
                winnerTextView.setVisibility(View.VISIBLE);
                disableButtons(gridLayout);
            }
        }

        // Check for a draw (all buttons filled and no winner)
        boolean draw = true;
        for (int state : gameState) {
            if (state == 0) {
                draw = false;
                break;
            }
        }

        if (draw && gameActive) {
            gameActive = false;
            winnerTextView.setText("It's a draw!");
            winnerTextView.setVisibility(View.VISIBLE);
        }
    }

    private void disableButtons(GridLayout gridLayout) {
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            Button button = (Button) gridLayout.getChildAt(i);
            button.setEnabled(false); // Disable all buttons
        }
    }

    private void resetGame(TextView winnerTextView, GridLayout gridLayout) {
        // Reset game state
        activePlayer = 1;
        gameActive = true;
        for (int i = 0; i < gameState.length; i++) {
            gameState[i] = 0;
        }

        // Reset the buttons in the grid
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            Button button = (Button) gridLayout.getChildAt(i);
            button.setText(""); // Clear button text
            button.setEnabled(true); // Enable buttons
        }

        // Hide the winner text
        winnerTextView.setVisibility(View.GONE);
    }
}
