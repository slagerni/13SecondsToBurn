package com.thirteensecondstoburn.CasinoPractice.GooglePlay;

/**
 * Created by Nick on 4/1/2016.
 */
public class ProductDetails {
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String sku;
    private String type;
    private String price;
    private String title;
    private String description;

    public ProductDetails(String sku, String type, String price, String title, String description) {
        this.sku = sku;
        this.type = type;
        this.price = price;
        this.title = title;
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("Sku: %s Type: %s Price: %s Title [ %s ] Description [ %s ]", sku, type, price, title, description);
    }
}
