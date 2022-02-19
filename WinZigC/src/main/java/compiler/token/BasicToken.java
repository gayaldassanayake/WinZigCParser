package compiler.token;

import compiler.exception.InvalidTokenTypeException;
import compiler.token.tokentype.ValueTokenType;

import java.util.HashMap;

public class BasicToken implements Token {
    private final String value;
    private final ValueTokenType type;
    private static final HashMap<ValueTokenType, String> typeValueMap = new HashMap<>();

    static {
        typeValueMap.put(ValueTokenType.IDENTIFIER, "<identifier>");
        typeValueMap.put(ValueTokenType.INTEGER, "<integer>");
        typeValueMap.put(ValueTokenType.CHAR, "<char>");
        typeValueMap.put(ValueTokenType.STRING, "<string>");
    }

    private String setValue(ValueTokenType type) throws InvalidTokenTypeException {
        if(!typeValueMap.containsKey(type)) {
            throw new InvalidTokenTypeException(type);
        }
        return typeValueMap.get(type);
    }

    public BasicToken(ValueTokenType type) throws InvalidTokenTypeException {
        this.type = type;
        this.value = setValue(type);
    }

    public String getValue() {
        return value;
    }

    public ValueTokenType getType() {
        return type;
    }
}
