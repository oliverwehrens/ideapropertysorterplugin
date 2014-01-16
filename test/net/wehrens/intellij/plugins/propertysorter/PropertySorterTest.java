package net.wehrens.intellij.plugins.propertysorter;

import java.util.List;
import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PropertySorterTest {

  private PropertySorter propertySorter = new PropertySorter();

  @Test(groups = "sortTest")
  public void testSort() {

    Properties prop = new Properties();
    prop.put("D", "1");
    prop.put("A", "2");
    prop.put("B", "3");

    List<String> sortedKeys = propertySorter.getSortedKeys(prop);

    Assert.assertSame(prop.get("A"), prop.get(sortedKeys.get(0)));
    Assert.assertSame(prop.get("B"), prop.get(sortedKeys.get(1)));
    Assert.assertSame(prop.get("D"), prop.get(sortedKeys.get(2)));
  }
}
