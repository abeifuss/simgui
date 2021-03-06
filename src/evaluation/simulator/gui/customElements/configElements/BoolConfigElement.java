package evaluation.simulator.gui.customElements.configElements;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import net.miginfocom.swing.MigLayout;
import evaluation.simulator.annotations.property.BoolProp;
import evaluation.simulator.gui.customElements.PluginPanel;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;

/**
 * implements the custom {@link Boolean} configurator for {@link PluginPanel}.
 * 
 * @author nachkonvention
 */
@SuppressWarnings("serial")
public class BoolConfigElement extends JPanel implements ItemListener, Observer {

	BoolProp property;
	JCheckBox checkbox;
	List<JTextArea> messages;
	Map<Component, Component> icons;

	SimPropRegistry simPropRegistry;

	/**
	 * @param boolProp
	 *            the property to configure
	 */
	public BoolConfigElement(BoolProp boolProp) {

		simPropRegistry = SimPropRegistry.getInstance();

		this.property = boolProp;
		simPropRegistry.registerGuiElement(this, property.getPropertyID());

		checkbox = new JCheckBox("enable");
		checkbox.addItemListener(this);
		checkbox.setToolTipText(property.getTooltip());
		checkbox.setSelected((boolean) simPropRegistry.getValue(property.getPropertyID()).getValue());

		this.messages = new LinkedList<JTextArea>();
		this.icons = new HashMap<Component, Component>();

		MigLayout migLayout = new MigLayout("", "[grow]", "");
		this.setLayout(migLayout);

		this.setBorder(BorderFactory.createTitledBorder(property.getName()));
		this.add(checkbox, "growx, push, wrap");

		if (!property.getInfo().equals("")) {
			JTextArea textarea = new JTextArea("Info: " + property.getInfo());
			textarea.setCaret(new DefaultCaret());
			textarea.setEditable(false);
			textarea.setLineWrap(true);
			textarea.setWrapStyleWord(true);
			textarea.setPreferredSize(new Dimension(10, 25));
			this.add(textarea, "growx, wmin 10, push, wrap");
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		simPropRegistry.setValue(this.property.getPropertyID(), this.checkbox.isSelected());
	}

	// Called when simprop has changed
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object o) {

		for (Component message : this.messages) {
			this.remove(message);
		}
		for (Component icon : this.icons.values()) {
			this.remove(icon);
		}

		this.messages.clear();
		this.icons.clear();

		this.checkbox.setSelected((boolean) simPropRegistry.getValue(property.getPropertyID()).getValue());

		if (property.getWarnings() != null && property.getWarnings().size() > 0) {
			for (String each : property.getWarnings()) {
				JTextArea text = new JTextArea(each);
				text.setCaret(new DefaultCaret());
				text.setBackground(new Color(250, 210, 115));
				text.setAutoscrolls(false);
				text.setEditable(false);
				text.setLineWrap(true);
				text.setWrapStyleWord(true);
				text.setPreferredSize(new Dimension(10, 25));
				this.messages.add(text);
				JLabel warning = new JLabel(new ImageIcon("etc/img/icons/warning/warning_16.png"));
				this.icons.put(text, warning);
			}
		}

		if (property.getErrors() != null && property.getErrors().size() > 0) {
			for (String each : property.getErrors()) {
				JTextArea text = new JTextArea(each);
				text.setCaret(new DefaultCaret());
				text.setBackground(new Color(250, 150, 135));
				text.setAutoscrolls(false);
				text.setEditable(false);
				text.setLineWrap(true);
				text.setWrapStyleWord(true);
				text.setPreferredSize(new Dimension(10, 25));
				this.messages.add(text);
				JLabel error = new JLabel(new ImageIcon("etc/img/icons/error/error_16.png"));
				this.icons.put(text, error);

			}
		}

		for (JTextArea message : this.messages) {
			DefaultCaret caret = (DefaultCaret) message.getCaret();
			caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
			message.setCaret(caret);
			message.setAutoscrolls(false);
			this.add(this.icons.get(message), "push, wmin 16, wrap");
			this.add(message, "growx, push, wmin 10");
		}

		updateUI();
	}

}
