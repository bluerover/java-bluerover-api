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
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;

import com.bluerover.gson.ResultDeserializer;
import com.bluerover.model.ApiResponse;
import com.bluerover.model.Device;
import com.bluerover.model.Event;
import com.bluerover.model.Result;
import com.bluerover.model.Rfid;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

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

	/**
	 * 
	 * @param pMap	Map of <String,String> that contains key, token, and baseURL
	 * @return
	 */
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

	@SuppressWarnings("unchecked")
	/**
	 * 
	 * @param startTime	Unix timestamp string for start time of range
	 * @param endTime	Unix timestamp string for end of range
	 * @param page		For pagination
	 * @return Result<Event[]>	Result object containing list of events
	 * @throws IOException
	 */
	public Result<Event[]> getEvents(String startTime, String endTime,
			String page) throws IOException {
		TreeMap<String, String> params = new TreeMap<String, String>();
		params.put("start_time", startTime);
		params.put("end_time", endTime);
		params.put("page", page);
		HttpGet request = generateRequest("/event", params, false);
		ApiResponse response = callApi(request);
		Result<Event[]> results = gson.fromJson(response.getRawResponse(),
				Result.class);
		results.consume(response);

		// Event post-processing with jsonObject
		if (results.getPages() > 0) {
			results.setNext(generateRequest(results.getRequest().getURI()));
		}
		Event[] events = gson.fromJson(results.getJsonObject().get("events"),
				Event[].class);
		results.setList(events);

		return results;
	}

	@SuppressWarnings("unchecked")
	public Result<Device[]> getDevices() throws IOException {
		HttpGet request = generateRequest("/device",
				new TreeMap<String, String>(), false);
		ApiResponse response = callApi(request);
		Result<Device[]> results = gson.fromJson(response.getRawResponse(),
				Result.class);
		results.consume(response);

		// Device post-processing with jsonArray
		Iterator<JsonElement> it = results.getJsonArray().iterator();
		ArrayList<Device> list = new ArrayList<Device>();
		while (it.hasNext()) {
			list.add(gson.fromJson(it.next(), Device.class));
		}
		results.setList(list.toArray(new Device[0]));
		return results;
	}

	@SuppressWarnings("unchecked")
	public Result<Rfid[]> getRfids() throws IOException {
		HttpGet request = generateRequest("/rfid",
				new TreeMap<String, String>(), false);
		ApiResponse response = callApi(request);
		Result<Rfid[]> results = gson.fromJson(response.getRawResponse(),
				Result.class);
		results.consume(response);

		// Rfid post-processing with jsonArray
		Iterator<JsonElement> it = results.getJsonArray().iterator();
		ArrayList<Rfid> list = new ArrayList<Rfid>();
		while (it.hasNext()) {
			list.add(gson.fromJson(it.next(), Rfid.class));
		}
		results.setList(list.toArray(new Rfid[0]));
		return results;
	}

	@SuppressWarnings("unchecked")
	/**
	 * 
	 * @param pResult	Result<Event[]> object that was returned before
	 * @return			The subsequent page of events in Result<Event[]>
	 * @throws IOException
	 */
	public Result<Event[]> next(Result<Event> pResult) throws IOException {
		ApiResponse response = callApi(pResult.getNext());
		Result<Event[]> results = gson.fromJson(response.getRawResponse(),
				Result.class);
		results.consume(response);

		// Event post-processing with jsonObject
		if (results.getPages() > 0) {
			results.setNext(generateRequest(results.getRequest().getURI()));
		}
		Event[] events = gson.fromJson(results.getJsonObject().get("events"),
				Event[].class);
		results.setList(events);
		return results;
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
		TreeMap<String, String> params = new TreeMap<String, String>();
		for (String param : queryParams) {
			String[] tmp = param.split("=");
			params.put(tmp[0], tmp[1]);
		}
		// Add 1 to the page #
		params.put("page",
				Integer.toString(Integer.parseInt(params.get("page") + 1)));

		return generateRequest(pUri.getPath(), params, false);
	}

	/**
	 * 
	 * @param pRequest
	 *            The entire request object with signature, url, token, etc.
	 * @return
	 * @throws IOException
	 * 
	 *             Gson TypeToken does not allow for a generic type, so it is
	 *             binded to the higher class in the stack
	 */
	private ApiResponse callApi(HttpUriRequest pRequest) throws IOException {
		System.out.println("Making request to " + pRequest.getURI().toString());
		HttpClient client = HttpClients.createDefault();
		HttpResponse response = null;
		try {
			response = client.execute(pRequest);
		} catch (IOException e) {
			System.err.println("Request to " + pRequest.getURI().toString()
					+ " failed:");
			e.printStackTrace();
			throw e;
		}

		System.out.println("Response Code : "
				+ response.getStatusLine().getStatusCode());
		if (response.getStatusLine().getStatusCode() == 403) {
			throw new SecurityException("invalid credentials");
		}
		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setRequest(pRequest).setResponse(response)
				.setRawResponse(result.toString());

		return apiResponse;
	}

	@SuppressWarnings("unused")
	private String streamApi(String relativeURL,
			TreeMap<String, String> params, boolean post_data) {
		throw new UnsupportedOperationException("Not implemented");
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
}
