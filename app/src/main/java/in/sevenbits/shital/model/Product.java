package in.sevenbits.shital.model;

public class Product {

    public long id;
    public String image;
    public String name;
    public boolean selected = false;

    public Product(long id, String image, String name) {
        this.id = id;
        this.image = image;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
