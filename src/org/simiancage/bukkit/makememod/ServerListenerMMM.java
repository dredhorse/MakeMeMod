package org.simiancage.bukkit.makememod;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.simiancage.bukkit.makememod.MakeMeMod.PERM_SYS;
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
    private static LoggerMMM log;
    private static ConfigMMM config;

    public ServerListenerMMM(MakeMeMod plugin) {
        this.plugin = plugin;

    }

    @Override
    public void onPluginEnable(PluginEnableEvent event) {
        log = MakeMeMod.getLog();
        config = ConfigMMM.getInstance();
        PluginManager pm = plugin.getServer().getPluginManager();
        Plugin pex = pm.getPlugin("PermissionsEx");
        Plugin vault = pm.getPlugin("Vault");
        if (!plugin.usingPerm && (pex != null))
        {
            plugin.pexPlugin = PermissionsEx.getPermissionManager();
            if (plugin.permUsed.equals(PERM_SYS.NULL) || (plugin.permUsed.equals(PERM_SYS.VAULT) && !config.preferVault()))
            {
                plugin.usingPerm = true;
                plugin.permUsed = PERM_SYS.PEX;
                log.info("is using PEX now.");
            }
        }

        if (!plugin.usingVault && (vault != null))
        {
            if ((plugin.permUsed.equals(PERM_SYS.PEX) && config.preferVault()) || plugin.permUsed.equals(PERM_SYS.NULL) )
            {
                RegisteredServiceProvider<Permission> permissionProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
                if (permissionProvider != null) {
                    plugin.permission = permissionProvider.getProvider();
                    if (!plugin.permission.getName().equalsIgnoreCase("superperms"))
                    {
                        plugin.usingVault = true;
                        plugin.usingPerm = true;
                        plugin.permUsed = PERM_SYS.VAULT;
                        log.info("found Vault with Permission Provider "+plugin.permission.getName());
                        log.info("is using Vault now.");
                    } else {
                        log.warning("found Vault with SuperPerms only!");
                        log.warning("Can't provide group changing!");
                        log.warning("not using Vault!");
                    }
                }  else {
                    log.warning("found Vault without a Permission Provider!");
                    log.warning("not using Vault!");
                }
            }
        }

    }

    @Override
    public void onPluginDisable(PluginDisableEvent event) {
        log = MakeMeMod.getLog();
        PluginManager pm = plugin.getServer().getPluginManager();
        Plugin pex = pm.getPlugin("PermissionsEx");
        Plugin vault = pm.getPlugin("Vault");
        if (plugin.usingPerm && (pex == null))
        {
            plugin.usingPerm = false;
            plugin.permUsed = PERM_SYS.NULL;
            log.warning("is not using PEX anymore.");

        }
        if (plugin.usingVault && (vault == null)){
            plugin.permUsed = PERM_SYS.NULL;
            plugin.usingPerm = false;
            log.warning("is not using Vault anymore.");
        }
    }

}

