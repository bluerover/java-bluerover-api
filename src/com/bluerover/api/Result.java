package com.bluerover.api;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

public class Result {

	long page;
	long results;
	private ArrayList<Object> list;
	long pages;
	private HttpUriRequest request;
	private HttpResponse response;
	private HttpUriRequest next;

	public Result setPage(long page) {
		this.page = page;
		return this;
	}

	public Result setResults(long results) {
		this.results = results;
		return this;
	}

	public Result setList(ArrayList<Object> list) {
		this.list = list;
		return this;
	}

	public Result setPages(long pages) {
		this.pages = pages;
		return this;
	}

	public Result setRequest(HttpUriRequest request) {
		this.request = request;
		return this;
	}

	public HttpUriRequest getRequest() {
		return request;
	}

	public Result setNext(HttpUriRequest pRequest) {
		this.next = pRequest;
		return this;
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

	public ArrayList<Object> getList() {
		return list;
	}

	public long getPages() {
		return pages;
	}

	public HttpResponse getResponse() {
		return response;
	}

	public Result setResponse(HttpResponse response) {
		this.response = response;
		return this;
	}
	
	@Override
	public String toString() {
		String text = "";
		for (Field f : getClass().getDeclaredFields()) {
		    try {
				text += f.getName() + "=" + f.get(this) + ",";
			} catch (Exception ignore) {}
		}
		return text;
	}
}
