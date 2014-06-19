package com.bluerover.model;

import java.lang.reflect.Field;

import com.bluerover.http.HttpRequest;
import com.bluerover.http.HttpResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Result<T> {

	T list;
	JsonObject jsonObject;
	JsonArray jsonArray;
	HttpRequest request;
	HttpResponse response;
	HttpRequest next;
	String rawResponse;

	public String getRawResponse() {
		return rawResponse;
	}

	public void setRawResponse(String rawResponse) {
		this.rawResponse = rawResponse;
	}

	public JsonObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JsonObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public JsonArray getJsonArray() {
		return jsonArray;
	}

	public void setJsonArray(JsonArray jsonArray) {
		this.jsonArray = jsonArray;
	}

	public Result<T> setList(T list) {
		this.list = list;
		return this;
	}

	public Result<T> setRequest(HttpRequest request) {
		this.request = request;
		return this;
	}

	public HttpRequest getRequest() {
		return request;
	}

	public Result<T> setNext(HttpRequest pRequest) {
		this.next = pRequest;
		return this;
	}

	public HttpRequest getNext() {
		return next;
	}

	public int getPage() {
		if(jsonObject != null) {
			return jsonObject.get("page").getAsInt();
		}
		return 0;
	}

	public long getResults() {
		if(jsonObject != null) {
			return jsonObject.get("results").getAsInt();
		}
		return 0;
	}

	public T getList() {
		return list;
	}

	public int getPages() {
		if(jsonObject != null) {
			return jsonObject.get("pages").getAsInt();
		}
		return 0;
	}

	public HttpResponse getResponse() {
		return response;
	}

	public Result<T> setResponse(HttpResponse response) {
		this.response = response;
		return this;
	}
	
	public void consume(ApiResponse pResponse) {
		request = pResponse.getRequest();
		response = pResponse.getResponse();
		rawResponse = pResponse.getRawResponse();
	}
	
	@Override
	public String toString() {
		String text = "";
		for (Field f : getClass().getDeclaredFields()) {
		    try {
				text += "\"" + f.getName() + "\":" + f.get(this) + ",";
			} catch (Exception ignore) {}
		}
		return text;
	}
}
