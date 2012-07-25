package team.cascade.spout.makememod.exceptions;


import team.cascade.spout.makememod.helper.Logger;
import org.spout.api.exception.SpoutException;

/**
 * Exception which is thrown by the CommentConfiguration when the configuration is not initialized.
 * NOTE: THIS IS SEVERE AND YOU SHOULD FIGURE OUT WHY THIS HAPPENED!
 *
 * @author $Author: dredhorse$
 * @version $FullVersion$
 */


@SuppressWarnings("ALL")
public class ConfigNotInitializedException extends SpoutException {

    private Exception exception;

    public ConfigNotInitializedException() {
        super("The configuration has not been initialized!");
        exception = null;
        Logger.severe("There is no configuration available for this plugin!");
        Logger.severe("This is an major error and you should notify the developer!");
    }

    public ConfigNotInitializedException(Exception ex) {
        super("The configuration has not been initialized!", ex);
        exception = ex;
        Logger.severe("There is no configuration available for this plugin!");
        Logger.severe("The following Exception was thrown:", exception);
        Logger.severe("This is an major error and you should notify the developer!");
    }


    public String getMessage() {
        return "The configuration has not been initialized!, please contact the developer as this is a major error!";
    }

    public Exception getException() {
        return exception;
    }
}
