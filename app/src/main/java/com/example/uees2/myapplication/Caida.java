package com.example.uees2.myapplication;

public class Caida {
    private String fecha_hora;
    private int tipo_caida;

    public Caida() {
    }

    public Caida(String fecha_hora, int tipo_caida) {
        this.fecha_hora = fecha_hora;
        this.tipo_caida = tipo_caida;
    }

    public int getTipo_caida() {
        return tipo_caida;
    }

    public void setTipo_caida(int tipo_caida) {
        this.tipo_caida = tipo_caida;
    }

    public String getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(String fecha_hora) {
        this.fecha_hora = fecha_hora;
    }
}
