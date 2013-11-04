package evaluation.simulator.gui.results;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author Infectiou
 *
 */
@SuppressWarnings("serial")
public class TextResult extends JPanel {
	
	private JTextArea textArea;
	private JScrollPane scroll;
	
	/**
	 * @param text
	 * @return
	 */
	private TextResult TextResult(String text) {

		this.textArea = new JTextArea();
		this.scroll = new JScrollPane(textArea);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = gridBagConstraints.BOTH;
		gridBagConstraints.anchor = GridBagConstraints.NORTH;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		gridBagConstraints.gridx = GridBagConstraints.RELATIVE;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagLayout.setConstraints(this, gridBagConstraints);
		
		this.setLayout( gridBagLayout );
		this.textArea.setEditable(false);
		
		this.add(this.scroll, gridBagConstraints);
		this.textArea.setVisible(true);
		
		this.scroll.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
		this.scroll.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
		
		this.textArea.setText(text);
		
		return this;
	}
	
	
	public TextResult TextResult() {
		return TextResult("No results found");
	}
	
}
