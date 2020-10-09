package ar.edu.itba.paw.services;

import ar.edu.itba.paw.model.Image;

import java.util.Optional;

public interface ImageService {

    public Optional<Image> getMedicImage(int medicId, String attribute);

    public Optional<Image> getOrderImage(long orderId, String attribute);

    public Optional<Image> getResultImage(long resultId, long orderId, String attribute);
}
