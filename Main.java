import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
        // TODO: fix all error throwing code
        String arg = Validator.validateArgs(args);
        if(Validator.help(arg) != 0) {
            return;
        }
        String program = ProgramReader.readProgram(arg);
        ArrayList<Token> tokens = LexicalAnalyzer.scan(program);
    }
}
