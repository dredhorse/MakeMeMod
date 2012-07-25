package team.cascade.spout.makememod.permissions;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * PERMISSIONS Tester.
 *
 * @author $Author: dredhorse$
 * @version 1.0
 * @since <pre>Jun 19, 2012</pre>
 */
public class PERMISSIONSTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getComment()
     */
    @Test
    public void testGetComment() throws Exception {
        PERMISSIONS.HLP_CLS_ADMIN.setComment("This is a test");
        Assert.assertEquals("This is a test", PERMISSIONS.HLP_CLS_ADMIN.getComment());

    }

    /**
     * Method: toString()
     */
    @Test
    public void testToString() throws Exception {
        Assert.assertEquals(PERMISSIONS.HLP_CLS_TEST.toString(), "hlp.cls.test");
    }


    /**
     * Method: handle(CommandSource source)
     */
    @Test
    public void testHandle() throws Exception {
//TODO: Test goes here...
    }

/**
 *
 * Method: has(Player player)
 *
 */
//@Test
//public void testHas() throws Exception {
//TODO: Test goes here...
//}


    /**
     * Method: has(Player player, int depth)
     */
    @Test
    public void testHas() throws Exception {
//TODO: Test goes here...
/*
try {
   Method method = PERMISSIONS.getClass().getMethod("has", Player.class, int.class);
   method.setAccessible(true);
   method.invoke(<Object>, <Parameters>);
} catch(NoSuchMethodException e) {
} catch(IllegalAccessException e) {
} catch(InvocationTargetException e) {
}
*/
    }

}
