package evaluation.simulator.annotations.plugin;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PluginSuperclass {

	public String documentationURL() default "";

	public String layerKey();
	
	public String layerName();

	public String fakePlugins() default "";

	public int position() default 100;

}