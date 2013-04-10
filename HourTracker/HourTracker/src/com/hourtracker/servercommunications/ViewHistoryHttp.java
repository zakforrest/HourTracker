package com.hourtracker.servercommunications;

import java.math.BigDecimal;
import java.net.URLEncoder;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import com.hourtracker.models.Hours;
import com.hourtracker.models.JobData;


public class ViewHistoryHttp {
	

	/*
	 * Returns a JobData object for the given job name
	 */
	public static JobData getJobDataFromServer(String requestedJobName) {
		
		Document theDocument;
		
		try {
			String encodedJobName = URLEncoder.encode(requestedJobName, "utf-8");
			theDocument = CommonHttp.receiveDocument("getHours.php?jobName="+encodedJobName);
		} catch(Exception e) {
			return null;
		}
		
		NodeList hoursNodes = theDocument.getElementsByTagName("hoursItem");
			
		JobData theJobData = new JobData();
					
		for (int currNode=0; currNode<hoursNodes.getLength(); currNode++) {		
			NamedNodeMap hoursAttributes = hoursNodes.item(currNode).getAttributes();
			
			Hours hoursItem = createHoursFromNodeAttributes(hoursAttributes);
			
			theJobData.totalHours = theJobData.totalHours.add(hoursItem.getHoursWorked());
			theJobData.totalPay = theJobData.totalPay.add(hoursItem.getPayout());
			
			theJobData.hours.add(hoursItem);
		}
		
		return theJobData;
	}
	
	
	/*
	 * Creates an hours object from the attributes in a NamedNodeMap and returns it
	 */
	private static Hours createHoursFromNodeAttributes(NamedNodeMap hoursAttributes) {
		Hours hoursItem = new Hours();
		
		String idString = hoursAttributes.getNamedItem("hoursId").getNodeValue();
		hoursItem.setId(Integer.parseInt(idString));
		
		String startTimeString = hoursAttributes.getNamedItem("startTime").getNodeValue();
		String[] startTimePieces = startTimeString.split(":");
		hoursItem.setStartTime(Integer.parseInt(startTimePieces[0]), Integer.parseInt(startTimePieces[1]));
		
		String endTimeString = hoursAttributes.getNamedItem("endTime").getNodeValue();
		String[] endTimePieces = endTimeString.split(":");
		hoursItem.setEndTime(Integer.parseInt(endTimePieces[0]), Integer.parseInt(endTimePieces[1]));
		
		String dateString = hoursAttributes.getNamedItem("date").getNodeValue();
		String[] datePieces = dateString.split("/");
		hoursItem.setTheDate(Integer.parseInt(datePieces[2]), Integer.parseInt(datePieces[0]), Integer.parseInt(datePieces[1]));
		
		String jobName = hoursAttributes.getNamedItem("jobName").getNodeValue();
		hoursItem.setJobName(jobName);
		
		String jobPayrate = hoursAttributes.getNamedItem("jobPayrate").getNodeValue();
		hoursItem.setPayout(new BigDecimal(jobPayrate));
		
		return hoursItem;
	}
	

	/*
	 * Deletes the specifed job name from the server and returns true if successful and
	 * false otherwise
	 */
	public static boolean deleteJobFromServer(String jobName) {
		return CommonHttp.passValueToServer(jobName, "deleteJob.php?jobName=");
	}
	
	
	/*
	 * Deletes the specifed job name from the server and returns true if successful and
	 * false otherwise
	 */
	public static boolean deleteHoursFromServer(int id) {
		return CommonHttp.passValueToServer("" + id, "deleteHours.php?id=");
	}
	

}
