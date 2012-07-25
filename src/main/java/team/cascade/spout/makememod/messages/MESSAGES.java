package team.cascade.spout.makememod.messages;


import java.util.regex.Pattern;

//~--- enums ------------------------------------------------------------------

/*
* Messages for translation. The format is:<br>
* * NODE_NAME =       for the message <br>
* * message    =      to display including any variables you would like, by default you can use <br>
*                     %(player), %(realName), %(world) and %(loc)  <br>
* * commentMessage  = comment which is being displayed in the properties file  <br>
* <br>
* <br>
*
* @todo You need to put the messages you want to translate into here, just take a look at the examples.
 */
public enum MESSAGES {
    TEMPLATE_MESSAGE(
            "äöü æåéø Server will be stopped in %m minutes", "Warning Message displayed to announce server stop.");
    /* // NO CHANGES BELOW HERE!!!!!
;


/*
   * Used for replacing _
*/

    private static final Pattern COMPILE = Pattern.compile("_");

    //~--- fields -------------------------------------------------------------

/*
    * comment which is being displayed in the properties file
 */

    private String commentMessage;

/*
    * message to display including any variables you would like
 */


    private String message;

    //~--- constructors -------------------------------------------------------

/*
    * Creating the ENUM with the correct information
    *
    * @param message to translate
    * @param comment from the properties file
 */

    /**
     * Constructs ...
     *
     * @param message
     * @param comment
     */
    private MESSAGES(String message, String comment) {
        this.message = message;
        this.commentMessage = comment;
    }

    //~--- get methods --------------------------------------------------------

/*
    * Returns the message associated with this ENUM
    *
    * @return message
 */

    /**
     * Method description
     *
     * @return
     */
    public String getMessage() {
        return this.message;
    }

/*
    * Returns the comment associated with this ENUM
    *
    * @return commentMessage
      */

    /**
     * Method description
     *
     * @return
     */
    public String getComment() {
        return this.commentMessage;
    }

    //~--- methods ------------------------------------------------------------

/*
    * Override for toString to return the Message
 */

    /**
     * Returns the message of an enum, not the enum as string
     *
     * @return
     */
    @Override
    public String toString() {
        return message;
    }

/*
    * Turns the ENUM into a CamelCase Style, STOP_MESSAGE => StopMessage
    *
    * @return s ENUM in CamelCase Style
 */

    /**
     * Method description
     *
     * @return
     */
    public String toNode() {
        return toCamelCase(super.toString());
    }

    //~--- set methods --------------------------------------------------------

/*
    * Set the Message to display for the ENUM
    *
    * @param message to display for the ENUM
 */

    /**
     * Method description
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    //~--- methods ------------------------------------------------------------

/*
    * Removes _ from the Enum and makes it CamelCase
    *
    * @param s string to turn into CamelCase
    * @return camelCaseString
 */

    /**
     * Method description
     *
     * @param s
     * @return
     */
    static String toCamelCase(String s) {
        final String[] parts = COMPILE.split(s);
        String camelCaseString = "";

        for (String part : parts) {
            camelCaseString = camelCaseString + toProperCase(part);
        }

        return camelCaseString;
    }

/*
    * Turns MEMORY into Memory (aka ProperCase)
    *
    * @param s string to turn to ProperCase
    * @return s string in properCase
 */

    /**
     * Method description
     *
     * @param s
     * @return
     */
    static String toProperCase(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
