package com.example.translator.lexer;

// Representa um token gerado pelo analisador léxico.
public class Token {
    public final TokenType type;
    public final String lexeme;
    public final Object literal;
    public final int line;

    // Construtor principal
    public Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    // Construtor utilitário (adicionado)
    public Token(TokenType type, String lexeme) {
        this(type, lexeme, null, 1);
    }

    @Override
    public String toString() {
        return "<" + type + ">" + lexeme + "</" + type + ">";
    }
}