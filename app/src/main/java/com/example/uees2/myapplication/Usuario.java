package com.example.uees2.myapplication;

public class Usuario {
    private String email;
    private String password;
    private int rol;
    private String playerId;

    public Usuario() {
    }

    public Usuario(String email, String password, int rol, String playerId) {
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }
}
