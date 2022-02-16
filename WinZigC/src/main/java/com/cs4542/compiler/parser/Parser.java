package com.cs4542.compiler.parser;

import com.cs4542.compiler.exception.OutOfOrderTokenException;
import com.cs4542.compiler.token.ASTToken;
import com.cs4542.compiler.token.ScannerToken;
import com.cs4542.compiler.token.tokentype.ASTTokenType;
import com.cs4542.compiler.token.tokentype.BasicTokenType;
import com.cs4542.compiler.token.tokentype.PredefinedTokenType;
import com.cs4542.compiler.token.tokentype.ScannerTokenType;

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

    private static void checkForOutOfOrderToken(ScannerToken token, ScannerTokenType type) throws OutOfOrderTokenException {
        if(!token.getType().equals(type)) {
            throw new OutOfOrderTokenException(token);
        }
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
    public static void procedureWinzig() throws OutOfOrderTokenException {
        if (next().getType().equals(PredefinedTokenType.T_PROGRAM)) {
            procedureName();
            checkForOutOfOrderToken(next(), PredefinedTokenType.T_COLON);
            read(next());
            procedureConsts();
            procedureTypes();
            procedureDclns();
            procedureSubProgs();
            procedureBody();
            procedureName();
            checkForOutOfOrderToken(next(), PredefinedTokenType.T_SINGLEDOT);
            read(next());
            buildTree(ASTTokenType.PROGRAM, 7);
        } else {
            throw new OutOfOrderTokenException(next());
        }
    }

    // Consts -> 'const' Const list ',' ';' => "consts"
    // Consts ->                            => "consts"
    private static void procedureConsts() throws OutOfOrderTokenException {
        if(next().getType().equals(PredefinedTokenType.T_CONST)) {
            read(next());
            int n = 1;
            procedureConst();
            while(next().getType().equals(PredefinedTokenType.T_COMMA)) {
                read(next());
                procedureConst();
                n++;
            }
            checkForOutOfOrderToken(next(), PredefinedTokenType.T_SEMICOLON);
            read((next()));
            buildTree(ASTTokenType.CONSTS, n);
        } else {
            buildTree(ASTTokenType.CONSTS, 0);
        }
    }

    // Const -> Name '=' ConstValue => "const"
    private static void procedureConst() throws OutOfOrderTokenException {
        procedureName();
        checkForOutOfOrderToken(next(), PredefinedTokenType.T_EQUAL);
        read(next());
        procedureConstValue();
        buildTree(ASTTokenType.CONST, 2);
    }

    // ConstValue -> '<integer>'
    // ConstValue -> '<char>'
    // ConstValue -> Name
    private static void procedureConstValue() throws OutOfOrderTokenException {
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
    private static void procedureTypes() throws OutOfOrderTokenException {
        if(next().getType().equals(PredefinedTokenType.T_TYPE)) {
            int n =1;
            procedureType();
            checkForOutOfOrderToken(next(), PredefinedTokenType.T_SEMICOLON);
            read(next());
            while(next().getType().equals(BasicTokenType.IDENTIFIER)) {
                procedureType();
                checkForOutOfOrderToken(next(), PredefinedTokenType.T_SEMICOLON);
                read(next());
                n++;
            }
            buildTree(ASTTokenType.TYPES, n);
        } else {
            buildTree(ASTTokenType.TYPES, 0);
        }
    }

    // Type -> Name '=' LitList => "type"
    private static void procedureType() throws OutOfOrderTokenException {
        procedureName();
        checkForOutOfOrderToken(next(), PredefinedTokenType.T_EQUAL);
        read(next());
        procedureLitList();
        buildTree(ASTTokenType.TYPE, 2);
    }

    // LitList -> '(' Name list ',' ')' => "lit"
    private static void procedureLitList() throws OutOfOrderTokenException {
        int n = 1;
        checkForOutOfOrderToken(next(), PredefinedTokenType.T_OPENBRAC);
        read(next());
        procedureName();
        while(next().getType().equals(PredefinedTokenType.T_COMMA)) {
            read(next());
            procedureName();
            n++;
        }
        checkForOutOfOrderToken(next(), PredefinedTokenType.T_CLOSEBRAC);
        read(next());
        buildTree(ASTTokenType.LIT, n);
    }

    // SubProgs -> Fcn* => "subprogs"
    private static void procedureSubProgs() throws OutOfOrderTokenException {
        int n = 0;
        while(next().getType().equals(PredefinedTokenType.T_FUNCTION)) {
            procedureFcn();
            n++;
        }
        buildTree(ASTTokenType.SUBPROGS, n);
    }

    // Fcn -> 'function' Name '(' Params ')' ':' Name ';' Consts Types Dclns Body Name ';' => "fcn";
    private static void procedureFcn() throws OutOfOrderTokenException {
        checkForOutOfOrderToken(next(), PredefinedTokenType.T_FUNCTION);
        read(next());
        procedureName();
        checkForOutOfOrderToken(next(), PredefinedTokenType.T_OPENBRAC);
        read(next());
        procedureParams();
        checkForOutOfOrderToken(next(), PredefinedTokenType.T_CLOSEBRAC);
        read(next());
        checkForOutOfOrderToken(next(), PredefinedTokenType.T_COLON);
        read(next());
        procedureName();
        checkForOutOfOrderToken(next(), PredefinedTokenType.T_SEMICOLON);
        read(next());
        procedureConsts();
        procedureTypes();
        procedureDclns();
        procedureDclns();
        procedureBody();
        procedureName();
        checkForOutOfOrderToken(next(), PredefinedTokenType.T_SEMICOLON);
        read(next());
        buildTree(ASTTokenType.FCN, 8);
    }

    // Params -> Dcln list ';' => "params";
    private static void procedureParams() throws OutOfOrderTokenException {
        int n = 1;
        procedureDclns();
        while(next().getType().equals(PredefinedTokenType.T_SEMICOLON)) {
            read(next());
            procedureDclns();
            n++;
        }
        buildTree(ASTTokenType.PARAMS, n);
    }

    // Dclns -> 'var' (Dcln ';')+       => "dclns"
    // Dclns ->                         => "dclns"
    private static void procedureDclns() throws OutOfOrderTokenException {
        if(next().getType().equals(PredefinedTokenType.T_VAR)) {
            checkForOutOfOrderToken(next(), PredefinedTokenType.T_VAR);
            read(next());

            int n = 1;
            procedureDcln();
            checkForOutOfOrderToken(next(), PredefinedTokenType.T_SEMICOLON);
            read(next());
            while(next().getType().equals(BasicTokenType.IDENTIFIER)) {
                procedureDcln();
                checkForOutOfOrderToken(next(), PredefinedTokenType.T_SEMICOLON);
                read(next());
                n++;
            }
            buildTree(ASTTokenType.DCLNS, n);
        } else {
            buildTree(ASTTokenType.DCLNS, 0);
        }
    }

    // Dcln -> Name list ',' ':' Name => "var";
    private static void procedureDcln() throws OutOfOrderTokenException {
        int n = 2;
        procedureName();
        while(next().getType().equals(PredefinedTokenType.T_COMMA)) {
            read(next());
            procedureName();
            n++;
        }
        checkForOutOfOrderToken(next(), PredefinedTokenType.T_COLON);
        read(next());
        procedureName();
        buildTree(ASTTokenType.VAR, n);
    }

    // Body -> 'begin' Statement list ';' 'end' => "block";
    private static void procedureBody() {

    }

    // Statement -> Assignment
    // Statement -> 'output' '(' OutExp list ',' ')'                            => "output"
    // Statement -> 'if' Expression 'then' Statement ('else' Statement)?        => "if"
    // Statement -> 'while' Expression 'do' Statement                           => "while"
    // Statement -> 'repeat' Statement list ';' 'until' Expression              => "repeat"
    // Statement -> 'for' '(' ForStat ';' ForExp ';' ForStat ')' Statement      => "for"
    // Statement -> 'loop' Statement list ';' 'pool'                            => "loop"
    // Statement -> 'case' Expression 'of' Caseclauses OtherwiseClause 'end'    => "case"
    // Statement -> 'read' '(' Name list ',' ')'                                => "read"
    // Statement -> 'exit'                                                      => "exit"
    // Statement -> 'return' Expression                                         => "return"
    // Statement -> Body
    // Statement ->                                                             => "<null>"
    private static void procedureStatement() {

    }

    // OutExp -> Expression     => "integer"
    // OutExp -> StringNode     => "string";
    private static void procedureOutExp() {

    }

    // StringNode -> '<string>'
    private static void procedureStringNode() {

    }

    // Caseclauses-> (Caseclause ';')+
    private static void procedureCaseclauses() {

    }

    // Caseclause -> CaseExpression list ',' ':' Statement  => "case_clause"
    private static void procedureCaseclause() {

    }

    // CaseExpression -> ConstValue
    // CaseExpression -> ConstValue '..' ConstValue     => "..";
    private static void procedureCaseExpression() {

    }

    // OtherwiseClause -> 'otherwise' Statement         => "otherwise"
    // OtherwiseClause ->
    private static void procedureOtherwiseClause() {

    }

    // Assignment -> Name ':=' Expression               => "assign"
    // Assignment -> Name ':=:' Name                    => "swap";
    private static void procedureAssignment() {

    }

    // ForStat -> Assignment
    // ForStat ->               => "<null>"
    private static void procedureForStat() {

    }

    // ForExp -> Expression
    // ForExp ->                => "true";
    private static void procedureForExp() {

    }

    // Expression -> Term
    // Expression -> Term '<=' Term     => "<="
    // Expression -> Term '<' Term      => "<"
    // Expression -> Term '>=' Term     => ">="
    // Expression -> Term '>' Term      => ">"
    // Expression -> Term '=' Term      => "="
    // Expression -> Term '<>' Term     => "<>"
    private static void procedureExpression() {

    }

    // Term -> Factor
    // Term -> Term '+' Factor      => "+"
    // Term -> Term '-' Factor      => "-"
    // Term -> Term 'or' Factor     => "or"
    private static void procedureTerm() {

    }

    // Factor -> Factor '*' Primary    => "*"
    // Factor -> Factor '/' Primary    => "/"
    // Factor -> Factor 'and' Primary  => "and"
    // Factor -> Factor 'mod' Primary  => "mod"
    // Factor -> Primary;
    private static void procedureFactor() {

    }

    // Primary -> '-' Primary                       => "-"
    // Primary -> '+' Primary
    // Primary -> 'not' Primary                     => "not"
    // Primary -> 'eof'                             => "eof"
    // Primary -> Name
    // Primary -> '<integer>'
    // Primary -> '<char>'
    // Primary -> Name '(' Expression list ',' ')'  => "call"
    // Primary -> '(' Expression ')'
    // Primary -> 'succ' '(' Expression ')'         => "succ"
    // Primary -> 'pred' '(' Expression ')'         => "pred"
    // Primary -> 'chr' '(' Expression ')'          => "chr"
    // Primary -> -> 'ord' '(' Expression ')'       => "ord";
    private static void procedurePrimary() {

    }

    // Name -> '<identifier>'
    public static void procedureName() throws OutOfOrderTokenException {
        checkForOutOfOrderToken(next(), BasicTokenType.IDENTIFIER);
        read(next());
    }

    public static void parse(ArrayList<ScannerToken> tokens) throws OutOfOrderTokenException {
        Parser.tokens = tokens;
        procedureWinzig();
    }
}
