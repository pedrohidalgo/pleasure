/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qualixium.playnb.project.newprojectwizard;

import com.qualixium.playnb.project.PlayProjectUtil;
import com.qualixium.playnb.project.general.PlayPanel;
import static com.qualixium.playnb.project.general.PlayPanel.ACTIVATOR_EXECUTABLE_PATH;
import com.qualixium.playnb.util.ExceptionManager;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.extexecution.ExecutionDescriptor;
import org.netbeans.api.extexecution.ExecutionService;
import org.netbeans.api.extexecution.ExternalProcessBuilder;
import org.netbeans.api.templates.TemplateRegistration;
import org.netbeans.spi.project.ui.support.ProjectChooser;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;
import org.openide.util.NbPreferences;

@TemplateRegistration(folder = "Project/Play",
        displayName = "#PlayTemplate_displayName",
        description = "PlayTemplateDescription.html",
        iconBase = "com/qualixium/playnb/play_icon.png")
@Messages("PlayTemplate_displayName=Play Framework App")
public class PlayTemplateWizardIterator implements WizardDescriptor./*Progress*/InstantiatingIterator {

    private int index;
    private WizardDescriptor.Panel[] panels;
    private WizardDescriptor wiz;

    public PlayTemplateWizardIterator() {
    }

    public static PlayTemplateWizardIterator createIterator() {
        return new PlayTemplateWizardIterator();
    }

    private WizardDescriptor.Panel[] createPanels() {
        return new WizardDescriptor.Panel[]{
            new PlayTemplateWizardPanel(),};
    }

    private String[] createSteps() {
        return new String[]{
            NbBundle.getMessage(PlayTemplateWizardIterator.class, "LBL_CreateProjectStep")
        };
    }

    @Override
    public Set/*<FileObject>*/ instantiate(/*ProgressHandle handle*/) throws IOException {
        Set<FileObject> resultSet = new LinkedHashSet<>();
        String activatorInstallationDir = NbPreferences.forModule(PlayPanel.class).get(ACTIVATOR_EXECUTABLE_PATH, "");

        if (!activatorInstallationDir.trim().isEmpty()) {
            String activatorExecutablePath = activatorInstallationDir + "/" + PlayProjectUtil.getActivatorCommandString();
            File dirF = FileUtil.normalizeFile((File) wiz.getProperty("projdir"));
            dirF.mkdirs(); //create all subdirectories if they don't exists of this project dir
            dirF.delete(); //delete the current app dir because activator will create this folder

            String appName = (String) wiz.getProperty("name");
            File parent = dirF.getParentFile();

            String templateName = (String) wiz.getProperty("templateName");

            ExecutionDescriptor descriptor = new ExecutionDescriptor()
                    .frontWindow(true)
                    .showProgress(true);

            String commandTitle = "activator new " + appName + " " + templateName;

            ExternalProcessBuilder processBuilder = new ExternalProcessBuilder(activatorExecutablePath)
                    .workingDirectory(parent)
                    .addArgument("new")
                    .addArgument(appName)
                    .addArgument(templateName);

            processBuilder = PlayProjectUtil.addJavaHomeVariable(processBuilder);

            ExecutionService service = ExecutionService.newService(processBuilder, descriptor, commandTitle);
            Future<Integer> futureRun = service.run();

            Integer exitValue;
            try {
                exitValue = futureRun.get();
                if (exitValue == 0) {
                    FileObject dir = FileUtil.toFileObject(new File(
                            parent.getAbsolutePath() + "/" + appName));
                    resultSet.add(dir);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ExceptionManager.logException(ex);
            }

            //Sets the folder last used for creating a new project.
            if (parent != null && parent.exists()) {
                ProjectChooser.setProjectsFolder(parent);
            }

        }
        return resultSet;
    }

    @Override
    public void initialize(WizardDescriptor wiz) {
        this.wiz = wiz;
        index = 0;
        panels = createPanels();
        // Make sure list of steps is accurate.
        String[] steps = createSteps();
        for (int i = 0; i < panels.length; i++) {
            Component c = panels[i].getComponent();
            if (steps[i] == null) {
                // Default step name to component name of panel.
                // Mainly useful for getting the name of the target
                // chooser to appear in the list of steps.
                steps[i] = c.getName();
            }
            if (c instanceof JComponent) { // assume Swing components
                JComponent jc = (JComponent) c;
                jc.putClientProperty("WizardPanel_contentSelectedIndex", new Integer(i));
                jc.putClientProperty("WizardPanel_contentData", steps);
            }
        }
    }

    @Override
    public void uninitialize(WizardDescriptor wiz) {
        this.wiz.putProperty("projdir", null);
        this.wiz.putProperty("name", null);
        this.wiz = null;
        panels = null;
    }

    @Override
    public String name() {
        return MessageFormat.format("{0} of {1}",
                new Object[]{new Integer(index + 1), new Integer(panels.length)});
    }

    @Override
    public boolean hasNext() {
        return index < panels.length - 1;
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

    @Override
    public WizardDescriptor.Panel current() {
        return panels[index];
    }

    // If nothing unusual changes in the middle of the wizard, simply:
    @Override
    public final void addChangeListener(ChangeListener l) {
    }

    @Override
    public final void removeChangeListener(ChangeListener l) {
    }

}
