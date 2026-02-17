package org.opendse.testcaseGenerator.visualization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;



class GeneratorClipboardFrame extends JFrame implements ClipboardOwner {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a {@link ClipboardFrame}.
	 * 
	 * @param content
	 *            the content as a string
	 */
	public GeneratorClipboardFrame(final String content) {
		final JTextPane text = new JTextPane() {
			private static final long serialVersionUID = 1L;

			@Override
			public void processMouseEvent(MouseEvent me) {
				switch (me.getID()) {
				case MouseEvent.MOUSE_CLICKED:
					switch (me.getButton()) {
					case MouseEvent.BUTTON1:
						StringSelection stringSelection = new StringSelection(content);
						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
						clipboard.setContents(stringSelection, GeneratorClipboardFrame.this);
						break;
					default:
						break;
					}
					GeneratorClipboardFrame.this.dispose();
					break;
				default:
					break;
				}
			}
		};
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				GeneratorClipboardFrame.this.dispose();
			}
		});

		text.setCaretColor(Color.WHITE);
		text.setEditable(false);
		text.setHighlighter(null);
		text.setBorder(BorderFactory.createLineBorder(Color.WHITE, 4));
		final StyledDocument doc = text.getStyledDocument();
		try {
			doc.insertString(doc.getLength(), content, null);
			doc.remove(doc.getLength() - 1, 1);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(text);
		panel.add(BorderLayout.SOUTH, new JLabel(" left-click: copy to clipboard"));
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		add(panel);

		setUndecorated(true);
		Point2D p = MouseInfo.getPointerInfo().getLocation();
		setLocation((int) p.getX() - 8, (int) p.getY() - 8);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.datatransfer.ClipboardOwner#lostOwnership(java.awt.datatransfer
	 * .Clipboard, java.awt.datatransfer.Transferable)
	 */
	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		// do nothing
	}
}
