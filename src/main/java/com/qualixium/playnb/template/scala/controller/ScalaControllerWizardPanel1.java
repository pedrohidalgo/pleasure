/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qualixium.playnb.template.scala.controller;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;

public class ScalaControllerWizardPanel1  implements WizardDescriptor.ValidatingPanel<WizardDescriptor>, DocumentListener {

    private boolean isValid = true;
    private final Set<ChangeListener> listeners = new HashSet<>(1);
    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private ScalaControllerVisualPanel1 component;
    private final WizardDescriptor wizard;

    ScalaControllerWizardPanel1(WizardDescriptor wizard) {
        this.wizard = wizard;
    }

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    @Override
    public ScalaControllerVisualPanel1 getComponent() {
        if (component == null) {
            component = new ScalaControllerVisualPanel1(wizard);
            component.getTxtName().getDocument().addDocumentListener(this);
        }
        return component;
    }

    @Override
    public HelpCtx getHelp() {
        // Show no Help button for this panel:
        return HelpCtx.DEFAULT_HELP;
        // If you have context help:
        // return new HelpCtx("help.key.here");
    }

    @Override
    public boolean isValid() {
        // If it is always OK to press Next or Finish, then:
        return isValid;
        // If it depends on some condition (form filled out...) and
        // this condition changes (last form field filled in...) then
        // use ChangeSupport to implement add/removeChangeListener below.
        // WizardDescriptor.ERROR/WARNING/INFORMATION_MESSAGE will also be useful.
    }

    @Override
    public void addChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.add(l);
        }
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }

    @Override
    public void readSettings(WizardDescriptor wiz) {
        // use wiz.getProperty to retrieve previous panel state
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {
        // use wiz.putProperty to remember current panel state
        wiz.putProperty("clasName", component.getTxtName().getText());
        if (!component.getCbxPackageName().getEditor().getItem().toString().isEmpty()) {
            wiz.putProperty("packageName", component.getCbxPackageName().getEditor().getItem().toString());
        }
    }

    @Override
    public void validate() throws WizardValidationException {
        if (component.getTxtName().getText().isEmpty()) {
            isValid = false;
            throw new WizardValidationException(component.getTxtName(), "Invalid Name", null);
        }
        File fileToCreate = new File(component.getTxtCreatedFile().getText());
        if(fileToCreate.exists()){
            isValid = false;
            throw new WizardValidationException(component.getTxtName(), "The File "+fileToCreate.getName()+" already exists", null);
        }
    }

    protected final void fireChangeEvent() {
        wizard.putProperty("WizardPanel_errorMessage", "");
        Iterator<ChangeListener> it;
        synchronized (listeners) {
            it = new HashSet<>(listeners).iterator();
        }
        ChangeEvent ev = new ChangeEvent(this);
        while (it.hasNext()) {
            it.next().stateChanged(ev);
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        change();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        change();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        change();
    }

    private void change() {
        String currentText = component.getTxtName().getText();
        boolean isValidInput = currentText != null && currentText.length() > 0;
        if (isValidInput) {
            setValid(true);
        } else {
            setValid(false);
        }
    }

    private void setValid(boolean val) {
        if (isValid != val) {
            isValid = val;
            fireChangeEvent();  // must do this to enable next/finish button
        }
    }

}
