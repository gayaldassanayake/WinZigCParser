package compiler.token;

import compiler.evaluator.AttributeCollection;

import java.util.ArrayList;

public class ASTNode {
    private final Token token;
    private ASTNode parent;
    private final ArrayList<ASTNode> children;
    private AttributeCollection inheritedCollection;
    private AttributeCollection synthesizedCollection;

    public ASTNode(Token token, ASTNode parent){
        this.token = token;
        this.parent = parent;
        children = new ArrayList<>();
    }

    public void setParent(ASTNode parent) {
        this.parent = parent;
    }

    public Token getToken() {
        return token;
    }

    public ASTNode getParent() {
        return parent;
    }

    public void addChild(ASTNode child) {
        children.add(child);
    }

    public ArrayList<ASTNode> getChildren() {
        return children;
    }

    public AttributeCollection getInheritedCollection() {
        return inheritedCollection;
    }

    public void setInheritedCollection(AttributeCollection inheritedCollection) {
        this.inheritedCollection = inheritedCollection;
    }

    public AttributeCollection getSynthesizedCollection() {
        return synthesizedCollection;
    }

    public void setSynthesizedCollection(AttributeCollection synthesizedCollection) {
        this.synthesizedCollection = synthesizedCollection;
    }
}
