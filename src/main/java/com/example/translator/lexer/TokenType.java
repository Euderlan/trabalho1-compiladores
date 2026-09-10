package com.example.translator.lexer;

/**
 * Enumeração dos tipos de token reconhecidos pelo analisador léxico (Scanner).
 */
public enum TokenType {
    // Parênteses
    LEFT_PAREN, RIGHT_PAREN,
    // Operadores aritméticos
    PLUS, MINUS, STAR, SLASH,
    // Literais numéricos e Identificadores
    NUMBER,
    IDENT, //Identificador (ex: preco, x, var1)
    IDENTIFIER,
    // Fim de arquivo (EOF)
    EOF
}