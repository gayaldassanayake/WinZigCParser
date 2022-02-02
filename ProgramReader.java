import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ProgramReader {
//    private final String path;
//    public ProgramReader (String path) {
//        this.path = path;
//    }
    public static String readProgram(String path) throws FileNotFoundException {
        File file = new File(path);
        Scanner reader = new Scanner(file);
        String program = reader.useDelimiter("\\Z").next();
        reader.close();
        System.out.println(program);
        return program;
    }
}
