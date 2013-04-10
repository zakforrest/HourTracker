package com.hourtracker.adapters;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hourtracker.R;

public class JobNamesListAdapter extends ArrayAdapter<String> {

	List<String> jobNames;
	
	public JobNamesListAdapter(Activity parentActivity, List<String> jobNames) {
		super(parentActivity, 0, jobNames);
		this.jobNames = jobNames;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Activity parentActivity = (Activity) getContext();
		LayoutInflater inflater = parentActivity.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.list_item_job, null);
		
		TextView jobName = (TextView) rowView.findViewById(R.id.list_item_job_name);
		jobName.setText(jobNames.get(position));
		
		return rowView;
	}
}
