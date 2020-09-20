package com.cortezhac.sqlitematerial;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.cortezhac.sqlitematerial.javaclass.ConexionSQLite;
import com.cortezhac.sqlitematerial.javaclass.Dto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.KeyEvent;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText edit_codigo, edit_descripcion, edit_precio;
    private Button btn_guardar, btn_consultar1, btn_consultar2, btn_eliminar, btn_actualizar;
    private TextView tv_resultado;
    boolean inputEt = false;
    boolean inputEd = false;
    boolean input1 = false;
    int resultadoInsert = 0;

    Modal ventanas = new Modal();
    // Conexion SQL
    ConexionSQLite conexion = new ConexionSQLite(this);
    // Objeto de datos
    Dto datos = new Dto();
    // ventana flotante
    AlertDialog.Builder constructor;

    // Listener para los botones virtuales
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            // Se crea un ventana flotante
            new android.app.AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_baseline_exit_to_app_24)
                    .setTitle("Warming")
                    .setMessage("Realmente desea salir?")
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // cierra la aplicacion
                            finishActivity();
                        }
                    }).show();
            return  true;
        }
        // detiene los demas procesos
        return  super.onKeyDown(keyCode,event);
    }
    // Listener inicair aplicacion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_ios_24));
        toolbar.setTitleTextColor(getResources().getColor(R.color.principal));
        toolbar.setTitleMargin(0,0,0,0);
        toolbar.setSubtitle("CRUD SQLite-2019");
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.secundario));
        toolbar.setTitle("Irvin Cortez");
        setSupportActionBar(toolbar);

        // Pantalla completa
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Seleccionar un elemnto del toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmacion();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                ventanas.Search(MainActivity.this);
            }
        });

        edit_codigo = findViewById(R.id.XeditCodigo);
        edit_descripcion = findViewById(R.id.XeditDescrip);
        edit_precio = findViewById(R.id.XeditArti);
        btn_guardar = findViewById(R.id.XbtnGuardar);
        btn_consultar1 = findViewById(R.id.XbtnBuscarDescrip);
        btn_consultar2 = findViewById(R.id.XbtnBuscarCod);
        btn_eliminar = findViewById(R.id.XbtnBorrar);
        btn_actualizar = findViewById(R.id.XbtnEdir);

        String senal = "";
        String codigo = "";
        String descripcion = "";
        String precio = "";
        try{
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                codigo = bundle.getString("codigo");
                senal = bundle.getString("senal");
                descripcion = bundle.getString("descripcion");
                precio = bundle.getString("precio");
                if(senal.equals("1")){
                    edit_codigo.setText(codigo);
                    edit_descripcion.setText(descripcion);
                    edit_precio.setText(precio);
                }
            }
        }catch (Exception ex){

        }
    }

    private void confirmacion(){
        String mensaje = "Realmente deseas salir?";
        constructor = new AlertDialog.Builder(MainActivity.this);
        constructor.setIcon(R.drawable.ic_baseline_exit_to_app_24);
        constructor.setTitle("Wrning");
        constructor.setMessage(mensaje);
        constructor.setCancelable(false);
        constructor.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cerrar actividad
                MainActivity.this.finish();
            }
        });

        constructor.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // No hacer nada
            }
        });
        constructor.show();
    }
    // Listener creacion de menu desplegable
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    // Listener opcion seleccionada del menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_limpiar) {// limpiar los campos menu desplegable res/menu
            edit_codigo.setText(null);
            edit_descripcion.setText(null);
            edit_precio.setText(null);
            return true;
        }else if(id == R.id.action_listarArticulos){
            Intent spinnerActivity = new Intent(MainActivity.this, ConsultaSpiner.class);
            startActivity(spinnerActivity);
            return true;
        }else if(id == R.id.action_listarArticulos1){
            Intent ListarActivity =  new Intent(MainActivity.this, ListarArticulos.class);
            startActivity(ListarActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    // Listener onclick
    public void alta(View v){
        if(edit_codigo.getText().toString().length() == 0){
            edit_codigo.setError("Campo obligatorio");
            inputEt = false;
        }else{
            inputEt = false;
        }

        if(edit_descripcion.getText().toString().length() == 0){
            edit_descripcion.setError("Campo Obligatorio");
            inputEd = false;
        }else {
            inputEd = false;
        }

        if(edit_precio.getText().toString().length() == 0){
            edit_precio.setError("Campo obligatorio");
            input1 = false;
        }else {
            input1 = false;
        }
        // Revision de el estado de los campos
        if(inputEt && input1&& inputEd){
            try{
                // Llenar objeto de datos
                datos.setCodigo(Integer.parseInt(edit_codigo.getText().toString()));
                datos.setDescripcion(edit_descripcion.getText().toString());
                datos.setPrecio(Double.parseDouble(edit_precio.getText().toString()));
                // Si la insercion es exitosa
                if(conexion.InsertTradicional(datos)){
                    Toast.makeText(this, "Registro agregado satisfactoriamente", Toast.LENGTH_LONG).show();
                    limpiarDatos();
                }else{
                    Toast.makeText(getApplicationContext(), "Ya existe un regristro :\n"
                            +"Codigo" + edit_codigo.getText().toString(), Toast.LENGTH_SHORT).show();
                    limpiarDatos();
                }
            }catch (Exception ex){
                Toast.makeText(getApplicationContext(), "Ya existe", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void mensaje(String valor){
        Toast.makeText(this, valor, Toast.LENGTH_SHORT).show();
    }

    public void limpiarDatos(){
        edit_codigo.setText(null);
        edit_descripcion.setText(null);
        edit_precio.setText(null);
        edit_codigo.requestFocus();
    }

    public void consultarPorCodigo(View v){
        if(edit_codigo.getText().toString().length() == 0){
            edit_codigo.setError("Campo obligatorio");
            inputEt = false;
        }else{
            inputEt = true;
        }

        if(inputEt){
            String codigo = edit_codigo.getText().toString();
            datos.setCodigo(Integer.parseInt(codigo));
            if(conexion.consultaArciulos(datos)){
                edit_descripcion.setText(datos.getDescripcion());
                edit_precio.setText(String.valueOf(datos.getPrecio()));
            }else{
                Toast.makeText(this, "No hay registro", Toast.LENGTH_SHORT).show();
                limpiarDatos();
            }
        }else{
            Toast.makeText(this, "Ingrese un codigo", Toast.LENGTH_SHORT).show();
        }
    }

    public void consultarPorDescripcion(View v){
        if(edit_descripcion.getText().toString().length() == 0){
            edit_descripcion.setError("Campo obligatorio");
            inputEd = false;
        }else{
            inputEd = true;
        }
        if(inputEd){
            String descripcion = edit_descripcion.getText().toString();
            datos.setDescripcion(descripcion);
            if(conexion.consultarDescripcion(datos)){
                edit_codigo.setText(datos.getCodigo());
                edit_descripcion.setText(datos.getDescripcion());
                edit_precio.setText(String.valueOf(datos.getPrecio()));
            }else {
                Toast.makeText(this, "El articulo no existe", Toast.LENGTH_SHORT).show();
                limpiarDatos();
            }
        }else{
            Toast.makeText(this, "Ingrese la descripcion del articulo", Toast.LENGTH_SHORT).show();
        }
    }

    public void bajarPorCodigo(View v){
        if(edit_codigo.getText().toString().length() == 0){
            edit_codigo.setError("campo obligatorio");
            inputEt = false;
        }else{
            inputEt = true;
        }
        if(inputEt){
            String codigo = edit_codigo.getText().toString();
            datos.setCodigo(Integer.parseInt(codigo));
            // Invoca la ventana emergente por eso el context
            if(conexion.bajaCodigo(MainActivity.this, datos)){
                limpiarDatos();
            }else{
                Toast.makeText(this, "El articulo no existe", Toast.LENGTH_SHORT).show();
                limpiarDatos();
            }
        }
    }

    public void modificacion(View v){
        if(edit_codigo.getText().toString().length() == 0){
            edit_codigo.setError("Campo obligatorio");
            inputEt = false;
        }else {
            inputEt = true;
        }

        if(inputEt){
            String cod = edit_codigo.getText().toString();
            String descrpcion = edit_codigo.getText().toString();
            double precio = Double.parseDouble(edit_precio.getText().toString())
            // setiar objeto
            datos.setCodigo(cod);
            datos.setDescripcion(descrpcion);
            datos.setPrecio(precio);
            if(conexion.modificar(datos)){
                Toast.makeText(this, "Registro modificado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}