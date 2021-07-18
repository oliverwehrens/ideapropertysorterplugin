package net.wehrens.intellij.plugins.propertysorter.ui;

import javax.swing.table.AbstractTableModel;

import java.util.List;

import net.wehrens.intellij.plugins.propertysorter.SortPropertiesResult;

/**
 * Table model which stores the results of sorting the properties files. There is one row per
 * sorted properties file.
 */
public class SortPropertiesResultsTableModel extends AbstractTableModel {
  private static final String[] COLUMN_NAMES = {"File", "Success", "Message"};
  private List<SortPropertiesResult> propertySortResults;
  
  public SortPropertiesResultsTableModel(List<SortPropertiesResult> propertySortResults) {
    this.propertySortResults = propertySortResults;
  }
  
  @Override
  public int getRowCount() {
    if (propertySortResults == null) {
      return 0;
    }
    return propertySortResults.size();
  }

  @Override
  public int getColumnCount() {
    return 3;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    if (propertySortResults == null) {
      return null;
    }
    if (rowIndex >= propertySortResults.size()) {
      return null;
    }

    SortPropertiesResult propertySortResult = propertySortResults.get(rowIndex);
    switch (columnIndex) {
      case 0:
        return propertySortResult.getVirtualFile().getName();
      case 1: 
        return propertySortResult.isSuccess();
      case 2:
        return propertySortResult.getMessage();
      default:
        return null;
    }
  }

  @Override
  public String getColumnName(int columnIndex) {
    return COLUMN_NAMES[columnIndex];
  }
}
