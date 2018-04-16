package com.yxzhm.jmeter;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketState;
import lombok.Getter;

public class Utility {
    @Getter
    private static final ThreadLocal<WebSocket> cachedWSConnection = new ThreadLocal<WebSocket>();

    public static void dispose(WebSocket ws) {
        if (ws != null) {
            try {
                if(ws.getState()==WebSocketState.OPEN) {
                    ws.disconnect();
                }
                ws=null;
                Utility.getCachedWSConnection().remove();
            } catch (Exception ex) {

            }

        }
    }
}
