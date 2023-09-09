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

public class vehiculosActivity extends AppCompatActivity {
    EditText jetplaca,jetmarca,jetmodelo,jetvalor;
    CheckBox jcbactivo;
    Button jbtguardar, jbtanular;
    String placa, marca, modelo, valor;
    ClsOpenHerlper admin=new ClsOpenHerlper(this,"Concesionario1.db",null,1);
    long respuesta;
    boolean sw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculos);
        jetplaca=findViewById(R.id.etplaca);
        jetmarca=findViewById(R.id.etmarca);
        jetmodelo=findViewById(R.id.etmodelo);
        jetvalor=findViewById(R.id.etvalor);
        jcbactivo=findViewById(R.id.cbactivo);
        jbtguardar=findViewById(R.id.btguardar);
        jbtanular=findViewById(R.id.btanular);
        sw=false;
        jetplaca.requestFocus();
        Toast.makeText(this, "Digite una placa y click en buscar", Toast.LENGTH_SHORT).show();
    }//fin onCreate

    public void ConsultarVehiculo(View view){
        //validar que la placa fue digitada
        placa=jetplaca.getText().toString();
        if (!placa.isEmpty()){
            SQLiteDatabase sqLiteDatabase = admin.getReadableDatabase();
            Cursor registro = sqLiteDatabase.rawQuery("select * from TblVehiculo where Placa='"+placa+"'",null);
            if (registro.moveToNext()){
                sw=true;
                jbtanular.setEnabled(true);
                jetmarca.setText(registro.getString(1));
                jetmodelo.setText(registro.getString(2));
                jetvalor.setText(registro.getString(3));
                if (registro.getString(4).equals("si")){
                    jcbactivo.setChecked(true);
                    jetmarca.setEnabled(true);
                    jetmodelo.setEnabled(true);
                    jetvalor.setEnabled(true);
                    jbtguardar.setEnabled(true);
                    jetmarca.requestFocus();
                    jetplaca.setEnabled(false);
                }else{
                    jcbactivo.setChecked(false);
                    Toast.makeText(this, "El registro existe pero esta anulado", Toast.LENGTH_SHORT).show();
                }
            }else {
                jcbactivo.setChecked(true);
                Toast.makeText(this, "Vehiculo no registrado", Toast.LENGTH_SHORT).show();
                jcbactivo.setChecked(true);
                jetmarca.setEnabled(true);
                jetmodelo.setEnabled(true);
                jetvalor.setEnabled(true);
                jbtguardar.setEnabled(true);
                jetmarca.requestFocus();
                jetplaca.setEnabled(false);
            }
            sqLiteDatabase.close();
        }else {
            Toast.makeText(this, "Placa es requerida", Toast.LENGTH_SHORT).show();
            jetplaca.requestFocus();
        }

    }//fin del metodo consultar

    public void Guardar(View view){
        placa=jetplaca.getText().toString();
        marca=jetmarca.getText().toString();
        modelo=jetmodelo.getText().toString();
        valor=jetvalor.getText().toString();
        if (!marca.isEmpty() && !modelo.isEmpty() && !valor.isEmpty()){
            SQLiteDatabase sqLiteDatabase=admin.getWritableDatabase();
            ContentValues fila=new ContentValues();
            fila.put("Placa",placa);
            fila.put("Marca",marca);
            fila.put("Modelo",modelo);
            fila.put("Valor",valor);
            if (sw == false)
                respuesta=sqLiteDatabase.insert("TblVehiculo",null,fila);
            else {
                sw=false;
                respuesta=sqLiteDatabase.update("TblVehiculo",fila,"Placa='"+placa+"'",null);
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
            jetmarca.requestFocus();
        }
    }

    public void Anular(View view){
        SQLiteDatabase sqLiteDatabase=admin.getWritableDatabase();
        ContentValues fila=new ContentValues();
        fila.put("Activo","no");
        respuesta=sqLiteDatabase.update("TblVehiculo",fila,"Placa='"+placa+"'",null);
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
        jetplaca.setEnabled(true);
        jetmarca.setEnabled(false);
        jetmodelo.setEnabled(false);
        jetvalor.setEnabled(false);
        jetplaca.setText("");
        jetmarca.setText("");
        jetmodelo.setText("");
        jetvalor.setText("");
        jcbactivo.setChecked(false);
        jbtguardar.setEnabled(false);
        jbtanular.setEnabled(false);
        jetplaca.requestFocus();
        sw=false;
    }//fin LImpiar
}