package evaluation.simulator.annotations;

import javax.swing.JOptionPane;

import evaluation.simulator.gui.pluginRegistry.DependencyChecker;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;

public abstract class Requirement {

	private String _comment;

	/**
	 * checks if an defined Requirement is met
	 * 
	 * @return true: Requirement is met, false if not
	 */
	public boolean check() {
		throw new RuntimeException("Please implement!");
	}

	protected boolean equals(String key, String value) {
		SimPropRegistry gcr = SimPropRegistry.getInstance();
		// return (gcr.getValue(key).getIntValue()+"").equals(value);
		return (gcr.getValue(key).getValue() + "").equals(value);
	}

	public String getComment() {
		return this._comment;
	}

	protected Integer getIntegerValueOfOption(String option) {
		SimPropRegistry gcr = SimPropRegistry.getInstance();
		Integer i = Integer
				.parseInt(gcr.getValue(option).getValue().toString());
		System.out.println("Requirement getIntegerValueOfOption Integer value:"
				+ i);
		return i;

	}

	public void setComment(String comment) {
		this._comment = comment;
	}

	// protected Integer getIntegerMaxValueOfOption(String option){
	// SimPropRegistry gcr = SimPropRegistry.getInstance();
	// SimProp simProp = gcr.getValue(option);
	// if (simProp instanceof IntProp){
	// return ((IntProp) simProp).getMinValue();
	// }
	// return null;
	// }
	protected void setIntegerMaxValueOfOption(String option, Integer newMax) {
		SimPropRegistry gcr = SimPropRegistry.getInstance();
		SimProp simProp = gcr.getValue(option);
		if (simProp instanceof IntProp) {
			((IntProp) simProp).setMaxValue(newMax);
		} else {
			throw new RuntimeException(
					"Unable to setMaxValue on an Integer prop");
		}

		if ((this.getIntegerValueOfOption(option) > newMax)) {
			JOptionPane.showMessageDialog(null,
					"New Max Value is lower than current value. Please change value of option "
							+ option + " to a value lower or equal than"
							+ newMax, "Warning", JOptionPane.WARNING_MESSAGE);
			DependencyChecker.errorsInConfig = true;
		}
	}

	protected void setIntegerMinValueOfOption(String option, Integer newMin) {
		SimPropRegistry gcr = SimPropRegistry.getInstance();
		SimProp simProp = gcr.getValue(option);
		if (simProp instanceof IntProp) {
			((IntProp) simProp).setMinValue(newMin);
		} else {
			throw new RuntimeException(
					"Unable to setMinValue on an Integer prop");
		}

		if ((this.getIntegerValueOfOption(option) < newMin)) {
			JOptionPane.showMessageDialog(null,
					"New Min Value is greater than current value. Please change value of option "
							+ option + " to a higher or equal than" + newMin,
					"Warning", JOptionPane.WARNING_MESSAGE);
			DependencyChecker.errorsInConfig = true;
		}
	}
}
