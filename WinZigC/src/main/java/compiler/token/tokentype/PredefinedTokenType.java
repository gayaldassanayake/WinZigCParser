package compiler.token.tokentype;

import java.util.HashMap;

public enum PredefinedTokenType implements ScannerTokenType {
    T_PROGRAM,
    T_VAR,
    T_CONST,
    T_TYPE,
    T_FUNCTION,
    T_RETURN,
    T_BEGIN,
    T_END,
    T_SWAP,
    T_ASSIGN,
    T_OUTPUT,
    T_IF,
    T_THEN,
    T_ELSE,
    T_WHILE,
    T_DO,
    T_CASE,
    T_OF,
    T_DOTS,
    T_OTHERWISE,
    T_REPEAT,
    T_FOR,
    T_UNTIL,
    T_LOOP,
    T_POOL,
    T_EXIT,
    T_LTE,
    T_NE,
    T_LT,
    T_GTE,
    T_GT,
    T_EQUAL,
    T_MOD,
    T_AND,
    T_OR,
    T_NOT,
    T_READ,
    T_SUCC,
    T_PRED,
    T_CHR,
    T_ORD,
    T_EOF,
    T_BEGBLOCK,
    T_COLON,
    T_SEMICOLON,
    T_SINGLEDOT,
    T_COMMA,
    T_OPENBRAC,
    T_CLOSEBRAC,
    T_PLUS,
    T_MINUS,
    T_MULTIPLY,
    T_DIVIDE;

    private static final HashMap<String, PredefinedTokenType> predefinedTokenValues = new HashMap<>();

    static {
        predefinedTokenValues.put("program", T_PROGRAM);
        predefinedTokenValues.put("var", T_VAR);
        predefinedTokenValues.put("const", T_CONST);
        predefinedTokenValues.put("type", T_TYPE);
        predefinedTokenValues.put("function", T_FUNCTION);
        predefinedTokenValues.put("return", T_RETURN);
        predefinedTokenValues.put("begin", T_BEGIN);
        predefinedTokenValues.put("end", T_END);
        predefinedTokenValues.put(":=:", T_SWAP);
        predefinedTokenValues.put(":=", T_ASSIGN);
        predefinedTokenValues.put("output", T_OUTPUT);
        predefinedTokenValues.put("if", T_IF);
        predefinedTokenValues.put("then", T_THEN);
        predefinedTokenValues.put("else", T_ELSE);
        predefinedTokenValues.put("while", T_WHILE);
        predefinedTokenValues.put("do", T_DO);
        predefinedTokenValues.put("case", T_CASE);
        predefinedTokenValues.put("of", T_OF);
        predefinedTokenValues.put("..", T_DOTS);
        predefinedTokenValues.put("otherwise", T_OTHERWISE);
        predefinedTokenValues.put("repeat", T_REPEAT);
        predefinedTokenValues.put("for", T_FOR);
        predefinedTokenValues.put("until", T_UNTIL);
        predefinedTokenValues.put("loop", T_LOOP);
        predefinedTokenValues.put("pool", T_POOL);
        predefinedTokenValues.put("exit", T_EXIT);
        predefinedTokenValues.put("<=", T_LTE);
        predefinedTokenValues.put("<>", T_NE);
        predefinedTokenValues.put("<", T_LT);
        predefinedTokenValues.put(">=", T_GTE);
        predefinedTokenValues.put(">", T_GT);
        predefinedTokenValues.put("=", T_EQUAL);
        predefinedTokenValues.put("mod", T_MOD);
        predefinedTokenValues.put("and", T_AND);
        predefinedTokenValues.put("or", T_OR);
        predefinedTokenValues.put("not", T_NOT);
        predefinedTokenValues.put("read", T_READ);
        predefinedTokenValues.put("succ", T_SUCC);
        predefinedTokenValues.put("pred", T_PRED);
        predefinedTokenValues.put("chr", T_CHR);
        predefinedTokenValues.put("ord", T_ORD);
        predefinedTokenValues.put("eof", T_EOF);
        predefinedTokenValues.put("{", T_BEGBLOCK);
        predefinedTokenValues.put(":", T_COLON);
        predefinedTokenValues.put(";", T_SEMICOLON);
        predefinedTokenValues.put(".", T_SINGLEDOT);
        predefinedTokenValues.put(",", T_COMMA);
        predefinedTokenValues.put("(", T_OPENBRAC);
        predefinedTokenValues.put(")", T_CLOSEBRAC);
        predefinedTokenValues.put("+", T_PLUS);
        predefinedTokenValues.put("-", T_MINUS);
        predefinedTokenValues.put("*", T_MULTIPLY);
        predefinedTokenValues.put("/", T_DIVIDE);
    }

    public static HashMap<String, PredefinedTokenType> getPredefinedTokenValues() {
        return predefinedTokenValues;
    }
}
