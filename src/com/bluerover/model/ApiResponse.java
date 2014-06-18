package com.bluerover.model;

import java.lang.reflect.Field;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

public class ApiResponse {

	HttpUriRequest request;
	HttpResponse response;
	String rawResponse;

	public HttpUriRequest getRequest() {
		return request;
	}

	public ApiResponse setRequest(HttpUriRequest request) {
		this.request = request;
		return this;
	}

	public HttpResponse getResponse() {
		return response;
	}

	public ApiResponse setResponse(HttpResponse response) {
		this.response = response;
		return this;
	}

	public String getRawResponse() {
		return rawResponse;
	}

	public ApiResponse setRawResponse(String rawResponse) {
		this.rawResponse = rawResponse;
		return this;
	}

	@Override
	public String toString() {
		String text = "";
		for (Field f : getClass().getDeclaredFields()) {
			try {
				text += f.getName() + "=" + f.get(this) + ",";
			} catch (Exception ignore) {
			}
		}
		return text;
	}
}
