package team.cascade.spout.makememod.config;

//~--- JDK imports ------------------------------------------------------------

//~--- enums ------------------------------------------------------------------


import team.cascade.spout.makememod.exceptions.WrongClassException;
import org.spout.api.inventory.ItemStack;

import java.util.*;
import java.util.regex.Pattern;

/**
 * The config Enum contains the full configuration of the plugin. The Enum will be parsed in order and written to file.
 * <br>
 * You can use \n as a line break but in this case you need to supply the # by yourself, see the example for CONFIG_AUTO_UPDATE
 * <br>
 * If the Enum doesn't contain an Object it will be a comment at that part of the config file.
 * <br>
 * You may want to keep the options DEBUG_LOG_ENABLED, DEBUG_LOG_TO_FILE, CHECK_FOR_UPDATE, CONFIG_AUTO_UPDATE, CONFIG_AUTO_SAVE, CONFIG_LOG_ENABLED
 * and use DEBUG_LOG_ENABLED, DEBUG_LOG_TO_FILE and CONFIG_LOG_ENABLED in your classes for better logging.
 * <br>
 * If you don't want to use those enums you will need to edit  {@link team.cascade.spout.makememod.helper.config.CommentConfiguration#customInit()}  and remove the line which correspond to those enums!
 * <br>
 * If you directly change the enum values there is no flagging or automatic saving during running the server.
 * If you change the enums via {@link team.cascade.spout.makememod.helper.config.CommentConfiguration#set} the enum is updated, configuration is flagged dirty and
 * if CONFIG_AUTO_SAVE is enabled will force the config to file directly.
 *
 * @todo You need to add all your configuration parameters here.
 *
 * @author $Author: dredhorse$
 * @version $FullVersion$
 */

public enum CONFIG {
    // you should really keep the following lines ==>>>
    CONFIG_VERSION("Version of the configuration","1.0"),
    DEBUG_LOG_ENABLED("Enable more logging.. could be messy!", false),
    DEBUG_LOG_TO_FILE("When debug logging, log into separate file", true),
    CHECK_FOR_UPDATE("Do you want to check if there is a new version available?", true),
    CONFIG_AUTO_UPDATE("Should the configuration automatically be updated \n#   when there is a new version available?", true),
    CONFIG_AUTO_SAVE("Should we automatically save any changes issued by commands to disc?", true),
    CONFIG_LOG_ENABLED("Enable logging of the config.. this could be lot's of info", true),
    CONFIG_END("End of Default Configuration"),
    PLUGIN_START("Begin of Plugin Configuration\n");
    // <<<====== from here on onwards it is all yours don't forget tor replace the ; on top with a ,



    // NO CHANGES BELOW HERE!!!!!


    /**
     * Used for replacing _
     */

    private static final Pattern COMPILE = Pattern.compile("_");

    //~--- fields -------------------------------------------------------------


    /**
     * The configuration Option
     */

    private Object configOption = null;


    /**
     * The comment for the configuration file for the configuration option
     */

    private String configComment;

    //~--- constructors -------------------------------------------------------


    /**
     * Creating the Comment ENUM
     *
     * @param configComment the comment for the configuration file
     */


    private CONFIG(String configComment) {
        this.configOption = null;
        this.configComment = configComment;
    }


    /**
     * Creating the ENUM with the correct information
     *
     * @param configOption  the configuration option
     * @param configComment the comment for the configuration file
     */

    private CONFIG(String configComment, Object configOption) {
        this.configOption = configOption;
        this.configComment = configComment;
    }

    //~--- methods ------------------------------------------------------------


    /**
     * Override for toString, returns the ENUM as CamelCaseString, eg. MEMORY_CMD => MemoryCmd
     *
     * @return s ENUM in CamelCase style
     */

    @Override
    public String toString() {
        return toCamelCase(super.toString());
    }


    /**
     * Removes _ from the Enum and makes it CamelCase
     *
     * @param s ENUM to turn into CamelCase
     * @return camelCaseString
     */

    static String toCamelCase(String s) {
        final String[] parts = COMPILE.split(s);
        String camelCaseString = "";

        for (String part : parts) {
            camelCaseString = camelCaseString + toProperCase(part);
        }

        return camelCaseString;
    }


    /**
     * Turns MEMORY into Memory (aka ProperCase)
     *
     * @param s string to turn to ProperCase
     * @return s string in properCase
     */

    private static String toProperCase(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    //~--- get methods --------------------------------------------------------


    /**
     * Returns the Configuration Comment
     *
     * @return configComment returns the configuration comment
     */
    public String getConfigComment() {
        return configComment;
    }


    /**
     * Returns the configuration option object, may return @null if comment only
     *
     * @return configOption configuration option object
     */

    public Object getConfigOption() {
        return configOption;
    }

    //~--- set methods --------------------------------------------------------


    /**
     * Allows to set the configuration option object.
     * Will check if the class of the object is the same.
     *
     * @param configOption
     */

    public void setConfigOption(Object configOption) throws WrongClassException {
        // ToDo well this doesn't really work with default bukkit methods :-(
        if (configOption.getClass().isInstance(this.configOption)) {
            this.configOption = configOption;
            return;
        }
        throw new WrongClassException(this, configOption);
    }


    /**
     * Workaround for Spout methods DON'T use this in your own classes!!!!!
     * Use setConfigOption(Object configOption) instead!
     *
     * @param configurationOption
     */

    public void setConfigurationOption(Object configurationOption) {
        configOption = configurationOption;
    }

    /**
     * Methods which give back the configOption
     * Note: This can cause exceptions if you don't do it correctly
     */

    public String getString() {
        return (String) configOption;
    }

    public boolean getBoolean() {
        return (Boolean) configOption;
    }

    public List getList() {

        return (List) configOption;
    }

    public Integer getInt() {
        return (Integer) configOption;
    }

    public Double getDouble() {
        return (Double) configOption;
    }

    public Long getLong() {

        return (Long) configOption;
    }

    public ItemStack getItemStack() {

        return (ItemStack) configOption;
    }

    public Vector getVector() {

        return (Vector) configOption;
    }

    public List<Boolean> getBooleanList() {

        return (List<Boolean>) configOption;
    }

    public List<Byte> getByteList() {

        return (List<Byte>) configOption;
    }

    public List<Character> getCharacterList(CONFIG configNode) {

        return (List<Character>) configOption;
    }

/*    public ConfigurationSection getConfigurationSection(CONFIG configNode) {
        Logger.debug(configNode.toString(), configNode.getConfigOption());
        return (ConfigurationSection) configNode.getConfigOption();
    }*/

    public List<Double> getDoubleList() {

        return (List<Double>) configOption;
    }

    public List<Float> getFloatList() {

        return (List<Float>) configOption;
    }

    public List<Integer> getIntegerList() {

        return (List<Integer>) configOption;
    }

    public List<Long> getLongList() {

        return (List<Long>) configOption;
    }

    public List<Map<String, Object>> getMapList() {

        return (List<Map<String, Object>>) configOption;
    }

    public Map<String, Object> getMap() {

        return (Map) configOption;
    }

    public List<Short> getShortList() {

        return (List<Short>) configOption;
    }

    public List<String> getStringList() {

        return (List<String>) configOption;
    }


}

