package annotations.simulationProperty;

public class BoolProp extends SimProp{

	private boolean _value;

	@Override
	public Object getValue() {
		return _value;
	}

	@Override
	public void setValue(Object o) {
		_value = (boolean)(o);
	}
	
	@Override
	public Class<?> getValueType() {
		return Boolean.class;
	}
}
