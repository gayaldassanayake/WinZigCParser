package com.cs4542.compiler;

import com.cs4542.compiler.exception.InvalidTokenTypeException;
import com.cs4542.compiler.exception.OutOfOrderTokenException;
import com.cs4542.compiler.lex.Scanner;
import com.cs4542.compiler.lex.Screener;
import com.cs4542.compiler.parser.Parser;
import com.cs4542.compiler.token.ScannerToken;
import com.cs4542.compiler.exception.InvalidTokenException;
import com.cs4542.compiler.util.ProgramReader;
import com.cs4542.compiler.util.Validator;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Compiler {
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
