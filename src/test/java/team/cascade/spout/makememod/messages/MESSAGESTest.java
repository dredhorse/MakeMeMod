package team.cascade.spout.makememod.messages;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * MESSAGES Tester.
 *
 * @author $Author: dredhorse$
 * @version 1.0
 * @since <pre>Jul 8, 2012</pre>
 */
public class MESSAGESTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getMessage()
     */
    @Test
    public void testGetMessage() throws Exception {
        Assert.assertEquals(MESSAGES.TEMPLATE_MESSAGE.getMessage(), "äöü æåéø Server will be stopped in %m minutes");
    }

    /**
     * Method: getComment()
     */
    @Test
    public void testGetComment() throws Exception {
        Assert.assertEquals(MESSAGES.TEMPLATE_MESSAGE.getComment(), "Warning Message displayed to announce server stop.");
    }

    /**
     * Method: toString()
     */
    @Test
    public void testToString() throws Exception {
        Assert.assertEquals(MESSAGES.TEMPLATE_MESSAGE.toString(), MESSAGES.TEMPLATE_MESSAGE.getMessage());
    }

    /**
     * Method: toNode()
     */
    @Test
    public void testToNode() throws Exception {
        Assert.assertEquals(MESSAGES.TEMPLATE_MESSAGE.toNode(), "TemplateMessage");
    }

    /**
     * Method: setMessage(String message)
     */
    @Test
    public void testSetMessage() throws Exception {
        MESSAGES.TEMPLATE_MESSAGE.setMessage("test");
        Assert.assertEquals(MESSAGES.TEMPLATE_MESSAGE.getMessage(), "test");
    }

    /**
     * Method: toCamelCase(String s)
     */
    @Test
    public void testToCamelCase() throws Exception {
        Assert.assertEquals(MESSAGES.toCamelCase("TEMPLATE_MESSAGE"), "TemplateMessage");
    }

    /**
     * Method: toProperCase(String s)
     */
    @Test
    public void testToProperCase() throws Exception {
        Assert.assertEquals(MESSAGES.toProperCase("TEST"), "Test");
    }


}
