package evaluation.simulator.annotations.property;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BoolSimulationProperty {
	// dependencies
	public Class<? extends Requirement>[] enable_requirements() default {};

	// general
//	public String id() default "";

	public String name();

	public int position() default 0;

	public String key();

	public String tooltip() default "No Tooltip available";
	
	public String info() default "";

	public String inject() default "";

	public boolean global() default false;
	// bool
	public boolean value() default true;

	// public Class<?> valueType() default Boolean.class;

	public Class<? extends Requirement>[] value_requirements() default {};

	public boolean isStatic() default false;
}