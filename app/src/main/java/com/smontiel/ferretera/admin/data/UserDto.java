package com.smontiel.ferretera.admin.data;

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

    /**
     * No args constructor for use in serialization
     */
    public UserDto() {}

    public UserDto(String nombre, String apellidos, String email, String password, String urlFoto, String telefono) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.password = password;
        this.urlFoto = urlFoto;
        this.telefono = telefono;
    }
}
