package team.cascade.spout.makememod.helper.commands;

import org.spout.api.command.CommandSource;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * Amended class to handle Enums, based on {@link org.spout.api.command.annotated.SimpleAnnotatedCommandExecutorFactory}
 *
 * @author $Author: dredhorse$
 * @version $FullVersion$
 */
public class EnumSimpleAnnotatedCommandExecutorFactory implements EnumAnnotatedCommandExecutorFactory {
    public EnumAnnotatedCommandExecutor getEnumAnnotatedCommandExecutor(Object instance, Method method) {
        return new EnumSimpleAnnotatedCommandExecutor(instance, method);
    }

    public static class EnumSimpleAnnotatedCommandExecutor extends EnumAnnotatedCommandExecutor {

        public EnumSimpleAnnotatedCommandExecutor(Object instance, Method method) {
            super(instance, method);
        }

        @Override
        public List<Object> getAdditionalArgs(CommandSource source, org.spout.api.command.Command command) {
            return Collections.emptyList();
        }
    }
}
