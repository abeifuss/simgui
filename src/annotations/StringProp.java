package annotations;

public class StringProp extends SimProp{

	String value;

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public void setValue(Object o) {
		value = (String)(o);
	}
	
	@Override
	public Class<?> getValueType() {
		return String.class;
	}
}
