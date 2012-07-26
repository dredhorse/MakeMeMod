package team.cascade.spout.makememod.helper.config;


import team.cascade.spout.makememod.config.CONFIG;
import team.cascade.spout.makememod.exceptions.ConfigNotAvailableException;
import team.cascade.spout.makememod.exceptions.ConfigNotInitializedException;
import team.cascade.spout.makememod.exceptions.WrongClassException;
import team.cascade.spout.makememod.helper.Logger;
import team.cascade.spout.makememod.helper.Messenger;
import team.cascade.spout.makememod.helper.file.UnicodeUtil;
import team.cascade.spout.makememod.permissions.PERMISSIONS;
import org.spout.api.Spout;
import org.spout.api.exception.ConfigurationException;
import org.spout.api.plugin.Plugin;
import org.spout.api.plugin.PluginDescriptionFile;
import org.spout.api.scheduler.TaskPriority;
import org.spout.api.util.config.yaml.YamlConfiguration;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Handel's all the saving, loading, checking for new version, reloading and transferring the config to and from the enums.<br>
 * You shouldn't really change anything in here normally all the things you perhaps want to change is in {@link Configuration}.<br>
 *
 *
 * @author $Author: dredhorse$
 * @version $FullVersion$
 */

@SuppressWarnings("ALL")
public abstract  class CommentConfiguration {

    //---- Default Values -----------------------------------------------------


    /**
     * Disable Plugin if spout Version is OLDER than plugin spout version
     *
     */

    private static boolean disableOnOlderBuilds = false;

     /**
     * This is the internal config version, which is being used if no Version is supplied from the plugin itself
     * use {@link #alterConfigCurrent(String conCurr)} for this
     */

    String configCurrent;


    /**
     * Link to the location of the plugin website
     * Note: This can be overwritten by {@link #alterPluginSlug(String plugSlug)}
     */

    String pluginSlug;

    /**
     * Link to the location of the recent version number, the file should be a text with <version> x.y.rb-Bb
     * for example your properties.yml
     * For a better explanation of the version number scheme please look here: {@literal https://github.com/dredhorse/MakeMeMod/wiki/Versioning}
     * For those people who are interested what the version number means, e.g 3.5.1597-20
     * x.y.rb-Bb
     * <p/>
     * x = Major Version = Major new stuff or internal rework
     * y = Minor Version = Minor new stuff and bug fixes
     * rb = Recommended Build of Spout
     * b = Development Build Version = if you have development builds for example B7
     */

    private String versionURL;


    /**
     * Instance of the Configuration Class
     */

    private static CommentConfiguration instance;


    /**
     * Name of the plugin
     */

    private static String pluginName;


/* Line for logger */

    private final static String LINE = "=====================================";


    //~--- fields -------------------------------------------------------------


    /**
     * Object to handle the configuration
     */

    private YamlConfiguration config;


    /**
     * Is the configuration available or did we have problems?
     */

    private boolean configAvailable = false;


    /**
     * Do we require a config update?
     */

    private boolean configRequiresUpdate = false;

    // Default plugin configuration


    /**
     * Configuration File Name
     */

    private String configFile = "config.yml";


    /**
     * This is the DEFAULT for the config file version, should be the same as configCurrent. Will afterwards be changed
     */

    private String configFileVer;


    /**
     * This is the configuration file version read from the configuration file.
     */

    private String configVer;


    /**
     * The plugin
     */

    private Plugin main;


    /**
     * Directory of the plugin configuration
     */

    private String pluginPath;


    /**
     * PluginDescriptionFile
     */

    private PluginDescriptionFile pdfFile;


    /**
     * Is a different Plugin available?
     */

    private static boolean newVersionAvailable;


    /**
     * Has the config been changed via commands and isn't saved yet?
     */


    private boolean configDirty = false;


    /**
     * Do we have a non recoverable error?  Will be set if we have problems creating the default values
     */

    private boolean nonRecoverableError = false;


    /**
     * Did we try fixing yaml issues?
     */

    private boolean triedFixingYamlIssues = false;


    // ======================= Default Configuration Stuff ==========================


    /**
     * Enable more logging.. could be messy!
     */

    boolean debugLogEnabled = false;


    /**
     * Enable logging to separate file if debug logging is enabled
     */

    boolean debugLogToFile = true;


    /**
     * Check if there is a new version of the plugin out.
     */

    boolean checkForUpdate = true;


    /**
     * AutoUpdate the config file if necessary. This will overwrite any changes outside the configuration parameters!
     */

    boolean configAutoUpdate = true;


    /**
     * Enable saving of the config file
     */

    boolean configAutoSave = true;


    /**
     * Enable logging of the config
     */

    boolean configLogEnabled = true;


    /**
     * Contains the getNewVersion if there is one available, otherwise null
     */
    private static String newVersion = null;


    /**
     * Method which will use the DEBUG_LOG_ENABLED, DEBUG_LOG_TO_FILE, CHECK_FOR_UPDATE, CONFIG_AUTO_UPDATE, CONFIG_AUTO_SAVE, CONFIG_LOG_ENABLED
     * enums to overwrite the default values configured in here.
     * <p/>
     * If you don't want to use those enums make sure that you delete the corresponding references here!
     */

    abstract void defaultInit();




    // ======================= Default Headers ======================================


    /**
     * Default config file header.
     * Note: Normally you don't want to change anything in here, if you really want to please feel free to do so,
     * but don't mess it up!
     * NOTE: Removing the configVer node will cause a ConfigNotAvailableException!
     *
     * @param stream access to the config file
     */

    private void topHeader(File stream) throws IOException {
        UnicodeUtil.saveUTF8File(stream, "# " + pluginName + " " + pdfFile.getVersion() + " by " + Messenger.getAuthors(main)+"\n",true);
        UnicodeUtil.saveUTF8File(stream, "#\n",true);
        UnicodeUtil.saveUTF8File(stream, "# Configuration File for " + pluginName + ".\n",true);
        UnicodeUtil.saveUTF8File(stream, "#\n",true);
        UnicodeUtil.saveUTF8File(stream, "# For detailed assistance please visit: " + pluginSlug+"\n",true);
        UnicodeUtil.saveUTF8File(stream,"\n",true );
        UnicodeUtil.saveUTF8File(stream,"\n",true );
        UnicodeUtil.saveUTF8File(stream, "# ------- Plugin Configuration\n",true);
        UnicodeUtil.saveUTF8File(stream,"\n",true );
    }

    /**
     * Method to save the configuration to file, this is done automatically if configured after every change.<br>
     * This method will only save if the configuration was changed, if the config file doesn't exist it will try to create it.
     *
     * @return true if the save was successful or no change required
     */


    public boolean saveConfig() {
        boolean success = true;
        if (!configFileExist(configFile)){
            Logger.debug("Config file doesn't exist, trying to create default");
            configDirty = true;
        }
        if (configDirty) {
            if (writeConfigToFile()) {
                configDirty = false;
            } else {
                success = false;
            }
        } else {
            Logger.debug("Nothing to save, skipping.");
        }
        return success;
    }


    /**
     * Method to handle the reloading of the configuration, this is done if configured automatically
     * when the config was AutoUpdated, changed or manually.
     * Note: This method will save the config if there was a change before, otherwise it will just load the config again.
     * Note: This method doesn't need to be called explicitly, but you can if you want
     *
     * @return true if the reload was successful, false if there was an issue.
     */


    public boolean reloadConfig() {
        boolean success = false;
        if (saveConfig()) {
            success = true;
        }
        if (success) {
            if (!loadConfig()) {
                success = false;
            } else {
                Logger.config("Configuration successfully reloaded");
            }

        }
        Logger.config(LINE);
        return success;
    }

    /**
     * Loads the configuration into memory via {@link #loadConfigToMemory(String)} and will than change the default config options<p>
     * @throws ConfigNotAvailableException if there was a problem during loading to memory.
     * </p>
     * @return true if loading was successful
     */

    public boolean loadConfig() {
        boolean success = false;
        Logger.config(LINE);
        Logger.config("Loading the configuration");
        try {
            loadConfigToMemory(configFile);
            success = true;
            Logger.config("Configuration successfully loaded");
            defaultInit();
        } catch (ConfigNotAvailableException e) {
            Logger.warning("There was a problem with loading the configuration", e);
        }
        return success;
    }


    // ======================= Writing the information to the file ===================


    /**
     * Finally writing the configuration to the configuration file
     *
     * @param stream access to the config file
     */

    private void configurationWriting(File stream) throws IOException {
        Logger.config("Writing the configuration");
        UnicodeUtil.saveUTF8File(stream, "\n", true);
        UnicodeUtil.saveUTF8File(stream, "configuration: \n",true );
        UnicodeUtil.saveUTF8File(stream,"\n", true);
        for (CONFIG config : CONFIG.values()) {
            Object configOption = config.getConfigOption();
            UnicodeUtil.saveUTF8File(stream, "#   " + config.getConfigComment()+"\n",true);
            // let's check if we just had a comment or also an option
            if (configOption != null) {
                Logger.debug(config.toString(), configOption);
                String configOptionLine = "    " + config + ": ";
                // check if it is a int, boolean, char, byte, short, double, long, float,
                // in which case we don't have to do anything when writing.
                if (isPrimitiveWrapper(configOption)) {
                    configOptionLine = configOptionLine + configOption.toString();
                } else {
                    // looks like we do have something different than the primitive wrapper can handle
                    // so let's check if we are a map
                    if (configOption instanceof Map) {
                        UnicodeUtil.saveUTF8File(stream, configOptionLine+"\n",true);
                        Iterator it = ((Map) configOption).keySet().iterator();
                        Object nextIt;
                        while (it.hasNext()) {
                            nextIt = it.next();
                            if (it instanceof Map) {
                                UnicodeUtil.saveUTF8File(stream, "        " + nextIt.toString().trim() + ": \n",true );
                                Iterator innerIt = ((Map) configOption).keySet().iterator();
                                Object nextInnerIt;
                                while (innerIt.hasNext()) {
                                    nextInnerIt = innerIt.next();
                                    Object mapOption = ((Map) nextIt).get(nextInnerIt);
                                    if (mapOption instanceof String) {
                                        UnicodeUtil.saveUTF8File(stream, "            " + nextInnerIt.toString().trim() + ": \"" + mapOption.toString().trim() + "\" \n",true );
                                    } else {
                                        UnicodeUtil.saveUTF8File(stream, "            " + nextInnerIt.toString().trim() + ": " + mapOption.toString().trim()+"\n",true);
                                    }
                                }
                            } else {
                                Object mapOption = ((Map) configOption).get(nextIt);
                                if (mapOption instanceof String) {
                                    UnicodeUtil.saveUTF8File(stream, "        " + nextIt.toString().trim() + ": \"" + mapOption.toString().trim() + "\" \n",true );
                                } else {
                                    UnicodeUtil.saveUTF8File(stream, "        " + nextIt.toString().trim() + ": " + mapOption.toString().trim()+"\n",true);
                                }
                            }
                        }
                        configOptionLine = "";

                    } else {
                        // or if we are a list
                        if (configOption instanceof List) {
                            UnicodeUtil.saveUTF8File(stream, configOptionLine +"\n",true);
                            for (Object listItem : (List<String>) (List<?>) configOption) {
                                if (listItem instanceof String) {
                                    UnicodeUtil.saveUTF8File(stream, "        - \"" + listItem + "\" \n",true );
                                } else {
                                    UnicodeUtil.saveUTF8File(stream, "        - " + listItem+ "\n",true);
                                }
                            }
                            configOptionLine = "";
                        } else {
                            // if it is a String let's wrap it in "
                            if (configOption instanceof String) {
                                configOptionLine = configOptionLine + "\"" + configOption + "\"";
                            } else {
                                // well let's just dump it in any other case
                                // ToDo make sure that this works or create other approaches for certain cases
                                configOptionLine = configOptionLine + configOption.toString().trim();
                            }
                        }

                    }
                }
                UnicodeUtil.saveUTF8File(stream, configOptionLine + "\n",true);
                UnicodeUtil.saveUTF8File(stream,"\n",true );
            }
        }

    }


// ======================= Reading the Configuration ===================


    /**
     * Reading the configuration in via standard spout means, a WrongClassException will be handled and default used in
     * this case.
     * <p/>
     * Please contact me with any issues here so that I can try to fix them in the {@link #configurationWriting(java.io.PrintWriter)}  section
     * or create a pull request for the changes.
     */


    private void configurationReading() {
        Logger.config(LINE);
        Logger.config("Reading the configuration");
        for (CONFIG configNode : CONFIG.values()) {
            if (configNode.getConfigOption() != null) {
                try {
                    configNode.setConfigOption(config.getNode("configuration." + configNode.toString()).getValue() != null ? config.getNode("configuration." + configNode.toString()).getValue() : configNode.getConfigOption());
                } catch (WrongClassException e) {
                    // if we got a WrongClassException we try to get the right kind of class
                    // todo make sure that we handle all cases correctly, please let me know
                    Logger.debug("Exception message", e);
                    Logger.debug("Getting the correct Class now!");
                    Map<String, Object> map = config.getNode("configuration." + configNode.toString()).getValues();
                    configNode.setConfigurationOption(map);
                } finally {
                    Logger.debug(configNode + ": " + configNode.getConfigOption().toString().trim());
                }
            }
        }
        Logger.config("Reading the configuration = done!");
    }


    /**
     * The best way to set the configuration options, as this will flag the config dirty and will also trigger a save of the config<p>
     * if {@link CONFIG.CONFIG_AUTO_SAVE} is enabled
     * </p>
     * @param configNode configuration node to set
     * @param value      object value to set
     * @return  false if there was a WrongClassException
     */

    public boolean set(CONFIG configNode, Object value) {
        try {
            configNode.setConfigOption(value);
        } catch (WrongClassException e) {
            Logger.severe(e.getMessage(), e);
            return false;
        }
        Logger.fine(configNode + " was updated with: [" + value.toString() + "]");
        configDirty = true;
        if (isConfigAutoSave()) {
            if (saveConfig()) {
                configDirty = false;
                Logger.config("Configuration was successfully written");
            } else {
                Logger.config("There was a problem saving the configuration");
            }
            Logger.config(LINE);
        }
        return true;
    }


    /**
     * Convenience method to get the state of {@link CONFIG.CONFIG_AUTO_SAVE}
     * @return  state of {@link CONFIG.CONFIG_AUTO_SAVE}
     */
    public boolean isConfigAutoSave() {
        return (Boolean) CONFIG.CONFIG_AUTO_SAVE.getConfigOption();
    }


    /**
     * Allows changing of the Configuration Version, which is being used to compare against the version inside the configuration file.
     * This will also alter the configVer value which is used to create the configuration file
     *
     * @param conCurr new String value of the new version
     */


    public void alterConfigCurrent(String conCurr) {
        configCurrent = conCurr;
        configVer = conCurr;
    }


    /**
     * Allows changing of the URL which is included in the configuration file
     *
     * @param plugSlug new String value of the URL
     */


    public void alterPluginSlug(String plugSlug) {
        pluginSlug = plugSlug;
    }


//~--- constructors -------------------------------------------------------


    /**
     * Constructor for CommentConfiguration, you will still need perhaps to configure some stuff.
     *
     * @param plugin the main plugin class as reference
     * @see #initializeConfig() for more information
     */

    public CommentConfiguration(Plugin plugin) {
        this.pdfFile = plugin.getDescription();
        pluginName = pdfFile.getName();
        pluginPath = plugin.getDataFolder() + System.getProperty("file.separator");
        instance = this;
        configFileVer = configCurrent;
        main = plugin;


    }

//~--- methods ------------------------------------------------------------


    /**
     * Initialize the Config, this should done be AFTER setting the needed values,
     * which can be either values needed for the ENUMs (see the example code) or
     * values needed for the class itself:
     * <p/>
     * @throws ConfigNotAvailableException when the config can not correctly be initialized
     * @see #alterConfigCurrent(String)
     * @see #alterPluginSlug(String)
     * <p/>
     * we do the following:
     * <ul>
     *     <li>create the config if it doesn't exist {@link #writeConfigToFile()}
     *     <li>load the config to memory {@link #loadConfigToMemory(String)}
     *     <li>fix the most common yaml issues if necessary {@link #fixYamlIssues()}
     *     <li>check if we could read something at all
     *     <li>apply the config to the enums {@link #configurationReading()}
     *     <li>set debug and config level correctly
     *     <li>check if there is an update available if configured {@link #autoCheck()}
     *     <li>check if the configuration requires an update and saves it if configured {@link #reloadConfig()}
     * </ul>
     *
     *
     */

    public void initializeConfig() throws ConfigNotAvailableException {
        // make sure that we use the correct configFileVer
        configVer = configCurrent;

        // checking  if the configuration file exists, otherwise create it via the defaults
        if (!configFileExist(configFile) && !writeConfigToFile()) {
            Logger.config("Using internal defaults");
        }

/**
 *  load the configuration into memory, if there are issues try to fix them.
 *  the hopefully fixed configuration is auto loaded again, if it fails again
 *  we throw a ConfigNotAvailableException
 */

        if (!loadConfigToMemory(configFile)) {
            fixYamlIssues();
            if (!loadConfigToMemory(configFile) && nonRecoverableError) {
                throw new ConfigNotAvailableException();
            }
        }

        // limited check if something was written at all into the configuration file
         if (config.getNode("configuration.ConfigVersion").getString() == null) {
            Logger.severe("Config is either missing or has wrong syntax!");
            throw new ConfigNotAvailableException();
        }
        // getting the version of the configuration file
        configVer = config.getNode("configuration.ConfigVersion").getString();

        // parsing the config back into the enums
        Logger.config(LINE);
        Logger.config("Applying the configuration");
        configurationReading();
        Logger.config("Applying the configuration = done!");
        Logger.config(LINE);
        // overwriting internal default if necessary
        defaultInit();
        // changing debug level if necessary
        Logger.setDebugLogEnabled(debugLogEnabled);
        // changing config logging if necessary
        if (configLogEnabled) {
            Logger.setLevel(Level.CONFIG);
        }
        // let's start the check for update if necessary
        if (CONFIG.CHECK_FOR_UPDATE.getBoolean()) {
            Logger.config("Enabling automatic update checking");
            autoCheck();
        }
        isConfigRequiresUpdate();
        // let's update the configuration if enabled
        if (CONFIG.CONFIG_AUTO_UPDATE.getBoolean() && configRequiresUpdate) {
            Logger.config("Auto updating the configuration file");
            reloadConfig();
        }
    }


    /**
     * Method to check if the config file already exists
     *
     * @param configFile which is being used by the plugin
     * @return true if configFile exists, false if not
     */

    private boolean configFileExist(String configFile) {
        return (new File(main.getDataFolder(), configFile)).exists();
    }


    /**
     * Method to handle the writing of the config file, either initially or afterwards
     *
     * @return true if writing was successful
     */

    private boolean writeConfigToFile() {
        Logger.config(LINE);
        Logger.config("Saving the configuration file");
        boolean success = false;
        File configurationFile;
        try {

            final File folder = main.getDataFolder();
            if (folder != null) {
                folder.mkdirs();
            }
            configurationFile = new File(pluginPath + configFile);
            UnicodeUtil.saveUTF8File(configurationFile,"",false);
            topHeader(configurationFile);
            configurationWriting(configurationFile);

            Logger.config("Finished writing the configuration file");
            success = true;
        } catch (FileNotFoundException e) {
            Logger.warning("Error saving the " + configFile + ".",e);
        } catch (IOException e) {
            Logger.warning("Error saving the " + configFile + ".", e);
        }
        return success;
    }


    /**
     * Method to load the config file to memory
     *
     * @param configFile which is being used by the plugin
     * @return true if retry it needed again after fixing yaml issues
     * @throws ConfigNotAvailableException if we already had a problem with creating the default values
     */

    private boolean loadConfigToMemory(String configFile) throws ConfigNotAvailableException {
        boolean success = false;
        config = new YamlConfiguration(new File(pluginPath + configFile));
        try {
            config.load();
            success = true;
        } catch (ConfigurationException e) {
            if (nonRecoverableError && triedFixingYamlIssues) {
                throw new ConfigNotAvailableException(e);
            } else {
                Logger.severe("Problem with the configuration in " + configFile + "!", e);
            }
        }
        return success;
    }


    /**
     * Fixes the most common issues in yaml files:
     * <ul>
     *     <li>tabs instead spaces
     *     <li>keys not having a : at the end
     *     <li>strings not being escaped by ' or " correctly
     * </ul>
     */

    private void fixYamlIssues() {
        // todo check if saving in utf-8 is needed
        triedFixingYamlIssues = true;
        Logger.config("Trying to fix the yaml file from issues!");

        /**
         * Code taken from http://bit.ly/Oiz1dG
         * bPermissions / src / de / bananaco / bpermissions / imp / YamlFile.java
         */

        List<String> data = new ArrayList<String>();

        try {
            final BufferedWriter bw = new BufferedWriter(new FileWriter(pluginPath + "temp.yml"));
            final BufferedReader br = new BufferedReader(new FileReader(pluginPath + configFile));

            String line;
            while ((line = br.readLine()) != null) {
                data.add(line);
            }

            List<String> newData = new ArrayList<String>();
            if (data.size() == 0) {
                nonRecoverableError = true;
                Logger.severe("Some files where not found during yaml fixing... please check your storage!");
                if (br != null) {
                    br.close();
                }

                if (bw != null) {
                    bw.close();
                }
                return;
            }

            for (int i = 0; i < data.size(); i++) {
                line = data.get(i);
                // Replace tabs with 4 spaces
                if (line.contains("\t")) {
                    Logger.debug("line " + i + " of " + configFile + " contained a tab. A fix was attempted.");
                    while (line.contains("\t")) {
                        line = line.replace("\t", "    ");
                    }
                }
                // Make sure keys have a key: at the end
                if (!line.replaceAll(" ", "").endsWith(":") && !line.replaceAll(" ", "").startsWith("-") && !line.endsWith("[]") && !line.contains(": ")) {
                    Logger.debug("line " + i + " of " + configFile + " contained a missing : . A fix was attempted.");
                    line = line + ":";
                }
                // Make sure that all 'strings' in a - 'string' list are escaped
                if (line.replaceAll(" ", "").startsWith("-") && line.replaceAll(" ", "").replace("-", "").startsWith("'") && !line.endsWith("'")) {
                    Logger.debug("line " + i + " of " + configFile + " contained a non closed string with '. A fix was attempted.");
                    line = line + "'";
                }
                // Make sure that all "strings" in a - "string" list are escaped
                if (line.replaceAll(" ", "").startsWith("-") && line.replaceAll(" ", "").replace("-", "").startsWith("\"") && !line.endsWith("\"")) {
                    Logger.debug("line " + i + " of " + configFile + " contained a non closed string with \". A fix was attempted.");
                    line = line + "\"";
                }
                // Ignore blank lines
                if (line.replaceAll(" ", "").equals("")) {
                    Logger.debug("line " + i + " of " + configFile + " contained a blank line. A fix was attempted.");
                    line = line.replaceAll(" ", "");
                }
                if (line.equals(":")) {
                    Logger.debug("line " + i + " of " + configFile + " contained a just a : . A fix was attempted.");
                    line = line.replaceAll(":", "");
                } else {
                    newData.add(line);
                }
            }
            for (int i = 0; i < newData.size(); i++) {
                bw.write(newData.get(i));
                bw.newLine();
            }
            br.close();
            bw.close();



// than delete the original and rename the temp file
            final File oldConfigFile = new File(pluginPath + configFile);
            final File tempFile = new File(pluginPath + "temp.yml");

            oldConfigFile.delete();
            tempFile.renameTo(oldConfigFile);
            Logger.config("I tried my best, try reloading!");
        } catch (FileNotFoundException e) {
            Logger.severe("Some files where not found during yaml fixing... please check your storage!", e);
            nonRecoverableError = true;

        } catch (IOException e) {
            Logger.severe("Some more issues with your config..", e);
            nonRecoverableError = true;
        }
    }


// Checking the Current Version via the Web


    /**
     * Auto Checker, which runs every 60 minutes to check for an update and announces it to
     * PERMISSIONS.MAKEMEMOD_ADMIN users if an update is found.
     * @see #versionCheck()
     */


    private void autoCheck() {

        Spout.getEngine().getScheduler().scheduleAsyncRepeatingTask(main, new Runnable() {

            @Override
            public void run() {
                versionCheck();
            }

        }, 0, 864000, TaskPriority.LOW);

    }


    /**
     * Method to figure out if we are out of date or running on an older / newer build of Spout. Will also set the getNewVersion field.
     *
     * @return true if version is same as the version on the web, false if there is a difference or web not accessible
     */


    public boolean versionCheck() {
        String newVersion = "";
        newVersionAvailable = false;
        boolean errorAccessingGithub = false;
        boolean versionFound = false;
        URL url;
        try {
            url = new URL(versionURL);
            BufferedReader in;
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains("<version>") && !versionFound) {
                    newVersion += line;
                    versionFound = true;
                }
            }
            in.close();
        } catch (MalformedURLException ex) {
            Logger.debug("Error accessing update URL.", ex);
            errorAccessingGithub = true;
        } catch (IOException ex) {
            Logger.debug("Error checking for update.", ex);
            errorAccessingGithub = true;
        }
        Logger.debug("newVersion", newVersion);
        if (!errorAccessingGithub) {
            Logger.config(LINE);
            int pluginMajor;
            int pluginMinor;
            int pluginSpout;
            int pluginDev;
            int githubMajor;
            int githubMinor;
            int githubSpout;
            int githubDev;
            String plVersion;
            String ghVersion;
            String spoutVersion = Spout.getAPIVersion();
            Logger.debug("Spout API Version " + spoutVersion);
            int spoutVer;
            int versionPos = spoutVersion.indexOf(" b") + 2;
            spoutVersion = spoutVersion.substring(versionPos, versionPos + 4);
            Logger.debug("spoutVersion", spoutVersion);
            try{
                spoutVer = Integer.parseInt(spoutVersion);
            }catch (NumberFormatException ex){
                Logger.severe("You are running on an unsupported Spout Version",ex);
                // setting spoutVer to 1 allows to disable the plugin if configured with disableOnOlderBuilds
                spoutVer = 1;
            }
            String pluginVersion = main.getDescription().getVersion();
            Logger.debug("pluginVersion", pluginVersion);
            plVersion = pluginVersion.replace(".", ":");
            String[] thisVersion = plVersion.split(":");
            pluginMajor = Integer.parseInt(thisVersion[0]);
            pluginMinor = Integer.parseInt(thisVersion[1]);
            String temp = thisVersion[2];
            versionPos = temp.indexOf("-B");
            Logger.debug("temp", temp);
            Logger.debug("versionPos", versionPos);
            String tempString = temp.substring(0, versionPos);
            Logger.debug("tempString", tempString);
            pluginSpout = Integer.parseInt(tempString);
            pluginDev = Integer.parseInt(temp.substring(versionPos + 2));
            Logger.debug("pluginDev", pluginDev);
            newVersion = newVersion.replace("<version>", "").replace("</version>", "").replaceAll(" ", "");
            Logger.debug("newVersion", newVersion);
            ghVersion = newVersion.replace(".", ":");
            Logger.debug("ghVersion", ghVersion);
            String[] githubVersion = ghVersion.split(":");
            githubMajor = Integer.parseInt(githubVersion[0]);
            githubMinor = Integer.parseInt(githubVersion[1]);
            temp = githubVersion[2];
            versionPos = temp.indexOf("-B");
            Logger.debug("temp", temp);
            Logger.debug("versionPos", versionPos);
            tempString = temp.substring(0, versionPos);
            Logger.debug("tempString", tempString);
            githubSpout = Integer.parseInt(tempString);
            githubDev = Integer.parseInt(temp.substring(versionPos + 2));
            Logger.debug("githubDev", githubDev);
            if (disableOnOlderBuilds && spoutVer > 0 && spoutVer < pluginSpout) {
                Logger.warning("You are running on an unsupported Spout Version");
                Logger.warning("Only Spout Versions higher / equal than " + pluginSpout + " are supported.");
                Logger.warning("Please upgrade your Spout Version!");
                disablePlugin();
                Logger.config(LINE);
                return false;
            }

            if (spoutVer < pluginSpout) {
                Logger.warning("You are running on an older Spout API Version");
                Logger.warning("There might be issues, just letting you know");
            }
            if (spoutVer > pluginSpout) {
                if (githubSpout >= spoutVer) {
                    Logger.warning("There is a new version available for your Spout API Version");
                    Logger.warning("You might want to update!");
                } else {
                    Logger.warning("You are running on an newer Spout API Version");
                    Logger.warning("There might be issues, just letting you know");
                }
            }

            if (newVersion.equals(pluginVersion)) {
                Logger.config("is up to date at version "
                        + pluginVersion + ".");
                if (pluginDev > 0) {
                    Logger.config("is a development build.");
                }
                Logger.config(LINE);
                return true;
            }

            if (githubSpout < pluginSpout) {
                Logger.warning("You are running a testbuild for Spout: " + pluginSpout);
            }
            if (githubSpout > pluginSpout) {
                Logger.info("There is a new Version available for Spout: " + githubSpout);
            }

            if ((githubMajor < pluginMajor) || githubMinor < pluginMinor || pluginDev > 0) {
                Logger.warning("You are running an dev-build. Be sure you know what you are doing!");
                Logger.warning("Please report any bugs via issues or tickets!");
                if (githubDev > pluginDev) {
                    Logger.warning("There is a NEWER dev-build available!");
                }
                if (githubDev < pluginDev) {
                    Logger.severe("WOW! Where did you get THIS version from?");
                    Logger.severe("You like living on the edge, do you?");
                }
            }
            if ((githubMajor > pluginMajor) || githubMinor > pluginMinor) {

                Logger.warning("is out of date!");
                Logger.warning("This version: " + pluginVersion + "; latest version: " + newVersion + ".");
                Spout.getEngine().broadcastMessage("A new Version of " + pluginName + " is available!", PERMISSIONS.MAKEMEMOD_ADMIN.asPermission());
                Spout.getEngine().broadcastMessage("Your Version: " + pluginVersion + " New Version: " + newVersion, PERMISSIONS.MAKEMEMOD_ADMIN.asPermission());
                newVersionAvailable = true;
            }
            this.newVersion = newVersion;
        } else {
            Logger.config("I have no idea if I'm up-to-date, sorry!");
            this.newVersion = null;
        }
        Logger.config(LINE);
        return false;
    }


    /**
     * Returns the instance of the configuration class
     *
     * @return configuration Class
     * @throws team.cascade.spout.makememod.exceptions.ConfigNotInitializedException
     *          when configuration wasn't initialized
     */

    public static CommentConfiguration getInstance() throws ConfigNotInitializedException {
        if (instance == null) {
            throw new ConfigNotInitializedException();
        }
        return instance;
    }


    /**
     * Checks if the config requires an update by comparing the configVer key from the config with the configCurrent field. <p>
     * If the config file requires an update we flag the config dirty.
     * </p>
     * @return true if the config requires an update
     */
    private boolean isConfigRequiresUpdate() {
        Logger.config("Checking configuration version");
        if (configVer.equalsIgnoreCase(configCurrent)) {
            configRequiresUpdate = false;
            Logger.config("Configuration is up to date!");
            return false;
        }
        configRequiresUpdate = true;
        configDirty = true;
        Logger.config("Configuration is not up to date!");
        Logger.config("Loaded version: [" + configVer + "], Current Version: [" + configCurrent + "]");
        Logger.config("You should update the configuration!");
        configFileVer = configCurrent;
        try {
            CONFIG.CONFIG_VERSION.setConfigOption(configCurrent);
        } catch (WrongClassException e) {
            Logger.debug("Well that shouldn't happen",e);
        }
        return true;
    }

    /**
     * Disables the plugin
     */
    private void disablePlugin() {
        Logger.warning("Disabling plugin NOW!");
        Spout.getEngine().getPluginManager().disablePlugin(main);
    }

    /**
     * Code taken from MemorySection Class of Bukkit<p>
     * checks if an object can be primitively wrapped with toString()
     * </p>
     *
     * @param input object which is checked
     * @return true if it can be primitively wrapped
     */

    private boolean isPrimitiveWrapper(Object input) {
        return input instanceof Integer || input instanceof Boolean ||
                input instanceof Character || input instanceof Byte ||
                input instanceof Short || input instanceof Double ||
                input instanceof Long || input instanceof Float;
    }

    /**
     * Returns the new Version which is available if known otherwise null
     *
     * @return new available version or null
     */

    public static String getNewVersion() {
        return newVersion;
    }

    /**
     * Checks if there is a new plugin available
     *
     * @return true if new plugin is available
     */
    public static boolean isNewVersionAvailable(){
        return newVersionAvailable;
    }


    /**
     * Allows setting of the url which contains the actual version number
     * @param versionURL
     */
    public void alterVersionUrl(String versionURL) {
        this.versionURL = versionURL;
    }
}
