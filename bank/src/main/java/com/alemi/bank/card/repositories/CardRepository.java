package com.alemi.bank.card.repositories;

import com.alemi.bank.card.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

	Optional<Card> findByCardNumber(String cardNumber);
}
