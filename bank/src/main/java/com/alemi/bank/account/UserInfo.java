package com.alemi.bank.account;

import com.alemi.bank.card.entities.Card;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table
public class UserInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column
	private String firstName;

	@Column
	private String lastName;

	@Column
	private String phoneNumber;

	@Column
	private String email;

	@Column
	private String address;

	@JsonIgnore
	@OneToMany(mappedBy = "userInfo", fetch = FetchType.LAZY)
	private Set<Card> cardSet = new HashSet <>();
}
