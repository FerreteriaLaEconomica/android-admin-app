package com.smontiel.ferretera.admin.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Salvador Montiel on 23/11/18.
 */

public class Inventario {
    @SerializedName("id") @Expose
    public int id;
    @SerializedName("producto") @Expose
    public Producto producto;
    @SerializedName("cantidad") @Expose
    public int cantidad;
    @SerializedName("id_sucursal") @Expose
    public int idSucursal;

    /**
     * No args constructor for use in serialization
     */
    public Inventario() {}

    public Inventario(int id, Producto producto, int cantidad, int idSucursal) {
        this.id = id;
        this.producto = producto;
        this.cantidad = cantidad;
        this.idSucursal = idSucursal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inventario that = (Inventario) o;
        if (id != that.id) return false;
        if (cantidad != that.cantidad) return false;
        if (idSucursal != that.idSucursal) return false;
        return producto != null ? producto.equals(that.producto) : that.producto == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (producto != null ? producto.hashCode() : 0);
        result = 31 * result + cantidad;
        result = 31 * result + idSucursal;
        return result;
    }

    @Override
    public String toString() {
        return "Inventario{" +
                "id=" + id +
                ", producto=" + producto +
                ", cantidad=" + cantidad +
                ", idSucursal=" + idSucursal +
                '}';
    }
}
