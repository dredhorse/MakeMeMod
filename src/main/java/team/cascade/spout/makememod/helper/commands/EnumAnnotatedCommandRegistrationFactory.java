package team.cascade.spout.makememod.helper.commands;

import team.cascade.spout.makememod.helper.Logger;
import org.spout.api.command.CommandRegistrationsFactory;
import org.spout.api.command.annotated.Injector;
import org.spout.api.command.annotated.NestedCommand;
import org.spout.api.util.Named;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Factory handling EnumAnnotated commands
 * Amended class to handle Enums, based on {@link org.spout.api.command.annotated.AnnotatedCommandRegistrationFactory}
 *
 * @author $Author: dredhorse$
 * @version $FullVersion$
 */
public class EnumAnnotatedCommandRegistrationFactory implements CommandRegistrationsFactory<Class<?>> {

    /**
     * An {@link Injector} used to create instances of command classes when
     * non-static methods are used.
     */
    private final EnumInjector injector;

    private final EnumAnnotatedCommandExecutorFactory executorFactory;

    public EnumAnnotatedCommandRegistrationFactory(EnumInjector injector, EnumAnnotatedCommandExecutorFactory executorFactory) {
        this.injector = injector;
        this.executorFactory = executorFactory;
    }

    public boolean create(Named owner, Class<?> commands, org.spout.api.command.Command parent) {
        Object instance = null;
        if (injector != null) {
            instance = injector.newInstance(commands);
        }
        boolean success = true;
        for (Method method : commands.getMethods()) {
            // Basic checks
            if (!Modifier.isStatic(method.getModifiers()) && instance == null) {
                continue;
            }
            if (!method.isAnnotationPresent(EnumCommand.class)) {
                continue;
            }

            EnumCommand command = method.getAnnotation(EnumCommand.class);
            if (command.command().getAliases().length < 1) {
                success = false;
                continue;
            }


            org.spout.api.command.Command child = parent.addSubCommand(owner, command.command().getCommand()).addAlias(command.command().getAliases()).setUsage(command.command().getUsage()).setHelp(command.command().getCmdDescription()).setArgBounds(command.command().getMinArgs(),command.command().getMaxArgs());


            child.setPermissions(command.command().getPermissions().getRequireAll(), command.command().getPermString());
            Logger.debug("Command: " + child.getPreferredName() + " Aliases: " + child.getNames() + " Usage: [" + child.getUsage() + "] Description: [" + child.getHelp() + "] Childs: " + child.getChildNames());

            if (method.isAnnotationPresent(NestedCommand.class)) {
                for (Class<?> clazz : method.getAnnotation(NestedCommand.class).value()) {
                    success &= create(owner, clazz, child);
                }
            } else {
                child.setExecutor(executorFactory.getEnumAnnotatedCommandExecutor(instance, method));
            }
            child.closeSubCommand();
        }
        return success;
    }

}
