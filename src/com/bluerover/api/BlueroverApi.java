package com.bluerover.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;

import com.bluerover.gson.ResultDeserializer;
import com.bluerover.model.Device;
import com.bluerover.model.Event;
import com.bluerover.model.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BlueroverApi {

	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

	String key;
	String token;
	String baseURL;

	Gson gson;
	
	@SuppressWarnings("rawtypes")
	public BlueroverApi() {
		GsonBuilder gsonBuilder = new GsonBuilder();
	    gsonBuilder.registerTypeAdapter(Result.class, new ResultDeserializer());
		gson = gsonBuilder.create();
	}
	
	public BlueroverApi setCredentials(TreeMap<String, String> pMap) {
		if (isNullOrEmpty(pMap.get("key")) || isNullOrEmpty(pMap.get("token"))
				|| isNullOrEmpty(pMap.get("baseURL"))) {
			throw new SecurityException("key, token, or baseURL is null/empty");
		}
		this.key = pMap.get("key");
		this.token = pMap.get("token");
		this.baseURL = pMap.get("baseURL");
		
		return this;
	}

	public void clearCredentials() {
		this.key = "";
		this.token = "";
	}

	public Result<Event> getEvents(String startTime, String endTime,
			String page) throws IOException {
		TreeMap<String, String> params = new TreeMap<String, String>();
		params.put("start_time", startTime);
		params.put("end_time", endTime);
		params.put("page", page);
		HttpGet request = generateRequest("/event", params, false);
		Result<Event> results = callApi(request); 
		return results;
	}
	
	public Result<Device> getDevices() throws IOException {
		HttpGet request = generateRequest("/device", new TreeMap<String,String>(), false);
		Result<Device> results = callApi(request); 
		return results;
	}

	public <T> Result<T> next(Result<T> pResult) {
		Result<T> result = null;
		try {
			result = callApi(pResult.getNext());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Error with connecting to server");
			e.printStackTrace();
		}
		return result;
	}
	private HttpGet generateRequest(String relativeURL,
			TreeMap<String, String> params, boolean post_data)
			throws MalformedURLException {
		// Post data not supported yet
		post_data = false;

		URL url = new URL(this.baseURL + relativeURL);

		String http_method = post_data ? "POST" : "GET";

		String signature = generateSignature(this.key, http_method, url, params);

		String endpointURL = url.toString();
		if (!post_data && !params.isEmpty()) {
			endpointURL += "?";
			StringBuilder joinedParams = new StringBuilder();
			for (Entry<String, String> entry : params.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				joinedParams.append(key + "=" + value + "&");
			}
			endpointURL += joinedParams.substring(0, joinedParams.length() - 1);
		}
		HttpGet request = new HttpGet(endpointURL);
		request.addHeader("Authorization", "BR " + this.token + ":" + signature);
		return request;
	}
	
	private HttpGet generateRequest(URI pUri) throws MalformedURLException {
		
		String[] queryParams = pUri.getQuery().split("&");
		TreeMap<String,String> params = new TreeMap<String,String>();
		for(String param : queryParams) {
			String[] tmp = param.split("=");
			params.put(tmp[0], tmp[1]);
		}
		//Add 1 to the page #
		params.put("page", Integer.toString(Integer.parseInt(params.get("page")+1)));
		
		return generateRequest(pUri.getPath(), params, false);
	}

	@SuppressWarnings("unchecked")
	private <T> Result<T> callApi(HttpUriRequest pRequest) throws IOException {
		HttpClient client = HttpClients.createDefault();
		HttpResponse response = null;
		try {
			response = client.execute(pRequest);
		} catch (IOException e) {
			System.err.println("Request to " + pRequest.getURI().toString() + " failed:");
			e.printStackTrace();
		}

		System.out.println("Response Code : "
				+ response.getStatusLine().getStatusCode());
		if(response.getStatusLine().getStatusCode() == 403) {
			throw new SecurityException("invalid credentials");
		}
		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		Result<T> result = gson.fromJson(rd, Result.class);
		gson.fromJson(rd, Result.class);
		result.setRequest(pRequest).setResponse(response);
		
		if (result.getPages() > 0) {
			result.setNext(generateRequest(pRequest.getURI()));
		}
		return result;
	}

	private String streamApi(String relativeURL,
			TreeMap<String, String> params, boolean post_data)
			throws ClientProtocolException, IOException {
		// Post data not supported yet
		post_data = false;

		URL url = new URL(this.baseURL + relativeURL);

		String http_method = post_data ? "POST" : "GET";

		String signature = generateSignature(this.key, http_method, url, params);

		String endpointURL = url.toString();
		if (!post_data && !params.isEmpty()) {
			endpointURL += "?";
			StringBuilder joinedParams = new StringBuilder();
			for (Entry<String, String> entry : params.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				joinedParams.append(key + "=" + value + "&");
			}
			endpointURL += joinedParams.substring(0, joinedParams.length());
		}

		// Musings on how to do the stream
		// HttpClient client = HttpClients.createDefault();
		// HttpGet request = new HttpGet(endpointURL);
		// request.addHeader("Authorization", "BR " + this.token + ":" +
		// signature);
		// request.setHeader("Connection", "keep-alive");
		// HttpResponse response = client.execute(request);
		// HeaderIterator it = response.headerIterator();
		// while (it.hasNext()) {
		// System.out.println(it.next());
		// }

		// Builder sconfig =
		// SocketConfig.custom().setSoTimeout(4000).setSoKeepAlive(true);
		// CloseableHttpClient client =
		// HttpClients.custom().setDefaultSocketConfig(sconfig.build()).build();
		// client.execute(endpointURL, request, ResponseHandler<T>);

		return null;
	}

	private String generateSignature(String pKey, String pMethod, URL pUrl,
			TreeMap<String, String> params) {
		String normalizedURL = pUrl.getProtocol().toLowerCase() + "://"
				+ pUrl.getAuthority().toLowerCase() + pUrl.getPath();
		ArrayList<String> baseArray = new ArrayList<String>();
		baseArray.add(pMethod.toUpperCase());
		baseArray.add(normalizedURL);

		if (!params.isEmpty()) {
			StringBuilder combinedParams = new StringBuilder();
			for (Entry<String, String> entry : params.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				combinedParams.append(key + "=" + value + "&");
			}
			baseArray.add(combinedParams.substring(0, combinedParams.length()));
		}

		// then you unicode encode
		StringBuilder escapedParams = new StringBuilder();
		for (String s : baseArray) {
			escapedParams.append(encodeURI(s) + '&');
		}

		// shave off '%26&' if there are params
		if (!params.isEmpty()) {
			escapedParams = escapedParams.delete(escapedParams.length() - 4,
					escapedParams.length());
		}

		return oauthHmacSha1(pKey, escapedParams.toString());
	}

	private static String oauthHmacSha1(String pKey, String substring) {
		try {
			// Get an hmac_sha1 key from the raw key bytes
			SecretKeySpec signingKey = new SecretKeySpec(pKey.getBytes(),
					HMAC_SHA1_ALGORITHM);

			// Get an hmac_sha1 Mac instance and initialize with the signing key
			Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
			mac.init(signingKey);
			// Compute the hmac on input data bytes
			byte[] signatureBytes = Base64.encodeBase64(mac.doFinal(substring
					.getBytes()));
			return new String(signatureBytes, "UTF-8");

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String encodeURI(String s) {
		String result = null;

		try {
			result = URLEncoder.encode(s, "UTF-8").replaceAll("\\+", "%20")
					.replaceAll("\\%21", "!").replaceAll("\\%27", "'")
					.replaceAll("\\%28", "(").replaceAll("\\%29", ")")
					.replaceAll("\\%7E", "~");
		}

		// This exception should never occur.
		catch (UnsupportedEncodingException e) {
			result = s;
		}

		return result;
	}

	private boolean isNullOrEmpty(String str) {
		return (str == null || str.isEmpty());
	}
	
	public static void main(String[] args) throws Exception {
		TreeMap<String, String> creds = new TreeMap<String, String>();
		creds.put("key", "yXIJ1omZUNtbo6wNjMOkKYBLNJakn0nr/OzgVtDKh2i5lDktVT2xv5xfbYlCkW+Z");
		creds.put("token", "9DquKlyhPKpZ35mxcjG/JUqWAd//U12O13ja6Wqp");
		creds.put("baseURL", "http://developers.bluerover.us");
		BlueroverApi api = new BlueroverApi().setCredentials(creds);
//		Result<Event> result = null;
//		try {
//			result = api.getEvents(Objects.toString(
//					System.currentTimeMillis() / 1000L - 10 * 1000, null), Objects
//					.toString(System.currentTimeMillis() / 1000L, null), "0");
//		} catch (IOException e) {
//			System.err.println("Error with connecting to server");
//			e.printStackTrace();
//		}
		Result<Device> result = null;
		try {
			result = api.getDevices();
		} catch (IOException e) {
			System.err.println("Error with connecting to server");
			e.printStackTrace();
		}
		System.out.println(result);
	}
}
