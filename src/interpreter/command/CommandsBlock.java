package interpreter.command;

import interpreter.util.Arguments;
import interpreter.util.Instance;

import java.util.ArrayList;
import java.util.List;

public class CommandsBlock extends Command {

    private List<Command> cmds;

    public CommandsBlock() {
        super(-1);

        cmds = new ArrayList<Command>();
    }

    public void addCommand(Command c) {
        cmds.add(c);
    }

    @Override
    public void execute(Instance self, Arguments args) {
        for (Command c : cmds) {
            c.execute(self, args);
        }
    }

}
