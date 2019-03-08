package com.example.uees2.myapplication;

import java.io.Serializable;

public class Paciente implements Serializable {

    String cedula;
    String nombres;
    String apellidos;
    String fechaRegistro;
    String genero;
    String habitcion;
    String nombreContacto;
    String numeroContacto;
    String familiaIds;

    public Paciente() {
    }

    public Paciente(String cedula, String nombres, String apellidos, String fechaRegistro, String genero, String habitcion, String nombreContacto, String numeroContacto, String familiaIds) {
        this.cedula = cedula;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fechaRegistro = fechaRegistro;
        this.genero = genero;
        this.habitcion = habitcion;
        this.nombreContacto = nombreContacto;
        this.numeroContacto = numeroContacto;
        this.familiaIds = familiaIds;
    }

    public String getHabitcion() {
        return habitcion;
    }

    public String getCedula() {
        return cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public String getGenero() {
        return genero;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public String getNumeroContacto() {
        return numeroContacto;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public void setHabitcion(String habitcion) {
        this.habitcion = habitcion;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public void setNumeroContacto(String numeroContacto) {
        this.numeroContacto = numeroContacto;
    }

    public String getFamiliaIds() {
        return familiaIds;
    }

    public void setFamiliaIds(String familiaIds) {
        this.familiaIds = familiaIds;
    }
}
