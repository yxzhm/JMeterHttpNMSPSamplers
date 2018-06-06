package com.yxzhm.jmeter;

import lombok.Getter;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.BoxLayout.X_AXIS;
import static javax.swing.BoxLayout.Y_AXIS;

public class CommandSamplerGuiPanel extends JPanel {

    enum CommandType {
        JSON,
        AUDIO,
    }

    public CommandSamplerGuiPanel() {
        init();
    }

    JComboBox<String> contentSelector;
    JButton addButton, delButton;
    JPanel opPanel, responsePanel, headerPanel, timeoutPanel;
    JLabel timeoutLabel;

    @Getter
    JTextField timeoutField;


    @Getter
    JPanel commandPanel;


    private void init() {

        headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, Y_AXIS));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0),
                BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""), BorderFactory.createEmptyBorder(3, 5, 5, 0))));


        opPanel = new JPanel();
        opPanel.setLayout(new BoxLayout(opPanel, X_AXIS));
        opPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0),
                BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Add/Remove Messages"), BorderFactory.createEmptyBorder(3, 5, 5, 0))));


        contentSelector = new JComboBox<String>(new String[]{"JSON", "AUDIO"});
        contentSelector.setMaximumSize(new Dimension(contentSelector.getMinimumSize().width, contentSelector.getMinimumSize().height));
        opPanel.add(contentSelector);
        opPanel.add(Box.createHorizontalStrut(10));

        addButton = new JButton("Add");
        addButton.setMaximumSize(new Dimension(80, contentSelector.getMinimumSize().height));
        addButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (contentSelector.getSelectedItem().equals("JSON")) {
                    addNewCommandGui("", CommandType.JSON);
                } else {
                    addNewCommandGui("", CommandType.AUDIO);
                }
                refresh();
            }
        });
        opPanel.add(addButton);
        opPanel.add(Box.createHorizontalStrut(10));

        delButton = new JButton("Remove");
        delButton.setMaximumSize(new Dimension(80, contentSelector.getMinimumSize().height));
        delButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeLastCommandGui();
                refresh();
            }
        });
        opPanel.add(delButton);
        opPanel.add(Box.createHorizontalStrut(10));

        timeoutPanel = new JPanel();
        timeoutPanel.setLayout(new BoxLayout(timeoutPanel, X_AXIS));
        timeoutPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0),
                BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Timeout Timers"), BorderFactory.createEmptyBorder(3, 5, 5, 0))));

        timeoutLabel = new JLabel("Timeout (sec): ");
        timeoutField = new JTextField();
        timeoutField.setColumns(10);
//        timeoutField.setMaximumSize(new Dimension(Integer.MAX_VALUE, timeoutField.getMinimumSize().height));

        timeoutPanel.add(timeoutLabel);
        timeoutPanel.add(timeoutField);

        opPanel.setMaximumSize(timeoutPanel.getMaximumSize());

        commandPanel = new JPanel();
        commandPanel.setLayout(new BoxLayout(commandPanel, Y_AXIS));
        commandPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0),
                BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Messages will be sent out"), BorderFactory.createEmptyBorder(3, 5, 5, 0))));


        responsePanel = new JPanel();
        responsePanel.setLayout(new BoxLayout(responsePanel, X_AXIS));
        responsePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0),
                BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Response Messages Validation"), BorderFactory.createEmptyBorder(3, 5, 5, 0))));


        this.setLayout(new BorderLayout());
        headerPanel.add(opPanel);
        headerPanel.add(timeoutPanel);
        add(headerPanel, BorderLayout.NORTH);
        add(commandPanel, BorderLayout.CENTER);

    }

    public void addNewCommandGui(String content, CommandType type) {
        String panelTitle = "JSON Message";
        if (type == CommandType.AUDIO) {
            panelTitle = "Audio File Path";
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, X_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0),
                BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(panelTitle), BorderFactory.createEmptyBorder(0, 0, 0, 0))));


        JTextComponent field = null;
        if (type == CommandType.JSON) {
            field = new JTextArea();
            field.setText(content);
            ((JTextArea) field).setColumns(10);
            field.setMaximumSize(new Dimension(field.getMaximumSize().width, 150));
        } else if (type == CommandType.AUDIO) {
            field = new JTextField();
            field.setText(content);
            ((JTextField) field).setColumns(10);
            field.setMaximumSize(new Dimension(field.getMaximumSize().width, field.getMinimumSize().height));
        }

        if (field != null) {
            panel.add(field);
            panel.add(Box.createVerticalStrut(10));
            commandPanel.add(panel);
        }
    }

    public void refresh() {
        revalidate();
        repaint();
    }

    private void removeLastCommandGui() {
        int commandSize = commandPanel.getComponents().length;
        if (commandSize >= 1) {
            commandPanel.remove(commandPanel.getComponent(commandSize - 1));
        }

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 1400);
        frame.getContentPane().add(new CommandSamplerGuiPanel());
        frame.setVisible(true);
    }

}
