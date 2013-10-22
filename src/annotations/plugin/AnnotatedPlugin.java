package annotations.plugin;

public class AnnotatedPlugin {

	private String id;
	private String name;
	private String documentationURL;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDocumentationURL() {
		return documentationURL;
	}

	public void setDocumentationURL(String documentationURL) {
		this.documentationURL = documentationURL;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getId(){
		return this.id;
	}

}
