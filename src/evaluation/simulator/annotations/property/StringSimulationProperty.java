package evaluation.simulator.annotations.property;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StringSimulationProperty {

	// dependencies
	public Class<? extends Requirement>[] enable_requirements() default {};

	// general

	public String name() default "";

	public int position() default 0;

	public String possibleValues() default "";
	
	public boolean multiSelection() default false;

	public String key() default "";

	public String tooltip() default "No Tooltip available";
	
	public String info() default "";

	public String inject() default "";

	public boolean global() default false;

	public Class<? extends Requirement>[] value_requirements() default {};

	public boolean isStatic() default false;
	
	public String regex() default "";
	
	public boolean property_to_vary() default false;

}