/**
 * 
 */
package framework.core.gui.util;

import java.io.File;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Converts paths
 * @author Marius Fink
 *
 */
public class PathsUtils {

	public static String toUnixPath(String path) {
		return path.replaceAll(StringEscapeUtils.escapeJava(File.separator), "/");
	}
	
	public static String toCurrentSystemPath(String path) {
		return path.replaceAll("/", StringEscapeUtils.escapeJava(File.separator));
	}
}
