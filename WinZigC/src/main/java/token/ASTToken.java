package token;

import exception.InvalidTokenTypeException;
import token.tokentype.ASTTokenType;

import java.util.HashMap;

public class ASTToken implements Token {
    private final String value;
    private final ASTTokenType type;
    private static final HashMap<ASTTokenType, String> typeValueMap = new HashMap<>();

    static {
        typeValueMap.put(ASTTokenType.PROGRAM, "program");
        typeValueMap.put(ASTTokenType.CONSTS, "consts");
        typeValueMap.put(ASTTokenType.CONST, "const");
        typeValueMap.put(ASTTokenType.TYPES, "types");
        typeValueMap.put(ASTTokenType.TYPE, "type");
        typeValueMap.put(ASTTokenType.LIT, "lit");
        typeValueMap.put(ASTTokenType.SUBPROGS, "subprogs");
        typeValueMap.put(ASTTokenType.FCN, "fcn");
        typeValueMap.put(ASTTokenType.PARAMS, "params");
        typeValueMap.put(ASTTokenType.DCLNS, "dclns");
        typeValueMap.put(ASTTokenType.VAR, "var");
        typeValueMap.put(ASTTokenType.BLOCK, "block");
        typeValueMap.put(ASTTokenType.OUTPUT, "output");
        typeValueMap.put(ASTTokenType.IF, "if");
        typeValueMap.put(ASTTokenType.WHILE, "while");
        typeValueMap.put(ASTTokenType.REPEAT, "repeat");
        typeValueMap.put(ASTTokenType.FOR, "for");
        typeValueMap.put(ASTTokenType.LOOP, "loop");
        typeValueMap.put(ASTTokenType.CASE, "case");
        typeValueMap.put(ASTTokenType.READ, "read");
        typeValueMap.put(ASTTokenType.EXIT, "exit");
        typeValueMap.put(ASTTokenType.RETURN, "return");
        typeValueMap.put(ASTTokenType.NULL, "<null>");
        typeValueMap.put(ASTTokenType.INTEGER, "integer");
        typeValueMap.put(ASTTokenType.STRING, "string");
        typeValueMap.put(ASTTokenType.CASE_CLAUSE, "case_clause");
        typeValueMap.put(ASTTokenType.DOUBLE_DOT, "..");
        typeValueMap.put(ASTTokenType.OTHERWISE, "otherwise");
        typeValueMap.put(ASTTokenType.ASSIGN, "assign");
        typeValueMap.put(ASTTokenType.SWAP, "swap");
        typeValueMap.put(ASTTokenType.TRUE, "true");
        typeValueMap.put(ASTTokenType.LTE, "<=");
        typeValueMap.put(ASTTokenType.LT, "<");
        typeValueMap.put(ASTTokenType.GTE, ">=");
        typeValueMap.put(ASTTokenType.GT, ">");
        typeValueMap.put(ASTTokenType.EQUAL, "=");
        typeValueMap.put(ASTTokenType.NOTEQUAL, "<>");
        typeValueMap.put(ASTTokenType.PLUS, "+");
        typeValueMap.put(ASTTokenType.MINUS, "-");
        typeValueMap.put(ASTTokenType.OR, "or");
        typeValueMap.put(ASTTokenType.MULTIPLY, "*");
        typeValueMap.put(ASTTokenType.DIVIDE, "/");
        typeValueMap.put(ASTTokenType.AND, "and");
        typeValueMap.put(ASTTokenType.MOD, "mod");
        typeValueMap.put(ASTTokenType.NOT, "not");
        typeValueMap.put(ASTTokenType.EOF, "eof");
        typeValueMap.put(ASTTokenType.CALL, "call");
        typeValueMap.put(ASTTokenType.SUCC, "succ");
        typeValueMap.put(ASTTokenType.PRED, "pred");
        typeValueMap.put(ASTTokenType.CHR, "chr");
        typeValueMap.put(ASTTokenType.ORD, "ord");
    }

    private String setValue(ASTTokenType type) throws InvalidTokenTypeException {
        if(!typeValueMap.containsKey(type)) {
            throw new InvalidTokenTypeException(type);
        }
        return typeValueMap.get(type);
    }

    public ASTToken(ASTTokenType type) throws InvalidTokenTypeException {
        this.type = type;
        this.value = setValue(type);
    }

    public ASTTokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
