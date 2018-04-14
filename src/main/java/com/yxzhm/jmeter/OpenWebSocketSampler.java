package com.yxzhm.jmeter;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketState;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.ThreadListener;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class OpenWebSocketSampler extends AbstractSampler implements ThreadListener {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(OpenWebSocketSampler.class);

    public SampleResult sample(Entry entry) {
        dispose(ConnectionCache.getCachedWSConnection().get());
        SampleResult result = new SampleResult();
        result.setSampleLabel(getName());
        result.sampleStart();
        result.setDataType(org.apache.jmeter.samplers.SampleResult.TEXT);
        try {
            String url = String.format("%s://%s:%s%s", getTLS() ? "wss" : "ws", getServer(), getPort(), getPath());
            logger.info("Connecting " + url);
            WebSocket ws = new WebSocketFactory().createSocket(url,5000);
            WebSocket connected = ws.connect();
            result.sampleEnd();
            result.setSuccessful(true);
            result.setResponseCodeOK();
            result.setResponseMessage("WebSocket Connected " + connected.getState().toString());
            logger.info("WebSocket Connected");

        } catch (IOException e) {
            result.sampleEnd();
            result.setSuccessful(false);
            result.setResponseCode("FAILED");
            result.setResponseMessage("IOExceptio: " + e.getMessage());
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (WebSocketException e) {
            result.sampleEnd();
            result.setSuccessful(false);
            result.setResponseCode("FAILED");
            result.setResponseMessage("WS Exception: " + e.getMessage());
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {

        }
        return result;
    }


    public void threadStarted() {

    }

    public void threadFinished() {
        dispose(ConnectionCache.getCachedWSConnection().get());
    }

    private void dispose(WebSocket ws) {
        if (ws != null) {
            try {
                if(ws.getState()==WebSocketState.OPEN) {
                    ws.disconnect();
                }
                ws=null;
                ConnectionCache.getCachedWSConnection().remove();
            } catch (Exception ex) {

            }

        }
    }

    public boolean getTLS() {
        return getPropertyAsBoolean("TLS");
    }

    public void setTLS(boolean value) {
        setProperty("TLS", value);
    }

    public String getServer() {
        return getPropertyAsString("server");
    }

    public void setServer(String server) {
        setProperty("server", server);
    }

    public String getPort() {
        return getPropertyAsString("port");
    }

    public void setPort(String port) {
        setProperty("port", port);
    }

    public String getPath() {
        return getPropertyAsString("path");
    }

    public void setPath(String path) {
        setProperty("path", path);
    }


}
