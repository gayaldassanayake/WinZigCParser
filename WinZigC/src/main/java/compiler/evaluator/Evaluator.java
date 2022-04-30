package compiler.evaluator;

import compiler.token.ASTNode;
import compiler.token.tokentype.ASTTokenType;
import compiler.token.tokentype.TokenType;
import compiler.token.tokentype.ValueTokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Evaluator {
    private static final ArrayList<String> code = new ArrayList<>();
    private static final ArrayList<String> error = new ArrayList<>();
    private static final Stack<Integer> memory = new Stack<>();
    private static final HashMap<String, Integer> variableMap = new HashMap<>();

    private static Integer lookup(String var) {
        return variableMap.getOrDefault(var, -1);
    }

    private static void evaluateInteger(ASTNode node) {
        try {
            int n = Integer.parseInt(node.getChildren().get(0).getToken().getValue().strip());
            memory.push(n);

            code.add("LIT "+n);
        } catch(NumberFormatException e) {
        }
        AttributeCollection inheritedCollection = node.getInheritedCollection();
        int next = inheritedCollection.getNext() + 1;
        int top = inheritedCollection.getTop() + 1;
        AttributeCollection synthesizedCollection = new AttributeCollection(next, top, DataType.INTEGER);
        node.setSynthesizedCollection(synthesizedCollection);
    }

    private static void evaluateIdentifier(ASTNode node, Environment env) {
        String id = node.getChildren().get(0).getToken().getValue();
        int varAddr = lookup(id);
        memory.push(memory.get(varAddr));

        String opcode = Environment.LOCAL.equals(env)? "LLV":"LGV";
        code.add(opcode+" "+varAddr);

        AttributeCollection inheritedCollection = node.getInheritedCollection();
        int next = inheritedCollection.getNext() + 1;
        int top = inheritedCollection.getTop() + 1;
        AttributeCollection synthesizedCollection = new AttributeCollection(next, top, null);
        node.setSynthesizedCollection(synthesizedCollection);
    }

    private static void evaluateBLE(ASTNode node, Environment env) {
        ASTNode firstNode = node.getChildren().get(0);
        evaluateFirstChildDefault(firstNode, env);

        ASTNode secondNode = node.getChildren().get(1);
        evaluateOtherChildDefault(secondNode, firstNode, env);

        code.add("BOP BLE");
        Integer right = memory.pop();
        Integer left = memory.pop();
        memory.push(left <= right ? 1:0);

        int next = secondNode.getSynthesizedCollection().getNext() +1;
        int top = secondNode.getSynthesizedCollection().getTop() -1;
        DataType type = DataType.BOOLEAN;
        AttributeCollection synthesizedCollection = new AttributeCollection(next, top, type);
        node.setSynthesizedCollection(synthesizedCollection);
    }

    private static void evaluateBLT(ASTNode node, Environment env) {
        ASTNode firstNode = node.getChildren().get(0);
        evaluateFirstChildDefault(firstNode, env);

        ASTNode secondNode = node.getChildren().get(1);
        evaluateOtherChildDefault(secondNode, firstNode, env);

        code.add("BOP BLT");
        Integer right = memory.pop();
        Integer left = memory.pop();
        memory.push(left < right ? 1:0);

        int next = secondNode.getSynthesizedCollection().getNext() +1;
        int top = secondNode.getSynthesizedCollection().getTop() -1;
        DataType type = DataType.BOOLEAN;
        AttributeCollection synthesizedCollection = new AttributeCollection(next, top, type);
        node.setSynthesizedCollection(synthesizedCollection);
    }

    private static void evaluateBGE(ASTNode node, Environment env) {
        ASTNode firstNode = node.getChildren().get(0);
        evaluateFirstChildDefault(firstNode, env);

        ASTNode secondNode = node.getChildren().get(1);
        evaluateOtherChildDefault(secondNode, firstNode, env);

        code.add("BOP BGE");
        Integer right = memory.pop();
        Integer left = memory.pop();
        memory.push(left >= right ? 1:0);

        int next = secondNode.getSynthesizedCollection().getNext() +1;
        int top = secondNode.getSynthesizedCollection().getTop() -1;
        DataType type = DataType.BOOLEAN;
        AttributeCollection synthesizedCollection = new AttributeCollection(next, top, type);
        node.setSynthesizedCollection(synthesizedCollection);
    }

    private static void evaluateBGT(ASTNode node, Environment env) {
        ASTNode firstNode = node.getChildren().get(0);
        evaluateFirstChildDefault(firstNode, env);

        ASTNode secondNode = node.getChildren().get(1);
        evaluateOtherChildDefault(secondNode, firstNode, env);

        code.add("BOP BGT");
        Integer right = memory.pop();
        Integer left = memory.pop();
        memory.push(left > right ? 1:0);

        int next = secondNode.getSynthesizedCollection().getNext() +1;
        int top = secondNode.getSynthesizedCollection().getTop() -1;
        DataType type = DataType.BOOLEAN;
        AttributeCollection synthesizedCollection = new AttributeCollection(next, top, type);
        node.setSynthesizedCollection(synthesizedCollection);
    }

    private static void evaluateBPlus(ASTNode node, Environment env) {
        ASTNode firstChild = node.getChildren().get(0);
        evaluateFirstChildDefault(firstChild, env);

        ASTNode secondChild = node.getChildren().get(1);
        evaluateOtherChildDefault(secondChild, firstChild, env);

        Integer right = memory.pop();
        Integer left = memory.pop();
        memory.push(left + right);

        code.add("BOP BPLUS");
        int next = secondChild.getSynthesizedCollection().getNext() + 1;
        int top = secondChild.getSynthesizedCollection().getTop() - 1;
        DataType type = DataType.INTEGER;
        node.setSynthesizedCollection(new AttributeCollection(next, top, type));
    }

    private static void evaluateBMinus(ASTNode node, Environment env) {
        ASTNode firstChild = node.getChildren().get(0);
        evaluateFirstChildDefault(firstChild, env);

        ASTNode secondChild = node.getChildren().get(1);
        evaluateOtherChildDefault(secondChild, firstChild, env);

        Integer right = memory.pop();
        Integer left = memory.pop();
        memory.push(left - right);

        code.add("BOP BMINUS");
        int next = secondChild.getSynthesizedCollection().getNext() + 1;
        int top = secondChild.getSynthesizedCollection().getTop() - 1;
        DataType type = DataType.INTEGER;
        node.setSynthesizedCollection(new AttributeCollection(next, top, type));
    }

    private static void evaluateBMULT(ASTNode node, Environment env) {
        ASTNode firstChild = node.getChildren().get(0);
        evaluateFirstChildDefault(firstChild, env);

        ASTNode secondChild = node.getChildren().get(1);
        evaluateOtherChildDefault(secondChild, firstChild, env);

        Integer right = memory.pop();
        Integer left = memory.pop();
        memory.push(left * right);

        code.add("BOP BMULT");
        int next = secondChild.getSynthesizedCollection().getNext() + 1;
        int top = secondChild.getSynthesizedCollection().getTop() - 1;
        DataType type = DataType.INTEGER;
        node.setSynthesizedCollection(new AttributeCollection(next, top, type));
    }

    private static void evaluateBDIV(ASTNode node, Environment env) {
        ASTNode firstChild = node.getChildren().get(0);
        evaluateFirstChildDefault(firstChild, env);

        ASTNode secondChild = node.getChildren().get(1);
        evaluateOtherChildDefault(secondChild, firstChild, env);

        Integer right = memory.pop();
        Integer left = memory.pop();
        memory.push(left / right);

        code.add("BOP BDIV");
        int next = secondChild.getSynthesizedCollection().getNext() + 1;
        int top = secondChild.getSynthesizedCollection().getTop() - 1;
        DataType type = DataType.INTEGER;
        node.setSynthesizedCollection(new AttributeCollection(next, top, type));
    }

    private static void evaluateBAND(ASTNode node, Environment env) {
        ASTNode firstChild = node.getChildren().get(0);
        evaluateFirstChildDefault(firstChild, env);

        ASTNode secondChild = node.getChildren().get(1);
        evaluateOtherChildDefault(secondChild, firstChild, env);

        Integer right = memory.pop();
        Integer left = memory.pop();
        memory.push(left * right);

        code.add("BOP BAND");
        int next = secondChild.getSynthesizedCollection().getNext() + 1;
        int top = secondChild.getSynthesizedCollection().getTop() - 1;
        DataType type = DataType.BOOLEAN;
        node.setSynthesizedCollection(new AttributeCollection(next, top, type));
    }

    private static void evaluateBOR(ASTNode node, Environment env) {
        ASTNode firstChild = node.getChildren().get(0);
        evaluateFirstChildDefault(firstChild, env);

        ASTNode secondChild = node.getChildren().get(1);
        evaluateOtherChildDefault(secondChild, firstChild, env);

        Integer right = memory.pop();
        Integer left = memory.pop();
        memory.push(left | right);

        code.add("BOP BOR");
        int next = secondChild.getSynthesizedCollection().getNext() + 1;
        int top = secondChild.getSynthesizedCollection().getTop() - 1;
        DataType type = DataType.BOOLEAN;
        node.setSynthesizedCollection(new AttributeCollection(next, top, type));
    }

    private static void evaluateUNOT(ASTNode node, Environment env) {
        ASTNode firstChild = node.getChildren().get(0);
        evaluateFirstChildDefault(firstChild, env);

        Integer value = memory.pop();
        memory.push(Math.abs(value-1));

        code.add("UOP UNOT");
        int next = firstChild.getSynthesizedCollection().getNext() + 1;
        int top = firstChild.getSynthesizedCollection().getTop();
        DataType type = DataType.BOOLEAN;
        node.setSynthesizedCollection(new AttributeCollection(next, top, type));
    }

    private static void evaluateUNEG(ASTNode node, Environment env) {
        ASTNode firstChild = node.getChildren().get(0);
        evaluateFirstChildDefault(firstChild, env);

        Integer value = memory.pop();
        memory.push(-value);

        code.add("UOP UNEG");
        int next = firstChild.getSynthesizedCollection().getNext() + 1;
        int top = firstChild.getSynthesizedCollection().getTop();
        DataType type = DataType.INTEGER;
        node.setSynthesizedCollection(new AttributeCollection(next, top, type));
    }

    private static void evaluateMinus(ASTNode node, Environment env) {
        if(node.getChildren().size() == 2) {
            evaluateBMinus(node, env);
        } else {
            evaluateUNEG(node, env);
        }
    }

    private static void evaluateAssign(ASTNode node, Environment env) {
        ASTNode secondNode = node.getChildren().get(1);
        evaluateFirstChildDefault(secondNode, env);

        ASTNode identifierNode = node.getChildren().get(0);
        String id = identifierNode.getChildren().get(0).getToken().getValue();

        int varAddr = lookup(id);
        int value = memory.pop();
        memory.add(varAddr, value);

        String opcode = Environment.LOCAL.equals(env)? "SLV":"SGV";
        code.add(opcode+" "+varAddr);
        evaluateChildBaringNodeDefault(node);
        node.getSynthesizedCollection().setNext(secondNode.getSynthesizedCollection().getNext() + 1);
    }

    private static void evaluateBlock(ASTNode node, Environment env) {
        ASTNode previousNode = null;
        if(node.getChildren().size()>0) {
            for (ASTNode child : node.getChildren()) {
                if (child.equals(node.getChildren().get(0))) {
                    evaluateFirstChildDefault(child, env);
                } else {
                    evaluateOtherChildDefault(child, previousNode, env);
                }
                previousNode = child;
            }
            evaluateChildBaringNodeDefault(node);
        } else {
            evaluateChildlessNodeDefault(node);
        }
    }

    private static void evaluateWhile(ASTNode node, Environment env) {
        ASTNode firstNode = node.getChildren().get(0);
        evaluateFirstChildDefault(firstNode, env);

        memory.pop();

        ASTNode secondNode = node.getChildren().get(1);
        int codeIndex = code.size();
        code.add("");

        int next = firstNode.getSynthesizedCollection().getNext()+1;
        int top = firstNode.getSynthesizedCollection().getTop()-1;
        DataType type = firstNode.getSynthesizedCollection().getType();

        secondNode.setInheritedCollection(
                new AttributeCollection(next, top, type));
        evaluate(secondNode, env);

        code.set(codeIndex,
                "COND " + node.getInheritedCollection().getNext() + " " +
                        (secondNode.getSynthesizedCollection().getNext()+1));

        code.add("GOTO "+ node.getInheritedCollection().getNext());
        next = secondNode.getSynthesizedCollection().getNext()+1;
        top = secondNode.getSynthesizedCollection().getTop();
        type = secondNode.getSynthesizedCollection().getType();
        node.setSynthesizedCollection(new AttributeCollection(next, top, type));
    }

    private static void evaluateVar(ASTNode node, Environment env) {
        int childrenCount = node.getChildren().size();
        int top = node.getInheritedCollection().getTop();
        int next = node.getInheritedCollection().getNext();
        DataType type = node.getInheritedCollection().getType();
        for(ASTNode child: node.getChildren()) {
            if(node.getChildren().get(childrenCount-1).equals(child)) {
                continue;
            }
            memory.add(0);
            top++;
            variableMap.put(child.getChildren().get(0).getToken().getValue(), top);
            code.add("LIT "+ top);
            next++;
        }
        node.setSynthesizedCollection(new AttributeCollection(next, top, type));
    }

    private static void evaluateConsts(ASTNode node, Environment env) {
        evaluateChildlessNodeDefault(node);
    }

    private static void evaluateTypes(ASTNode node, Environment env) {
        evaluateChildlessNodeDefault(node);
    }

    private static void evaluateDclns(ASTNode node, Environment env) {
        ASTNode firstNode = node.getChildren().get(0);
        evaluateFirstChildDefault(firstNode, env);
        ASTNode leftNode = firstNode;
        for(int i=1; i<node.getChildren().size(); i++) {
            ASTNode child = node.getChildren().get(i);
            evaluateOtherChildDefault(child, leftNode, env);
            leftNode = child;
        }
        evaluateChildBaringNodeDefault(node);
    }

    private static void evaluateSubprogs(ASTNode node, Environment env) {
        evaluateChildlessNodeDefault(node);
    }

    private static void evaluateProgram(ASTNode node, Environment env) {
        node.setInheritedCollection(new AttributeCollection(1, -1, null));

        ASTNode firstNode = node.getChildren().get(1);
        evaluateFirstChildDefault(firstNode, env);
        ASTNode leftNode = firstNode;
        for(int i=2; i<node.getChildren().size()-1; i++) {
            ASTNode child = node.getChildren().get(i);
            evaluateOtherChildDefault(child, leftNode, env);
            leftNode = child;
        }
        code.add("HALT");
        ASTNode lastChild = node.getChildren().get(node.getChildren().size()-2);
        int next = lastChild.getSynthesizedCollection().getNext()+1;
        int top = lastChild.getSynthesizedCollection().getTop();
        DataType type = lastChild.getSynthesizedCollection().getType();
        node.setSynthesizedCollection(new AttributeCollection(next, top, type));
    }

    private static void evaluateChildlessNodeDefault(ASTNode node) {
        node.setSynthesizedCollection(node.getInheritedCollection());
    }

    private static void evaluateChildBaringNodeDefault(ASTNode node) {
        node.setSynthesizedCollection(node.getChildren().get(node.getChildren().size()-1).getSynthesizedCollection());
    }

    private static void evaluateFirstChildDefault(ASTNode firstChild, Environment env) {
        firstChild.setInheritedCollection(firstChild.getParent().getInheritedCollection());
        evaluate(firstChild, env);
    }

    private static void evaluateOtherChildDefault(ASTNode child, ASTNode left, Environment env) {
        child.setInheritedCollection(left.getSynthesizedCollection());
        evaluate(child, env);
    }

    private static void printCode() {
        for(String line: code) {
            System.out.println(line);
        }
    }

    private static void evaluate(ASTNode node, Environment env) {
        TokenType tokenType = node.getToken().getType();

        if(tokenType.equals(ValueTokenType.INTEGER)) {
            evaluateInteger(node);
        } else if (tokenType.equals(ValueTokenType.IDENTIFIER)) {
            evaluateIdentifier(node, env);
        } else if(tokenType.equals(ASTTokenType.PLUS)) {
            evaluateBPlus(node, env);
        } else if(tokenType.equals(ASTTokenType.MINUS)) {
            evaluateMinus(node, env);
        } else if(tokenType.equals(ASTTokenType.AND)) {
            evaluateBAND(node, env);
        } else if(tokenType.equals(ASTTokenType.OR)) {
            evaluateBOR(node, env);
        } else if(tokenType.equals(ASTTokenType.NOT)) {
            evaluateUNOT(node, env);
        } else if(tokenType.equals(ASTTokenType.LTE)) {
            evaluateBLE(node, env);
        } else if(tokenType.equals(ASTTokenType.LT)) {
            evaluateBLT(node, env);
        } else if(tokenType.equals(ASTTokenType.GTE)) {
            evaluateBGE(node, env);
        } else if(tokenType.equals(ASTTokenType.GT)) {
            evaluateBGT(node, env);
        } else if(tokenType.equals(ASTTokenType.MULTIPLY)) {
            evaluateBMULT(node, env);
        } else if(tokenType.equals(ASTTokenType.DIVIDE)) {
            evaluateBDIV(node, env);
        } else if(tokenType.equals(ASTTokenType.ASSIGN)) {
            evaluateAssign(node, env);
        } else if(tokenType.equals(ASTTokenType.BLOCK)) {
            evaluateBlock(node, env);
        } else if(tokenType.equals(ASTTokenType.WHILE)) {
            evaluateWhile(node, env);
        } else if(tokenType.equals(ASTTokenType.VAR)) {
            evaluateVar(node, env);
        } else if(tokenType.equals(ASTTokenType.CONSTS)) {
            evaluateConsts(node, env);
        } else if(tokenType.equals(ASTTokenType.TYPES)) {
            evaluateTypes(node, env);
        } else if(tokenType.equals(ASTTokenType.DCLNS)) {
            evaluateDclns(node, env);
        } else if(tokenType.equals(ASTTokenType.SUBPROGS)) {
            evaluateSubprogs(node, env);
        } else if(tokenType.equals(ASTTokenType.PROGRAM)) {
            evaluateProgram(node, env);
        }
    }

    public static void evaluate(ASTNode head) {
        evaluate(head, Environment.GLOBAL);
        printCode();
    }
}
