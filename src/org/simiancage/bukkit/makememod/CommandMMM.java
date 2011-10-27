package org.simiancage.bukkit.makememod;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
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
    private static Configuration configuration;


    public CommandMMM(MakeMeMod plugin) {
        this.plugin = plugin;
        this.configuration = plugin.configuration;
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
                    plugin.reloadConfig();
                    GetConfigMMM.GetConfig();
                    msg = plugin.logName + "reloaded config!";
                    plugin.sendMessage(msg, player);
                    return true;
                } else {

                    cmd = args[0];
                    System.out.print("well "+cmd);
                    System.out.print(player.getName());
                    if (player.hasPermission("mmm.command.engi")){
                        System.out.print("hmm"+plugin.isValid(cmd));
                        if (plugin.isValid(cmd)){
                            String newGroup = plugin.getNewGroup(cmd);
                            String oldGroup = plugin.getOldGroup(cmd);
                            String world = player.getWorld().getName();
                            if (!plugin.changeBack(player))
                            {
                                msg = plugin.executeChange(player, oldGroup, newGroup, world);
                            }

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
                    plugin.log.info(plugin.logName + "Try mmm reload or just let it be!");
                }
            }

            return true;
        }
        return false;
    }


}

