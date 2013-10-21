package framework.core.gui.userInterfaces.genericXMLGeneratedGUI.genericXMLGUIBranch;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import framework.core.gui.model.XMLPart;
import framework.core.gui.model.XMLPartGUIContainer;
import framework.core.gui.model.XMLResource;
import framework.core.gui.util.visualUtilities.ImageLoader;

/**
 * MultipleSetting is a JPanel for one setting that can have multiple values 
 * @author Marius Fink
 * @version 11.08.2012
 */
public class MultipleSetting extends JPanel implements XMLPartGUIContainer
{

    private static final long serialVersionUID = -2811423589767027384L;
    private XMLResource xml;
    private JPanel linesPanel;
    private List<MultipleSettingsLine> allMultipleSettingsLines = new LinkedList<MultipleSettingsLine>();
    private List<MultipleSettingsLine> selectedMultipleSettingsLines = new LinkedList<MultipleSettingsLine>();
    private String tempPrefix;

    /**
     * Creates a multiple setting panel that extracts the gui values by itself
     * from the xml file.
     * 
     * @param xml
     *            the XML-Settings-Container
     * @param multipleIndex
     *            the index of this element
     */
    public MultipleSetting(XMLResource xml, int mainBranchIndex, int multipleIndex) {
	this.xml = xml;
	
	tempPrefix = "mainBranch[" + mainBranchIndex + "]/settingsGroups/multipleSetting[" + multipleIndex + "]/";
	this.xml.setTemporaryPrefix(tempPrefix);
	init();
	this.xml.resetTemporaryPrefix();
    }
    
    /**
     * Initializes this component.
     */
    public void init() {
	setLayout(new BorderLayout());
	String title = xml.getPropertyAsString("name");
	setBorder(BorderFactory.createTitledBorder(title));
	
	JPanel wrapperPanel1 = new JPanel(new BorderLayout());
	linesPanel = new JPanel();
	linesPanel.setLayout(new BoxLayout(linesPanel, BoxLayout.Y_AXIS));
	
	//generate lines
	addLine();
	
	wrapperPanel1.add(linesPanel, BorderLayout.NORTH);
	JScrollPane scrollPanel = new JScrollPane(wrapperPanel1);
	scrollPanel.setSize(new Dimension(scrollPanel.getSize().width, 80));
	scrollPanel.setPreferredSize(new Dimension(scrollPanel.getSize().width, 80));
	scrollPanel.setMinimumSize(new Dimension(scrollPanel.getSize().width, 80));
	scrollPanel.setMaximumSize(new Dimension(scrollPanel.getSize().width, 80));
	scrollPanel.setBorder(null);
	add(scrollPanel, BorderLayout.CENTER);
	add(generateActionPanel(), BorderLayout.SOUTH);
    }

    /**
     * Adds a blank line - the xml has to be set already to the multipleSetting element
     * @return the created line
     */
    private MultipleSettingsLine addLine() {
	xml.setTemporaryPrefix(tempPrefix);
	final MultipleSettingsLine line = new MultipleSettingsLine(xml, "");
	xml.resetTemporaryPrefix();
	
	allMultipleSettingsLines.add(line);
	line.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		if (line.isSelected()) {
		    selectedMultipleSettingsLines.add(line);
		} else {
		    selectedMultipleSettingsLines.remove(line);
		}
	    }
	});
	linesPanel.add(line);
	return line;
    }

    /**
     * @return a panel with the delete and add buttons
     */
    private Component generateActionPanel() {
	JPanel panel = new JPanel(new BorderLayout());
	JPanel deletePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	
	JButton addBtn = new JButton(ImageLoader.loadIcon("add"));
	addBtn.setMargin(new Insets(1, 1, 1, 1));
	addBtn.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		addLine();
		updateUI();
	    }
	});
	JButton deleteBtn = new JButton(ImageLoader.loadIcon("delete"));
	deleteBtn.setMargin(new Insets(1,1,1,1));
	deleteBtn.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		for (MultipleSettingsLine line: selectedMultipleSettingsLines) {
		    linesPanel.remove(line);
		    allMultipleSettingsLines.remove(line);
		}
		
		updateUI();
		selectedMultipleSettingsLines.clear();
	    }
	});
	
	deletePanel.add(deleteBtn);
	
	addPanel.add(addBtn);

	panel.add(deletePanel, BorderLayout.WEST);
	panel.add(addPanel, BorderLayout.EAST);
	
	panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
	return panel;
    }
    
    private void removeAllLines() {
	allMultipleSettingsLines.clear();
	selectedMultipleSettingsLines.clear();
	linesPanel.removeAll();
	linesPanel.updateUI();
    }

    @Override
    public Set<XMLPart> getCurrentXMLParts() {
	Set<XMLPart> allXmlParts = new HashSet<XMLPart>();

	int i = 1;
	for (MultipleSettingsLine line: allMultipleSettingsLines) {
	    line.setCurrentXmlIndex(i);
	    i++;
	    allXmlParts.addAll(line.getCurrentXMLParts());
	}
	
	return allXmlParts;
    }

    @Override
    public void setCurrentXMLParts(Collection<XMLPart> toBeDisplayed) {
	//1 remove all lines
	removeAllLines();
	//2 get location of this multiple settings
	final String thisLocation = xml.getPropertyAsString(tempPrefix + "location");
	//3 filter all toBeDisplayed-Parts that contain location + \\[\\d+\\]
	SortedSet<XMLPart> filteredParts = new TreeSet<XMLPart>();
	for (XMLPart p: toBeDisplayed){
	    if (p.getLocation().startsWith(thisLocation)) {
		filteredParts.add(p);
	    }
	}
	//4 order (no ordering necessary -> SortedSet)
	//5 add lines for them
	Map<Integer, Set<XMLPart>> linesAndValues = new HashMap<Integer, Set<XMLPart>>();
	//5a create different lists, that only contain the XMLParts with location matching: ...[1]...; ...[2]...; ... ...[n]...;
	for (XMLPart p: filteredParts) {
	    //5ai find out number
	    String location = p.getLocation();
	    try {
		int index = Integer.parseInt(location.substring(location.lastIndexOf('[')+1, location.lastIndexOf('[')+2));
		if (!linesAndValues.containsKey(index)) {
		    linesAndValues.put(index, new HashSet<XMLPart>());
		}
		linesAndValues.get(index).add(p);
	    } catch(NumberFormatException nfe) {
		//do nothing
	    }
	}
	
	for (Entry<Integer, Set<XMLPart>> e: linesAndValues.entrySet()) {
	    //5b delegate them to the new line
	    addLine().setCurrentXMLParts(e.getValue());
	}
    }
}
