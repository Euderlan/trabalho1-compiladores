package com.example.translator.lexer;

import java.util.Objects;

// Classe que representa um token lexical, contendo tipo, texto, literal e linha.
public final class Token {
    public final TokenType type;
    public final String lexeme;
    public final Object literal;
    public final int line;

    // Construtor que inicializa os campos do token.
    public Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    @Override
    public String toString() {
        // Retorna representação legível do token.
        return type + " " + lexeme + (literal != null ? " " + literal : "");
    }

    @Override
    public boolean equals(Object o) {
        // Implementação de equals.
        if (this == o) return true;
        if (!(o instanceof Token)) return false;
        Token token = (Token) o;
        return line == token.line &&
                type == token.type &&
                Objects.equals(lexeme, token.lexeme) &&
                Objects.equals(literal, token.literal);
    }

    @Override
    public int hashCode() {
        // Implementação de hashCode.
        return Objects.hash(type, lexeme, literal, line);
    }
}
