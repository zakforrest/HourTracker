package com.hourtracker.fragments;

import com.hourtracker.R;
import com.hourtracker.adapters.HoursListAdapter;
import com.hourtracker.models.JobData;
import com.hourtracker.servercommunications.ViewHistoryHttp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

/*
 * A fragment that displays data about a job along with a list of all the hours
 * recorded for that specific job
 */
public class JobViewFragment extends DialogFragment implements OnItemLongClickListener {

	private String jobName;
	private View theView;
	private JobData theJobData;
	
	/*
	 * Constructor
	 */
	public static JobViewFragment newInstance(String jobName) {
		JobViewFragment theJobViewer = new JobViewFragment();
		
		theJobViewer.jobName = jobName;
		
		return theJobViewer;
	}
	
	
	/*
	 * Creates the fragment from the xml layout and loads the hours from the server
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		if (savedInstanceState!=null)
			jobName = savedInstanceState.getString("jobName");
		
		AlertDialog.Builder theBuilder = new AlertDialog.Builder(getActivity());
		//Give the dialog a title and an 'Ok' button
		theBuilder.setTitle(jobName+" History")
			.setCancelable(false)
			.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
	                
				}
			});

		theView = getActivity().getLayoutInflater().inflate(R.layout.fragment_job_history, null);
		
		loadHours();
		
		theBuilder.setView(theView);
		Dialog theDialog = theBuilder.create();
		return theDialog;
	}
	
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString("jobName", jobName);
		
		super.onSaveInstanceState(outState);
	}
	
	
	/*
	 * Loads the hours from the server using the asynctask GetHoursFromServerTask
	 */
	private void loadHours() {
		GetHoursFromServerTask getHoursTask = new GetHoursFromServerTask(this);
		getHoursTask.execute();
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parentAdapter, View view, int position, long id) {
		final DialogFragment currFragment = this;
		final int currHoursId = theJobData.hours.get(position).getId();
		
		AlertDialog.Builder theBuilder = new AlertDialog.Builder(currFragment.getActivity());
		theBuilder.setMessage("Would you like to delete these hours?")
			.setCancelable(false)
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
	                DeleteHoursFromServerTask deleteJobTask = new DeleteHoursFromServerTask(currFragment);
	                deleteJobTask.execute(currHoursId);
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
	 * Gets data about a job from the server and displays them on screen
	 */
	//Fix for screen rotate during task
	//http://stackoverflow.com/questions/3821423/background-task-progress-dialog-orientation-change-is-there-any-100-working/3821998#3821998
	private class GetHoursFromServerTask extends AsyncTask<Void, Void, JobData> {

		//The parent fragment
		JobViewFragment parent;
		
		//Constructor, saves the parent activity
		public GetHoursFromServerTask(DialogFragment parent) {
			this.parent = (JobViewFragment)parent;
		}
		
		//Returns a JobFragmentData as received from the server
		@Override
		protected JobData doInBackground(Void... params) {
			return ViewHistoryHttp.getJobDataFromServer(parent.jobName);
		}
		
		//Display the job stats and the hours in the hours list
		@Override
		protected void onPostExecute(JobData theJobData) {
			if (theJobData!=null) {
				parent.theJobData = theJobData;
				
				TextView totalHoursDisplay = (TextView)parent.theView.findViewById(R.id.job_history_total_hours);
				totalHoursDisplay.setText("Total Hours: " + theJobData.totalHours.toString());
				
				TextView totalPayDisplay = (TextView)parent.theView.findViewById(R.id.job_history_total_pay);
				totalPayDisplay.setText("Total Pay: " + theJobData.totalPay.toString());
				
				ListView theList = (ListView)parent.theView.findViewById(R.id.job_history_hours_list);
				theList.setAdapter(new HoursListAdapter(parent.getActivity(), theJobData.hours));
				theList.setOnItemLongClickListener(parent);
			}
		}
		
	}
	
	private class DeleteHoursFromServerTask extends AsyncTask<Integer, Void, Boolean> {

		JobViewFragment parent;
		
		public DeleteHoursFromServerTask(DialogFragment parent) {
			this.parent = (JobViewFragment)parent;
		}
		
		@Override
		protected Boolean doInBackground(Integer... id) {
			return ViewHistoryHttp.deleteHoursFromServer(id[0]);
		}
		
		@Override
		protected void onPostExecute(Boolean wasSuccessful) {
			if (wasSuccessful)
				parent.loadHours();
		}
		
	}
	
}
