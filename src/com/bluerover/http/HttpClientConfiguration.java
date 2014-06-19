package com.bluerover.http;

public interface HttpClientConfiguration {
    String getHttpProxyHost();

    int getHttpProxyPort();

    String getHttpProxyUser();

    String getHttpProxyPassword();

    int getHttpConnectionTimeout();

    int getHttpReadTimeout();

    int getHttpRetryCount();

    int getHttpRetryIntervalSeconds();

    boolean isPrettyDebugEnabled();

    boolean isGZIPEnabled();
}
