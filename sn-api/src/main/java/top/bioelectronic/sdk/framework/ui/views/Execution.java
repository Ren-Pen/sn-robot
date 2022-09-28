package top.bioelectronic.sdk.framework.ui.views;

import lombok.Getter;

/**
 * 执行
 */
public abstract class Execution {

    @Getter
    private final String name;

    @Getter
    private final String command;

    public Execution(String command, String name) {
        this.command = command;
        this.name = name;
    }

    public final boolean match(String cmd) {
        return cmd.startsWith(command);
    }

    public final boolean matchAndRun(String cmd) {
        if (!match(cmd)) {
            return false;
        }
        String[] s = cmd.substring(command.length()).trim().split(" ");
        if (s.length == 0) {
            return run();
        } else {
            return run(s);
        }
    }

    public abstract boolean run(String... args);

    @Override
    public String toString() {
        return getName();
    }
}
