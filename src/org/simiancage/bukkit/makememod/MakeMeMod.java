package org.simiancage.bukkit.makememod;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * PluginName: MakeMeMod
 * Class: MakeMeMod
 * User: DonRedhorse
 * Date: 23.10.11
 * Time: 20:30
 */

public class MakeMeMod extends JavaPlugin{

    // Configuration Options
    private String configVer = '1.0'; // Internal Configversion
    protected String configVersion; // ConfigVersion from Config File
    protected boolean broadcastAll = true; // Broadcast group changes to all players
    protected boolean broadcastGroup = false; // Broadcast to specific groups
    protected ArrayList<String> broadcastTargets = new ArrayList<String>();
    protected Map<String, String> changeCommands = new HashMap<String, String>();

    ServerListenerMMM serverListener = new ServerListenerMMM(this);
    PlayerListenerMMM playerListener = new PlayerListenerMMM(this);
    boolean usingpex;
    protected Logger log;
    private MakeMeMod plugin = this;
    protected File configFile;
    protected String logName;
    protected String pluginName;
    protected String pluginVersion;
    protected String pluginPath;
    protected File persistanceFile;
    protected ArrayList<String> pluginAuthor;
    protected Map<Player, String> groupModification = new HashMap<Player, String>();
    protected static PermissionManager pexPlugin = null;
    protected FileConfiguration configuration;


    public void onEnable() {
        log = Bukkit.getServer().getLogger();
        pluginName = getDescription().getName();
        logName = "[" + pluginName + "] ";
        pluginVersion = getDescription().getVersion();
        pluginAuthor = getDescription().getAuthors();
        pluginPath = getDataFolder() + System.getProperty("file.separator");
        configFile = new File(pluginPath+"config.yml");
        persistanceFile = new File(pluginPath+"Persistance.obj");
        GetConfigMMM.CheckDefaultConfig();
        GetConfigMMM.GetConfig();
        private Object result;
        result = PersistanceMMM.load();
        if (!(result == null)) {
            groupModification =  (HashMap<Player, String>)result;
        }
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLUGIN_ENABLE, serverListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLUGIN_DISABLE, serverListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerListener, Event.Priority.Normal, this);
        addCommands();
        if (!configVer.equals(configVersion))
        {
            log.warning(logName + "Your config file is out of date! Rename your config and reload to see the new options.");
            log.warning(logName+"Using set options from config file and defaults for new options...");
        }
        log.info(logName+"v"+pluginVersion+" is enabled");
    }


    public void onDisable() {
        PersistanceMMM.save(groupModification);
        log.info(logName + "Disabled");
    }

    //// Sub Methods

    private void addCommands() {
        getCommand("mmm").setExecutor(new CommandMMM(this));
        // ToDo add alias commands
    }

    boolean changeGroup (Player player, String group, String world) {
        private boolean changedGroup = false
        if (usingpex)
        {
            PermissionUser user = pexPlugin.getUser(player);
            user.addGroup(group, world);
            changedGroup= true;
        }
        return changedGroup;
    }

    void sendMessage (String msg, Player player) {
        if (!broadcastAll && broadcastGroup && usingpex) {
            for (String groups: broadcastTargets) {
                for (Player player: getServer().getOnlinePlayers()){
                    String world = getServer().getPlayer(player.toString()).getWorld().getName();
                    if (pexPlugin.getUsers(groups,world).toString().contains(player.toString()))
                    {
                        getServer().getPlayer(player.toString()).sendMessage(msg);
                    }
                }
            }
        }
            if (broadcastAll) {
                for (Player player: getServer().getOnlinePlayers()){
                    getServer().getPlayer(player.toString()).sendMessage(msg);
                }
            }
        if (!(broadcastAll && broadcastGroup)) {
            getServer().getPlayer(player.toString()).sendMessage(msg);
        }

    }

}

