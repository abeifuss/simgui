package evaluation.simulator.annotations;

// pojo
public abstract class SimProp {

	private String _category;
	private String _description;
	private String _id;
	private String _name;
	private String _tooltip;

	private Class<? extends Requirement>[] enable_requirements;
	// dependencies
	boolean enabled;
	private Class<? extends Requirement>[] value_requirements;

	public String getCategory() {
		return this._category;
	}

	public String getDescription() {
		return this._description;
	}

	public boolean getEnable() {
		return this.enabled;
	}

	public Class<? extends Requirement>[] getEnable_requirements() {
		return this.enable_requirements;
	}

	public String getId() {
		return this._id;
	}

	public String getName() {
		return this._name;
	}

	public String getPlugin() {
		// TODO Implement
		return "DummyPlugin";
	}

	public String getTooltip() {
		return this._tooltip;
	}

	public abstract Object getValue();

	public Class<? extends Requirement>[] getValue_requirements() {
		return this.value_requirements;
	}

	// abstract methods
	public abstract Class<?> getValueType();

	public void printOut() {
		System.out.println("ID(" + this._id + ") aka. " + this._name + " has");
		System.out.println("Description: " + this._description);
		System.out.println("Tooltip: " + this._tooltip);
		System.out.println("Its value type is " + this.getValueType());
		System.out.println("==================================");
	}

	public void setCategory(String category) {
		this._category = category;
	}

	public void setDescription(String description) {
		this._description = description;
	}

	public void setEnable(boolean enableFlag) {
		this.enabled = enableFlag;
	}

	public void setEnable_requirements(
			Class<? extends Requirement>[] enable_requirements) {
		this.enable_requirements = enable_requirements;
	}

	public void setId(String id) {
		this._id = id;
	}

	public void setName(String name) {
		this._name = name;
	}

	public void setTooltip(String tooltip) {
		this._tooltip = tooltip;
	}

	public abstract void setValue(Object value);

	public void setValue_requirements(
			Class<? extends Requirement>[] value_requirements) {
		this.value_requirements = value_requirements;
	}

}
