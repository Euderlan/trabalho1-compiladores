package com.example.translator.parser;

import com.example.translator.lexer.Scanner;
import com.example.translator.lexer.Token;
import com.example.translator.lexer.TokenType;

/**
 * Parser de Descida Recursiva com suporte a múltiplos comandos (letStatement e printStatement).
 */
public class Parser {

    private Scanner scan;
    private Token currentToken;

    public Parser(byte[] input) {
        this.scan = new Scanner(new String(input));
        this.currentToken = scan.nextToken();
    }

    public Parser(String input) {
        this(input.getBytes());
    }

    private void nextToken() {
        this.currentToken = scan.nextToken();
    }

    private void match(TokenType t) {
        if (currentToken != null && currentToken.type == t) {
            nextToken();
        } else {
            throw new Error("syntax error");
        }
    }

    public void parse() {
        statements();
    }

    /** statements -> statement* */
    void statements() {
        while (currentToken != null && currentToken.type != TokenType.EOF) {
            statement();
        }
    }

    /** statement -> printStatement | letStatement */
    void statement() {
        if (currentToken.type == TokenType.PRINT) {
            printStatement();
        } else if (currentToken.type == TokenType.LET) {
            letStatement();
        } else {
            throw new Error("syntax error");
        }
    }

    /** printStatement -> 'print' expr ';' */
    void printStatement() {
        match(TokenType.PRINT);
        expr();
        System.out.println("print");
        match(TokenType.SEMICOLON);
    }

    /** letStatement -> 'let' identifier '=' expr ';' */
    void letStatement() {
        match(TokenType.LET);
        String id = currentToken.lexeme;
        match(TokenType.IDENT);
        match(TokenType.EQ);
        expr();
        System.out.println("pop " + id);
        match(TokenType.SEMICOLON);
    }

    /** expr -> term oper */
    void expr() {
        term();
        oper();
    }

    /** oper -> + term oper | - term oper | ϵ */
    void oper() {
        if (currentToken != null && currentToken.type == TokenType.PLUS) {
            match(TokenType.PLUS);
            term();
            System.out.println("add");
            oper();
        } else if (currentToken != null && currentToken.type == TokenType.MINUS) {
            match(TokenType.MINUS);
            term();
            System.out.println("sub");
            oper();
        }
    }

    /** term -> number | identifier */
    void term() {
        if (currentToken != null && currentToken.type == TokenType.NUMBER) {
            number();
        } else if (currentToken != null && currentToken.type == TokenType.IDENT) {
            System.out.println("push " + currentToken.lexeme);
            match(TokenType.IDENT);
        } else {
            throw new Error("syntax error");
        }
    }

    void number() {
        System.out.println("push " + currentToken.lexeme);
        match(TokenType.NUMBER);
    }

    /** Teste */
    public static void main(String[] args) {
        String input = """
            let a = 42 + 5 - 8;
            let b = 56 + 8;
            print a + b + 6;
                """;

        Parser p = new Parser(input.getBytes());
        p.parse();
    }
}