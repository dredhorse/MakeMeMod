package team.cascade.spout.makememod.helper;

//~--- non-JDK imports --------------------------------------------------------

import org.spout.api.Spout;
import org.spout.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.logging.*;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Logger is a HelperClass which allows you to use the full logging levels instead of just INFO, WARNING and SEVERE.
 * It is using a workaround to accomplish this by logging the not existing levels via a custom tag and the INFO level.
 * It also adds a DEBUG log function which will even printout the module.method and line which called that function if
 * logging is done via FINER and higher.
 * <br>
 * It allows you to log to a file INSTEAD of logging to a console or to log to both. If you enable Logging to FILE
 * everything starting from CONFIG onwards will be logged to file only, including DEBUG.
 * <br>
 * On Top of that it supplies a enableMessage and disableMessage method which will log via INFO level a message.
 *
 * @author $Author: dredhorse$
 * @version $FullVersion$
 */
public class Logger {
    /**
     * DateFormat
     */
    private static String dateFormat = "yyyy-MM-dd HH:mm:ss";

    /**
     * Line marker
     */
    private static final String LINE = "---------------------------------------";

    /**
     * File Separator
     */
    private static final String FILE_SEPARATOR = "file.separator";

    /**
     * The version of the plugin using this logger
     */
    private static String pluginVersion = "";

    /**
     * Enable Message Formatting
     */
    private static boolean enableMessageFormatting = false;

    /**
     * Switch being used to store enableMessageFormatting for file logging
     * as we don't need it there
     */
    private static boolean wasMessageFormattingEnabled = true;

    /**
     * Log to LogFile only
     */
    private static boolean logToConsoleAndFile = true;

    /**
     * Configures Fancy Logging of Exceptions
     */
    private static boolean fancyExceptionLog = true;

    /**
     * Enable Debug Logging
     */
    private static boolean debugLogEnabled;

    /**
     * Enable logging to file
     */
    private static boolean enableFileLog;

    /**
     * FileHandler for Logging
     */
    private static FileHandler fileHandler;

    /**
     * Instance of Logger
     */
    private static Logger instance;

    /**
     * The Logger
     */
    private static java.util.logging.Logger log;

    /**
     * LogDirectoy
     */
    private static String logDirectory;

    /**
     * LogFileName
     */
    private static String logFileName;

    /**
     * The directory of the plugin configuration and logs
     */
    private static String pluginDirectory;

    /**
     * The name of the plugin using this logger
     */
    private static String pluginName;

    //~--- fields -------------------------------------------------------------

    /**
     * Default Log Level = Level.CONFIG
     */
    private Level defaultLevel = Level.CONFIG;

    /**
     * ConsoleHandler for Logging
     */
    private ConsoleHandler consoleHandler;

    //~--- constructors -------------------------------------------------------

    /**
     * Singleton Constructor which creates a logger and sets the pluginName and pluginDirectory Defaults
     * This is the preferred method to use this class!
     *
     * @param plugin to log for
     */
    public Logger(Plugin plugin) {
        log = plugin.getLogger();
        log.setLevel(defaultLevel);
        pluginName = plugin.getDescription().getName();
        pluginDirectory = "plugins" + System.getProperty(FILE_SEPARATOR) + pluginName
                + System.getProperty(FILE_SEPARATOR);
        logFileName = pluginName + ".log";
        logDirectory = "logs" + System.getProperty(FILE_SEPARATOR);
        /**
         *  Setting debug Log and Loging to File and Console to the value of the spout debugMode.
         *  Make sure that you change those values to the right values when the config is read!
         *  @todo change debugLogEnabled and logToConsoleAndFile to correct values from config
         */

        debugLogEnabled = Spout.debugMode();
        logToConsoleAndFile = Spout.debugMode();
        configureFileLog(Spout.debugMode());
    }

    //~--- methods ------------------------------------------------------------

//  Log Level Methods

    /**
     * logs message with INFO level to console or file
     *
     * @param msg
     */
    public static void info(String msg) {
        log.info(formatMessage(msg));
    }

    /**
     * logs messages with SEVERE level to console or file
     *
     * @param msg
     */
    public static void severe(String msg) {
        log.severe(formatMessage(msg));
    }

    /**
     * logs messages and exceptions with SEVERE level to console or file
     *
     * @param msg
     * @param ex
     */
    public static void severe(String msg, Throwable ex) {
        if (fancyExceptionLog) {
            log.severe(formatMessage(LINE));

            if (!"".equals(msg)) {
                log.severe("# " + msg);

            }

            log.severe(ex.toString());
            log.severe("#");
            for (final StackTraceElement stack : ex.getStackTrace()) {
                log.severe("\t" + stack.toString());
            }

            log.severe(formatMessage(LINE));
        } else {
            log(Level.SEVERE, formatMessage(msg), ex);
        }
    }

    /**
     * logs messages with WARNING level to console or file
     *
     * @param msg
     */
    public static void warning(String msg) {
        log.warning(formatMessage(msg));
    }

    /**
     * logs messages and exceptions with WARNING level to console or file
     *
     * @param msg
     * @param ex
     */
    public static void warning(String msg, Throwable ex) {
        if (fancyExceptionLog) {
            log.warning(formatMessage(LINE));

            if (!"".equals(msg)) {
                log.warning("# " + msg);

            }

            log.warning(ex.toString());
            log.warning("#");
            for (final StackTraceElement stack : ex.getStackTrace()) {
                log.warning("\t" + stack.toString());
            }

            log.warning(formatMessage(LINE));
        } else {
            log(Level.WARNING, formatMessage(msg), ex);
        }
    }

    /**
     * logs messages with CONFIG level to console or file
     *
     * @param msg
     */
    public static void config(String msg) {
        if (log.isLoggable(Level.CONFIG) && !enableFileLog) {
            log.info(formatMessage("[CONFIG] " + msg));
        } else {
            log.config(msg);
        }
    }

    /**
     * logs messages with FINE level to console or file
     *
     * @param msg
     */
    public static void fine(String msg) {
        if (log.isLoggable(Level.FINE) && !enableFileLog) {
            log.info(formatMessage("[FINE] " + msg));
        } else {
            log.fine(msg);
        }
    }

    /**
     * logs messages with FINER level to console or file
     *
     * @param msg
     */
    public static void finer(String msg) {
        if (log.isLoggable(Level.FINER) && !enableFileLog) {
            log.info(formatMessage("[FINER] " + msg));
        } else {
            log.finer(msg);
        }
    }

    /**
     * logs messages with FINEST level to console or file
     *
     * @param msg
     */
    public static void finest(String msg) {
        if (log.isLoggable(Level.FINEST) && !enableFileLog) {
            log.info(formatMessage("[FINEST] " + msg));
        } else {
            log.finest(msg);
        }
    }

    /**
     * will output with specified level to console or file
     *
     * @param level     LoggerLevel = FINEST, FINER, FINE, CONFIG, INFO, WARNING, SEVERE
     * @param msg       message to output
     * @param exception exception to output
     */
    public static void log(Level level, String msg, Throwable exception) {
        log.log(level, formatMessage(msg), exception);
    }

//  Log Level Configuration Methods

    /**
     * Debug Logging contains information depending on FINE, FINER, FINEST
     *
     * @param msg Mesage to display, if Level is FINE or higher will also log class, method, line of call
     */
    public static void debug(String msg) {
        debug(msg, null);
    }

    /**
     * Debug Logging contains information depending on FINE, FINER, FINEST
     *
     * @param msg Mesage to display, if Level is FINE or higher will also log class, method, line of call
     * @param o   object to add to the message in []
     */
    public static void debug(String msg, Object o) {
        if (!debugLogEnabled) {
            return;
        }

        if (log.getLevel().intValue() > Level.INFO.intValue()) {

            // we do debug logging via INFO so return if not possible
            return;
        }

        // if log level is FINE or higher print line of code
        if (log.getLevel().intValue() <= Level.FINE.intValue()) {
            int index;

            if (o != null) {
                index = 1;
            } else {
                index = 2;
            }

            StackTraceElement[] ste = new RuntimeException().getStackTrace();
            String className = ste[index].getClassName().substring(ste[index].getClassName().lastIndexOf(".") + 1);
            String methodName = ste[index].getMethodName();
            int lineNumber = ste[index].getLineNumber();

            msg = className + "." + methodName + " line: " + lineNumber + " = " + msg;
        }

        if (o != null) {
            msg = msg + " [" + o.toString() + "]";
        }

        if (enableFileLog && !logToConsoleAndFile) {
            log.log(LogUtilityLevel.DEBUG, " " + msg);
        } else {
            info("[DEBUG] " + msg);
        }
    }

    /**
     * Decreases the LogLevel
     * FINEST (lowest value)
     * FINER
     * FINE
     * CONFIG
     * INFO
     * WARNING
     * SEVERE (highest value)
     * and returns it.
     *
     * @return newLevel Level of logging,  Level.WARNING as lowest
     */
    public static Level decreaseLogLevel() {
        int oldLevelInt = log.getLevel().intValue();

        if (oldLevelInt == Level.WARNING.intValue()) {

            // Nothing to do
            return Level.WARNING;
        }

        int newLevelInt = oldLevelInt;

        if (oldLevelInt == Level.ALL.intValue()) {
            newLevelInt = Level.FINEST.intValue();
        } else {
            newLevelInt = oldLevelInt + 100;
        }

        Level newLevel = Level.parse(Integer.toString(newLevelInt));

        log.setLevel(newLevel);

        return newLevel;
    }

    /**
     * Increases the LogLevel
     * SEVERE (highest value)
     * WARNING
     * INFO
     * CONFIG
     * FINE
     * FINER
     * FINEST (lowest value)
     * and returns it.
     *
     * @return newLevel Level of logging,  Level.ALL as Highest
     */
    public static Level increaseLogLevel() {
        int oldLevelInt = log.getLevel().intValue();

        if (oldLevelInt == Level.ALL.intValue()) {

            // Nothing to do
            return Level.ALL;
        }

        int newLevelInt = oldLevelInt;

        if (oldLevelInt == Level.FINEST.intValue()) {
            newLevelInt = Level.ALL.intValue();
        } else {
            newLevelInt = oldLevelInt - 100;
        }

        Level newLevel = Level.parse(Integer.toString(newLevelInt));

        log.setLevel(newLevel);

        return newLevel;
    }

    /**
     * Returns the Level of logging
     *
     * @return Level   = FINEST, FINER, FINE, CONFIG, INFO, WARNING, SEVERE
     */
    public static Level returnLogLevel() {
        return log.getLevel();
    }

    /**
     * formats the message by adding the [PluginName] in front.
     *
     * @param message to format, e.g. this is a test
     * @return formated message, e.g. [PluginName] this is a test
     */
    private static String formatMessage(String message) {
        // leaving this in as perhaps implemented again in the future
       /* if (enableMessageFormatting) {
            return "[" + pluginName + "] " + message;
        }*/
        
        return message;
    }

    /**
     * Enables logging to file and disables console logging if configured via {@see #logToConsoleAndFile}
     *
     * @param enableFileLog true / false
     */
    private static void configureFileLog(boolean enableFileLog) {
        Logger.enableFileLog = enableFileLog;

        if (enableFileLog) {
            try {
                File folder = new File(logDirectory);

                if (folder != null) {
                    folder.mkdirs();
                }

                debug("Enabling File logging");
                debug("File will be: " + logFileName);
                fileHandler = new FileHandler(logDirectory + logFileName, true);
                log.setUseParentHandlers(logToConsoleAndFile);

                for (Handler handler : log.getHandlers()) {
                    log.removeHandler(handler);
                }

                log.addHandler(fileHandler);
                fileHandler.setLevel(Level.ALL);
                fileHandler.setFormatter(new LogFormatter());
                wasMessageFormattingEnabled = enableMessageFormatting;

                if (!logToConsoleAndFile) {
                    enableMessageFormatting = false;
                }
            } catch (SecurityException e) {
                severe("We have some issues switching to file logging", e);
                enableFileLog = false;
                configureFileLog(enableFileLog);
            } catch (IOException e) {
                severe("We have access issues to the file", e);
                enableFileLog = false;
                configureFileLog(enableFileLog);
            }
        } else {
            debug("Disabling File logging");
            log.setUseParentHandlers(true);

            for (Handler handler : log.getHandlers()) {
                log.removeHandler(handler);
            }
            if (fileHandler != null) {
                fileHandler.close();
            }

            enableMessageFormatting = wasMessageFormattingEnabled;
            debug("Console logging enabled");
        }
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Set the level of logging directly:
     * SEVERE (highest value)
     * WARNING
     * INFO
     * CONFIG
     * FINE
     * FINER
     * FINEST (lowest value)
     *
     * @param level = FINEST, FINER, FINE, CONFIG, INFO, WARNING, SEVERE
     */
    public static void setLevel(Level level) {
        log.setLevel(level);
    }

    //~--- methods ------------------------------------------------------------

    /**
     * will output [PluginName] v "VersionNumber" enabled
     * or [PluginName] enabled
     */
    public static void enableMsg() {
        if (pluginVersion.length() == 0) {
            info("enabled");
        } else {
            info("v " + pluginVersion + " enabled");
        }
    }

    /**
     * will output [PluginName] v "VersionNumber" disabled
     * or [PluginName] disabled
     */
    public static void disableMsg() {
        if (pluginVersion.length() == 0) {
            info("disabled");
        } else {
            info("v " + pluginVersion + " disabled");
        }
    }

//  Getters & Setters

    /**
     * Enable logging with level DEBUG to console or file
     */
    public static void enableDebug() {
        debugLogEnabled = true;
    }

    /**
     * Disable logging with level DEBUG to console or file
     */
    public static void disableDebug() {
        debugLogEnabled = false;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Check if we are logging to Console AND File at the same time
     *
     * @return logToConsoleAndFile
     */
    public static boolean isLogToConsoleAndFile() {
        return logToConsoleAndFile;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Set if we are logging to Console and File at the same time
     *
     * @param logToConsoleAndFile true / false
     */
    public static void setLogToConsoleAndFile(boolean logToConsoleAndFile) {
        Logger.logToConsoleAndFile = logToConsoleAndFile;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Enable Logging to Console and File at the same time
     */
    public static void enableLogToConsoleAndFile() {
        logToConsoleAndFile = true;
    }

    /**
     * Disable Logging to Console and File at the same time
     */
    public static void disableLogToConsoleAndFile() {
        logToConsoleAndFile = false;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Returns the pluginName being used by the logger
     *
     * @return pluginName
     */
    public static String getPluginName() {
        return pluginName;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Sets the pluginName being used by the logger to display messages. Use this if your class name isn't the name you
     * want to be used during logging when you did use {@see #getLogger()}
     *
     * @param pluginName pluginName to be used to display messages
     */
    public static void setPluginName(String pluginName) {
        Logger.pluginName = pluginName;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Returns the pluginVersion which is being used by the logger.
     *
     * @return pluginVersion
     */
    public static String getPluginVersion() {
        return pluginVersion;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Sets the pluginVersion being used by the logger during enableMessage and disableMessage. Use this if you did use {@see #getLogger()}
     *
     * @param pluginVersion pluginVersion to be used for enableMessage and disableMessage
     */
    public static void setPluginVersion(String pluginVersion) {
        Logger.pluginVersion = pluginVersion;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Returns the pluginDirectory
     *
     * @return pluginDirectory
     */
    public static String getPluginDirectory() {
        return pluginDirectory;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Sets the pluginDirectory being used by the logger to save the log file to. Use this if your class name isn't the correct name you
     * need to be used during logging when you did use {@see #getLogger()}
     *
     * @param pluginDirectory directory being used to write the log files
     */
    public static void setPluginDirectory(String pluginDirectory) {
        Logger.pluginDirectory = pluginDirectory;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Returns if DEBUG logging is enabled
     *
     * @return debugLogEnabled
     */
    public static boolean isDebugLogEnabled() {
        return debugLogEnabled;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Configures logging with DEBUG level.
     *
     * @param debugLogEnabled true / false
     */
    public static void setDebugLogEnabled(boolean debugLogEnabled) {
        Logger.debugLogEnabled = debugLogEnabled;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Returns if File logging is enabled
     *
     * @return enableFileLog
     */
    public static boolean isEnableFileLog() {
        return enableFileLog;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Enables or disables the logging to file
     *
     * @param enableFileLog
     */
    public static void setEnableFileLog(boolean enableFileLog) {
        Logger.enableFileLog = enableFileLog;
        configureFileLog(enableFileLog);
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Returns the logFileName
     *
     * @return logFileName
     */
    public static String getLogFileName() {
        return logFileName;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Sets the logFileName being used by the logger to log messages to file. By default this is pluginName.log.
     * Use this if your logFileName isn't the filename you want to be used during logging when you did use {@see #getLogger()}
     * or if you want to name the file differently
     *
     * @param logFileName
     */
    public static void setLogFileName(String logFileName) {
        Logger.logFileName = logFileName;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Enables fancy Exception logging
     */
    public static void enableFancyExceptionLog() {
        fancyExceptionLog = true;
    }

    /**
     * Disable fancy Exception logging
     */
    public static void disableFancyExceptionLog() {
        fancyExceptionLog = false;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Status of FancyExceptionLogging
     *
     * @return fancyExceptionLog
     */
    public static boolean isFancyExceptionLog() {
        return fancyExceptionLog;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Set fancyExceptionLog
     *
     * @param enable true / false of fancyExceptionLog
     */
    public static void setFancyExceptionLog(boolean enable) {
        fancyExceptionLog = enable;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Returns the log directoy
     *
     * @return String LogDirectory
     */
    public static String getLogDirectory() {
        return logDirectory;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Sets the logDirectory being used by the logger to save the log file to. Use this if your class name isn't the correct name you
     * need to be used during logging when you did use {@see #getLogger()}
     *
     * @param logDirectory
     */
    public static void setLogDirectory(String logDirectory) {
        Logger.logDirectory = logDirectory;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Returns if we enabled Message formatting in the style of [PluginName] msg
     * Note: there is no message formatting being done in the log file.
     *
     * @return enableMessageFormatting
     */
    public static boolean isEnableMessageFormatting() {
        return enableMessageFormatting;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Enables / Disables message formatting
     * Note: there is no message formatting being done in the log file.
     *
     * @param enableMessageFormatting
     */
    public static void setEnableMessageFormatting(boolean enableMessageFormatting) {
        Logger.enableMessageFormatting = enableMessageFormatting;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Returns the date format being used for logging to file, default is:
     * "yyyy-MM-dd HH:mm:ss"
     *
     * @return dateFormat
     */
    public static String getDateFormat() {
        return dateFormat;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Sets the date format being used for logging to file
     *
     * @param dateFormat
     */
    public static void setDateFormat(String dateFormat) {
        Logger.dateFormat = dateFormat;
    }

    //~--- get methods --------------------------------------------------------

//  Singleton creation

    /**
     * Singleton Creation which uses the name of the class to create a logger for the plugin. To better customize the
     * logging you should set the correct values if needed
     *
     * @return
     * @see #setPluginName
     * @see #setPluginDirectory
     * @see #setPluginVersion
     * @see #setLogDirectory
     */
    public static Logger getLogger() {
        if (instance == null) {
            StackTraceElement[] ste = new RuntimeException().getStackTrace();
            String className = ste[1].getClassName().substring(ste[1].getClassName().lastIndexOf(".") + 1);
            Plugin plugin = Spout.getEngine().getPluginManager().getPlugin(className);
            instance = new Logger(plugin);
        }

        return instance;
    }

    /**
     * Singleton Creation which uses the Plugin to create a logger for the plugin
     * This is more sophisticated than the getLogger() function but requires passing the PluginObject
     *
     * @param plugin
     * @return
     */
    public static Logger getLogger(Plugin plugin) {
        if (instance == null) {
            instance = new Logger(plugin);
            pluginDirectory = plugin.getDataFolder() + System.getProperty(FILE_SEPARATOR);
            pluginVersion = plugin.getDescription().getVersion();
            logDirectory = "logs" + System.getProperty(FILE_SEPARATOR);
        }

        return instance;
    }

    /**
     * will output with specified level to console or file
     *
     * @param level   LoggerLevel = FINEST, FINER, FINE, CONFIG, INFO, WARNING, SEVERE
     * @param message message to output
     */

    public static void log(Level level, String message) {
        log.log(level, formatMessage(message));
    }

    //~--- inner classes ------------------------------------------------------

    /**
     * LogFormatter Private class.
     */
    private static class LogFormatter extends Formatter {
        /**
         * Dateformat being used for logging to file
         */
        private final SimpleDateFormat date = new SimpleDateFormat(dateFormat);

        //~--- methods --------------------------------------------------------

        /**
         * Formatter to generate the log file messages
         *
         * @param record
         * @return
         */
        @Override
        public String format(LogRecord record) {
            StringBuilder builder = new StringBuilder();
            @SuppressWarnings("ThrowableResultOfMethodCallIgnored") Throwable ex = record.getThrown();

            builder.append(this.date.format(record.getMillis()));
            builder.append(" [");
            builder.append(record.getLevel().getLocalizedName().toUpperCase());
            builder.append("] ");
            builder.append(record.getMessage());
            builder.append('\n');

            if (ex != null) {
                StringWriter writer = new StringWriter();

                ex.printStackTrace(new PrintWriter(writer));
                builder.append(writer);
            }

            return builder.toString();
        }
    }


    /**
     * Own Debug Level Level
     */
    private static class LogUtilityLevel extends Level {
        // Create the new level

        /**
         * Debug Log Level
         */
        public static final Level DEBUG = new LogUtilityLevel("DEBUG", Level.CONFIG.intValue() + 1);

        //~--- constructors ---------------------------------------------------

        /**
         * Creates the DEBUG log level
         *
         * @param name
         * @param value
         */
        public LogUtilityLevel(String name, int value) {
            super(name, value);
        }
    }
}
