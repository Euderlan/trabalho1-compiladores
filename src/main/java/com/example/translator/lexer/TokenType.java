package com.example.translator.lexer;

/**
 * Enumeração dos tipos de token reconhecidos pelo analisador léxico (Scanner).
 * Cada constante representa um elemento sintático da linguagem de expressões aritméticas.
 */
public enum TokenType {
    // Parênteses
    LEFT_PAREN, RIGHT_PAREN,
    // Operadores aritméticos
    PLUS, MINUS, STAR, SLASH,
    // Literais numéricos
    NUMBER,
    // Fim de arquivo (EOF)
    EOF
}
