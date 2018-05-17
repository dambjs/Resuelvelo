package com.example.sebas_pc.resuelvelo.view;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sebas_pc.resuelvelo.R;

public class CrearIncidencia extends AppCompatActivity {

    // The drop down menu list text.
    private String dropDownItemArr[] = {"Prioridad Baja", "Prioridad Media", "Prioridad Alta"};

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_incidencia);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String[] departamentos = {"Sistemas Informáticos","Electricidad","Iluminación","Mobiliario","Equipos o Maquinaria"};
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, departamentos));

        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        String[] destinatario = {"A","B","C","D","E"};
        spinner2.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, destinatario));

        Spinner spinner3 = (Spinner) findViewById(R.id.spinner3);
        String[] motivo = {"Conexión a Internet","Fallo Impresora","Fallo del Ordenador"};
        spinner3.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, motivo));

        // Get ActionBar
        actionBar = getSupportActionBar();

        // SpinnerAdapter is used to calculate data and view that will be shown in drop down menu list.
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter() {

            // This method return the View object for dropdown list item.
            // The return view contains one ImageView and one TextView.
            @Override
            public View getDropDownView(int itemIndex, View view, ViewGroup viewGroup) {
                // Create a LinearLayout view object.
                LinearLayout linearLayout = new LinearLayout(CrearIncidencia.this);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
                linearLayout.setLayoutParams(layoutParams);

                // Create TextView and set it's text and color.
                TextView itemTextView = new TextView(CrearIncidencia.this);
                String itemText = dropDownItemArr[itemIndex];
                itemTextView.setText(itemText);
                itemTextView.setTextSize(20);
                linearLayout.setBackgroundColor(Color.WHITE);
                linearLayout.setPadding(10,10,50,10);
                itemTextView.setBackgroundColor(Color.WHITE);
                itemTextView.setTextColor(Color.BLACK);

                // Add TextView in return view.
                linearLayout.addView(itemTextView,0);

                return linearLayout;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver dataSetObserver) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

            }

            @Override
            public int getCount() {
                // Return drop down menu item count.
                return dropDownItemArr.length;
            }

            @Override
            public Object getItem(int itemIndex) {
                // Return drop down menu item text.
                return dropDownItemArr[itemIndex];
            }

            @Override
            public long getItemId(int itemIndex) {
                return itemIndex;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            // This method return the View object for drop down input box.
            // It only return TextView object.
            @Override
            public View getView(int itemIndex, View view, ViewGroup viewGroup) {
                TextView itemTextView = new TextView(CrearIncidencia.this);
                String itemText = dropDownItemArr[itemIndex];
                itemTextView.setText(itemText);
                itemTextView.setTextSize(20);
                itemTextView.setTextColor(Color.WHITE);

                return itemTextView;
            }

            @Override
            public int getItemViewType(int i) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                // This method must return 1, otherwise java.lang.IllegalArgumentException will be thrown.
                return 1;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        };

        // Set action bar navigation mode to list mode.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        // Set action bar list navigation data and item click listener.
        actionBar.setListNavigationCallbacks(spinnerAdapter, new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                return true;
            }
        });

    }
}