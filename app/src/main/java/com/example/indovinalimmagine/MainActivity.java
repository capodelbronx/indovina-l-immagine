package com.example.indovinalimmagine;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private boolean musicStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //il file audio
        mediaPlayer = MediaPlayer.create(this, R.raw.musica_epica);

        //imposta il loop per riprodurre continuamente la musica
        mediaPlayer.setLooping(true);

        startMusic();
    }

    private void startMusic() {
        if (!musicStarted) {
            mediaPlayer.start();
            musicStarted = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //quando l'attività viene distrutta evita che le risorse rimangano
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    public void onBandieraButtonClick(View view) {
        Intent intent = new Intent(this, BandieraActivity.class);
        startActivity(intent);
        }
    public void onCittaButtonClick(View view) {
            Intent intent = new Intent(this, CittaActivity.class);
            startActivity(intent);

    }

    public void onRulesButtonClick(View view){
        Intent intent = new Intent(this, RegoleActivity.class);
        startActivity(intent);
    }

}