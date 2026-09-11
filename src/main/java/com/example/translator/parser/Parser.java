package com.example.translator.parser;

import com.example.translator.lexer.Scanner;
import com.example.translator.lexer.Token;
import com.example.translator.lexer.TokenType;

/**
 * Parser de Descida Recursiva atualizado para aceitar termos (números ou identificadores).
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
        expr();
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

    /** Teste da Parte 2. */
    public static void main(String[] args) {
        String input = "45  + preco - 876";
        Parser p = new Parser(input.getBytes());
        p.parse();
    }
}