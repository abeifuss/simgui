package annotations.simulationProperty;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BoolSimulationProperty {

	// general
	public String id() default "";
	public String name() default "";
	public String description() default "No Description available";
	public String tooltip() default "No Tooltip available";
	public int order() default 0;
	public String propertykey() default "";
	
	public Class<?> valueType() default Boolean.class;
	
	// bool
	public boolean value() default true;
	
	// dependencies
	public Class<? extends Requirement>[] enable_requirements() default {};
	
}