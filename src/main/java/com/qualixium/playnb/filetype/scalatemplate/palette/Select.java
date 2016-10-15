package com.qualixium.playnb.filetype.scalatemplate.palette;

import java.util.ArrayList;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import org.openide.text.ActiveEditorDrop;

public class Select implements ActiveEditorDrop {

    public String formVariableName;
    public String name;
    public String id;
    public String label;
    public String clazz;
    public int size;
    public boolean disabled;
    public boolean multiple;
    public boolean required;

    private String createBody() {
        StringBuilder sbBody = new StringBuilder();
        if (HTMLPaletteUtilities.PREFIX_WITH_HELPER) {
            sbBody.append("@helper.select(");
        } else {
            sbBody.append("@select(");
        }
        sbBody.append(formVariableName).append("(\"").append(name).append("\")");
        
        List<String> args = new ArrayList<>();
        args.add("options = options(\"Yes\",\"No\")");
        
        if (!id.isEmpty()) {
            args.add("'id -> \"" + id + "\"");
        }
        if (!label.isEmpty()) {
            args.add("'_label -> \"" + label + "\"");
        }
        if (!clazz.isEmpty()) {
            args.add("'class -> \"" + clazz + "\"");
        }
        if (size != 1) {
            args.add("'size -> \"" + size + "\"");
        }
        if (disabled) {
            args.add("'disabled -> \"disabled\"");
        }
        if (multiple) {
            args.add("'multiple -> \"multiple\"");
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
        SelectCustomizer c = new SelectCustomizer(this, targetComponent);
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
