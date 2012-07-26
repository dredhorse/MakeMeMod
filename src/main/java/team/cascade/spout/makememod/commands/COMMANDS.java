package team.cascade.spout.makememod.commands;

import org.spout.api.plugin.CommonPlugin;
import team.cascade.spout.makememod.helper.Logger;
import team.cascade.spout.makememod.permissions.PERMISSIONS;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The command information for all the commands you are using, as an example the MEMORY command and the basic admin commands.
 * <br>
 * NOTE: A root command is an ENUM without an _ , a child command starts with the root command and than a _ and than the child command<br>
 * you can only have one _ per command.
 * </p>
 * <br>
 * <br>
 * The command has several parts:
 * <br>
 * <ul>
 * <li> aliases = the aliased command. Note: Spout will take the first free alias and will drop the others. Also you can only enter one alias here.
 * <li> cmdUsage  =  the cmdUsage of the command which is displayed, /<command> will be added by default.
 * <li> cmdDescription  =  the description of the command which is displayed
 * <li> perm = the permission for this command (NOTE: This takes a permission and will not show in the config.
 * <li> minArgs = number of minimum arguments for the command
 * <li> maxArgs = number of maximum arguments for the command
 * </ul>
 * <br>
 * The full description including default command, aliased command and permission can be dumped automatically into a file
 * called commands.yml for easy reference.
 * <br>
 * NOTE: You can't use the command PERMISSIONS with this, but you can use PERMS as command and permissions as alias.
 * <br>
 * NOTE: ALL Commands need to be unique on the server so use only commands which contain your plugin in one way or the other.
 * <br>
 * NOTE: Atm aliases, usage and description can not be changed after the commands are initialized.
 *
 */

// todo figure out how to manage aliases on the fly

public enum COMMANDS {

    // Keep the stuff below if you want to keep the default commands
    MAKEMEMOD("mmm", "reload, save, info or help", "Admin root command for the Plugin", PERMISSIONS.MAKEMEMOD_USER,0,0),
    MAKEMEMOD_INFO("info", "info", "Information about the plugin", PERMISSIONS.MAKEMEMOD_USER,0,0),
    MAKEMEMOD_HELP("help", "help", "Help / Usage information", PERMISSIONS.MAKEMEMOD_USER,0,0),
    MAKEMEMOD_SAVE("save", "save", "Save any changes made to the config", PERMISSIONS.MAKEMEMOD_ADMIN,0,0),
    MAKEMEMOD_RELOAD("reload", "reload", "Reload the configuration", PERMISSIONS.MAKEMEMOD_ADMIN,0,0);


    // NO CHANGES BELOW HERE!!!!!


    private List<String> aliases = new ArrayList<String>();

    private String usage = "";

    private String desc;

    private PERMISSIONS permissions;

    private int minArgs;

    private int maxArgs;


    private COMMANDS(String alias, String usage, String description, PERMISSIONS perm, int minArgs, int maxArgs) {
        aliases.add(alias);
        this.usage = usage;
        this.desc = description;
        this.permissions = perm;
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
    }

    /**
     * Get the instance of the enum
     * @return  instance of enum
     */
    public COMMANDS command() {
        return this;
    }

    /**
     * Get the usage of the command
     * @return string with command usage
     */
    public String usage() {
        return getUsage();
    }

    /**
     * Get the description of the command
     * @return string with the command description
     */
    public String desc() {
        return getCmdDescription();
    }

    /**
     * Get the aliases of a command
     * @return String[] with the aliases of a command
     */
    public String[] aliases() {
        return getAliases();
    }

    /**
     * Get the description of the command
     * @return string with the command description
     */
    public String getCmdDescription() {
        Logger.debug("Description for Command: " + toString(), desc);
        return desc;
    }

    /**
     * Get the permissions of a command
     * @return PERMISSIONS of a command
     */
    public PERMISSIONS getPermissions() {
        Logger.debug("Permission for Command: " + toString(), permissions);
        return permissions;
    }

    /**
     * Get the permissions of a command directly as a string
     * @return string representing the permission of the command
     */
    public String getPermString() {
        Logger.debug("PermissionString for Command: " + toString(), permissions.asPermission());
        return permissions.asPermission();
    }

    /**
     * Get the aliases of a command
     * @return String[] with the aliases of a command
     */
    public String[] getAliases() {
        String[] simpleArray = new String[aliases.size()];
        Logger.debug("Aliases for Command: " + toString(), aliases.toString());
        return aliases.toArray(simpleArray);
    }

    /**
     * Get the usage of the command
     * @return string with command usage
     */
    public String getUsage() {
        Logger.debug("Usage for Command: " + toString(), usage);
        return usage;
    }

    /**
     * Set the usage of the command <br>
     * Note: You can not change the usage of a command without a server reload
     * @param usage of the command
     */
    public void setUsage(String usage) {
        this.usage = usage;
    }

    /**
     * Set the aliases of a command<br>
     * Note: You can not change the aliases of a command without a server reload
     * </p>
     * @param aliases of the command
     */
    public void setAliases(String[] aliases) {
        this.aliases = new ArrayList<String>(Arrays.asList(aliases));
    }

    /**
     * Add an alias to the alias list of an command.<br>
     * Note: You can not change the aliases of a command without a server reload. Also only the first free alias is taken.
     * @param alias  of the command
     */
    public void addAlias(String alias) {
        aliases.add(alias);
    }

    /**
     * Remove an alias from an command.<br>
     * Note: You can not change the aliases of a command without a server reload.<br>
     * Note: An Empty Alias will probably cause a NPE during command registration.
     * @param alias to remove
     */
    public void removeAlias(String alias) {
        aliases.remove(alias);
    }

    /**
     * Sets the description of a command.
     * @param description of a command
     */
    public void setDescription(String description) {
        this.desc = description;
    }

    /**
     * Get the description of a command
     * @return description of a command
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Get the minimum arguments of a command
     * @return minArgs of the command
     */
    public int getMinArgs() {
        return minArgs;
    }

    /**
     * Get the maximum arguments of a command
     * @return maxArgs of the command
     */
    public int getMaxArgs() {
        return maxArgs;
    }


    /**
     * Turns commands into root and child commands, from MAKEMEMOD_HELP in MAKEMEMOD.HELP
     *
     * @return string with commands in node from
     */
    @Override
    public String toString() {
        return toCommands(super.toString());
    }

    /**
     * Turns commands into root and child commands, from MAKEMEMOD_HELP in MAKEMEMOD.HELP
     *
     * @param command
     * @return  string with commands in node from
     */

    private static String toCommands(String command) {
        final String[] commands = command.split("_");
        command = "";
        for (String part : commands) {
            command = command + "." + toProperCase(part);
        }
        Logger.debug("Command: ", command.substring(1));
        return command.substring(1);
    }

    /**
     * Turns MEMORY into Memory (aka ProperCase)
     *
     * @param s string to turn to ProperCase
     * @return s string in properCase
     */

    static String toProperCase(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    /**
     * Return the root command
     *
     * @return root command
     */
    public String getRootCommand() {
        String command = super.toString();
        if (command.contains("_")) {
            String[] commands = command.split("_");
            command = commands[0];
        }
        Logger.debug("Root Command for Command: " + toString(), command.toLowerCase());
        return command.toLowerCase();
    }

    /**
     * Return the child command of the root command, may return null
     *
     * @return child command of the root command or null
     */
    public String getChildCommand() {
        String command = super.toString();
        if (command.contains("_")) {
            String[] commands = command.split("_");
            Logger.debug("Child Command for Command: " + toString(), commands[1].toLowerCase());
            return commands[1].toLowerCase();
        }
        Logger.debug("There is no Child Command for Command: " + toString());
        return null;
    }

    /**
     * Returns either the the command or the child command.
     *
     * @return command, child command if the command is a child
     */

    public String getCommand() {
        String command;
        if (isChildCommand()) {
            command = getChildCommand();
        } else {
            command = getRootCommand();
        }
        return command;
    }


    /**
     * Check if the command has child commands
     */
    public boolean isChildCommand() {
        if (super.toString().contains("_")) {
            Logger.debug("Command " + toString() + " is a child command");
            return true;
        } else {
            Logger.debug("Command " + toString() + " is not a child command");
            return false;
        }
    }

    /**
     * Dump the commands to a file for easy reference, for example to put the permissions into your plugin.yml
     * todo check if this is still valid and which changes are necessary
     */

    public static final void dumpCommands(CommonPlugin main) {

        Logger.debug("Dumping commands to file");
        PrintWriter stream = null;
        try {

            File folder = main.getDataFolder();
            if (folder != null) {
                if (!folder.mkdirs()){
                    Logger.warning("There was an issue during creation of the plugin directory");
                }
            }
            stream = new PrintWriter(main.getDataFolder() + System.getProperty("file.separator") + "commands.yml");
            stream.println("# Commands for the " + main.getDescription().getName() + " Plugin.");
            stream.println("#");
            stream.println("This list is for reference ONLY!");
            stream.println("#");
            stream.println("commands:");
            stream.println();
            for (COMMANDS commands : values()) {
                // <command> will be replaced in game with the correct command

                stream.println("    " + commands.toString() + ":");
                stream.println("        description: " + commands.getCmdDescription());
                stream.println("        usage: |");
                stream.println("            /<command> " + commands.getUsage());
                stream.println("        permission: " + commands.getPermString());
                stream.println();
            }
            stream.println();
            stream.close();
        } catch (FileNotFoundException e) {
            Logger.warning("Error saving the commands.yml file");
        }
        Logger.debug("Dumping commands to file = done!");
    }


}

