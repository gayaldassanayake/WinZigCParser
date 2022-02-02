import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        String arg = Validator.validateArgs(args);
        if(Validator.help(arg) != 0) {
            return;
        }
        String program = ProgramReader.readProgram(arg);
        Token[] tokens = LexicalAnalyzer.scan(program);
    }
}
