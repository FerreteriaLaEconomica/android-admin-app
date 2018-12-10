package com.smontiel.ferretera.admin.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Salvador Montiel on 28/10/18.
 */
public class UserDto {
    @SerializedName("email") @Expose
    public String email;
    @SerializedName("password") @Expose
    public String password;
    @SerializedName("nombre") @Expose
    public String nombre;
    @SerializedName("apellidos") @Expose
    public String apellidos;
    @SerializedName("url_foto") @Expose
    public String urlFoto;
    @SerializedName("telefono") @Expose
    public String telefono;
    @SerializedName("direccion") @Expose
    public String direccion;

    /**
     * No args constructor for use in serialization
     */
    public UserDto() {}

    public UserDto(String nombre, String apellidos, String email, String password, String urlFoto, String telefono, String direccion) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.password = password;
        this.urlFoto = urlFoto;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        if (email != null ? !email.equals(userDto.email) : userDto.email != null) return false;
        if (password != null ? !password.equals(userDto.password) : userDto.password != null)
            return false;
        if (nombre != null ? !nombre.equals(userDto.nombre) : userDto.nombre != null) return false;
        if (apellidos != null ? !apellidos.equals(userDto.apellidos) : userDto.apellidos != null)
            return false;
        if (urlFoto != null ? !urlFoto.equals(userDto.urlFoto) : userDto.urlFoto != null)
            return false;
        if (telefono != null ? !telefono.equals(userDto.telefono) : userDto.telefono != null)
            return false;
        return direccion != null ? direccion.equals(userDto.direccion) : userDto.direccion == null;
    }

    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (apellidos != null ? apellidos.hashCode() : 0);
        result = 31 * result + (urlFoto != null ? urlFoto.hashCode() : 0);
        result = 31 * result + (telefono != null ? telefono.hashCode() : 0);
        result = 31 * result + (direccion != null ? direccion.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", urlFoto='" + urlFoto + '\'' +
                ", telefono='" + telefono + '\'' +
                ", direccion='" + direccion + '\'' +
                '}';
    }
}
