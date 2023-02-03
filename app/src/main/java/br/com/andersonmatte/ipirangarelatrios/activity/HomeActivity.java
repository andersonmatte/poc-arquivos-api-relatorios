package br.com.andersonmatte.ipirangarelatrios.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import br.com.andersonmatte.ipirangarelatrios.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void geraRelatorio(View view) {
        Intent intent = new Intent(HomeActivity.this, GerarRelatorioActivity.class);
        startActivity(intent);
        finish();
    }

    public void salvaRelatorio(View view) {
        Intent intent = new Intent(HomeActivity.this, SalvarRelatorioActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(HomeActivity.this, SplashScreenActivity.class);
        startActivity(intent);
        finish();
    }

}