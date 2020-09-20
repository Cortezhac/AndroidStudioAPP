package com.cortezhac.sqlitematerial.javaclass;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.cortezhac.sqlitematerial.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ConexionSQLite extends SQLiteOpenHelper {
    boolean estadoDelete = true;
    ArrayList<String> listaArticulos;
    ArrayList<Dto> articulosList;

    public ConexionSQLite(@Nullable Context context) {
        super(context, "administracion_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE articulos (" +
                "codigo integer not null primary key," +
                "descripcion text," +
                "precio real)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS articulos");
        onCreate(db);
    }

    public SQLiteDatabase bd(){
        SQLiteDatabase bd = this.getWritableDatabase();
        return  bd;
    }

    public boolean InsertTradicional(Dto datos){
        boolean estado = true;
        int resultado;
        try{
            int codigo =  datos.getCodigo();
            String descripcion = datos.getDescripcion();
            double precio = datos.getPrecio();
            // Cursor obtiene los datos de la bd equivalente a ResulSet
            Cursor fila = bd().rawQuery("SELECT codigo FROM articulos WHERE codigo = " + codigo + ";" , null);
            if(fila.moveToFirst() == true){
                estado = false;
            }else{
                String SQL = "INSERT INTO articulos (codigo,descripcion, precio) " +
                        "VALUES (" + String.valueOf(codigo) + ", '"+ descripcion + "', " + String.valueOf(precio) + ");";
                // Ejecutar sentencia SQLite
                bd().execSQL(SQL);
                // Cerrar conexion
                bd().close();
                estado = true;
            }
        }catch (Exception ex){
            estado = false;
            Log.e("Error InsertTra " , ex.toString());
        }
        return estado;
    }

    public boolean insertarDatos(Dto datos){
        boolean estado = true;
        int resultado;
        ContentValues registro = new ContentValues();
        try{
            registro.put("codigo", datos.getCodigo());
            registro.put("descripcion", datos.getDescripcion());
            registro.put("precio", datos.getPrecio());
            Cursor fila  = bd().rawQuery("SELECT  codigo FROM articulos WHERE " +
                    "codigo = " + datos.getCodigo() + ";", null);
            if(fila.moveToFirst() == true){
                estado = true;
            }else{
                //                                                                 datos de la tabla
                resultado = (int) bd().insert("articulos", null, registro);
                if(resultado > 0){
                    estado = true;
                }else{
                    estado = false;
                }
            }
        }catch (Exception e){
            estado = false;
            Log.e("Error InsertDa", e.toString());
        }
        return estado;
    }

    public boolean insertarRegistros(Dto registro){
        boolean estado = false;
        int resultado;
        try{
            int codigo = registro.getCodigo();
            String descripcion = registro.getDescripcion();
            double precio = registro.getPrecio();
            // Obtener fecha actual
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            String fechaUno = formatoFecha.format(cal.getTime());
            Cursor fila = bd().rawQuery("SELECT codigo FROM aarticulos WHERE codigo = " + registro.getCodigo() + ";", null);
            if(fila.moveToFirst() == true){
                estado = false;
            }else{
                // Insert usando consultas preparadas
                String SQL = "INSERT INTO articulos " +
                        "(codigo, descripcion, precio) VALUES(?,?,?)";
                //                     Arreglo con todos los parametros que sustituiran a los ? del String SQL
                bd().execSQL(SQL, new String[]{String.valueOf(codigo), descripcion, String.valueOf(precio)});
                estado = true;
            }
        }catch (Exception ex){
            estado = true;
            Log.e("Error ", ex.toString());
        }
        return estado;
    }

    public boolean consultaCodigo(Dto datos){
        boolean estado = true;
        int resultado;
        // Obentener acceso a la BD
        SQLiteDatabase bd = this.getWritableDatabase();
        try{
            int codigo = datos.getCodigo();
            Cursor fila = bd.rawQuery("SELECT codigo,descripcion,precio FROM articulos" +
                    "WHERE codigo = " + codigo + ";", null);
            if(fila.moveToFirst()){
                datos.setCodigo(Integer.parseInt(fila.getString(0)));
                datos.setDescripcion(fila.getString(1));
                datos.setPrecio(Double.parseDouble(fila.getString(2)));
                estado = true;
            }else{
                estado = false;
            }
            bd.close();
        }catch (Exception ex){
            estado = false;
            Log.e("Error consulCod", ex.toString());
        }
        return estado;
    }

    public boolean consultaArciulos(Dto datos){
        boolean estado =  true;
        int resultado;
        // Traer la base de datos en modo lectura
        SQLiteDatabase bd = this.getReadableDatabase();
        try{
            String[] parametro = {String.valueOf(datos.getCodigo())};
            String[] campos = {"codigo", "descripcion","precio"};
            Cursor fila = bd.query("articulos", campos, "codigo=?", parametro,null,null,null);
            if(fila.moveToFirst()){
                datos.setCodigo(Integer.parseInt(fila.getString(0)));
                datos.setDescripcion(fila.getString(1));
                datos.setPrecio(Double.parseDouble(fila.getString(2)));
                estado = true;
            }else{
                estado = false;
            }
        }catch (Exception ex){
            estado = false;
            Log.e("Error consuLArt " , ex.toString());
        }
        return estado;
    }

    public boolean consultarDescripcion(Dto datos){
        boolean estado = true;
        int resultado;
        SQLiteDatabase bd = this.getWritableDatabase();
        try{
            String descripcion = datos.getDescripcion();
            Cursor fila = bd.rawQuery("SELECT codigo, descripcion,precio FROM articulos" +
                    "WHERE descirpcion = '" + descripcion + "';",null);
            if(fila.moveToFirst()){
                datos.setCodigo(Integer.parseInt(fila.getString(0)));
                datos.setDescripcion(fila.getString(1));
                datos.setPrecio(Double.parseDouble(fila.getString(2)));
                estado = true;
            }else{
                estado = false;
            }
        }catch (Exception ex){
            estado = false;
            Log.e("Error consulDes", ex.toString());
        }
        return estado;
    }
    /*
        Ventana emergente
     */
    public boolean bajaCodigo(final Context context, final Dto datos){
        estadoDelete = true;
        try{
            int codigo = datos.getCodigo();
            Cursor fila = bd().rawQuery("SELECT * FROM articulos WHERE codigo = "+ codigo + ";",null);
            if(fila.moveToFirst()){
                datos.setCodigo(Integer.parseInt(fila.getString(0)));
                datos.setDescripcion(fila.getString(1));
                datos.setPrecio(Double.parseDouble(fila.getString(2)));
                // Ventan flotante
                final AlertDialog.Builder constructor = new AlertDialog.Builder(context);
                constructor.setIcon(R.drawable.ic_baseline_delete_outline_24);
                constructor.setTitle("Precausion");
                constructor.setMessage("Esta seguro  de borrar el registro?\n" +
                        "Codigo: "+ datos.getCodigo() + "\nDescripcion: "+ datos.getDescripcion());
                constructor.setCancelable(false);
                // Asignar funcionabilidad a el botno que devuelve true
                constructor.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int codigo = datos.getCodigo();
                        // Eliminar registro devuelve un entero
                        int cant = bd().delete("articulos","codigo = " + codigo + ";",null);
                        if(cant > 0){
                            estadoDelete = true;
                            Toast.makeText(context,"Registro aliminado satisfactoriamente", Toast.LENGTH_LONG).show();
                        }else{
                            estadoDelete = false;
                        }
                        bd().close();
                    }
                });
                // Asignar funcionabilidad a boton respuesta negativa
                constructor.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //NADA
                    }
                });
                // mostrar mensaje
                AlertDialog mensajeFlotante = constructor.create();
                mensajeFlotante.show();
            }else{
                Toast.makeText(context, "No hay resultados encontrados para la busqueda especificada",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e){
          estadoDelete = false;
          Log.e("Error: bajaCod", e.toString());
        }
        return estadoDelete;
    }

    public boolean modificar(Dto datos){
        boolean estado = true;
        int resultado;
        SQLiteDatabase bd = this.getWritableDatabase();
        try{
            int codigo = datos.getCodigo();
            String descripcion = datos.getDescripcion();
            double precio = datos.getPrecio();
        }catch (Exception ex){
            estado = false;
            Log.e("Error: ", ex.toString());
        }
        return estado;
    }

    // ArrayList para rellenar el Spinner
    public ArrayList<Dto> consultaListaArticulos(){
        boolean estado = false;
        SQLiteDatabase bd = this.getReadableDatabase();
        Dto articulos = null;
        try{
            Cursor fila = bd.rawQuery("SELECT * FROM articulos", null);
            while (fila.moveToNext()){
                articulos = new Dto();
                articulos.setCodigo(fila.getInt(0));
                articulos.setDescripcion(fila.getString(1));
                articulos.setPrecio(fila.getDouble(2));

                articulosList.add(articulos);
                Log.i("codigo", String.valueOf(articulos.getCodigo()));
                Log.i("descripcion", articulos.getDescripcion());
                Log.i("precio", String.valueOf(articulos.getPrecio()));
            }
            obtenerListaArticulos();
        }catch (Exception ex){
            Log.e("Dio error esta madre", ex.toString());
        }
        return articulosList;
    }

    public ArrayList<String> obtenerListaArticulos(){
        // Variable de  la funcion de deonde fue invocada
        listaArticulos = new ArrayList<String>();
        listaArticulos.add("Seleccione");
        for (int  i = 0; i < articulosList.size(); i++){
            listaArticulos.add(articulosList.get(i).getCodigo() + "~~" + articulosList.get(i).getDescripcion());
        }
        return listaArticulos;
    }
    // end spinner
    // listview
    public ArrayList<String> consultaListaArticulosUno(){
        boolean estado =  false;
        SQLiteDatabase bd = this.getReadableDatabase();
        Dto articulos = null; // instancia vacia
        articulosList = new ArrayList<Dto>();
        try{
            Cursor fila = bd.rawQuery("SELECT * FROM articulos", null);
            while(fila.moveToNext()){
                // Llenar objeto de datos
                articulos = new Dto();
                articulos.setPrecio(fila.getInt(0));
                articulos.setDescripcion(fila.getString(1));
                articulos.setPrecio(fila.getDouble(2));
                articulosList.add(articulos);
            }
            // Llenar arraylist para introducir en el ListView
            listaArticulos = new ArrayList<String>();
            for (int i = 0 ; i <= articulosList.size(); i++){
                listaArticulos.add(articulos.getCodigo() + "~~" + articulos.getDescripcion());
            }
        }catch (Exception ex){

        }
        return listaArticulos;
    }
}
