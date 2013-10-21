package framework.core.gui.services;

import java.util.List;

import framework.core.gui.model.Treeable;

/**
 * A generized interface to mark input data sources for JTrees for static functions and plugins
 * 
 * @author Marius Fink
 * 
 * @param <T>
 *            <code>extends Treeable</code> everything that can be displayed in a Tree and is marked
 *            likewise
 */
public interface TreeServiceInputSource<T extends Treeable> {

	public List<T> getTreeObjectsForLayer(int layer);
}
