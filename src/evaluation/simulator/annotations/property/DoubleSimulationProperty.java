package evaluation.simulator.annotations.property;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation
 * 
 * @author alex
 *
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DoubleSimulationProperty {
	
	// dependencies
	public Class<? extends Requirement>[] enable_requirements() default {};

	// general
//	public String id() default "";

	/**
	 * @return
	 * 		maximum value
	 */
	public double max() default Double.MAX_VALUE;

	/**
	 * @return
	 * 		minimum value
	 */
	public double min() default Double.NEGATIVE_INFINITY;

	/**
	 * @return
	 * 		property name
	 */
	public String name() default "";

	/**
	 * @return
	 * 		property position
	 */
	public int position() default 0;

	/**
	 * This string has to be unique
	 * 
	 * @return
	 * 		the key that identifies the simprop in the config file (e.g. BASIC_BATCH_BATCH_SIZE)
	 */
	public String key() default "";

	/**
	 * @return
	 * 		tooltip text
	 */
	public String tooltip() default "No Tooltip available";
	
	/**
	 * @return
	 * 		info text
	 */
	public String info() default "";

	/**
	 * @return
	 * 		injection string
	 */
	public String inject() default "";

	/**
	 * @return
	 * 		global flag
	 */
	public boolean global() default false;

	/**
	 * @return
	 * 		array of value requirements - see {@link Requirement}
	 */
	public Class<? extends Requirement>[] value_requirements() default {};

	/**
	 * @return
	 * 		static flag
	 */
	public boolean isStatic() default false;
	
	/**
	 * @return
	 * 		auto checkbox flag
	 */
	public boolean enableAuto() default false;
	
	/**
	 * @return
	 * 		unlimited checkbox flag
	 */
	public boolean enableUnlimited() default false;
	
	/**
	 * Needed for gui element
	 * @return
	 * 		step size (for spinner)
	 */
	public double stepSize() default 0.001f;

	/**
	 * @return
	 * 		identifying string of the gui element to manipulate the simprop
	 */
	public String guiElement() default "spinner";
	
	/**
	 * Returns true if the property is variable otherwise false
	 * @return
	 * 		property to vary flag
	 */
	public boolean property_to_vary() default true;

}