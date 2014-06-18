package com.bluerover.model;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

public class Result<T> {

	long page;
	long results;
	private ArrayList<T> list;
	long pages;
	private HttpUriRequest request;
	private HttpResponse response;
	private HttpUriRequest next;

	public Result<T> setPage(long page) {
		this.page = page;
		return this;
	}

	public Result<T> setResults(long results) {
		this.results = results;
		return this;
	}

	public Result<T> setList(ArrayList<T> list) {
		this.list = list;
		return this;
	}

	public Result<T> setPages(long pages) {
		this.pages = pages;
		return this;
	}

	public Result<T> setRequest(HttpUriRequest request) {
		this.request = request;
		return this;
	}

	public HttpUriRequest getRequest() {
		return request;
	}

	public Result<T> setNext(HttpUriRequest pRequest) {
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

	public ArrayList<T> getList() {
		return list;
	}

	public long getPages() {
		return pages;
	}

	public HttpResponse getResponse() {
		return response;
	}

	public Result<T> setResponse(HttpResponse response) {
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
