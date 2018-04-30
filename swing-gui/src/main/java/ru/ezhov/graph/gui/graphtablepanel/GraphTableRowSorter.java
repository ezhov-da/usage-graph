package ru.ezhov.graph.gui.graphtablepanel;

import ru.ezhov.graph.gui.domain.GraphObjectGui;

import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableStringConverter;

class GraphTableRowSorter extends TableRowSorter<TableModel> {
    private GraphTableModel graphTableModel;

    public GraphTableRowSorter(GraphTableModel graphTableModel) {
        super(graphTableModel);
        this.graphTableModel = graphTableModel;
    }

    private final TableStringConverter tableStringConverterAllTask =
            new TableStringConverter() {
                @Override
                public String toString(TableModel model, int row, int column) {
                    GraphObjectGui graphObjectGui = (GraphObjectGui) model.getValueAt(row, column);

                    switch (column) {
                        case 0:
                            return graphObjectGui.id();
                        case 1:
                            return numberForEquals(graphObjectGui.parents().size() + "");
                        case 2:
                            return numberForEquals(graphObjectGui.children().size() + "");
                        case 3:
                            return numberForEquals(graphObjectGui.rows() + "");
                        case 4:
                            return numberForEquals(graphObjectGui.stability() + "");
                        default:
                            return "";
                    }
                }

                private String numberForEquals(String val) {
                    int len = val.length();
                    int adds = 100 - len;
                    String res = new String(new char[adds]).replace("\0", "0") + val;
                    return res;
                }
            };

    @Override
    public TableStringConverter getStringConverter() {
        return tableStringConverterAllTask;
    }
}
