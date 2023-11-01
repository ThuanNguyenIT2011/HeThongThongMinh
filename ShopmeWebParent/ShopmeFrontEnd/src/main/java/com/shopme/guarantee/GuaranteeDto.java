package com.shopme.guarantee;

import java.util.Date;

public class GuaranteeDto {
	public int guaranteeId; 
	public int productId; 	
	public String productName; 
	public int quality;
	public Date guaranteeStartTime; 
	public Date guaranteeEndTime;
	public boolean stillUnderGuarantee; 
}
