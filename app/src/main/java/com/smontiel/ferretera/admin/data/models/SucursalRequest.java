package com.smontiel.ferretera.admin.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Salvador Montiel on 23/11/18.
 */

public class SucursalRequest {
    @SerializedName("nombre") @Expose
    public String nombre;
    @SerializedName("calle") @Expose
    public String calle;
    @SerializedName("numero_exterior") @Expose
    public String numeroExterior;
    @SerializedName("colonia") @Expose
    public String colonia;
    @SerializedName("codigo_postal") @Expose
    public String codigoPostal;
    @SerializedName("localidad") @Expose
    public String localidad;
    @SerializedName("municipio") @Expose
    public String municipio;
    @SerializedName("estado") @Expose
    public String estado;
    @SerializedName("email_administrador") @Expose
    public String emailAdmin;

    public SucursalRequest(String nombre, String calle, String numeroExterior, String colonia, String codigoPostal, String localidad, String municipio, String estado, String emailAdmin) {
        this.nombre = nombre;
        this.calle = calle;
        this.numeroExterior = numeroExterior;
        this.colonia = colonia;
        this.codigoPostal = codigoPostal;
        this.localidad = localidad;
        this.municipio = municipio;
        this.estado = estado;
        this.emailAdmin = emailAdmin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SucursalRequest that = (SucursalRequest) o;
        if (nombre != null ? !nombre.equals(that.nombre) : that.nombre != null) return false;
        if (calle != null ? !calle.equals(that.calle) : that.calle != null) return false;
        if (numeroExterior != null ? !numeroExterior.equals(that.numeroExterior) : that.numeroExterior != null)
            return false;
        if (colonia != null ? !colonia.equals(that.colonia) : that.colonia != null) return false;
        if (codigoPostal != null ? !codigoPostal.equals(that.codigoPostal) : that.codigoPostal != null)
            return false;
        if (localidad != null ? !localidad.equals(that.localidad) : that.localidad != null)
            return false;
        if (municipio != null ? !municipio.equals(that.municipio) : that.municipio != null)
            return false;
        if (estado != null ? !estado.equals(that.estado) : that.estado != null) return false;
        return emailAdmin != null ? emailAdmin.equals(that.emailAdmin) : that.emailAdmin == null;
    }

    @Override
    public int hashCode() {
        int result = nombre != null ? nombre.hashCode() : 0;
        result = 31 * result + (calle != null ? calle.hashCode() : 0);
        result = 31 * result + (numeroExterior != null ? numeroExterior.hashCode() : 0);
        result = 31 * result + (colonia != null ? colonia.hashCode() : 0);
        result = 31 * result + (codigoPostal != null ? codigoPostal.hashCode() : 0);
        result = 31 * result + (localidad != null ? localidad.hashCode() : 0);
        result = 31 * result + (municipio != null ? municipio.hashCode() : 0);
        result = 31 * result + (estado != null ? estado.hashCode() : 0);
        result = 31 * result + (emailAdmin != null ? emailAdmin.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SucursalRequest{" +
                "nombre='" + nombre + '\'' +
                ", calle='" + calle + '\'' +
                ", numeroExterior='" + numeroExterior + '\'' +
                ", colonia='" + colonia + '\'' +
                ", codigoPostal='" + codigoPostal + '\'' +
                ", localidad='" + localidad + '\'' +
                ", municipio='" + municipio + '\'' +
                ", estado='" + estado + '\'' +
                ", emailAdmin='" + emailAdmin + '\'' +
                '}';
    }
}
