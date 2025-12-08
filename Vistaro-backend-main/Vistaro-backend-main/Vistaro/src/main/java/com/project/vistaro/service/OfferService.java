package com.project.vistaro.service;

import java.util.Map;

public interface OfferService {
    Map<String, Object> validateOffer(String code, double amount);
}
