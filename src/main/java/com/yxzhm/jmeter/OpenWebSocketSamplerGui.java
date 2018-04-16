package com.yxzhm.jmeter;

import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;

import java.awt.*;

public class OpenWebSocketSamplerGui extends AbstractSamplerGui {
    private OpenWebSocketSamplerGuiPanel settingsPanel;

    public OpenWebSocketSamplerGui() {
        init();
    }

    private void init() {
        super.setName(getLabelResource());
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH);
        settingsPanel = new OpenWebSocketSamplerGuiPanel();
        add(settingsPanel, BorderLayout.CENTER);
    }

    @Override
    public String getStaticLabel() {
        return "Nuance WebSocket Open Sampler";
    }

    public String getLabelResource() {
        return getStaticLabel();
    }


    public TestElement createTestElement() {
        OpenWebSocketSampler element = new OpenWebSocketSampler();
        configureTestElement(element);
        return element;
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        if (element instanceof OpenWebSocketSampler) {
            OpenWebSocketSampler sampler = (OpenWebSocketSampler) element;
            settingsPanel.setServer(sampler.getServer());
            settingsPanel.setPort(sampler.getPort());
            settingsPanel.setPath(sampler.getPath());
            settingsPanel.setTLS(sampler.getTLS());
        }
    }

    public void modifyTestElement(TestElement testElement) {
        super.configureTestElement(testElement);
        if (testElement instanceof OpenWebSocketSampler) {
            OpenWebSocketSampler sampler = (OpenWebSocketSampler) testElement;
            sampler.setTLS(settingsPanel.getTLS());
            sampler.setServer(settingsPanel.getServer());
            sampler.setPort(settingsPanel.getPort());
            sampler.setPath(settingsPanel.getPath());
        }
    }
}
