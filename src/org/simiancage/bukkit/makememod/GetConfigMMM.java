package org.simiancage.bukkit.makememod;



import org.bukkit.configuration.Configuration;

import java.io.*;
import java.util.ArrayList;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * PluginName: MakeMeMod
 * Class: GetConfigMMM
 * User: DonRedhorse
 * Date: 23.10.11
 * Time: 20:44
 */

public class GetConfigMMM {

    static public MakeMeMod plugin;
    static Configuration configuration;



    public GetConfigMMM(MakeMeMod plugin) {
        this.plugin = plugin;
        this.configuration = plugin.configuration;
    }


    static void CheckDefaultConfig() {

        if (!plugin.configFile.exists()) {
            new File(plugin.getDataFolder().toString()).mkdir();
            try {
                JarFile jar = new JarFile("plugins" + System.getProperty("file.separator") + plugin.getDescription().getName() + ".jar");
                ZipEntry config = jar.getEntry("config.yml");
                InputStream in = new BufferedInputStream(jar.getInputStream(config));
                OutputStream out = new BufferedOutputStream(new FileOutputStream(plugin.configFile));
                int c;
                while((c = in.read()) != -1){
                    out.write(c);
                }
                out.flush();
                out.close();
                in.close();
                jar.close();
                plugin.log.info(plugin.logName + "Default config created successfully!");
            } catch (Exception e)  {
                plugin.log.warning(plugin.logName + "Default config could not be created!");

            }
        }
    }

    @SuppressWarnings(value = "unchecked")
    static void GetConfig() {
        configuration = plugin.getConfig();
        configuration.addDefault("debug", plugin.debug);
        configuration.addDefault("broadcast-all", plugin.broadcastAll);
        configuration.addDefault("broadcast-groups", plugin.broadcastGroups);
        configuration.addDefault("broadcast-targets", plugin.broadcastTargets);
        configuration.addDefault("general-perm-changes", plugin.generalPermChanges);
        plugin.debug=configuration.getBoolean("debug");
        logToConsole("debug",plugin.debug);
        plugin.configVer=configuration.getString("configVer");
        logToConsole("configVer",plugin.configVer);
        plugin.broadcastAll=configuration.getBoolean("broadcast-all");
        logToConsole("broadcast-all",plugin.broadcastAll);
        plugin.broadcastGroups =configuration.getBoolean("broadcast-groups");
        logToConsole("broadcast-groups",plugin.broadcastGroups);
        plugin.broadcastTargets=(ArrayList<String>) configuration.getList("broadcast-targets");
        logToConsole("broadcast-targets",plugin.broadcastTargets);
        plugin.generalPermChanges=configuration.getBoolean("general-perm-changes");
        logToConsole("general-perm-changes",plugin.generalPermChanges);
        plugin.changeCommands=configuration.getConfigurationSection("alias-list").getValues(true);
        logToConsole("alias-list",plugin.changeCommands);
    }

    private static void logToConsole(String varName, Object logToConsole) {
        if (plugin.debug)
        {
        plugin.log.info(plugin.logName+varName+"= "+logToConsole.toString());
        }
    }


}

