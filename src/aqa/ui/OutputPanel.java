/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.ui;

import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import aqa.parser.OutputWriter;

/**
 *
 * @author martinhart
 */
public class OutputPanel extends JPanel implements OutputWriter {

    JTextArea textArea;
    
    public void create() {
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(Color.LIGHT_GRAY);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JScrollPane(textArea));
    }
    
    public void clear() {
        textArea.setText("");
    }
    
    @Override
    public void output(String message) {
        textArea.append(message);
        textArea.append("\n");
    }
}
