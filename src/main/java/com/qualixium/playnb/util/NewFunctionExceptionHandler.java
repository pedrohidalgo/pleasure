package com.qualixium.playnb.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Callable;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

public class NewFunctionExceptionHandler extends Handler implements Callable<JButton> {

    private JButton newFunctionButton;
    private NewFunctionActionListener newFunctionActionListener = new NewFunctionActionListener();

    @Override
    public void publish(LogRecord record) {
        if (record.getThrown() != null) {
            System.out.println("*********** HERE IN PUBLIS");
            newFunctionActionListener.setLogRecord(record);
        }
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }

    // Return the button we want to be displayed in the Uncaught Exception Dialog.
    @Override
    public JButton call() throws Exception {
        if (newFunctionButton == null) {
            newFunctionButton = new JButton("Report to Pleasure website");
            newFunctionButton.addActionListener(newFunctionActionListener);
        }

        System.out.println("BEFORE RETURNING NEW BUTTON****************");
        return newFunctionButton;
    }

    private class NewFunctionActionListener implements ActionListener {

        private LogRecord logRecord;

        public NewFunctionActionListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Close our Uncaught Exception Dialog first.
            SwingUtilities.windowForComponent(newFunctionButton).setVisible(false);
            System.out.println("HERE I'LL SEND THE EXCEPTION TO MY SERVER");
        }

        public void setLogRecord(LogRecord logRecord) {
            this.logRecord = logRecord;
        }
    }
}
