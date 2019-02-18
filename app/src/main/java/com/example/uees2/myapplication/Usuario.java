package com.example.uees2.myapplication;

import java.io.Serializable;

public class Usuario  implements Serializable {
    private String userId;
    private String email;
    private String password;
    private String rol;
    private String playerId;

    public Usuario() {
    }

   /* public Usuario(String email, String password, int rol, String playerId) {
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.playerId = playerId;
    }*/



    public Usuario(String userId, String email, String password, String rol, String playerId) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.playerId = playerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
