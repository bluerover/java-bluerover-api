package com.bluerover.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

public class BlueroverApi {

	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

	String key;
	String token;
	String baseURL;

	public void setCredentials(TreeMap<String, String> pMap) {
		if (isNullOrEmpty(pMap.get("key")) || isNullOrEmpty(pMap.get("token"))
				|| isNullOrEmpty(pMap.get("baseURL"))) {
			throw new SecurityException("key, token, or baseURL is null/empty");
		}
		this.key = pMap.get("key");
		this.token = pMap.get("token");
		this.baseURL = pMap.get("baseURL");
	}

	public void clearCredentials() {
		this.key = "";
		this.token = "";
	}

	public String callApi(String relativeURL, TreeMap<String, String> params,
			boolean post_data) throws ClientProtocolException, IOException {

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
			endpointURL += joinedParams.substring(0,joinedParams.length()-1);
		}

		HttpClient client = HttpClients.createDefault();
		HttpGet request = new HttpGet(endpointURL);
		request.addHeader("Authorization", "BR " + this.token + ":" + signature);
		HttpResponse response = client.execute(request);

		System.out.println("\nSending 'GET' request to URL : " + endpointURL);
		System.out.println("Response Code : "
				+ response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		return result.toString();
	}

	public String streamApi(String relativeURL, TreeMap<String, String> params,
			boolean post_data) throws ClientProtocolException, IOException {
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

	public static void main(String[] args) throws Exception {
		BlueroverApi api = new BlueroverApi();
		TreeMap<String, String> creds = new TreeMap<String, String>();
		// siloman to test calls
		creds.put("key",
				"yXIJ1omZUNtbo6wNjMOkKYBLNJakn0nr/OzgVtDKh2i5lDktVT2xv5xfbYlCkW+Z");
		creds.put("token", "9DquKlyhPKpZ35mxcjG/JUqWAd//U12O13ja6Wqp");
		creds.put("baseURL", "http://developers.polairus.com");
		api.setCredentials(creds);
		System.out.println("Testing 1 - Send Http GET request (no stream)");
		// '/event', {'end_time':end,'start_time':start,'page':page}
		TreeMap<String, String> times = new TreeMap<String, String>();
		long startTime = System.currentTimeMillis() / 1000L - 5 * 60 * 1000;
		long endTime = System.currentTimeMillis() / 1000L;
		times.put("start_time", Objects.toString(startTime, null));
		times.put("end_time", Objects.toString(endTime, null));
		times.put("page", "1");
		String result = api.callApi("/event", times, false);
		System.out.println(result);

		// foodsafety to test stream
//		creds.put("key",
//		"0OZW0W/dO8KiWlmee24z7S8YxZGqb9ALYDT1x3QUsgpJvYzpiPCgZHoiu7QKUIdQ");
//		creds.put("token", "v0P6TZqlK3QQ5dHpg8FgEno2GRx6Phh+w9QQQ7vH");
//		creds.put("baseURL", "http://developers.polairus.com");
//		api.setCredentials(creds);
//		
//		String result = api.streamApi("/eventstream",
//		new TreeMap<String, String>(), false);
//		System.out.println(result);

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
			baseArray.add(combinedParams.substring(0,
					combinedParams.length()));
		}

		// then you unicode encode
		StringBuilder escapedParams = new StringBuilder();
		for (String s : baseArray) {
			escapedParams.append(encodeURI(s) + '&');
		}
		
		//shave off '%26&' if there are params
		if(!params.isEmpty()) {
			escapedParams = escapedParams.delete(escapedParams.length()-4, escapedParams.length());
		}
		
		return oauthHmacSha1(pKey, escapedParams.toString());
	}

	private static String oauthHmacSha1(String pKey, String substring) {
		System.out.println(substring);
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
