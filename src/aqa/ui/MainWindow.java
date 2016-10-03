/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.ui;

import aqa.ui.AboutDialog;
import aqa.Interpreter;
import aqa.InterpreterException;
import java.awt.FileDialog;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.net.URI;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;

/**
 *
 * @author martinhart
 */
public class MainWindow extends JFrame {

    private JMenuBar menuBar;
    private CodeEditorPanel editorPanel;
    private OutputPanel outputPanel;
    private String fileName;
    private boolean isDirty;

    public MainWindow() {
        fileName = "";
        isDirty = false;
    }

    public void create() {
        JPanel panel = new JPanel();

        createMenu();
        createEditorPanel();
        createOutputPanel();

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setTitle("AQA Pseudocode Interpreter");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, editorPanel, outputPanel);
        sp.setResizeWeight(0.8);
        panel.add(sp);
        getContentPane().add(panel);
        pack();
        this.setSize(1024, 768);
        setLocationRelativeTo(null);
        onNew();

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (itIsOkToReplaceEditorBuffer()) {
                    dispose();
                    System.exit(0);
                }
            }
        });
    }

    private void createMenu() {
        menuBar = new JMenuBar();
        createFileMenu();
        createInterpreterMenu();
        createHelpMenu();
        setJMenuBar(menuBar);
    }

    private void createFileMenu() {
        JMenu menu = new JMenu("File");
        menu.setMnemonic('F');

        JMenuItem menuItem = new JMenuItem("New", KeyEvent.VK_N);
        menuItem.addActionListener((ActionEvent e) -> {
            onNew();
        });
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menuItem.setMnemonic('N');
        menu.add(menuItem);

        menuItem = new JMenuItem("Open...", KeyEvent.VK_O);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menuItem.setMnemonic('O');
        menuItem.addActionListener((ActionEvent e) -> {
            onOpen();
        });
        menu.add(menuItem);

        menuItem = new JMenuItem("Save");
        menuItem.setMnemonic('S');
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menuItem.addActionListener((ActionEvent e) -> {
            onSave();
        });
        menu.add(menuItem);

        menuItem = new JMenuItem("Save as...");
        menuItem.addActionListener((ActionEvent e) -> {
            onSaveAs();
        });
        menu.add(menuItem);

        menuBar.add(menu);
    }

    private void createInterpreterMenu() {
        JMenu menu = new JMenu("Interpreter");
        menu.setMnemonic('I');

        JMenuItem menuItem = new JMenuItem("Run");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menuItem.setMnemonic('R');
        menuItem.addActionListener((ActionEvent e) -> {
            onInterpret();
        });
        menu.add(menuItem);
        menuBar.add(menu);
    }

    private void createHelpMenu() {
        JMenu menu = new JMenu("Help");
        menu.setMnemonic('H');

        JMenuItem menuItem = new JMenuItem("About...");
        menuItem.setMnemonic('A');
        menuItem.addActionListener((ActionEvent e) -> {
            new AboutDialog().setVisible(true);
        });
        menu.add(menuItem);

        menuItem = new JMenuItem("AQA Pseudocode Specification...");
        menuItem.addActionListener((ActionEvent e) -> {
            URI uri = URI.create("http://filestore.aqa.org.uk/resources/computing/AQA-8520-TG-PC.PDF");
            try {
                java.awt.Desktop.getDesktop().browse(uri);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        menuItem.setMnemonic('S');
        menu.add(menuItem);

        menuBar.add(menu);
    }

    private void createEditorPanel() {
        editorPanel = new CodeEditorPanel();
        editorPanel.create();
        editorPanel.addListener(() -> {
            isDirty = true;
            setTitle();
        });
    }

    private void setTitle() {
        String title = "AQA Pseudocode Interpreter - " + fileName;
        if (isDirty) {
            title += "*";
        }
        setTitle(title);
    }

    private void createOutputPanel() {
        outputPanel = new OutputPanel();
        outputPanel.create();
    }

    private void onNew() {
        if (itIsOkToReplaceEditorBuffer()) {
            editorPanel.setContent("");
            fileName = "";
            isDirty = false;
            setTitle();
        }
    }

    private void onOpen() {
        if (itIsOkToReplaceEditorBuffer()) {
            FileDialog fd = new FileDialog(this, "Open", FileDialog.LOAD);
            fd.setVisible(true);
            if (fd.getFile() != null) {
                try {
                    fileName = fd.getDirectory() + "/" + fd.getFile();
                    String content = new Scanner(new File(fileName)).useDelimiter("\\Z").next();
                    editorPanel.setContent(content);
                    isDirty = false;
                    setTitle();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e.toString(), "Error ", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void onInterpret() {
        try {
            outputPanel.clear();
            editorPanel.clearHighlights();
            new Interpreter(new StringReader(editorPanel.getContent()), outputPanel, new AWTInputProvider()).execute();
            editorPanel.clearHighlights();
        } catch (InterpreterException e) {
            outputPanel.output("ERROR! " + e.getMessage());
            editorPanel.highlightLine(e.getLine());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.toString(), "Error ", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean itIsOkToReplaceEditorBuffer() {
        if (isDirty) {
            int result = JOptionPane.showConfirmDialog(this, "Save changes?");
            switch (result) {
                case JOptionPane.CANCEL_OPTION:
                    return false;
                case JOptionPane.YES_OPTION:
                    return onSave();
                case JOptionPane.NO_OPTION:
                    return true;
                default:
                    return false;
            }
        }
        return true;
    }

    private boolean onSave() {
        if (fileName.isEmpty()) {
            return onSaveAs();
        }
        return doSave();
    }

    private boolean onSaveAs() {
        FileDialog fd = new FileDialog(this, "Save as", FileDialog.SAVE);
        fd.setVisible(true);
        if (fd.getFile() != null) {
            fileName = fd.getDirectory() + "/" + fd.getFile();
            if (doSave()) {
                return true;
            }
        }
        return false;
    }

    private boolean doSave() {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"))) {
            writer.write(editorPanel.getContent());
            writer.write("\n");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        isDirty = false;
        setTitle();
        return true;
    }
}
