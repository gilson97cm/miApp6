package com.codemort.minimarket.model;

public class OrderVo {

    Integer id_pedido;
    String date_order;
    String empresaprov;
    String nombre_prove;
    String telefono_prove;
    Integer codProd;
    String nombreProd;
    String correo_prove;
    String cantidad_prod;

    public String getCantidad_prod() {
        return cantidad_prod;
    }

    public void setCantidad_prod(String cantidad_prod) {
        this.cantidad_prod = cantidad_prod;
    }

    public String getCorreo_prove() {
        return correo_prove;
    }

    public void setCorreo_prove(String correo_prove) {
        this.correo_prove = correo_prove;
    }

    public Integer getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(Integer id_pedido) {
        this.id_pedido = id_pedido;
    }

    public String getDate_order() {
        return date_order;
    }

    public void setDate_order(String date_order) {
        this.date_order = date_order;
    }

    public String getEmpresaprov() {
        return empresaprov;
    }

    public void setEmpresaprov(String empresaprov) {
        this.empresaprov = empresaprov;
    }

    public String getNombre_prove() {
        return nombre_prove;
    }

    public void setNombre_prove(String nombre_prove) {
        this.nombre_prove = nombre_prove;
    }

    public String getTelefono_prove() {
        return telefono_prove;
    }

    public void setTelefono_prove(String telefono_prove) {
        this.telefono_prove = telefono_prove;
    }

    public Integer getCodProd() {
        return codProd;
    }

    public void setCodProd(Integer codProd) {
        this.codProd = codProd;
    }

    public String getNombreProd() {
        return nombreProd;
    }

    public void setNombreProd(String nombreProd) {
        this.nombreProd = nombreProd;
    }
}
