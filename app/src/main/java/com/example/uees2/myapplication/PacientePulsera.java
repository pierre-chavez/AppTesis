package com.example.uees2.myapplication;

public class PacientePulsera {
    private String idPulsera;
    private String cedula;
    private int estado;

    public PacientePulsera() {
    }

    public PacientePulsera(String idPulsera, String cedula, int estado) {
        this.idPulsera = idPulsera;
        this.cedula = cedula;
        this.estado = estado;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getIdPulsera() {
        return idPulsera;
    }

    public void setIdPulsera(String idPulsera) {
        this.idPulsera = idPulsera;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
}
