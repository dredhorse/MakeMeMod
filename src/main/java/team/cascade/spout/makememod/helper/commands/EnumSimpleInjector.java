package team.cascade.spout.makememod.helper.commands;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Amended class to handle Enums, based on {@link org.spout.api.command.annotated.SimpleInjector}
 *
 * @author $Author: dredhorse$
 * @version $FullVersion$
 */
public class EnumSimpleInjector implements EnumInjector {
    private Object[] args;
    private Class<?>[] argClasses;

    public EnumSimpleInjector(Object... args) {
        this.args = args;
        argClasses = new Class[args.length];
        for (int i = 0; i < args.length; ++i) {
            argClasses[i] = args[i].getClass();
        }
    }

    public Object newInstance(Class<?> clazz) {
        try {
            Constructor<?> ctr = null;
            int lowestSubclassCount = Integer.MAX_VALUE;
            for (Constructor<?> c : clazz.getConstructors()) {
                boolean match = true;
                Class<?>[] args = c.getParameterTypes();
                if (args == null || args.length != argClasses.length) {
                    continue;
                }
                int subclassCount = 0;
                for (int i = 0; i < args.length; i++) {
                    if (!args[i].isAssignableFrom(argClasses[i])) {
                        match = false;
                        break;
                    } else {
                        Class<?> a = argClasses[i];
                        while (a != null && !a.equals(args[i])) {
                            subclassCount++;
                            a = a.getSuperclass();
                        }
                    }
                }
                if (match) {
                    if (subclassCount < lowestSubclassCount) {
                        lowestSubclassCount = subclassCount;
                        ctr = c;
                    }
                }
            }
            if (ctr == null) {
                ctr = clazz.getConstructor(argClasses);
            }
            return ctr.newInstance(args);
        } catch (NoSuchMethodException e) {
            return null;
        } catch (InvocationTargetException e) {
            return null;
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
    }
}
