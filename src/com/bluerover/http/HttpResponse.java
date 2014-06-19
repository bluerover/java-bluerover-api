package com.bluerover.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HttpResponse {
	private HttpURLConnection con;
    protected final HttpClientConfiguration CONF;

//    HttpResponse() {
//        this.CONF = ConfigurationContext.getInstance().getHttpClientConfiguration();
//    }
//
//    public HttpResponse(HttpClientConfiguration conf) {
//        this.CONF = conf;
//    }

    protected int statusCode;
    protected String responseAsString = null;
    protected InputStream is;
    private boolean streamConsumed = false;

    public HttpResponse(HttpURLConnection con, HttpClientConfiguration conf) throws IOException {
    	this.CONF = conf;
        this.con = con;
        this.statusCode = con.getResponseCode();
        if (null == (is = con.getErrorStream())) {
            this.is = con.getInputStream();
        }
    }
    
    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseHeader(String name) {
    	return null;
    };

    public Map<String, List<String>> getResponseHeaderFields() {
    	Map<String, List<String>> ret = new TreeMap<String, List<String>>();
//        for (Map.Entry<String, String> entry : headers.entrySet()) {
//            ret.put(entry.getKey(), Arrays.asList(entry.getValue()));
//        }
        return ret;
    };

    /**
     * Returns the response stream.<br>
     * This method cannot be called after calling asString() or asDcoument()<br>
     * It is suggested to call disconnect() after consuming the stream.
     * <p/>
     * Disconnects the internal HttpURLConnection silently.
     *
     * @return response body stream
     * @see #disconnect()
     */
    public InputStream asStream() {
        if (streamConsumed) {
            throw new IllegalStateException("Stream has already been consumed.");
        }
        return is;
    }

    /**
     * Returns the response body as string.<br>
     * Disconnects the internal HttpURLConnection silently.
     *
     * @return response body
     * @throws TwitterException
     */
    public String asString() {
        if (null == responseAsString) {
            BufferedReader br = null;
            InputStream stream = null;
            try {
                stream = asStream();
                if (null == stream) {
                    return null;
                }
                br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                StringBuilder buf = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    buf.append(line).append("\n");
                }
                this.responseAsString = buf.toString();
                stream.close();
                streamConsumed = true;
            } catch (IOException ioe) {
            	System.err.println("IO Error in HttpResponse " + ioe.getMessage());
            	ioe.printStackTrace();
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException ignore) {
                    }
                }
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException ignore) {
                    }
                }
                disconnect();
            }
        }
        return responseAsString;
    }

//    private JsonObject json = null;
//
//    /**
//     * Returns the response body as twitter4j.JSONObject.<br>
//     * Disconnects the internal HttpURLConnection silently.
//     *
//     * @return response body as twitter4j.JSONObject
//     * @throws TwitterException
//     */
//    public JsonObject asJSONObject() {
//        if (json == null) {
//            Reader reader = null;
//            try {
//                if (responseAsString == null) {
//                    reader = asReader();
//                    json = reader.read
//                } else {
//                    json = new JSONObject(responseAsString);
//                }
//            } catch (JSONException jsone) {
//                if (responseAsString == null) {
//                    throw new TwitterException(jsone.getMessage(), jsone);
//                } else {
//                    throw new TwitterException(jsone.getMessage() + ":" + this.responseAsString, jsone);
//                }
//            } finally {
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (IOException ignore) {
//                    }
//                }
//                disconnectForcibly();
//            }
//        }
//        return json;
//    }
//
//    private JSONArray jsonArray = null;
//
//    /**
//     * Returns the response body as twitter4j.JSONArray.<br>
//     * Disconnects the internal HttpURLConnection silently.
//     *
//     * @return response body as twitter4j.JSONArray
//     * @throws TwitterException
//     */
//    public JSONArray asJSONArray() throws TwitterException {
//        if (jsonArray == null) {
//            Reader reader = null;
//            try {
//                if (responseAsString == null) {
//                    reader = asReader();
//                    jsonArray = new JSONArray(new JSONTokener(reader));
//                } else {
//                    jsonArray = new JSONArray(responseAsString);
//                }
//                if (CONF.isPrettyDebugEnabled()) {
//                    logger.debug(jsonArray.toString(1));
//                } else {
//                    logger.debug(responseAsString != null ? responseAsString :
//                            jsonArray.toString());
//                }
//            } catch (JSONException jsone) {
//                if (logger.isDebugEnabled()) {
//                    throw new TwitterException(jsone.getMessage() + ":" + this.responseAsString, jsone);
//                } else {
//                    throw new TwitterException(jsone.getMessage(), jsone);
//                }
//            } finally {
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (IOException ignore) {
//                    }
//                }
//                disconnectForcibly();
//            }
//        }
//        return jsonArray;
//    }

    public Reader asReader() {
        try {
            return new BufferedReader(new InputStreamReader(is, "UTF-8"));
        } catch (java.io.UnsupportedEncodingException uee) {
            return new InputStreamReader(is);
        }
    }

    private void disconnect() {
        try {
            con.disconnect();
        } catch (Exception ignore) {
        }
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "statusCode=" + statusCode +
                ", responseAsString='" + responseAsString + '\'' +
                ", is=" + is +
                ", streamConsumed=" + streamConsumed +
                '}';
    }
}
