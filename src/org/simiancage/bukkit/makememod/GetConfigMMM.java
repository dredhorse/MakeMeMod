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

    static void GetConfig() {
        configuration = plugin.getConfig();
        configuration.addDefault("broadcast-all", plugin.broadcastAll);
        configuration.addDefault("broadcast-group", plugin.broadcastGroup);
        configuration.addDefault("broadcast-targets", plugin.broadcastTargets);
        plugin.configVer=configuration.getString("configVer");
        System.out.print(plugin.configVer);
        plugin.broadcastAll=configuration.getBoolean("broadcast-all");
        System.out.print(plugin.broadcastAll);
        plugin.broadcastGroup=configuration.getBoolean("broadcast-groups");
        System.out.print(plugin.broadcastGroup);
        plugin.broadcastTargets=(ArrayList<String>) configuration.getList("broadcast-targets");
        System.out.print(plugin.broadcastTargets);
        plugin.changeCommands=configuration.getList("alias-list");
        System.out.print(plugin.changeCommands);
    }



}

