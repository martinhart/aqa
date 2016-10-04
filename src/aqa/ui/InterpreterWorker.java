/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.ui;

import aqa.Interpreter;
import aqa.InterpreterException;
import aqa.parser.InputProvider;
import aqa.parser.InstructionListener;
import aqa.parser.OutputWriter;
import aqa.parser.VirtualMachine;
import java.io.StringReader;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 * This class is used to provide information from WorkerThread to DispatchThread
 * so that UI can be updated.
 *
 * @author martinhart
 */
class UpdateInformation {

    /**
     * If not null - this is sent to the OUTPUT panel
     */
    public final String message;

    /**
     * If >0 this is used to highlight a line on the EDITOR panel
     */
    public final int line;

    /**
     * If not null - this is sent to variables panel
     */
    public final VirtualMachine vm;
    
    public UpdateInformation(int line, String message, VirtualMachine vm) {
        this.line = line;
        this.message = message;
        this.vm = vm;
    }
    
    public UpdateInformation(int line, String message) {
        this(line, message, null);
    }

    public UpdateInformation(String message) {
        this(0, message, null);
    }
    
    public UpdateInformation(int line) {
        this(line, null, null);
    }
    
    public UpdateInformation(VirtualMachine vm) {
        this(0, null, vm);
    }
}

/**
 * This is used by the interpreter to get user input. Since we're running on a
 * worker thread, we need to invokeAndWait for a dialog to complete on the
 * dispatch thread. This class implements that behaviour.
 *
 * @author martinhart
 */
class BlockingInputProvider implements InputProvider {

    public String input = "";

    @Override
    public String getInput() {
        try {
            SwingUtilities.invokeAndWait(() -> {
                input = new AWTInputProvider().getInput();
                if (input == null) {
                    input = "";
                }
            });
        } catch (Exception e) {
            //TODO: how does user ever know about this exception?
            input = "";
        }
        return input;
    }

}

/**
 * Run the interpreter on a worker thread and update UI on dispatch thread.
 *
 * @author martinhart
 */
public class InterpreterWorker extends SwingWorker<Integer, UpdateInformation> {

    private final MainWindow mainWindow;
    private final CodeEditorPanel editorPanel;
    private final OutputPanel outputPanel;
    private final VariableTablePanel variableTablePanel;
    private final InstructionListener instructionListener;
    private final String codeToExecute;
    
    public InterpreterWorker(MainWindow mainWindow, CodeEditorPanel editorPanel, 
            OutputPanel outputPanel, VariableTablePanel variableTablePanel,
            InstructionListener instructionListener) {
        this.mainWindow = mainWindow;
        this.editorPanel = editorPanel;
        this.outputPanel = outputPanel;
        this.variableTablePanel = variableTablePanel;
        this.instructionListener = instructionListener;
        this.codeToExecute = editorPanel.getContent();
        
    }

    public InterpreterWorker(MainWindow mainWindow, CodeEditorPanel editorPanel, 
            OutputPanel outputPanel, VariableTablePanel variableTablePanel) {
        this(mainWindow, editorPanel, outputPanel, variableTablePanel, 
                new InstructionListener());
    }

    @Override
    protected Integer doInBackground() throws Exception {
        try {
            new Interpreter(new StringReader(codeToExecute), new OutputWriter() {
                @Override
                public void output(String message) {
                    publish(new UpdateInformation(message));
                }

            },
                    new BlockingInputProvider(),
                    instructionListener
            ).execute();
            return 0;        
        } catch (InterpreterException e) {
            publish(new UpdateInformation(e.getLine(), "error: " + e.getLocalizedMessage()));
            return 2;
        }
    }

    @Override
    protected void process(List<UpdateInformation> outputData) {
        for (UpdateInformation i : outputData) {
            if (i.line > 0) {
                editorPanel.highlightLine(i.line);
            }
            if (i.message != null) {
                outputPanel.output(i.message);
            }
            if (i.vm != null) {
                variableTablePanel.displayVMState(i.vm);
            }
        }
    }

    @Override
    protected void done() {
        int status;
        try {
            status = get();
            outputPanel.output("completed with status " + status);
            if (status == 0) {
                editorPanel.clearHighlights();
            }
        } catch (InterruptedException e) {
            outputPanel.output("process interrupted");
            editorPanel.clearHighlights();
        } catch (CancellationException e) {
            outputPanel.output("process cancelled");
            editorPanel.clearHighlights();
        } catch (ExecutionException e) {
            e.printStackTrace();
            outputPanel.output("fatal error: " + e.getLocalizedMessage());
        }
        mainWindow.enableAllMenus();
    }
    
    public void publishLine(int line) {
        publish(new UpdateInformation(line));
    }
    
    public void publishVM(VirtualMachine vm) {
        publish(new UpdateInformation(vm));
    }
}
