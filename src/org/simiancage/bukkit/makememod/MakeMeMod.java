package org.simiancage.bukkit.makememod;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;


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
    protected boolean broadcastGroups = false; // Broadcast to specific groups
    protected ArrayList<String> broadcastTargets = new ArrayList<String>(); // List of groups to send messages to
    protected Map<String, Object> changeCommands; // Alias and group changes
    public boolean debug = false; // Debug Mode

    ServerListenerMMM serverListener = new ServerListenerMMM(this);
    PlayerListenerMMM playerListener = new PlayerListenerMMM(this);
    GetConfigMMM getConfigMMM = new GetConfigMMM(this);
    boolean usingPerm;
    protected Logger log;
    protected File configFile;
    protected String logName;
    protected String pluginName;
    protected String pluginVersion;
    protected String pluginPath;
    protected ArrayList<String> pluginAuthor;
    protected PermissionManager pexPlugin = null;
    protected FileConfiguration configuration;
    MakeMeMod plugin = this;
    protected boolean generalPermChanges;


    public enum PERM_SYS {PEX,BPER,SUPPERM}

    protected PERM_SYS permUsed;




    public void onEnable() {

        log = Bukkit.getServer().getLogger();
        pluginName = getDescription().getName();
        logName = "[" + pluginName + "] ";
        pluginVersion = getDescription().getVersion();
        pluginAuthor = getDescription().getAuthors();
        pluginPath = getDataFolder() + System.getProperty("file.separator");
        configFile = new File(pluginPath+"config.yml");
        GetConfigMMM.CheckDefaultConfig();
        GetConfigMMM.GetConfig();
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLUGIN_ENABLE, serverListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLUGIN_DISABLE, serverListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerListener, Event.Priority.Normal, this);
        addCommands();
        if (!configVer.equals(configVersion))
        {
            log.warning(logName + "Your config file is out of date! Rename your config and reload to see the new options.");
            log.warning(logName + "Using set options from config file and defaults for new options...");
        }
        if (debug) log.info(logName+"Debug Logging is enabled!");
        log.info(logName+"v"+pluginVersion+" is enabled");
    }


    public void onDisable() {
        log.info(logName + "Disabled");
        GetConfigMMM getConfigMMM = null;
        CommandMMM commandMMM = null;
    }

    //// Sub Methods

    private void addCommands() {
        getCommand("mmm").setExecutor(new CommandMMM(this));

    }


    String changeGroup (Player player, String oldGroup, String newGroup, String world) {
        boolean changedGroup = false;
        String msg;
        if (usingPerm && (!(oldGroup.isEmpty() || newGroup.isEmpty())))
        {
            switch (permUsed){
                case PEX: {
                    PermissionUser user = pexPlugin.getUser(player);
                    boolean isUpgrade =false;
                    // Check if we are upgrading = player is still in the oldGroup
                    if (generalPermChanges)
                    {
                        isUpgrade = user.inGroup(oldGroup, false);
                    } else {
                        isUpgrade = user.inGroup(oldGroup, world, false);
                    }
                    if (!isUpgrade)
                    // We are going back to the oldGroup
                    {
                        String t = oldGroup;
                        oldGroup = newGroup;
                        newGroup = t;
                    }
                    if (generalPermChanges)
                    {
                        user.removeGroup(oldGroup);
                        user.addGroup(newGroup);
                    } else {
                        user.removeGroup(oldGroup,world);
                        user.addGroup(newGroup,world);
                    }
                    changedGroup= true;
                }

            }
        }
        if (changedGroup){
            msg = "Successfully changed "+ ChatColor.BLUE+player.getName()+ChatColor.WHITE +" to group "+ ChatColor.RED +newGroup+ ChatColor.WHITE + " in world "+ ChatColor.GREEN +world;
        } else {
            msg = "There was a problem with changing "+ChatColor.BLUE +player.getName();
        }

        return msg;
    }


    void sendMessage (String msg, Player player) {
        // ToDo condense it more, look for console sender and also enable logging
        if (!(player==null)){
            if ((!broadcastAll && broadcastGroups && usingPerm)) {
                for (String groups: broadcastTargets) {
                    for (Player allPlayers: getServer().getOnlinePlayers()){
                        String world = allPlayers.getWorld().getName();
                        switch (permUsed){
                            case PEX: {

                                if (pexPlugin.getUsers(groups,world).toString().contains(allPlayers.getName()))
                                {
                                    allPlayers.sendMessage(msg);
                                }
                            }

                        }
                    }
                }
            }
            if (broadcastAll) {
                getServer().broadcastMessage(msg);

            } else {
                player.sendMessage(msg);
            }
        } else {
            log.info(msg);
        }
    }





    public Boolean isValid(String command)
    {
        return changeCommands.containsKey(command);
    }

    public String getOldGroup(String command) {
        String[] groups = changeCommands.get(command).toString().split(",");
        String oldGroup = "";
        if (groups.length<2){
            log.warning(logName+"There is no correct configuration for command: "+command);
        } else {
        oldGroup = groups[0];
        }
        return oldGroup;
    }

    public String getNewGroup(String command) {
        String[] groups = changeCommands.get(command).toString().split(",");
        String newGroup = "";
        if (groups.length<2){
            log.warning(logName+"There is no correct configuration for command: "+command);
        } else {
            newGroup = groups[1];
        }
        return newGroup;
    }

    private void logToConsole(String varName, Object logToConsole) {
        if (plugin.debug)
        {
            plugin.log.info(plugin.logName+varName+"= "+logToConsole.toString());
        }
    }
}

