package framework.core.gui.services;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import framework.core.gui.model.PlugInBean;
import framework.core.gui.model.StatefulViewPlugIn;
import framework.core.gui.model.XMLResource;

/**
 * This service checks and validates the given PlugIns against a currently selected one.
 * 
 * @author Marius Fink
 */
public class CompositionDependencyService {

	private final Collection<StatefulViewPlugIn> currentlyAvailablePlugIns;

	/**
	 * Creates a new service with the currently available plug-ins
	 * 
	 * @param availablePlugIns
	 *            the currently available plug-ins (not changable, create new service if changes)
	 */
	public CompositionDependencyService(Collection<StatefulViewPlugIn> availablePlugIns) {
		this.currentlyAvailablePlugIns = availablePlugIns;
	}

	/**
	 * Checks if the static-function requirements of the selected plugin matches the static function
	 * capabilities of the others. If not, this method marks the other plug-in <code>illegal</code>.
	 * 
	 * @param currentlySelectedPlugIn
	 */
	public void checkDependencies(StatefulViewPlugIn currentlySelectedPlugIn) {
		// first reset
		for (StatefulViewPlugIn p : currentlyAvailablePlugIns) {
			p.setIllegal(false);
			p.setSourceIllegal(false);
		}

		currentlySelectedPlugIn.setSourceIllegal(true);

		for (StatefulViewPlugIn other : currentlyAvailablePlugIns) {
			// The selected one won't be
			if (!other.equals(currentlySelectedPlugIn)) {
				if (!matches(currentlySelectedPlugIn, other)) {
					other.setIllegal(true);
				}
			}
		}

		// IMPORTANT Tell the listeners that the plug-in state is final for the moment.
		for (StatefulViewPlugIn p : currentlyAvailablePlugIns) {
			p.finishedEditing();
		}
	}

	/**
	 * Checks if the match contains all static functions from the source requirements.
	 * 
	 * @param source
	 *            the plug-in that has the
	 * @param match
	 *            the plug-in to check if it matches the requirements
	 * @return true if the match matches to the requirements of the source
	 */
	private boolean matches(PlugInBean source, PlugInBean match) {
		return getCapabilitiesFrom(match).containsAll(getRequirementsFrom(source));
	}

	/**
	 * Gets the ids of the required static functions of the given plug-in
	 * 
	 * @param plugIn
	 *            the plug-in
	 * @return all requirement ids
	 */
	private Collection<String> getRequirementsFrom(PlugInBean plugIn) {
		Set<String> req = new HashSet<String>();

		int elements = plugIn.getResource().numberOfElements(
				"/plugIn/plugInSettings/staticFunctionRequirements/staticFunction");
		for (int i = 1; i <= elements; i++) {
			String id = plugIn.getResource().getRawAttributeValue(
					"/plugIn/plugInSettings/staticFunctionRequirements/staticFunction[" + i + "]", "id");
			if (id != null) {
				req.add(id);
			}
		}

		return req;
	}

	/**
	 * Gets the ids of the static functions the given plug-in is capable of
	 * 
	 * @param plugIn
	 *            the plug-in
	 * @return all capable ids
	 */
	private Collection<String> getCapabilitiesFrom(PlugInBean plugIn) {
		Set<String> req = new HashSet<String>();

		int elements = plugIn.getResource().numberOfElements(
				"/plugIn/plugInSettings/staticFunctionCapabilities/staticFunction");
		for (int i = 1; i <= elements; i++) {
			String id = plugIn.getResource().getRawAttributeValue(
					"/plugIn/plugInSettings/staticFunctionCapabilities/staticFunction[" + i + "]", "id");
			if (id != null) {
				req.add(id);
			}
		}

		return req;
	}

	public static boolean isConfigValid(XMLResource generalSettingsXml) {
		return true; // TODO implement: everything has to match everything
	}
}