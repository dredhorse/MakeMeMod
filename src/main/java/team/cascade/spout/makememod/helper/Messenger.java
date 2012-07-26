package team.cascade.spout.makememod.helper;

//~--- non-JDK imports --------------------------------------------------------

import com.google.common.base.Splitter;
import team.cascade.spout.makememod.messages.MESSAGES;
import team.cascade.spout.makememod.permissions.PERMISSIONS;
import org.spout.api.Spout;
import org.spout.api.chat.ChatArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.player.Player;
import org.spout.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Pattern;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * The following Methods are some helpers for command handling, support guessing of commands, message formatting etc
 * <br>
 * Allows sending of messages in a formatted way and allows replacing of variables which are wrapped in %(variable).
 * <br>
 * The following variables are included by default:
 * <br>
 * %(player), %(realName), %(world), %(loc)
 * <br>
 * The following {@code <br>} will force a line break.
 * <br>
 *
 * @author $Author: dredhorse$
 * @version $FullVersion$
 */
public final class Messenger {

     /**
     * Wrap / Split position
     */
    private static final int WRAP_POS = 60;

    /**
     * Pattern for SPACE_SPLIT
     */
    private static final Pattern SPACE_SPLIT = Pattern.compile(" ");

    /**
     * Pattern for newLineSplit
     */
    private static final Pattern NEWLINE_SPLIT = Pattern.compile("\n");


    private Messenger() {
        // constructor is never used
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Makes a Object toString yellow, with a trailing green colour.
     *
     * @param toString Object which toString output should be wrapped
     * @return toString expression of the Object with wrapped colours
     */
    public static String wrap(Object toString) {
        return wrap(Chat_Styles.YELLOW, toString.toString(), Chat_Styles.BRIGHT_GREEN);
    }

    /**
     * Makes a Object toString with a certain CHAT_STYLES and adds a trailing ChatColour.
     *
     * @param wrap     @CHAT_STYLES which is used to color the toString
     * @param toString Object which toString output should be wrapped
     * @param trail    @CHAT_STYLES which is added at the end of the string
     * @return toString expression of the Object with wrapped colours
     */
    public static String wrap(Chat_Styles wrap, Object toString, Chat_Styles trail) {
        return wrap(wrap, toString.toString(), trail);
    }

    /**
     * Makes a string with a certain CHAT_STYLES and adds a trailing ChatColour.
     *
     *
     *
     * @param wrap   @CHAT_STYLES which is used to color the toString
     * @param string Object which toString output should be wrapped
     * @param trail  @CHAT_STYLES which is added at the end of the string
     * @return string expression of the Object with wrapped colours
     */
    public static String wrap(Chat_Styles wrap, String string, Chat_Styles trail) {
        return wrap + string + trail;
    }

    /**
     * This method will take a message enum, a variable string and the replacement value
     *
     * @param message
     * @param variable
     * @param value
     * @return
     */
    public static String replaceVariable(MESSAGES message, String variable, String value) {
        return message.getMessage().replace(variable, value);
    }

    /**
     * This method will send a red error message
     *
     * @param player
     * @param error
     */
    public static void sendError(Player player, String error) {
        send(player, Chat_Styles.RED + error);
    }

    /**
     * This method will send a red error message
     *
     * @param sender
     * @param error
     */
    public static void sendError(CommandSource sender, String error) {
        send(sender, Chat_Styles.RED + error);
    }

    /**
     * Send a message to the source, if the source is a player %(player), %(realName), %(world) and %(loc) are replaced with the correct values.
     * <br> will split a message
     *
     * @param sender
     * @param messages
     */
    public static void send(final CommandSource sender, String messages) {
        for (String message : Splitter.on("<br>").omitEmptyStrings().split(messages)) {
            message = dictFormat(sender,message);
            message = colourise(message);
            sender.sendMessage(ChatArguments.fromString(message));
        }
    }

    /**
     * Sends a Header to the source
     *
     * @param sender
     * @param header
     */
    public static void sendHeader(final CommandSource sender, String header) {
        header = Chat_Styles.BRIGHT_GREEN + "----------" + Chat_Styles.WHITE + " [" + Chat_Styles.CYAN + header + Chat_Styles.WHITE + "] " + Chat_Styles.BRIGHT_GREEN + "----------";
        send(sender, header);
        send(sender, "");
    }


    /**
     * Method to unicode escape the minecraft colour codes for changing colours in text
     *
     * @param string where & will be replaced by the unicode expression
     * @return string with unicode expression
     * @url www.minecraftwiki.net/wiki/Formatting_Codes
     */
    public static String colourise(String string) {
        return string.replace("&", "\u00A7");
    }

    /**
     * Simple method to replace variables in a string
     * Will catch any Exception and logs them in console.
     * If Exception occurs. message is returned without replacement.
     *
     * @param message     to translate, for example "Hello %player"
     * @param variable    to replace, for example %player
     * @param replacement which replaces variable, for example dredhorse
     * @return changedMessage     messages with replaced values, for example "Hello dredhorse"
     */
    public static String replaceVariables(String message, String variable, String replacement) {
        String changedMessage = message;
        try {
            changedMessage = message.replace(variable, replacement);
        } catch (Exception ex) {
            Logger.severe("Error while replacing in String: " + message, ex);
        }

        return changedMessage;
    }

    /**
     * Adjust string length to a certain length or via <br>
     *
     * @param string to adjust to length and or <br>
     * @param length length to adjust the string to, 40 if length <= 0
     * @return splitted strings
     */
    public static List<String> stringSplit(String string, int length) {
        List<String> result = new ArrayList<String>();
        if (length <= 0) {
            Logger.debug("Length in stringSplit < 0, using default of " + WRAP_POS);
            length = WRAP_POS;
        }

        String[] lines;

        lines = string.contains("<br>")
                ? NEWLINE_SPLIT.split(string)
                : SPACE_SPLIT.split(string);

        for (String s : lines) {
            while (s.length() > length) {
                result.add(s.substring(0, length));
                s = s.substring((length));
            }
            result.add(s);
        }

        return result;
    }

    /**
     * Split string via <br> and a length of WRAP_POS
     *
     * @param string to split <br>
     * @return splitted strings
     */
    public static List<String> stringSplit(String string) {
        return stringSplit(string, WRAP_POS);
    }

    /**
     * Wrap text at around position WRAP_POS with <br> when there is a space
     *
     * @param string
     * @return
     */
    public static String stringWrap(String string) {
        return stringWrap(string, WRAP_POS);
    }

    /**
     * Wrap text at around a position with <br> when there is a space
     *
     * @param string to wrap at pos
     * @param pos    position to wrap string approximately at
     * @return
     */
    public static String stringWrap(String string, int pos) {
        StringBuilder sb = new StringBuilder(string);
        int i = 0;

        while ((i + pos < sb.length()) && (i = sb.lastIndexOf(" ", i + pos)) != -1) {
            sb.replace(i, i + 1, "<br>");
        }

        return sb.toString();
    }


    /**
     * Taken from http://stackoverflow.com/a/2295004
     * <br>
     * Will replace the player information either with player information or
     * if the CommandSource is console some default replacements will be done
     *
     * @param format string to format
     * @param source of the message
     * @return string with replaced color
     */


    public static String dictFormat(CommandSource source, String format) {
        if (source instanceof Player) {
            format = Messenger.dictFormat(format, getPlayerDict((Player) source));
        } else {
            Hashtable defaultDict = new Hashtable();
            defaultDict.put("%(player)", "CONSOLE");
            defaultDict.put("%(realName)", "CONSOLE");
            defaultDict.put("%(world)", "An Other Dimension");
            defaultDict.put("%(loc)", "unknown");
            format = Messenger.dictFormat(format, defaultDict);
        }
        return format;
    }



    /**
     * Taken from http://stackoverflow.com/a/2295004
     * <br>
     * Allows you to pass a hashtable with the format String, Object where String represents
     * %(variable) which is than replaced by the toString expression of the object.
     * See also {@link #getPlayerDict}
     *
     * @param format string to format
     * @param values %(variable) and replacement values in hashtable
     * @return string with replaced text
     */


    public static String dictFormat(String format, Hashtable<String, Object> values) {
        StringBuilder convFormat = new StringBuilder(format);
        Enumeration<String> keys = values.keys();
        ArrayList valueList = new ArrayList();
        int currentPos = 1;

        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            String formatKey;
            if (key.startsWith("%(")) {
                formatKey = key;
            } else {
                formatKey = "%(" + key + ")";
            }
            String formatPos = "%" + Integer.toString(currentPos) + "$s";
            int index = -1;

            while ((index = convFormat.indexOf(formatKey, index)) != -1) {
                convFormat.replace(index, index + formatKey.length(), formatPos);
                index += formatPos.length();
            }

            valueList.add(values.get(key));
            ++currentPos;
        }

        return String.format(convFormat.toString(), valueList.toArray());
    }

    /**
     * Creates a hashtable with holds the default information values normally used when replacing text
     * concerning a player. {@link #dictFormat(String, java.util.Hashtable)}
     *
     * @param player to retrieve information from
     * @return replacement values for %(player), %(realName), %(world), %(loc)
     */

    public static Hashtable<String, Object> getPlayerDict(Player player) {
        Hashtable defaultDict = new Hashtable();
        defaultDict.put("%(player)", player.getDisplayName());
        defaultDict.put("%(realName)", player.getName());
        defaultDict.put("%(world)", player.getEntity().getWorld().getName());
        int x = player.getEntity().getPosition().getBlockX();
        int y = player.getEntity().getPosition().getBlockY();
        int z = player.getEntity().getPosition().getBlockZ();
        defaultDict.put("%(loc)", "x=" + x + ", y=" + y + ", z=" + z);
        return defaultDict;
    }

     /**
     * Creates a bullet ( - ) list with the first part in the color provided
     */

    public static String makeBulletPoint(String string, Chat_Styles wrap, Chat_Styles trail) {
        return trail + "- " + wrap(wrap, string, trail);
    }

    /**
     * Creates a bullet ( - ) list with the first part in the color provided
     *
     * @see #wrap(Object)
     */

    public static String makeBulletPoint(String string) {
        return "- " + wrap(string);
    }

    /**
     * Send an red error Message and log to console too
     *
     * @param source  Target to send the message too
     * @param level   Loglevel to use for logging
     * @param message Message to send and log
     */

    public static void sendError(CommandSource source, Level level, String message) {
        sendError(source, message);
        Logger.log(level, message);
    }


    /**
     * Send an red error Message and log to console too
     *
     * @param player  Target to send the message too
     * @param level   Loglevel to use for logging
     * @param message Message to send and log
     */

    public static void sendError(Player player, Level level, String message) {
        sendError(player, message);
        Logger.log(level, message);
    }

    /**
     * Send an red error Message to people with a certain permission and log to console too
     *
     * @param permission Target to send the message too
     * @param level      Loglevel to use for logging
     * @param message    Message to send and log
     */

    public static void sendError(PERMISSIONS permission, Level level, String message) {
        Spout.getEngine().broadcastMessage(message, permission.asPermission());
        Logger.log(level, message);
    }

    /**
     * Parse the Authors Array into a readable String with ',' and 'and'.
     * taken from MultiVerse-core https://github.com/Multiverse/Multiverse-Core
     *
     * @return
     */

    public static String getAuthors(Plugin plugin) {
        String authors = "";
        List<String> auths = plugin.getDescription().getAuthors();
        if (auths.size() == 0) {
            return "";
        }

        if (auths.size() == 1) {
            return auths.get(0);
        }

        for (int i = 0; i < auths.size(); i++) {
            if (i == auths.size() - 1) {
                authors += " and " + auths.get(i);
            } else {
                authors += ", " + auths.get(i);
            }
        }
        return authors.substring(2);
    }

    /**
     * Allows sending a broadcast message which is fully supported with the {@link #send(org.spout.api.command.CommandSource, String)}
     * method.
     *
     * @param message
     */
    public static void broadcastMessage(String message){
        for (Player player : Spout.getEngine().getOnlinePlayers()){
            send(player,message);
        }
    }


}
