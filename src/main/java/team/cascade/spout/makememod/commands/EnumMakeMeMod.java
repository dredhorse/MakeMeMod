package team.cascade.spout.makememod.commands;

import team.cascade.spout.makememod.helper.commands.EnumCommand;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.NestedCommand;
import org.spout.api.exception.CommandException;
import org.spout.api.plugin.CommonPlugin;

/**
 * Set of default commands which are normally useful for plugin management. As this is a nested command <br>
 * there is nothing in this class really except the root command and the annotation @NestedCommand which than references</p>
 * the other class which holds all the child commands. {@link EnumMakeMeModCMDS}
 *
 * @author $Author: dredhorse$
 * @version $FullVersion$
 * @todo make sure that you use your own commands in these two classes
 */
public class EnumMakeMeMod {
    private final CommonPlugin plugin;

    /**
     * We must pass in an instance of our plugin's object for the annotation to register under the factory.
     *
     * @param instance
     */
    public EnumMakeMeMod(CommonPlugin instance) {
        plugin = instance;
    }

    @EnumCommand(command = COMMANDS.MAKEMEMOD)
    @NestedCommand(EnumMakeMeModCMDS.class)
    public void helperClasses(CommandContext args, CommandSource source) throws CommandException {
    }

}
