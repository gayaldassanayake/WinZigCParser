package compiler.parser;

import compiler.exception.InvalidTokenTypeException;
import compiler.exception.OutOfOrderTokenException;
import compiler.token.ASTToken;
import compiler.token.BasicToken;
import compiler.token.ScannerToken;
import compiler.token.tokentype.ASTTokenType;
import compiler.token.tokentype.ValueTokenType;
import compiler.token.tokentype.PredefinedTokenType;
import compiler.token.tokentype.ScannerTokenType;
import compiler.util.Util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class Parser {
    private static ArrayList<ScannerToken> tokens;
    private static final Stack<ASTNode> stack = new Stack<>();
    private static int readPointer =0;

    private static ScannerToken next() {
        return tokens.get(readPointer);
    }

    private static void read(ScannerToken token) throws InvalidTokenTypeException {
        if(token.getType() instanceof ValueTokenType) {
            ASTNode basicToken = new ASTNode(new BasicToken((ValueTokenType) token.getType()), null);
            ASTNode valueToken = new ASTNode(token, basicToken);
            basicToken.addChild(valueToken);
            stack.push(basicToken);
        }
        readPointer++;
    }

    private static void read(ScannerToken token, ScannerTokenType type)
            throws OutOfOrderTokenException, InvalidTokenTypeException {
        if(!token.getType().equals(type)) {
            throw new OutOfOrderTokenException(token);
        }
        read(token);
    }

    private static void buildTree(ASTTokenType astTokenType, int n) throws InvalidTokenTypeException {
        ASTNode headNode = new ASTNode(new ASTToken(astTokenType), null);
        Stack<ASTNode> reverseStack = new Stack<>();
        for(int i=0; i< n; i++) {
            // to avoid reversing the order of children in parent node
            reverseStack.push(stack.pop());
        }
        for(int i=0; i<n; i++) {
            ASTNode childNode = reverseStack.pop();
            childNode.setParent(headNode);
            headNode.addChild(childNode);
        }
        stack.push(headNode);
    }

    // Winzig -> 'program' Name ':' Consts Types Dclns SubProgs Body Name '.' => "program"
    public static void procedureWinzig() throws OutOfOrderTokenException, InvalidTokenTypeException {
        if (next().getType().equals(PredefinedTokenType.T_PROGRAM)) {
            read(next());
            procedureName();
            read(next(), PredefinedTokenType.T_COLON);
            procedureConsts();
            procedureTypes();
            procedureDclns();
            procedureSubProgs();
            procedureBody();
            procedureName();
            read(next(), PredefinedTokenType.T_SINGLEDOT);
            buildTree(ASTTokenType.PROGRAM, 7);
        } else {
            throw new OutOfOrderTokenException(next());
        }
    }

    // Consts -> 'const' Const list ',' ';' => "consts"
    // Consts ->                            => "consts"
    private static void procedureConsts() throws OutOfOrderTokenException, InvalidTokenTypeException {
        if(next().getType().equals(PredefinedTokenType.T_CONST)) {
            read(next());
            int n = 1;
            procedureConst();
            while(next().getType().equals(PredefinedTokenType.T_COMMA)) {
                read(next());
                procedureConst();
                n++;
            }
            read(next(), PredefinedTokenType.T_SEMICOLON);
            buildTree(ASTTokenType.CONSTS, n);
        } else {
            buildTree(ASTTokenType.CONSTS, 0);
        }
    }

    // Const -> Name '=' ConstValue => "const"
    private static void procedureConst() throws OutOfOrderTokenException, InvalidTokenTypeException {
        procedureName();
        read(next(), PredefinedTokenType.T_EQUAL);
        procedureConstValue();
        buildTree(ASTTokenType.CONST, 2);
    }

    // ConstValue -> '<integer>'
    // ConstValue -> '<char>'
    // ConstValue -> Name
    private static void procedureConstValue() throws OutOfOrderTokenException, InvalidTokenTypeException {
        if(next().getType().equals(ValueTokenType.INTEGER)) {
            read((next()));
        } else if (next().getType().equals(ValueTokenType.CHAR)) {
            read((next()));
        } else {
            procedureName();
        }
    }

    // Types -> 'type' (Type ';')+      => "types"
    // Types ->                         => "types"
    private static void procedureTypes() throws OutOfOrderTokenException, InvalidTokenTypeException {
        if(next().getType().equals(PredefinedTokenType.T_TYPE)) {
            read(next());
            int n =1;
            procedureType();
            read(next(), PredefinedTokenType.T_SEMICOLON);
            while(next().getType().equals(ValueTokenType.IDENTIFIER)) {
                procedureType();
                read(next(), PredefinedTokenType.T_SEMICOLON);
                n++;
            }
            buildTree(ASTTokenType.TYPES, n);
        } else {
            buildTree(ASTTokenType.TYPES, 0);
        }
    }

    // Type -> Name '=' LitList => "type"
    private static void procedureType() throws OutOfOrderTokenException, InvalidTokenTypeException {
        procedureName();
        read(next(), PredefinedTokenType.T_EQUAL);
        procedureLitList();
        buildTree(ASTTokenType.TYPE, 2);
    }

    // LitList -> '(' Name list ',' ')' => "lit"
    private static void procedureLitList() throws OutOfOrderTokenException, InvalidTokenTypeException {
        int n = 1;
        read(next(), PredefinedTokenType.T_OPENBRAC);
        procedureName();
        while(next().getType().equals(PredefinedTokenType.T_COMMA)) {
            read(next());
            procedureName();
            n++;
        }
        read(next(), PredefinedTokenType.T_CLOSEBRAC);
        buildTree(ASTTokenType.LIT, n);
    }

    // SubProgs -> Fcn* => "subprogs"
    private static void procedureSubProgs() throws OutOfOrderTokenException, InvalidTokenTypeException {
        int n = 0;
        while(next().getType().equals(PredefinedTokenType.T_FUNCTION)) {
            procedureFcn();
            n++;
        }
        buildTree(ASTTokenType.SUBPROGS, n);
    }

    // Fcn -> 'function' Name '(' Params ')' ':' Name ';' Consts Types Dclns Body Name ';' => "fcn";
    private static void procedureFcn() throws OutOfOrderTokenException, InvalidTokenTypeException {
        read(next(), PredefinedTokenType.T_FUNCTION);
        procedureName();
        read(next(), PredefinedTokenType.T_OPENBRAC);
        procedureParams();
        read(next(), PredefinedTokenType.T_CLOSEBRAC);
        read(next(), PredefinedTokenType.T_COLON);
        procedureName();
        read(next(), PredefinedTokenType.T_SEMICOLON);
        procedureConsts();
        procedureTypes();
        procedureDclns();
        procedureBody();
        procedureName();
        read(next(), PredefinedTokenType.T_SEMICOLON);
        buildTree(ASTTokenType.FCN, 8);
    }

    // Params -> Dcln list ';' => "params";
    private static void procedureParams() throws OutOfOrderTokenException, InvalidTokenTypeException {
        int n = 1;
        procedureDcln();
        while(next().getType().equals(PredefinedTokenType.T_SEMICOLON)) {
            read(next());
            procedureDcln();
            n++;
        }
        buildTree(ASTTokenType.PARAMS, n);
    }

    // Dclns -> 'var' (Dcln ';')+       => "dclns"
    // Dclns ->                         => "dclns"
    private static void procedureDclns() throws OutOfOrderTokenException, InvalidTokenTypeException {
        if(next().getType().equals(PredefinedTokenType.T_VAR)) {
            read(next());
            int n = 1;
            procedureDcln();
            read(next(), PredefinedTokenType.T_SEMICOLON);
            while(next().getType().equals(ValueTokenType.IDENTIFIER)) {
                procedureDcln();
                read(next(), PredefinedTokenType.T_SEMICOLON);
                n++;
            }
            buildTree(ASTTokenType.DCLNS, n);
        } else {
            buildTree(ASTTokenType.DCLNS, 0);
        }
    }

    // Dcln -> Name list ',' ':' Name => "var";
    private static void procedureDcln() throws OutOfOrderTokenException, InvalidTokenTypeException {
        int n = 2;
        procedureName();
        while(next().getType().equals(PredefinedTokenType.T_COMMA)) {
            read(next());
            procedureName();
            n++;
        }
        read(next(), PredefinedTokenType.T_COLON);
        procedureName();
        buildTree(ASTTokenType.VAR, n);
    }

    // Body -> 'begin' Statement list ';' 'end' => "block";
    private static void procedureBody() throws OutOfOrderTokenException, InvalidTokenTypeException {
        int n = 1;
        read(next(), PredefinedTokenType.T_BEGIN);
        procedureStatement();
        while (next().getType().equals(PredefinedTokenType.T_SEMICOLON)) {
            read(next());
            procedureStatement();
            n++;
        }
        read(next(), PredefinedTokenType.T_END);
        buildTree(ASTTokenType.BLOCK, n);
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
    private static void procedureStatement() throws InvalidTokenTypeException, OutOfOrderTokenException {
        ScannerTokenType type = next().getType();
        if(type.equals(ValueTokenType.IDENTIFIER)) {
            // Assignment
            procedureAssignment();
        } else if(type.equals(PredefinedTokenType.T_OUTPUT)) {
            // output
            int n = 1;
            read(next());
            read(next(), PredefinedTokenType.T_OPENBRAC);
            procedureOutExp();
            while (next().getType().equals(PredefinedTokenType.T_COMMA)) {
                read(next());
                procedureOutExp();
                n++;
            }
            read(next(), PredefinedTokenType.T_CLOSEBRAC);
            buildTree(ASTTokenType.OUTPUT, n);
        } else if(type.equals(PredefinedTokenType.T_IF)) {
            // if
            int n = 2;
            read(next());
            procedureExpression();
            read(next(), PredefinedTokenType.T_THEN);
            procedureStatement();
            if(next().getType().equals(PredefinedTokenType.T_ELSE)) {
                read(next());
                procedureStatement();
                n++;
            }
            buildTree(ASTTokenType.IF, n);
        } else if(type.equals(PredefinedTokenType.T_WHILE)) {
            // while
            read(next());
            procedureExpression();
            read(next(), PredefinedTokenType.T_DO);
            procedureStatement();
            buildTree(ASTTokenType.WHILE, 2);
        } else if(type.equals(PredefinedTokenType.T_REPEAT)) {
            // repeat
            int n = 2;
            read(next());
            procedureStatement();
            while(next().getType().equals(PredefinedTokenType.T_SEMICOLON)) {
                read(next());
                procedureStatement();
                n++;
            }
            read(next(), PredefinedTokenType.T_UNTIL);
            procedureExpression();
            buildTree(ASTTokenType.REPEAT, n);
        } else if(type.equals(PredefinedTokenType.T_FOR)) {
            // for
            read(next());
            read(next(), PredefinedTokenType.T_OPENBRAC);
            procedureForStat();
            read(next(), PredefinedTokenType.T_SEMICOLON);
            procedureForExp();
            read(next(), PredefinedTokenType.T_SEMICOLON);
            procedureForStat();
            read(next(), PredefinedTokenType.T_CLOSEBRAC);
            procedureStatement();
            buildTree(ASTTokenType.FOR, 4);
        } else if(type.equals(PredefinedTokenType.T_LOOP)) {
            // loop
            int n = 1;
            read(next());
            procedureStatement();
            while(next().getType().equals(PredefinedTokenType.T_SEMICOLON)) {
                read(next());
                procedureStatement();
                n++;
            }
            read(next(), PredefinedTokenType.T_POOL);
            buildTree(ASTTokenType.LOOP, n);
        } else if(type.equals(PredefinedTokenType.T_CASE)) {
            int n = 1;
            // case
            read(next());
            procedureExpression();
            read(next(), PredefinedTokenType.T_OF);
            n += procedureCaseclauses();
            n += procedureOtherwiseClause();
            read(next(), PredefinedTokenType.T_END);
            buildTree(ASTTokenType.CASE, n);
        } else if(type.equals(PredefinedTokenType.T_READ)) {
            // read
            int n = 1;
            read(next());
            read(next(), PredefinedTokenType.T_OPENBRAC);
            procedureName();
            while(next().getType().equals(PredefinedTokenType.T_COMMA)) {
                read(next());
                procedureName();
                n++;
            }
            read(next(), PredefinedTokenType.T_CLOSEBRAC);
            buildTree(ASTTokenType.READ, n);
        } else if(type.equals(PredefinedTokenType.T_EXIT)) {
            // exit
            read(next());
            buildTree(ASTTokenType.EXIT, 0);
        } else if(type.equals(PredefinedTokenType.T_RETURN)) {
            // return
            read(next());
            procedureExpression();
            buildTree(ASTTokenType.RETURN, 1);
        } else if(type.equals(PredefinedTokenType.T_BEGIN)) {
            // Body
            procedureBody();
        } else {
            buildTree(ASTTokenType.NULL, 0);
        }
    }

    // OutExp -> Expression     => "integer"
    // OutExp -> StringNode     => "string";
    private static void procedureOutExp() throws InvalidTokenTypeException, OutOfOrderTokenException {
        if(next().getType().equals(ValueTokenType.STRING)) {
            procedureStringNode();
            buildTree(ASTTokenType.STRING, 1);
        } else {
            procedureExpression();
            buildTree(ASTTokenType.INTEGER, 1);
        }
    }

    // StringNode -> '<string>'
    private static void procedureStringNode() throws OutOfOrderTokenException, InvalidTokenTypeException {
        read(next(), ValueTokenType.STRING);
    }

    // Caseclauses-> (Caseclause ';')+
    private static int procedureCaseclauses() throws OutOfOrderTokenException, InvalidTokenTypeException {
        int n = 1;
        procedureCaseclause();
        read(next(), PredefinedTokenType.T_SEMICOLON);
        ScannerTokenType type = next().getType();
        while (type.equals(ValueTokenType.INTEGER) || type.equals(ValueTokenType.CHAR) ||
                type.equals(ValueTokenType.IDENTIFIER)) {
            procedureCaseclause();
            read(next(), PredefinedTokenType.T_SEMICOLON);
            type = next().getType();
            n++;
        }
        return n;
    }

    // Caseclause -> CaseExpression list ',' ':' Statement  => "case_clause"
    private static void procedureCaseclause() throws InvalidTokenTypeException, OutOfOrderTokenException {
        int n = 2;
        procedureCaseExpression();
        while (next().getType().equals(PredefinedTokenType.T_COMMA)) {
            read(next());
            procedureCaseExpression();
            n++;
        }
        read(next(), PredefinedTokenType.T_COLON);
        procedureStatement();
        buildTree(ASTTokenType.CASE_CLAUSE, n);
    }

    // CaseExpression -> ConstValue
    // CaseExpression -> ConstValue '..' ConstValue     => "..";
    private static void procedureCaseExpression() throws OutOfOrderTokenException, InvalidTokenTypeException {
        procedureConstValue();
        if(next().getType().equals(PredefinedTokenType.T_DOTS)) {
            read(next());
            procedureConstValue();
            buildTree(ASTTokenType.DOUBLE_DOT, 2);
        }
    }

    // OtherwiseClause -> 'otherwise' Statement         => "otherwise"
    // OtherwiseClause ->
    private static int procedureOtherwiseClause() throws InvalidTokenTypeException, OutOfOrderTokenException {
        if(next().getType().equals(PredefinedTokenType.T_OTHERWISE)) {
            read(next());
            procedureStatement();
            buildTree(ASTTokenType.OTHERWISE, 1);
            return 1;
        }
        return 0;
    }

    // Assignment -> Name ':=' Expression               => "assign"
    // Assignment -> Name ':=:' Name                    => "swap";
    private static void procedureAssignment() throws OutOfOrderTokenException, InvalidTokenTypeException {
        procedureName();
        if(next().getType().equals(PredefinedTokenType.T_ASSIGN)) {
            read(next());
            procedureExpression();
            buildTree(ASTTokenType.ASSIGN, 2);
        } else {
            read(next(), PredefinedTokenType.T_SWAP);
            procedureName();
            buildTree(ASTTokenType.SWAP, 2);
        }
    }

    // ForStat -> Assignment
    // ForStat ->               => "<null>"
    private static void procedureForStat() throws OutOfOrderTokenException, InvalidTokenTypeException {
        if(next().getType().equals(ValueTokenType.IDENTIFIER)) {
            procedureAssignment();
        } else {
            buildTree(ASTTokenType.NULL, 0);
        }
    }

    // ForExp -> Expression
    // ForExp ->                => "true";
    private static void procedureForExp() throws InvalidTokenTypeException, OutOfOrderTokenException {
        Set<ScannerTokenType> firstSet = new HashSet<>();
        firstSet.add(PredefinedTokenType.T_MINUS);
        firstSet.add(PredefinedTokenType.T_PLUS);
        firstSet.add(PredefinedTokenType.T_NOT);
        firstSet.add(ValueTokenType.IDENTIFIER);
        firstSet.add(ValueTokenType.INTEGER);
        firstSet.add(ValueTokenType.CHAR);
        firstSet.add(PredefinedTokenType.T_OPENBRAC);
        firstSet.add(PredefinedTokenType.T_SUCC);
        firstSet.add(PredefinedTokenType.T_PRED);
        firstSet.add(PredefinedTokenType.T_CHR);
        firstSet.add(PredefinedTokenType.T_ORD);

        if(firstSet.contains(next().getType())) {
            procedureExpression();
        } else {
            buildTree(ASTTokenType.TRUE, 0);
        }
    }

    // Expression -> Term
    // Expression -> Term '<=' Term     => "<="
    // Expression -> Term '<' Term      => "<"
    // Expression -> Term '>=' Term     => ">="
    // Expression -> Term '>' Term      => ">"
    // Expression -> Term '=' Term      => "="
    // Expression -> Term '<>' Term     => "<>"
    private static void procedureExpression() throws InvalidTokenTypeException, OutOfOrderTokenException {
        procedureTerm();
        ScannerTokenType type = next().getType();
        if(type.equals(PredefinedTokenType.T_LTE) || type.equals(PredefinedTokenType.T_LT) ||
                type.equals(PredefinedTokenType.T_GTE) || type.equals(PredefinedTokenType.T_GT) ||
                type.equals(PredefinedTokenType.T_EQUAL) || type.equals(PredefinedTokenType.T_NE) ) {
            read(next());
            procedureTerm();
            ASTTokenType astTokenType = Util.getASTTypeForPredefType((PredefinedTokenType) type);
            buildTree(astTokenType, 2);
        }
    }

    // Term -> Factor
    // Term -> Term '+' Factor      => "+"
    // Term -> Term '-' Factor      => "-"
    // Term -> Term 'or' Factor     => "or"
    private static void procedureTerm() throws InvalidTokenTypeException, OutOfOrderTokenException {
        procedureFactor();
        ScannerTokenType type = next().getType();
        while(type.equals(PredefinedTokenType.T_PLUS) || type.equals(PredefinedTokenType.T_MINUS) ||
                type.equals(PredefinedTokenType.T_OR)) {
            read(next());
            procedureFactor();
            ASTTokenType astTokenType = Util.getASTTypeForPredefType((PredefinedTokenType) type);
            buildTree(astTokenType, 2);
            type = next().getType();
        }
    }

    // Factor -> Factor '*' Primary    => "*"
    // Factor -> Factor '/' Primary    => "/"
    // Factor -> Factor 'and' Primary  => "and"
    // Factor -> Factor 'mod' Primary  => "mod"
    // Factor -> Primary;
    private static void procedureFactor() throws InvalidTokenTypeException, OutOfOrderTokenException {
        procedurePrimary();
        ScannerTokenType type = next().getType();
        while (type.equals(PredefinedTokenType.T_MULTIPLY) || type.equals(PredefinedTokenType.T_DIVIDE) ||
                type.equals(PredefinedTokenType.T_AND) || type.equals(PredefinedTokenType.T_MOD)) {
            read(next());
            procedurePrimary();
            ASTTokenType astTokenType = Util.getASTTypeForPredefType((PredefinedTokenType) type);
            buildTree(astTokenType, 2);
            type = next().getType();
        }
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
    private static void procedurePrimary() throws OutOfOrderTokenException, InvalidTokenTypeException {
        ScannerTokenType type = next().getType();
        if(type.equals(PredefinedTokenType.T_MINUS) || type.equals(PredefinedTokenType.T_NOT)) {
            read(next());
            procedurePrimary();
            ASTTokenType astTokenType = Util.getASTTypeForPredefType((PredefinedTokenType) type);
            buildTree(astTokenType, 1);
        } else if(type.equals(PredefinedTokenType.T_PLUS)) {
            read(next());
            procedurePrimary();
        }else if(type.equals(PredefinedTokenType.T_EOF)) {
            read(next());
            buildTree(ASTTokenType.EOF, 0);
        } else if(type.equals(ValueTokenType.INTEGER) || type.equals(ValueTokenType.CHAR)) {
            read(next());
        } else if(type.equals(PredefinedTokenType.T_OPENBRAC)) {
            read(next());
            procedureExpression();
            read(next(), PredefinedTokenType.T_CLOSEBRAC);
        }else if(type.equals(PredefinedTokenType.T_SUCC) || type.equals(PredefinedTokenType.T_PRED) ||
                type.equals(PredefinedTokenType.T_CHR) || type.equals(PredefinedTokenType.T_ORD)) {
            read(next());
            read(next(), PredefinedTokenType.T_OPENBRAC);
            procedureExpression();
            read(next(), PredefinedTokenType.T_CLOSEBRAC);
            ASTTokenType astTokenType = Util.getASTTypeForPredefType((PredefinedTokenType) type);
            buildTree(astTokenType, 1);
        } else {
            procedureName();
            if(next().getType().equals(PredefinedTokenType.T_OPENBRAC)) {
                read(next());
                procedureExpression();
                int n = 2;
                while (next().getType().equals(PredefinedTokenType.T_COMMA)) {
                    read(next());
                    procedureExpression();
                    n++;
                }
                read(next(), PredefinedTokenType.T_CLOSEBRAC);
                buildTree(ASTTokenType.CALL, n);
            }
        }
    }

    // Name -> '<identifier>'
    private static void procedureName() throws OutOfOrderTokenException, InvalidTokenTypeException {
        read(next(), ValueTokenType.IDENTIFIER);
    }

    private static void printAST(ASTNode head, int depth) {
        String indent = Util.buildRepeatedCharString('.', depth);
        ArrayList<ASTNode> children = head.getChildren();
        System.out.println(indent  + head.getToken().getValue() + "(" + children.size() + ")");
        for(ASTNode child: children) {
            printAST(child, depth+1);
        }
    }

    public static void printAST() {
        printAST(stack.peek(), 0);
    }

    public static void parse(ArrayList<ScannerToken> tokens) throws OutOfOrderTokenException,
            InvalidTokenTypeException {
        Parser.tokens = tokens;
        procedureWinzig();
        printAST();
    }
}
