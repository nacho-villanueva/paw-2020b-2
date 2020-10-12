package ar.edu.itba.paw.model;

public class Media {

    private String name;
    private byte[] file;
    private String contentType;

    public Media(){

    }

    public Media(String name, byte[] image, String contentType) {
        this.name = name;
        this.file = image;
        this.contentType = contentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] image) {
        this.file = image;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
