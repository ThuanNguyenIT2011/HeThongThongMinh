package com.shopme.common.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="guarantee")
public class Guarantee {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	
	@Column(name="start_time")
	private Date startTime;
	@Column(name="end_time")
	private Date endDate;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "orders_id")
	private Order order; 
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="products_id")
	private Product product; 
	
	
	public Guarantee() {}
	
	
	public Guarantee(Date startTime, Date endDate, Order order, Product product) {
		this.startTime = startTime;
		this.endDate = endDate;
		this.order = order;
		this.product = product;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Date getStartTime() {
		return startTime;
	}


	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}


	public Date getEndDate() {
		return endDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	public Order getOrder() {
		return order;
	}


	public void setOrder(Order order) {
		this.order = order;
	}


	public Product getProduct() {
		return product;
	}


	public void setProduct(Product product) {
		this.product = product;
	}
	

}
