package com.hourtracker.models;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.Time;

public class Hours implements Parcelable {

	private static final String TIME_FORMAT = "%l:%M%P";
	
	
	private String jobName;
	private Time startTime;
	private Time endTime;
	private GregorianCalendar theDate;
	
	private BigDecimal payout;
	private int id;
	
	//Empty constructor
	public Hours() {
		
	}
	
	
	/*
	 * Returns true if Hours is all set up
	 */
	public int isFinished() {
		if (startTime==null||endTime==null||theDate==null||jobName==null)
			return 1;
		else if (endTime.before(startTime))
			return 2;
		return 0;
	}
	
	public String getJobName() {
		return jobName;
	}
	
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	
	/*
	 * Creates a Time from an int hour and minute
	 */
	private Time convertIntsToTime(int hour, int minute) {
		Time resultTime = new Time();
		resultTime.hour = hour;
		resultTime.minute = minute;
		return resultTime;
	}
	
	public Time getStartTime() {
		return startTime;
	}
	
	public String getStartTimeString() {
		return startTime.format(TIME_FORMAT);
	}
	
	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}
	
	public void setStartTime(int startHour, int startMinute) {
		this.startTime = convertIntsToTime(startHour, startMinute);
	}
	
	public Time getEndTime() {
		return endTime;
	}
	
	public String getEndTimeString() {
		return endTime.format(TIME_FORMAT);
	}
	
	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}
	
	public void setEndTime(int endHour, int endMinute) {
		this.endTime = convertIntsToTime(endHour, endMinute);
	}
	
	public String getTimeRangeString() {
		return getStartTimeString() + " - " + getEndTimeString();
	}
	
	/*
	 * Returns the number of hours worked
	 */
	public BigDecimal getHoursWorked() {
		//Convert start and end time to milliseconds
		long endTimeMillis = endTime.toMillis(false);
		long startTimeMillis = startTime.toMillis(false);
		
		long timeWorkedMillis = endTimeMillis - startTimeMillis;
		
		//Convert milliseconds to hours
		BigDecimal hoursWorked = new BigDecimal(TimeUnit.MILLISECONDS.toHours(timeWorkedMillis));
		
		return hoursWorked;
	}
	
	/*
	 * Creates a new GregorianCalendar from a year, month, and day
	 */
	private GregorianCalendar convertIntsToDate(int year, int month, int day) {
		return new GregorianCalendar(year, month, day, 0, 0, 0);
	}
	
	public GregorianCalendar getTheDate() {
		return theDate;
	}
	
	//Use SimpleDateFormat http://stackoverflow.com/questions/1755199/calendar-returns-wrong-month
	public String getTheDateString() {
		return (theDate.get(Calendar.MONTH)+1)+"/"+theDate.get(Calendar.DATE)+"/"+theDate.get(Calendar.YEAR);
	}
	
	public void setTheDate(GregorianCalendar theDate) {
		this.theDate = theDate;
	}
	
	public void setTheDate(int theYear, int theMonth, int theDay) {
		this.theDate = convertIntsToDate(theYear, theMonth, theDay);
	}
	
	public BigDecimal getPayout() {
		return payout;
	}
	
	public void setPayout(BigDecimal payrate) {
		payout = payrate.multiply(getHoursWorked());
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	
	//Implementing Parcelable
	public int describeContents() {
		return 0;
	}
	
	public void writeToParcel(Parcel out, int flags) {
		if (jobName!=null)
			out.writeString(jobName);
		else
			out.writeString("");
		
		if (startTime!=null)
			out.writeString(startTime.toString());
		else
			out.writeString("");
		
		if (endTime!=null)
			out.writeString(endTime.toString());
		else
			out.writeString("");
		
		if (theDate!=null)
			out.writeString(theDate.toString());
		else
			out.writeString("");
		
		if (payout!=null)
			out.writeString(payout.toString());
		else
			out.writeString("");
	}
	
	public static final Parcelable.Creator<Hours> CREATOR = new Parcelable.Creator<Hours>() {

		@Override
		public Hours createFromParcel(Parcel in) {
			return new Hours(in);
		}

		@Override
		public Hours[] newArray(int size) {
			return new Hours[size];
		}
	};

	private Hours(Parcel in) {
		jobName = in.readString();
		String startTimeString = in.readString();
		if (startTimeString!="")
			startTime = new Time(startTimeString);
		String endTimeString = in.readString();
		if (endTimeString!="")
			endTime = new Time(endTimeString);
		String theDateString = in.readString();
		//if (theDateString!="")
		//	theDate = new GregorianCalendar(theDateString);
		String payoutString = in.readString();
		if (payoutString!="")
			payout = new BigDecimal(payoutString);
	}
}
