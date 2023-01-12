package w1t1.uebung.w2t3_uebung_primzahlen_arzu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity
{

    private final String DATAINAME = "primzahlen.txt";

    private EditText etEnwert;
    private Button btnBerechnen;
    private TextView lbl;
    private Integer startwert, endWert;
    private File datei;
//    ==============================================================================================
        private class MyOCL implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {

            String eingabe = etEnwert.getText().toString();

            try
            {
                endWert = Integer.valueOf(eingabe);
                if (endWert >= startwert)
                {
                    lbl.setText("rechne ...");
                    rechnen();
                    etEnwert.setText("");
                }
                else
                {
                    lbl.setText(String.format("%d < %d", endWert, startwert));
                    etEnwert.setText("");
                }
            }
            catch (Exception ex)
            {
                lbl.setText("Fehler!");
            }
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
//    ==============================================================================================
    private void permissions()
    {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE

        }, 13);

        if (Build.VERSION.SDK_INT >= 30){
            if (!Environment.isExternalStorageManager()){
                Intent getpermission = new Intent();
                getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(getpermission);
            }
        }
    }
//    ==============================================================================================
    private void initComponents()
    {
        lbl = (TextView) findViewById(R.id.lbl);
        etEnwert = (EditText) findViewById(R.id.etEndwert);
        btnBerechnen = (Button) findViewById(R.id.btnStart);
        btnBerechnen.setOnClickListener(new MyOCL());

        datei =new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/"+DATAINAME);
    }
//    ==============================================================================================
    private void startAuslesen()
    {
        startwert = null;
        try
        {
            FileReader fr = new FileReader(datei);
            BufferedReader br = new BufferedReader(fr);

            String zeile  = br.readLine();
            while (zeile != null)
            {
                startwert = Integer.parseInt(zeile)+1;
                zeile = br.readLine();
            }

            br.close();
            fr.close();
        }
        catch (IOException ex)
        {
            Log.e("*** FEHLER ***", ex.getMessage());

        }
        if (startwert == null)
            startwert = 2;

    }

//    ----------------------------------------------------------------------------------------------
    private void rechnen()
    {
       try
       {
           FileWriter fw = new FileWriter(datei,true);
           boolean istPrim = true;
           int zahl = startwert;
           int teiler;
           int anzahlTeiler;


           while (zahl <= endWert)
           {
               lbl.setText("" + zahl);
               teiler = 2;
               anzahlTeiler = 0;
               while ((anzahlTeiler == 0) && (teiler < zahl-1))
               {
                   if (zahl % teiler == 0)
                       anzahlTeiler++;
                   teiler++;
               }
               if (anzahlTeiler == 0)
                   fw.write(String.format("%d\n", zahl));
               zahl++;
           }
           fw.close();
       }
       catch (IOException ex)
       {

       }
       startwert = endWert;
        lbl.setText("Start: " + startwert);

    }
//    ----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        permissions();

        startAuslesen();

        lbl.setText("Start: " + startwert);

    }
//    ----------------------------------------------------------------------------------------------
}