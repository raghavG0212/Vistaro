package com.project.vistaro.repository;

import java.util.Optional;

import com.project.vistaro.model.GiftCard;

public interface GiftCardDao {

    Optional<GiftCard> findByCode(String code);

    int markRedeemed(Integer giftcardId);
}
