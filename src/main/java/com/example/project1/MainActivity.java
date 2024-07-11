package com.example.project1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button one_player_button, two_player_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        one_player_button = findViewById((R.id.one_player_button));
        two_player_button = findViewById((R.id.two_player_button));

        onePlayerButtonSelection(one_player_button);
        twoPlayerButtonSelection(two_player_button);

    }

    private void onePlayerButtonSelection(Button button){
        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, onePlayer.class);
            startActivity(intent);
        });
    }

    private void twoPlayerButtonSelection(Button button){
        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, twoPlayer.class);
            startActivity(intent);
        });
    }
}