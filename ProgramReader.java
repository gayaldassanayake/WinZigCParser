import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ProgramReader {
    public static String readProgram(String path) throws FileNotFoundException {
        File file = new File(path);
        Scanner reader = new Scanner(file);
        String program = reader.useDelimiter("\\Z").next();
        reader.close();
        return program;
    }
}
