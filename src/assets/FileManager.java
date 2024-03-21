package assets;

import exceptions.BlankFileException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileManager {
  public static void writeToFile(File file, byte[] bytes) {
    try (var fos = new FileOutputStream(file, true)) {
      fos.write(bytes);
    } catch (FileNotFoundException e) {
      try {
        file.getParentFile().mkdirs();
        file.createNewFile();
        writeToFile(file, bytes);
      } catch (IOException ignored) {
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void rewriteToFile(File file, String oldChars, String replacement)
      throws BlankFileException {
    var fileStructure = readInFile(file);
    try (var fos = new FileOutputStream(file, false)) {
      fos.write(fileStructure.replace(oldChars, replacement).getBytes(StandardCharsets.UTF_8));
    } catch (FileNotFoundException e) {
      try {
        file.getParentFile().mkdirs();
        file.createNewFile();
        rewriteToFile(file, oldChars, replacement);
      } catch (IOException ignored) {
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String readInFile(File file) throws BlankFileException {
    try {
      if (!file.exists()) {
        file.getParentFile().mkdirs();
        file.createNewFile();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    try (var fis = new FileInputStream(file)) {
      var strBuilder = new StringBuilder();
      int i;
      while ((i = fis.read()) != -1) strBuilder.append((char) i);
      if (strBuilder.capacity() != 0) return strBuilder.toString();
      else throw new BlankFileException("No data in file");
    } catch (IOException e) {
      return readInFile(file);
    }
  }
}
