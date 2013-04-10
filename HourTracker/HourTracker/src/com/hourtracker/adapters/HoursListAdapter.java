package com.hourtracker.adapters;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hourtracker.R;
import com.hourtracker.models.Hours;

public class HoursListAdapter extends ArrayAdapter<Hours> {
	
	List<Hours> hours;
	
	public HoursListAdapter(Activity parent, List<Hours> hours) {
		super(parent, 0, hours);
		this.hours = hours;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Activity parentActivity = (Activity) getContext();
		LayoutInflater inflater = parentActivity.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.list_item_hours, null);
		
		Hours currHour = hours.get(position);

		TextView timeRange = (TextView) rowView.findViewById(R.id.list_item_hours_timerange);
		timeRange.setText(currHour.getTimeRangeString() + " on " + currHour.getTheDateString());
		
		TextView totalPay = (TextView) rowView.findViewById(R.id.history_item_hours_totalpay);
		totalPay.setText("Pay: " + currHour.getPayout().toString());
		
		return rowView;
	}
	
}
