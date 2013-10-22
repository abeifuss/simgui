package annotations.simulationProperty;

// pojo
public abstract class SimProp {

	private String id;
	private String name;
	private String description;
	private String tooltip;
	private String pluginLayer;
	private String namespace;
	
	// dependencies
	boolean enabled;
	private Class<? extends Requirement>[] enable_requirements;
	public Class<? extends Requirement>[] getEnable_requirements() {
		return enable_requirements;
	}

	public void setEnable_requirements(Class<? extends Requirement>[] enable_requirements) {
		this.enable_requirements = enable_requirements;
	}
	
	private Class<? extends Requirement>[] value_requirements;
	

	public Class<? extends Requirement>[] getValue_requirements() {
		return this.value_requirements;
	}

	public void setValue_requirements(Class<? extends Requirement>[] value_requirements) {
		this.value_requirements = value_requirements;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTooltip() {
		return this.tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public String getPluginLayer() {
		return this.pluginLayer;
	}

	public void setPluginLayer(String pluginLayer) { 
		this.pluginLayer = pluginLayer;
	}
	
	public String getNamespace() {
		return this.namespace;
	}

	public void setNamespace(String namespace) { 
		this.namespace = namespace;
	}

	public void printOut() {
		System.out.println("ID(" + id + ") aka. " + name + " has");
		System.out.println("Description: " + description);
		System.out.println("Tooltip: " + tooltip);
		System.out.println("Its value type is " + getValueType());
		System.out.println("==================================");
	}

	public void setEnable(boolean enableFlag) {
		enabled = enableFlag;
	}
	
	public boolean getEnable() {
		return enabled;
	}
	
	// abstract methods
	public abstract Class<?> getValueType();
	public abstract Object getValue();
	public abstract void setValue(Object value);

	public String getPlugin() {
		// TODO Implement
		return "DummyPlugin";
	}

}
