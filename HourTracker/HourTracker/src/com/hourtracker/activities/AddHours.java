package com.hourtracker.activities;

import java.util.Calendar;
import java.util.List;

import com.hourtracker.R;
import com.hourtracker.fragments.DatePickerFragment;
import com.hourtracker.fragments.JobSelectorFragment;
import com.hourtracker.fragments.TimePickerFragment;
import com.hourtracker.models.Hours;
import com.hourtracker.servercommunications.AddHoursHttp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.text.format.Time;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AddHours extends FragmentActivity {

	private static final int SLEEP_TIME_BETWEEN_QUERIES = 5000;
	private static final String[] ADDHOURS_SP_FIELDS = {"addHoursSavedDataExists", "addHoursJobName", 
		"addHoursStartTimeHour", "addHoursStartTimeMinute", "addHoursEndTimeHour", "addHoursEndTimeMinute", 
		"addHoursDateYear", "addHoursDateMonth", "addHoursDateDay"};
	
	private Hours createdHours;
	private List<String> jobNames;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_hours);
		
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		

		createdHours = new Hours();
		
		//Check for saved data and load it if it exists
		SharedPreferences sp = getPreferences(MODE_PRIVATE);
		if (sp.contains("addHoursSavedDataExists"))
			loadSavedHours(sp);
		
		//Create a new thread to get job names
		//http://stackoverflow.com/questions/11194663/extending-asynctaskvoid-void-void
		Runnable getJobNames = new Runnable() {
			@Override
			public void run() {
				while (jobNames==null) {
					jobNames = AddHoursHttp.getJobsFromServer();
					SystemClock.sleep(SLEEP_TIME_BETWEEN_QUERIES);
				}
			}
		};
		Thread getJobNamesThread = new Thread(getJobNames);
		getJobNamesThread.start();
	}
	
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		saveCurrentHours(getPreferences(MODE_PRIVATE));

		super.onSaveInstanceState(outState);
	}
	
	
	@Override
	public void onRestoreInstanceState(Bundle inState) {
		super.onRestoreInstanceState(inState);

		loadSavedHours(getPreferences(MODE_PRIVATE));
	}
	
	
	/*
	 * Loads saved data into createdHours
	 */
	private void loadSavedHours(SharedPreferences sp) {
		if (sp.contains("addHoursJobName")) {
			setChosenJob(sp.getString("addHoursJobName", null));
		}
		if (sp.contains("addHoursStartTimeHour")) {
			int savedHour = sp.getInt("addHoursStartTimeHour", 0);
			int savedMinute = sp.getInt("addHoursStartTimeMinute", 0);
			setChosenTime("startTime", savedHour, savedMinute);
		}
		if (sp.contains("addHoursEndTimeHour")) {
			int savedHour = sp.getInt("addHoursEndTimeHour", 0);
			int savedMinute = sp.getInt("addHoursEndTimeMinute", 0);
			setChosenTime("endTime", savedHour, savedMinute);
		}
		if (sp.contains("addHoursDateYear")) {
			int savedYear = sp.getInt("addHoursDateYear", 2013);
			int savedMonth = sp.getInt("addHoursDateMonth", 0);
			int savedDay = sp.getInt("addHoursDateDay", 1);
			setChosenDate(savedYear, savedMonth, savedDay);
		}
		clearAddHoursSP(sp);
	}
	
	
	/*
	 * Deletes the SharedPreferences stored for addHours
	 */
	private void clearAddHoursSP(SharedPreferences sp) {
		Editor spEditor = sp.edit();
		//remove all addHours data
		for (String currParameter: ADDHOURS_SP_FIELDS)
			spEditor.remove(currParameter);
		spEditor.commit();
	}
	
	
	private void saveCurrentHours(SharedPreferences sp) {
		SharedPreferences.Editor spEditor = sp.edit();
		spEditor.putBoolean("addHoursSavedDataExists", true);
		if (createdHours.getJobName()!=null) {
			spEditor.putString("addHoursJobName", createdHours.getJobName());
		}
		if (createdHours.getStartTime()!=null) {
			spEditor.putInt("addHoursStartTimeHour", createdHours.getStartTime().hour);
			spEditor.putInt("addHoursStartTimeMinute", createdHours.getStartTime().minute);
		}
		if (createdHours.getEndTime()!=null) {
			spEditor.putInt("addHoursEndTimeHour", createdHours.getEndTime().hour);
			spEditor.putInt("addHoursEndTimeMinute", createdHours.getEndTime().minute);
		}
		if (createdHours.getTheDate()!=null) {
			spEditor.putInt("addHoursDateYear", createdHours.getTheDate().get(Calendar.YEAR));
			spEditor.putInt("addHoursDateMonth", createdHours.getTheDate().get(Calendar.MONTH));
			spEditor.putInt("addHoursDateDay", createdHours.getTheDate().get(Calendar.DATE));
		}
		spEditor.commit();
		
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	//http://developer.android.com/guide/topics/ui/dialogs.html
	public void selectJobButtonClick(View theView) {
		if (jobNames!=null) {
			JobSelectorFragment theJobSelector = JobSelectorFragment.newInstance(jobNames);
			theJobSelector.show(getSupportFragmentManager(), "jobSelector");
		}
		else
			Toast.makeText(this, this.getString(R.string.error_loading_jobs_message), Toast.LENGTH_SHORT).show();
	}
	
	
	public void setChosenJob(String jobName) {
		createdHours.setJobName(jobName);
		Button selectJobButton = (Button)findViewById(R.id.selectJobButton);
		selectJobButton.setText(createdHours.getJobName());
	}
	
	
	/*
	 * Open a timePicker for the startTime and then set the button name to the chosen time.
	 */
	public void selectStartTimeButtonClick(View theView) {
		TimePickerFragment theTimePicker = createTimePickerFragment("startTime", createdHours.getStartTime());
		theTimePicker.show(getSupportFragmentManager(), "startPicker");
	}
	
	
	/*
	 * Open timePicker for the endTime and then set the button name to the chosen time
	 */
	public void selectEndTimeButtonClick(View theView) {
		TimePickerFragment theTimePicker = createTimePickerFragment("endTime", createdHours.getEndTime());
		theTimePicker.show(getSupportFragmentManager(), "endPicker");
	}
	
	
	/*
	 * Creates a TimePickerFragment, using already exiting data if possible
	 */
	private TimePickerFragment createTimePickerFragment(String timePickerType, Time existingData) {
		if (existingData!=null) {
			return TimePickerFragment.newInstance(timePickerType, existingData.hour, existingData.minute);
		}
		else
			return TimePickerFragment.newInstance(timePickerType, -1, -1);
	}
	
	
	public void setChosenTime(String timePickerType, int chosenHour, int chosenMinute) {
		if (timePickerType == "startTime") {
			createdHours.setStartTime(chosenHour, chosenMinute);
			Button selectStartTimeButton = (Button)findViewById(R.id.selectStartTimeButton);
			selectStartTimeButton.setText(createdHours.getStartTimeString());
		}
		else {
			createdHours.setEndTime(chosenHour, chosenMinute);
			Button selectEndTimeButton = (Button)findViewById(R.id.selectEndTimeButton);
			selectEndTimeButton.setText(createdHours.getEndTimeString());
		}
	}
	
	
	/*
	 * Open datePicker and then set the button name to the chosen date
	 */
	public void selectDateButtonClick(View theView) {
		DatePickerFragment theDatePicker;
		Calendar existingData = createdHours.getTheDate();
		if (existingData!=null) {
			int savedYear = existingData.get(Calendar.YEAR);
			int savedMonth = existingData.get(Calendar.MONTH);
			int savedDay = existingData.get(Calendar.DATE);
			theDatePicker = DatePickerFragment.newInstance(savedYear, savedMonth, savedDay);
		}
		else
			theDatePicker = DatePickerFragment.newInstance(-1, -1, -1);
		theDatePicker.show(getSupportFragmentManager(), "datePicker");
	}
	
	
	public void setChosenDate(int chosenYear, int chosenMonth, int chosenDay) {
		createdHours.setTheDate(chosenYear, chosenMonth, chosenDay);
		Button selectDateButton = (Button)findViewById(R.id.selectDateButton);
		selectDateButton.setText(createdHours.getTheDateString());
	}
	
	
	/*
	 * Add new hours or give error message
	 */
	public void addButtonClick(View theView) {
		switch(createdHours.isFinished()) {
		case 0: 
			AddHoursToServerTask addHoursTask = new AddHoursToServerTask(this);
			addHoursTask.execute(createdHours);
			break;
		case 1: 
			Toast.makeText(this, this.getString(R.string.unfinished_hours_message), Toast.LENGTH_SHORT).show();
			break;
		case 2: 
			Toast.makeText(this, this.getString(R.string.incorrect_hours_message), Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
	
	
	/*
	 * Save the current choices for next time it is opened
	 */
	public void saveButtonClick(View theView) {
		//Use sharedPreferences to save whatever data exists
		SharedPreferences sp = getPreferences(MODE_PRIVATE);
		saveCurrentHours(sp);
		
		finish();
	}
	
	
	public void cancelButtonClick(View theView) {
		finish();
	}
	
	
	private class AddHoursToServerTask extends AsyncTask<Hours, Void, Boolean> {
		
		AddHours parentActivity;
		
		public AddHoursToServerTask(Activity parentActivity) {
			this.parentActivity = (AddHours)parentActivity;
		}
		
		@Override
		protected Boolean doInBackground(Hours... params) {
			return AddHoursHttp.addHoursToServer(params[0]);
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if (result)
				finish();
			else {
				Toast.makeText(parentActivity, R.string.error_submitting_hours_message, Toast.LENGTH_SHORT).show();
			}
		}
	}

}
