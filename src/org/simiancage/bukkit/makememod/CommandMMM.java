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
    	if (sender instanceof Player) {
    		player = (Player) sender;
    	}
      	if (command.getName().equalsIgnoreCase("mmm")){
                   String subcommand = // ToDo figure out how to get the command after mmm


    		return true;
    	}
    	return false;
    }



    public static Boolean isValid(String command)
    {
        // ToDo make sure this works
        return configuration.getConfigurationSection("alias-list").contains(command);
    }

    public static String getGroup(String command) {
        // ToDo make sure this works
        String group = configuration.getString("alias-list"+"."+command);
        if (group==null){
            plugin.log.warning(plugin.logName+"There is no group for command:"+command);
        }
        return group;
    }
}

