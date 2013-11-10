package evaluation.simulator.annotations.simulationProperty;

// pojo
public abstract class SimProp {

	private String description;
	private Class<? extends Requirement>[] enable_requirements;
	// dependencies
	boolean enabled;
	private String id;
	private String name;
	private String plugin;
	private int order;
	private String pluginLayer;
	private String propertykey;
	private String tooltip;
	private Class<? extends Requirement>[] value_requirements;
	private boolean isSuperclassProperty;
	private boolean isGlobal;

	public String getDescription() {
		return this.description;
	}

	public boolean getEnable() {
		return this.enabled;
	}

	public Class<? extends Requirement>[] getEnable_requirements() {
		return this.enable_requirements;
	}

	public String getPropertyID() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getPluginID() {
		return this.plugin;
	}

	public int getOrder() {
		return this.order;
	}

	public String getPluginLayerID() {
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

	public abstract String toString();

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

	public void setPluginID(String namespace) {
		this.plugin = namespace;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public void setPluginLayerID(String pluginLayer) {
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

	public void isGlobal(boolean isGlobal) {
		this.isGlobal = isGlobal;
		
	}
	
	public boolean isGlobal() {
		return isGlobal;
	}

	public boolean isSuperclass() {
		return isSuperclassProperty;
	}

	public void isSuperclass(boolean isSuperclass) {
		this.isSuperclassProperty = isSuperclass;
	}

}
