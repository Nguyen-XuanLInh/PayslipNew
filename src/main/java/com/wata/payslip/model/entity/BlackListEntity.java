package com.wata.payslip.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "blacklist")
public class BlackListEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Long id;
	@Column(name = "token", nullable = false)
	private String token;

	public BlackListEntity(Long id, String token) {
		this.id = id;
		this.token = token;
	}

	public BlackListEntity() {
		// TODO Auto-generated constructor stub
	}

	@Column(name = "token", nullable = false)
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
