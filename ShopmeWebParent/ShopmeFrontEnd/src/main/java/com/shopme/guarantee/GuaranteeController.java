package com.shopme.guarantee; 

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.order.OrderService;

@RestController
public class GuaranteeController {
	
	@Autowired
	private OrderService orderService; 
	
	
	
	
	@GetMapping("api/v1/guarantee/{orderId}")
	public ResponseEntity<WrapperGuaranteeDto> getGuarantes(@PathVariable int orderId){
		
		return new ResponseEntity<WrapperGuaranteeDto>(orderService.getGuarantees(orderId), HttpStatus.OK);  
	}

}
