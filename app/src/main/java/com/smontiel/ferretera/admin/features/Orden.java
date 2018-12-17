package com.smontiel.ferretera.admin.features;

public class Orden {

    public String email;
    public String subtotal;
    public String estado_orden;
    public String envio;

    public Orden(String email, String subtotal) {
        this.email = email;
        this.subtotal = subtotal;
    }

    public Orden() {}
}
