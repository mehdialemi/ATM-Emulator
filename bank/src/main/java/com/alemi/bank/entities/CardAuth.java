package com.alemi.bank.entities;

import com.alemi.common.models.auth.CardAuthOption;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Embeddable
public class CardAuth {

	@Enumerated(EnumType.ORDINAL)
	private CardAuthOption cardAuthOption = CardAuthOption.PIN;

	@Column
	private Integer unsuccessfulAttempts = 0;

	@Column
	private Boolean blocked = false;
}
