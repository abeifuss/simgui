package framework.core.util;

/**
 * FallbackConversion is a Converter/Parser of String values to specified types
 * - ALWAYS RETURNING A FALLBACK VALUE ON ERROR!
 * 
 * @author Marius Fink
 * @version 11.08.2012
 */
public class FallbackConversion {

    /**
     * Parses a String safely to int - returning fallback on error
     * 
     * @param toParse
     *            the String to parse
     * @param fallback
     *            the fallback value
     * @return the parsed String or fallback on error
     */
    public static int parseInt(String toParse, int fallback) {
	try {
	    int i = Integer.parseInt(toParse);
	    return i;
	} catch (NumberFormatException nfe) {
	    return fallback;
	}
    }

    /**
     * Parses a String safely to float - returning fallback on error
     * 
     * @param toParse
     *            the String to parse
     * @param fallback
     *            the fallback value
     * @return the parsed String or fallback on error
     */
    public static float parseFloat(String toParse, float fallback) {
	try {
	    float i = Float.parseFloat(toParse);
	    return i;
	} catch (NumberFormatException nfe) {
	    return fallback;
	}
    }

    /**
     * Parses a String safely to double - returning fallback on error
     * 
     * @param toParse
     *            the String to parse
     * @param fallback
     *            the fallback value
     * @return the parsed String or fallback on error
     */
    public static double parseDouble(String toParse, double fallback) {
	try {
	    double i = Double.parseDouble(toParse);
	    return i;
	} catch (NumberFormatException nfe) {
	    return fallback;
	}
    }

    /**
     * Parses a String safely to boolean - returning fallback on error "true",
     * "false" as well as "1", "0" can be parsed (all trimmed and case
     * insensitive). Everything else return fallback.
     * 
     * @param toParse
     *            the String to parse
     * @param fallback
     *            the fallback value
     * @return the parsed String or fallback on error
     */
    public static boolean parseBoolean(String toParse, boolean fallback) {
	if (toParse == null) {
	    return fallback;
	}
	if (toParse.trim().equalsIgnoreCase("true")
		|| toParse.trim().equalsIgnoreCase("1")) {
	    return true;
	} else if (toParse.trim().equalsIgnoreCase("false")
		|| toParse.trim().equalsIgnoreCase("0")) {
	    return false;
	} else {
	    return fallback;
	}
    }
}
