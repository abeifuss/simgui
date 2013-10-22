package gui.pluginRegistry;

import gui.customElements.accordion.ListEntry;

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

import log.LogLevel;
import log.Logger;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import plugIns.outputStrategy.ConstantRate;

import annotations.plugin.AnnotatedPlugin;
import annotations.plugin.PluginAnnotation;
import annotations.simulationProperty.BoolProp;
import annotations.simulationProperty.BoolSimulationProperty;
import annotations.simulationProperty.FloatProp;
import annotations.simulationProperty.FloatSimulationProperty;
import annotations.simulationProperty.IntProp;
import annotations.simulationProperty.IntSimulationProperty;
import annotations.simulationProperty.SimProp;
import annotations.simulationProperty.StringProp;
import annotations.simulationProperty.StringSimulationProperty;

import com.google.classpath.ClassPath;
import com.google.classpath.ClassPathFactory;
import com.google.classpath.RegExpResourceFilter;
import com.google.common.collect.Sets;

public class SimPropRegistry {

	private Map<String, SimProp> _properties = new HashMap<String, SimProp>();

	@SuppressWarnings("unchecked")
	private Map<String, String>[] _pluginLayerMap = new HashMap[6];

	private static SimPropRegistry _instance = null;

	private SimPropRegistry() {
		scanPlugins();
	}

	public static SimPropRegistry getInstance() {
		if (_instance == null) {
			_instance = new SimPropRegistry();
		}
		return _instance;
	}

	// Scans the simulation properties
	@SuppressWarnings("unchecked")
	public void scan() {

		SimProp property;
		AnnotatedPlugin plugin;

		Reflections reflectionsPlugins = new Reflections(
				ClasspathHelper.forPackage("gui.layout"),
				new TypeAnnotationsScanner());

		Set<Class<?>> types = reflectionsPlugins
				.getTypesAnnotatedWith(annotations.plugin.PluginAnnotation.class);

		for (Class<?> c : types) {
			PluginAnnotation pa = c.getAnnotation(PluginAnnotation.class);

			plugin = new AnnotatedPlugin();
			plugin.setId(c.getName());
			plugin.setName(pa.name());
			plugin.setDocumentationURL(pa.documentationURL());
			// System.out.println(plugin.getId() + "," + plugin.getName() + ","
			// + plugin.getDocumentationURL());

			String category = (plugin.getId()).split("\\.", 3)[1];

			// FIXME: Class<? extends Annotation> does not work
			Class<?>[] annotations = {
					annotations.simulationProperty.BoolSimulationProperty.class,
					annotations.simulationProperty.FloatSimulationProperty.class,
					annotations.simulationProperty.IntSimulationProperty.class,
					annotations.simulationProperty.StringSimulationProperty.class };

			try {
				for (Field feld : Class.forName(c.getCanonicalName())
						.getDeclaredFields()) {
					// System.out.println(c.getCanonicalName()+":" +
					// feld.getName());
					Annotation[] a = feld.getAnnotations();
					for (int k = 0; k < a.length; k++) {
						// System.out.println("Annotations " + a[k]);
						if (a[k].annotationType() == BoolSimulationProperty.class) {
							BoolSimulationProperty annotation = feld.getAnnotation(BoolSimulationProperty.class);
							property = new BoolProp();
							if (annotation != null) {
								// property.setId(f.getName());
								property.setId(plugin.getName() + "::"+ annotation.propertykey());
								property.setNamespace(plugin.getName());
								property.setName(annotation.name());
								property.setDescription(annotation.description());
								property.setTooltip(annotation.tooltip());
								property.setPluginLayer(category);
								property.setEnable_requirements(annotation.enable_requirements());
								((BoolProp) property).setValue(annotation.value());
								property.setEnable(true);
							}
							register(property);
						} else if (a[k].annotationType() == IntSimulationProperty.class) {
							IntSimulationProperty annotation = feld.getAnnotation(IntSimulationProperty.class);
							property = new IntProp();
							if (annotation != null) {
								property.setId(plugin.getName() + "::"+ annotation.propertykey());
								property.setNamespace(plugin.getName());
								property.setName(annotation.name());
								property.setDescription(annotation.description());
								property.setTooltip(annotation.tooltip());
								property.setPluginLayer(category);
								property.setEnable_requirements(annotation.enable_requirements());
								property.setValue_requirements(annotation.value_requirements());
								((IntProp) property).setMinValue(annotation.min());
								((IntProp) property).setMaxValue(annotation.max());
								property.setValue(annotation.value());
								property.setEnable(true);
							}
							register(property);
						} else if (a[k].annotationType() == FloatSimulationProperty.class) {
							FloatSimulationProperty annotation = feld.getAnnotation(FloatSimulationProperty.class);
							property = new FloatProp();
							if (annotation != null) {
								property.setId(plugin.getName() + "::"+ annotation.propertykey());
								property.setNamespace(plugin.getName());
								property.setName(annotation.name());
								property.setDescription(annotation.description());
								property.setTooltip(annotation.tooltip());
								property.setPluginLayer(category);
								property.setEnable_requirements(annotation.enable_requirements());
								((FloatProp) property).setMinValue(annotation.min());
								((FloatProp) property).setMaxValue(annotation.max());
								((FloatProp) property).setValue(annotation.value());
								property.setEnable(true);
							}
							register(property);
						} else if (a[k].annotationType() == StringSimulationProperty.class) {
							StringSimulationProperty annotation = feld.getAnnotation(StringSimulationProperty.class);
							property = new StringProp();
							if (annotation != null) {
								property.setId(plugin.getName() + "::"+ annotation.propertykey());
								property.setNamespace(plugin.getName());
								property.setName(annotation.name());
								property.setDescription(annotation.description());
								property.setTooltip(annotation.tooltip());
								property.setPluginLayer(category);
								property.setEnable_requirements(annotation.enable_requirements());
								((StringProp) property).setValue(annotation.value());
								((StringProp) property).setPossibleValues(annotation.possibleValues());
								property.setEnable(true);
							}
							register(property);
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

	public void register(SimProp s) {
		System.out.println("Register: " + s.getId());
		if (_properties.containsKey(s.getId())) {

			GraphicsDevice gd = GraphicsEnvironment
					.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			int x = gd.getDisplayMode().getWidth();
			int y = gd.getDisplayMode().getHeight();

			JOptionPane alert = new JOptionPane("Redefinition of " + s.getId()
					+ " (" + s.getPlugin() + ") detected!");

			JDialog d = alert.createDialog(null, "");
			int w = d.getWidth();
			int h = d.getHeight();
			d.setLocation(x / 2 - w / 2, y / 2 - h / 2);
			d.setVisible(true);

		} else {
			_properties.put(s.getId(), s);
		}
	}

	public Set<Entry<String, SimProp>> getAllSimProps() {
		return _properties.entrySet();
	}

	public SimProp getValue(String key) {

		return _properties.get(key);
	}

	public void setValue(String key, Object arg0) {

		if (arg0.getClass() == Boolean.class) {
			System.out.println("Integer");
			_properties.get(key).setValue((Boolean) arg0);

		} else if (arg0.getClass() == Float.class) {
			System.out.println("Integer");
			_properties.get(key).setValue((Float) arg0);

		} else if (arg0.getClass() == Integer.class) {
			System.out.println("Integer");
			_properties.get(key).setValue((Integer) arg0);

		} else if (arg0.getClass() == String.class) {
			System.out.println("Integer");
			_properties.get(key).setValue((String) arg0);

		} else {

		}

	}

	public List<Entry<String, String>> getCategoryItems(String category) {

		List<Entry<String, String>> hm = new LinkedList<Entry<String, String>>();

		Iterator<Entry<String, SimProp>> iter = _properties.entrySet()
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

	public void scanPlugins() {
		ClassPath classpath = new ClassPathFactory().createFromJVM();
		RegExpResourceFilter regExpResourceFilter = new RegExpResourceFilter(
				".*", ".*\\.class");
		String[] resources = classpath.findResources("", regExpResourceFilter);

		String[] plugInLayer = { "clientSendStyle", "delayBox", "mixSendStyle",
				"outputStrategy", "plotType", "trafficSource" };

		for (int i = 0; i < 6; i++) {

			// Pattern p = Pattern.compile("^plugIns/" + plugInLayer[i] +
			// "/.+/.+");
			Pattern p = Pattern.compile("^evaluation/simulator/plugins/"
					+ plugInLayer[i] + "/.+");
			_pluginLayerMap[i] = new HashMap<String, String>();

			for (final String r : resources) {

				if (p.matcher(r).matches()) {
					String[] splitString = r.split("/");

					if (splitString.length != 5) {
						Logger.Log(log.LogLevel.ERROR, "Length != 5");
						System.exit(1);
					}

					String plugInName = splitString[4];
					String plugInPath = splitString[0] + "/" + splitString[1]
							+ "/" + splitString[2] + "/" + splitString[3] + "/";

					_pluginLayerMap[i].put(plugInName, plugInPath);

					// Iterator<Entry<String, String>> iter =
					// _pluginLayerMap[i].entrySet().iterator();
					//
					// while (iter.hasNext()) {
					// Entry<String, String> entry = iter.next();
					// Logger.Log(
					// log.LogLevel.DEBUG,
					// "Name: " + entry.getKey() + "; Path: "
					// + entry.getValue());
					// }

					continue;
				}
			}
		}
	}

	public Map<String, String>[] getPlugIns() {
		return _pluginLayerMap;
	}

	public void dumpConfiguration() {

	}
}
