package evaluation.simulator.annotations.simulationProperty;

// pojo
public abstract class SimProp {

	private String description;
	private Class<? extends Requirement>[] enable_requirements;
	// dependencies
	boolean enabled;
	private String id;
	private String name;
	private String namespace;
	private int order;
	private String pluginLayer;

	private String propertykey;
	private String tooltip;
	private Class<? extends Requirement>[] value_requirements;
	private boolean isSuperclass;

	public String getDescription() {
		return this.description;
	}

	public boolean getEnable() {
		return this.enabled;
	}

	public Class<? extends Requirement>[] getEnable_requirements() {
		return this.enable_requirements;
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getNamespace() {
		return this.namespace;
	}

	public int getOrder() {
		return this.order;
	}

	public String getPlugin() {
		// TODO Implement
		return "DummyPlugin";
	}

	public String getPluginLayer() {
		return this.pluginLayer;
	}

	public String getPropertyKey() {
		return this.propertykey;
	}

	public String getTooltip() {
		return this.tooltip;
	}

	public abstract Object getValue();

	public Class<? extends Requirement>[] getValue_requirements() {
		return this.value_requirements;
	}

	// abstract methods
	public abstract Class<?> getValueType();

	public void printOut() {
		System.out.println("ID(" + this.id + ") aka. " + this.name + " has");
		System.out.println("Description: " + this.description);
		System.out.println("Tooltip: " + this.tooltip);
		System.out.println("Its value type is " + this.getValueType());
		System.out.println("==================================");
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setEnable(boolean enableFlag) {
		this.enabled = enableFlag;
	}

	public void setEnable_requirements(
			Class<? extends Requirement>[] enable_requirements) {
		this.enable_requirements = enable_requirements;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public void setPluginLayer(String pluginLayer) {
		this.pluginLayer = pluginLayer;
	}

	public void setPropertyKey(String key) {
		this.propertykey = key;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public abstract void setValue(Object value);

	public void setValue_requirements(
			Class<? extends Requirement>[] value_requirements) {
		this.value_requirements = value_requirements;
	}

	public void setIsSuperclassProperty(boolean b) {
		this.isSuperclass = b;
		
	}

}
