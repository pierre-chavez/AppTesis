package com.example.uees2.myapplication;

public class Notificacion {
    String admin;
    String enfermero;

    public Notificacion() {
    }

    public Notificacion(String admin, String enfermero) {
        this.admin = admin;
        this.enfermero = enfermero;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getEnfermero() {
        return enfermero;
    }

    public void setEnfermero(String enfermero) {
        this.enfermero = enfermero;
    }
}
