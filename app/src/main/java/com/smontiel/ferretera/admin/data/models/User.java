package com.smontiel.ferretera.admin.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Salvador Montiel on 28/10/18.
 */
public class User {
    @SerializedName("email") @Expose
    public String email;
    @SerializedName("nombre") @Expose
    public String nombre;
    @SerializedName("apellidos") @Expose
    public String apellidos;
    @SerializedName("url_foto") @Expose
    public String urlFoto;
    @SerializedName("telefono") @Expose
    public String telefono;
    @SerializedName("is_super_admin") @Expose
    public boolean isSuperAdmin;

    /**
     * No args constructor for use in serialization
     */
    public User() {}

    public User(String nombre, String apellidos, String email, String urlFoto, String telefono, boolean isSuperAdmin) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.urlFoto = urlFoto;
        this.telefono = telefono;
        this.isSuperAdmin = isSuperAdmin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        if (isSuperAdmin != user.isSuperAdmin) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (nombre != null ? !nombre.equals(user.nombre) : user.nombre != null) return false;
        if (apellidos != null ? !apellidos.equals(user.apellidos) : user.apellidos != null)
            return false;
        if (urlFoto != null ? !urlFoto.equals(user.urlFoto) : user.urlFoto != null) return false;
        return telefono != null ? telefono.equals(user.telefono) : user.telefono == null;
    }

    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (apellidos != null ? apellidos.hashCode() : 0);
        result = 31 * result + (urlFoto != null ? urlFoto.hashCode() : 0);
        result = 31 * result + (telefono != null ? telefono.hashCode() : 0);
        result = 31 * result + (isSuperAdmin ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", urlFoto='" + urlFoto + '\'' +
                ", telefono='" + telefono + '\'' +
                ", isSuperAdmin=" + isSuperAdmin +
                '}';
    }
}
