package evaluation.simulator.annotations.plugin;

import com.google.common.collect.Sets;

import evaluation.simulator.conf.service.SimulationConfigService;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class SimGuiPlugin {
	private static Logger logger = Logger.getLogger(SimGuiPlugin.class);

	private String documentationURL;
	private String id;
	private String name;
	private String pluginLayer;
	private boolean visible;
	private boolean globalFields;
	private String fallbackLayer;
	private boolean allowGlobalFields;

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

	public boolean isGlobal() {
		return this.globalFields;
	}
	
	public void  isGlobal( boolean globalFields ) {
		this.globalFields = globalFields;
	}
	
	public void setFallbackLayer(String pluginLayer) {
		this.fallbackLayer = pluginLayer;		
	}
	
	public String getFallbackLayer() {
		return this.fallbackLayer;		
	}

	public void allowGlobalFields( boolean allowGlobalFields ) {
		this.allowGlobalFields = allowGlobalFields;
	}
	
	public boolean allowGlobalFields() {
		return this.allowGlobalFields;
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
				logger.log(Level.ERROR, this + " parameter pluginClass is not an instance of Plugin!!!");
				System.exit(-1);
			}
			return null;
		}
		
		Class<?> thisClass = pluginClass.getSuperclass();
		while ( !thisClass.isAnnotationPresent( PluginSuperclass.class ) && 
				!thisClass.getCanonicalName().equals("java.lang.Object") ){
			logger.log(Level.DEBUG, thisClass.getCanonicalName());
			thisClass = thisClass.getSuperclass();
		}
		
		if ( thisClass.isAnnotationPresent( PluginSuperclass.class ) ){
			return thisClass;
		}
		
		return null;
	}
	
}
