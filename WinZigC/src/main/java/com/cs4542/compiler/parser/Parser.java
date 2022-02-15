package com.cs4542.compiler.parser;

import com.cs4542.compiler.exception.OutOfOrderTokenOrderException;
import com.cs4542.compiler.token.ASTToken;
import com.cs4542.compiler.token.ScannerToken;
import com.cs4542.compiler.token.tokentype.ASTTokenType;
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
    private static final Stack<AST> stack = new Stack<>();
    private static int readPointer =0;

    private static ScannerToken getNextToken() {
        return tokens.get(readPointer++);
    }

    private static ScannerToken peekNextToken() {
        return tokens.get(readPointer);
    }

    private static void read(ScannerToken token) {
        stack.push(new AST(new ASTNode(token, null)));
    }

    private static void buildTree(ASTToken astToken, int n) {
        ASTNode headNode = new ASTNode(astToken, null);
        AST tree = new AST(headNode);
        for(int i=0; i< n; i++) {
            AST childTree = stack.pop();
            ASTNode childNode = childTree.getHead();
            childNode.setParent(headNode);
            headNode.addChild(childNode);
        }
        stack.push(tree);
    }

    public static void procedureWinzig() throws OutOfOrderTokenOrderException {
        int n = 1;
        ScannerToken next = getNextToken();
        read(next);
        if (next.getType().equals(PredefinedTokenType.T_PROGRAM)) {
            procedureName();
            next = getNextToken();
            assert next.getType() == PredefinedTokenType.T_COLON;
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
            buildTree(new ASTToken(ASTTokenType.PROGRAM), n);
        } else {
            throw new OutOfOrderTokenOrderException(next);
        }
    }

    private static void procedureBody() {
    }

    private static void procedureSubProgs() {
    }

    private static void procedureDclns() {
    }

    private static void procedureTypes() {
    }

    private static void procedureConsts() {
    }

    public static void procedureName(){

    }

    public static void parse(ArrayList<ScannerToken> tokens) throws OutOfOrderTokenOrderException {
        Parser.tokens = tokens;
        procedureWinzig();
    }
}
