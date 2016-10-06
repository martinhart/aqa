/*
 * Copyright (c) 2016 Martin Hart.  Under the terms of the MIT licence.
 */
package aqa;

import aqa.ui.MainWindow;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Application entry point.
 * This class initialises the UI and then creates and displays the main window.
 * @author martinhart
 */
public class AQAPseudocodeInterpreter {

    /**
     * @param args the command line arguments (which are ignored!)
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
