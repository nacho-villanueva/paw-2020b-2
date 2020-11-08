package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Media;

import java.util.Optional;

public interface ImageService {

    public Optional<Media> getMedicImage(int medicId, String attribute);

    public Optional<Media> getOrderImage(long orderId, String attribute);

    public Optional<Media> getResultImage(long resultId, long orderId, String attribute);
}
