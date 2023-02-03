package br.com.andersonmatte.ipirangarelatrios.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import br.com.andersonmatte.ipirangarelatrios.R;

public class SplashScreenActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        imageView = (ImageView) findViewById(R.id.logoIpiranga);
        textView = (TextView) findViewById(R.id.appName);
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                mostrarHome();
            }
        }, 2500);
    }

    //Chama a home do APP.
    private void mostrarHome() {
        Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}