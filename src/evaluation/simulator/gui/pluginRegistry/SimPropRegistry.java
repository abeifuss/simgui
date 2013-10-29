package evaluation.simulator.gui.pluginRegistry;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;

import com.google.classpath.ClassPath;
import com.google.classpath.ClassPathFactory;
import com.google.classpath.RegExpResourceFilter;

import evaluation.simulator.annotations.plugin.AnnotatedPlugin;
import evaluation.simulator.annotations.plugin.PluginAnnotation;
import evaluation.simulator.annotations.simulationProperty.BoolProp;
import evaluation.simulator.annotations.simulationProperty.BoolSimulationProperty;
import evaluation.simulator.annotations.simulationProperty.DoubleProp;
import evaluation.simulator.annotations.simulationProperty.DoubleSimulationProperty;
import evaluation.simulator.annotations.simulationProperty.FloatProp;
import evaluation.simulator.annotations.simulationProperty.FloatSimulationProperty;
import evaluation.simulator.annotations.simulationProperty.IntProp;
import evaluation.simulator.annotations.simulationProperty.IntSimulationProperty;
import evaluation.simulator.annotations.simulationProperty.SimProp;
import evaluation.simulator.annotations.simulationProperty.StringProp;
import evaluation.simulator.annotations.simulationProperty.StringSimulationProperty;
import evaluation.simulator.gui.customElements.accordion.ListEntry;
import evaluation.simulator.log.LogLevel;
import evaluation.simulator.log.Logger;

public class SimPropRegistry {

	private static SimPropRegistry _instance = null;

	public static SimPropRegistry getInstance() {
		if (_instance == null) {
			_instance = new SimPropRegistry();
		}
		return _instance;
	}

	@SuppressWarnings("unchecked")
	private final Map<String, String>[] pluginLayerMap = new HashMap[7];

	private final Map<String, SimProp> properties = new HashMap<String, SimProp>();

	private SimPropRegistry() {
		this.scan();
		this.scanPlugins();
	}

	public void dumpConfiguration() {

	}

	public Set<Entry<String, SimProp>> getAllSimProps() {
		return this.properties.entrySet();
	}

	public List<Entry<String, String>> getCategoryItems(String category) {

		List<Entry<String, String>> hm = new LinkedList<Entry<String, String>>();

		Iterator<Entry<String, SimProp>> iter = this.properties.entrySet()
				.iterator();
		while (iter.hasNext()) {
			SimProp entry = iter.next().getValue();
			if (category.equals(entry.getPluginLayer())) {
				hm.add(new ListEntry<String, String>(entry.getId(), entry
						.getName()));
			}
		}

		return hm;
	}

	public Map<String, String>[] getPlugIns() {
		return this.pluginLayerMap;
	}

	public SimProp getValue(String key) {

		return this.properties.get(key);
	}

	public void register(SimProp s) {
		System.out.println("Register: " + s.getId());
		if (this.properties.containsKey(s.getId())) {

			GraphicsDevice gd = GraphicsEnvironment
					.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			int x = gd.getDisplayMode().getWidth();
			int y = gd.getDisplayMode().getHeight();

			JOptionPane alert = new JOptionPane("Redefinition of " + s.getId()
					+ " (" + s.getPlugin() + ") detected!");

			JDialog d = alert.createDialog(null, "");
			int w = d.getWidth();
			int h = d.getHeight();
			d.setLocation((x / 2) - (w / 2), (y / 2) - (h / 2));
			d.setVisible(true);

		} else {
			this.properties.put(s.getId(), s);
		}
	}

	// Scans the simulation properties
	@SuppressWarnings("unchecked")
	public void scan() {

		SimProp property;
		AnnotatedPlugin plugin;

		Reflections reflectionsPlugins = new Reflections(
				ClasspathHelper.forPackage("evaluation.simulator.plugins"),
				new TypeAnnotationsScanner());

		Set<Class<?>> types = reflectionsPlugins
				.getTypesAnnotatedWith(evaluation.simulator.annotations.plugin.PluginAnnotation.class);

		for (Class<?> c : types) {
			PluginAnnotation pa = c.getAnnotation(PluginAnnotation.class);

			plugin = new AnnotatedPlugin();
			plugin.setId(c.getName());
			plugin.setName(pa.name());
			plugin.setDocumentationURL(pa.documentationURL());

			String plugInLayer = (plugin.getId()).split("\\.", 5)[3];

			try {
				for (Field feld : Class.forName(c.getCanonicalName())
						.getDeclaredFields()) {
					// System.out.println(c.getCanonicalName()+":" +
					// feld.getName());
					Annotation[] a = feld.getAnnotations();
					for (Annotation element : a) {
						// System.out.println("Annotations " + a[k]);
						if (element.annotationType() == BoolSimulationProperty.class) {
							BoolSimulationProperty annotation = feld
									.getAnnotation(BoolSimulationProperty.class);
							property = new BoolProp();
							if (annotation != null) {
								// property.setId(f.getName());
								property.setId(plugin.getName() + "::"
										+ annotation.propertykey());
								property.setNamespace(plugin.getName());
								property.setName(annotation.name());
								property.setDescription(annotation
										.description());
								property.setTooltip(annotation.tooltip());
								property.setPluginLayer(plugInLayer);
								property.setEnable_requirements(annotation
										.enable_requirements());
								((BoolProp) property).setValue(annotation
										.value());
								property.setEnable(true);
							}
							this.register(property);
						} else if (element.annotationType() == IntSimulationProperty.class) {
							IntSimulationProperty annotation = feld
									.getAnnotation(IntSimulationProperty.class);
							property = new IntProp();
							if (annotation != null) {
								property.setId(plugin.getName() + "::"
										+ annotation.propertykey());
								property.setNamespace(plugin.getName());
								property.setName(annotation.name());
								property.setDescription(annotation
										.description());
								property.setTooltip(annotation.tooltip());
								property.setPluginLayer(plugInLayer);
								property.setEnable_requirements(annotation
										.enable_requirements());
								property.setValue_requirements(annotation
										.value_requirements());
								((IntProp) property).setMinValue(annotation
										.min());
								((IntProp) property).setMaxValue(annotation
										.max());
								property.setValue(annotation.value());
								property.setEnable(true);
							}
							this.register(property);
						} else if (element.annotationType() == FloatSimulationProperty.class) {
							FloatSimulationProperty annotation = feld
									.getAnnotation(FloatSimulationProperty.class);
							property = new FloatProp();
							if (annotation != null) {
								property.setId(plugin.getName() + "::"
										+ annotation.propertykey());
								property.setNamespace(plugin.getName());
								property.setName(annotation.name());
								property.setDescription(annotation
										.description());
								property.setTooltip(annotation.tooltip());
								property.setPluginLayer(plugInLayer);
								property.setEnable_requirements(annotation
										.enable_requirements());
								((FloatProp) property).setMinValue(annotation
										.min());
								((FloatProp) property).setMaxValue(annotation
										.max());
								((FloatProp) property).setValue(annotation
										.value());
								property.setEnable(true);
							}
							this.register(property);
						} else if (element.annotationType() == DoubleSimulationProperty.class) {
							DoubleSimulationProperty annotation = feld
									.getAnnotation(DoubleSimulationProperty.class);
							property = new DoubleProp();
							if (annotation != null) {
								property.setId(plugin.getName() + "::"
										+ annotation.propertykey());
								property.setNamespace(plugin.getName());
								property.setName(annotation.name());
								property.setDescription(annotation
										.description());
								property.setTooltip(annotation.tooltip());
								property.setPluginLayer(plugInLayer);
								property.setEnable_requirements(annotation
										.enable_requirements());
								((DoubleProp) property).setMinValue(annotation
										.min());
								((DoubleProp) property).setMaxValue(annotation
										.max());
								((DoubleProp) property).setValue(annotation
										.value());
								property.setEnable(true);
							}
							this.register(property);
						} else if (element.annotationType() == StringSimulationProperty.class) {
							StringSimulationProperty annotation = feld
									.getAnnotation(StringSimulationProperty.class);
							property = new StringProp();
							if (annotation != null) {
								property.setId(plugin.getName() + "::"
										+ annotation.propertykey());
								property.setNamespace(plugin.getName());
								property.setName(annotation.name());
								property.setDescription(annotation
										.description());
								property.setTooltip(annotation.tooltip());
								property.setPluginLayer(plugInLayer);
								property.setEnable_requirements(annotation
										.enable_requirements());
								((StringProp) property).setValue(annotation
										.value());
								((StringProp) property)
										.setPossibleValues(annotation
												.possibleValues());
								property.setEnable(true);
							}
							this.register(property);
						} else {
							Logger.Log(LogLevel.ERROR,
									"GuiConfigRegistry - bad type");
							continue;
						}
					}
				}
			} catch (SecurityException | ClassNotFoundException e) {
				e.printStackTrace();
			}

		}

		// call initial dependency-check for per plugin configurations
		DependencyChecker.checkAll(this);
	}

	public void scanPlugins() {
		
		for (int i = 0; i < 7; i++) {
			this.pluginLayerMap[i] = new HashMap<String, String>();
		}
		
		String[] plugInLayer = { "clientSendStyle", "delayBox", "mixSendStyle",
				"outputStrategy", "plotType", "topology", "trafficSource" };

		System.err.println( this.properties.size() );
		
		for ( String key : properties.keySet() ) {
			SimProp simProp = properties.get(key);
			String layer = simProp.getPluginLayer();
			String name = simProp.getNamespace();
			System.err.println( "LAYER: " + layer + " NAME: " + name );
			if ( layer.equals(plugInLayer[0]) ){
				this.pluginLayerMap[0].put( name, layer );
			}else if ( layer.equals(plugInLayer[1]) ){
				this.pluginLayerMap[1].put( name, layer );
			}else if ( layer.equals(plugInLayer[2]) ){
				this.pluginLayerMap[2].put( name, layer );
			}else if ( layer.equals(plugInLayer[3]) ){
				this.pluginLayerMap[3].put( name, layer );
			}else if ( layer.equals(plugInLayer[4]) ){
				this.pluginLayerMap[4].put( name, layer );
			}else if ( layer.equals(plugInLayer[5]) ){
				this.pluginLayerMap[5].put( name, layer );
			}else if ( layer.equals(plugInLayer[6]) ){
				this.pluginLayerMap[6].put( name, layer );
			}else {
				System.err.println( "No such plugin layer: " + layer );
			}
		}
	}

	public void setValue(String key, Object arg0) {

		if (arg0.getClass() == Boolean.class) {
			System.out.println("Boolean");
			this.properties.get(key).setValue(arg0);
		} else if (arg0.getClass() == Float.class) {
			System.out.println("Float");
			this.properties.get(key).setValue(arg0);
		} else if (arg0.getClass() == Double.class) {
			System.out.println("Double");
			this.properties.get(key).setValue(arg0);
		}else if (arg0.getClass() == Integer.class) {
			System.out.println("Integer");
			this.properties.get(key).setValue(arg0);
		} else if (arg0.getClass() == String.class) {
			System.out.println("String");
			this.properties.get(key).setValue(arg0);

		} else {

		}

	}
}
