package team.cascade.spout.makememod.commands;

import team.cascade.spout.makememod.config.CONFIG;
import team.cascade.spout.makememod.exceptions.ConfigNotInitializedException;
import team.cascade.spout.makememod.helper.Chat_Styles;
import team.cascade.spout.makememod.helper.Logger;
import team.cascade.spout.makememod.helper.Messenger;
import team.cascade.spout.makememod.helper.commands.EnumCommand;
import team.cascade.spout.makememod.helper.config.CommentConfiguration;
import team.cascade.spout.makememod.helper.file.CommandsLoadAndSave;
import team.cascade.spout.makememod.helper.file.MessagesLoadAndSave;
import org.spout.api.Spout;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;
import org.spout.api.plugin.CommonPlugin;

/**
 * The child commands of the MakeMeMod
 *
 * @author $Author: dredhorse$
 * @version $FullVersion$
 * @todo make sure that you use your own commands in these two classes
 */
public class EnumMakeMeModCMDS {

    private final CommonPlugin plugin;

    public EnumMakeMeModCMDS(CommonPlugin instance) {
        plugin = instance;
    }

    /**
     * Displays a list of information, mainly the child commands and what they do.
     *
     * @param args
     * @param source
     * @throws CommandException
     */

    @EnumCommand(command = COMMANDS.MAKEMEMOD_HELP)
    public void help(CommandContext args, CommandSource source) throws CommandException {
        Messenger.sendHeader(source, plugin.getName());
        Messenger.send(source, Chat_Styles.BRIGHT_GREEN + "- " + Chat_Styles.CYAN + "/" + COMMANDS.MAKEMEMOD.getRootCommand() + " " + COMMANDS.MAKEMEMOD_HELP.getChildCommand() + Chat_Styles.BRIGHT_GREEN + " : " + COMMANDS.MAKEMEMOD_HELP.getCmdDescription());
        Messenger.send(source, Chat_Styles.BRIGHT_GREEN + "- " + Chat_Styles.CYAN + "/" + COMMANDS.MAKEMEMOD.getRootCommand() + " " + COMMANDS.MAKEMEMOD_INFO.getChildCommand() + Chat_Styles.BRIGHT_GREEN + " : " + COMMANDS.MAKEMEMOD_INFO.getCmdDescription());
        Messenger.send(source, Chat_Styles.BRIGHT_GREEN + "- " + Chat_Styles.CYAN + "/" + COMMANDS.MAKEMEMOD.getRootCommand() + " " + COMMANDS.MAKEMEMOD_RELOAD.getChildCommand() + Chat_Styles.BRIGHT_GREEN + " : " + COMMANDS.MAKEMEMOD_RELOAD.getCmdDescription());
        Messenger.send(source, Chat_Styles.BRIGHT_GREEN + "- " + Chat_Styles.CYAN + "/" + COMMANDS.MAKEMEMOD.getRootCommand() + " " + COMMANDS.MAKEMEMOD_SAVE.getChildCommand() + Chat_Styles.BRIGHT_GREEN + " : " + COMMANDS.MAKEMEMOD_SAVE.getCmdDescription());
    }

    /**
     * Displays some informational messages and tries to display if there is a new version available.
     *
     * @param args
     * @param source
     * @throws CommandException
     */
    @EnumCommand(command = COMMANDS.MAKEMEMOD_INFO)
    public void info(CommandContext args, CommandSource source) throws CommandException {
        Messenger.sendHeader(source, plugin.getName());
        Messenger.send(source, Chat_Styles.BRIGHT_GREEN + plugin.getDescription().getName() + " " + plugin.getDescription().getVersion());
        Messenger.send(source, Chat_Styles.BRIGHT_GREEN + "Copyright (c) " + Messenger.getAuthors(plugin));
        Messenger.send(source, Chat_Styles.BRIGHT_GREEN + plugin.getDescription().getWebsite());
        Messenger.send(source, Chat_Styles.BRIGHT_GREEN + "Powered by Spout " + Spout.getEngine().getVersion());
        Messenger.send(source, Chat_Styles.BRIGHT_GREEN + "( Implementing SpoutAPI " + Spout.getAPIVersion() + " )");
        try {
            if (CommentConfiguration.getNewVersion() != null) {
                Messenger.send(source, "");
                Messenger.send(source, Chat_Styles.BRIGHT_GREEN + "There is a new version available: Version " + CommentConfiguration.getInstance().getNewVersion());
            }
        } catch (ConfigNotInitializedException e) {
            Messenger.sendError(source, "Something went wrong during checking the version. Please check the logs!");
            Logger.warning("A problem occurred during reloading of the config.", e);
        }
    }


    /**
     * Will reload the plugin configuration. If CONFIG_AUTO_SAVE is true we will save the configuration first.
     *
     * @param args
     * @param source
     * @throws CommandException
     */
    @EnumCommand(command = COMMANDS.MAKEMEMOD_RELOAD)
    public void reload(CommandContext args, CommandSource source) throws CommandException {
        Messenger.sendHeader(source, plugin.getName());
        Messenger.send(source, "Reloading config");
        try {
            if (CONFIG.CONFIG_AUTO_SAVE.getBoolean()) {
                CommentConfiguration.getInstance().saveConfig();
            }
            CommentConfiguration.getInstance().loadConfig();
        } catch (ConfigNotInitializedException e) {
            Messenger.sendError(source, "Something went wrong during the reload. Please check the logs!");
            Logger.warning("A problem occurred during reloading of the config.", e);
        }
        CommandsLoadAndSave.reload();
        MessagesLoadAndSave.reload();
        Messenger.send(source, Messenger.wrap("Well") + " we are done.");
    }


    /**
     * Will save the plugin configuration to file. This is helpful when you allow changing configuration via commands <br>
     * and want to allow people to manually save the changes if they have CONFIG_AUTO_SAVE false </p>
     *
     * @param args
     * @param source
     * @throws CommandException
     */
    @EnumCommand(command = COMMANDS.MAKEMEMOD_SAVE)
    public void save(CommandContext args, CommandSource source) throws CommandException {
        Messenger.sendHeader(source, plugin.getName());
        Messenger.send(source, "Saving config");
        try {
            CommentConfiguration.getInstance().saveConfig();
        } catch (ConfigNotInitializedException e) {
            Messenger.sendError(source, "Something went wrong during the save. Please check the logs!");
            Logger.warning("Something went wrong during saving the config.", e);
        }
        Messenger.send(source, Messenger.wrap("Well") + " we are done.");
    }




}
