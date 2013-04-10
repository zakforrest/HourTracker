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

import com.hourtracker.models.Job;

public class AddJobHttp {
	
	public static final int SUCCESS = 0;
	public static final int JOB_ALREADY_EXISTS = 1;
	public static final int ERROR = 2;

	public static int addJobToServer(Job createdJob) {
		
		HttpClient theHttpClient = new DefaultHttpClient();
		HttpPost thePost = new HttpPost(ServerInfo.serverUrl+"addJob.php");
		
		HttpResponse serverResponse;
		
		try {
			List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>(4);
			nameValuePairList.add(new BasicNameValuePair("name", createdJob.getName()));
			nameValuePairList.add(new BasicNameValuePair("payrate", createdJob.getPayrateString()));
			
			thePost.setEntity(new UrlEncodedFormEntity(nameValuePairList));
			
			serverResponse = theHttpClient.execute(thePost);
			
		} catch (Exception e) {
			return ERROR;
		}
		
		String httpResponseString = CommonHttp.getStringFromHttpResponse(serverResponse);
		
		if (!"Success".equals(httpResponseString)) {
			if (httpResponseString.contains("Job already exists."))
				return JOB_ALREADY_EXISTS;
			else
				return ERROR;
		}

		return SUCCESS;
	}
	
}
