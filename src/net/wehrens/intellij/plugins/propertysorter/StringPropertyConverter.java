package net.wehrens.intellij.plugins.propertysorter;

import java.util.*;

public class StringPropertyConverter {

  public static final String ideaLineSeperator = "\n";
  private final String COMMENTS_AT_THE_END = "";

  public Properties convertString(String stringToBeConverted) throws ConvertException {

    String[] lines = stringToBeConverted.split("\n");

    Properties result = new Properties();

    for (String line : lines) {

      if (lineContainsProperty(line)) {
        String[] keyValuePair = line.split("=");
        String key = extractKey(keyValuePair);
        String value = extractValue(keyValuePair);
        result.put(key, value);
      }
      else if (!lineIsAComment(line) && !lineIsEmpty(line)) {
        throw new ConvertException("This is does not look like a properties text.");
      }
    }

    return result;
  }

  private boolean lineContainsProperty(String line) {
    return line.contains("=");
  }

  private String extractValue(String[] keyValuePair) {
    StringBuffer value = new StringBuffer();
    if (keyValuePair.length >= 2) {
      value = value.append(keyValuePair[1].trim());
      for (int i = 2; i < keyValuePair.length; i++) {
        value.append("=").append(keyValuePair[i]);
      }
    }
    return value.toString();
  }

  private String extractKey(String[] keyValuePair) {
    return keyValuePair[0].trim();
  }

  private boolean lineIsEmpty(String line) {
    return line.trim().equals("");
  }

  private boolean lineIsAComment(String line) {
    return line.trim().startsWith("#");
  }

  public String sortAndConvertProperties(Properties properties, List<String> orderedKeyList) {
    StringBuilder result = new StringBuilder();

    for (String key : orderedKeyList) {
      String value = properties.getProperty(key);
      result.append(key).append("=").append(value).append(ideaLineSeperator);
    }

    return result.toString();
  }

  public String mergeComments(String sortedPropertyText, String originalText) {

    Map<String, String> commentsForProperties = getCommentsForProperties(originalText);

    StringBuffer result = new StringBuffer();

    String[] lines = sortedPropertyText.split("\n");
    for (String line : lines) {
      if (commentsForProperties.containsKey(line)) {
        line = commentsForProperties.get(line)+line;
      }
      result.append(line).append(ideaLineSeperator);
    }

    if (commentsForProperties.containsKey(COMMENTS_AT_THE_END)) {
      result.append(commentsForProperties.get(COMMENTS_AT_THE_END));
    }

    return result.toString();
  }

  private Map<String, String> getCommentsForProperties(String originalText) {
    Map<String, String> commentsForProperties = new HashMap<String,String>();
    String[] lines = originalText.split("\n");
    for (int i=0;i<lines.length;i++) {
      String line = lines[i];
      if (lineIsAComment(line)) {
        String nextPropertyLine = getNextPropertyLine(i, lines);
        String existingCommentsForThisProperty ="";
        if (commentsForProperties.containsKey(nextPropertyLine)) {
          existingCommentsForThisProperty = commentsForProperties.get(nextPropertyLine);
        }
        existingCommentsForThisProperty = existingCommentsForThisProperty+line+ideaLineSeperator;
        commentsForProperties.put(nextPropertyLine, existingCommentsForThisProperty);
      }
    }
    return commentsForProperties;
  }

  private String getNextPropertyLine(int lineToStart, String[] allLines) {
    for (int i=lineToStart;i<allLines.length;i++) {
      String line = allLines[i];
      if (lineContainsProperty(line)) {
        return line;
      }
    }
    return COMMENTS_AT_THE_END;
  }
}
