package com.hourtracker.fragments;

import java.util.Calendar;

import com.hourtracker.activities.AddHours;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

//http://developer.android.com/guide/topics/ui/controls/pickers.html
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

	private int setHour;
	private int setMinute;
	
	private String timePickerType;
	
	//http://developer.android.com/reference/android/app/DialogFragment.html#BasicDialog
	public static TimePickerFragment newInstance(String timePickerType, int savedHour, int savedMinute) {
		TimePickerFragment theTimePicker = new TimePickerFragment();
		
		theTimePicker.timePickerType = timePickerType;
		
		//If there is saved data...
		if (savedHour!=-1) {
			theTimePicker.setHour = savedHour;
			theTimePicker.setMinute = savedMinute;
		}
		else {
			//Get the current time
			final Calendar currTime = Calendar.getInstance();
			theTimePicker.setHour = currTime.get(Calendar.HOUR_OF_DAY);
			theTimePicker.setMinute = currTime.get(Calendar.MINUTE);
		}
		
		return theTimePicker;
	}
	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new TimePickerDialog(getActivity(), this, setHour, setMinute, DateFormat.is24HourFormat(getActivity()));
	}
	
	
	/*
	 * Send the chosen time back to AddHours
	 */
	public void onTimeSet(TimePicker view, int chosenHour, int chosenMinute) {
		((AddHours) getActivity()).setChosenTime(timePickerType, chosenHour, chosenMinute);
	}
}
