package com.codemort.minimarket.model;

public class OrderVo {
    Integer id;
    String name_product;
    String cant_product;
    Integer prov_id;
    String date_order;

    Integer id_prov;
    String name_prov;
    String last_name_prov;
    String email_prov;
    String phone_prov;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName_product() {
        return name_product;
    }

    public void setName_product(String name_product) {
        this.name_product = name_product;
    }

    public String getCant_product() {
        return cant_product;
    }

    public void setCant_product(String cant_product) {
        this.cant_product = cant_product;
    }

    public Integer getProv_id() {
        return prov_id;
    }

    public void setProv_id(Integer prov_id) {
        this.prov_id = prov_id;
    }

    public String getDate_order() {
        return date_order;
    }

    public void setDate_order(String date_order) {
        this.date_order = date_order;
    }

    public Integer getId_prov() {
        return id_prov;
    }

    public void setId_prov(Integer id_prov) {
        this.id_prov = id_prov;
    }

    public String getName_prov() {
        return name_prov;
    }

    public void setName_prov(String name_prov) {
        this.name_prov = name_prov;
    }

    public String getLast_name_prov() {
        return last_name_prov;
    }

    public void setLast_name_prov(String last_name_prov) {
        this.last_name_prov = last_name_prov;
    }

    public String getEmail_prov() {
        return email_prov;
    }

    public void setEmail_prov(String email_prov) {
        this.email_prov = email_prov;
    }

    public String getPhone_prov() {
        return phone_prov;
    }

    public void setPhone_prov(String phone_prov) {
        this.phone_prov = phone_prov;
    }
}
