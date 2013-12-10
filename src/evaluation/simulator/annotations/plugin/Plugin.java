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

	public String documentationURL() default "dummyPlugin.html";

	public String pluginName() default "";
	
	public String pluginKey();	

	public String pluginLayerKey() default "";

	public boolean visible() default true;

	public boolean global() default false;

	public boolean allowGlobalFields() default true;
	
}
