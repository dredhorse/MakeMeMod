package org.simiancage.bukkit.makememod;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;

import java.io.IOException;

/**
 * PluginName: MakeMeMod
 * Class: MakeMeMod
 * User: DonRedhorse
 * Date: 23.10.11
 * Time: 20:30
 */

public class MakeMeMod extends JavaPlugin {

    private ServerListenerMMM serverListener; // = new ServerListenerMMM(this);
    private PlayerListenerMMM playerListener; // = new PlayerListenerMMM(this);
    private static ConfigMMM config;
    public static LoggerMMM log;
    boolean usingPerm;
    String logName;
    private String pluginName;
    private String pluginVersion;
    PermissionManager pexPlugin = null;
    private FileConfiguration configuration;
    private final MakeMeMod plugin = this;
    boolean usingVault;
    Permission permission = null;


    public enum PERM_SYS {PEX, VAULT, BPERM, NULL}

    PERM_SYS permUsed = PERM_SYS.NULL;

    public static LoggerMMM getLog() {
        return log;
    }


    public void onEnable() {

        log = LoggerMMM.getInstance(this);
        pluginName = getDescription().getName();
        logName = "[" + pluginName + "] ";
        pluginVersion = getDescription().getVersion();
        config = ConfigMMM.getInstance();
        config.setupConfig(configuration, plugin);
        PluginManager pm = getServer().getPluginManager();
        serverListener = new ServerListenerMMM(this);
        playerListener = new PlayerListenerMMM(this);
        pm.registerEvents(serverListener, this);
        pm.registerEvents(playerListener, this);
        addCommands();
        log.enableMsg();

	    try {
	        // create a new metrics object
	        Metrics metrics = new Metrics();

	        // 'this' in this context is the Plugin object
	        metrics.beginMeasuringPlugin(this);
	    } catch (IOException e) {
	        // Failed to submit the stats :-(
	    }
    }


    public void onDisable() {
        log.disableMsg();
    }

    //// Sub Methods

    private void addCommands() {
        getCommand("mmm").setExecutor(new CommandMMM(this));
        log.debug("Commmands Enabled", getCommand("mmm"));

    }


    String changeGroup(Player player, String oldGroup, String newGroup, String world) {
        boolean changedGroup = false;
        String msg;
        if (usingPerm && (!(oldGroup.isEmpty() || newGroup.isEmpty()))) {
            log.debug("permUsed", permUsed);
            log.debug("generalPermChanges", config.isGeneralPermChanges());
            switch (permUsed) {
                case PEX: {
                    PermissionUser user = pexPlugin.getUser(player);
                    boolean isUpgrade;
                    // Check if we are upgrading = player is still in the oldGroup
                    if (config.isGeneralPermChanges()) {
                        isUpgrade = user.inGroup(oldGroup, false);
                    } else {
                        isUpgrade = user.inGroup(oldGroup, world, false);
                    }
                    log.debug("isUpgrade", isUpgrade);
                    if (!isUpgrade)
                    // We are going back to the oldGroup
                    {
                        String t = oldGroup;
                        oldGroup = newGroup;
                        newGroup = t;
                    }
                    if (config.isGeneralPermChanges()) {
                        user.removeGroup(oldGroup);
                        user.addGroup(newGroup);
                    } else {
                        user.removeGroup(oldGroup, world);
                        user.addGroup(newGroup, world);
                    }
                    changedGroup = true;
                    break;
                }
                case VAULT: {

                    boolean isUpgrade;
                    // Check if we are upgrading = player is still in the oldGroup
                    if (config.isGeneralPermChanges()) {
                        isUpgrade = permission.playerInGroup(player, oldGroup);
                    } else {
                        log.debug("world", world);
                        log.debug("player", player.getName());
                        isUpgrade = permission.playerInGroup(world, player.getName(), oldGroup);
                    }
                    log.debug("isUpgrade", isUpgrade);
                    if (!isUpgrade) {
                        String t = oldGroup;
                        oldGroup = newGroup;
                        newGroup = t;
                        log.debug("oldGroup", oldGroup);
                        log.debug("newGroup", newGroup);
                    }
                    if (config.isGeneralPermChanges()) {
                        boolean t;
                        log.debug("generalPermChanges", config.isGeneralPermChanges());
                        t = permission.playerRemoveGroup(player, oldGroup);
                        log.debug("removeGroup", t);
                        t = permission.playerAddGroup(player, newGroup);
                        log.debug("addGroup", t);
                    } else {
                        boolean t;
                        t = permission.playerRemoveGroup(world, player.getName(), oldGroup);
                        log.debug("removeGroup", t);
                        t = permission.playerAddGroup(world, player.getName(), newGroup);
                        log.debug("addGroup", t);
                    }
                    changedGroup = true;
                    break;
                }
                case BPERM: {

                    boolean isUpgraded;
                    // Check if we are upgrading = player is not already in newGroup
                    if (config.isGeneralPermChanges()) {
                        isUpgraded = permission.playerInGroup(player, newGroup);
                    } else {
                        log.debug("world", world);
                        log.debug("player", player.getName());
                        isUpgraded = permission.playerInGroup(world, player.getName(), newGroup);
                    }
                    log.debug("isUpgraded", isUpgraded);
                    if (!isUpgraded) {
                        if (config.isGeneralPermChanges()) {
                            boolean t;
                            log.debug("generalPermChanges", config.isGeneralPermChanges());
                            t = permission.playerAddGroup(player, newGroup);
                            log.debug("addGroup", t);
                        } else {
                            boolean t;
                            t = permission.playerAddGroup(world, player.getName(), newGroup);
                            log.debug("addGroup", t);
                        }
                    } else {
                        if (config.isGeneralPermChanges()) {
                            boolean t;
                            log.debug("generalPermChanges", config.isGeneralPermChanges());
                            t = permission.playerRemoveGroup(player, newGroup);
                            log.debug("removeGroup", t);
                        } else {
                            boolean t;
                            t = permission.playerRemoveGroup(world, player.getName(), newGroup);
                            log.debug("removeGroup", t);

                        }
                        newGroup = oldGroup;
                    }
                    changedGroup = true;
                    break;
                }

            }
        }
        if (changedGroup) {
            msg = "Successfully changed " + ChatColor.BLUE + player.getName() + ChatColor.WHITE + " to group " + ChatColor.RED + newGroup + ChatColor.WHITE + " in world " + ChatColor.GREEN + world;
        } else {
            msg = "There was a problem with changing " + ChatColor.BLUE + player.getName();
        }
        log.debug("msg", msg);
        return msg;
    }


    void sendMessage(String msg, Player player) {
        // ToDo condense it more, look for console sender and also enable logging
        if (!(player == null)) {
            if ((!config.isBroadcastAll() && config.isBroadcastGroups() && usingPerm)) {
                log.debug("msg", msg);
                for (String groups : config.broadcastTargets()) {
                    for (Player allPlayers : getServer().getOnlinePlayers()) {
                        String world = allPlayers.getWorld().getName();
                        switch (permUsed) {
                            case PEX: {

                                if (pexPlugin.getUsers(groups, world).toString().contains(allPlayers.getName())) {
                                    log.debug("allPlayers", allPlayers);
                                    allPlayers.sendMessage(msg);
                                }
                            }
                            case VAULT: {
                                if (permission.playerInGroup(allPlayers, groups)) {
                                    log.debug("allPlayers", allPlayers);
                                    allPlayers.sendMessage(msg);
                                }
                            }
                            case BPERM: {
	                            if (permission.playerInGroup(allPlayers, groups)){
		                            log.debug("allPlayers", allPlayers);
		                            allPlayers.sendMessage(msg);
	                            }
                            }

                        }
                    }
                }
            }
            if (config.isBroadcastAll()) {
                getServer().broadcastMessage(msg);
                log.debug("broadcastAll", msg);

            } else {
                player.sendMessage(msg);
                log.debug("player", msg);
            }
        } else {
            log.info(msg);
        }
    }
}

