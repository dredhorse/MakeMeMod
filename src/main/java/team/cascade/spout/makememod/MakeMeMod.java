package team.cascade.spout.makememod;

//~--- non-JDK imports --------------------------------------------------------

import org.spout.api.Engine;
import org.spout.api.Spout;
import org.spout.api.command.CommandRegistrationsFactory;
import org.spout.api.plugin.CommonPlugin;
import team.cascade.spout.makememod.commands.EnumMakeMeMod;
import team.cascade.spout.makememod.config.CONFIG;
import team.cascade.spout.makememod.exceptions.ConfigNotAvailableException;
import team.cascade.spout.makememod.exceptions.WrongClassException;
import team.cascade.spout.makememod.helper.Logger;
import team.cascade.spout.makememod.helper.Metrics;
import team.cascade.spout.makememod.helper.commands.EnumAnnotatedCommandRegistrationFactory;
import team.cascade.spout.makememod.helper.commands.EnumSimpleAnnotatedCommandExecutorFactory;
import team.cascade.spout.makememod.helper.commands.EnumSimpleInjector;
import team.cascade.spout.makememod.helper.config.CommentConfiguration;
import team.cascade.spout.makememod.helper.config.Configuration;
import team.cascade.spout.makememod.helper.file.CommandsLoadAndSave;
import team.cascade.spout.makememod.helper.file.MessagesLoadAndSave;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static team.cascade.spout.makememod.helper.file.CommandsLoadAndSave.commandsInit;
import static team.cascade.spout.makememod.helper.file.MessagesLoadAndSave.messageInit;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Simple test plugin to demonstrate the MakeMeMod
 *
 * @author $Author: dredhorse$
 * @version $FullVersion$
 */
public class MakeMeMod extends CommonPlugin {
    /**
     * Instance of the plugin
     */
    private static MakeMeMod instance;

    /**
     * Use this together with the {@code log = Logger.getLogger(this);} in <br>
     * {@see #onLoad()} to get all the data from the properties.yml
     * This doesn't need later calls to the <br>
     * {@see Logger#setPluginName},<br>
     * {@see Logger#setPluginVersion},<br>
     * {@see Logger#setPluginDirectory},<br>
     * {@see Logger#setLogDirectory}<br>
     * to configure the Logger correctly.
     * <br>
     * Use {@code Logger log = Logger.getLogger();} to use generated defaults from the Class Name. <br>
     * NOTE: This can mean that information goes into the wrong directory if you not set the correct values later in onLoad()
     * Use the methods  setPluginName, setPluginVersion, setPluginDirectory, setLogDirectory to set the correct values.
     */
    private static Logger log;

    //~--- fields -------------------------------------------------------------

    /**
     * Reference to the Configuration
     */
    private Configuration configuration;

    /**
     * The Engine running the plugin, can be Client or Server
     */
    private Engine engine;

    /**
     * Reference to the command Registration Factory
     */
    private CommandRegistrationsFactory<Class<?>> commandRegFactory = null;

    //~--- methods ------------------------------------------------------------

    /**
     * This method is triggered during the loading of the plugin, only implement code which is really
     * needed before the plugin is enabled.
     */
    @Override
    public void onLoad() {
        instance = this;

        /** Logger.getLogger (this) is required if you use Logger log; */
        log = Logger.getLogger(this);

        /** from here onwards you can use log.whatever or Logger.whatever, see for yourself */
        log.debug("Loading");
        engine = getEngine();
    }

    /**
     * This method is triggered when the plugin is enabled. You should implement your main central code in here.
     * <br>
     * In the code here you will find a lot of examples what you are able to do with these classes, but also necessary
     * code to make the classes work.
     * <br>
     * Make changes wisely.
     */
    @Override
    public void onEnable() {

         // creating the new configuration for this plugin
        configuration = new Configuration(this);
        Logger.info("Adding configuration not directly generated via the ENUMS");


        Map<String, Object> defaultAliasList = new HashMap<String, Object>();
        ArrayList<String> defaultBroadcastTargets = new ArrayList<String>();
        defaultBroadcastTargets.add("Admin");
        defaultBroadcastTargets.add("Moderators");
        defaultAliasList.put("mod", "Builder,Moderators");
        defaultAliasList.put("admin", "Builder,Admins");
        try {
            CONFIG.BROADCAST_GROUP.setConfigOption(defaultBroadcastTargets);
            CONFIG.COMMAND_PERMISSION_GROUP_MAPPING.setConfigOption(defaultAliasList);
        } catch (WrongClassException e) {
           Logger.debug("Uupsa",e);
        }

        // lets initialize the config, which means if not there write the default to file and read it, otherwise just read it
        try {
            configuration.initializeConfig();
        } catch (ConfigNotAvailableException e) {
            Logger.info("Well...no config.");
        }

        /*
                   * Now let's read the translations for the messages in game
                   */
        messageInit(this);

        /*
                   * Now let's read the translations for the commands in game
                   */
        commandsInit(this);

        /**
         * Enabling the commands
         */
        Logger.config("Enabling Commands now!");


        // couldn't get easier or?
        registerCommand(EnumMakeMeMod.class);


        /**
         * Enable Metrics  https://github.com/Hidendra/mcstats.org/wiki
         */

        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            Logger.debug("Problems with contacting mcstats.org", e);
        }


        // and now we are done..
        log.enableMsg();
    }

    /**
     * This method is called during disabling of the plugin, this should hold your
     * saving and other cleanup code.
     */
    @Override
    public void onDisable() {
        if (CONFIG.CONFIG_AUTO_SAVE.getBoolean()) {
            Logger.config("Saving configuration");
            if (configuration.saveConfig()){
                Logger.debug("Config saved");
            } else {
                Logger.config("There was a problem saving the configuration.");
            }
            if (CommandsLoadAndSave.reload()){
                Logger.debug("Commands saved");
            } else {
                Logger.config("There was a problem saving the commands.");
            }
            if (MessagesLoadAndSave.reload()){
                Logger.debug("Messages saved");
            } else {
                Logger.config("There was a problem saving the Messages.");
            }
        }
        log.disableMsg();
    }

    /**
     * This method is called during reloading of the spout server. Make sure that you do the right thing.
     */
    @Override
    public void onReload() {
        Logger.info("Reloading configuration");
        configuration.reloadConfig();
        configuration.loadConfig();
        CommandsLoadAndSave.reload();
        MessagesLoadAndSave.reload();
    }


    /**
     * Method to register the commands, you need to pass the CLASS, for example if your command
     * is in the EnumMemory class you need to pass the EnumMemory.class object
     *
     * @param commandClass command object to be registered
     */
    private void registerCommand(Class<?> commandClass) {
        if (commandRegFactory == null) {
            commandRegFactory = new EnumAnnotatedCommandRegistrationFactory(new EnumSimpleInjector(this),
                    new EnumSimpleAnnotatedCommandExecutorFactory());
        }
        Spout.getEngine().getRootCommand().addSubCommands(this, commandClass, commandRegFactory);
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Get the instance of the plugin
     *
     * @return Plugin instance
     */
    public static MakeMeMod getInstance() {
        return instance;
    }

    /**
     * Get the instance of the configuration
     */

    public CommentConfiguration getConfig() {
        return configuration;
    }
}
