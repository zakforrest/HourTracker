package com.hourtracker.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/*
 * Return item from getJobDataFromServer function call in ViewHistoryHttp
 */
public class JobData {
	public List<Hours> hours;
	public BigDecimal totalHours;
	public BigDecimal totalPay;
	
	public JobData() {
		totalHours = new BigDecimal(0);
		totalPay = new BigDecimal(0);
		hours = new ArrayList<Hours>();
	}
	
}
