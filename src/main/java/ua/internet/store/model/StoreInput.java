package ua.internet.store.model;

public class StoreInput {
    private String type;
    private String country;
    private String city;
    private String author;

    public StoreInput(){}

    public StoreInput(String type, String country, String city, String author) {
        this.type = type;
        this.country = country;
        this.city = city;
        this.author = author;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
