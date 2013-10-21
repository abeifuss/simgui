package evaluation.simulator.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StringSimulationProperty {

	public String category() default "unknown";

	public String description() default "No Description available";

	// dependencies
	public Class<? extends Requirement>[] enable_requirements() default {};

	// general
	public String id() default "";

	public String name() default "";

	public String tooltip() default "No Tooltip available";

	// string
	public String value() default "";

	public Class<?> valueType() default Integer.class;

}