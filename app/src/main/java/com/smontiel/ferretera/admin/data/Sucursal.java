package com.smontiel.ferretera.admin.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Salvador Montiel on 30/10/18.
 */
public class Sucursal {
    @SerializedName("id") @Expose
    public int id;
    @SerializedName("nombre") @Expose
    public String nombre;
    @SerializedName("calle") @Expose
    public String calle;
    @SerializedName("numeroExterior") @Expose
    public String numeroExterior;
    @SerializedName("colonia") @Expose
    public String colonia;
    @SerializedName("codigoPostal") @Expose
    public String codigoPostal;
    @SerializedName("localidad") @Expose
    public String localidad;
    @SerializedName("municipio") @Expose
    public String municipio;
    @SerializedName("estado") @Expose
    public String estado;
    @SerializedName("administrador") @Expose
    public Admin admin;

    /**
     * No args constructor for use in serialization
     */
    public Sucursal() {}

    public Sucursal(int id, String nombre, String calle, String numeroExterior, String colonia, String codigoPostal, String localidad, String municipio, String estado, Admin admin) {
        this.id = id;
        this.nombre = nombre;
        this.calle = calle;
        this.numeroExterior = numeroExterior;
        this.colonia = colonia;
        this.codigoPostal = codigoPostal;
        this.localidad = localidad;
        this.municipio = municipio;
        this.estado = estado;
        this.admin = admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sucursal sucursal = (Sucursal) o;
        if (id != sucursal.id) return false;
        if (nombre != null ? !nombre.equals(sucursal.nombre) : sucursal.nombre != null)
            return false;
        if (calle != null ? !calle.equals(sucursal.calle) : sucursal.calle != null) return false;
        if (numeroExterior != null ? !numeroExterior.equals(sucursal.numeroExterior) : sucursal.numeroExterior != null)
            return false;
        if (colonia != null ? !colonia.equals(sucursal.colonia) : sucursal.colonia != null)
            return false;
        if (codigoPostal != null ? !codigoPostal.equals(sucursal.codigoPostal) : sucursal.codigoPostal != null)
            return false;
        if (localidad != null ? !localidad.equals(sucursal.localidad) : sucursal.localidad != null)
            return false;
        if (municipio != null ? !municipio.equals(sucursal.municipio) : sucursal.municipio != null)
            return false;
        if (estado != null ? !estado.equals(sucursal.estado) : sucursal.estado != null)
            return false;
        return admin != null ? admin.equals(sucursal.admin) : sucursal.admin == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (calle != null ? calle.hashCode() : 0);
        result = 31 * result + (numeroExterior != null ? numeroExterior.hashCode() : 0);
        result = 31 * result + (colonia != null ? colonia.hashCode() : 0);
        result = 31 * result + (codigoPostal != null ? codigoPostal.hashCode() : 0);
        result = 31 * result + (localidad != null ? localidad.hashCode() : 0);
        result = 31 * result + (municipio != null ? municipio.hashCode() : 0);
        result = 31 * result + (estado != null ? estado.hashCode() : 0);
        result = 31 * result + (admin != null ? admin.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Sucursal{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", calle='" + calle + '\'' +
                ", numeroExterior='" + numeroExterior + '\'' +
                ", colonia='" + colonia + '\'' +
                ", codigoPostal='" + codigoPostal + '\'' +
                ", localidad='" + localidad + '\'' +
                ", municipio='" + municipio + '\'' +
                ", estado='" + estado + '\'' +
                ", admin=" + admin +
                '}';
    }
}
