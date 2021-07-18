package net.wehrens.intellij.plugins.propertysorter;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.UndoConfirmationPolicy;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;

import net.wehrens.intellij.plugins.propertysorter.ui.SortPropertiesResultsPanel;


public class SortPropertiesAction extends AnAction {

  private PropertySorter propertySorter = new PropertySorter();
  private StringPropertyConverter stringPropertyConverter = new StringPropertyConverter();

  public void actionPerformed(AnActionEvent e) {
    try {
      VirtualFile[] files = e.getData(DataKeys.VIRTUAL_FILE_ARRAY);
      if (files == null || files.length == 0) {
        throw new RuntimeException("No files selected!");
      }
      List<SortPropertiesResult> resultList = sortPropertiesFiles(e, files);
      
      ApplicationManager.getApplication().invokeAndWait(()->{
        SortPropertiesResultsPanel dialog = new SortPropertiesResultsPanel(e.getProject(), resultList);
        dialog.setTitle("Property Sorter Result");
        dialog.show();
      });
     
    } catch (Exception ex) {
      Messages.showErrorDialog(e.getProject(), "Error while sorting properties. " + ex.getLocalizedMessage(),
          "Property Sorter");
      ex.printStackTrace();
    }
  }

  private List<SortPropertiesResult> sortPropertiesFiles(AnActionEvent e, VirtualFile[] files) {
    List<SortPropertiesResult> resultList = new ArrayList<>();
    for (VirtualFile virtualFile : files) {
      resultList.add(sortOnePropertiesFile(e, virtualFile));
    }
    return resultList;
  }

  private SortPropertiesResult sortOnePropertiesFile(AnActionEvent e, VirtualFile virtualFile) {
    SortPropertiesResult result = new SortPropertiesResult(virtualFile, false, null);
    
    if (virtualFile.isDirectory()) {
      result.setMessage("this is a directory");
      return result;
    }

    if (!virtualFile.getName().endsWith(".properties")) {
      result.setMessage("file must have .properties extension");
      return result;
    }
    
    Document document = FileDocumentManager.getInstance().getDocument(virtualFile);
    final String fileText = document.getText();
    if (fileText == null) {
      result.setMessage("No content");
      return result;
    }
    
    try {
      Properties properties = stringPropertyConverter.convertString(fileText);
      List<String> sortedKeys = propertySorter.getSortedKeys(properties);
      final String newDocumentContent = stringPropertyConverter.sortAndConvertProperties(properties,
          sortedKeys);
      
      ApplicationManager.getApplication().runWriteAction(
          () -> CommandProcessor.getInstance().executeCommand(e.getProject(),
              () -> document.replaceString(0, document.getTextLength(), 
                  stringPropertyConverter.mergeComments(newDocumentContent, fileText)), 
              "Property Sorter Plugin", null, UndoConfirmationPolicy.DO_NOT_REQUEST_CONFIRMATION));
    }
    catch (ConvertException convertException) {
      result.setMessage("There are lines containing non property styled text. File was not changed.");
      return result;
    }
    result.setSuccess(true);
    result.setMessage("sorted properties");
    return result;
  }
}
