package ua.internet.store.model;

public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private  String photo;
    private String country_id;
    private String city_id;
    private String author_id;
    private String color_id;
    private String firm_id;
    private String type_id;
    public Product(){}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getColor_id() {
        return color_id;
    }

    public void setColor_id(String color_id) {
        this.color_id = color_id;
    }

    public String getFirm_id() {
        return firm_id;
    }

    public void setFirm_id(String firm_id) {
        this.firm_id = firm_id;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public Product(int id, String name, String description, double price, String photo, String country_id, String city_id, String author_id, String color_id, String firm_id, String type_id) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.photo = photo;
        this.country_id = country_id;
        this.city_id = city_id;
        this.author_id = author_id;
        this.color_id = color_id;
        this.firm_id = firm_id;
        this.type_id = type_id;
    }
}
