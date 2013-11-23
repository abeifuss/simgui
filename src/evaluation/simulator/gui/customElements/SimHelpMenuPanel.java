package evaluation.simulator.gui.customElements;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.InputStream;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import evaluation.simulator.gui.layout.TutorialPlayer;

@SuppressWarnings("serial")
public class SimHelpMenuPanel extends JPanel implements HyperlinkListener {
	
	private static Logger logger = Logger.getLogger(SimHelpMenuPanel.class);

	static JEditorPane _htmlPane;
	private static SimHelpMenuPanel _instance = null;

	private static Element _lastHyperlinkElementEntered;

	private static void changeColor(Element el, Color color) {
		if (_lastHyperlinkElementEntered != null) {
			HTMLDocument doc = (HTMLDocument) _htmlPane.getDocument();
			int start = el.getStartOffset();
			int end = el.getEndOffset();
			StyleContext ss = doc.getStyleSheet();
			Style style = ss.addStyle("HighlightedHyperlink", null);
			style.addAttribute(StyleConstants.Foreground, color);
			doc.setCharacterAttributes(start, end - start, style, false);
		}
	}

	public static SimHelpMenuPanel getInstance() {
		if (_instance == null) {
			_instance = new SimHelpMenuPanel();
		}
		return _instance;
	}

	private static void highlightHyperlink(Element hyperlinkElement) {
		if (hyperlinkElement != _lastHyperlinkElementEntered) {
			_lastHyperlinkElementEntered = hyperlinkElement;
			changeColor(hyperlinkElement, new Color(128, 0, 128));
		}
	}

	private static void removeHyperlinkHighlight() {
		changeColor(_lastHyperlinkElementEntered, Color.BLUE);
		_lastHyperlinkElementEntered = null;
	}

	String _htmlNavigation;

	InputStream _is;

	private SimHelpMenuPanel() {
		this.init();
	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getInputEvent().getModifiers() == InputEvent.BUTTON1_MASK) {
			if (e.getDescription().equals("VIDEO1")) {
				JFrame jf = new TutorialPlayer("TEST");
				jf.setVisible(true);
				jf.setBounds(400, 400, 640, 480);
				logger.log(Level.DEBUG,
						"VIDEO Event for " + e.getDescription());
			} else {
				SimHelpContentPanel.getInstance().loadURL(
						e.getDescription() + "index.html");
				logger.log(Level.DEBUG,
						"Hyperlink Event for " + e.getDescription());
			}
		}
	}

	private void init() {

		String navigation = NavigationService.getMenu();

		try {
			_htmlPane = new JEditorPane();

			_htmlPane.setEditable(false);

			HTMLEditorKit kit = new HTMLEditorKit();
			_htmlPane.setEditorKit(kit);

			StyleSheet styleSheet = kit.getStyleSheet();
			styleSheet.addRule("a { color: blue; text-decoration: none; }");

			_htmlPane.addHyperlinkListener(this);
			this.initListeners();

			Document doc = kit.createDefaultDocument();
			_htmlPane.setDocument(doc);
			_htmlPane.setText(navigation);

			this.setLayout(new BorderLayout());
			this.setBackground(Color.WHITE);
			// System.out.println(navigation);

			this.add(new JScrollPane(_htmlPane), BorderLayout.CENTER);
		} catch (Exception ex) {
			// ex.printStackTrace();
			System.out.println("problem has occured" + ex.getMessage());
		}
	}

	private void initListeners() {
		_htmlPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				removeHyperlinkHighlight();
			}
		});

		_htmlPane.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				Point pt = new Point(e.getX(), e.getY());
				int pos = _htmlPane.viewToModel(pt);
				if (pos >= 0) {
					HTMLDocument hdoc = (HTMLDocument) _htmlPane.getDocument();
					Element elem = hdoc.getCharacterElement(pos);
					if (elem != null) {
						AttributeSet a = elem.getAttributes();
						AttributeSet anchor = (AttributeSet) a
								.getAttribute(HTML.Tag.A);

						if (elem != _lastHyperlinkElementEntered) {
							removeHyperlinkHighlight();
						}
						if (anchor != null) {
							highlightHyperlink(elem);
						}
					}
				}
			}
		});
	}

}