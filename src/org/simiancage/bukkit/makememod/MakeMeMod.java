package org.simiancage.bukkit.makememod;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;

import java.io.File;
import java.util.ArrayList;

/**
 * PluginName: MakeMeMod
 * Class: MakeMeMod
 * User: DonRedhorse
 * Date: 23.10.11
 * Time: 20:30
 */

public class MakeMeMod extends JavaPlugin{

    ServerListenerMMM serverListener = new ServerListenerMMM(this);
    PlayerListenerMMM playerListener = new PlayerListenerMMM(this);
    public static ConfigMMM config;
    public static LoggerMMM log;
    boolean usingPerm;
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

    public static LoggerMMM getLog(){
        return log;
    }


    public void onEnable() {

        log = new LoggerMMM(this);
        System.out.print(log);
        pluginName = getDescription().getName();
        logName = "[" + pluginName + "] ";
        pluginVersion = getDescription().getVersion();
        config  = ConfigMMM.getInstance();
        config.setupConfig(configuration, plugin);
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLUGIN_ENABLE, serverListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLUGIN_DISABLE, serverListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerListener, Event.Priority.Normal, this);
        addCommands();
        if (config.debugLogEnabled()) log.info("Debug Logging is enabled!");
        log.enableMsg();
    }


    public void onDisable() {
        log.disableMsg();
        config = null;

    }

    //// Sub Methods

    private void addCommands() {
        getCommand("mmm").setExecutor(new CommandMMM(this));
        log.debug("Commmands Enabled",getCommand("mmm") );

    }


    String changeGroup (Player player, String oldGroup, String newGroup, String world) {
        boolean changedGroup = false;
        String msg;
        if (usingPerm && (!(oldGroup.isEmpty() || newGroup.isEmpty())))
        {
            switch (permUsed){
                case PEX: {
                    PermissionUser user = pexPlugin.getUser(player);
                    boolean isUpgrade = false;
                    // Check if we are upgrading = player is still in the oldGroup
                    if (config.generalPermChanges())
                    {
                        isUpgrade = user.inGroup(oldGroup, false);
                    } else {
                        isUpgrade = user.inGroup(oldGroup, world, false);
                    }
                    log.debug("isUpgrade",isUpgrade );
                    if (!isUpgrade)
                    // We are going back to the oldGroup
                    {
                        String t = oldGroup;
                        oldGroup = newGroup;
                        newGroup = t;
                    }
                    if (config.generalPermChanges())
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
        log.debug("msg",msg );
        return msg;
    }


    void sendMessage (String msg, Player player) {
        // ToDo condense it more, look for console sender and also enable logging
        if (!(player==null)){
            if ((!config.broadcastAll() && config.broadcastGroups() && usingPerm)) {
                for (String groups: config.broadcastTargets()) {
                    for (Player allPlayers: getServer().getOnlinePlayers()){
                        String world = allPlayers.getWorld().getName();
                        switch (permUsed){
                            case PEX: {

                                if (pexPlugin.getUsers(groups,world).toString().contains(allPlayers.getName()))
                                {
                                    log.debug("allPlayers",msg );
                                    allPlayers.sendMessage(msg);
                                }
                            }

                        }
                    }
                }
            }
            if (config.broadcastAll()) {
                getServer().broadcastMessage(msg);
                log.debug("broadcastAll",msg );

            } else {
                player.sendMessage(msg);
                log.debug("player",msg );
            }
        } else {
            log.info(msg);
        }
    }
}

