package evaluation.simulator.annotations.simulationProperty;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IntSimulationProperty {

	public String description() default "No Description available";

	// dependencies
	public Class<? extends Requirement>[] enable_requirements() default {};

	// general
	public String id() default "";

	public int max() default Integer.MAX_VALUE;

	public int min() default Integer.MIN_VALUE;

	public String name() default "";

	public int order() default 0;

	public String propertykey() default "";

	public String tooltip() default "No Tooltip available";
	
	public String inject() default "";
	
	public boolean global() default false;

	// int
	public int value() default 0;

	public Class<? extends Requirement>[] value_requirements() default {};

	public Class<?> valueType() default Integer.class;

}