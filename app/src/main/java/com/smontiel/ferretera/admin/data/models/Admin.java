package com.smontiel.ferretera.admin.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Salvador Montiel on 30/10/18.
 */
public class Admin {
    @SerializedName("nombre") @Expose
    public String nombre;
    @SerializedName("apellidos") @Expose
    public String apellidos;
    @SerializedName("email") @Expose
    public String email;
    @SerializedName("url_oto") @Expose
    public String urlFoto;
    @SerializedName("telefono") @Expose
    public String telefono;

    /**
     * No args constructor for use in serialization
     */
    public Admin() {}

    public Admin(String nombre, String apellidos, String email, String urlFoto, String telefono) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.urlFoto = urlFoto;
        this.telefono = telefono;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Admin admin = (Admin) o;
        if (nombre != null ? !nombre.equals(admin.nombre) : admin.nombre != null) return false;
        if (apellidos != null ? !apellidos.equals(admin.apellidos) : admin.apellidos != null)
            return false;
        if (email != null ? !email.equals(admin.email) : admin.email != null) return false;
        if (urlFoto != null ? !urlFoto.equals(admin.urlFoto) : admin.urlFoto != null) return false;
        return telefono != null ? telefono.equals(admin.telefono) : admin.telefono == null;
    }

    @Override
    public int hashCode() {
        int result = nombre != null ? nombre.hashCode() : 0;
        result = 31 * result + (apellidos != null ? apellidos.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (urlFoto != null ? urlFoto.hashCode() : 0);
        result = 31 * result + (telefono != null ? telefono.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", email='" + email + '\'' +
                ", urlFoto='" + urlFoto + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}
