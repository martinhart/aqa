/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.ui;

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
    private VariableTablePanel variableTablePanel;
    private String fileName;
    private boolean isDirty;
    private final StepLock stepLock;
    private JMenu fileMenu;
    private JMenu interpreterMenu;
    private JMenuItem runMenuItem;
    private JMenuItem debugMenuItem;
    private JMenuItem stepMenuItem;
    private JMenuItem stopMenuItem;
    private InterpreterWorker worker;
    
    public MainWindow() {
        fileName = "";
        isDirty = false;
        stepLock = new StepLock();
    }

    public void create() {

        createMenu();
        createEditorPanel();
        createOutputPanel();
        createVariableTablePanel();

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setTitle("AQA Pseudocode Interpreter");
        
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        JSplitPane leftSplitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT, editorPanel, outputPanel);
        leftSplitter.setResizeWeight(0.8);
        leftPanel.add(leftSplitter);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        JSplitPane rightSplitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, variableTablePanel);
        rightSplitter.setResizeWeight(1);
        mainPanel.add(rightSplitter);
        
        getContentPane().add(mainPanel);
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
    
    public void enableAllMenus() {
        fileMenu.setEnabled(true);
        interpreterMenu.setEnabled(true);
        runMenuItem.setEnabled(true);
        debugMenuItem.setEnabled(true);
        stepMenuItem.setEnabled(false);
        stopMenuItem.setEnabled(false);
    }

    private void createMenu() {
        menuBar = new JMenuBar();
        createFileMenu();
        createInterpreterMenu();
        createHelpMenu();
        setJMenuBar(menuBar);
    }

    private void createFileMenu() {
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');

        JMenuItem menuItem = new JMenuItem("New", KeyEvent.VK_N);
        menuItem.addActionListener((ActionEvent e) -> {
            onNew();
        });
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menuItem.setMnemonic('N');
        fileMenu.add(menuItem);

        menuItem = new JMenuItem("Open...", KeyEvent.VK_O);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menuItem.setMnemonic('O');
        menuItem.addActionListener((ActionEvent e) -> {
            onOpen();
        });
        fileMenu.add(menuItem);

        menuItem = new JMenuItem("Save");
        menuItem.setMnemonic('S');
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menuItem.addActionListener((ActionEvent e) -> {
            onSave();
        });
        fileMenu.add(menuItem);

        menuItem = new JMenuItem("Save as...");
        menuItem.addActionListener((ActionEvent e) -> {
            onSaveAs();
        });
        fileMenu.add(menuItem);

        menuBar.add(fileMenu);
    }

    private void createInterpreterMenu() {
        interpreterMenu = new JMenu("Interpreter");
        interpreterMenu.setMnemonic('I');

        runMenuItem = new JMenuItem("Run");
        runMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        runMenuItem.setMnemonic('R');
        runMenuItem.addActionListener((ActionEvent e) -> {
            onInterpret();
        });
        interpreterMenu.add(runMenuItem);

        debugMenuItem = new JMenuItem("Debug");
        debugMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        debugMenuItem.addActionListener((ActionEvent e) -> {
            onDebug();
        });
        interpreterMenu.add(debugMenuItem);

        stepMenuItem = new JMenuItem("Step through");
        stepMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        stepMenuItem.addActionListener((ActionEvent e) -> {
            onStep();
        });
        stepMenuItem.setEnabled(false);
        interpreterMenu.add(stepMenuItem);
        
        stopMenuItem = new JMenuItem("Stop Debugging");
        stopMenuItem.addActionListener((ActionEvent e) -> {
            onStopDebugging();
        });
        stopMenuItem.setEnabled(false);
        interpreterMenu.add(stopMenuItem);

        menuBar.add(interpreterMenu);
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
    
    private void createVariableTablePanel() {
        variableTablePanel = new VariableTablePanel();
        variableTablePanel.create();
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
        fileMenu.setEnabled(false);
        runMenuItem.setEnabled(false);
        debugMenuItem.setEnabled(false);
        stepMenuItem.setEnabled(false);
        stopMenuItem.setEnabled(true);
        outputPanel.clear();
        editorPanel.clearHighlights();

        worker = new InterpreterWorker(this, editorPanel, 
                outputPanel, variableTablePanel);
        worker.execute();
    }

    private void onDebug() {
        fileMenu.setEnabled(false);
        runMenuItem.setEnabled(false);
        debugMenuItem.setEnabled(false);
        stepMenuItem.setEnabled(true);
        stopMenuItem.setEnabled(true);
        outputPanel.clear();
        editorPanel.clearHighlights();
        StepInstructionListener instructionListener = new StepInstructionListener(stepLock);
        worker = new InterpreterWorker(this, editorPanel, outputPanel, 
                variableTablePanel, instructionListener);
        instructionListener.executeWorker(worker);
    }
    
    private void onStopDebugging() {
        if (worker != null) {
            worker.cancel(true);
        }
    }
    
    private void onStep() {
        stepLock.allowContinue();
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
