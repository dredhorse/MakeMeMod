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
                    GetConfigMMM.GetConfig();
                    msg = plugin.logName + "reloaded config!";
                } else {
                    cmd = args[0];
                    if (plugin.isValid(cmd)){
                        String group = plugin.getGroup(cmd);
                        String world = plugin.getServer().getPlayer(player.toString()).getWorld().getName();
                        msg = plugin.executeChange(player, group, world);

                    }else{
                        msg = plugin.logName + "there is no config for: "+cmd;
                    }
                }
            plugin.sendMessage(msg, player);
            } else {
                msg = "/mmm groupname = switches to groupname and back";
                player.sendMessage(msg);
            }

    		return true;
    	}
    	return false;
    }


}

