package ua.internet.store.model;

public class UpperFilter {
    private String type;
    private String country;
    private String city;
    private String author;
    private double maxPrice;
    private double minPrice;

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public UpperFilter(String type, String country, String city, String author, double minPrice, double maxPrice) {
        this.type = type;
        this.country = country;
        this.city = city;
        this.author = author;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }


    public UpperFilter(){}

    public UpperFilter(String type, String country, String city, String author) {
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
