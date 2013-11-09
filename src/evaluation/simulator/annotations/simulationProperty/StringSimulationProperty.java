package evaluation.simulator.annotations.simulationProperty;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StringSimulationProperty {

	public String description() default "No Description available";

	// dependencies
	public Class<? extends Requirement>[] enable_requirements() default {};

	// general
	public String id() default "";

	public String name() default "";

	public int order() default 0;

	public String possibleValues() default "";

	public String propertykey() default "";

	public String tooltip() default "No Tooltip available";
	
	public String inject() default "";
	
	public boolean global() default false;

	// string
	public String value() default "";

	public Class<?> valueType() default String.class;

}