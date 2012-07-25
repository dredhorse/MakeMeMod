package team.cascade.spout.makememod.config;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * CONFIG Tester.
 *
 * @author $Author: dredhorse$
 * @version 1.0
 * @since <pre>Jul 8, 2012</pre>
 */
public class CONFIGTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: toString()
     */
    @Test
    public void testToString() throws Exception {
        Assert.assertEquals("DebugLogEnabled", CONFIG.DEBUG_LOG_ENABLED.toString());
    }


    /**
     * Method: getConfigComment()
     */
    @Test
    public void testGetConfigComment() throws Exception {
        Assert.assertEquals("Enable more logging.. could be messy!", CONFIG.DEBUG_LOG_ENABLED.getConfigComment());
    }

    /**
     * Method: getConfigOption()
     */
    @Test
    public void testGetConfigOption() throws Exception {
        boolean bool = (Boolean) CONFIG.DEBUG_LOG_ENABLED.getConfigOption();
        Assert.assertEquals(bool, false);
    }

    /**
     * Method: setConfigOption(Object configOption)
     */
    @Test
    public void testSetConfigOption() throws Exception {
        CONFIG.DEBUG_LOG_ENABLED.setConfigOption(false);
        boolean bool = (Boolean) CONFIG.DEBUG_LOG_ENABLED.getConfigOption();
        Assert.assertEquals(bool, false);
    }

    /**
     * Method: setConfigurationOption(Object configurationOption)
     */
    @Test
    public void testSetConfigurationOption() throws Exception {
        CONFIG.DEBUG_LOG_ENABLED.setConfigurationOption(false);
        boolean bool = (Boolean) CONFIG.DEBUG_LOG_ENABLED.getConfigOption();
        Assert.assertEquals(bool, false);
    }

    /**
     * Method: getString()
     */
    @Test
    public void testGetString() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: getBoolean()
     */
    @Test
    public void testGetBoolean() throws Exception {
        // must be false because we did set it this way before!
        Assert.assertEquals(CONFIG.DEBUG_LOG_ENABLED.getBoolean(), false);
    }

    /**
     * Method: getList()
     */
    @Test
    public void testGetList() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: getInt()
     */
    @Test
    public void testGetInt() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: getDouble()
     */
    @Test
    public void testGetDouble() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: getLong()
     */
    @Test
    public void testGetLong() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: getItemStack()
     */
    @Test
    public void testGetItemStack() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: getVector()
     */
    @Test
    public void testGetVector() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: getBooleanList()
     */
    @Test
    public void testGetBooleanList() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: getByteList()
     */
    @Test
    public void testGetByteList() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: getCharacterList(CONFIG configNode)
     */
    @Test
    public void testGetCharacterList() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: getDoubleList()
     */
    @Test
    public void testGetDoubleList() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: getFloatList()
     */
    @Test
    public void testGetFloatList() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: getIntegerList()
     */
    @Test
    public void testGetIntegerList() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: getLongList()
     */
    @Test
    public void testGetLongList() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: getMapList()
     */
    @Test
    public void testGetMapList() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: getMap()
     */
    @Test
    public void testGetMap() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: getShortList()
     */
    @Test
    public void testGetShortList() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: getStringList()
     */
    @Test
    public void testGetStringList() throws Exception {
//TODO: Test goes here...
    }


}
