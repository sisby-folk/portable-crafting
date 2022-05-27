package folk.sisby.portable_crafting_standalone.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import org.quiltmc.loader.api.QuiltLoader;

/**
 * @version 4.0.0-charm
 */
public class LogHelper {
    public static String DEFAULT_INSTANCE;
    public static Map<String, Logger> INSTANCES = new HashMap<>();

	private static String capitalize(String string) {
		if (string == null || string.isEmpty()) {
			return string;
		}

		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}

    public static Logger instance(String modId) {
        return INSTANCES.computeIfAbsent(modId, m -> LogManager.getFormatterLogger(capitalize(modId)));
    }

    public static void info(Class<?> source, String message, Object... args) {
        if (DEFAULT_INSTANCE != null) {
            info(DEFAULT_INSTANCE, source, message, args);
        }
    }

    public static void info(String modId, Class<?> source, String message, Object... args) {
        instance(modId).info(assembleMessage(source, message), args);
    }

    public static void warn(Class<?> source, String message, Object... args) {
        if (DEFAULT_INSTANCE != null) {
            warn(DEFAULT_INSTANCE, source, message, args);
        }
    }

    public static void warn(String modId, Class<?> source, String message, Object... args) {
        instance(modId).warn(assembleMessage(source, message), args);
    }

    public static void error(Class<?> source, String message, Object... args) {
        if (DEFAULT_INSTANCE != null) {
            error(DEFAULT_INSTANCE, source, message, args);
        }
    }

    public static void error(String modId, Class<?> source, String message, Object... args) {
        instance(modId).error(assembleMessage(source, message), args);
    }

    public static void debug(Class<?> source, String message, Object... args) {
        if (DEFAULT_INSTANCE != null) {
            debug(DEFAULT_INSTANCE, source, message, args);
        }
    }

    public static void debug(String modId, Class<?> source, String message, Object... args) {
        if (QuiltLoader.isDevelopmentEnvironment()) {
            instance(modId).info(assembleMessage(source, message), args);
        }
    }

    private static String assembleMessage(Class<?> source, String message) {
        return "[" + source.getSimpleName() + "] " + message;
    }
}
