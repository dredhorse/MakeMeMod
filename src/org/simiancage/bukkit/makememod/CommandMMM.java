package org.simiancage.bukkit.makememod;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * PluginName: MakeMeMod
 * Class: CommandMMM
 * User: DonRedhorse
 * Date: 23.10.11
 * Time: 22:25
 */

public class CommandMMM implements CommandExecutor {

    private static MakeMeMod plugin;
    private static LoggerMMM log;
    private static ConfigMMM config;


    public CommandMMM(MakeMeMod plugin) {
        this.plugin = plugin;
        log = MakeMeMod.log;
        config = ConfigMMM.getInstance();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = null;
        String msg = "";
        String cmd = "";
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (command.getName().equalsIgnoreCase("mmm")){

            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("reload")){
                    config.reloadConfig();
                    msg = plugin.logName + "reloaded config!";
                    plugin.sendMessage(msg, player);
                    return true;
                } else {
                    cmd = args[0];
                    if (player.hasPermission("mmm.command."+cmd)){
                        if (config.isValid(cmd)){
                            String newGroup = config.getNewGroup(cmd);
                            String oldGroup = config.getOldGroup(cmd);
                            String world = player.getWorld().getName();
                            msg = plugin.changeGroup(player, oldGroup, newGroup, world);
                        }else{
                            msg = plugin.logName + "there is no config for: "+cmd;
                        }
                        plugin.sendMessage(msg, player);
                    } else {
                        player.sendMessage("You don't have the permission to do that.");
                    }

                }
            } else {
                msg = "/mmm groupname = switches to groupname and back";
                if (!(player==null)){
                    player.sendMessage(msg);
                } else {
                    log.info("Try mmm reload or just let it be!");
                }
            }

            return true;
        }
        return false;
    }

}

