package com.qualixium.playnb.actions;

import com.qualixium.playnb.PlayProject;
import com.qualixium.playnb.project.PlayProjectUtil;
import com.qualixium.playnb.project.specific.ProjectSpecificSettings;
import static com.qualixium.playnb.project.specific.ProjectSpecificSettings.KEYS_COMMAND_PARAMETERS;
import static com.qualixium.playnb.project.specific.ProjectSpecificSettings.KEYS_DEBUG_PORT;
import static com.qualixium.playnb.project.specific.ProjectSpecificSettings.KEYS_RUN_PORT;
import com.qualixium.playnb.util.ExceptionManager;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import org.netbeans.api.debugger.jpda.DebuggerStartException;
import org.netbeans.api.debugger.jpda.JPDADebugger;
import org.netbeans.api.extexecution.ExecutionDescriptor;
import org.netbeans.api.extexecution.ExecutionService;
import org.netbeans.api.extexecution.ExternalProcessBuilder;
import org.openide.LifecycleManager;
import org.openide.awt.StatusDisplayer;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 *
 * @author pedro
 */
public class ActionsProcessor {

    private final PlayProject playProject;

    public ActionsProcessor(PlayProject project) {
        this.playProject = project;
    }

    public enum ActionsEnum {

        RUN("run", "Run", true),
        RUN_AUTOCOMPILE("~run", "Run with compile on save", KeyStroke.getKeyStroke("F6"), true),
        CLEAN("clean", "Clean", true),
        COMPILE("compile", "Compile", KeyStroke.getKeyStroke("F11"), true),
        CLEAN_BUILD("clean compile", "Clean and Compile", KeyStroke.getKeyStroke(KeyEvent.VK_F11, InputEvent.SHIFT_DOWN_MASK), true),
        TEST("test", "Test", true),
        TEST_AUTOCOMPILE("~test", "Test continually", KeyStroke.getKeyStroke(KeyEvent.VK_F6, InputEvent.ALT_DOWN_MASK), true),
        TEST_ONLY("testOnly ${param1}", "Test File", true),
        TEST_ONLY_AUTOCOMPILE("~testOnly ${param1}", "Test File continually", true),
        DEBUG("-jvm-debug ${debugPort} run", "Debug", true),
        DEBUG_AUTOCOMPILE("-jvm-debug ${debugPort} ~run", "Debug with compile on save", KeyStroke.getKeyStroke(KeyEvent.VK_F5, InputEvent.CTRL_DOWN_MASK), true),
        //        NOT STANDARD ACTIONS BELOW...
        SBT_SCOVERAGE("sbt-coverage", "Run sbt-scoverage  (Scala)", false),
        JACOCO4SBT("jacoco", "Run jacoco4sbt  (Java and Scala)", false),
        RELOAD_PROJECT("reload", "Reload Project ClassPaths", false);

        public final String command;
        public final String name;
        public final Optional<KeyStroke> keyStrokeOptional;
        public final boolean isStandard;

        private ActionsEnum(String command, String name, boolean isStandard) {
            this.command = command;
            this.name = name;
            this.isStandard = isStandard;
            this.keyStrokeOptional = Optional.empty();
        }

        private ActionsEnum(String command, String name, KeyStroke keyStrokeOptional, boolean isStandard) {
            this.command = command;
            this.name = name;
            this.keyStrokeOptional = Optional.ofNullable(keyStrokeOptional);
            this.isStandard = isStandard;
        }

    }

    public void executeAction(ActionsEnum action) {
        executeAction(action, Optional.empty());
    }

    public void executeAction(ActionsEnum action, Optional<String> param1Optional) {
        try {
            if (PlayProjectUtil.isActivatorExecutablePathSpecified()) {
                String projectName = playProject.getProjectName();
                if (action.isStandard) {
                    closePreviousTabs(projectName);

                    ExecutionDescriptor descriptor = new ExecutionDescriptor()
                            .frontWindow(true)
                            .showProgress(true)
                            .inputVisible(true)
                            .preExecution(() -> LifecycleManager.getDefault().saveAll())
                            .controllable(true);

                    ExternalProcessBuilder processBuilder = new ExternalProcessBuilder(PlayProjectUtil.getActivatorExecutablePath())
                            .workingDirectory(new File(playProject.getProjectDirectory().getPath()));

                    processBuilder = PlayProjectUtil.addJavaHomeVariable(processBuilder);

                    String commandString = getCommandString(action);
                    String[] arguments;
                    if (param1Optional.isPresent()) {
                        String argumentTemp = commandString.replace("${param1}", param1Optional.get());
                        if (action.equals(ActionsEnum.TEST_ONLY) || action.equals(ActionsEnum.TEST_ONLY_AUTOCOMPILE)) {
                            arguments = new String[]{argumentTemp};
                        } else {
                            arguments = argumentTemp.split(" ");
                        }
                    } else {
                        arguments = commandString.split(" ");
                    }

                    for (String argument : arguments) {
                        processBuilder = processBuilder.addArgument(argument);
                    }

                    String displayName = getTabDisplayName(projectName, action, param1Optional);
                    ExecutionService service = ExecutionService.newService(processBuilder,
                            descriptor, displayName);

                    StatusDisplayer.getDefault().setStatusText("(" + projectName + ") Executing SBT TASK: [" + commandString + "]");

                    service.run();

                    if (action.equals(ActionsEnum.DEBUG_AUTOCOMPILE)
                            || action.equals(ActionsEnum.DEBUG)) {
                        attachDebugger();
                    }

                } else {
                    switch (action) {
                        case RELOAD_PROJECT:
                            playProject.getClassPathProviderImpl().reloadProject();
                            break;
                        case SBT_SCOVERAGE:
                            ActionsHelper.runSBTSCoverage(playProject);
                            break;
                        case JACOCO4SBT:
                            ActionsHelper.runJacoco4sbt(playProject);
                            break;
                        default:
                            break;
                    }
                }
            } else {
                PlayProjectUtil.openPlayFrameworkOptionWindow();
            }
        } catch (Exception ex) {
            ExceptionManager.logException(ex);
        }
    }

    private String getCommandString(ActionsEnum action) {
        try {
            ProjectSpecificSettings settings = new ProjectSpecificSettings(playProject.getProjectDirectory().getPath());
            StringBuilder sbCommand = new StringBuilder();
            if (action.equals(ActionsEnum.RUN) || action.equals(ActionsEnum.RUN_AUTOCOMPILE)) {
                sbCommand.append(action.command);

                Optional<String> runPortOptional = settings.getValue(KEYS_RUN_PORT);
                if (runPortOptional.isPresent()) {
                    sbCommand.append(" -Dhttp.port=").append(runPortOptional.get());
                }
                sbCommand.append(" ").append(settings.getValue(KEYS_COMMAND_PARAMETERS).orElse(""));
                return sbCommand.toString();
            } else if (action.equals(ActionsEnum.DEBUG) || action.equals(ActionsEnum.DEBUG_AUTOCOMPILE)) {
                String debugPort = settings.getValue(KEYS_DEBUG_PORT).orElse("9999");
                sbCommand.append(action.command.replace("${debugPort}", debugPort));
                sbCommand.append(" -Dhttp.port=").append(settings.getValue(KEYS_RUN_PORT).orElse("9000"));
                sbCommand.append(" ").append(settings.getValue(KEYS_COMMAND_PARAMETERS).orElse(""));

                return sbCommand.toString();
            }

        } catch (IOException ex) {
            ExceptionManager.logException(ex);
        }
        return action.command;
    }

    private void attachDebugger() {
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                StatusDisplayer.getDefault().setStatusText("trying to attach debugger...");
                ProjectSpecificSettings settings = new ProjectSpecificSettings(playProject.getProjectDirectory().getPath());
                int debugPort = Integer.parseInt(settings.getValue(KEYS_DEBUG_PORT).orElse("9999"));
                JPDADebugger.attach("localhost", debugPort, new Object[0]);
            } catch (InterruptedException | IOException | DebuggerStartException ex) {
                ExceptionManager.logException(ex);
            }
        }).start();
    }

    private void closePreviousTabs(String projectName) {
        for (ActionsEnum actionEnum : ActionsEnum.values()) {
            String commandString = actionEnum.command;
            if (!commandString.contains("run")
                    && !commandString.contains("test")
                    && !commandString.contains("debug")) {
                InputOutput inputOutput = IOProvider.getDefault().getIO(
                        getTabDisplayName(projectName, actionEnum, Optional.empty()), false);
                if (inputOutput != null) {
                    inputOutput.closeInputOutput();
                }
            }
        }
    }

    private String getTabDisplayName(String projectName, ActionsEnum action, Optional<String> param1Optional) {
        String firstPart = "(" + projectName + ") ";
        if (action.equals(ActionsEnum.DEBUG)) {
            return firstPart + "Debug";
        } else if (action.equals(ActionsEnum.DEBUG_AUTOCOMPILE)) {
            return firstPart + "~Debug";
        } else if (action.equals(ActionsEnum.TEST_ONLY) || action.equals(ActionsEnum.TEST_ONLY_AUTOCOMPILE)) {
            if (param1Optional.isPresent()) {
                return firstPart + action.command.replace("${param1}", param1Optional.get());
            }
        }

        return firstPart + action.command;
    }

    public AbstractAction createAction(ActionsEnum action) {
        AbstractAction newAction = new AbstractAction(action.name) {

            @Override
            public void actionPerformed(ActionEvent e) {
                executeAction(action);
            }
        };

        if (action.keyStrokeOptional.isPresent()) {
            newAction.putValue(Action.ACCELERATOR_KEY, action.keyStrokeOptional.get());
        }

        return newAction;
    }

}
