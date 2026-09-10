package com.example.translator.parser;

import com.example.translator.lexer.Scanner;
import com.example.translator.lexer.Token;
import com.example.translator.lexer.TokenType;

/**
 * Parser de Descida Recursiva configurado para analisar expressões com 
 * números e operadores ('+' e '-'), imprimindo as instruções de máquina de pilha.
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

    void expr() {
        number();
        oper();
    }

    void oper() {
        if (currentToken != null && currentToken.type == TokenType.PLUS) {
            match(TokenType.PLUS);
            number();
            System.out.println("add");
            oper();
        } else if (currentToken != null && currentToken.type == TokenType.MINUS) {
            match(TokenType.MINUS);
            number();
            System.out.println("sub");
            oper();
        }
    }

    void number() {
        System.out.println("push " + currentToken.lexeme);
        match(TokenType.NUMBER);
    }

    /** Exemplo de teste conforme o documento fornecido. */
    public static void main(String[] args) {
        String input = "45  + 89   -       876";
        Parser p = new Parser(input.getBytes());
        p.parse();
    }
}