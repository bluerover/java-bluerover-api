package com.bluerover.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.TreeMap;

public class HttpClient implements Serializable {
	 private static final long serialVersionUID = -8016974810651763053L;
	 protected final HttpClientConfiguration CONF;
	 
	 private final HashMap<String, String> requestHeaders;
	 
	 public HttpClient(HttpClientConfiguration conf) {
	        this.CONF = conf;
	        requestHeaders = new HashMap<String, String>();
//	        requestHeaders.put("X-Twitter-Client-Version", Version.getVersion());
//	        requestHeaders.put("X-Twitter-Client-URL", "http://twitter4j.org/en/twitter4j-" + Version.getVersion() + ".xml");
//	        requestHeaders.put("X-Twitter-Client", "Twitter4J");
//	        requestHeaders.put("User-Agent", "twitter4j http://twitter4j.org/ /" + Version.getVersion());
//	        if (conf.isGZIPEnabled()) {
//	            requestHeaders.put("Accept-Encoding", "gzip");
//	        }
	    }

	    protected boolean isProxyConfigured() {
	        return CONF.getHttpProxyHost() != null && !CONF.getHttpProxyHost().equals("");
	    }

	    public void write(DataOutputStream out, String outStr) throws IOException {
	        out.writeBytes(outStr);
	    }

	    public HashMap<String, String> getRequestHeaders() {
	        return requestHeaders;
	    }

	    public void addDefaultRequestHeader(String name, String value) {
	        requestHeaders.put(name, value);
	    }

	    public final HttpResponse request(HttpRequest req) throws Exception {
	        return handleRequest(req);
	    }

	    public final HttpResponse request(HttpRequest req, HttpResponseListener listener) throws Exception  {
//	        try {
	            HttpResponse res = handleRequest(req);
	            if (listener != null) {
	                listener.httpResponseReceived(new HttpResponseEvent(req, res));
	            }
	            return res;
//	        } 
//	        catch (TwitterException te) {
//	            if (listener != null) {
//	                listener.httpResponseReceived(new HttpResponseEvent(req, null, te));
//	            }
//	            throw te;
//	        }
	    }

	    public HttpResponse handleRequest(HttpRequest req) throws IOException {
	        HttpResponse res = null;
	        while(true) {
	            int responseCode = -1;
	            try {
	                HttpURLConnection con;
	                OutputStream os = null;
	                try {
	                    con = getConnection(req.getURL());
	                    con.setDoInput(true);
	                    setHeaders(req, con);
	                    con.setRequestMethod(req.getMethod());
//	                    if (req.getMethod() == RequestMethod.POST) {	                    	
//	                        if (HttpParameter.containsFile(req.getParameters())) {
//	                            String boundary = "----Twitter4J-upload" + System.currentTimeMillis();
//	                            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
//	                            boundary = "--" + boundary;
//	                            con.setDoOutput(true);
//	                            os = con.getOutputStream();
//	                            DataOutputStream out = new DataOutputStream(os);
//	                            for (HttpParameter param : req.getParameters()) {
//	                                if (param.isFile()) {
//	                                    write(out, boundary + "\r\n");
//	                                    write(out, "Content-Disposition: form-data; name=\"" + param.getName() + "\"; filename=\"" + param.getFile().getName() + "\"\r\n");
//	                                    write(out, "Content-Type: " + param.getContentType() + "\r\n\r\n");
//	                                    BufferedInputStream in = new BufferedInputStream(
//	                                            param.hasFileBody() ? param.getFileBody() : new FileInputStream(param.getFile())
//	                                    );
//	                                    byte[] buff = new byte[1024];
//	                                    int length;
//	                                    while ((length = in.read(buff)) != -1) {
//	                                        out.write(buff, 0, length);
//	                                    }
//	                                    write(out, "\r\n");
//	                                    in.close();
//	                                } else {
//	                                    write(out, boundary + "\r\n");
//	                                    write(out, "Content-Disposition: form-data; name=\"" + param.getName() + "\"\r\n");
//	                                    write(out, "Content-Type: text/plain; charset=UTF-8\r\n\r\n");
//	                                    logger.debug(param.getValue());
//	                                    out.write(param.getValue().getBytes("UTF-8"));
//	                                    write(out, "\r\n");
//	                                }
//	                            }
//	                            write(out, boundary + "--\r\n");
//	                            write(out, "\r\n");
//
//	                        } else {
//	                            con.setRequestProperty("Content-Type",
//	                                    "application/x-www-form-urlencoded");
//	                            String postParam = HttpParameter.encodeParameters(req.getParameters());
//	                            logger.debug("Post Params: ", postParam);
//	                            byte[] bytes = postParam.getBytes("UTF-8");
//	                            con.setRequestProperty("Content-Length",
//	                                    Integer.toString(bytes.length));
//	                            con.setDoOutput(true);
//	                            os = con.getOutputStream();
//	                            os.write(bytes);
//	                        }
//	                        os.flush();
//	                        os.close();
//	                    }
	                    res = new HttpResponse(con, CONF);
	                    responseCode = con.getResponseCode();
//	                    if (logger.isDebugEnabled()) {
//	                        logger.debug("Response: ");
//	                        Map<String, List<String>> responseHeaders = con.getHeaderFields();
//	                        for (String key : responseHeaders.keySet()) {
//	                            List<String> values = responseHeaders.get(key);
//	                            for (String value : values) {
//	                                if (key != null) {
//	                                    logger.debug(key + ": " + value);
//	                                } else {
//	                                    logger.debug(value);
//	                                }
//	                            }
//	                        }
//	                    }
	                    if (responseCode != 200) {
	                    	if(responseCode == 403) {
	                    		throw new SecurityException("not authorized");
	                    	}
	                    	else {
	                    		throw new UnsupportedOperationException("");
	                    	}
	                    } else {
	                        break;
	                    }
	                } finally {
	                    try {
	                        os.close();
	                    } catch (Exception ignore) {
	                    }
	                }
	            } catch (SocketTimeoutException ste) {
	                // connection timeout or read timeout
	            	System.err.println("oh no, io exception " + ste.getMessage());
	            }
	        }
	        return res;
	    };

	    public HttpResponse get(String method, String url, TreeMap<String,String> parameters
	            , HttpResponseListener listener) throws Exception  {
	        return request(new HttpRequest(method, url, parameters, this.requestHeaders), listener);
	    }

	    public HttpResponse get(String url) throws Exception  {
	        return request(new HttpRequest("GET", url, new TreeMap<String,String>(), this.requestHeaders));
	    }
	    
	    private HttpURLConnection getConnection(String url) throws IOException {
	        HttpURLConnection con;
	        con = (HttpURLConnection) new URL(url).openConnection();
	        con.setConnectTimeout(4*60*1000);
	        con.setReadTimeout(4*60*1000);
	        con.setInstanceFollowRedirects(false);
	        return con;
	    }
	    
	    private void setHeaders(HttpRequest req, HttpURLConnection connection) {
//	        if (logger.isDebugEnabled()) {
//	            logger.debug("Request: ");
//	            logger.debug(req.getMethod().name() + " ", req.getURL());
//	        }

//	        String authorizationHeader;
//	        if (req.getAuthorization() != null && (authorizationHeader = req.getAuthorization().getAuthorizationHeader(req)) != null) {
//	            if (logger.isDebugEnabled()) {
//	                logger.debug("Authorization: ", authorizationHeader.replaceAll(".", "*"));
//	            }
//	            connection.addRequestProperty("Authorization", authorizationHeader);
//	        }
	        if (req.getRequestHeaders() != null) {
	            for (String key : req.getRequestHeaders().keySet()) {
	                connection.addRequestProperty(key, req.getRequestHeaders().get(key));
	            }
	        }
	    }
}
