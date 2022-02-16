package com.cs4542.compiler.parser;

import com.cs4542.compiler.exception.OutOfOrderTokenOrderException;
import com.cs4542.compiler.token.ASTToken;
import com.cs4542.compiler.token.ScannerToken;
import com.cs4542.compiler.token.tokentype.ASTTokenType;
import com.cs4542.compiler.token.tokentype.BasicTokenType;
import com.cs4542.compiler.token.tokentype.PredefinedTokenType;

import java.util.ArrayList;
import java.util.Stack;

public class Parser {
    /**
     * TODO
     * 1. Define build tree function
     * 2. Define read_token function
     * 3. Complete procedure for grammar rules of one non terminal
     * 4. Complete all the procedures
     * 5. Print output in the desired manner
     * 6. Test for all 15 examples
     * 7. (Optional) create automated github test action
     */
    private static ArrayList<ScannerToken> tokens;
    private static final Stack<ASTNode> stack = new Stack<>();
    private static int readPointer =0;

    private static ScannerToken getNextToken() {
        return tokens.get(readPointer++);
    }

    private static ScannerToken peekNextToken() {
        return tokens.get(readPointer);
    }

    private static void read(ScannerToken token) {
        if(token.getType() instanceof BasicTokenType) {
            stack.push(new ASTNode(token, null));
        }
    }

    private static void buildTree(ASTTokenType astTokenType, int n) {
        ASTNode headNode = new ASTNode(new ASTToken(astTokenType), null);
        for(int i=0; i< n; i++) {
            ASTNode childNode = stack.pop();
            childNode.setParent(headNode);
            headNode.addChild(childNode);
        }
        stack.push(headNode);
    }

    // Winzig -> 'program' Name ':' Consts Types Dclns SubProgs Body Name '.' => "program"
    public static void procedureWinzig() throws OutOfOrderTokenOrderException {
        ScannerToken next = getNextToken();
        read(next);
        if (next.getType().equals(PredefinedTokenType.T_PROGRAM)) {
            procedureName();
            next = getNextToken();
            assert next.getType().equals(PredefinedTokenType.T_COLON);
            read(next);
            procedureConsts();
            procedureTypes();
            procedureDclns();
            procedureSubProgs();
            procedureBody();
            procedureName();
            next = getNextToken();
            assert next.getType() == PredefinedTokenType.T_SINGLEDOT;
            read(next);
            buildTree(ASTTokenType.PROGRAM, 7);
        } else {
            throw new OutOfOrderTokenOrderException(next);
        }
    }

    // Name -> '<identifier>'
    public static void procedureName(){
        ScannerToken next = getNextToken();
        assert next.getType().equals(BasicTokenType.IDENTIFIER);
        read(next);
    }

    // Consts -> 'const' Const list ',' ';' => "consts"
    // Consts ->                            => "consts"
    private static void procedureConsts() {
        ScannerToken next;
        if(peekNextToken().getType().equals(PredefinedTokenType.T_CONST)) {
            // read 'const' and increment the read pointer
            int n = 1;
            next = getNextToken();
            procedureConst();
            while(peekNextToken().getType().equals(PredefinedTokenType.T_COMMA)) {
                next = getNextToken();
                read(next);
                next = getNextToken();
                assert next.getType().equals(PredefinedTokenType.T_CONST);
                procedureConst();
                n++;
            }
            next = getNextToken();
            assert next.getType().equals(PredefinedTokenType.T_SEMICOLON);
            read((next));
            buildTree(ASTTokenType.CONSTS, n);
        } else {
            buildTree(ASTTokenType.CONSTS, 0);
        }
    }

    // Const -> Name '=' ConstValue => "const"
    private static void procedureConst() {
        ScannerToken next = getNextToken();
        procedureName();
        next = getNextToken();
        assert next.getType().equals(PredefinedTokenType.T_EQUAL);
        read(next);
        next = getNextToken();

    }

    private static void procedureBody() {
    }

    private static void procedureSubProgs() {
    }

    private static void procedureDclns() {
    }

    private static void procedureTypes() {
    }

    public static void parse(ArrayList<ScannerToken> tokens) throws OutOfOrderTokenOrderException {
        Parser.tokens = tokens;
        procedureWinzig();
    }
}
