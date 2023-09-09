package com.example.concesionario;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

class ClsOpenHerlper extends SQLiteOpenHelper {
    public ClsOpenHerlper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table TblCliente(Ident_cliente text primary key,Nombre_cliente text not null,Dir_cliente not null,Tel_cliente not null,Activo text default 'si')");
        sqLiteDatabase.execSQL("create table TblVehiculo(Placa text primary key, Marca text not null, Modelo text not null,Valor integer not null,Activo text default 'si')");
        sqLiteDatabase.execSQL("create table Tblfactura(code_factura text primary key,Fecha text not null,Ident_cliente text not null,Placa text not null,Activo text default 'si',constraint pk_facturas foreign key (Ident_cliente) references TblCLiente(Ident_cliente), foreign key (Placa) references TblVehiculo(Placa))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("Drop table TblFactura");
        sqLiteDatabase.execSQL("Drop table TblVehiculo");
        sqLiteDatabase.execSQL("Drop table TblCliente");{
            onCreate(sqLiteDatabase);
        }
    }
}