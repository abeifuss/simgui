package annotations;

public class BoolProp extends SimProp{

	boolean value;

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public void setValue(Object o) {
		value = (boolean)(o);
	}
	
	@Override
	public Class<?> getValueType() {
		return Boolean.class;
	}
}
