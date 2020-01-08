package xyz.keksdose.keksscreen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Properties;

public enum ConfigReader {



  Config("config.properties");

  ConfigReader(String filename) {
    try (InputStream defaultConfig = getClass().getResourceAsStream("/" + filename)) {
      if (!userConfigFile.exists()) {
        Files.copy(defaultConfig, userConfigFile.toPath());
      }
      prop.load(new FileInputStream(userConfigFile));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private Properties prop = new Properties();
  private File userConfigFile = new File("config.properties");

  public static String[] getSCPCommand(String localFile, String remoteName) {
    String[] scpCommand = new String[3];
    scpCommand[0] = "scp";
    scpCommand[1] = localFile;
    scpCommand[2] = Config.prop.getProperty("user") + "@" + Config.prop.getProperty("host") + ":"
        + Config.prop.getProperty("path") + remoteName;
    return scpCommand;
  }

  public static String getDomain() {
    return Config.prop.getProperty("domain");
  }

  public static String getFormat() {
    return Config.prop.getProperty("format");
  }

  public static int getKey() {
    return Integer.valueOf(Config.prop.getProperty("key", "0x0E45"));
  }
}
