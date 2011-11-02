package org.simiancage.bukkit.makememod;

/**
 *
 * PluginName: MakeMeMod
 * Class: LoggerMMM
 * User: DonRedhorse
 * Date: 02.11.11
 * Time: 19:19
 *
 */

import org.bukkit.plugin.Plugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerMMM {

    private final Logger logger;
    private final String pluginName;
    private final String version;
    private final ConfigMMM config = ConfigMMM.getInstance();

    public void debug (String msg, Object object){
        if (config.debugLogEnabled()) info(msg +"= ["+object.toString()+"]");
    }

    public LoggerMMM(String loggerName, String pluginName) {
        this(Logger.getLogger(loggerName), pluginName, "");
    }



    public LoggerMMM(String pluginName) {
        this("Minecraft", pluginName);
    }

    private LoggerMMM(Logger logger, String pluginName, String version) {
        this.logger = logger;
        this.pluginName = pluginName;
        this.version = version;
    }

    public LoggerMMM(Plugin plugin) {
        this(plugin.getServer().getLogger(), plugin.getDescription().getName(), plugin.getDescription().getVersion());
    }

    private String formatMessage(String message) {
        return "[" + pluginName + "]: " + message;
    }

    public void info(String msg) {
        this.logger.info(this.formatMessage(msg));
    }

    public void warning(String msg) {
        this.logger.warning(this.formatMessage(msg));
    }

    public void severe(String msg) {
        this.logger.severe(this.formatMessage(msg));
    }

    public void severe(String msg, Throwable exception) {
        this.log(Level.SEVERE, msg, exception);
    }

    public void log(Level level, String msg, Throwable exception) {
        this.logger.log(level, this.formatMessage(msg), exception);
    }

    public void warning(String msg, Throwable exception) {
        this.log(Level.WARNING, msg, exception);
    }

    public void enableMsg() {
        this.info("[" + this.pluginName + "] v" + this.version + " enabled");
    }

    public void disableMsg() {
        this.info("[" + this.pluginName + "] v" + this.version + " disabled");
    }


}

