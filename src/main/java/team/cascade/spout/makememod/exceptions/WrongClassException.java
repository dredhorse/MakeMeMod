package team.cascade.spout.makememod.exceptions;

import team.cascade.spout.makememod.config.CONFIG;
import org.spout.api.exception.SpoutException;

/**
 * Exception which is thrown by the Config enum when the class which is being set is different to the
 * class which is already stored in the enum
 *
 * @author $Author: dredhorse$
 * @version $FullVersion$
 */


public class WrongClassException extends SpoutException {

    private CONFIG configNode;

    private Object setObject;

    public WrongClassException() {
        super("A wrong class was passed");
        configNode = null;
        setObject = null;
    }

    public WrongClassException(CONFIG configNode, Object setObject) {
        super("Class expected: " + configNode.getConfigOption().getClass() + " Class found: " + setObject.getClass() + " for " + configNode);
        this.configNode = configNode;
        this.setObject = setObject;
    }

    public String getMessage() {
        return configNode == null ? "A wrong class was passed" : "Class expected: " + configNode.getConfigOption().getClass() + " Class found: " + setObject.getClass() + " for " + configNode;
    }
}


