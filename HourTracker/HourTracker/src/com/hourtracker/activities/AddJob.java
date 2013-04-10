package com.hourtracker.activities;

import com.hourtracker.MoneyInputFilter;
import com.hourtracker.R;
import com.hourtracker.models.Job;
import com.hourtracker.servercommunications.AddJobHttp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.text.InputFilter;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class AddJob extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_job);
		
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
			
		EditText payrateEditText = (EditText)findViewById(R.id.jobPayrateEditText);
		InputFilter[] theFilters = new InputFilter[] {new MoneyInputFilter(), new InputFilter.LengthFilter(8)};
		payrateEditText.setFilters(theFilters);
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
	
	
	/*
	 * Add to jobs when clicked
	 */
	public void addButtonClick(View theView) {
		String jobName = ((EditText)findViewById(R.id.jobNameEditText)).getText().toString();
		String jobPayrateString = ((EditText)findViewById(R.id.jobPayrateEditText)).getText().toString();
		Job createdJob = new Job(jobName, jobPayrateString);
		if (createdJob.isValid()) {
			AddJobToServerTask addJobTask = new AddJobToServerTask(this);
			addJobTask.execute(createdJob);
		}
		else
			Toast.makeText(this, this.getString(R.string.unfinished_job_message), Toast.LENGTH_SHORT).show();
	}
	
	
	/*
	 * Return to home
	 */
	public void cancelButtonClick(View theView) {
		finish();
	}
	
	
	private class AddJobToServerTask extends AsyncTask<Job, Void, Integer> {
		
		AddJob parentActivity;
		
		public AddJobToServerTask(Activity parentActivity) {
			this.parentActivity = (AddJob)parentActivity;
		}
		
		@Override
		protected Integer doInBackground(Job... params) {
			return AddJobHttp.addJobToServer(params[0]);
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			switch(result) {
			case AddJobHttp.SUCCESS:
				finish();
				break;
			case AddJobHttp.JOB_ALREADY_EXISTS:
				Toast.makeText(parentActivity, R.string.job_already_exists_message, Toast.LENGTH_SHORT).show();
				break;
			case AddJobHttp.ERROR:
				Toast.makeText(parentActivity, R.string.error_submitting_job_message, Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}		
	}
	
}
