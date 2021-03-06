package com.qualixium.playnb.filetype.scalatemplate.palette;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.text.JTextComponent;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.NbBundle;

public class InputDateCustomizer extends javax.swing.JPanel {

    private Dialog dialog = null;
    private DialogDescriptor descriptor = null;
    private boolean dialogOK = false;
    private final InputDate inputDate;
    private final JTextComponent target;

    /**
     * Creates new form InputTextCustomizer
     */
    public InputDateCustomizer(InputDate inputDate, JTextComponent target) {
        this.inputDate = inputDate;
        this.target = target;
        initComponents();
        myOwnInitComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        chDisabled = new javax.swing.JCheckBox();
        chReadOnly = new javax.swing.JCheckBox();
        chRequired = new javax.swing.JCheckBox();
        chAutocomplete = new javax.swing.JCheckBox();
        spStep = new javax.swing.JSpinner();
        txtName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtFormVariableName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtLabel = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtClass = new javax.swing.JTextField();
        chPrefixWithHelper = new javax.swing.JCheckBox();

        jLabel1.setText("Id:");

        jLabel2.setText("Step:");

        chDisabled.setText("Disabled");

        chReadOnly.setText("Read Only");

        chRequired.setText("Required");

        chAutocomplete.setSelected(true);
        chAutocomplete.setText("Autocomplete");

        spStep.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));

        jLabel3.setText("Name:");

        jLabel4.setText("Form variable name:");

        jLabel6.setText("Label:");

        jLabel7.setText("Class:");

        chPrefixWithHelper.setText("Prefix with @helper.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(3, 3, 3))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel2))
                        .addGap(4, 4, 4)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtName)
                    .addComponent(txtFormVariableName, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtLabel)
                    .addComponent(txtId, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtClass)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(chDisabled)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chReadOnly)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chRequired)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chAutocomplete))
                            .addComponent(chPrefixWithHelper)
                            .addComponent(spStep, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 22, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtFormVariableName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(txtClass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(spStep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chDisabled)
                    .addComponent(chReadOnly)
                    .addComponent(chRequired)
                    .addComponent(chAutocomplete))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chPrefixWithHelper)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chAutocomplete;
    private javax.swing.JCheckBox chDisabled;
    private javax.swing.JCheckBox chPrefixWithHelper;
    private javax.swing.JCheckBox chReadOnly;
    private javax.swing.JCheckBox chRequired;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JSpinner spStep;
    private javax.swing.JTextField txtClass;
    private javax.swing.JTextField txtFormVariableName;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtLabel;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables

    @NbBundle.Messages({"LBL_Customizer_InsertPrefix_InputDate=Insert inputDate", "NAME_Play-InputDate=InputDate"})
    public boolean showDialog() {
        dialogOK = false;
        descriptor = new DialogDescriptor(this, Bundle.LBL_Customizer_InsertPrefix_InputDate(), true, DialogDescriptor.OK_CANCEL_OPTION, DialogDescriptor.OK_OPTION, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (descriptor.getValue().equals(DialogDescriptor.OK_OPTION)) {
                    evaluateInput();
                    dialogOK = true;
                }
                dialog.dispose();
            }
        });
        dialog = DialogDisplayer.getDefault().createDialog(descriptor);
        dialog.setVisible(true);
        return dialogOK;
    }

    private void evaluateInput() {
        HTMLPaletteUtilities.PREFIX_WITH_HELPER = chPrefixWithHelper.isSelected();
        HTMLPaletteUtilities.MY_FORM_VARIABLE_NAME = txtFormVariableName.getText();

        inputDate.formVariableName = txtFormVariableName.getText();
        inputDate.name = txtName.getText();
        inputDate.id = txtId.getText();
        inputDate.label = txtLabel.getText();
        inputDate.clazz = txtClass.getText();
        inputDate.step = (int) spStep.getValue();
        inputDate.disabled = chDisabled.isSelected();
        inputDate.readOnly = chReadOnly.isSelected();
        inputDate.required = chRequired.isSelected();
        inputDate.autocomplete = chAutocomplete.isSelected();
    }

    private void myOwnInitComponents() {
        chPrefixWithHelper.setSelected(HTMLPaletteUtilities.PREFIX_WITH_HELPER);
        txtFormVariableName.setText(HTMLPaletteUtilities.MY_FORM_VARIABLE_NAME);
        txtClass.setText(inputDate.clazz == null ? "" : inputDate.clazz);
        txtFormVariableName.selectAll();
    }

}
