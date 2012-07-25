package team.cascade.spout.makememod.helper.file;

import team.cascade.spout.makememod.helper.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The default headers which are used in the properties files.
 *
 * @author $Author: dredhorse$
 * @version $FullVersion$
 */
public final class PropertiesHeader {


    private static String translationHeader = null;

    private PropertiesHeader(){
        // constructor is never called
    }


    public static void saveTranslationHeader(File file) {
        if (translationHeader == null) {
            // generate the default translationHeader once
            translationHeader = "";
            ArrayList<String> header = new ArrayList<String>();
            header.add("\n# ------- Translation Features\n");
            header.add("\n# Almost everything player visible can be translated!\n");
            header.add("# Please change to your liking.\n\n");
            header.add("# The following variables can be used and will be replaced:\n");
            header.add("# %(player), %(realName), %(world) and %(loc)\n");
            header.add("\n\n");
            for (String line : header) {
                translationHeader = translationHeader + line;
            }
        }
        try {
            UnicodeUtil.saveUTF8File(file, translationHeader, false);
        } catch (IOException e) {
            Logger.warning("Something happened during saving", e);
        }
    }
}
