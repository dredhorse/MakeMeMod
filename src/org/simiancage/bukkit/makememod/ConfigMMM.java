package org.simiancage.bukkit.makememod;

/**
 *
 * PluginName: MakeMeMod
 * Class: ConfigMMM
 * User: DonRedhorse
 * Date: 02.11.11
 * Time: 19:16
 *
 */

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SuppressWarnings({"UnusedDeclaration"})
public class ConfigMMM {
    private static ConfigMMM instance = null;


    // Nothing to change from here to ==>>>
    private FileConfiguration config;
    private Plugin plugin;
    private final String configFile = "config.yml";
    private boolean configAvailable = false;
    // Default plugin configuration
    private boolean errorLogEnabled = true;
    private boolean debugLogEnabled = false;
    private boolean checkForUpdate = true;
    private boolean autoUpdateConfig = false;
    private boolean saveConfig = false;
    private String pluginName;
    private String pluginVersion;
    private boolean configRequiresUpdate = false;

// <<<<=== here..


// Stuff with minor changes

    // ToDo Change the pluginSlug, versionURL and the LoggerClass  for the Plugin!
    @SuppressWarnings({"FieldCanBeLocal"})
    private final String pluginSlug = "http://dev.bukkit.org/server-mods/makememod/";
    @SuppressWarnings({"FieldCanBeLocal"})
    private final String versionURL = "https://raw.github.com/dredhorse/MakeMeMod/master/resources/makememod.ver";
    private static LoggerMMM log;

    // ToDo Change the configCurrent if the config changes!
    private final String configCurrent = "1.1";

// and now the real stuff


    // Default Config Variables start here!
// You can declare defaults already here or later on in setupDefaultVariables()
    private String configVer = "1.1";
    private boolean broadcastAll = true;
    private boolean broadcastGroups = false;
    private ArrayList<String> broadcastTargets = new ArrayList<String>();
    private boolean generalPermChanges = false;
    private Map<String, Object> aliasList;
    private Map<String, Object> defaultAliasList = new HashMap<String, Object>();
    private ArrayList<String> defaultBroadcastTargets = new ArrayList<String>();
    private boolean preferVault = true;


/*  Here comes the custom config, the default config is later on in the class
Keep in mind that you need to create your config file in a way which is
afterwards parsable again from the configuration class of bukkit
*/

// First we have the default part..
// Which is devided in setting up some variables first

    private void setupDefaultVariables() {
        // Start here
        defaultBroadcastTargets.add("Admin");
        defaultBroadcastTargets.add("Moderators");
        defaultAliasList.put("mod", "Builder,Moderators");
        defaultAliasList.put("admin", "Builder,Admins");
        broadcastTargets = defaultBroadcastTargets;
        aliasList = defaultAliasList;

    }

// And than we add the defaults

    private void customDefaultConfig() {
        // Start here

        config.addDefault("preferVault", preferVault);
        config.addDefault("broadcastAll", broadcastAll);
        config.addDefault("broadcastGroups", broadcastGroups);
        config.addDefault("generalPermChanges", generalPermChanges);

        //config.addDefaults(); Default("aliasList", defaultAliasList);

    }


// Than we load it....

    private void loadCustomConfig() {
        // Start here
        preferVault = config.getBoolean("preferVault");
        broadcastAll = config.getBoolean("broadcastAll");
        broadcastGroups = config.getBoolean("broadcastGroups");
        generalPermChanges = config.getBoolean("generalPermChanges");
        broadcastTargets = (ArrayList<String>) config.getStringList("broadcastTargets");
        aliasList = config.getConfigurationSection("aliasList").getValues(true);

        // Don't forget the debugging

        log.debug("preferVault", preferVault);
        log.debug("broadcastAll", broadcastAll);
        log.debug("broadcastGroups", broadcastGroups);
        log.debug("generalPermChanges", generalPermChanges);
        log.debug("broadcastTargets", broadcastTargets);
        log.debug("aliasList", aliasList);
    }

// And than we write it....


    private void writeCustomConfig(PrintWriter stream) {
        //Start here!!!
        stream.println("#-------- Plugin Configuration");
        stream.println();
        stream.println("# Prefer Vault Plugin as permission handler");
        stream.println("preferVault: " + preferVault);
        stream.println();
        stream.println("# Broadcast to all players on the server when MakeMeMod is used");
        stream.println("broadcastAll: " + broadcastAll);
        stream.println();
        stream.println("# Broadcast to specific groups on the server when MakeMeMod is used");
        stream.println("broadcastGroups: " + broadcastGroups);
        stream.println();
        stream.println("# Groups to broadcast to");
        stream.println("broadcastTargets:");

        for (String groups : broadcastTargets) {

            stream.println("- '" + groups + "'");
        }
        stream.println();
        stream.println("# Make changes to GroupMembership general or world based?");
        stream.println("# NOTE: You need to have your permissions correctly set up for this,");
        stream.println("#       or you will see strange results.");
        stream.println("generalPermChanges: " + generalPermChanges);
        stream.println();
        stream.println("#--------- Group Change Commands");
        stream.println();
        stream.println("# Customize group change commands in form of");
        stream.println("# alias: GroupTheyAreIn,GroupTheyShouldBeIn");
        stream.println("aliasList:");
        List<String> aliasKeys = new ArrayList<String>(aliasList.keySet());
        for (String alias : aliasKeys) {
            stream.println("    " + alias + ": " + aliasList.get(alias));
        }
    }


// The plugin specific getters start here!

    public boolean isPreferVault() {
        return preferVault;
    }

    public boolean isBroadcastAll() {
        return broadcastAll;
    }

    public boolean isBroadcastGroups() {
        return broadcastGroups;
    }

    public boolean isGeneralPermChanges() {
        return generalPermChanges;
    }

    public ArrayList<String> broadcastTargets() {
        return broadcastTargets;
    }

    public Map<String, Object> aliasList() {
        return aliasList;
    }

    public boolean isAlias(String alias) {
        return aliasList.containsKey(alias);
    }

    public Boolean isValid(String alias) {
        return aliasList.containsKey(alias);
    }

    public String getOldGroup(String alias) {
        String[] groups = aliasList.get(alias).toString().split(",");
        String oldGroup = "";
        if (groups.length < 2) {
            log.warning("There is no correct configuration for command: " + alias);
        } else {
            oldGroup = groups[0];
        }
        log.debug("OldGroup", oldGroup);
        return oldGroup;
    }

    public String getNewGroup(String alias) {
        String[] groups = aliasList.get(alias).toString().split(",");
        String newGroup = "";
        if (groups.length < 2) {
            log.warning("There is no correct configuration for command: " + alias);
        } else {
            newGroup = groups[1];
        }
        log.debug("NewGroup", newGroup);
        return newGroup;
    }


// *******************************************************************************************************************
// Other Methods no change normally necessary


// The class stuff first

    public static ConfigMMM getInstance() {
        if (instance == null) {
            instance = new ConfigMMM();
        }
        log = MakeMeMod.getLog();
        return instance;
    }

    private ConfigMMM() {

    }


// than the getters

    public String pluginName() {
        return pluginName;
    }

    public String pluginVersion() {
        return pluginVersion;
    }

    public String configVer() {
        return configVer;
    }

    public boolean isErrorLogEnabled() {
        return errorLogEnabled;
    }

    public boolean isDebugLogEnabled() {
        return debugLogEnabled;
    }

    public boolean isCheckForUpdate() {
        return checkForUpdate;
    }

    public boolean isAutoUpdateConfig() {
        return autoUpdateConfig;
    }

    public boolean isSaveConfig() {
        return saveConfig;
    }

    public boolean isConfigRequiresUpdate() {
        return configRequiresUpdate;
    }

// And the rest

// Setting up the config

    public void setupConfig(FileConfiguration config, Plugin plugin) {

        this.config = config;
        this.plugin = plugin;
  // Checking if config file exists, if not create it
        if (!(new File(plugin.getDataFolder(), configFile)).exists()) {
            log.info("Creating default configuration file");
            defaultConfig();
        }
// Loading the config from file
        loadConfig();

// Checking internal configCurrent and configfile configVer

        updateNecessary();
// If config file has new options update it if enabled
        if (autoUpdateConfig) {
            updateConfig();
        }
// Also check for New Version of the plugin
        if (checkForUpdate) {
            versionCheck();
        }
        configAvailable = true;
    }


// Creating the defaults

// Configuring the Default options..

    private void defaultConfig() {
        setupDefaultVariables();
        if (!writeConfig()) {
            log.info("Using internal Defaults!");
        }
        config = plugin.getConfig();
        config.addDefault("configVer", configVer);
        config.addDefault("errorLogEnabled", errorLogEnabled);
        config.addDefault("debugLogEnabled", debugLogEnabled);
        config.addDefault("checkForUpdate", checkForUpdate);
        config.addDefault("autoUpdateConfig", autoUpdateConfig);
        config.addDefault("saveConfig", saveConfig);
        customDefaultConfig();
    }


// Loading the configuration

    private void loadConfig() {
        config = plugin.getConfig();
        // Starting to update the default configuration
        configVer = config.getString("configVer");
        errorLogEnabled = config.getBoolean("errorLogEnabled");
        debugLogEnabled = config.getBoolean("debugLogEnabled");
        checkForUpdate = config.getBoolean("checkForUpdate");
        autoUpdateConfig = config.getBoolean("autoUpdateConfig");
        saveConfig = config.getBoolean("saveConfig");
        // Debug OutPut NOW!
        if (debugLogEnabled) {
            log.info("Debug Logging is enabled!");
        }
        log.debug("configCurrent", configCurrent);
        log.debug("configVer", configVer);
        log.debug("errorLogEnabled", errorLogEnabled);
        log.debug("checkForUpdate", checkForUpdate);
        log.debug("autoUpdateConfig", autoUpdateConfig);
        log.debug("saveConfig", saveConfig);

        loadCustomConfig();

        log.info("Configuration v." + configVer + " loaded.");
    }


//  Writing the config file

    private boolean writeConfig() {
        boolean success = false;
        try {
            PrintWriter stream;
            File folder = plugin.getDataFolder();
            if (folder != null) {
                folder.mkdirs();
            }
            String pluginPath = plugin.getDataFolder() + System.getProperty("file.separator");
            PluginDescriptionFile pdfFile = this.plugin.getDescription();
            pluginName = pdfFile.getName();
            pluginVersion = pdfFile.getVersion();
            stream = new PrintWriter(pluginPath + configFile);
//Let's write our config ;)
            stream.println("# " + pluginName + " " + pdfFile.getVersion() + " by " + pdfFile.getAuthors().toString());
            stream.println("#");
            stream.println("# Configuration File for " + pluginName + ".");
            stream.println("#");
            stream.println("# For detailed assistance please visit: " + pluginSlug);
            stream.println();
            stream.println("#------- Default Configuration");
            stream.println();
            stream.println("# Configuration Version");
            stream.println("configVer: '" + configVer + "'");
            stream.println();
            stream.println("# Error Log Enabled");
            stream.println("# Enable logging to server console");
            stream.println("# Warning and Severe will still be logged.");
            stream.println("errorLogEnabled: " + errorLogEnabled);
            stream.println();
            stream.println("# Debug Log Enabled");
            stream.println("# Enable more logging.. could be messy!");
            stream.println("DebugLogEnabled: " + debugLogEnabled);
            stream.println();
            stream.println("# Check for Update");
            stream.println("# Will check if there is a new version of the plugin out.");
            stream.println("checkForUpdate: " + checkForUpdate);
            stream.println();
            stream.println("# Auto Update Config");
            stream.println("# This will overwrite any changes outside the configuration parameters!");
            stream.println("autoUpdateConfig: " + autoUpdateConfig);
            stream.println();
            stream.println("# Save Config");
            stream.println("# This will overwrite any changes outside the configuration parameters!");
            stream.println("# Only needed if you use ingame commands to change the configuration.");
            stream.println("saveConfig: " + saveConfig);
            stream.println();

// Getting the custom config information from the top of the class
            writeCustomConfig(stream);

            stream.println();

            stream.close();

            success = true;

        } catch (FileNotFoundException e) {
            log.warning("Error saving the " + configFile + ".");
        }
        log.debug("DefaultConfig written", success);
        return success;
    }


// Checking if the configVersions differ

    private void updateNecessary() {
        if (configVer.equalsIgnoreCase(configCurrent)) {
            log.info("Config is up to date");
        } else {
            log.warning("Config is not up to date!");
            log.warning("Config File Version: " + configVer);
            log.warning("Internal Config Version: " + configCurrent);
            log.warning("It is suggested to update the config.yml!");
            configRequiresUpdate = true;
        }
    }

// Checking the Current Version via the Web

    private void versionCheck() {
        String thisVersion = plugin.getDescription().getVersion();
        URL url;
        try {
            url = new URL(versionURL);
            BufferedReader in;
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            String newVersion = "";
            String line;
            while ((line = in.readLine()) != null) {
                newVersion += line;
            }
            in.close();
            if (newVersion.equals(thisVersion)) {
                log.info("is up to date at version "
                        + thisVersion + ".");

            } else {
                log.warning("is out of date!");
                log.warning("This version: " + thisVersion + "; latest version: " + newVersion + ".");
            }
        } catch (MalformedURLException ex) {
            log.warning("Error accessing update URL.", ex);
        } catch (IOException ex) {
            log.warning("Error checking for update.", ex);
        }
    }

// Updating the config

    private void updateConfig() {
        if (configRequiresUpdate) {
            configVer = configCurrent;
            if (writeConfig()) {
                log.info("Configuration was updated with new default values.");
                log.info("Please change them to your liking.");
            } else {
                log.warning("Configuration file could not be auto updated.");
                log.warning("Please rename " + configFile + " and try again.");
            }
        }
    }

// Reloading the config

    public String reloadConfig() {
        String msg;
        if (configAvailable) {
            loadConfig();
            log.info("Config reloaded");
            msg = "Config was reloaded";
        } else {
            log.severe("Reloading Config before it exists.");
            log.severe("Flog the developer!");
            msg = "Something terrible terrible did go really really wrong, see console log!";
        }
        return msg;
    }
// Saving the config

    public boolean saveConfig(){
        boolean saved = false;
        if (saveConfig){
            saved = writeConfig();
        }
        return saved;
    }

}
