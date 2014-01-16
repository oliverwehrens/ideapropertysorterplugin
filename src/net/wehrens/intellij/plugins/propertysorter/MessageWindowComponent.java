package net.wehrens.intellij.plugins.propertysorter;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.ui.Messages;

public class MessageWindowComponent implements ApplicationComponent {
  public MessageWindowComponent() {
  }

  public void initComponent() {
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
