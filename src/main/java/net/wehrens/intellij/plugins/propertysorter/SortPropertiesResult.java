package net.wehrens.intellij.plugins.propertysorter;

import com.intellij.openapi.vfs.VirtualFile;

public class SortPropertiesResult {
  private VirtualFile virtualFile;
  private boolean success;
  private String message;
  
  public SortPropertiesResult(VirtualFile virtualFile, boolean success, String message) {
    this.virtualFile = virtualFile;
    this.success = success;
    this.message = message;
  }

  public VirtualFile getVirtualFile() {
    return virtualFile;
  }

  public void setVirtualFile(VirtualFile virtualFile) {
    this.virtualFile = virtualFile;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return "PropertySortResult{" +
        "virtualFile=" + virtualFile +
        ", success=" + success +
        ", message='" + message + '\'' +
        '}';
  }
}
