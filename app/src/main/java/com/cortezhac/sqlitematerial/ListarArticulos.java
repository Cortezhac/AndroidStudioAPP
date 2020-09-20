package com.cortezhac.sqlitematerial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.cortezhac.sqlitematerial.javaclass.ConexionSQLite;
import com.cortezhac.sqlitematerial.javaclass.Dto;

import java.util.ArrayList;

public class ListarArticulos extends AppCompatActivity {
    ListView ListaDePersonas;
    ArrayAdapter adaptador;
    SearchView searchView;
    ListView listView;
    ArrayList lista;
    ArrayAdapter adapter;
    String[] version = {"Aestro","Bleder","CupCake","Donut","Eclair","Froyo","GingerBread","HoneyComb","IceCream Sanwich","Jelly Bean",
                        "Kitkat","Lolipop","Marshmallow","Nought","Oreo"};
    ConexionSQLite conexion = new ConexionSQLite(this);
    Dto datos = new Dto();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_articulos);
        // ListView
        ListaDePersonas = findViewById(R.id.ListaResultados);
        // Buscador
        searchView = findViewById(R.id.StrBuscarLista);
        // Array adapter para el spinner o ListView
        adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, conexion.consultaListaArticulosUno());
        ListaDePersonas.setAdapter(adaptador);
        // Buscador
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText;
                adaptador.getFilter().filter(text);
                return false;
            }
        });
        // ListView que mostrara la inforamcion
        ListaDePersonas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String informacion = "Codigo: " + String.valueOf(conexion.consultaListaArticulos().get(position).getCodigo());
                informacion += "\nDescripcion: " + conexion.consultaListaArticulos().get(position).getDescripcion();
                informacion += "\nPrecio: " + String.valueOf(conexion.consultaListaArticulos().get(position).getPrecio());

                // Objeto que se enviara a la actividad
                Dto articulos = conexion.consultaListaArticulos().get(position);
                Intent intent = new Intent(ListarArticulos.this, DetallesArticulos.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("articulo", articulos);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}