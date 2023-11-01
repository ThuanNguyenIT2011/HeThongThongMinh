package com.shopme.order;

import java.util.*;

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
import com.shopme.guarantee.GuaranteeDto;
import com.shopme.guarantee.WrapperGuaranteeDto;
import com.shopme.setting.StateRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderService {
	public static final int ORDER_PER_PAGE = 3;
	@Autowired private OrderRepository orderRepository;
	@Autowired private StateRepository stateRepository;
	
	public Order createOrder(Customer customer, Address address, List<CartItem> cartItems,
			PaymentMethod paymentMethod, CheckoutInfo checkoutInfo) {
		System.out.println("có vào đây");
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
			

			Guarantee guarantee = new Guarantee(); 
			guarantee.setOrder(order);
			guarantee.setStartTime(new Date());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(guarantee.getStartTime()); 
			calendar.add(Calendar.DAY_OF_MONTH, 365 * 2); 
			Date endDate = calendar.getTime();
			guarantee.setEndDate(endDate); 
			guarantee.setQuality(detail.getQuantity());
			guarantee.setProduct(cartItem.getProduct());
			
			guarantees.add(guarantee); 
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
	public WrapperGuaranteeDto getGuarantees(int id) {
		try {
			Order order = orderRepository.findById(id).get(); 
			WrapperGuaranteeDto wrapperGuaranteeDto = new WrapperGuaranteeDto(); 
			
			List<Guarantee> guarantees =  order.getGuarantees();  
			List<GuaranteeDto> guaranteeDtos = new ArrayList<GuaranteeDto>(); 
			guarantees.forEach(g -> {
				GuaranteeDto guaranteeDto = new GuaranteeDto(); 
				guaranteeDto.guaranteeId = g.getId(); 
				guaranteeDto.guaranteeStartTime = g.getStartTime(); 
				guaranteeDto.guaranteeEndTime = g.getEndDate(); 	
				
				if (guaranteeDto.guaranteeEndTime.compareTo(new Date()) > 0) {
					guaranteeDto.stillUnderGuarantee = true; 
				}
				else {
					guaranteeDto.stillUnderGuarantee = false; 
				}
				guaranteeDto.productId = g.getProduct().getId(); 
				guaranteeDto.productName = g.getProduct().getName(); 
				guaranteeDtos.add(guaranteeDto);
			});
			wrapperGuaranteeDto.guarantees = guaranteeDtos; 
			wrapperGuaranteeDto.customerName = order.getFullName(); 
			wrapperGuaranteeDto.customerPhoneNumber = order.getPhoneNumber(); 
			wrapperGuaranteeDto.orderId = order.getId(); 
			
			return wrapperGuaranteeDto; 
		}
		catch (Exception e) {
			return null; 
		}

	}
}
