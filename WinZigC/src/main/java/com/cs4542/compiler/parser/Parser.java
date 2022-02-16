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

    private static ScannerToken next() {
        return tokens.get(readPointer);
    }

    private static void read(ScannerToken token) {
        if(token.getType() instanceof BasicTokenType) {
            stack.push(new ASTNode(token, null));
        }
        readPointer++;
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
        read(next());
        if (next().getType().equals(PredefinedTokenType.T_PROGRAM)) {
            procedureName();
            assert next().getType().equals(PredefinedTokenType.T_COLON);
            read(next());
            procedureConsts();
            procedureTypes();
            procedureDclns();
            procedureSubProgs();
            procedureBody();
            procedureName();
            assert next().getType() == PredefinedTokenType.T_SINGLEDOT;
            read(next());
            buildTree(ASTTokenType.PROGRAM, 7);
        } else {
            throw new OutOfOrderTokenOrderException(next());
        }
    }

    // Consts -> 'const' Const list ',' ';' => "consts"
    // Consts ->                            => "consts"
    private static void procedureConsts() {
        if(next().getType().equals(PredefinedTokenType.T_CONST)) {
            read(next());
            int n = 1;
            procedureConst();
            while(next().getType().equals(PredefinedTokenType.T_COMMA)) {
                read(next());
                procedureConst();
                n++;
            }
            assert next().getType().equals(PredefinedTokenType.T_SEMICOLON);
            read((next()));
            buildTree(ASTTokenType.CONSTS, n);
        } else {
            buildTree(ASTTokenType.CONSTS, 0);
        }
    }

    // Const -> Name '=' ConstValue => "const"
    private static void procedureConst() {
        procedureName();
        assert next().getType().equals(PredefinedTokenType.T_EQUAL);
        read(next());
        procedureConstValue();
        buildTree(ASTTokenType.CONST, 2);
    }

    // ConstValue -> '<integer>'
    // ConstValue -> '<char>'
    // ConstValue -> Name
    private static void procedureConstValue() {
        if(next().getType().equals(BasicTokenType.INTEGER)) {
            read((next()));
        } else if (next().getType().equals(BasicTokenType.CHAR)) {
            read((next()));
        } else {
            procedureName();
        }
    }

    // Types -> 'type' (Type ';')+      => "types"
    // Types ->                         => "types"
    private static void procedureTypes() {
        if(next().getType().equals(PredefinedTokenType.T_TYPE)) {
            int n =1;
            procedureType();
            assert next().getType().equals(PredefinedTokenType.T_SEMICOLON);
            read(next());
            while(next().getType().equals(BasicTokenType.IDENTIFIER)) {
                procedureType();
                assert next().getType().equals(PredefinedTokenType.T_SEMICOLON);
                read(next());
                n++;
            }
            buildTree(ASTTokenType.TYPES, n);
        } else {
            buildTree(ASTTokenType.TYPES, 0);
        }
    }

    // Type -> Name '=' LitList => "type"
    private static void procedureType() {
        procedureName();
        assert next().getType().equals(PredefinedTokenType.T_EQUAL);
        read(next());
        procedureLitList();
        buildTree(ASTTokenType.TYPE, 2);
    }

    // LitList -> '(' Name list ',' ')' => "lit"
    private static void procedureLitList() {
        int n = 1;
        assert next().getType().equals(PredefinedTokenType.T_OPENBRAC);
        read(next());
        procedureName();
        while(next().getType().equals(PredefinedTokenType.T_COMMA)) {
            read(next());
            procedureName();
            n++;
        }
        assert next().getType().equals(PredefinedTokenType.T_CLOSEBRAC);
        read(next());
        buildTree(ASTTokenType.LIT, n);
    }

    // SubProgs -> Fcn* => "subprogs"
    private static void procedureSubProgs() {
        int n = 0;
        while(next().getType().equals(PredefinedTokenType.T_FUNCTION)) {
            procedureFcn();
            n++;
        }
        buildTree(ASTTokenType.SUBPROGS, n);
    }

    // Fcn -> 'function' Name '(' Params ')' ':' Name ';' Consts Types Dclns Body Name ';' => "fcn";
    private static void procedureFcn() {
    }

    private static void procedureBody() {
    }

    private static void procedureDclns() {
    }

    // Name -> '<identifier>'
    public static void procedureName(){
        assert next().getType().equals(BasicTokenType.IDENTIFIER);
        read(next());
    }

    public static void parse(ArrayList<ScannerToken> tokens) throws OutOfOrderTokenOrderException {
        Parser.tokens = tokens;
        procedureWinzig();
    }
}
