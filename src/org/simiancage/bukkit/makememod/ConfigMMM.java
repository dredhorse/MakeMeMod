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


public class ConfigMMM {
    private static ConfigMMM instance = null;
    private FileConfiguration config;
    private Plugin plugin;
    private final String configFile = "config.yml";
    private boolean configAvailable = false;
    // ToDo Change the pluginSlug, versionURL and log for the Plugin!
    private final String pluginSlug = "http://dev.bukkit.org/server-mods/makememod/";
    private final String versionURL = "https://raw.github.com/dredhorse/MakeMeMod/master/resources/makememod.ver";
    private LoggerMMM log;

    // ToDo Change the configCurrent if the config changes!
    private final String configCurrent = "1.1";

    // Default plugin configuration
    private boolean errorLogEnabled = true;
    private boolean debugLogEnabled = false;
    private boolean checkForUpdate = true;
    private boolean autoUpdateConfig = false;
    private boolean saveConfig = false;
    private String pluginName;
    private String pluginVersion;

// Default Config Variables start here!

    private String configVer = "1.1";
    private boolean broadcastAll = true;
    private boolean broadcastGroups = false;
    private ArrayList<String> broadcastTargets = new ArrayList<String>();
    private boolean generalPermChanges = false;
    private Map<String, Object> aliasList;
    private Map<String, Object> defaultAliasList = new HashMap<String, Object>();
    private ArrayList<String> defaultBroadcastTargets = new ArrayList<String>();


    public static ConfigMMM getInstance() {
        if (instance == null) {
            instance = new ConfigMMM();
        }
        return instance;
    }

    private ConfigMMM() {
    }

    private boolean writeConfig() {
        boolean success = false;
        try {
            PrintWriter stream = null;
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
            stream.println("configVer: '" + configVer+"'");
            stream.println();
            stream.println("# Error Log Enabled");
            stream.println("# Enable logging to server console");
            stream.println("errorLogEnabled: " + errorLogEnabled);
            stream.println();
            stream.println("# Debug Log Enabled");
            stream.println("# Enable more logging.. could be messy!");
            stream.println("debugLogEnabled: " + debugLogEnabled);
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
            stream.println("#-------- Plugin Configuration");
            stream.println();
            stream.println("# Broadcast to all players on the server when MakeMeMod is used");
            stream.println("broadcastAll: " + broadcastAll);
            stream.println();
            stream.println("# Broadcast to specific groups on the server when MakeMeMod is used");
            stream.println("broadcastGroups: " + broadcastGroups);
            stream.println();
            stream.println("# Groups to broadcast to");
            stream.println("broadcastTargets:");

            for (String groups: broadcastTargets) {

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
            for ( String alias : aliasKeys) {
                stream.println("    "+alias+": "+aliasList.get(alias));
            }

            stream.println();

            stream.close();

            success = true;

        } catch (FileNotFoundException e) {
            logWarn("Error saving the " + configFile + ".");
        }
        log.debug("DefaultConfig written",success );
        return success;
    }


// Configuring the Default options..

    private void defaultConfig() {

        defaultBroadcastTargets.add("Admin");
        defaultBroadcastTargets.add("Moderators");
        defaultAliasList.put("mod", "Builder,Moderators");
        defaultAliasList.put("admin", "Builder,Admins");
        broadcastTargets  = defaultBroadcastTargets;
        aliasList  = defaultAliasList;
        if (!writeConfig()) {
            logInfo("Using internal Defaults!");
        }
        config = plugin.getConfig();
        config.addDefault("configVer", configVer);
        config.addDefault("errorLogEnabled", errorLogEnabled);
        config.addDefault("debugLogEnabled", debugLogEnabled);
        config.addDefault("checkForUpdate", checkForUpdate);
        config.addDefault("autoUpdateConfig", autoUpdateConfig);
        config.addDefault("saveConfig", saveConfig);
        config.addDefault("broadcastAll", broadcastAll);
        config.addDefault("broadcastGroups", broadcastGroups);
        config.addDefault("generalPermChanges",generalPermChanges);

        //config.addDefaults(); Default("aliasList", defaultAliasList);
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
        broadcastAll = config.getBoolean("broadcastAll");
        broadcastGroups = config.getBoolean("broadcastGroups");
        generalPermChanges = config.getBoolean("generalPermChanges");
        broadcastTargets = (ArrayList<String>) config.getList("broadcastTargets", defaultBroadcastTargets );
        aliasList = config.getConfigurationSection("aliasList").getValues(true);

        logInfo("Configuration v." + configVer + " loaded.");
    }


// The plugin specific getters start here!
// ToDo add them

    public boolean broadcastAll() {
        return broadcastAll;
    }

    public boolean broadcastGroups() {
        return broadcastGroups;
    }

    public boolean generalPermChanges () {
        return generalPermChanges;
    }

    public ArrayList<String> broadcastTargets() {
        return broadcastTargets;
    }

    public Map<String, Object> aliasList(){
        return aliasList;
    }

    public boolean isAlias(String alias) {
        return aliasList.containsKey(alias);
    }

    public Boolean isValid(String alias)
    {
        return aliasList.containsKey(alias);
    }

    public String getOldGroup(String alias) {
        String[] groups = aliasList.get(alias).toString().split(",");
        String oldGroup = "";
        if (groups.length<2){
            logWarn("There is no correct configuration for command: "+alias);
        } else {
            oldGroup = groups[0];
        }
        log.debug("OldGroup",oldGroup );
        return oldGroup;
    }

    public String getNewGroup(String alias) {
        String[] groups = aliasList.get(alias).toString().split(",");
        String newGroup = "";
        if (groups.length<2){
            logWarn("There is no correct configuration for command: "+alias);
        } else {
            newGroup = groups[1];
        }
        log.debug("NewGroup",newGroup );
        return newGroup;
    }
// Other Methods no change normally necessary

// Getters first

    public String pluginName() {
        return pluginName;
    }

    public String pluginVersion() {
        return pluginVersion;
    }

    public String configVer() {
        return configVer;
    }

    public boolean errorLogEnabled() {
        return errorLogEnabled;
    }

    public boolean debugLogEnabled() {
        return debugLogEnabled;
    }

    public boolean checkForUpdate() {
        return checkForUpdate;
    }

    public boolean autoUpdateConfig() {
        return autoUpdateConfig;
    }

    public boolean saveConfig() {
        return saveConfig;
    }

// And the rest

// Setting up the config

    public void setupConfig(FileConfiguration config, Plugin plugin) {

        this.config = config;
        this.plugin = plugin;
        this.log = MakeMeMod.getLog();
// Checking if config file exists, if not create it
        if (!(new File(plugin.getDataFolder(), configFile)).exists()) {
            logInfo("Creating default configuration file");
            defaultConfig();
        }
// Loading the config from file
        loadConfig();
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

// Checking the Current Version via the Web

    private void versionCheck() {
        String thisVersion = plugin.getDescription().getVersion();
        URL url = null;
        try {
            url = new URL(versionURL);
            BufferedReader in = null;
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            String newVersion = "";
            String line;
            while ((line = in.readLine()) != null) {
                newVersion += line;
            }
            in.close();
            if (newVersion.equals(thisVersion)) {
                logInfo("is up to date at version "
                        + thisVersion + ".");

                return;
            } else {
                logWarn("is out of date!");
                logWarn("This version: " + thisVersion + "; latest version: " + newVersion + ".");
                return;
            }
        } catch (MalformedURLException ex) {
            logWarn("Error accessing update URL.");
            return;
        } catch (IOException ex) {
            logWarn("Error checking for update.");
            return;
        }
    }

// Updating the config

    private void updateConfig() {
        if (writeConfig()) {
            logInfo("Configuration was updated with new default values.");
            logInfo("Please change them to your liking.");
        } else {
            logWarn("Configuration file could not be auto updated.");
            logWarn("Please rename " + configFile + " and try again.");
        }
    }

// Reloading the config

    public String reloadConfig() {
        String msg;
        if (configAvailable) {
            loadConfig();
            logInfo("Config reloaded");
            msg = "Config was reloaded";
        } else {
            logSevere("Reloading Config before it exists.");
            logSevere("Flog the developer!");
            msg = "Something terrible terrible did go really really wrong, see console log!";
        }
        return msg;
    }

// Logging it..

    private void logInfo(String message) {
        if (errorLogEnabled) {
            log.info(message);
        }
    }

    private void logWarn(String message) {
        if (errorLogEnabled) {
            log.warning( message);
        }
    }

    private void logSevere(String message) {
        if (errorLogEnabled) {
            log.severe(message);
        }
    }

}
