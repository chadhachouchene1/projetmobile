package com.example.biblio;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnLivres  = findViewById(R.id.btn_livres);
        Button btnMembres = findViewById(R.id.btn_membres);

        btnLivres.setOnClickListener(v ->
                startActivity(new Intent(this, LivresActivity.class)));

        btnMembres.setOnClickListener(v ->
                startActivity(new Intent(this, MembresActivity.class)));
    }
}