package evaluation.simulator.annotations.property;

import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;

/**
 * abstract class for all Requirements
 * @author hans
 *
 */
public abstract class Requirement {

	/**
	 * checks if an defined Requirement is met
	 * 
	 * @return true: Requirement is met, false if not
	 */
	public boolean check() {
		throw new RuntimeException("Please implement!");
	}

	/**
	 * checks if the key is equal to the overgiven value
	 * @param key
	 * @param value
	 * @return
	 */
	protected boolean equals(String key, String value) {
		SimPropRegistry gcr = SimPropRegistry.getInstance();
		return (gcr.getValue(key).getValue() + "").equals(value);
	}

	/**
	 * extracts and Integer value out of an option
	 * @param option
	 * @return parsed value
	 */
	protected Integer getIntegerValueOfOption(String option) {
		SimPropRegistry gcr = SimPropRegistry.getInstance();
		Integer i = Integer
				.parseInt(gcr.getValue(option).getValue().toString());
		return i;
	}

	/**
	 * Sets the max value of an Integer property
	 * @param key name of the property
	 * @param newMax new max Value
	 */
	protected void setIntegerMaxValueOfOption(String key, Integer newMax) {
		SimPropRegistry gcr = SimPropRegistry.getInstance();
		SimProp simProp = gcr.getValue(key);
		if (simProp instanceof IntProp) {
			((IntProp) simProp).setMaxValue(newMax);
		} else {
			throw new RuntimeException(
					"Unable to setMaxValue on an Integer prop");
		}
	}

	/**
	 * Set the min value of an Integer property
	 * @param key name of the property
	 * @param newMin new min Value
	 */
	protected void setIntegerMinValueOfOption(String key, Integer newMin) {
		SimPropRegistry gcr = SimPropRegistry.getInstance();
		SimProp simProp = gcr.getValue(key);
		if (simProp instanceof IntProp) {
			((IntProp) simProp).setMinValue(newMin);
		} else {
			throw new RuntimeException(
					"Unable to setMinValue on an Integer prop");
		}
	}
}
