package com.yxzhm.jmeter;

import com.neovisionaries.ws.client.WebSocket;
import lombok.Getter;

public class ConnectionCache {
    @Getter
    private static final ThreadLocal<WebSocket> cachedWSConnection = new ThreadLocal<WebSocket>();
}
