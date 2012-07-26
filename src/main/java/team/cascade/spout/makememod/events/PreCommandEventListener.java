package team.cascade.spout.makememod.events;

import org.spout.api.Spout;
import org.spout.api.event.EventHandler;
import org.spout.api.event.Listener;
import org.spout.api.event.server.PreCommandEvent;
import org.spout.api.player.Player;
import team.cascade.spout.makememod.MakeMeMod;
import team.cascade.spout.makememod.config.CONFIG;
import team.cascade.spout.makememod.helper.Logger;
import team.cascade.spout.makememod.helper.Messenger;
import team.cascade.spout.makememod.messages.MESSAGES;

import java.util.Hashtable;

/**
 * We need to use a PreCommandEventListener Listener as we don't know how many commands to permission group mappings people will make<br>
 *
 * </br>
 * @author $Author: dredhorse$
 * @version $FullVersion$
 */
public class PreCommandEventListener implements Listener {

    MakeMeMod plugin;

    PreCommandEventListener instance;

    public PreCommandEventListener(MakeMeMod plugin){
        instance = this;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin(PreCommandEvent event) {
        if (event.isCancelled()){
            Logger.debug("PreCommandEvent was cancelled");
            return;
        }
        Logger.debug("PlayerLoginEvent executed");
        String command = event.getCommand();
        if (!isValid(command)){
            Logger.debug("Not a MakeMeMod command",command);
            return;
        }
        event.setCancelled(true);
        if (!(event.getCommandSource() instanceof Player)){
            Logger.info("You can't use this command from console!");
            return;
        }
        Player player = (Player) event.getCommandSource();
        String msg ="";
        if (player.hasPermission("makememod.change." + command)) {
            String[] groups = CONFIG.COMMAND_PERMISSION_GROUP_MAPPING.getMap().get(command).toString().split(",");
            Logger.debug("groups",groups);
            if (groups.length < 2) {
                Logger.warning("There is no correct configuration for command: " + command);
                Messenger.send(player,Messenger.replaceVariable(MESSAGES.CONFIGURATION_ERROR,"%(command)",command));
                return;
            } else {
                String oldGroup = groups[0];
                String newGroup = groups[1];
                if ((oldGroup == null || newGroup == null)) {
                    msg = Messenger.replaceVariable(MESSAGES.CONFIGURATION_ERROR,"%(command)",command);
                }  else {
                    msg = changeGroup(player, oldGroup, newGroup);
                }
            }

        } else {
            msg = Messenger.replaceVariable(MESSAGES.YOU_DONT_HAVE_PERMISSION,"%(command)",command);

        }
        sendMessage(player, msg);


    }

    private void sendMessage(Player player, String msg) {
        if (!CONFIG.BROADCAST_CHANGE_MESSAGE_TO_ALL.getBoolean() && CONFIG.BROADCAST_CHANGE_MESSAGE_TO_GROUP.getBoolean()) {
            Logger.debug("Broadcast Group", msg);
            for (String groups : CONFIG.BROADCAST_GROUP.getStringList()) {
                for (Player allPlayers : Spout.getEngine().getOnlinePlayers()) {
                    if (allPlayers.isInGroup(groups)){
                        Messenger.send(allPlayers,msg);
                    }
                }
            }
        }
        if (CONFIG.BROADCAST_CHANGE_MESSAGE_TO_ALL.getBoolean()) {
            Messenger.broadcastMessage(msg);
            Logger.debug("Broadcast All", msg);
        } else {
            Messenger.send(player,msg);
            Logger.debug("Broadcast Player", msg);
        }
    }


    private String changeGroup(Player player, String oldGroup, String newGroup) {
        boolean isUpgrade = true;
        boolean isInCorrectGroups = false;
        boolean changedGroup = false;
        Hashtable<String,Object> temp = new Hashtable<String, Object>();
        String msg = "";
        if (player.isInGroup(oldGroup)||player.isInGroup(newGroup)){
            isInCorrectGroups = true;
        } else {
            temp.put("%(oldGroup)",oldGroup);
            temp.put("%(newGroup)",newGroup);
            temp.put("%(player)",player.getDisplayName());
            Messenger.send(player,Messenger.dictFormat(MESSAGES.YOU_ARE_NOT_IN_THE_CORRECT_GROUPS_TO_USE_THAT_COMMAND.getMessage(),temp));
        }
        if (isInCorrectGroups){
            // Check if we are upgrading = player is still in the oldGroup
            isUpgrade = player.isInGroup(oldGroup);
            Logger.debug("isUpgrade", isUpgrade);
            if (!isUpgrade)
            // We are going back to the oldGroup
            {
                String t = oldGroup;
                oldGroup = newGroup;
                newGroup = t;
            }

            // todo waiting for setGroup implementation
            //  player.setGroup(newGroup);
            changedGroup = true;
            if (changedGroup) {
                temp.put("%(oldGroup)",oldGroup);
                temp.put("%(newGroup)",newGroup);
                temp.put("%(player)",player.getDisplayName());
                msg = Messenger.dictFormat(MESSAGES.SUCCESFULLY_CHANGED.getMessage(),temp);

            } else {
                msg = Messenger.dictFormat(player, MESSAGES.THERE_WAS_A_PROBLEM_CHANGING.getMessage());
            }
            Logger.debug("msg", msg);

        } else {
            msg = Messenger.dictFormat(player, MESSAGES.THERE_WAS_A_PROBLEM_CHANGING.getMessage());
        }
        return msg;
    }


    private boolean isValid(String command) {
        if (CONFIG.COMMAND_PERMISSION_GROUP_MAPPING.getMap().containsKey(command)){
            return true;
        }
        return false;
    }
}
