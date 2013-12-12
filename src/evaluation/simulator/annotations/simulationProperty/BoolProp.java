package evaluation.simulator.annotations.simulationProperty;

public class BoolProp extends SimProp {

	private boolean value;

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public Class<?> getValueType() {
		return Boolean.class;
	}

	@Override
	public void setValue(Object o) {
		this.value = (boolean) (o);
	}

	@Override
	public String toString() {
		return super.getName() + "" + this.value;
	}

}
