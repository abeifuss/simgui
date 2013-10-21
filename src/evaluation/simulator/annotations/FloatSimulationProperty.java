package evaluation.simulator.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FloatSimulationProperty {

	public String category() default "unknown";

	public String description() default "No Description available";

	// dependencies
	public Class<? extends Requirement>[] enable_requirements() default {};

	// general
	public String id() default "";

	public float max() default 1.0f;

	public float min() default 0.0f;

	public String name() default "";

	public String tooltip() default "No Tooltip available";

	// float
	public float value() default 0.0f;

	public Class<?> valueType() default Integer.class;

}