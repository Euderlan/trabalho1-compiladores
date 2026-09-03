package com.example.translator.parser;

import com.example.translator.lexer.Token;
import com.example.translator.lexer.TokenType;

import java.util.ArrayList;
import java.util.List;

// Classe responsável por analisar a lista de tokens e gerar representação pós‑fixa (RPN).
public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    // Construtor que recebe a lista de tokens.
    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    /**
     * Analisa todo o programa e devolve uma lista de instruções, cada uma representada como lista de tokens em ordem pós‑fixa.
     */
    public List<List<Token>> parse() {
        List<List<Token>> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(declaration());
        }
        return statements;
    }

    // Analisa uma declaração (let ou print).
    private List<Token> declaration() {
        if (match(TokenType.LET)) {
            return letStatement();
        }
        if (match(TokenType.PRINT)) {
            return printStatement();
        }
        throw error(peek(), "Expected 'let' or 'print' statement.");
    }

    // Analisa declaração let.
    private List<Token> letStatement() {
        Token name = consume(TokenType.IDENTIFIER, "Expect variable name.");
        consume(TokenType.EQUAL, "Expect '=' after variable name.");
        List<Token> exprPostfix = expression();
        consume(TokenType.SEMICOLON, "Expect ';' after expression.");
        // Token sintético STORE carrega o nome da variável.
        exprPostfix.add(new Token(TokenType.STORE, name.lexeme, null, name.line));
        return exprPostfix;
    }

    // Analisa declaração print.
    private List<Token> printStatement() {
        List<Token> exprPostfix = expression();
        consume(TokenType.SEMICOLON, "Expect ';' after expression.");
        // Token sintético PRINT_CMD sinaliza impressão.
        exprPostfix.add(new Token(TokenType.PRINT_CMD, "print", null, previous().line));
        return exprPostfix;
    }

    // Expressão (precedência + e -).
    private List<Token> expression() {
        return term(); // Trata + e -
    }

    // Term (precedência * e /).
    private List<Token> term() {
        List<Token> tokens = factor();
        while (match(TokenType.PLUS, TokenType.MINUS)) {
            Token operator = previous();
            List<Token> right = factor();
            tokens.addAll(right);
            tokens.add(operator); // RPN: esquerda direita operador
        }
        return tokens;
    }

    // Fator (operadores de multiplicação/divisão).
    private List<Token> factor() {
        List<Token> tokens = primary();
        while (match(TokenType.STAR, TokenType.SLASH)) {
            Token operator = previous();
            List<Token> right = primary();
            tokens.addAll(right);
            tokens.add(operator);
        }
        return tokens;
    }

    // Primário (número, identificador ou expressão entre parênteses).
    private List<Token> primary() {
        if (match(TokenType.NUMBER, TokenType.IDENTIFIER)) {
            // O token já foi adicionado à lista pós‑fixa.
            Token t = previous();
            List<Token> list = new ArrayList<>();
            list.add(t);
            return list;
        }
        if (match(TokenType.LEFT_PAREN)) {
            List<Token> inner = expression();
            consume(TokenType.RIGHT_PAREN, "Expect ')' after expression.");
            return inner;
        }
        throw error(peek(), "Expect expression.");
    }

    // ---------- Métodos auxiliares ----------

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        throw error(peek(), message);
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private RuntimeException error(Token token, String message) {
        return new RuntimeException("[line " + token.line + "] Error at '" + token.lexeme + "': " + message);
    }
}
