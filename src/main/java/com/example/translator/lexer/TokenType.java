package com.example.translator.lexer;

public enum TokenType {
    // Single-character tokens.
    LEFT_PAREN, RIGHT_PAREN,
    PLUS, MINUS, STAR, SLASH,
    EQUAL, SEMICOLON,

    // Literals.
    IDENTIFIER, NUMBER,

    // Keywords.
    LET, PRINT,

    // End-of-file.
    EOF,

    // Synthetic tokens used by the interpreter.
    STORE, PRINT_CMD
}
