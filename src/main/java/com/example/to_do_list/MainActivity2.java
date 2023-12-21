package com.example.to_do_list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


public class MainActivity2 extends AppCompatActivity {

    private TextView detailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        detailTextView = findViewById(R.id.detailTextView);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("selectedItem")) {
            String selectedItem = intent.getStringExtra("selectedItem");
            detailTextView.setText(selectedItem);
        }
    }
}