package ar.edu.itba.paw.interfaces;

public interface UrlEncoderService {

    public String encode(long id);

    public long decode(String path);
}
