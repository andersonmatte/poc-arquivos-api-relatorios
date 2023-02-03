package br.com.andersonmatte.ipirangarelatrios.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import br.com.andersonmatte.ipirangarelatrios.R;
import es.dmoral.toasty.Toasty;

public class GerarRelatorioActivity extends AppCompatActivity {

    EditText date;
    TextView resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerar_relatorio);
        resultado = (TextView) findViewById(R.id.resultado);

        date = (EditText) findViewById(R.id.date);

        TextWatcher tw = new TextWatcher() {

            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon - 1);
                        year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%02d", day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    date.setText(current);
                    date.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        };
        date.addTextChangedListener(tw);
    }

    // Busca no diretório /data/user/0/br.com.andersonmatte.ipirangarelatrios/files/data.json
    // Exemplo: /data/user/0/br.com.andersonmatte.ipirangarelatrios/files/15012023.json
    public void geraRelatorioPorDia(View view) {

        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        String retorno = "";

        try {
            InputStream inputStream = this.openFileInput(date.getText().toString().replace("/", "") + ".json");
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                retorno = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Toasty.error(this, "Relatório não encontrado na data : " + date.getText().toString(), Toast.LENGTH_LONG, true).show();
            Log.e("RELATORIO:" + date, "Erro ao recuperar o relatório do dia " + " File not found " + e);
        } catch (IOException e) {
            Toasty.error(this, "Relatório não encontrado na data : " + date.getText().toString(), Toast.LENGTH_LONG, true).show();
            Log.e("RELATORIO:" + date, "Erro ao recuperar o relatório do dia " + " Can not read file " + e);
        }

        this.resultado.setText(retorno);
        this.resultado.setVisibility(View.VISIBLE);
        Toasty.success(this, "Relatório Gerado com sucesso para a data : " + date.getText().toString(), Toast.LENGTH_LONG, true).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(GerarRelatorioActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}