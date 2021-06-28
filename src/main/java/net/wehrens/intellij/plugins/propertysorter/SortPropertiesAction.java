package net.wehrens.intellij.plugins.propertysorter;

import java.util.List;
import java.util.Properties;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.UndoConfirmationPolicy;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;

public class SortPropertiesAction extends AnAction {

  private PropertySorter propertySorter = new PropertySorter();
  private StringPropertyConverter stringPropertyConverter = new StringPropertyConverter();

  public void actionPerformed(AnActionEvent e) {
    // todo: turn this into a sort all property files method
    final Editor editor = e.getData(DataKeys.EDITOR);
    final String fileText = e.getData(DataKeys.FILE_TEXT);
    MessageWindowComponent messageWindowComponent = getMessageComponent();

    if (fileText != null) {
      try {
        Properties properties = stringPropertyConverter.convertString(fileText);
        List<String> sortedKeys = propertySorter.getSortedKeys(properties);
        final String newDocumentContent = stringPropertyConverter.sortAndConvertProperties(properties,
                                                                                          sortedKeys);


        ApplicationManager.getApplication().runWriteAction(new Runnable() {

          public void run() {

            final Document document = editor.getDocument();

            CommandProcessor.getInstance().executeCommand(editor.getProject(), new Runnable() {
              public void run() {
                document.replaceString(0, document.getTextLength(), stringPropertyConverter.mergeComments(newDocumentContent, fileText));
              }
            }, "Property Sorter Plugin", null, UndoConfirmationPolicy.DO_NOT_REQUEST_CONFIRMATION);
          }
        });
        messageWindowComponent.say("Properties were sorted.");
      }
      catch (ConvertException convertException) {
        messageWindowComponent.say("There are lines containing non property styled text. File was not changed.");
      }
    }
    else {
      messageWindowComponent.say("Please select an editor window with a properties file.");
    }
  }

  private MessageWindowComponent getMessageComponent() {
    Application application = ApplicationManager.getApplication();
    MessageWindowComponent messageWindowComponent = application.getComponent(MessageWindowComponent.class);
    return messageWindowComponent;
  }
}
