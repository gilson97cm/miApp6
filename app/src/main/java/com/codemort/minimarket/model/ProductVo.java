package com.codemort.minimarket.model;

public class ProductVo {
    Integer codProd;
    String fechaProd;
    String nombreProd;
    String detalleProd;
    String precioProd;
    Integer stockProd;

    String company;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Integer getCodProd() {
        return codProd;
    }

    public void setCodProd(Integer codProd) {
        this.codProd = codProd;
    }

    public String getFechaProd() {
        return fechaProd;
    }

    public void setFechaProd(String fechaProd) {
        this.fechaProd = fechaProd;
    }

    public String getNombreProd() {
        return nombreProd;
    }

    public void setNombreProd(String nombreProd) {
        this.nombreProd = nombreProd;
    }

    public String getDetalleProd() {
        return detalleProd;
    }

    public void setDetalleProd(String detalleProd) {
        this.detalleProd = detalleProd;
    }

    public String getPrecioProd() {
        return precioProd;
    }

    public void setPrecioProd(String precioProd) {
        this.precioProd = precioProd;
    }

    public Integer getStockProd() {
        return stockProd;
    }

    public void setStockProd(Integer stockProd) {
        this.stockProd = stockProd;
    }
}
