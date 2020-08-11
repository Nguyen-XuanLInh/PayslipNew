package com.wata.payslip.model.dto;

import com.wata.payslip.model.entity.EmployeeEntity;

public class AccountDTO {

	private int id;
	private String username;
	private String password;
	private Boolean active = false;
	private String roles = "ROLE_USER";
	private String token;

	private EmployeeEntity employeeEntity;

	public AccountDTO() {

	}

	public AccountDTO(String email, String password, String token, String roles) {

		this.username = email;
		this.password = password;
		this.token = token;
		this.roles = roles;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String userName) {
		this.username = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public EmployeeEntity getEmployeeEntity() {
		return employeeEntity;
	}

	public void setEmployeeEntity(EmployeeEntity token) {
		this.employeeEntity = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	/*
	 * public EmployeeEntity getEmployeeEntity() { return employeeEntity; }
	 * 
	 * public void setEmployeeEntity(EmployeeEntity token) { this.employeeEntity =
	 * token; }
	 */
}
