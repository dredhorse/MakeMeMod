package org.simiancage.bukkit.makememod;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

/**
 * PluginName: MakeMeMod
 * Class: ServerListenerMMM
 * User: DonRedhorse
 * Date: 23.10.11
 * Time: 20:46
 */

public class ServerListenerMMM extends ServerListener{
    private static MakeMeMod plugin;


    public ServerListenerMMM(MakeMeMod plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPluginEnable(PluginEnableEvent event) {
        PluginManager pm = plugin.getServer().getPluginManager();
        Plugin pex = pm.getPlugin("PermissionsEx");
        if (!plugin.usingpex && !(pex == null))
        {
            plugin.pexPlugin = PermissionsEx.getPermissionManager();
            plugin.usingpex = true;
            plugin.log.info(plugin.logName + "is using PEX now.");
        }

    }

    @Override
    public void onPluginDisable(PluginDisableEvent event) {
        PluginManager pm = plugin.getServer().getPluginManager();
        Plugin pex = pm.getPlugin("PermissionsEx");
        if (plugin.usingpex && (pex == null))
        {
            plugin.usingpex = false;
            plugin.log.info(plugin.logName + "is not using PEX anymore.");

        }
    }
}

