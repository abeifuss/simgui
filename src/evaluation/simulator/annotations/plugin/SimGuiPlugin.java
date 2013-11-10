package evaluation.simulator.annotations.plugin;

import com.google.common.collect.Sets;

import evaluation.simulator.log.LogLevel;
import evaluation.simulator.log.Logger;

public class SimGuiPlugin {

	private String documentationURL;
	private String id;
	private String name;
	private String pluginLayer;
	private boolean visible;
	private boolean globalFields;

	public String getDocumentationURL() {
		return this.documentationURL;
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public void setDocumentationURL(String documentationURL) {
		this.documentationURL = documentationURL;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPluginLayer() {
		return pluginLayer;
	}

	public void setPluginLayer(String pluginLayer) {
		this.pluginLayer = pluginLayer;
	}

	public boolean isVisible() {
		return this.visible;
	}
	
	public void isVisible( boolean isVisible) {
		this.visible = isVisible;
	}

	public boolean allowFieldsGlobal() {
		return this.globalFields;
	}
	
	public void  makeFieldsGlobal( boolean globalFields ) {
		this.globalFields = globalFields;
	}

	/**
	 * @param pluginClass A class that is annotated with @Plugin
	 * @return The class above pluginClass that is annotated with
	 * @PluginSuperclass. If no such class is found null is returned.
	 * If pluginClass has no @Plugin annotation, null is also returned,
	 * since there is no reason to scan such a class!
	 */
	public Class<?> getPluginSuperclass(Class<?> pluginClass) {
		
		// This can just happen if someone screwed the code!
		if ( !pluginClass.isAnnotationPresent( Plugin.class ) ){
			if ( !pluginClass.isInstance( this )){
				Logger.Log(LogLevel.ERROR, this + " parameter pluginClass is not an instance of Plugin!!!");
				System.exit(-1);
			}
			return null;
		}
		
		Class<?> thisClass = pluginClass.getSuperclass();
		while ( !thisClass.isAnnotationPresent( PluginSuperclass.class ) && 
				!thisClass.getCanonicalName().equals("java.lang.Object") ){
			Logger.Log(LogLevel.DEBUG, thisClass.getCanonicalName());
			thisClass = thisClass.getSuperclass();
		}
		
		if ( thisClass.isAnnotationPresent( PluginSuperclass.class ) ){
			return thisClass;
		}
		
		return null;
	}
	
}
