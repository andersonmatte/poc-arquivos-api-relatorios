package br.com.andersonmatte.ipirangarelatrios.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Calendar;

import br.com.andersonmatte.ipirangarelatrios.R;
import es.dmoral.toasty.Toasty;

public class SalvarRelatorioActivity extends AppCompatActivity {

    EditText date;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salvar_relatorio);

        date = (EditText) findViewById(R.id.dateSalvar);

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

    // Salva no diretório /data/user/0/br.com.andersonmatte.ipirangarelatrios/files/data.json
    // Exemplo: /data/user/0/br.com.andersonmatte.ipirangarelatrios/files/15012023.json
    public void salvarRelatorioDia(View view) {

        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        String nomeArquivo = date.getText().toString().replace("/", "") + ".json";
        // Busca Json default, só para popular mesmo.
        String data = buscarDadosRelatorioPorDia("01022023");
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.openFileOutput(nomeArquivo, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Toasty.error(this, "Erro ao salvar Relatório com a data : " + date.getText().toString(), Toast.LENGTH_LONG, true).show();
            Log.e("RELATORIO:" + date, "Erro ao slavar o Relatório do dia " + " " + date + e);
        }
        Toasty.success(this, "Relatório Salvo com sucesso para a data : " + date.getText().toString(), Toast.LENGTH_LONG, true).show();
    }

    public String buscarDadosRelatorioPorDia(String dia) {
        String json = null;
        try {
            InputStream is = this.getAssets().open(dia + ".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("RELATORIO:" + dia, "Erro ao recuperar o relatório do dia " + " " + dia + e);
            return "";
        }
        return json;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SalvarRelatorioActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}