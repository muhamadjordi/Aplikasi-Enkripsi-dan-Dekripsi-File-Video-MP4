package com.jordi.tugasakhir_skripsi_mj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    private Button btn_fEnciphering, btn_fDeciphering;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_acitivity);

        btn_fEnciphering = findViewById(R.id.btn_mEnciphering);
        btn_fDeciphering = findViewById(R.id.btn_mDeciphering);

        //Menjalankan Activity Form Enciphering.
        btn_fEnciphering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fEncipher = new Intent(HomeActivity.this, FormEncipheringActivity.class);
                startActivity(fEncipher);
            }
        });

        //Menjalankan Activity Form Deciphering.
        btn_fDeciphering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fDecipher = new Intent(HomeActivity.this, FormDecipheringActivity.class);
                startActivity(fDecipher);
            }
        });
    }
}
