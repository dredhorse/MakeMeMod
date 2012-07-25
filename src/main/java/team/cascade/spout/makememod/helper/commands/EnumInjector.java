package team.cascade.spout.makememod.helper.commands;

/**
 * Amended class to handle Enums, based on {@link org.spout.api.command.annotated.Injector}
 * @author $Author: dredhorse$
 * @version $FullVersion$
 */

/**
 * An {@link EnumAnnotatedCommandRegistrationFactory} uses this this class to create
 * a new instance of command objects.
 */

public interface EnumInjector {
    public Object newInstance(Class<?> clazz);
}
