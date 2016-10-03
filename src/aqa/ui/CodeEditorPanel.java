/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.ui;

import aqa.ui.TextLineNumber;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

/**
 *
 * @author martinhart
 */
public class CodeEditorPanel extends JPanel {

    private final List<CodeEditorListener> listeners;
    private JTextArea textArea;

    CodeEditorPanel() {
        listeners = new ArrayList<>();
    }

    /**
     * Create all UI components. must be done before this object can be
     * displayed.
     */
    public void create() {
        textArea = new JTextArea();
        textArea.setFont(new Font("monospaced", Font.PLAIN, 14));
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JScrollPane jsp = new JScrollPane(textArea);
        TextLineNumber tln = new TextLineNumber(textArea);
        jsp.setRowHeaderView(tln);
        add(jsp);
        
        textArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                clearHighlights();
                notifyListeners();
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    /**
     * Highlight the given line in the text editor. Nothing is done if
     * lineNumber is out of range.
     *
     * @param lineNumber - the 1-based number of line to highlight.
     */
    public void highlightLine(int lineNumber) {
        try {
            Highlighter h = textArea.getHighlighter();            
            clearHighlights();
            h.addHighlight(textArea.getLineStartOffset(lineNumber - 1),
                    textArea.getLineEndOffset(lineNumber - 1),
                    DefaultHighlighter.DefaultPainter);
        } catch (BadLocationException ex) { //ignore
        }
    }

    /**
     * Remove all existing highlights.
     */
    public void clearHighlights() {
        Highlighter h = textArea.getHighlighter();
        h.removeAllHighlights();
    }

    /**
     * Set the code to display. Causes content to be cleared and reset and
     * removes all existing highlights.
     *
     * @param content - the text to display.
     */
    public void setContent(String content) {
        clearHighlights();
        textArea.setText(content);
    }

    /**
     * get the content of the code editor.
     *
     * @return the content
     */
    public String getContent() {
        return textArea.getText();
    }

    /**
     * Add a listener to be called when events are triggered from this object.
     *
     * @param cel - A code editor listener instance.
     */
    public void addListener(CodeEditorListener cel) {
        listeners.add(cel);
    }
    
    /**
     * Fire a notification to each listener.
     */
    private void notifyListeners() {
        for (CodeEditorListener cel : listeners) {
            cel.contentChanged();
        }
    }
}
