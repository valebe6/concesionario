package com.example.concesionario;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class clientesActivity extends AppCompatActivity {
    EditText jetidentificacion,jetnombre,jetdireccion,jettelefono;
    CheckBox jcbactivo;
    Button jbtguardar, jbtanular;
    String identificacion, nombre, direccion, telefono;
    ClsOpenHerlper admin=new ClsOpenHerlper(this,"Concesionario1.db",null,1);
    long respuesta;
    boolean sw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);
        jetidentificacion=findViewById(R.id.etidentificacion);
        jetnombre=findViewById(R.id.etnombre);
        jetdireccion=findViewById(R.id.etdireccion);
        jettelefono=findViewById(R.id.ettelefono);
        jcbactivo=findViewById(R.id.cbactivo);
        jbtguardar=findViewById(R.id.btguardar);
        jbtanular=findViewById(R.id.btanular);
        sw=false;
        jetidentificacion.requestFocus();
        Toast.makeText(this, "Digite identificacion y click en buscar", Toast.LENGTH_SHORT).show();
    }//fin onCreate

    public void Consultar(View view){
        //validar que la identificacion fue digitada
        identificacion=jetidentificacion.getText().toString();
        if (!identificacion.isEmpty()){
            SQLiteDatabase sqLiteDatabase = admin.getReadableDatabase();
            Cursor registro = sqLiteDatabase.rawQuery("select * from TblCliente where Ident_cliente='"+identificacion+"'",null);
            if (registro.moveToNext()){
                sw=true;
                jbtanular.setEnabled(true);
                jetnombre.setText(registro.getString(1));
                jetdireccion.setText(registro.getString(2));
                jettelefono.setText(registro.getString(3));
                if (registro.getString(4).equals("si")){
                    jcbactivo.setChecked(true);
                    jetnombre.setEnabled(true);
                    jetdireccion.setEnabled(true);
                    jettelefono.setEnabled(true);
                    jbtguardar.setEnabled(true);
                    jetnombre.requestFocus();
                    jetidentificacion.setEnabled(false);
                }else{
                    jcbactivo.setChecked(false);
                    Toast.makeText(this, "El registro existe pero esta anulado", Toast.LENGTH_SHORT).show();
                }
            }else {
                jcbactivo.setChecked(true);
                Toast.makeText(this, "Cliente no registrado", Toast.LENGTH_SHORT).show();
                jcbactivo.setChecked(true);
                jetnombre.setEnabled(true);
                jetdireccion.setEnabled(true);
                jettelefono.setEnabled(true);
                jbtguardar.setEnabled(true);
                jetnombre.requestFocus();
                jetidentificacion.setEnabled(false);
            }
            sqLiteDatabase.close();
        }else {
            Toast.makeText(this, "Identificion es requerida", Toast.LENGTH_SHORT).show();
            jetidentificacion.requestFocus();
        }

    }//fin del metodo consultar

    public void Guardar(View view){
        identificacion=jetidentificacion.getText().toString();
        nombre=jetnombre.getText().toString();
        direccion=jetdireccion.getText().toString();
        telefono=jettelefono.getText().toString();
        if (!nombre.isEmpty() && !direccion.isEmpty() && !telefono.isEmpty()){
            SQLiteDatabase sqLiteDatabase=admin.getWritableDatabase();
            ContentValues fila=new ContentValues();
            fila.put("Ident_cliente",identificacion);
            fila.put("Nombre_cliente",nombre);
            fila.put("Dir_cliente",direccion);
            fila.put("Tel_cliente",telefono);
            if (sw == false)
                respuesta=sqLiteDatabase.insert("TblCliente",null,fila);
            else {
                sw=false;
                respuesta=sqLiteDatabase.update("TblCliente",fila,"Ident_cliente='"+identificacion+"'",null);
            }
            if (respuesta > 0){
                Toast.makeText(this,"registro guardado",Toast.LENGTH_SHORT).show();
                Limpiar_campos();
            }else {
                Toast.makeText(this,"Error guardando registro",Toast.LENGTH_SHORT).show();
            }

            sqLiteDatabase.close();
        }else {
            Toast.makeText(this,"Todos los datos son requeridos",Toast.LENGTH_SHORT).show();
            jetnombre.requestFocus();
        }
    }

    public void Anular(View view){
        SQLiteDatabase sqLiteDatabase=admin.getWritableDatabase();
        ContentValues fila=new ContentValues();
        fila.put("Activo","no");
        respuesta=sqLiteDatabase.update("TblCliente",fila,"Ident_cliente='"+identificacion+"'",null);
        if (respuesta > 0){
            Toast.makeText(this,"Registro anulado", Toast.LENGTH_SHORT).show();
            Limpiar_campos();
        }else{
            Toast.makeText(this,"Error anulando registro", Toast.LENGTH_SHORT).show();
        }
        sqLiteDatabase.close();
    }

    public void Regresar(View view){
        Intent intmain=new Intent(this, MainActivity.class);
        startActivity(intmain);
    }

    public void Cancelar(View view){
        Limpiar_campos();
    }

    private  void  Limpiar_campos(){
        jetidentificacion.setEnabled(true);
        jetnombre.setEnabled(false);
        jettelefono.setEnabled(false);
        jetidentificacion.setText("");
        jetnombre.setText("");
        jetdireccion.setText("");
        jettelefono.setText("");
        jcbactivo.setChecked(false);
        jbtguardar.setEnabled(false);
        jbtanular.setEnabled(false);
        jetidentificacion.requestFocus();
        sw=false;
    }//fin LImpiar
}