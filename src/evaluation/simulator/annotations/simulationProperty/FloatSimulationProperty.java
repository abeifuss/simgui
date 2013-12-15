package evaluation.simulator.annotations.simulationProperty;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FloatSimulationProperty {
	
	// dependencies
	public Class<? extends Requirement>[] enable_requirements() default {};

	// general
//	public String id() default "";

	public float max() default Float.MAX_VALUE;

	public float min() default Float.NEGATIVE_INFINITY;

	public String name() default "";

	public int position() default 0;

	public String key() default "";

	public String tooltip() default "No Tooltip available";
	
	public String info() default "";

	public String inject() default "";

	public boolean global() default false;

	// float
	public float value() default 0.0f;

	public Class<?> valueType() default Float.class;

	public Class<? extends Requirement>[] value_requirements() default {};

	public boolean isStatic() default false;
	
	public boolean enableAuto() default false;
	
	public boolean enableUnlimited() default false;
	
	public float stepSize() default 0.1f;
	
	public String guiElement() default "spinner";

}