package framework.core.gui.userInterfaces.pluginRegistryGui.addRessource;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import framework.core.gui.userInterfaces.genericXMLGeneratedGUI.genericXMLGUIBranch.DescriptionPanel;
import framework.core.gui.userInterfaces.genericXMLGeneratedGUI.inputFields.StringInputField;
import framework.core.gui.util.GUIText;
import framework.core.gui.util.components.JTextFieldContextMenu;
import framework.core.gui.util.visualUtilities.ImageLoader;

/**
 * AddResourceGui is a small dialog with a layer spinner and a semi-fix path textfield
 * 
 * @author Marius Fink
 * @version 13.11.2012
 */
public class AddResourceGui {

    public static final byte PATH_PART_1 = 1;
    public static final byte PATH_PART_2 = 2;
    public static final byte PATH_PART_3 = 3;
    private JDialog dialog;
    private JButton okButton;
    private JButton cancelButton;
    private JPanel contentPanel;
    private JLabel nameOfLayerLabel;
    private JSpinner layerSpinner;
    private JTextField path2TextField;
    private boolean confirmed = false;
    private JLabel path1Label = new JLabel();
    private JLabel path3Label = new JLabel();
    private StringInputField idTextField;

    /**
     * Constructor of AddResourceGui
     */
    public AddResourceGui() {
	JPanel idPanelWrapper = new JPanel(new BorderLayout(10, 3));
	idPanelWrapper.setBorder(BorderFactory.createTitledBorder(GUIText.getText("resourceId")));
	idPanelWrapper.add(new JLabel(), BorderLayout.WEST);
	setIdTextField(new StringInputField("uniqueId", "[a-zA-Z0-9]+"));
	idPanelWrapper.add(getIdTextField(), BorderLayout.CENTER);

	JPanel layerPanelWrapper = new JPanel(new BorderLayout(10, 3));
	layerPanelWrapper.setBorder(BorderFactory.createTitledBorder(GUIText.getText("layer")));

	JPanel layerPanel = new JPanel(new BorderLayout(10, 3));
	layerPanel.add(new JLabel(GUIText.getText("layer")), BorderLayout.WEST);

	setLayerSpinner(new JSpinner(new SpinnerNumberModel(1, 1, 5, 1)));
	getLayerSpinner().setPreferredSize(new Dimension(50, getLayerSpinner().getHeight()));
	layerPanel.add(getLayerSpinner(), BorderLayout.CENTER);

	JPanel layerDescriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

	nameOfLayerLabel = new JLabel("\"" + GUIText.getText("layer1") + "\"");

	layerDescriptionPanel.add(nameOfLayerLabel);
	layerPanelWrapper.add(layerPanel, BorderLayout.WEST);
	layerPanelWrapper.add(layerDescriptionPanel, BorderLayout.CENTER);

	JPanel pathPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 3));
	pathPanel.setBorder(BorderFactory.createTitledBorder(GUIText
		.getText("pathToSettingsDocument")));
	path2TextField = new JTextFieldContextMenu();
	pathPanel.add(path1Label);
	path2TextField.setColumns(15);
	pathPanel.add(path2TextField);
	pathPanel.add(path3Label);

	JPanel infoPanel = new DescriptionPanel(GUIText.getText("settingsImmutable"));

	JPanel boxPanel = new JPanel();
	boxPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	BoxLayout boxLayout = new BoxLayout(boxPanel, BoxLayout.Y_AXIS);
	boxPanel.setLayout(boxLayout);
	boxPanel.add(idPanelWrapper);
	boxPanel.add(layerPanelWrapper);
	boxPanel.add(pathPanel);
	boxPanel.add(infoPanel);

	JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 2));
	okButton = new JButton(GUIText.getText("ok"), ImageLoader.loadIcon("ok"));
	okButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		confirmed = true;
		dialog.dispose();
	    }
	});
	cancelButton = new JButton(GUIText.getText("cancel"), ImageLoader.loadIcon("cancel"));
	cancelButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		dialog.dispose();
	    }
	});
	buttonPanel.add(okButton);
	buttonPanel.add(cancelButton);

	contentPanel = new JPanel(new BorderLayout());
	contentPanel.add(boxPanel, BorderLayout.NORTH);
	contentPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Shows a modal dialog
     */
    public void showDialog() {
	dialog = new JDialog();
	dialog.setModal(true);
	dialog.setLocationRelativeTo(null);
	dialog.setTitle(GUIText.getText("newResource"));
	dialog.setSize(contentPanel.getPreferredSize().width + 145,
		contentPanel.getPreferredSize().height + 40);
	dialog.getContentPane().add(contentPanel);
	dialog.setIconImage(ImageLoader.loadIcon("plugin").getImage());
	dialog.setVisible(true);
	dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    /**
     * sets the specified part of the path to the given text
     * 
     * @param part
     *            the part to change (see public static final bytes of this class)
     * @param text
     *            the test to display
     */
    public void setPathTextOfPart(byte part, String text) {
	switch (part) {
	case PATH_PART_1:
	    path1Label.setText(text);
	    break;
	case PATH_PART_2:
	    path2TextField.setText(text);
	    break;
	case PATH_PART_3:
	    path3Label.setText(text);
	    break;
	}
    }

    /**
     * gets the current text of the path part
     * 
     * @param part
     *            the part of the path which text to get (see static final bytes of this
     *            class)
     * @return the text of the specified path part
     */
    public String getPathTextOfPart(byte part) {
	switch (part) {
	case PATH_PART_1:
	    return path1Label.getText();
	case PATH_PART_2:
	    return path2TextField.getText();
	case PATH_PART_3:
	    return path3Label.getText();
	default:
	    return null;
	}
    }

    /**
     * @return the okButton
     */
    public JButton getOkButton() {
	return okButton;
    }

    /**
     * @param okButton
     *            the okButton to set
     */
    public void setOkButton(JButton okButton) {
	this.okButton = okButton;
    }

    public void setLayerDescription(String text) {
	nameOfLayerLabel.setText(text);
    }

    /**
     * @return the abortButton
     */
    public JButton getCancelButton() {
	return cancelButton;
    }

    /**
     * @param abortButton
     *            the abortButton to set
     */
    public void setCancelButton(JButton abortButton) {
	this.cancelButton = abortButton;
    }

    /**
     * @return the layerSpinner
     */
    public JSpinner getLayerSpinner() {
	return layerSpinner;
    }

    /**
     * @param layerSpinner
     *            the layerSpinner to set
     */
    public void setLayerSpinner(JSpinner layerSpinner) {
	this.layerSpinner = layerSpinner;
    }

    /**
     * @return the confirmed
     */
    public boolean isConfirmed() {
	return confirmed;
    }

    /**
     * @param confirmed
     *            the confirmed to set
     */
    public void setConfirmed(boolean confirmed) {
	this.confirmed = confirmed;
    }

    /**
     * @return the pathTextField
     */
    public JTextField getPathTextField() {
	return path2TextField;
    }

    /**
     * @param pathTextField
     *            the pathTextField to set
     */
    public void setPathTextField(JTextField pathTextField) {
	this.path2TextField = pathTextField;
    }

    /**
     * @return the idTextField
     */
    public StringInputField getIdTextField() {
	return idTextField;
    }

    /**
     * @param idTextField
     *            the idTextField to set
     */
    public void setIdTextField(StringInputField idTextField) {
	this.idTextField = idTextField;
    }
}
