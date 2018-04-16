package com.yxzhm.jmeter;

import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;

import javax.swing.*;
import java.awt.*;

public class CommandSamplerGui extends AbstractSamplerGui {
    private CommandSamplerGuiPanel settingsPanel;

    public CommandSamplerGui() {
        init();
    }

    private void init() {
        super.setName(getLabelResource());
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH);
        settingsPanel = new CommandSamplerGuiPanel();
        add(settingsPanel, BorderLayout.CENTER);
    }

    @Override
    public String getStaticLabel() {
        return "Nuance WebSocket Command Sampler";
    }

    public String getLabelResource() {
        return getStaticLabel();
    }


    public TestElement createTestElement() {
        CommandSampler element = new CommandSampler();
        configureTestElement(element);
        return element;
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        if (element instanceof CommandSampler) {
            CommandSampler sampler = (CommandSampler) element;
            int commandNum = sampler.getCommandNum();
            if (commandNum > 0) {
                if(settingsPanel.getCommandPanel().getComponentCount()!=commandNum) {
                    settingsPanel.getCommandPanel().removeAll();
                    for (int i = 0; i < commandNum; i++) {
                        String content = sampler.getCommandContent(i);
                        CommandSamplerGuiPanel.CommandType type = sampler.getCommandType(i);
                        settingsPanel.addNewCommandGui(content, type);
                    }
                    settingsPanel.refresh();
                }
            }
        }
    }

    public void modifyTestElement(TestElement testElement) {
        super.configureTestElement(testElement);
        if (testElement instanceof CommandSampler) {
            CommandSampler sampler = (CommandSampler) testElement;

            int commandNum = settingsPanel.getCommandPanel().getComponentCount();
            sampler.setCommandNum(commandNum);

            for (int i = 0; i < commandNum; i++) {
                Component component = settingsPanel.getCommandPanel().getComponent(i);
                if (component instanceof JPanel) {
                    JPanel panel = (JPanel) component;
                    if (panel.getComponentCount() > 0) {
                        Component field = panel.getComponent(0);
                        if (field instanceof JTextArea) {
                            JTextArea area = (JTextArea) field;
                            sampler.setCommandType(CommandSamplerGuiPanel.CommandType.JSON, i);
                            sampler.setCommandContent(area.getText(), i);
                        } else if (field instanceof JTextField) {
                            JTextField test = (JTextField) field;
                            sampler.setCommandType(CommandSamplerGuiPanel.CommandType.AUDIO, i);
                            sampler.setCommandContent(test.getText(), i);
                        }
                    }
                }
            }

        }
    }
}
