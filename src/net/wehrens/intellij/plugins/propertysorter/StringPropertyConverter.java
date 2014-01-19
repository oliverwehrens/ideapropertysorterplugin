package net.wehrens.intellij.plugins.propertysorter;

import java.util.*;

public class StringPropertyConverter {

  public static final String ideaLineSeparator = "\n";
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
        throw new ConvertException("This does not look like a properties text.");
      }
    }

    return result;
  }

  private boolean lineContainsProperty(String line) {
    return line.contains("=");
  }

  private String extractValue(String[] keyValuePair) {
    StringBuffer result = new StringBuffer();

    if (keyValuePair.length >= 2) { // there could be more than one "=" in the line
      String value = keyValuePair[1].trim();
      // Hack for ResourceBundle: when the value begins with a space
      // ResourceBundle writes key = \   value to the file
      if (value.startsWith("\\ ")) { // starts with one space at least
        value = value.substring(2).trim(); // will remove the rest of the spaces as well
      }
      result.append(value);

      // append the rest of the "="s at the end
      for (int i = 2; i < keyValuePair.length; i++) {
        result.append("=").append(keyValuePair[i]);
      }
    }
    return result.toString();
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
      result.append(key).append("=").append(value).append(ideaLineSeparator);
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
      result.append(line).append(ideaLineSeparator);
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
        existingCommentsForThisProperty = existingCommentsForThisProperty+line+ideaLineSeparator;
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
