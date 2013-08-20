package annotations;

// pojo
public abstract class SimProp {

	String id;
	String name;
	String description;
	String tooltip;
	String category;
	
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
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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



}
