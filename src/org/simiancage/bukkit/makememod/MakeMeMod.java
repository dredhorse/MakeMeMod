package org.simiancage.bukkit.makememod;

import org.bukkit.Bukkit;
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
    protected boolean broadcastGroup = false; // Broadcast to specific groups
    protected ArrayList<String> broadcastTargets = new ArrayList<String>();
    protected Map<String, Object> changeCommands;

    ServerListenerMMM serverListener = new ServerListenerMMM(this);
    PlayerListenerMMM playerListener = new PlayerListenerMMM(this);
    GetConfigMMM getConfigMMM = new GetConfigMMM(this);
    PersistanceMMM persistanceMMM = new PersistanceMMM(this);
    boolean usingPerm;
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
    MakeMeMod plugin = this;

    public enum PERM_SYS {PEX,BPER,SUPPERM}

    protected PERM_SYS permUsed;




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

    boolean changeGroup (Player player, String oldGroup, String newGroup, String world) {
        boolean changedGroup = false;
        if (usingPerm)
        {
            switch (permUsed){
                case PEX: {
                    PermissionUser user = pexPlugin.getUser(player);
                    groupModification.put(player, oldGroup+";"+newGroup+";"+world);
                    user.removeGroup(oldGroup,world);
                    user.addGroup(newGroup,world);
                    changedGroup= true;
                }
                default: {
                    player.sendMessage ("No Permission System found");
                }
            }
        }
        return changedGroup;
    }

    boolean changeBack (Player player) {
        boolean changedBack = false;
        if (usingPerm)
        {
            if (groupModification.containsKey(player))
            {
                String[] groupMod=groupModification.get(player).split(";");
                String oldGroup=groupMod[0];
                String newGroup=groupMod[1];
                String world=groupMod[2];
                String msg;
                switch (permUsed) {
                    case PEX: {

                        PermissionUser user = pexPlugin.getUser(player);
                        user.removeGroup(newGroup, world);
                        user.addGroup(oldGroup, world);
                        changedBack = setChangedBack(player, oldGroup, world);

                    }
                    default: {
                        msg = "Unsuccessfully changed "+player+" to group "+newGroup+" in world "+world;
                        sendMessage(msg,player);
                    }
                }




            }
        }
        return changedBack;
    }

    private boolean setChangedBack(Player player, String oldGroup, String world) {
        boolean changedBack;
        changedBack = true;
        groupModification.remove(player);
        String msg = "Successfully changed "+player+" to group "+oldGroup+" in world "+world;
        sendMessage(msg,player);
        return changedBack;
    }

    void sendMessage (String msg, Player player) {
        if (!(player==null)){
            System.out.print(broadcastAll);
            System.out.print(broadcastGroup);
            System.out.print((broadcastAll && broadcastGroup));
            if ((!broadcastAll && broadcastGroup && usingPerm)) {
                for (String groups: broadcastTargets) {
                    for (Player allPlayers: getServer().getOnlinePlayers()){
                        String world = getServer().getPlayer(allPlayers.toString()).getWorld().getName();
                        switch (permUsed){
                            case PEX: {

                                if (pexPlugin.getUsers(groups,world).toString().contains(allPlayers.getName()))
                                {
                                    allPlayers.sendMessage("bg"+msg);
                                }
                            }
                            default:
                            {
                                // Nothing realy
                            }
                        }
                    }
                }
            }
            if (broadcastAll) {
                getServer().broadcastMessage("ba"+msg);

            } else {
                player.sendMessage("op"+msg);
            }
        } else {
            log.info(msg);
        }
    }

    String executeChange(Player player, String oldGroup, String newGroup, String world) {
        String msg;
        if (changeGroup(player, oldGroup, newGroup, world)){
            msg = "Successfully changed "+player+" to group "+newGroup+" in world "+world;
        } else {
            msg = "Unsuccessfully changed "+player+" to group "+newGroup+" in world "+world;
        }
        return msg;
    }



    public Boolean isValid(String command)
    {
        System.out.print("ChangeCommands"+changeCommands);

        System.out.print("isvalid "+ changeCommands.containsKey(command));
        return changeCommands.containsKey(command);
        // return changeCommands.contains(command);
    }

    public String getOldGroup(String command) {
        // ToDo make sure this works
        String[] groups = changeCommands.get(command).toString().split(",");
        String oldGroup = groups[0];
        if (oldGroup==null){
            log.warning(logName+"There is no group for command:"+command);
        }
        return oldGroup;
    }

    public String getNewGroup(String command) {
        // ToDo make sure this works
        String[] groups = changeCommands.get(command).toString().split(",");
        String newGroup = groups[1];
        if (newGroup==null){
            log.warning(logName+"There is no group for command:"+command);
        }
        return newGroup;
    }

}

