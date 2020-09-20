package com.cortezhac.sqlitematerial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.cortezhac.sqlitematerial.javaclass.ConexionSQLite;
import com.cortezhac.sqlitematerial.javaclass.Dto;

public class ConsultaSpiner extends AppCompatActivity {
    private Spinner sp_option;
    private TextView tv_codigo, tv_descropcion, tv_precio;

    // Conexion SQL
    ConexionSQLite conexion = new ConexionSQLite(this);
    Dto datos = new Dto();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_spiner);
        sp_option = findViewById(R.id.spinner);
        tv_codigo = findViewById(R.id.StrtxtCod);
        tv_descropcion = findViewById(R.id.StrtxtDes);
        tv_precio = findViewById(R.id.StrtxtPrecio);

        conexion.consultaListaArticulos();
        ArrayAdapter<CharSequence> adaptador = new ArrayAdapter(this, android.R.layout.simple_spinner_item, conexion.obtenerListaArticulos());
        sp_option.setAdapter(adaptador);
        sp_option.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    // El spinner comienza en 1 y el origen ArrayList esta en posicion 0 y contiene un obj Dto
                    tv_codigo.setText(conexion.consultaListaArticulos().get(position - 1).getCodigo());
                    tv_descropcion.setText(conexion.consultaListaArticulos().get(position - 1).getDescripcion());
                    tv_precio.setText(String.valueOf(conexion.consultaListaArticulos().get(position - 1).getPrecio()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}