package annotations.simulationProperty;

public class StringProp extends SimProp{

	String value;
	String possibleValues;

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public void setValue(Object o) {
		this.value = (String)(o);
	}
	
	@Override
	public Class<?> getValueType() {
		return String.class;
	}
	
	public String getPossibleValues() {
		return possibleValues;
	}

	public void setPossibleValues(String values) {
		this.possibleValues = values;
	}
}
