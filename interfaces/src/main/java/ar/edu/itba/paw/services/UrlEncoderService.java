package ar.edu.itba.paw.services;

public interface UrlEncoderService {

    public String encode(long id);

    public long decode(String path);
}
