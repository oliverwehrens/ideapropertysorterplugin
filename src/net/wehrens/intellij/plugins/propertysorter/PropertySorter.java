package net.wehrens.intellij.plugins.propertysorter;

import java.util.*;

public class PropertySorter {

  public List<String> getSortedKeys(Properties properties) {

    Enumeration<Object> keys = properties.keys();
    List<String> elementList = new ArrayList<String>();
    while (keys.hasMoreElements()) {
      elementList.add((String) keys.nextElement());
    }

    Collections.sort(elementList);

    return elementList;
  }
}
