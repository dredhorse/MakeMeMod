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

public class PlayerListenerMMM extends PlayerListener {

    private ConfigMMM config = ConfigMMM.getInstance();
    private MakeMeMod plugin;
    private LoggerMMM log;

    public PlayerListenerMMM(MakeMeMod plugin) {
        this.plugin = plugin;
        log = MakeMeMod.log;
    }

    @Override
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String[] args = event.getMessage().split(" ");
        String command = args[0].substring(1);
        String msg;
        if (!config.isValid(command)) {
            return;
        }
        Player player = event.getPlayer();
        event.setCancelled(true);
        if (player.hasPermission("mmm.command." + command)) {
            String oldGroup = config.getOldGroup(command);
            String newGroup = config.getNewGroup(command);
            if ((oldGroup == null || newGroup == null)) {
                msg = "Configuration error for command: " + command;
                player.sendMessage(msg);
                return;
            }
            String world = player.getWorld().getName();

            msg = plugin.changeGroup(player, oldGroup, newGroup, world);


        } else {
            msg = "You don't have the permission mmm.command." + command + "!";
        }
        plugin.sendMessage(msg, player);
    }


}

