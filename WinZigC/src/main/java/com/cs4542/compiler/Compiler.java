package com.cs4542.compiler;

import com.cs4542.compiler.lex.Scanner;
import com.cs4542.compiler.lex.Screener;
import com.cs4542.compiler.token.Token;
import com.cs4542.compiler.util.InvalidTokenException;
import com.cs4542.compiler.util.ProgramReader;
import com.cs4542.compiler.util.Validator;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Compiler {
    public static void main(String[] args)
            throws IllegalArgumentException, FileNotFoundException, InvalidTokenException {
        String arg = Validator.validateArgs(args);
        if(Validator.help(arg) != 0) {
            return;
        }
        String program = ProgramReader.readProgram(arg);
        ArrayList<Token> scannerTokens = Scanner.scan(program);
        ArrayList<Token> screenerTokens = Screener.screen(scannerTokens);
    }
}
