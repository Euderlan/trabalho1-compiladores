package com.example.translator.lexer;

import java.util.ArrayList;
import java.util.List;

/**
 * Analisador léxico (tokenizador) simples para expressões aritméticas.
 * Reconhece literais inteiros, operadores (+, -, *, /) e parênteses.
 */
public class Scanner {
    /** Texto de entrada (fonte) a ser analisado. */
    private final String source;
    /** Lista de tokens reconhecidos. */
    private final List<Token> tokens = new ArrayList<>();

    // Indices auxiliares para o algoritmo de varredura.
    private int start = 0;   // início do lexema atual
    private int current = 0; // posição atual no texto de entrada
    private int line = 1;    // número da linha (para mensagens de erro)

    /**Cria um scanner para o texto fornecido. */
    /** @param source texto contendo a expressão a ser tokenizada */
    public Scanner(String source) {
        this.source = source;
    }

    // Estágio 1 — nextToken() retornando String

    /**Retorna o próximo token como String, avançando na entrada.
     * Reconhece números inteiros (um ou mais dígitos), +, - e EOF.
     * @return próximo token ou null ao fim da entrada */
    public Token nextToken() {
        char ch = peek();
        if (ch == '\0') return null; // Retorna null ao chegar ao fim para evitar loop infinito

        if (ch == '0') {
            advance();
            return new Token(TokenType.NUMBER, Character.toString(ch), null, line);
        } else if (Character.isDigit(ch)) {
            return readNumber(); // Chama readNumber() aqui
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

    /**Reconhece um número inteiro positivo a partir da posição atual.
     * Consome todos os dígitos consecutivos e devolve o lexema.
     * @return lexema numérico */
    private Token readNumber() {
        int start = current;
        while (Character.isDigit(peek())) {
            advance();
        }
        String n = source.substring(start, current);
        return new Token(TokenType.NUMBER, n, null, line);
    }


     //@return lista imutável dos tokens encontrados, incluindo o token EOF ao final
    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            // Cada iteração inicia a captura de um novo lexema.
            start = current;
            scanToken();
        }
        // Token de fim de arquivo.
        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    //Analisa o próximo caractere da entrada e gera o token correspondente.
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
                // Ignora espaços em branco.
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

    /**Reconhece um número (inteiro ou ponto flutuante) a partir da posição atual. */
    private void number() {
        while (isDigit(peek())) advance();
        // Verifica ponto decimal.
        if (peek() == '.' && isDigit(peekNext())) {
            // Consome o ponto.
            advance();
            // Consome a parte fracionária.
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

    // Lookahead one character ahead without consuming.
    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    /**Verifica se o caractere é uma letra (a‑z, A‑Z) ou sublinhado. */
    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
               (c >= 'A' && c <= 'Z') ||
               c == '_';
    }

    /**Verifica se o caractere é alfanumérico (letra ou dígito). */
    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    /**Reconhece um identificador (sequência de letras, dígitos ou sublinhado). */
    private void identifier() {
        while (isAlphaNumeric(peek())) advance();
        // O lexema já está na substring; literal opcional não usado.
        addToken(TokenType.IDENTIFIER);
    }

    /**Emite mensagem de erro de lexicalização para o usuário. */
    private void error(String message) {
        System.err.println(message);
    }

    /**Verifica se o caractere está entre '0' e '9'.*/
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    /**Consome o próximo caractere da fonte e avança o cursor.*/
    private char advance() {
        return source.charAt(current++);
    }

    /**Olha o próximo caractere sem avançar o cursor.*/
    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    /**Indica se já chegamos ao final da string de origem.*/
    private boolean isAtEnd() {
        return current >= source.length();
    }

    /**Cria um token sem literal associado.*/
    private void addToken(TokenType type) {
        addToken(type, null);
    }

    /**Cria um token possivelmente contendo um literal (ex.: valor numérico).*/
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }


    /**Teste de smoke: imprime cada token da entrada "289-85+0+69". */
    public static void main(String[] args) {
        String input = "289-85+0+69";
        Scanner scan = new Scanner(input);
        Token tok; // <--- Altere para Token
        while ((tok = scan.nextToken()) != null) {
            System.out.println(tok);
        }
    }
}
