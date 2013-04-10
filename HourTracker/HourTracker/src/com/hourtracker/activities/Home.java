package com.hourtracker.activities;

import com.hourtracker.R;

import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.content.Intent;

public class Home extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
	}

	/*
	 * When Add Hours is clicked, go to the Add Hours activity
	 */
	public void addHoursButtonClick(View theView) {
		Intent addHoursIntent = new Intent(this, AddHours.class);
		startActivity(addHoursIntent);
	}
	
	
	/*
	 * When Add Job is clicked, go to the Add Job activity
	 */
	public void addJobButtonClick(View theView) {
		Intent addJobIntent = new Intent(this, AddJob.class);
		startActivity(addJobIntent);
	}
	
	/*
	 * When View Stats is clicked, go to the View Stats activity
	 */
	public void viewHistoryButtonClick(View theView) {
		Intent viewStatsIntent = new Intent(this, ViewHistory.class);
		startActivity(viewStatsIntent);
	}

}
