package com.alemi.bank.entities;

import com.alemi.common.models.transaction.OperationType;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Enumerated(EnumType.ORDINAL)
	@Column
	private OperationType operationType;

	@Column(unique = true)
	private String transactionId;

	@Column
	private BigDecimal amount;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date = new Date();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cardId", referencedColumnName = "id", nullable = false)
	private Card card;

	@Column
	private String destination;

}
