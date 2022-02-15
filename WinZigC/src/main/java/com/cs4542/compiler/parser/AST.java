package com.cs4542.compiler.parser;

public class AST {
    private final ASTNode head;

    public AST(ASTNode head) {
        this.head = head;
    }

    public ASTNode getHead() {
        return head;
    }
}

