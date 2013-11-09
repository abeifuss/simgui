package evaluation.simulator.annotations.plugin;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Plugin {

	public String documentationURL() default "";

	public String name() default "";
	
	public String pluginLayer() default "";

	public boolean vilible() default true;

	public boolean allowFieldsGlobal() default false;
}
