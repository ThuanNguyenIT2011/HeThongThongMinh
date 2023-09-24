package com.shopme.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table
@Entity(name = "currency")
public class Currency {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 64, nullable = false)
	private String name;
	
	@Column(length = 3, nullable = false)
	private String symbol;
	
	@Column(length = 4, nullable = false)
	private String code;

	public Currency() {	}
	
	public Currency(String name, String symbol, String code) {
		this.name = name;
		this.symbol = symbol;
		this.code = code;
	}

	public Currency(Integer id, String name, String symbol, String code) {
		this.id = id;
		this.name = name;
		this.symbol = symbol;
		this.code = code;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return  name + " - " + code + " - " + symbol;
	}
	
	
	
	
}
