package com.hourtracker.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.hourtracker.R;
import com.hourtracker.adapters.JobNamesListAdapter;
import com.hourtracker.fragments.JobViewFragment;
import com.hourtracker.servercommunications.AddHoursHttp;
import com.hourtracker.servercommunications.ViewHistoryHttp;


public class ViewHistory extends FragmentActivity implements OnItemClickListener, OnItemLongClickListener {

	private List<String> jobNames;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_history);
		
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		jobNames = new ArrayList<String>();
		
		loadJobNames();
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

	
	@Override
	public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
		JobViewFragment theJobViewer = JobViewFragment.newInstance(jobNames.get(position));
		theJobViewer.show(getSupportFragmentManager(), "jobViewer");
	}
	
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parentAdapter, View view, int position, long id) {
		final Activity currActivity = this;
		final String currJobName = jobNames.get(position);
		
		AlertDialog.Builder theBuilder = new AlertDialog.Builder(currActivity);
		theBuilder.setMessage("Would you like to delete " + currJobName + "?")
			.setCancelable(false)
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
	                DeleteJobFromServerTask deleteJobTask = new DeleteJobFromServerTask(currActivity);
	                deleteJobTask.execute(currJobName);
				}
			})
			.setNegativeButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					
				}
			});
		
		Dialog theDialog = theBuilder.create();
		theDialog.show();
		
		return true;
	}
	
	
	/*
	 * Calls an asyctask to load the jobs from the database and
	 * display them on screen
	 */
	private void loadJobNames() {
		GetJobNamesFromServerTask getJobNamesTask = new GetJobNamesFromServerTask(this);
		getJobNamesTask.execute();
	}
	
	
	/*
	 * Gets a list of job names and displays them on screen
	 */
	private class GetJobNamesFromServerTask extends AsyncTask<Void, Void, List<String>> {
		
		ViewHistory parentActivity;
		
		public GetJobNamesFromServerTask(Activity parentActivity) {
			this.parentActivity = (ViewHistory)parentActivity;
		}
		
		@Override
		protected List<String> doInBackground(Void... params) {
			return AddHoursHttp.getJobsFromServer();
		}
		
		@Override
		protected void onPostExecute(List<String> jobNames) {
			parentActivity.jobNames = jobNames;
			
			if (jobNames!=null) {
				ListView theList = (ListView)findViewById(R.id.view_history_job_names_list);
				theList.setAdapter(new JobNamesListAdapter(parentActivity, jobNames));
				theList.setOnItemClickListener(parentActivity);
				theList.setOnItemLongClickListener(parentActivity);
				theList.setTag("jobNamesList");
			}
			
			else
				Toast.makeText(parentActivity, parentActivity.getString(R.string.error_loading_history_message), Toast.LENGTH_SHORT).show();
		}
		
	}
	
	
	private class DeleteJobFromServerTask extends AsyncTask<String, Void, Boolean> {

		ViewHistory parentActivity;
		
		public DeleteJobFromServerTask(Activity parentActivity) {
			this.parentActivity = (ViewHistory)parentActivity;
		}
		
		@Override
		protected Boolean doInBackground(String... jobName) {
			return ViewHistoryHttp.deleteJobFromServer(jobName[0]);
		}
		
		@Override
		protected void onPostExecute(Boolean wasSuccessful) {
			if (wasSuccessful)
				parentActivity.loadJobNames();
		}
		
	}


}
