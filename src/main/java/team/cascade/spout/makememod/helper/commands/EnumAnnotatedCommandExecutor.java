package team.cascade.spout.makememod.helper.commands;

import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandExecutor;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;
import org.spout.api.exception.WrappedCommandException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Amended class to handle Enums, based on {@link org.spout.api.command.annotated.AnnotatedCommandExecutor}
 *
 * @author $Author: dredhorse$
 * @version $FullVersion$
 */
public abstract class EnumAnnotatedCommandExecutor implements CommandExecutor {
    private final Object instance;
    private final Method method;

    public EnumAnnotatedCommandExecutor(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    public boolean processCommand(CommandSource source, Command command, CommandContext args) throws CommandException {
        try {
            List<Object> commandArgs = new ArrayList<Object>(4);
            commandArgs.add(args);
            commandArgs.add(source);
            commandArgs.addAll(getAdditionalArgs(source, command));
            method.invoke(instance, commandArgs.toArray());
        } catch (IllegalAccessException e) {
            throw new WrappedCommandException(e);
        } catch (InvocationTargetException e) {
            if (e.getCause() == null) {
                throw new WrappedCommandException(e);
            }

            Throwable cause = e.getCause();
            if (cause instanceof CommandException) {
                throw (CommandException) cause;
            }

            throw new WrappedCommandException(cause);
        }
        return true;
    }

    public abstract List<Object> getAdditionalArgs(CommandSource source, Command command);

}
