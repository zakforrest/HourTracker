package com.hourtracker.servercommunications;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import com.hourtracker.models.Hours;
import com.hourtracker.servercommunications.CommonHttp;

public class AddHoursHttp {
	
	/*
	 * Adds a newly created hours to the server
	 */
	//http://stackoverflow.com/questions/2938502/sending-post-data-in-android
	public static boolean addHoursToServer(Hours createdHours) {
		
		HttpClient theHttpClient = new DefaultHttpClient();
		HttpPost thePost = new HttpPost(ServerInfo.serverUrl+"addHours.php");
		
		HttpResponse serverResponse;
		
		try {
			List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>(4);
			nameValuePairList.add(new BasicNameValuePair("startTime", createdHours.getStartTimeString()));
			nameValuePairList.add(new BasicNameValuePair("endTime", createdHours.getEndTimeString()));
			nameValuePairList.add(new BasicNameValuePair("date", createdHours.getTheDateString()));
			nameValuePairList.add(new BasicNameValuePair("jobName", createdHours.getJobName()));
			
			thePost.setEntity(new UrlEncodedFormEntity(nameValuePairList));
			
			serverResponse = theHttpClient.execute(thePost);
			
		} catch (Exception e) {
			return false;
		}
		
		String httpResponseString = CommonHttp.getStringFromHttpResponse(serverResponse);
		
		if (httpResponseString.contains("Error:")) {
			return false;
		}
		
		return true;
	}
	
	
	/*
	 * Gets the names of jobs from the server
	 */
	//http://stackoverflow.com/questions/3058434/xml-parse-file-from-http
	public static List<String> getJobsFromServer() {

		Document theDocument;
		
		try {
			theDocument = CommonHttp.receiveDocument("getJobNames.php");
		} catch(Exception e) {
			return null;
		}
		
		NodeList jobNodes = theDocument.getElementsByTagName("job");
			
		List<String> jobNames = new ArrayList<String>();
					
		for (int currNode=0; currNode<jobNodes.getLength(); currNode++) {
			NamedNodeMap jobAttributes = jobNodes.item(currNode).getAttributes();
			String jobName = jobAttributes.getNamedItem("jobName").getNodeValue();
			jobNames.add(jobName);
		}
				
		return jobNames;
	}
	

	
}
