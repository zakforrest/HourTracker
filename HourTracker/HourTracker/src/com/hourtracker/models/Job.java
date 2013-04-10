package com.hourtracker.models;

import java.math.BigDecimal;

public class Job {

	private String name;
	private BigDecimal payrate;
	
	
	public Job (String name, String payrateString) {
		this.name = name;
		try {
			payrate = new BigDecimal(payrateString);
		} catch (Exception e) {
			payrate = null;
		}
	}
	
	public boolean isValid() {
		if (name!=null&&payrate!=null)
			return true;
		return false;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPayrateString() {
		return payrate.toString();
	}
	
}
