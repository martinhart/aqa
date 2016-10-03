/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 *
 * @author martinhart
 */
public class AboutDialog extends JDialog {

    public AboutDialog() {
        setTitle("About AQA Pseudocode Interpreter");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        
        ImageIcon img = new ImageIcon(getClass().getClassLoader().getResource("aqa/splash.png"));
        JLabel image = new JLabel(img);
        add(image);
        
        setSize(img.getIconWidth(), img.getIconHeight());
        setModalityType(ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    dispose();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        
        });
    }
}
