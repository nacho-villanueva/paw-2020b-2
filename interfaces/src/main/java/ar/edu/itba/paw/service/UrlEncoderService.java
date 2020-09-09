package ar.edu.itba.paw.service;

public interface UrlEncoderService {

    public String encode(long id);

    public long decode(String path);
}
