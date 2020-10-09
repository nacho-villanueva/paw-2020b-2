package ar.edu.itba.paw.model;

public class Image {

    private String name;
    private byte[] image;
    private String contentType;

    public Image(){

    }

    public Image(String name, byte[] image, String contentType) {
        this.name = name;
        this.image = image;
        this.contentType = contentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
