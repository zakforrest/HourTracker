package com.hourtracker.fragments;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.hourtracker.activities.AddHours;

//http://developer.android.com/guide/topics/ui/controls/pickers.html
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

	private int setYear;
	private int setMonth;
	private int setDay;
	
	//http://developer.android.com/reference/android/app/DialogFragment.html#BasicDialog
	//http://www.kylebeal.com/2011/11/android-datepickerdialog-and-the-dialogfragment/
	public static DatePickerFragment newInstance(int savedYear, int savedMonth, int savedDay) {
		DatePickerFragment theDatePicker = new DatePickerFragment();
		
		//If there is no saved data...
		if (savedYear!=-1) {
			theDatePicker.setYear = savedYear;
			theDatePicker.setMonth = savedMonth;
			theDatePicker.setDay = savedDay;
		}
		else {
			//Get the current date
			final Calendar currDate = Calendar.getInstance();
			theDatePicker.setYear = currDate.get(Calendar.YEAR);
			theDatePicker.setMonth = currDate.get(Calendar.MONTH);
			theDatePicker.setDay = currDate.get(Calendar.DAY_OF_MONTH);
		}
		
		return theDatePicker;
	}
	
		
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new DatePickerDialog(getActivity(), this, setYear, setMonth, setDay);
	}
	
	
	/*
	 * Send the chosen date back to AddHours
	 */
	public void onDateSet(DatePicker view, int chosenYear, int chosenMonth, int chosenDay) {
		((AddHours) getActivity()).setChosenDate(chosenYear, chosenMonth, chosenDay);
	}
}
