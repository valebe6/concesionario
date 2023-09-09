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

public class facturaActivity extends AppCompatActivity {

    EditText jetcodigo,jetfecha,jetidentificacion,jetnombre,jettelefono,jetplaca,jetmarca,jetvalor;
    CheckBox jcbactivo;
    Button jbtbuscarfactura,jbtguardar,jbtanular,jbtbuscarcliente,jbtbuscarvehiculo;
    String codigo,fecha,identificacion,placa;
    long respuesta;
    boolean sw;
    ClsOpenHerlper admin=new ClsOpenHerlper(this,"Concesionario1.db",null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura);
        jetcodigo=findViewById(R.id.etcodigo);
        jetfecha=findViewById(R.id.etfecha);
        jetidentificacion=findViewById(R.id.etidentificacionF);
        jetnombre=findViewById(R.id.etnombreF);
        jettelefono=findViewById(R.id.ettelefonoF);
        jetplaca=findViewById(R.id.etplacaF);
        jetmarca=findViewById(R.id.etmarcaF);
        jetvalor=findViewById(R.id.etvalorF);
        jcbactivo=findViewById(R.id.cbactivoF);
        jbtguardar=findViewById(R.id.btguardar);
        jbtanular=findViewById(R.id.btanular);
        jbtbuscarfactura=findViewById(R.id.btbuscarfactura);
        jbtbuscarcliente=findViewById(R.id.btbuscarcliente);
        jbtbuscarvehiculo=findViewById(R.id.btbuscarvehiculo);
        sw=false;
    }

    public void ConsultarFactura(View view){
        codigo=jetcodigo.getText().toString();
        if (!codigo.isEmpty() ){
            SQLiteDatabase sqLiteDatabase = admin.getReadableDatabase();
            Cursor registro=sqLiteDatabase.rawQuery("select * from Tblfactura where code_factura='"+codigo+"'",null);
            if  (registro.moveToNext()) {
                jetfecha.setText(registro.getString(1));
                jetidentificacion.setText(registro.getString(2));
                jetplaca.setText(registro.getString(3));
                jcbactivo.setChecked(true);
                jbtanular.setEnabled(true);

                identificacion=jetidentificacion.getText().toString();
                Cursor registroC=sqLiteDatabase.rawQuery("select * from TblCliente where Ident_cliente='"+identificacion+"'",null);
                if (registroC.moveToNext()){
                    jetnombre.setText(registroC.getString(1));
                    jettelefono.setText(registroC.getString(3));
                }

                placa=jetplaca.getText().toString();
                Cursor registroV=sqLiteDatabase.rawQuery("select * from TblVehiculo where Placa='"+placa+"'",null);
                if (registroV.moveToNext()){
                    jetmarca.setText(registroV.getString(1));
                    jetvalor.setText(registroV.getString(3));
                }
                if (registro.getString(4).equals("si")){
                    jetcodigo.setEnabled(false);
                }else{
                    jcbactivo.setChecked(false);
                    Toast.makeText(this, "El registro existe pero esta anulado", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "Factura no encontrada, registre una nueva.", Toast.LENGTH_SHORT).show();
                jetfecha.setEnabled(true);
                ContentValues fila=new ContentValues();
                fila.put("code_factura",codigo);
                fila.put("Fecha",fecha);
                jbtbuscarcliente.setEnabled(true);
                jbtbuscarvehiculo.setEnabled(true);
                jetidentificacion.setEnabled(true);
                jcbactivo.setChecked(true);
                sqLiteDatabase.close();
            }
        }else{
            Toast.makeText(this, "Codigo requerido", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
        }
    }

    public void ConsultarCliente(View view){
        identificacion=jetidentificacion.getText().toString();
        SQLiteDatabase sqLiteDatabase = admin.getReadableDatabase();
        Cursor registro1 = sqLiteDatabase.rawQuery("select * from TblCliente where Ident_cliente='"+identificacion+"'",null);
        if(registro1.moveToNext()){
            sw=true;
            jetnombre.setText(registro1.getString(1));
            jettelefono.setText(registro1.getString(3));
            jetplaca.setEnabled(true);
        }else{
            Toast.makeText(this, "Cliente no registrado", Toast.LENGTH_SHORT).show();
            jetidentificacion.requestFocus();
        }
        sqLiteDatabase.close();
    }

    public void ConsultarVehiculo(View view){
        SQLiteDatabase sqLiteDatabase = admin.getReadableDatabase();
        placa=jetplaca.getText().toString();
        Cursor registro2 = sqLiteDatabase.rawQuery("select * from TblVehiculo where Placa='"+placa+"'",null);
        if (registro2.moveToNext()){
            if (registro2.getString(4).equals("si")){
                jetmarca.setText(registro2.getString(1));
                jetvalor.setText(registro2.getString(3));
                jbtguardar.setEnabled(true);
                jbtanular.setEnabled(true);
            }else {
                Toast.makeText(this, "el vehiculo no está activo.", Toast.LENGTH_SHORT).show();
                jetplaca.requestFocus();
            }
        }else {
            Toast.makeText(this, "Vehiculo no registrado", Toast.LENGTH_SHORT).show();
            jetplaca.requestFocus();
        }
        sqLiteDatabase.close();
    }

    public void GuardarVenta(View view){
        codigo=jetcodigo.getText().toString();
        fecha=jetfecha.getText().toString();
        placa=jetplaca.getText().toString();
        if (!codigo.isEmpty()){
            SQLiteDatabase sqLiteDatabase=admin.getWritableDatabase();
            ContentValues fila=new ContentValues();
            fila.put("code_factura",codigo);
            fila.put("Fecha",fecha);
            fila.put("Ident_cliente",identificacion);
            fila.put("Placa",placa);
            if (sw == false)
                respuesta=sqLiteDatabase.insert("Tblfactura",null,fila);
            else{
                sw=false;
                respuesta=sqLiteDatabase.update("Tblfactura",fila,"code_factura='"+codigo+"'",null);
            }
            if (respuesta > 0){
                Toast.makeText(this,"Venta exitosa",Toast.LENGTH_SHORT).show();
                Limpiar_campos();
            }else{
                Toast.makeText(this,"Error guardando registro",Toast.LENGTH_SHORT).show();
            }
            sqLiteDatabase.close();
        }else {
            Toast.makeText(this,"Todos los datos son requeridos",Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
        }
    }
    public void AnularFactura(View view){
        codigo=jetcodigo.getText().toString();
        SQLiteDatabase sqLiteDatabase=admin.getWritableDatabase();
        ContentValues fila=new ContentValues();
        respuesta=sqLiteDatabase.update("Tblfactura",fila,"code_factura='"+codigo+"'",null);
        if (respuesta > 0){
            Toast.makeText(this,"Factura anulada", Toast.LENGTH_SHORT).show();
            fila.put("Activo","no");
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
        jetcodigo.setText("");
        jetfecha.setText("");
        jetidentificacion.setText("");
        jetidentificacion.setEnabled(false);
        jetnombre.setText("");
        jettelefono.setText("");
        jetplaca.setText("");
        jetplaca.setEnabled(false);
        jetmarca.setText("");
        jetvalor.setText("");
        jbtguardar.setEnabled(false);
        jbtanular.setEnabled(false);
        sw=false;
    }//fin LImpiar
}