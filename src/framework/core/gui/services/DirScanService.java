package framework.core.gui.services;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * DirScanService is a service that scans folders non-recursively for matching files or folders.
 * 
 * @author Marius Fink
 * @version 20.11.2012
 */
public class DirScanService {

	/**
	 * Finds files in a given directory (non recursively) with the given ending.
	 * 
	 * @param root
	 *            the directory (can be a file too, but then only the file will be validated and in
	 *            a list returned or an empty list will be returned)
	 * @param suffix
	 *            the ending of all files to return
	 * @return a list of matching files
	 */
	public List<File> find(File root, String suffix) {
		List<File> matchingFiles = new LinkedList<File>();

		File absoluteFile = root.getAbsoluteFile();
		if (absoluteFile.toString().endsWith(suffix)) {
			matchingFiles.add(absoluteFile);
		} else if (root.isDirectory()) {
			String[] subNote = root.list();
			for (String filename : subNote) {
				matchingFiles.addAll(find(new File(root.getPath() + File.separator + filename),
						suffix));
			}
		}
		return matchingFiles;
	}

	/**
	 * Finds the first directory in the given root directory non recursively that matches the given
	 * prefix.
	 * 
	 * @param root
	 *            the directory to search in
	 * @param prefix
	 *            the prefix of the folder to return
	 * @return the first matching folder or null if no foldername starts with prefix.
	 */
	public File getDirThatStartsWith(File root, String prefix) {
		assert root.isDirectory() : "Precondition violated: node.isDirectory()";

		for (String filename : root.list()) {
			File file = new File(root.getPath() + File.separator + filename);
			if (file.isDirectory() && file.getName().startsWith(prefix)) {
				return file;
			}
		}
		return null;
	}
}
