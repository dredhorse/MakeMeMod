package org.simiancage.bukkit.makememod;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import org.simiancage.bukkit.makememod.GetConfigMMM;


import java.io.File;
import java.util.*;
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
    private String configVersion = "1.0"; // Internal Configversion
    protected String configVer; // ConfigVersion from Config File
    protected boolean broadcastAll = true; // Broadcast group changes to all players
    protected boolean broadcastGroup = false; // Broadcast to specific groups
    protected ArrayList<String> broadcastTargets = new ArrayList<String>();
    protected List<String> changeCommands;

    ServerListenerMMM serverListener = new ServerListenerMMM(this);
    PlayerListenerMMM playerListener = new PlayerListenerMMM(this);
    GetConfigMMM getConfigMMM = new GetConfigMMM(this);
    PersistanceMMM persistanceMMM = new PersistanceMMM(this);
    boolean usingpex;
    protected Logger log;
    protected File configFile;
    protected String logName;
    protected String pluginName;
    protected String pluginVersion;
    protected String pluginPath;
    protected File persistanceFile;
    protected ArrayList<String> pluginAuthor;
    protected Map<Player, String> groupModification = new HashMap<Player, String>();
    protected PermissionManager pexPlugin = null;
    protected FileConfiguration configuration;




    public void onEnable() {

        // CommandMMM commandMMM = new CommandMMM(this);
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
        Object result;
        result = PersistanceMMM.load();
        if (!(result == null)) {
            groupModification =  (HashMap<Player, String>)result;
            // Todo making persistance work
        }
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLUGIN_ENABLE, serverListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLUGIN_DISABLE, serverListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerListener, Event.Priority.Normal, this);
        addCommands();
        System.out.print(configVer);
        if (!configVer.equals(configVersion))
        {
            log.warning(logName + "Your config file is out of date! Rename your config and reload to see the new options.");
            log.warning(logName + "Using set options from config file and defaults for new options...");
        }
        log.info(logName+"v"+pluginVersion+" is enabled");
    }


    public void onDisable() {
        PersistanceMMM.save(groupModification);
        log.info(logName + "Disabled");
        GetConfigMMM getConfigMMM = null;
        PersistanceMMM persistanceMMM = null;
        CommandMMM commandMMM = null;
    }

    //// Sub Methods

    private void addCommands() {
        getCommand("mmm").setExecutor(new CommandMMM(this));

    }

    boolean changeGroup (Player player, String group, String world) {
        boolean changedGroup = false;
        if (usingpex)
        {
            PermissionUser user = pexPlugin.getUser(player);
            user.addGroup(group, world);
            changedGroup= true;
        }
        return changedGroup;
    }

    void sendMessage (String msg, Player player) {
        if (!(player==null)){

            if (!broadcastAll && broadcastGroup && usingpex) {
                for (String groups: broadcastTargets) {
                    for (Player allPlayers: getServer().getOnlinePlayers()){
                        String world = getServer().getPlayer(allPlayers.toString()).getWorld().getName();
                        if (pexPlugin.getUsers(groups,world).toString().contains(allPlayers.toString()))
                        {
                            getServer().getPlayer(allPlayers.toString()).sendMessage(msg);
                        }
                    }
                }
            }
            if (broadcastAll) {
                for (Player allPlayers: getServer().getOnlinePlayers()){
                    getServer().getPlayer(allPlayers.toString()).sendMessage(msg);
                }
            }
            if (!(broadcastAll && broadcastGroup)) {
                getServer().getPlayer(player.toString()).sendMessage(msg);
            }
        } else {
            System.out.print(msg);
        }
    }

    String executeChange(Player player, String group, String world) {
        String msg;
        if (changeGroup(player, group, world)){
            msg = "Successfully changed "+player+" to group "+group+" in world "+world;
        } else {
            msg = "Unsuccessfully changed "+player+" to group "+group+" in world "+world;
        }
        return msg;
    }

    public Boolean isValid(String command)
    {
        // ToDo make sure this works
        return changeCommands.contains(command);
    }

    public String getGroup(String command) {
        // ToDo make sure this works
        int ind = changeCommands.indexOf(command);
        String[] changeCommand = changeCommands.get(ind).split(":");
        String group = changeCommand[1];
        if (group==null){
            log.warning(logName+"There is no group for command:"+command);
        }
        return group;
    }

}

