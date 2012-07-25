package team.cascade.spout.makememod.helper.file;

//~--- JDK imports ------------------------------------------------------------

import team.cascade.spout.makememod.helper.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

//~--- classes ----------------------------------------------------------------

/**
 * Class description
 *
 * @author $Author: dredhorse$
 * @version $FullVersion$
 */
public final class PropertiesLoader {
    /**
     * Map to store the messages internally
     */
    private static Map<String, String> msg = new HashMap<String, String>();

    private PropertiesLoader() {
        // constructor is never used
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method to check if the properties file already exists
     *
     * @param path         where the property file is
     * @param propertyFile which is being used by the plugin
     * @return true if propertyFile exists, false if not
     */
    public static boolean propertyFileExist(File path, String propertyFile) {
        return (new File(path, propertyFile)).exists();
    }

    /**
     * Initializes the msgMap by reading from the announcements-file.
     *
     * @param path             to the file
     * @param propertyFilename property file name
     * @return
     */
    public static Map<String, String> read(File path, String propertyFilename) {
        msg.clear();

        // Grab the property-file.
        File msgFile = new File(path, propertyFilename);

        // read the file's contents.
        try {
            BufferedReader br = new BufferedReader(new UnicodeReader(new FileInputStream(msgFile),
                    "UTF-8"));

            String s;

            while ((s = br.readLine()) != null) {

                // Ignore empty lines and comments
                if (!s.isEmpty() && !s.startsWith("#")) {
                    Logger.debug("line", s);
                    // Logger.debug("line converted", convertFromUTF8(s));
                    process(s);
                }
            }

            br.close();
        } catch (Exception e) {
            Logger.warning("Problems with reading the properties", e);
        }

        return msg;
    }

    /**
     * Helper-method for parsing the strings from the
     * property-file.
     *
     * @param s string to parse
     */
    private static void process(String s) {

        // If the line ends with =, just add a space
        if (s.endsWith("=") || s.endsWith("|")) {
            s += " ";
        }

        // Split the string by the equals-sign.
        String[] split = s.split("=");

        if (split.length != 2) {
            Logger.config("Couldn't parse \"" + s + "\". Check property-file.");

            return;
        }

        // For simplicity...
        String key = split[0];
        String val = split[1].replaceAll("\"","");

        msg.put(key, val);
    }


    /**
     * From http://www.jguru.com/faq/view.jsp?EID=137049
     * <br>
     * convert from UTF-8 encoded HTML-Pages -> internal Java String Format
     *
     * @param s
     * @return
     */
    public static String convertFromUTF8(String s) {
        String out = null;

        Logger.debug("system encoding", System.getProperty("file.encoding"));

        try {
            out = new String(s.getBytes(System.getProperty("file.encoding")), Charset.forName("UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }

        return out;
    }

    /**
     * convert from internal Java String Format -> UTF-8 encoded HTML/JSP-Pages
     *
     * @param s
     * @return
     */
    public static String convertToUTF8(String s) {
        return new String(s.getBytes(Charset.forName("UTF-8")));
    }
}
