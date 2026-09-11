package com.example.translator.lexer;

/**
 * Enumeração dos tipos de token reconhecidos pelo analisador léxico (Scanner).
 */
public enum TokenType {
    // Parênteses e Delimitadores
    LEFT_PAREN, RIGHT_PAREN, SEMICOLON,
    // Operadores aritméticos e Atribuição
    PLUS, MINUS, STAR, SLASH, EQ,
    // Literais numéricos e Identificadores
    NUMBER,
    IDENT,
    IDENTIFIER,
    // Palavras reservadas
    LET,
    // Fim de arquivo (EOF)
    EOF
}