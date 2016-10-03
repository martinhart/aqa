/*
 * Copyright (c) 2016 Martin Hart.  Under the terms of the MIT licence.
 */
package aqa;

import aqa.ui.MainWindow;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author martinhart
 */
public class AQAPseudocodeInterpreter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) { // ignore
            System.out.println(e.getMessage());
        }
        
        SwingUtilities.invokeLater(() -> {
            MainWindow win = new MainWindow();
            win.create();
            win.setVisible(true);
        });
    }
    
}
