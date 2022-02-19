import exception.InvalidTokenTypeException;
import exception.OutOfOrderTokenException;
import lex.Scanner;
import lex.Screener;
import parser.Parser;
import token.ScannerToken;
import exception.InvalidTokenException;
import util.ProgramReader;
import util.Validator;

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
