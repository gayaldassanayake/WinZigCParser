import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

enum TokenType {
    PREDEFINED,
    WHITESPACE,
    IDENTIFIER,
    INTEGER,
    CHAR,
    STRING,
    COMMENT
}

public class Token {
    private static final ArrayList<String> predefinedTokens = new ArrayList<>();
    String value;
    TokenType type;

    private static void initializePredefTokens() throws IOException {
        BufferedReader bufReader = new BufferedReader(new FileReader("predeftokens.txt"));

        String line = bufReader.readLine().strip();
        while (line != null) {
            predefinedTokens.add(line);
            line = bufReader.readLine();
        }
        bufReader.close();
    }
    static {
        try {
            initializePredefTokens();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getPredefinedTokens() {
        return predefinedTokens;
    }

    public Token(String value, TokenType type) {
        this.value = value;
        this.type = type;
    }
}
