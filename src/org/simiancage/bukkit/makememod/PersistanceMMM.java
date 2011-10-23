package org.simiancage.bukkit.makememod;


import java.io.*;

/**
 * PluginName: MakeMeMod
 * Class: PersistanceMMM
 * User: DonRedhorse
 * Date: 23.10.11
 * Time: 21:12
 * based on code from Tomsik68
 */

public class PersistanceMMM {

    private static MakeMeMod plugin;

    public PersistanceMMM(MakeMeMod plugin) {
        this.plugin = plugin;
    }

    public static Object load() {
        Object result = null;
        if (plugin.persistanceFile.exists()) {
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(new FileInputStream(plugin.persistanceFile));
                result = ois.readObject();
                ois.close();
            } catch (Exception e) {
                plugin.log.warning(plugin.logName + "Couldn't load Persistance File, dropped group changes!!");
                plugin.log.warning(plugin.logName+"Please check your permissions!!");
                plugin.getServer().broadcastMessage(plugin.logName + "Couldn't load Persistance File, dropped group changes!!");
                plugin.getServer().broadcastMessage(plugin.logName+"Please check you permissions!!");
                e.printStackTrace();
            }

        }
        return result;
    }

    public static void save(Object obj)
    {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(plugin.persistanceFile));
            oos.writeObject(obj);
            oos.flush();
            oos.close();
        } catch (Exception e) {
            plugin.log.warning(plugin.logName + "Couldn't save Persistance File, dropped group changes!!");
            plugin.log.warning(plugin.logName+"Please check your permissions!!");
            plugin.getServer().broadcastMessage(plugin.logName + "Couldn't save Persistance File, dropped group changes!!");
            plugin.getServer().broadcastMessage(plugin.logName+"Please check you permissions!!");
            e.printStackTrace();
        }

    }
}

