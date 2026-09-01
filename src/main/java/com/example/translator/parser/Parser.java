package com.example.translator.parser;

import com.example.translator.lexer.Token;
import com.example.translator.lexer.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    /** Parses the whole program and returns a list of statements, each represented as a list of postfix tokens. */
    public List<List<Token>> parse() {
        List<List<Token>> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(declaration());
        }
        return statements;
    }

    private List<Token> declaration() {
        if (match(TokenType.LET)) {
            return letStatement();
        }
        if (match(TokenType.PRINT)) {
            return printStatement();
        }
        throw error(peek(), "Expected 'let' or 'print' statement.");
    }

    private List<Token> letStatement() {
        Token name = consume(TokenType.IDENTIFIER, "Expect variable name.");
        consume(TokenType.EQUAL, "Expect '=' after variable name.");
        List<Token> exprPostfix = expression();
        consume(TokenType.SEMICOLON, "Expect ';' after expression.");
        // Synthetic STORE token carries the variable name.
        exprPostfix.add(new Token(TokenType.STORE, name.lexeme, null, name.line));
        return exprPostfix;
    }

    private List<Token> printStatement() {
        List<Token> exprPostfix = expression();
        consume(TokenType.SEMICOLON, "Expect ';' after expression.");
        // Synthetic PRINT_CMD token triggers printing.
        exprPostfix.add(new Token(TokenType.PRINT_CMD, "print", null, previous().line));
        return exprPostfix;
    }

    // Expression parsing with precedence (recursive descent).
    private List<Token> expression() {
        return term(); // Handles + and -
    }

    private List<Token> term() {
        List<Token> tokens = factor();
        while (match(TokenType.PLUS, TokenType.MINUS)) {
            Token operator = previous();
            List<Token> right = factor();
            tokens.addAll(right);
            tokens.add(operator); // RPN: left right op
        }
        return tokens;
    }

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

    private List<Token> primary() {
        if (match(TokenType.NUMBER, TokenType.IDENTIFIER)) {
            // The matched token is already added to the postfix list by virtue of being returned.
            // We'll create a list containing that token.
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

    // Helper methods -------------------------------------------------------

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
