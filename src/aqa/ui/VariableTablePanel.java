/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa.ui;

import aqa.InterpreterException;
import aqa.parser.VirtualMachine;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author martinhart
 */
public class VariableTablePanel extends JPanel {
    
    private JTable table;
    private VMTableModel model;
    
    public void create() {
        model = new VMTableModel();        
        table = new JTable(model);
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JScrollPane(table));
        
    }
    
    public void displayVMState(VirtualMachine vm) {
        model.setVM(vm);
    }
    
    private class VMTableModel extends AbstractTableModel {
        
        private VirtualMachine vm;
        private List<String> varNames;
        
        public VMTableModel() {
            vm = null;
        }
        
        public void setVM(VirtualMachine vm) {
            this.vm = vm;
            varNames = vm.getVariables().getNames();
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            if (vm == null) {
                return 0;
            }
            return varNames.size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }
        
        @Override
        public String getColumnName(int col) {
            if (col == 0) {
                return "Variable";
            }
            else {
                return "Value";
            }
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (vm == null) {
                return null;
            }
            
            if (columnIndex == 0) {
                return varNames.get(rowIndex);
            }
            else {
                try {
                    return vm.getVariable(varNames.get(rowIndex)).getValue().inspect();
                } catch (InterpreterException ex) {
                    return ex.getLocalizedMessage();
                }
            }
        }
        
    }
    
}
