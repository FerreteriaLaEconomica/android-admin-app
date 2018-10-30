package com.smontiel.ferretera.admin.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Salvador Montiel on 28/10/18.
 */
public class ApiError {
    @Expose
    public String error;
    @Expose
    public String message;
}
