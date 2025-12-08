package com.project.vistaro.repository;

import java.util.Optional;

import com.project.vistaro.model.Offer;

public interface OfferDao {

    Optional<Offer> findByCode(String code);
}
