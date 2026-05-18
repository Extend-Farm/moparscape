import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import javax.imageio.ImageIO;

public final class GuiRobot {

  private final Robot robot;

  private GuiRobot() throws AWTException {
    this.robot = new Robot();
    this.robot.setAutoDelay(40);
  }

  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      throw new IllegalArgumentException("Expected an action");
    }
    GuiRobot guiRobot = new GuiRobot();
    switch (args[0]) {
      case "capture" -> guiRobot.capture(args);
      case "click" -> guiRobot.click(args);
      case "type" -> guiRobot.type(args);
      case "key" -> guiRobot.key(args);
      case "sleep" -> guiRobot.sleep(args);
      default -> throw new IllegalArgumentException("Unknown action: " + args[0]);
    }
  }

  private void capture(String[] args) throws IOException {
    if (args.length != 6) {
      throw new IllegalArgumentException("capture requires x y width height path");
    }
    int x = Integer.parseInt(args[1]);
    int y = Integer.parseInt(args[2]);
    int width = Integer.parseInt(args[3]);
    int height = Integer.parseInt(args[4]);
    Path outputPath = Path.of(args[5]);
    Files.createDirectories(outputPath.getParent());
    BufferedImage image = robot.createScreenCapture(new Rectangle(x, y, width, height));
    ImageIO.write(image, "png", outputPath.toFile());
  }

  private void click(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException("click requires x y");
    }
    int x = Integer.parseInt(args[1]);
    int y = Integer.parseInt(args[2]);
    robot.mouseMove(x, y);
    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
  }

  private void type(String[] args) {
    if (args.length != 2) {
      throw new IllegalArgumentException("type requires text");
    }
    for (char character : args[1].toCharArray()) {
      pressCharacter(character);
    }
  }

  private void key(String[] args) {
    if (args.length != 2) {
      throw new IllegalArgumentException("key requires a KeyEvent constant name");
    }
    int keyCode = resolveKeyCode(args[1]);
    robot.keyPress(keyCode);
    robot.keyRelease(keyCode);
  }

  private void sleep(String[] args) throws InterruptedException {
    if (args.length != 2) {
      throw new IllegalArgumentException("sleep requires milliseconds");
    }
    Thread.sleep(Long.parseLong(args[1]));
  }

  private void pressCharacter(char character) {
    boolean shift = false;
    int keyCode;
    if (Character.isUpperCase(character)) {
      shift = true;
      keyCode = KeyEvent.getExtendedKeyCodeForChar(Character.toUpperCase(character));
    } else {
      switch (character) {
        case ' ' -> keyCode = KeyEvent.VK_SPACE;
        case '.' -> keyCode = KeyEvent.VK_PERIOD;
        case '-' -> keyCode = KeyEvent.VK_MINUS;
        case '_' -> {
          shift = true;
          keyCode = KeyEvent.VK_MINUS;
        }
        case '@' -> {
          shift = true;
          keyCode = KeyEvent.VK_2;
        }
        default -> keyCode = KeyEvent.getExtendedKeyCodeForChar(character);
      }
    }
    if (keyCode == KeyEvent.VK_UNDEFINED) {
      throw new IllegalArgumentException("Unsupported character: " + character);
    }
    if (shift) {
      robot.keyPress(KeyEvent.VK_SHIFT);
    }
    robot.keyPress(keyCode);
    robot.keyRelease(keyCode);
    if (shift) {
      robot.keyRelease(KeyEvent.VK_SHIFT);
    }
  }

  private int resolveKeyCode(String name) {
    return switch (name.toUpperCase(Locale.ROOT)) {
      case "ENTER" -> KeyEvent.VK_ENTER;
      case "TAB" -> KeyEvent.VK_TAB;
      case "ESCAPE" -> KeyEvent.VK_ESCAPE;
      case "BACK_SPACE" -> KeyEvent.VK_BACK_SPACE;
      case "UP" -> KeyEvent.VK_UP;
      case "DOWN" -> KeyEvent.VK_DOWN;
      case "LEFT" -> KeyEvent.VK_LEFT;
      case "RIGHT" -> KeyEvent.VK_RIGHT;
      default -> throw new IllegalArgumentException("Unsupported key: " + name);
    };
  }
}
