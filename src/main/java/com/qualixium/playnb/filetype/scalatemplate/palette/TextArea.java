package com.qualixium.playnb.filetype.scalatemplate.palette;

import java.util.ArrayList;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import org.openide.text.ActiveEditorDrop;

public class TextArea implements ActiveEditorDrop {

    public String formVariableName;
    public String name;
    public String id;
    public String label;
    public String clazz;
    public int cols;
    public int rows;
    public String wrap;
    public String placeHolder;
    public int maxLength;
    public boolean disabled;
    public boolean readOnly;
    public boolean required;

    private String createBody() {
        StringBuilder sbBody = new StringBuilder();
        if (HTMLPaletteUtilities.PREFIX_WITH_HELPER) {
            sbBody.append("@helper.textarea(");
        } else {
            sbBody.append("@textarea(");
        }
        sbBody.append(formVariableName).append("(\"").append(name).append("\")");
        
        List<String> args = new ArrayList<>();
        if (!id.isEmpty()) {
            args.add("'id -> \"" + id + "\"");
        }
        if (!label.isEmpty()) {
            args.add("'_label -> \"" + label + "\"");
        }
        if (!clazz.isEmpty()) {
            args.add("'class -> \"" + clazz + "\"");
        }
        if (cols != 20) {
            args.add("'cols -> \"" + cols + "\"");
        }
        if (rows != 2) {
            args.add("'rows -> \"" + rows + "\"");
        }
        if (!wrap.isEmpty() && !wrap.equals("soft")) {
            args.add("'wrap -> \"" + wrap + "\"");
        }
        if (!placeHolder.isEmpty()) {
            args.add("'placeholder -> \"" + placeHolder + "\"");
        }
        if (maxLength > 0) {
            args.add("'maxlength -> \"" + maxLength + "\"");
        }
        if (disabled) {
            args.add("'disabled -> \"disabled\"");
        }
        if (readOnly) {
            args.add("'readonly -> \"readonly\"");
        }
        if (required) {
            args.add("'required -> \"true\"");
        }

        args.stream().forEach((arg) -> {
            sbBody.append(", ").append(arg);
        });
        sbBody.append(")");

        return sbBody.toString();
    }

    @Override
    public boolean handleTransfer(JTextComponent targetComponent) {
        TextAreaCustomizer c = new TextAreaCustomizer(this, targetComponent);
        boolean accept = c.showDialog();
        if (accept) {
            String body = createBody();
            try {
                HTMLPaletteUtilities.insert(body, targetComponent);
            } catch (BadLocationException ble) {
                accept = false;
            }
        }
        return accept;
    }
}
