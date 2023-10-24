package com.shopme.order;

import java.util.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.checkout.CheckoutInfo;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Guarantee;
import com.shopme.common.entity.Order;
import com.shopme.common.entity.OrderDetail;
import com.shopme.common.entity.OrderStatus;
import com.shopme.common.entity.PaymentMethod;
import com.shopme.setting.StateRepository;

import guarantee.GuaranteeDao;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderService {
	public static final int ORDER_PER_PAGE = 3;
	@Autowired private OrderRepository orderRepository;
	@Autowired private StateRepository stateRepository;
	
	public Order createOrder(Customer customer, Address address, List<CartItem> cartItems,
			PaymentMethod paymentMethod, CheckoutInfo checkoutInfo) {
		Order order = new Order();
		order.setDateOrdertime(new Date());
		order.setOrderStatus(OrderStatus.NEW);
		order.setCustomer(customer);
		order.setTotal(checkoutInfo.getTotal());
		order.setShippingCost(checkoutInfo.getShippingCost());
		order.setDeliverDays(checkoutInfo.getDeliverDays());
		order.setDeliverDate(checkoutInfo.getDeliverDate());
		order.setPaymentMethod(paymentMethod);
		
		if (address == null) {
			order.copyAddressFromCustomer();
			order.setState(stateRepository.findByName(customer.getState()));
		} else {
			order.copyShippingAddress(address);
		}
		
		Set<OrderDetail> orderdetail = new HashSet<>();
		List<Guarantee> guarantees = new ArrayList<>(); 
		
		for (CartItem cartItem : cartItems) {
			OrderDetail detail = new OrderDetail();
			detail.setOrder(order);
			detail.setProduct(cartItem.getProduct());
			detail.setQuantity(cartItem.getQuantity());
			detail.setProductCost((float)cartItem.getProduct().getPriceDiscountPercent()); 
			
			int quality = cartItem.getQuantity(); 
			for (int i = 0; i < quality;i++) {
				Guarantee guarantee = new Guarantee(); 
				guarantee.setStartTime(new Date());
				 Calendar calendar = Calendar.getInstance();
				 calendar.setTime(guarantee.getStartTime()); 
				 calendar.add(Calendar.DAY_OF_MONTH, 365 * 2); // bao hanh 2 nam
				 Date endDate = calendar.getTime();
				 guarantee.setEndDate(endDate); 
				 guarantee.setProduct(cartItem.getProduct());
				 guarantees.add(guarantee); 
			}
			
			orderdetail.add(detail);
		}
		order.setOrderDetails(orderdetail);
		order.setGuarantees(guarantees);
		return orderRepository.save(order);
	}
	
	public Page<Order> listByPage(int pageNum, String sortDir, String sortField, String keyword,
			Customer customer){
		Sort sorted = Sort.by(sortField);
		sorted = sortDir.equals("asc") ? sorted.ascending() : sorted.descending();
		
		Pageable pageable = PageRequest.of(pageNum - 1, ORDER_PER_PAGE, sorted);
		
		if (keyword != null) {
 			return orderRepository.findAll(keyword, customer.getId(), pageable);
 		}
 		
		return orderRepository.findAll(pageable);
	}
	
	public Order get(Integer id) {
		return orderRepository.findById(id).get();
	}
	
	public void updateOrderStatusByID(Integer orderId, OrderStatus orderStatus) {
		orderRepository.updateStatus(orderId, orderStatus);
	}
	public List<Guarantee> getGuarantees(int id) {
		Order order = orderRepository.findById(id).get(); 
		return order.getGuarantees(); 
	}
}
