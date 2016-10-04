/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.ui;

import aqa.Interpreter;
import aqa.InterpreterException;
import aqa.parser.InputProvider;
import aqa.parser.OutputWriter;
import java.io.StringReader;
import java.util.List;
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
    public String message;

    /**
     * If >0 this is used to highlight a line on the EDITOR panel
     */
    public int line;

    public UpdateInformation(int line, String message) {
        this.line = line;
        this.message = message;
    }

    public UpdateInformation(String message) {
        this.line = 0;
        this.message = message;
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

    private final CodeEditorPanel editorPanel;
    private final OutputPanel outputPanel;
    private final String codeToExecute;

    public InterpreterWorker(CodeEditorPanel editorPanel, OutputPanel outputPanel) {
        this.editorPanel = editorPanel;
        this.outputPanel = outputPanel;
        this.codeToExecute = editorPanel.getContent();
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
                    new BlockingInputProvider()
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
            outputPanel.output(i.message);
        }
    }

    @Override
    protected void done() {
        int status;
        try {
            status = get();
            outputPanel.output("completed with status " + status);
        } catch (Exception e) {
            outputPanel.output("fatal: " + e.getLocalizedMessage());
        }
    }
}
