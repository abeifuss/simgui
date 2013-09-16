package annotations;

// pojo
public abstract class SimProp {

	private String _id;
	private String _name;
	private String _description;
	private String _tooltip;
	private String _category;
	
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
		return value_requirements;
	}

	public void setValue_requirements(Class<? extends Requirement>[] value_requirements) {
		this.value_requirements = value_requirements;
	}

	public String getId() {
		return _id;
	}

	public void setId(String id) {
		this._id = id;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		this._name = name;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		this._description = description;
	}

	public String getTooltip() {
		return _tooltip;
	}

	public void setTooltip(String tooltip) {
		this._tooltip = tooltip;
	}

	public String getCategory() {
		return _category;
	}

	public void setCategory(String category) { 
		this._category = category;
	}

	public void printOut() {
		System.out.println("ID(" + _id + ") aka. " + _name + " has");
		System.out.println("Description: " + _description);
		System.out.println("Tooltip: " + _tooltip);
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
