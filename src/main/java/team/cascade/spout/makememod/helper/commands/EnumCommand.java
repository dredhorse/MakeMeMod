package team.cascade.spout.makememod.helper.commands;

import team.cascade.spout.makememod.commands.COMMANDS;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for handling EnumCommand
 *
 * @author $Author: dredhorse$
 * @version $FullVersion$
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumCommand {
    public COMMANDS command();

}
