package evaluation.simulator.annotations.plugin;

public class AnnotatedPlugin {

	private String documentationURL;
	private String id;
	private String name;
	private String pluginLayer;

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
	
	

}
