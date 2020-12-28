/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.appinvestigacion;

import java.util.logging.Logger;

/**
 *
 * @author yessica
 */
public class Producto {
    
    private Integer codigo;
    private String descripcion;
    private Integer iva;
    private Integer stock;
    private Integer precio;
    private String marca;


    public Producto(Integer codigo, String descripcion, Integer iva, Integer stock, Integer precio, String marca) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.iva = iva;
        this.stock = stock;
        this.precio = precio;
        this.marca = marca;
    }
    private static final Logger LOG = Logger.getLogger(Producto.class.getName());
  
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCodigo() {
        return codigo;
    }
    
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getIva() {
        return iva;
    }

    public Integer getStock() {
        return stock;
    }

    public Integer getPrecio() {
        return precio;
    }

    public String getMarca() {
        return marca;
    }

    public static Logger getLOG() {
        return LOG;
    }

    public void setIva(Integer iva) {
        this.iva = iva;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }


}
