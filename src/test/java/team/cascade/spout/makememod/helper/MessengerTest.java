package team.cascade.spout.makememod.helper;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import team.cascade.spout.makememod.messages.MESSAGES;

import java.util.Hashtable;
import java.util.List;

/**
 * Messenger Tester.
 *
 * @author $Author: dredhorse$
 * @version 1.0
 * @since <pre>Jul 8, 2012</pre>
 */
public class MessengerTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: wrap(Object toString)
     */
    @Test
    public void testWrap() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: replaceVariable(MESSAGES message, String variable, String value)
     */
    @Test
    public void testReplaceVariable() throws Exception {
        Assert.assertEquals(Messenger.replaceVariable(MESSAGES.TEMPLATE_MESSAGE, "%m", "5"), "äöü æåéø Server will be stopped in 5 minutes");
    }

    /**
     * Method: sendError(Player player, String error)
     */
    @Test
    public void testSendError() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: send(final CommandSource sender, String messages)
     */
    @Test
    public void testSend() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: sendHeader(final CommandSource sender, String header)
     */
    @Test
    public void testSendHeader() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: colourise(String string)
     */
    @Test
    public void testColourise() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: replaceVariables(String message, String variable, String replacement)
     */
    @Test
    public void testReplaceVariables() throws Exception {
        String orgMessage = "This is a %test";
        String variable = "%test";
        String replacement = "SUCCESS";
        String result = "This is a SUCCESS";
        Assert.assertEquals(Messenger.replaceVariables(orgMessage, variable, replacement), result);
    }

    /**
     * Method: stringSplit(String string, int length)
     */
    @Test
    public void testStringSplit() throws Exception {
        String longString = "1234567890";
        List<String> splitString = Messenger.stringSplit(longString, 5);
        Assert.assertEquals("12345", splitString.get(0));
        Assert.assertEquals("67890", splitString.get(1));
        String longerString = "1234567890qwertzuiopüasdfghjklöäyxcvbnm,.-123345678990adf3fsfdafaeasfv";
        splitString = Messenger.stringSplit(longerString);
        Assert.assertEquals("fdafaeasfv", splitString.get(1));
    }

    /**
     * Method: stringWrap(String string)
     */
    @Test
    public void testStringWrap() throws Exception {
        String longString = "12 3456 7890";
        Assert.assertEquals("12<br>3456 7890", Messenger.stringWrap(longString, 5));
    }

    /**
     * Method: dictFormat(String format, CommandSource source)
     */
    @Test
    public void testDictFormat() throws Exception {
        Hashtable defaultDict = new Hashtable();
        defaultDict.put("player", "DisplayName");
        defaultDict.put("realName", "PlayerName");
        defaultDict.put("%(world)", "Earth");
        String replaceString = "%(realName) was also known as %(player) in %(world)";
        Assert.assertEquals("PlayerName was also known as DisplayName in Earth", Messenger.dictFormat(replaceString, defaultDict));
    }

    /**
     * Method: getPlayerDict(Player player)
     */
    @Test
    public void testGetPlayerDict() throws Exception {
//TODO: Test goes here...
    }


    /**
     * Method: makeBulletPoint(String string, ChatStyle wrap, ChatStyle trail)
     */
    @Test
    public void testMakeBulletPoint() throws Exception {
//TODO: Test goes here...
    }


    /**
     * Method: colouriser(String message)
     */
    @Test
    public void testColouriser() throws Exception {
//TODO: Test goes here...
/*
try {
   Method method = Messenger.getClass().getMethod("colouriser", String.class);
   method.setAccessible(true);
   method.invoke(<Object>, <Parameters>);
} catch(NoSuchMethodException e) {
} catch(IllegalAccessException e) {
} catch(InvocationTargetException e) {
}
*/
    }

    /**
     * Method: chatStyleLockup(String color)
     */
    @Test
    public void testChatStyleLockup() throws Exception {
//TODO: Test goes here...
/*
try {
   Method method = Messenger.getClass().getMethod("chatStyleLockup", String.class);
   method.setAccessible(true);
   method.invoke(<Object>, <Parameters>);
} catch(NoSuchMethodException e) {
} catch(IllegalAccessException e) {
} catch(InvocationTargetException e) {
}
*/
    }

}
