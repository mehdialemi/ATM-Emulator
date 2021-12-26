package com.alemi.bank.card.entities;

import com.alemi.bank.card.models.CardAuthOption;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
public class CardAuth {

	@Column
	private CardAuthOption option = CardAuthOption.PIN;

	@Column
	private Integer unsuccessfulAttempts = 0;

	@Column
	private Boolean blocked = false;
}
