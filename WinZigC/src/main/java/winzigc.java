import compiler.exception.InvalidTokenTypeException;
import compiler.exception.OutOfOrderTokenException;
import compiler.lex.Scanner;
import compiler.lex.Screener;
import compiler.parser.Parser;
import compiler.token.ScannerToken;
import compiler.exception.InvalidTokenException;
import compiler.util.ProgramReader;
import compiler.util.Validator;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class winzigc {
    public static void main(String[] args) {
        if(!Validator.validateArgs(args)) {
            return;
        }
        String path = args[1];
        try {
            String program = ProgramReader.readProgram(path);
            ArrayList<ScannerToken> scannerTokens = Scanner.scan(program);
            ArrayList<ScannerToken> screenerTokens = Screener.screen(scannerTokens);
            Parser.parse(screenerTokens);
        } catch (FileNotFoundException | InvalidTokenException |
                OutOfOrderTokenException | InvalidTokenTypeException e) {
            e.printStackTrace();
        }
    }
}
