/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.ui;

import javax.swing.JOptionPane;
import aqa.parser.InputProvider;

/**
 *
 * @author martinhart
 */
public class AWTInputProvider implements InputProvider {

    @Override
    public String getInput() {
        return (String) JOptionPane.showInputDialog(null, "User Input: ");
    }
    
}
