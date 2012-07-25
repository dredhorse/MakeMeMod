package team.cascade.spout.makememod.exceptions;


import team.cascade.spout.makememod.helper.Logger;
import org.spout.api.exception.SpoutException;

/**
 * Exception which is thrown by the CommentConfiguration when the configuration is not available.
 * NOTE: THIS IS SEVERE AND YOU SHOULD DISABLE YOUR PLUGIN IN THIS CASE!
 *
 * @author $Author: dredhorse$
 * @version $FullVersion$
 */


@SuppressWarnings("ALL")
public class ConfigNotAvailableException extends SpoutException {

    private static final String NO_CONFIG = "There is no configuration available for this plugin!";
    private Exception exception;

    public ConfigNotAvailableException() {
        super(NO_CONFIG);
        exception = null;
        Logger.severe(NO_CONFIG);
        Logger.severe("This is an major error and you should notify the developer!");
    }

    public ConfigNotAvailableException(Exception ex) {
        super(NO_CONFIG, ex);
        exception = ex;
        Logger.severe(NO_CONFIG);
        Logger.severe("The following Exception was thrown:", exception);
        Logger.severe("This is an major error and you should notify the developer!");
    }


    public String getMessage() {
        return NO_CONFIG + "\nPlease contact the developer as this is a major error!";
    }

    public Exception getException() {
        return exception;
    }
}
