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
        String msg ="";
        if(!plugin.isValid(command)) return;
        Player player = event.getPlayer();
        event.setCancelled(true);
        if (player.hasPermission("mmm.command."+command)){
            String group = plugin.getGroup(command);
            if (group==null){
                msg = "Configuration error for command: "+command;
                plugin.sendMessage(msg, player);
                return;
            }
            String world = player.getWorld().getName();
            
            msg = plugin.executeChange(player, group, world);


        } else {
            msg = "You don't have the permission mmm.command."+command+"!";
        }
        plugin.sendMessage(msg, player);
    }


}
