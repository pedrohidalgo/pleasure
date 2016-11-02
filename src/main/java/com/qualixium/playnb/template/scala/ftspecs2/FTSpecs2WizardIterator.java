package com.qualixium.playnb.template.scala.ftspecs2;

import com.qualixium.playnb.PlayProject;
import static com.qualixium.playnb.project.PlayProjectUtil.PLAY_SCALA_FOLDER_TEMPLATES;
import com.qualixium.playnb.template.TemplateUtil;
import static com.qualixium.playnb.util.MiscUtil.LINE_SEPARATOR;
import com.qualixium.playnb.util.MiscUtil.Language;
import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.project.Project;
import org.netbeans.api.templates.TemplateRegistration;
import org.netbeans.spi.project.ui.templates.support.Templates;
import org.openide.WizardDescriptor;
import org.openide.util.NbBundle.Messages;

@TemplateRegistration(folder = PLAY_SCALA_FOLDER_TEMPLATES, position = 7, displayName = "#FTSpecs2WizardIterator_displayName",
        iconBase = "com/qualixium/playnb/template/scala/specs2/specs2_icon.png", description = "ftSpecs2.html")
@Messages("FTSpecs2WizardIterator_displayName=Functional Test Specs2")
public final class FTSpecs2WizardIterator implements WizardDescriptor.InstantiatingIterator<WizardDescriptor> {

    private int index;

    private WizardDescriptor wizard;
    private List<WizardDescriptor.Panel<WizardDescriptor>> panels;

    private List<WizardDescriptor.Panel<WizardDescriptor>> getPanels() {
        if (panels == null) {
            panels = new ArrayList<>();
            panels.add(new FTSpecs2WizardPanel1(wizard));
            String[] steps = createSteps();
            for (int i = 0; i < panels.size(); i++) {
                Component c = panels.get(i).getComponent();
                if (steps[i] == null) {
                    // Default step name to component name of panel. Mainly
                    // useful for getting the name of the target chooser to
                    // appear in the list of steps.
                    steps[i] = c.getName();
                }
                if (c instanceof JComponent) { // assume Swing components
                    JComponent jc = (JComponent) c;
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, i);
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
                    jc.putClientProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, true);
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, true);
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, true);
                }
            }
        }

        return panels;
    }

    @Override
    public Set<?> instantiate() throws IOException {
        String className = (String) wizard.getProperty("clasName");
        String packageName = "";
        if (wizard.getProperty("packageName") != null) {
            packageName = (String) wizard.getProperty("packageName");
        }
        return TemplateUtil.createTemplateFile(wizard, getSource(packageName, className), Language.SCALA);
    }

    @Override
    public void initialize(WizardDescriptor wizard) {
        this.wizard = wizard;
    }

    @Override
    public void uninitialize(WizardDescriptor wizard) {
        panels = null;
    }

    @Override
    public WizardDescriptor.Panel<WizardDescriptor> current() {
        return getPanels().get(index);
    }

    @Override
    public String name() {
        return index + 1 + ". from " + getPanels().size();
    }

    @Override
    public boolean hasNext() {
        return index < getPanels().size() - 1;
    }

    @Override
    public boolean hasPrevious() {
        return index > 0;
    }

    @Override
    public void nextPanel() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        index++;
    }

    @Override
    public void previousPanel() {
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }
        index--;
    }

    // If nothing unusual changes in the middle of the wizard, simply:
    @Override
    public void addChangeListener(ChangeListener l) {
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
    }
    // If something changes dynamically (besides moving between panels), e.g.
    // the number of panels changes in response to user input, then use
    // ChangeSupport to implement add/removeChangeListener and call fireChange
    // when needed

    // You could safely ignore this method. Is is here to keep steps which were
    // there before this wizard was instantiated. It should be better handled
    // by NetBeans Wizard API itself rather than needed to be implemented by a
    // client code.
    private String[] createSteps() {
        Project project = Templates.getProject(wizard);
        assert (project instanceof PlayProject) == true : "This wizard only works with Play Projects";

        String[] beforeSteps = (String[]) wizard.getProperty("WizardPanel_contentData");
        assert beforeSteps != null : "This wizard may only be used embedded in the template wizard";
        String[] res = new String[(beforeSteps.length - 1) + panels.size()];
        for (int i = 0; i < res.length; i++) {
            if (i < (beforeSteps.length - 1)) {
                res[i] = beforeSteps[i];
            } else {
                res[i] = panels.get(i - beforeSteps.length + 1).getComponent().getName();
            }
        }
        return res;
    }

    private String getSource(String packageName, String className) {
        String templateSourceCode
                = "${packageDeclaration}"
                + ""
                + "import play.api.test._\n"
                + "import play.api.test.Helpers._\n"
                + "\n"
                + "object ${className} extends PlaySpecification {\n"
                + "  \"The specification\" should {\n"
                + "\n"
                + "    \"have access to HeaderNames\" in {\n"
                + "      USER_AGENT must be_===(\"User-Agent\")\n"
                + "    }\n"
                + "\n"
                + "    \"have access to Status\" in {\n"
                + "      OK must be_===(200)\n"
                + "    }"
                + "\n"
                + "    \"test server logic\" in new WithServer(app = fakeApplicationWithBrowser, port = testPort) {\n"
                + "  // The test payment gateway requires a callback to this server before it returns a result...\n"
                + "      val callbackURL = s\"http://$myPublicAddress/callback\"\n"
                + "\n"
                + "      // await is from play.api.test.FutureAwaits\n"
                + "      val response = await(WS.url(testPaymentGatewayURL).withQueryString(\"callbackURL\" -> callbackURL).get())\n"
                + "\n"
                + "      response.status must equalTo(OK)\n"
                + "    }"
                + "  }\n"
                + "}";

        return templateSourceCode
                .replace("${packageDeclaration}", packageName.isEmpty() ? "" : "package " + packageName + LINE_SEPARATOR + LINE_SEPARATOR)
                .replace("${className}", className);
    }

}
