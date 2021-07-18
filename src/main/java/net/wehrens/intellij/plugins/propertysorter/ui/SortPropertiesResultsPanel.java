package net.wehrens.intellij.plugins.propertysorter.ui;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;

import java.awt.BorderLayout;

import java.awt.Dimension;
import java.util.List;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;

import net.wehrens.intellij.plugins.propertysorter.SortPropertiesResult;

import org.jetbrains.annotations.Nullable;

/**
 * Panel that contains a table which shows the results of sorting the properties files. There is one row per
 * sorted properties file.
 */
public class SortPropertiesResultsPanel extends DialogWrapper {

  private JPanel panel;
  private JBTable table;
  
  public SortPropertiesResultsPanel(@Nullable Project project, List<SortPropertiesResult> propertySortResults) {
    super(project);
    panel = new JBPanel(new BorderLayout());
    panel.setMinimumSize(new Dimension(400, 400));
    panel.setPreferredSize(new Dimension(600, 400));
    TableModel tableModel = new SortPropertiesResultsTableModel(propertySortResults);
    table = new JBTable(tableModel);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.getTableHeader().setReorderingAllowed(false);
    table.setRowSelectionAllowed(true);
    table.setRowSelectionInterval(0, 0);
    table.getColumnModel().getColumn(0).setPreferredWidth(200);
    table.getColumnModel().getColumn(1).setPreferredWidth(100);
    table.getColumnModel().getColumn(2).setPreferredWidth(450);
    
    panel.add(new JBScrollPane(table, JBScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JBScrollPane.HORIZONTAL_SCROLLBAR_NEVER),  BorderLayout.CENTER);

    setModal(true);
    init();
  }

  @Nullable
  @Override
  protected JComponent createCenterPanel() {
    return panel;
  }
}
