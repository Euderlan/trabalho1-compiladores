package com.example.translator.lexer;

// Enum que representa os diferentes tipos de tokens reconhecidos pelo analisador léxico.
public enum TokenType {
    // Tokens de um único caractere.
    LEFT_PAREN, RIGHT_PAREN,
    PLUS, MINUS, STAR, SLASH,
    EQUAL, SEMICOLON,

    // Literais.
    IDENTIFIER, NUMBER,

    // Palavras‑chave.
    LET, PRINT,

    // Fim de arquivo.
    EOF,

    // Tokens sintéticos usados pelo interpretador.
    STORE, PRINT_CMD
}
