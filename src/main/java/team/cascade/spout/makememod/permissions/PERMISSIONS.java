package team.cascade.spout.makememod.permissions;
//~--- non-JDK imports --------------------------------------------------------

import team.cascade.spout.makememod.exceptions.NoPermissionException;
import team.cascade.spout.makememod.helper.Logger;
import org.spout.api.command.CommandSource;
import org.spout.api.player.Player;
import org.spout.api.plugin.Plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//~--- JDK imports ------------------------------------------------------------


//~--- enums ------------------------------------------------------------------


/**
 * ENUM for permissions.
 * <br>
 * The ENUM supports a #has method which returns true if the player has the permission or throws a @throws NoPermissionException. <br>
 * It will also directly parse any permission sets you create, for example you can have the following permissions:
 * <br>
 * * hlp.cls.test  = which is the base permission or the HelperClass <br>
 * * hlp.cls.user  = which is the user permission, but also has the hlp.cls.test permission<br>
 * * hlp.cls.admin = which is the admin permission, but also has the hlp.cls.test and hlp.cls.user permission<br>
 * <br>
 * You can name the permission nodes like you want and put as many permission nodes you like into a permission.
 * <br>
 * The permissions will be automatically registered with the server and also dumped into a file called permissions.yml
 * for easy reference.
 * <br>
 * based on code from
 *
 * @author bergerkiller
 *         from http://bit.ly/zlhZg1
 * @author $Author: dredhorse$
 * @todo You need to add all the permissions you want to use into here. Just take a look at the examples.
 */

public enum PERMISSIONS {


    /**
     * Example Permission MAKEMEMOD_CHANGE which will be turned into makememod.change, </p>
     * has a description of "Base permission for the Group changes" and an RequireAll permissions of false
     */

    MAKEMEMOD_CHANGE("Base permission for the Group changes", false),
    MAKEMEMOD_USER("User permission", false, MAKEMEMOD_CHANGE),
    MAKEMEMOD_ADMIN("Admin permission, will also be used for broadcasting messages to admins", false, MAKEMEMOD_CHANGE, MAKEMEMOD_USER);


    // NO CHANGES BELOW HERE!!!!!


    /**
     * Maximum Depths of PermissionsSets
     */

    private static final int MAX_DEPTH_PERMISSIONS_SETS = 2;

    /**
     * Storage for the permissions sets mapping.
     */

    private Map<PERMISSIONS, List<PERMISSIONS>> permissionsSet = new HashMap<PERMISSIONS, List<PERMISSIONS>>();


    /**
     * permissions sets for a permission
     */

    private PERMISSIONS[] starPerms = {};


    /**
     * the comment for the permission
     */

    private String comment;

    /**
     * Require All
     */

    private boolean requireAll = false;

    //~--- constructors -------------------------------------------------------


    /**
     * Creating the ENUM with the correct information
     *
     * @param comment    comment for the permission
     * @param requireAll boolean value for require all, which is being used by commands (probably not necessary or just wrong here.
     * @param starPerms  additional permissions which are included in the permissions sets
     */

    private PERMISSIONS(String comment, boolean requireAll, PERMISSIONS... starPerms) {
        this.starPerms = starPerms;
        this.comment = comment;
        this.requireAll = requireAll;

        // adding the starPerms reverse into the permissionsSet hashmap
        if (starPerms.length > 0) {
            Logger.debug("Adding star permissions");
            for (PERMISSIONS perms : starPerms) {
                if (permissionsSet.containsKey(perms)) {
                    List<PERMISSIONS> temp = permissionsSet.get(perms);

                    if (!temp.contains(this)) {
                        temp.add(this);
                        permissionsSet.put(perms, temp);
                    }
                } else {
                    permissionsSet.put(perms, Arrays.asList(this));
                }
            }
        }
    }

    //~--- methods ------------------------------------------------------------


    /**
     * Retrieves the permission comment
     */

    public String getComment() {
        return comment;
    }


    /**
     * Sets the permission comment
     */

    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Return the permission as string
     */

    public String value() {
        return asPermission();
    }

    /**
     * Retrieves requireAll
     */
    public boolean requireAll() {
        return requireAll;
    }

    /**
     * Retrieves requireAll
     */
    public boolean getRequireAll() {
        return requireAll;
    }

    /**
     * Sets the requireAll
     */

    public void setRequireAll(boolean requireAll) {
        this.requireAll = requireAll;
    }


    /**
     * Converts the ENUM into a string expression of the permission node
     *
     * @return s as string expression
     */

    @Override
    public String toString() {
        return toPermission(super.toString());
    }


    /**
     * Creates a permission node syntax out of the enum, eg MAKEMEMOD_USER => hlp.cls.user
     * Replaces _ with . and converts the ENUM to lowercase.
     *
     * @param s string to convert to permission node syntax
     * @return s as permission node syntax
     */

    static String toPermission(String s) {
        s = s.replace("_", ".");
        s = s.toLowerCase();

        return s;
    }


    /**
     * Converts the ENUM Permission into a string representing the permission.
     *
     * @return permission
     */


    public String asPermission() {
        return this.toString();
    }


    /**
     * handle throws a NoPermissionException, which can be handled by the underlying command invoker.
     *
     * @param sender CommandSource
     * @throws team.cascade.spout.makememod.exceptions.NoPermissionException
     *
     */

    public final void handle(CommandSource sender) throws NoPermissionException {
        if (!(sender instanceof Player)) {
            return;
        }

        if (!this.has((Player) sender)) {
            throw new NoPermissionException(this, (Player) sender);
        }
    }

    //~--- get methods --------------------------------------------------------


    /**
     * Will return true if the player either has the permission directly or if he has one of the permissions Sets.
     * Please note that those permissions Sets are NOT permission.* but a permission node you define and that those
     * permissions Sets only work with this method or the handle method.
     * <br>
     * NOTE: You can cause an endless loop if you have one permission node in permissionSet which contains that
     * permission node in a permissionSet again!
     *
     * @param player Player to check permissions for.
     * @return true / false
     */

    public boolean has(Player player) {
        if (player.hasPermission(this.toString())) {
            return true;
        }

        // checking if the player has the permissions because of the permissionsSet
        if (permissionsSet.size() > 0) {
            List<PERMISSIONS> temp = permissionsSet.get(this);
            if (temp != null) {
                for (PERMISSIONS perm : temp) {
                    if (perm.has(player, 1)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    private boolean has(Player player, int depth) {
        if (player.hasPermission(this.toString())) {
            return true;
        }

        // checking if the player has the permissions because of the permissionsSet
        if (depth <= MAX_DEPTH_PERMISSIONS_SETS) {
            if (permissionsSet.size() > 0) {
                List<PERMISSIONS> temp = permissionsSet.get(this);
                if (temp != null) {
                    for (PERMISSIONS perm : temp) {
                        if (perm.has(player, depth + 1)) {
                            return true;
                        }
                    }
                }
            }
        } else {
            Logger.warning("MaxDepthPermissionSets reached while checking permissions");
            Logger.warning("Please contact the developer!");
        }
        return false;


    }

    //~--- methods ------------------------------------------------------------


/**
 * will register the permission with spout
 */

    //todo figure out if this is still necessary

/*
    public final void register() {
        // if we have a star permission we also register all the children
        if (starPerms.length > 0) {
            Map<String, Boolean> children = new HashMap<String, Boolean>();
            for (PERMISSIONS permissions : starPerms) {
                children.put(permissions.toString(), true);
            }
            Bukkit.getServer().getPluginManager().addPermission(new Permission(this.toString(), this.getComment(), this.def, children));
            return;

        }
        Bukkit.getServer().getPluginManager().addPermission(
                new org.bukkit.permissions.Permission(this.toString(), this.getComment(), this.def));
    }*/


    /**
     * will register all permission nodes with spout
     */

    //todo figure out if this is still necessary

    /* public static final void registerAll() {
        for (PERMISSIONS perm : values()) {
            perm.register();
        }
    }*/


    /**
     * Dump the permissions to a file for easy reference
     * todo figure out if this needs changes and needs requireAll implemented
     */

    public static void dumpPermissions(Plugin main) {

        Logger.debug("Dumping permissions to file");
        PrintWriter stream = null;
        try {

            File folder = main.getDataFolder();
            if (folder != null) {
                if (!folder.mkdirs()){
                    Logger.warning("There was an issue during creation of the plugin directory");
                }
            }
            stream = new PrintWriter(main.getDataFolder() + System.getProperty("file.separator") + "permission.yml");
            stream.println("# Permissions for the " + main.getDescription().getName() + " Plugin.");
            stream.println("#");
            stream.println("permissions:");
            stream.println();
            for (PERMISSIONS permissions : values()) {
                stream.println("    " + permissions.toString() + ":");
                stream.println("        description: " + permissions.getComment());
                // let's see if we have a normal permission or a star permission
                if (permissions.starPerms.length > 0) {
                    // we have a star permission
                    stream.println("        children:");
                    for (PERMISSIONS children : permissions.starPerms) {
                        // todo check if :true is still needed
                        stream.println("            " + children + ": true");
                    }

                }
                stream.println();
            }
            stream.println();
            stream.close();
        } catch (FileNotFoundException e) {
            Logger.warning("Error saving the permissions.yml file");
        }
        Logger.debug("Dumping permissions to file = done!");
    }
}

