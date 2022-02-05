package com.cs4542.compiler;

import com.cs4542.compiler.lex.LexicalAnalyzer;
import com.cs4542.compiler.token.Token;
import com.cs4542.compiler.util.ProgramReader;
import com.cs4542.compiler.util.Validator;

import java.util.ArrayList;

public class Compiler {
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
