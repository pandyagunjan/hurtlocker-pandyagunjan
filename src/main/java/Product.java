import java.util.Date;

public class Product {
    private String name;
    private Double price;
    private String type;
    private Date expiration;

    public Product()
    {
        this.name = "";
        this.price = 0d;
        this.type = "";
        this.expiration = null;
    }

    public Product(String name, Double price, String type, Date expiration) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.expiration = expiration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }


}
