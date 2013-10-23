package evaluation.simulator.annotations.simulationProperty;

public class StringProp extends SimProp {

	String possibleValues;
	String value;

	public String getPossibleValues() {
		return this.possibleValues;
	}

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public Class<?> getValueType() {
		return String.class;
	}

	public void setPossibleValues(String values) {
		this.possibleValues = values;
	}

	@Override
	public void setValue(Object o) {
		this.value = (String) (o);
	}
}
