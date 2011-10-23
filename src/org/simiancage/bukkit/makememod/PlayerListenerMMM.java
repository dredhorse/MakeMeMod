package org.simiancage.bukkit.makememod;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;

/**
 * PluginName: MakeMeMod
 * Class: PlayerListenerMMM
 * User: DonRedhorse
 * Date: 23.10.11
 * Time: 23:26
 */

public class PlayerListenerMMM extends PlayerListener{

    private MakeMeMod plugin;

    public PlayerListenerMMM (MakeMeMod plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String[] args = event.getMessage().split(" ");
        String command = args[0].substring(1);
        if(!CommandMMM.isValid(command)) return;
        Player player = event.getPlayer();
        event.setCancelled(true);
        if (player.hasPermission("mmm.command."+command)){
            String group = CommandMMM.getGroup(command);
            if (group==null){
                player.sendMessage("Configuration error for command: "+command);
                return;
            }
            String world = player.getWorld().getName();
            String msg;
            if (plugin.changeGroup(player, group, world)){
                msg = "Successfully changed "+player+" to group "+group+" in world "+world;
            } else {
                msg = "Unuccessfully changed "+player+" to group "+group+" in world "+world;
            }
            plugin.sendMessage(msg, player);

        } else {
            player.sendMessage("You don't have the permission mmm.command."+command+"!");
        }
    }
}

