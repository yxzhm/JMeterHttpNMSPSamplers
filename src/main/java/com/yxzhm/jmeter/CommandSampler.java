package com.yxzhm.jmeter;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketState;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.ThreadListener;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static jodd.util.ThreadUtil.sleep;

public class CommandSampler extends AbstractSampler implements ThreadListener {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CommandSampler.class);

    public SampleResult sample(Entry entry) {
        SampleResult result = new SampleResult();
        result.setSampleLabel(getName());
        result.setDataType(org.apache.jmeter.samplers.SampleResult.TEXT);
        WebSocket ws = Utility.getCachedWSConnection().get();

        if (ws == null) {
            logger.info("The WS is null ");
            result.setSuccessful(false);
            result.setResponseCode("FAILED");
            result.setResponseMessage("WebSocket is null");
        } else if (ws.getState() != WebSocketState.OPEN) {
            logger.info("The WS status is " + ws.getState());
            result.setSuccessful(false);
            result.setResponseCode("FAILED");
            result.setResponseMessage("WebSocket status is " + ws.getState());
        } else {
            int commandNum = getCommandNum();
            logger.info("Start the send " + commandNum + " Commands");
            ws.clearListeners();
            final List<String> receivedMsg = new ArrayList<>();
            StringBuffer sb = new StringBuffer();

            ws.addListener(new WebSocketAdapter() {
                @Override
                public void onTextMessage(WebSocket websocket, String message) {
                    logger.info("REV: " + message);
                    receivedMsg.add(message);
                    sb.append(message + Strings.LINE_SEPARATOR);
                }
            });

            for (int i = 0; i < commandNum; i++) {
                CommandSamplerGuiPanel.CommandType type = getCommandType(i);
                if (type == CommandSamplerGuiPanel.CommandType.JSON) {
                    String content = getCommandContent(i);
                    logger.info("SED: " + content);
                    ws.sendText(content);
                    sleep(20);
                } else if (type == CommandSamplerGuiPanel.CommandType.AUDIO) {
                    try {
                        Path path = Paths.get(getCommandContent(i));
                        byte[] data = Files.readAllBytes(path);
                        ws.sendBinary(data);
                        logger.info("SED: Audio {} bytes", data.length);
                    } catch (Exception e) {
                        logger.error("Can't open audio file.");
                        e.printStackTrace();
                        result.setSuccessful(false);
                        result.setResponseCode("FAILED");
                        result.setResponseMessage(e.getMessage());
                        return result;
                    }
                }
            }
            result.sampleStart();
            long oldTime = System.currentTimeMillis();
            int timeout = getTimeoutTimer();
            if(timeout<=0){
                timeout=5;
            }
            long timeoutTimer = timeout * 1000;
            boolean isEnd = false;
            while (true) {
                logger.debug("Waiting for messages");
                try {
                    Thread.sleep(50);
                    long deltaTime = System.currentTimeMillis() - oldTime;
                    if (deltaTime > timeoutTimer) {
                        result.setSuccessful(false);
                        result.setResponseCode("FAILED");
                        result.setResponseMessage("Waiting Response Timeout");
                        break;
                    } else {
                        logger.debug("Waiting " + deltaTime + " ms");
                    }

                    for (String msg : receivedMsg) {
                        if (msg.toLowerCase().contains("query_response") && result.isStampedAtStart()) {
                            if(!isEnd) {
                                result.sampleEnd();
                                isEnd=true;
                            }
                        }
                        if (msg.toLowerCase().contains("transaction completed")) {
                            result.setResponseMessage(sb.toString());
                            result.setSuccessful(true);
                            return result;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    result.setSuccessful(false);
                    result.setResponseCode("FAILED");
                    result.setResponseMessage(e.getMessage());
                }
            }


        }
        return result;
    }

    public void threadStarted() {

    }

    public void threadFinished() {
        Utility.dispose(Utility.getCachedWSConnection().get());
    }

    public int getCommandNum() {
        int value = getPropertyAsInt("command_num");
        logger.info("get CommandNum " + value);
        return value;
    }

    public void setCommandNum(int value) {
        logger.info("Set CommandNum " + value);
        setProperty("command_num", value);
    }

    public int getTimeoutTimer(){
        return getPropertyAsInt("timeout");
    }

    public void setTimeoutTimer(int value){
        setProperty("timeout", value);
    }

    public String getCommandContent(int index) {
        return getPropertyAsString("command_content_" + index);
    }

    public void setCommandContent(String value, int index) {
        setProperty("command_content_" + index, value);
    }

    public CommandSamplerGuiPanel.CommandType getCommandType(int index) {
        int type = getPropertyAsInt("command_type_" + index);
        if (type == 0) {
            return CommandSamplerGuiPanel.CommandType.JSON;
        } else {
            return CommandSamplerGuiPanel.CommandType.AUDIO;
        }
    }

    public void setCommandType(CommandSamplerGuiPanel.CommandType type, int index) {
        if (type == CommandSamplerGuiPanel.CommandType.JSON) {
            setProperty("command_type_" + index, 0);
        } else {
            setProperty("command_type_" + index, 1);
        }
    }


}
