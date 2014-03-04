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
public @interface BoolSimulationProperty {
	// dependencies
	public Class<? extends Requirement>[] enable_requirements() default {};

	// general
//	public String id() default "";

	/**
	 * @return
	 * 		display name of the simprop
	 */
	public String name();

	/**
	 * @return
	 * 		the positon (within the plugin) of the simprop
	 */
	public int position() default 0;

	/**
	 * This string has to be unique
	 * 
	 * @return
	 * 		the key that identifies the simprop in the config file (e.g. BASIC_BATCH_BATCH_SIZE)
	 */
	public String key();

	/**
	 * @return
	 * 		the tooltip text
	 */
	public String tooltip() default "No Tooltip available";
	
	/**
	 * @return
	 * 		the info text
	 */
	public String info() default "";

	/**
	 * @return
	 * 		injection string
	 */
	public String inject() default "";

	/**
	 * @return
	 * 		the global flag
	 */
	public boolean global() default false;

	/**
	 * @return
	 * 		array of value requirements - see {@link Requirement}
	 */
	public Class<? extends Requirement>[] value_requirements() default {};

	/**
	 * @return
	 * 	the  static flag
	 */
	public boolean isStatic() default false;
	
	/**
	 * Returns true if the property is variable otherwise false
	 * @return
	 * 		property to vary flag
	 */
	public boolean property_to_vary() default true;
}