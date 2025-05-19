package com.tabacapp;

import com.tabacapp.db.DBConnection;

public class Ejecutar {
    public static void main(String[] args) {
        DBConnection.getConnection();
    }
}
