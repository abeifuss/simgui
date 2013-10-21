package framework.core.gui.services;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import framework.core.gui.model.StaticFunctionBean;
import framework.core.gui.model.XMLResource;
import framework.core.gui.model.XMLResourceContainer;
import framework.core.gui.util.GUIText;

/**
 * This is an extension for the DirScanService which can easily be used as a
 * data source for the static function-tree
 * 
 * @author Marius Fink
 */
public class StaticFunctionDirScanService extends DirScanService implements
		TreeServiceInputSource<XMLResourceContainer> {

	@Override
	public List<XMLResourceContainer> getTreeObjectsForLayer(int layer) {

		List<XMLResourceContainer> hits = new LinkedList<XMLResourceContainer>();

		String layerPath = GUIText.getText("layer" + layer
				+ "staticFunctionPath");
		for (File file : find(new File(layerPath), "StaticFunctionSettings.xml")) {
			XMLResource res = new XMLResource(file.getPath());

			hits.add(new StaticFunctionBean(res));
		}
		return hits;
	}
}
