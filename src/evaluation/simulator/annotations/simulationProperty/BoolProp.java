package evaluation.simulator.annotations.simulationProperty;

public class BoolProp extends SimProp {

	private boolean _value;

	@Override
	public Object getValue() {
		return this._value;
	}

	@Override
	public Class<?> getValueType() {
		return Boolean.class;
	}

	@Override
	public void setValue(Object o) {
		this._value = (boolean) (o);
	}
}
