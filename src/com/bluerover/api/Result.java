package com.bluerover.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Result {

	long page;
	long results;
	private ArrayList<JSONObject> list;
	long pages;
	private HttpUriRequest request;
	private HttpResponse response;
	private HttpUriRequest next;

	@SuppressWarnings("unchecked")
	public Result(HttpUriRequest pRequest, HttpResponse pResponse)
			throws ParseException, IOException {
		request = pRequest;
		setResponse(pResponse);

		BufferedReader rd = new BufferedReader(new InputStreamReader(pResponse
				.getEntity().getContent()));

		StringBuffer resultBuffer = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			resultBuffer.append(line);
		}
		JSONObject json = (JSONObject) new JSONParser().parse(resultBuffer
				.toString());
		page = (long) json.get("page");
		results = (long) json.get("results");
		pages = (long) json.get("pages");
		list = (ArrayList<JSONObject>) json.get("events");
	}

	public HttpUriRequest getRequest() {
		return request;
	}

	public void setNext(HttpUriRequest pRequest) {
		this.next = pRequest;
	}

	public HttpUriRequest getNext() {
		return next;
	}

	public long getPage() {
		return page;
	}

	public long getResults() {
		return results;
	}

	public ArrayList<JSONObject> getList() {
		return list;
	}

	public long getPages() {
		return pages;
	}

	public HttpResponse getResponse() {
		return response;
	}

	public void setResponse(HttpResponse response) {
		this.response = response;
	}
}
