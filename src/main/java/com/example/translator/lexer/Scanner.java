package com.example.translator.lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Classe que realiza a análise léxica (scanner) da fonte.
public class Scanner {
    // Fonte de código e lista de tokens.
    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    private int start = 0;
    private int current = 0;
    private int line = 1;

    private static final Map<String, TokenType> keywords;
    static {
        // Mapeamento de palavras‑chave.
        keywords = new HashMap<>();
        keywords.put("let", TokenType.LET);
        keywords.put("print", TokenType.PRINT);
    }

    // Construtor que recebe a fonte a ser analisada.
    public Scanner(String source) {
        this.source = source;
    }

    // Escaneia toda a fonte e retorna a lista de tokens.
    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    // Processa um único caractere.
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
            case '=':
                addToken(TokenType.EQUAL);
                break;
            case ';':
                addToken(TokenType.SEMICOLON);
                break;
            case ' ':
            case '\r':
            case '\t':
                // Ignora espaço em branco.
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
                    // Por enquanto, ignora caracteres inesperados.
                }
                break;
        }
    }

    // Lê um identificador ou palavra‑chave.
    private void identifier() {
        while (isAlphaNumeric(peek())) advance();
        String text = source.substring(start, current);
        TokenType type = keywords.getOrDefault(text, TokenType.IDENTIFIER);
        addToken(type);
    }

    // Lê um número literal.
    private void number() {
        while (isDigit(peek())) advance();
        String text = source.substring(start, current);
        int value = Integer.parseInt(text);
        addToken(TokenType.NUMBER, value);
    }

    // Verifica se caractere é letra ou underline.
    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }

    // Verifica se letra ou número.
    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    // Verifica se caractere é dígito.
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    // Avança e devolve o caractere atual.
    private char advance() {
        return source.charAt(current++);
    }

    // Olha próximo caractere sem consumir.
    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    // Verifica se o scanner chegou ao final da fonte.
    private boolean isAtEnd() {
        return current >= source.length();
    }

    // Adiciona token simples sem literal.
    private void addToken(TokenType type) {
        addToken(type, null);
    }

    // Adiciona token, opcionalmente com literal.
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }
}
