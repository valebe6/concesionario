package com.example.concesionario;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void Clientes(View view){
        Intent intclientes=new Intent(this, clientesActivity.class);
        startActivity(intclientes);
    }

    public void Vehiculos(View view){
        Intent intvehiculos=new Intent(this, vehiculosActivity.class);
        startActivity(intvehiculos);
    }

    public void Facturas(View view){
        Intent intfacturas=new Intent(this, facturaActivity.class);
        startActivity(intfacturas);
    }
}