package com.hourtracker.servercommunications;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;


public class CommonHttp {
	
	//http://thinkandroid.wordpress.com/2009/12/30/getting-response-body-of-httpresponse/
	public static String getStringFromHttpResponse(HttpResponse theResponse) {
		String httpResponseString;
		try {
			HttpEntity responseEntity = theResponse.getEntity();
			httpResponseString = translateEntityToString(responseEntity);
		} catch (Exception e) {
			return "Error: "+e.toString();
		}
		return httpResponseString;
	}
	
	
	//http://thinkandroid.wordpress.com/2009/12/30/getting-response-body-of-httpresponse/
	private static String translateEntityToString(final HttpEntity responseEntity) throws Exception {
		if (responseEntity==null)
			throw new IllegalArgumentException("HTTP entity may not be null");
		InputStream inStream = responseEntity.getContent();
		if (inStream==null)
			return "Error: No response in string";
		Reader streamReader = new InputStreamReader(inStream, HTTP.DEFAULT_CONTENT_CHARSET);
		StringBuilder builderBuffer = new StringBuilder();
		try {
			char[] buffer = new char[1024];
			int length;
			while ((length = streamReader.read(buffer))!=-1)
				builderBuffer.append(buffer, 0, length);
		} finally {
			streamReader.close();
		}
		return builderBuffer.toString();
	}
	
	/*
	 * Passes the specified value to the url and returns true if successful, false otherwise
	 */
	public static boolean passValueToServer(String value, String url) {
		Document theDocument;

		try {
			String encodedValue = URLEncoder.encode(value, "utf-8");
			theDocument = CommonHttp.receiveDocument(url+encodedValue);
		} catch(Exception e) {
			return false;
		}
		
		String answerMessage = CommonHttp.getStringResultFromDocument(theDocument);
		
		if (!"Success".equals(answerMessage))
			return false;

		return true;
	}
	
	//http://stackoverflow.com/questions/3058434/xml-parse-file-from-http
	public static Document receiveDocument(String phpFile) throws Exception {
		URL getJobsURL = new URL(ServerInfo.serverUrl+phpFile);
		HttpURLConnection theConnection = (HttpURLConnection) getJobsURL.openConnection();
		return parseXML(theConnection.getInputStream());
	}
	
	
	//http://stackoverflow.com/questions/3058434/xml-parse-file-from-http
	private static Document parseXML(InputStream theStream) throws Exception {
		DocumentBuilderFactory theDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder theDocumentBuilder = theDocumentBuilderFactory.newDocumentBuilder();
		Document theDocument = theDocumentBuilder.parse(theStream);
		return theDocument;
	}
	
	public static String getStringResultFromDocument(Document theDocument) {
		NodeList answerNode = theDocument.getElementsByTagName("answer");
		NamedNodeMap answerAttribute = answerNode.item(0).getAttributes();
		return answerAttribute.getNamedItem("message").getNodeValue();
	}
	
}
