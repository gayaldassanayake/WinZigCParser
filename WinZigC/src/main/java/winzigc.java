import compiler.evaluator.Evaluator;
import compiler.exception.InvalidTokenTypeException;
import compiler.exception.OutOfOrderTokenException;
import compiler.lex.Scanner;
import compiler.lex.Screener;
import compiler.parser.Parser;
import compiler.token.ASTNode;
import compiler.token.ScannerToken;
import compiler.exception.InvalidTokenException;
import compiler.util.ProgramMode;
import compiler.util.ProgramReader;
import compiler.util.Validator;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class winzigc {
    public static void main(String[] args) {
        ProgramMode mode = Validator.validateArgs(args);
        if(mode.equals(ProgramMode.HELP)) {
            return;
        }
        String path = args[1];
        try {
            String program = ProgramReader.readProgram(path);
            ArrayList<ScannerToken> scannerTokens = Scanner.scan(program);
            ArrayList<ScannerToken> screenerTokens = Screener.screen(scannerTokens);
            ASTNode head = Parser.parse(screenerTokens, mode);
            if(mode.equals(ProgramMode.CODE)) {
                Evaluator.evaluate(head);
            }
        } catch (FileNotFoundException | InvalidTokenException |
                OutOfOrderTokenException | InvalidTokenTypeException e) {
            e.printStackTrace();
        }
    }
}
