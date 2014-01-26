package evaluation.simulator.annotations.property;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IntSimulationProperty {

	// dependencies
	public Class<? extends Requirement>[] enable_requirements() default {};

	// general
//	public String id() default "";

	public int max() default Integer.MAX_VALUE;

	public int min() default Integer.MIN_VALUE;

	public String name() default "";

	public int position() default 0;

	public String key() default "";

	public String tooltip() default "No Tooltip available";
	
	public String info() default "";
	
	public String inject() default "";

	public boolean global() default false;

	public Class<? extends Requirement>[] value_requirements() default {};

	public boolean isStatic() default false;
	
	public boolean enableAuto() default false;
	
	public boolean enableUnlimited() default false;
	
	public int stepSize() default 1;

	public String guiElement() default "spinner";
	
	public boolean property_to_vary() default false;

}