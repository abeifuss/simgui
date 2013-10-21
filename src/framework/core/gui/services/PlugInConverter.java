/**
 * 
 */
package framework.core.gui.services;

import java.util.LinkedList;
import java.util.List;

import framework.core.gui.model.PlugInBean;
import framework.core.gui.model.StatefulViewPlugIn;

/**
 * Converts normal plug-ins to stateful plug-ins
 * 
 * @author Marius Fink
 * 
 */
public class PlugInConverter {

	/**
	 * Converts the Plugins to stateful plugins (initially neither filtered nor illegal)
	 * 
	 * @param availablePlugIns2
	 *            the plugins to convert
	 * @return the converted plugins
	 */
	public List<StatefulViewPlugIn> convertToStateful(List<PlugInBean> availablePlugIns2) {
		List<StatefulViewPlugIn> result = new LinkedList<StatefulViewPlugIn>();
		for (PlugInBean p : availablePlugIns2) {
			result.add(new StatefulViewPlugIn(p));
		}
		return result;
	}
}
