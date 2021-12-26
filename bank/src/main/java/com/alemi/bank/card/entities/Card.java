package com.alemi.bank.card.entities;

import com.alemi.bank.account.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table
public class Card {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(unique =  true, nullable = false)
	private String cardNumber;

	@Column
	private BigDecimal balance = BigDecimal.ZERO;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column
	private Integer pinCode;

	@Column
	private String fingerprint;

	@Embedded
	@Column(nullable = false)
	private CardAuth cardAuth;

	@JsonIgnore
	@OneToMany(mappedBy = "card", fetch = FetchType.LAZY)
	private Set<Transaction> transactions = new HashSet<>();

}
