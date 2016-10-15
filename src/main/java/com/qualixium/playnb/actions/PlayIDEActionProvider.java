package com.qualixium.playnb.actions;

import com.qualixium.playnb.PlayProject;
import com.qualixium.playnb.actions.ActionsProcessor.ActionsEnum;
import org.netbeans.spi.project.ActionProvider;
import org.openide.util.Lookup;

/**
 *
 * @author pedro
 */
public class PlayIDEActionProvider implements ActionProvider {

    private final ActionsProcessor actionsProcessor;

    public PlayIDEActionProvider(PlayProject playProject) {
        this.actionsProcessor = new ActionsProcessor(playProject);
    }

    @Override
    public String[] getSupportedActions() {
        return new String[]{
            ActionProvider.COMMAND_RUN,
            ActionProvider.COMMAND_CLEAN,
            ActionProvider.COMMAND_REBUILD,
            ActionProvider.COMMAND_BUILD,
            ActionProvider.COMMAND_TEST,
            ActionProvider.COMMAND_DEBUG};
    }

    @Override
    public void invokeAction(String command, Lookup lookup) throws IllegalArgumentException {
        switch (command) {
            case COMMAND_RUN:
                actionsProcessor.executeAction(ActionsEnum.RUN_AUTOCOMPILE);
                break;
            case COMMAND_BUILD:
                actionsProcessor.executeAction(ActionsEnum.COMPILE);
                break;
            case COMMAND_CLEAN:
                actionsProcessor.executeAction(ActionsEnum.CLEAN);
                break;
            case COMMAND_REBUILD:
                actionsProcessor.executeAction(ActionsEnum.CLEAN_BUILD);
                break;
            case COMMAND_TEST:
                actionsProcessor.executeAction(ActionsEnum.TEST_AUTOCOMPILE);
                break;
            case COMMAND_DEBUG:
                actionsProcessor.executeAction(ActionsEnum.DEBUG_AUTOCOMPILE);
                break;
        }
    }

    @Override
    public boolean isActionEnabled(String string, Lookup lkp) throws IllegalArgumentException {
        return true;
    }

}
