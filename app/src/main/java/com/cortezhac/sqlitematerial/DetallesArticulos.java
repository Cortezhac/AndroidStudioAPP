package com.cortezhac.sqlitematerial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.cortezhac.sqlitematerial.javaclass.Dto;

public class DetallesArticulos extends AppCompatActivity {
    private TextView tv_codigo, tv_descripcion, tv_precio;
    private TextView tv_codigo1, tv_descripcion1, tv_precio1, tv_fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_articulos);
        // Variables a setear
        tv_codigo = findViewById(R.id.StrtxtCodLista);
        tv_descripcion = findViewById(R.id.StrtxtDesLista);
        tv_precio = findViewById(R.id.StrtxtPreciolista);
        tv_fecha = findViewById(R.id.StrFecha);
        // Obetner datos del intent
        Bundle objeto = getIntent().getExtras();
        Dto datos = null;
        if(objeto != null){
            datos = (Dto) objeto.getSerializable("articulo")
        }
    }
}