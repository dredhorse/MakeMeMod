package team.cascade.spout.makememod.helper.file;


import team.cascade.spout.makememod.helper.Logger;

import java.io.*;

/**
 * taken from  AdminCmd    http://bit.ly/RLz4Po
 *
 * @author $Author: dredhorse$
 * @version $FullVersion$
 */
public class UnicodeUtil {

    public static final byte[] UTF8_BOMS = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
    public static final int BOM_SIZE = 4;

    private UnicodeUtil(){
        // constructor never called
    }

    /**
     * Save a String to a file in UTF8 with Signature
     *
     * @param file   file to save
     * @param data   data to write
     * @param append append data
     * @throws IOException something goes wrong with the file
     */
    public static void saveUTF8File(final File file, final String data, final boolean append)
            throws IOException {
        BufferedWriter bw = null;
        OutputStreamWriter osw = null;

        final FileOutputStream fos = new FileOutputStream(file, append);
        try {
            // write UTF8 BOM mark if file is empty
            if (file.length() < 1) {
                fos.write(UTF8_BOMS);
            }

            osw = new OutputStreamWriter(fos, "UTF-8");
            bw = new BufferedWriter(osw);
            if (data != null) {
                bw.write(data);
            }
        } finally {
            try {
                bw.close();
                fos.close();
            } catch (final Exception ex) {
                Logger.warning("Something went wrong while writing the file", ex);
            }
        }
    }

    /**
     * Save the content of an InputStream to a file in UTF8 with Signature
     *
     * @param file   file to save
     * @param stream InputStream to copy to the file
     * @param append append data
     * @throws IOException something goes wrong with the file
     */
    public static void saveUTF8File(final File file, final InputStream stream, final boolean append)
            throws IOException {
        BufferedWriter bw = null;
        OutputStreamWriter osw = null;

        final FileOutputStream fos = new FileOutputStream(file, append);
        final Reader reader = new BufferedReader(new UnicodeReader(stream, "UTF-8"));
        try {
            // write UTF8 BOM mark if file is empty
            if (file.length() < 1) {
                fos.write(UTF8_BOMS);
            }
            osw = new OutputStreamWriter(fos, "UTF-8");
            bw = new BufferedWriter(osw);
            if (reader != null) {
                for (int i = 0; (i = reader.read()) > 0; ) {
                    bw.write(i);
                }
            }
        } finally {
            try {
                bw.close();
                fos.close();
                reader.close();
            } catch (final Exception ex) {
                Logger.warning("Something went wrong while writing the file", ex);
            }
        }
    }

}
