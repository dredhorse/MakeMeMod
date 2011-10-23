package org.simiancage.bukkit.makememod;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/**
 * PluginName: MakeMeMod
 * Class: ServerListenerMMM
 * User: DonRedhorse
 * Date: 23.10.11
 * Time: 20:46
 */

public class ServerListenerMMM extends ServerListener{
    private MakeMeMod plugin;

    public ServerListenerMMM(MakeMeMod plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPluginDisable(PluginDisableEvent event) {
        PluginManager pm = plugin.getServer().getPluginManager();
        Plugin pex = pm.getPlugin("PermissionsEx");
        
    }
}

