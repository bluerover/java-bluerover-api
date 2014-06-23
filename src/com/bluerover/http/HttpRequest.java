package com.bluerover.http;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.TreeMap;

public class HttpRequest implements Serializable {
	private static final long serialVersionUID = 3365496352032493020L;
    private final String method;

    private final String url;

    private final TreeMap<String,String> parameters;

    //private final Authorization authorization;

    private final HashMap<String, String> requestHeaders;


    //private static final HttpParameter[] NULL_PARAMETERS = new HttpParameter[0];

    /**
     * @param method         Specifies the HTTP method
     * @param url            the request to request
     * @param parameters     parameters
     * @param authorization  Authentication implementation. Currently BasicAuthentication, OAuthAuthentication and NullAuthentication are supported.
     * @param requestHeaders request headers
     */
    public HttpRequest(String method, String url, TreeMap<String,String> parameters, HashMap<String, String> requestHeaders) {
        this.method = method;
        this.url = url;
        this.parameters = parameters;
        this.requestHeaders = requestHeaders;
    }

    public String getMethod() {
        return method;
    }

    public TreeMap<String,String> getParameters() {
        return parameters;
    }

    public String getURL() {
        return url;
    }

    public HashMap<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    @SuppressWarnings("unused")
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
    
    @Override
    public String toString() {
        return "HttpRequest{" +
                "requestMethod=" + method +
                ", url='" + url + '\'' +
                ", postParams=" + (parameters == null ? null : parameters) +
                ", requestHeaders=" + requestHeaders +
                '}';
    }
}
