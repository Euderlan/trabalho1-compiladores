package com.example.translator.lexer;

// Representa um token gerado pelo analisador léxico.
//
// Cada token contém:
// - type: o tipo do token (TokenType);
// - lexeme: a sequência original de caracteres que forma o token;
// - literal: o valor literal associado (por exemplo, um número inteiro) ou null;
// - line: a linha em que o token foi encontrado.
public class Token {
    // Tipo do token (ex.: NUMBER, PLUS, LEFT_PAREN, etc.).
    public final TokenType type;
    // Texto original que compõe o token.
    public final String lexeme;
    // Valor literal associado (ex.: Integer para números) ou null.
    public final Object literal;
    // Linha do código em que o token foi encontrado.
    public final int line;

    // Cria um novo token.
    public Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;  // tipo do token
        this.lexeme = lexeme; // texto original do token
        this.literal = literal; // valor literal associado ao token (pode ser null)
        this.line = line;     //linha em que o token foi encontrado
    }

    @Override
    public String toString() {
        return type + " " + lexeme + (literal != null ? " " + literal : "");
    }
}
