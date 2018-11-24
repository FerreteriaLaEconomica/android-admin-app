package com.smontiel.ferretera.admin.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Salvador Montiel on 4/11/18.
 */
public class Producto {
    @SerializedName("id") @Expose
    public int id;
    @SerializedName("codigo_barras") @Expose
    public String codigoBarras;
    @SerializedName("nombre") @Expose
    public String nombre;
    @SerializedName("descripcion") @Expose
    public String descripcion;
    @SerializedName("url_foto") @Expose
    public String urlFoto;
    @SerializedName("formato") @Expose
    public String formato;
    @SerializedName("categoria") @Expose
    public String categoria;
    @SerializedName("precio_compra") @Expose
    private double precioCompra;
    @SerializedName("precio_venta") @Expose
    public double precioVenta;
    @SerializedName("porcentaje_descuento") @Expose
    public int descuento;

    /**
     * No args constructor for use in serialization
     */
    public Producto() {}

    public Producto(int id, String codigoBarras, String nombre, String descripcion, String urlFoto, String formato, String categoria, double precioVenta, int descuento) {
        this.id = id;
        this.codigoBarras = codigoBarras;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.urlFoto = urlFoto;
        this.formato = formato;
        this.categoria = categoria;
        this.precioCompra = 0.00;
        this.precioVenta = precioVenta;
        this.descuento = descuento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        if (id != producto.id) return false;
        if (Double.compare(producto.precioCompra, precioCompra) != 0) return false;
        if (Double.compare(producto.precioVenta, precioVenta) != 0) return false;
        if (Double.compare(producto.descuento, descuento) != 0) return false;
        if (codigoBarras != null ? !codigoBarras.equals(producto.codigoBarras) : producto.codigoBarras != null)
            return false;
        if (nombre != null ? !nombre.equals(producto.nombre) : producto.nombre != null)
            return false;
        if (descripcion != null ? !descripcion.equals(producto.descripcion) : producto.descripcion != null)
            return false;
        if (urlFoto != null ? !urlFoto.equals(producto.urlFoto) : producto.urlFoto != null)
            return false;
        if (formato != null ? !formato.equals(producto.formato) : producto.formato != null)
            return false;
        return categoria != null ? categoria.equals(producto.categoria) : producto.categoria == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + (codigoBarras != null ? codigoBarras.hashCode() : 0);
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (descripcion != null ? descripcion.hashCode() : 0);
        result = 31 * result + (urlFoto != null ? urlFoto.hashCode() : 0);
        result = 31 * result + (formato != null ? formato.hashCode() : 0);
        result = 31 * result + (categoria != null ? categoria.hashCode() : 0);
        temp = Double.doubleToLongBits(precioCompra);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(precioVenta);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(descuento);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", codigoBarras='" + codigoBarras + '\'' +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", urlFoto='" + urlFoto + '\'' +
                ", formato='" + formato + '\'' +
                ", categoria='" + categoria + '\'' +
                ", precioCompra=" + precioCompra +
                ", precioVenta=" + precioVenta +
                ", descuento=" + descuento +
                '}';
    }
}
