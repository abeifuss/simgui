package annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IntSimulationProperty {

	// general
	public String id() default "";
	public String name() default "";
	public String description() default "No Description available";
	public String tooltip() default "No Tooltip available";
	public String category() default "unknown";
	
	public Class<?> valueType() default Integer.class;
	
	// int
	public int value() default 0;
	public int min() default 0;
	public int max() default 1;
	
	// dependencies
	public Class<? extends Requirement>[] enable_requirements() default {};
	public Class<? extends Requirement>[] value_requirements() default {};
	
}