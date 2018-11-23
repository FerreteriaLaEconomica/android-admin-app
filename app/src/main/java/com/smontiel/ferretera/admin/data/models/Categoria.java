package com.smontiel.ferretera.admin.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Salvador Montiel on 15/11/18.
 */
public class Categoria {
    @SerializedName("id") @Expose
    public int id;
    @SerializedName("nombre") @Expose
    public String nombre;

    /**
     * No args constructor for use in serialization
     */
    public Categoria() {}

    public Categoria(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        if (id != categoria.id) return false;
        return nombre != null ? nombre.equals(categoria.nombre) : categoria.nombre == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
