package com.qualixium.playnb.filetype.scalatemplate.palette;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.text.JTextComponent;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.NbBundle;

public class TextAreaCustomizer extends javax.swing.JPanel {

    private Dialog dialog = null;
    private DialogDescriptor descriptor = null;
    private boolean dialogOK = false;
    private final TextArea textArea;
    private final JTextComponent target;

    /**
     * Creates new form InputTextCustomizer
     */
    public TextAreaCustomizer(TextArea textArea, JTextComponent target) {
        this.textArea = textArea;
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        chDisabled = new javax.swing.JCheckBox();
        chReadOnly = new javax.swing.JCheckBox();
        chRequired = new javax.swing.JCheckBox();
        txtName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtFormVariableName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        ftfMaxLength = new javax.swing.JFormattedTextField();
        jLabel6 = new javax.swing.JLabel();
        txtLabel = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtClass = new javax.swing.JTextField();
        chPrefixWithHelper = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        spCols = new javax.swing.JSpinner();
        spRows = new javax.swing.JSpinner();
        jLabel10 = new javax.swing.JLabel();
        chSoft = new javax.swing.JRadioButton();
        chHard = new javax.swing.JRadioButton();
        jLabel11 = new javax.swing.JLabel();
        txtPlaceHolder = new javax.swing.JTextField();

        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });

        jLabel1.setText("Id:");

        chDisabled.setText("Disabled");

        chReadOnly.setText("Read Only");

        chRequired.setText("Required");

        jLabel3.setText("Name:");

        jLabel4.setText("Form variable name:");

        jLabel5.setText("Max Length:");

        ftfMaxLength.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

        jLabel6.setText("Label:");

        jLabel7.setText("Class:");

        chPrefixWithHelper.setText("Prefix with @helper.");

        jLabel8.setText("Cols:");

        jLabel9.setText("Rows");

        spCols.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(20), Integer.valueOf(1), null, Integer.valueOf(1)));

        spRows.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(2), Integer.valueOf(1), null, Integer.valueOf(1)));

        jLabel10.setText("Wrap:");

        buttonGroup1.add(chSoft);
        chSoft.setSelected(true);
        chSoft.setText("soft");

        buttonGroup1.add(chHard);
        chHard.setText("hard");

        jLabel11.setText("PlaceHolder:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel4)
                        .addComponent(jLabel3)
                        .addComponent(jLabel1)
                        .addComponent(jLabel6)
                        .addComponent(jLabel7)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel10)
                                .addComponent(jLabel8)
                                .addComponent(jLabel11))
                            .addGap(3, 3, 3)))
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(chPrefixWithHelper)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtName, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                            .addComponent(txtFormVariableName)
                            .addComponent(txtId)
                            .addComponent(txtLabel)
                            .addComponent(txtClass)
                            .addComponent(txtPlaceHolder)
                            .addComponent(ftfMaxLength)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(spCols, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(chSoft))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel9)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(spRows, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(chHard)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(chDisabled)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(chReadOnly)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(chRequired)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(29, 29, 29))))
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtClass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(spCols, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(spRows, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(chSoft)
                    .addComponent(chHard))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtPlaceHolder))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ftfMaxLength)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chDisabled)
                    .addComponent(chReadOnly)
                    .addComponent(chRequired))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chPrefixWithHelper)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
        txtFormVariableName.requestFocus();
    }//GEN-LAST:event_formFocusGained

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox chDisabled;
    private javax.swing.JRadioButton chHard;
    private javax.swing.JCheckBox chPrefixWithHelper;
    private javax.swing.JCheckBox chReadOnly;
    private javax.swing.JCheckBox chRequired;
    private javax.swing.JRadioButton chSoft;
    private javax.swing.JFormattedTextField ftfMaxLength;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSpinner spCols;
    private javax.swing.JSpinner spRows;
    private javax.swing.JTextField txtClass;
    private javax.swing.JTextField txtFormVariableName;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtLabel;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPlaceHolder;
    // End of variables declaration//GEN-END:variables

    @NbBundle.Messages({"LBL_Customizer_InsertPrefix_TextArea=Insert TextArea", "NAME_Play-TextArea=Text Area"})
    public boolean showDialog() {
        dialogOK = false;
        descriptor = new DialogDescriptor(this, Bundle.LBL_Customizer_InsertPrefix_InputText(), true, DialogDescriptor.OK_CANCEL_OPTION, DialogDescriptor.OK_OPTION, new ActionListener() {
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
        textArea.formVariableName = txtFormVariableName.getText();
        textArea.name = txtName.getText();
        textArea.id = txtId.getText();
        textArea.label = txtLabel.getText();
        textArea.clazz = txtClass.getText();
        textArea.cols = (int) spCols.getValue();
        textArea.rows = (int) spRows.getValue();
        textArea.wrap = chSoft.isSelected() ? "soft" : "hard";
        textArea.placeHolder = txtPlaceHolder.getText();
        if (ftfMaxLength.getText().isEmpty()) {
            textArea.maxLength = 0;
        } else {
            textArea.maxLength = Integer.valueOf(ftfMaxLength.getText());
        }
        textArea.disabled = chDisabled.isSelected();
        textArea.readOnly = chReadOnly.isSelected();
        textArea.required = chRequired.isSelected();
    }

    private void myOwnInitComponents() {
        chPrefixWithHelper.setSelected(HTMLPaletteUtilities.PREFIX_WITH_HELPER);
        txtClass.setText(textArea.clazz == null ? "" : textArea.clazz);
        txtFormVariableName.setText(HTMLPaletteUtilities.MY_FORM_VARIABLE_NAME);
        txtFormVariableName.selectAll();
    }

}