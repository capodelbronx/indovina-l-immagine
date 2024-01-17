package com.example.indovinalimmagine;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CittaActivity extends AppCompatActivity {

    boolean[] fileEstrattiCitta = new boolean[46];
    private FrameLayout frameLayout;
    private String cittaCorretta;

    private int risposteCorrette;

    private int risposteErrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.citta);

        // Inizializza la RadioGroup
        frameLayout = findViewById(R.id.frame_scelte);

        String[] nomi = new String[7];

        cittaCorretta = sceltaImmagine(fileEstrattiCitta, cittaCorretta);
        sceltaNomi(nomi, 46, cittaCorretta);

        impostaImmagineCitta(cittaCorretta);

        aggiungiRadioButtons(nomi, cittaCorretta);
    }

    public void mostraMenuOpzioni(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.opzioni_menu_citta, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.cambia_gioco) {
                    Intent cambiaGiocoIntent = new Intent(CittaActivity.this, BandieraActivity.class);
                    startActivity(cambiaGiocoIntent);
                    return true;
                } else if (item.getItemId() == R.id.torna_al_menu) {
                    Intent tornaAlMenuIntent = new Intent(CittaActivity.this, MainActivity.class);
                    startActivity(tornaAlMenuIntent);
                    return true;
                } else {
                    return false;
                }
            }
        });

        popupMenu.show();
    }

    public String sceltaImmagine(boolean[] fileEstratti, String nomeCorretto) {

        try {
            InputStream inputStream = getAssets().open("citta.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            List<String> elencoFiles = new ArrayList<>();
            String nomeFile;
            while ((nomeFile = bufferedReader.readLine()) != null) {
                elencoFiles.add(nomeFile);
            }

            Random random = new Random();
            int index = random.nextInt(elencoFiles.size() - 1);
            nomeCorretto = elencoFiles.get(index);

            do {
                if (!fileEstratti[index]) {
                    fileEstratti[index] = true;
                    bufferedReader.close();

                    return nomeCorretto;
                }
            } while (true);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return nomeCorretto;
    }

    public void sceltaNomi(String[] nomi, int num, String nomeCorretto) {
        String[] temp = new String[300];
        int j = 0;

        try {
            InputStream inputStream = this.getAssets().open("citta.txt");
            BufferedReader fIN = new BufferedReader(new InputStreamReader(inputStream));

            String nome = fIN.readLine();
            int i = 0;
            while (nome != null) {
                temp[i] = nome;
                i++;
                nome = fIN.readLine();
            }

            fIN.close();

            String nomeCasuale;
            nomeCasuale = null;
            do {
                int indiceCasuale = (int) (Math.random() * num);
                if (!fileEstrattiCitta[indiceCasuale]) {
                    fileEstrattiCitta[indiceCasuale] = true;
                    nomeCasuale = temp[indiceCasuale];
                    if (!nomeCasuale.equals(nomeCorretto)) {
                        nomi[j] = nomeCasuale;
                        j++;
                    }
                }
            } while (nomeCasuale != null && j < nomi.length - 1);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void impostaImmagineCitta(String cittaCorretta) {
        try {
            AssetManager assetManager = getAssets();
            String imagePath = "citta/" + cittaCorretta + ".jpg";

            InputStream is = assetManager.open(imagePath);
            Bitmap bitmap = BitmapFactory.decodeStream(is);

            ImageView immagineCitta = findViewById(R.id.immagine_citta);

            // Imposta l'immagine nella ImageView
            immagineCitta.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void aggiungiRadioButtons(String[] nomi, String cittaCorretta) {
        // Rimuovi tutte le viste precedenti dal GridLayout
        frameLayout.removeAllViews();

        // Crea un elenco di Button
        ArrayList<Button> buttons = new ArrayList<>();

        // Aggiungi 6 Button casuali
        for (int i = 0; i < 6; i++) {
            String nome = nomi[i];
            Button button = new Button(this);
            button.setId(View.generateViewId());  // Assegna un ID univoco al Button
            button.setText(nome);
            button.setTextSize(15);
            button.setTextColor(getResources().getColor(android.R.color.black)); // Imposta il colore del testo
            button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.white))); // Imposta il colore di sfondo
            button.setLayoutParams(new LinearLayout.LayoutParams(1000, LinearLayout.LayoutParams.WRAP_CONTENT)); // Imposta larghezza e altezza
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    risposteErrate += 1;
                    TextView risposta = findViewById(R.id.contatore_errate);
                    risposta.setText("Risposte Errate: " + risposteErrate);
                    Intent intent = new Intent(CittaActivity.this, Wrong_answerActivity.class);
                    // Per passare il valore di cittaCorretta
                    intent.putExtra("cittaCorretta", cittaCorretta);
                    startActivity(intent);
                }
            });
            // Aggiungi Button all'elenco
            buttons.add(button);
        }

        // Aggiungi il Button corretto
        Button buttonCorretto = new Button(this);
        buttonCorretto.setId(View.generateViewId()); // Assegna un ID univoco al Button
        buttonCorretto.setText(cittaCorretta);
        buttonCorretto.setTextSize(15);
        buttonCorretto.setTextColor(getResources().getColor(android.R.color.black));
        buttonCorretto.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.white)));
        buttonCorretto.setLayoutParams(new LinearLayout.LayoutParams(1000, LinearLayout.LayoutParams.WRAP_CONTENT));
        buttonCorretto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                risposteCorrette += 1;
                TextView risposta = findViewById(R.id.contatore_corrette);
                risposta.setText("Risposte Corrette: " + risposteCorrette);
                Intent intent = new Intent(CittaActivity.this, Correct_answerActivity.class);
                // Per passare il valore di cittaCorretta
                intent.putExtra("cittaCorretta", cittaCorretta);
                startActivity(intent);
            }
        });

        // Aggiungi all'elenco
        buttons.add(buttonCorretto);

        // Mescola l'elenco di Button
        Collections.shuffle(buttons);

        // Crea un nuovo RadioGroup (anche se stiamo usando i Button)
        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(RadioGroup.VERTICAL);

        // Aggiungi i Button al RadioGroup
        for (int i = 0; i < buttons.size(); i++) {
            Button button = buttons.get(i);

            // Aggiungi il Button al RadioGroup
            radioGroup.addView(button);
        }

        // Aggiungi il RadioGroup al GridLayout
        frameLayout.addView(radioGroup);
    }

}
