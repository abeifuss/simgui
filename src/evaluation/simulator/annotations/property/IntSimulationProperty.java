package evaluation.simulator.annotations.property;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author alex
 *
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IntSimulationProperty {

	// dependencies
	public Class<? extends Requirement>[] enable_requirements() default {};

	// general
//	public String id() default "";

	/**
	 * @return
	 * 		maximum value
	 */
	public int max() default Integer.MAX_VALUE;

	/**
	 * @return
	 * 		minimum value
	 */
	public int min() default Integer.MIN_VALUE;

	/**
	 * @return
	 * 		simprop display name
	 */
	public String name() default "";

	/**
	 * @return
	 * 		simprop position (gui) within the plugin
	 */
	public int position() default 0;

	/**
	 * @return
	 * 		simprop config name
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
	 * 		auto flag
	 */
	public boolean enableAuto() default false;
	
	/**
	 * @return
	 * 		unlimited flag
	 */
	public boolean enableUnlimited() default false;
	
	/**
	 * @return
	 * 		spinner step size
	 */
	public int stepSize() default 1;

	/**
	 * @param guiElement
	 * 		gui element identifying string ("spinner" or "slider")
	 */
	public String guiElement() default "spinner";
	
	/**
	 * Returns true if the property is variable otherwise false
	 * @return
	 * 		property to vary flag
	 */
	public boolean property_to_vary() default false;

}