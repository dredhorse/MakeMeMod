package team.cascade.spout.makememod.helper.commands;

/**
 *
 * @author $Author: dredhorse$
 * @version $FullVersion$
 */

import java.lang.reflect.Method;

/**
 * Classes that implement this interface are used by
 * {@link EnumAnnotatedCommandRegistrationFactory}s to register commands.
 */

public interface EnumAnnotatedCommandExecutorFactory {
    public EnumAnnotatedCommandExecutor getEnumAnnotatedCommandExecutor(Object instance, Method method);
}
