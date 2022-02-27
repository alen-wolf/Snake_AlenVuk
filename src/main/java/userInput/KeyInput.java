package userInput;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;

public class KeyInput {
    private Direction key = Direction.RIGHT;

    public KeyInput(){
        new Thread(()->{
            try(Terminal terminal= TerminalBuilder.terminal()) {
                while (true){
                    setKey(getInputs(terminal.reader().read()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static Direction getInputs(int ch) {
        return switch (ch) {
            case 65 -> Direction.UP;
            case 66 -> Direction.DOWN;
            case 68 -> Direction.LEFT;
            case 67 -> Direction.RIGHT;
            default -> Direction.SPACE;
        };
    }


    public Direction getKey() {
        return key;
    }

    public KeyInput setKey(Direction key) {
        this.key = key;
        return this;
    }
}
