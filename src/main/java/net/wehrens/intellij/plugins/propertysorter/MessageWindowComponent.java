package net.wehrens.intellij.plugins.propertysorter;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.UndoConfirmationPolicy;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileDocumentManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.*;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Properties;

public class MessageWindowComponent implements ApplicationComponent {
    private PropertySorter propertySorter = new PropertySorter();
    private StringPropertyConverter stringPropertyConverter = new StringPropertyConverter();
    private MyVfsListener myVfsListener = new MyVfsListener();

    public MessageWindowComponent() {
    }

    public void initComponent() {
        // idea from http://arhipov.blogspot.ch/2011/04/code-snippet-intercepting-on-save.html
        MessageBus bus = ApplicationManager.getApplication().getMessageBus();
        MessageBusConnection connection = bus.connect();

        // the following conflicts with Messages.showMessageDialog and freezes IntelliJ
//        connection.subscribe(AppTopics.FILE_DOCUMENT_SYNC, myListener);
        VirtualFileManager.getInstance().addVirtualFileListener(myVfsListener);
    }

    /**
     * A VirtualFileListener (VirtualFileAdapter is a dump implementation of) listens for changes in the files no matter where they come from.
     * <p/>
     * Could be implemented with a FileDocumentManagerListener but this doesn't listen to change from other plugins (e.g. ResourceBundle)
     * nor the filesystem.
     */
    public class MyVfsListener extends VirtualFileAdapter {

        /**
         * Sorts an IDEA VirtualFile in place.
         * Using Wehrens code.
         *
         * @param vf
         * @throws ConvertException
         */
        private void sort(VirtualFile vf) throws ConvertException {
            final Document document = FileDocumentManager.getInstance().getDocument(vf);

            if (document != null) { // todo how can it be?
                Properties properties = stringPropertyConverter.convertString(document.getText());
                List<String> sortedKeys = propertySorter.getSortedKeys(properties);
                final String newDocumentContent = stringPropertyConverter.sortAndConvertProperties(properties, sortedKeys);

                ApplicationManager.getApplication().runWriteAction(new Runnable() {
                    public void run() {
                        // todo is it null?
                        if (ProjectManager.getInstance() == null) {
                            Messages.showMessageDialog(
                                    "The project manager instance is not available, aborting",
                                    "Property Sorter Info",
                                    Messages.getInformationIcon()
                            );
                            return;
                        }

                        // todo are there more than one? why?
                        // You should probably update all open projects: http://devnet.jetbrains.com/message/5283128
                        for (Project project : ProjectManager.getInstance().getOpenProjects()) {

                            CommandProcessor.getInstance().executeCommand(project, new Runnable() {
                                public void run() {
                                    try {
                                        document.replaceString(0, document.getTextLength(), stringPropertyConverter.mergeComments(newDocumentContent, document.getText()));
                                        FileDocumentManager.getInstance().saveDocument(document);
                                    } catch (Exception e) {
                                        Messages.showMessageDialog(
                                                "Exception while replacing",
                                                "Property Sorter Info",
                                                Messages.getInformationIcon()
                                        );
                                    }
                                }
                            }, "Property Sorter Plugin", null, UndoConfirmationPolicy.DO_NOT_REQUEST_CONFIRMATION);
                        }
                    }
                });
            }
        }

        @Override
        public void contentsChanged(VirtualFileEvent event) {
            VirtualFile currentFile = event.getFile();

            // do this only for properties file names
            if (currentFile.getNameWithoutExtension().contains("messages") && currentFile.getName().contains(".properties")) {
                try {
                    sort(currentFile);
                } catch (ConvertException e) {
                    Messages.showMessageDialog(
                            "Convert Exception while sorting " + currentFile.getNameWithoutExtension(),
                            "Property Sorter Info",
                            Messages.getInformationIcon()
                    );
                } catch (Exception e) {
                    Messages.showMessageDialog(
                            "Something bad happened while trying to sort, what have you done?",
                            "Property Sorter Info",
                            Messages.getInformationIcon()
                    );
                }

                // todo: get a balloon on the status bar (bottom)
//        StatusBar statusBar = WindowManager.getInstance().getStatusBar(DataKeys.PROJECT.getData(actionEvent.getDataContext()));
//        JBPopupFactory.getInstance()
//                .createHtmlTextBalloonBuilder("hello world", messageType, null)
//                .setFadeoutTime(7500)
//                .createBalloon()
//                .show(RelativePoint.getCenterOf(statusBar.getComponent()),
//                        Balloon.Position.atRight);
            }
        }
    }

    public void disposeComponent() {
    }

    @NotNull
    public String getComponentName() {
        return "PropertySorterMessageComponent";
    }

    public void say(String text) {
        // Show dialog with message
        Messages.showMessageDialog(
                text,
                "Property Sorter Info",
                Messages.getInformationIcon()
        );
    }
}
