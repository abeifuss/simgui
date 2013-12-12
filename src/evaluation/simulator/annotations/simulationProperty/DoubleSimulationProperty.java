package evaluation.simulator.annotations.simulationProperty;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DoubleSimulationProperty {
	
	// dependencies
	public Class<? extends Requirement>[] enable_requirements() default {};

	// general
//	public String id() default "";

	public double max() default Double.MAX_VALUE;

	public double min() default Double.NEGATIVE_INFINITY;

	public String name() default "";

	public int order() default 0;

	public String propertykey() default "";

	public String tooltip() default "No Tooltip available";
	
	public String info() default "";

	public String inject() default "";

	public boolean global() default false;

	// float
	public double value() default 0.0f;

	public Class<?> valueType() default Double.class;

	public Class<? extends Requirement>[] value_requirements() default {};

	public boolean isStatic() default false;
	
	public boolean enableAuto() default false;
	
	public boolean enableUnlimited() default false;
	
	public double stepSize() default 0.001f;

	public String guiElement() default "spinner";

}