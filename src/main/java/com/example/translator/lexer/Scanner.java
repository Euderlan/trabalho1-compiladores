package com.example.translator.lexer;

import java.util.ArrayList;
import java.util.List;

/**
 * Analisador léxico (tokenizador) simples para expressões aritméticas.
 * Reconhece literais inteiros e operadores (+, -).
 */
public class Scanner {
    /** Texto de entrada (fonte) a ser analisado. */
    private final String source;
    /** Lista de tokens reconhecidos. */
    private final List<Token> tokens = new ArrayList<>();

    // Índices auxiliares para o algoritmo de varredura.
    private int start = 0;   // início do lexema atual
    private int current = 0; // posição atual no texto de entrada
    private int line = 1;    // número da linha (para mensagens de erro)

    /** Cria um scanner para o texto fornecido através de String. */
    public Scanner(String source) {
        this.source = source;
    }

    /** Cria um scanner para o texto fornecido através de um array de bytes. */
    public Scanner(byte[] input) {
        this.source = new String(input);
    }

    /** Ignores espaços em branco, tabulações e quebras de linha na posição atual. */
    private void skipWhitespace() {
        char ch = peek();
        while (ch == ' ' || ch == '\r' || ch == '\t' || ch == '\n') {
            if (ch == '\n') {
                line++;
            }
            advance();
            ch = peek();
        }
    }

    /** Retorna o próximo token como Token, avançando na entrada. */
    public Token nextToken() {
        // Pula espaços antes de tentar extrair o próximo token
        skipWhitespace();

        char ch = peek();
        if (ch == '\0') {
            return new Token(TokenType.EOF, "", null, line);
        }

        if (Character.isDigit(ch)) {
            return readNumber();
        }

        switch (ch) {
            case '+':
                advance();
                return new Token(TokenType.PLUS, "+", null, line);
            case '-':
                advance();
                return new Token(TokenType.MINUS, "-", null, line);
            default:
                break;
        }
        throw new Error("lexical error at " + ch);
    }

    /** Reconhece um número inteiro positivo a partir da posição atual. */
    private Token readNumber() {
        int startPos = current;
        while (Character.isDigit(peek())) {
            advance();
        }
        String n = source.substring(startPos, current);
        return new Token(TokenType.NUMBER, n, null, line);
    }

    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(':
                addToken(TokenType.LEFT_PAREN);
                break;
            case ')':
                addToken(TokenType.RIGHT_PAREN);
                break;
            case '+':
                addToken(TokenType.PLUS);
                break;
            case '-':
                addToken(TokenType.MINUS);
                break;
            case '*':
                addToken(TokenType.STAR);
                break;
            case '/':
                addToken(TokenType.SLASH);
                break;
            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                line++;
                break;
            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    error("Caractere inesperado: '" + c + "' na linha " + line);
                }
                break;
        }
    }

    private void number() {
        while (isDigit(peek())) advance();
        if (peek() == '.' && isDigit(peekNext())) {
            advance();
            while (isDigit(peek())) advance();
        }
        String text = source.substring(start, current);
        Object value;
        if (text.contains(".")) {
            value = Double.parseDouble(text);
        } else {
            value = Integer.parseInt(text);
        }
        addToken(TokenType.NUMBER, value);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
               (c >= 'A' && c <= 'Z') ||
               c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) advance();
        addToken(TokenType.IDENTIFIER);
    }

    private void error(String message) {
        System.err.println(message);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private char advance() {
        return source.charAt(current++);
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }
}