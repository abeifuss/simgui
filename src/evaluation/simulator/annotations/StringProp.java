package evaluation.simulator.annotations;

public class StringProp extends SimProp {

	String value;

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public Class<?> getValueType() {
		return String.class;
	}

	@Override
	public void setValue(Object o) {
		this.value = (String) (o);
	}
}
