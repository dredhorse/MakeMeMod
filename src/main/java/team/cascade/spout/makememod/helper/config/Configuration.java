/******************************************************************************
 * This file is part of MakeMeMod (http://www.spout.org/).                *
 *                                                                            *
 * MakeMeMod is licensed under the SpoutDev License Version 1.            *
 *                                                                            *
 * MakeMeMod is free software: you can redistribute it and/or modify      *
 * it under the terms of the GNU Lesser General Public License as published by*
 * the Free Software Foundation, either version 3 of the License, or          *
 * (at your option) any later version.                                        *
 *                                                                            *
 * In addition, 180 days after any changes are published, you can use the     *
 * software, incorporating those changes, under the terms of the MIT license, *
 * as described in the SpoutDev License Version 1.                            *
 *                                                                            *
 * MakeMeMod is distributed in the hope that it will be useful,           *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of             *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the              *
 * GNU Lesser General Public License for more details.                        *
 *                                                                            *
 * You should have received a copy of the GNU Lesser General Public License,  *
 * the MIT license and the SpoutDev License Version 1 along with this program.*
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.                                                 *
 ******************************************************************************/

package team.cascade.spout.makememod.helper.config;

import team.cascade.spout.makememod.config.CONFIG;
import team.cascade.spout.makememod.helper.Logger;
import org.spout.api.plugin.Plugin;

/**
 * Here you will find all the information you will normally want to change to your liking. If you make changes to the
 * {@link CONFIG} enums increase the {@link #configCurrent} number and the plugin will handle autoupdating if configured.<br>
 * <br>
 * The following is the default configuration of the plugin if you don't change it via the corresponding setters or the enums TILL the config is loaded:
 * <ul>
 *     <li>configFile = config.yml
 *     <li>debugLogEnabled = false
 *     <li>debugLogToFile = true
 *     <li>checkForUpdate = true
 *     <li>configAutoUpdate = true
 *     <li>configAutoSave = true
 *     <li>configLogEnabled = true
 *     <li>disableOnOlderBuilds = false
 * </ul>
 *
 * </p>
 *
 * @author $Author: dredhorse$
 * @version $FullVersion$
 */
public class Configuration extends CommentConfiguration {


    /**
     * This is the internal config version, which is being used if no Version is supplied from the plugin itself
     * use {@link CommentConfiguration#alterConfigCurrent(String conCurr)} for this
     */

    String configCurrent = CONFIG.CONFIG_VERSION.getString();


    /**
     * Link to the location of the plugin website
     * Note: This can be overwritten by {@link CommentConfiguration#alterPluginSlug(String plugSlug)}
     */

    String pluginSlug = "https://github.com/dredhorse/MakeMeMod";

    /**
     * Link to the location of the recent version number, the file should be a text with {@code <version> x.y.rb-Bb}
     * for example your pom.yml <br>
     * For a better explanation of the version number scheme please look here: <A HREF=https://github.com/dredhorse/MakeMeMod/wiki/Versioning>Wiki</A> <br>
     * For those people who are interested what the version number means, e.g 3.5.1597-20  <br>
     * x.y.rb-Bb<br>
     * <br>
     * x = Major Version = Major new stuff or internal rework<br>
     * y = Minor Version = Minor new stuff and bug fixes<br>
     * rb = Recommended Build of Spout<br>
     * b = Development Build Version = if you have development builds for example B7<br>
     */

    String versionURL = "https://raw.github.com/dredhorse/MakeMeMod/master/pom.xml";


    /**
        * Method which will use the DEBUG_LOG_ENABLED, DEBUG_LOG_TO_FILE, CHECK_FOR_UPDATE, CONFIG_AUTO_UPDATE, CONFIG_AUTO_SAVE, CONFIG_LOG_ENABLED
        * enums to overwrite the default values configured in here.
        * <br>
        * If you don't want to use those enums make sure that you delete the corresponding references here!
        */


       void defaultInit() {
           // delete the corresponding lines if you delete the enums from the config! Default values will be used instead.
           debugLogEnabled = CONFIG.DEBUG_LOG_ENABLED.getBoolean();
           Logger.debug("debugLogEnabled [" + debugLogEnabled + "]");
           debugLogToFile = CONFIG.DEBUG_LOG_TO_FILE.getBoolean();
           checkForUpdate =  CONFIG.CHECK_FOR_UPDATE.getBoolean();
           configAutoUpdate =  CONFIG.CONFIG_AUTO_UPDATE.getBoolean();
           configAutoSave =  CONFIG.CONFIG_AUTO_SAVE.getBoolean();
           configLogEnabled = CONFIG.CONFIG_LOG_ENABLED.getBoolean();
           Logger.config("configLogEnabled [" + configLogEnabled + "]");

       }


    /**
     * Constructor for CommentConfiguration, you will still need perhaps to configure some stuff.
     *
     * @param plugin the main plugin class as reference
     * @see #initializeConfig() for more information
     */
    public Configuration(Plugin plugin) {
        super(plugin);
        super.alterConfigCurrent(configCurrent);
        super.alterPluginSlug(pluginSlug);
        super.alterVersionUrl(versionURL);
    }



}
