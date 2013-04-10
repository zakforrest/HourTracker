package com.hourtracker.fragments;

import java.util.ArrayList;
import java.util.List;

import com.hourtracker.R;
import com.hourtracker.activities.AddHours;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class JobSelectorFragment extends DialogFragment {

	private List<String> jobNames;
	
	
	public static JobSelectorFragment newInstance(List<String> jobNames) {
		JobSelectorFragment theJobSelector = new JobSelectorFragment();
		theJobSelector.jobNames = jobNames;
		return theJobSelector;
	}
	
	
	//http://developer.android.com/guide/topics/ui/dialogs.html
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		if (savedInstanceState!=null)
			jobNames = savedInstanceState.getStringArrayList("jobNames");
		
		String jobNamesArray[] = jobNames.toArray(new String[jobNames.size()]);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.select_job_string)
			.setItems(jobNamesArray, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					((AddHours) getActivity()).setChosenJob(jobNames.get(which));
				}
			});
		return builder.create();
	}
	
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putStringArrayList("jobNames", (ArrayList<String>)jobNames);
		
		super.onSaveInstanceState(outState);
	}
	
}
