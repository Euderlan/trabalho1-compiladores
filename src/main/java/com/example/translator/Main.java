package com.example.translator;

import com.example.translator.lexer.Scanner;
import com.example.translator.parser.Parser;
import com.example.translator.interpreter.Interpreter;
import com.example.translator.lexer.Token;

import java.nio.file.Paths;
import java.nio.file.Files;

import java.nio.charset.StandardCharsets;

import java.util.List;

// Classe principal que inicia o fluxo de compilação (lexical, parsing, interpretação).
public class Main {
    // Ponto de entrada da aplicação.
    public static void main(String[] args) {
        try {
            String source;
            if (args.length > 0) {
                byte[] bytes = Files.readAllBytes(Paths.get(args[0]));
                if (bytes.length >= 2 && (bytes[0] & 0xFF) == 0xFF && (bytes[1] & 0xFF) == 0xFE) {
                    source = new String(bytes, StandardCharsets.UTF_16LE);
                    // Remover possível BOM, se presente.
                } else {
                    source = new String(bytes, StandardCharsets.UTF_8);
                }
            } else {
                System.out.println("Enter source code (terminate with EOF):");
                source = new String(System.in.readAllBytes());
            }

            // Análise léxica
            Scanner scanner = new Scanner(source);
            List<Token> tokens = scanner.scanTokens();

            // Parsing (gera instruções em notação pós‑fixa)
            Parser parser = new Parser(tokens);
            List<List<Token>> statements = parser.parse();

            // Imprime a notação pós‑fixa (push/add/sub/...)
            printPostfix(statements);

            // Interpretação (executa as instruções)
            Interpreter interpreter = new Interpreter();
            interpreter.interpret(statements);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
    // Helper para imprimir a representação pós‑fixa das instruções.
    private static void printPostfix(List<List<Token>> statements) {
        for (List<Token> stmt : statements) {
            for (Token token : stmt) {
                switch (token.type) {
                    case NUMBER:
                        System.out.println("push " + token.literal);
                        break;
                    case PLUS:
                        System.out.println("add");
                        break;
                    case MINUS:
                        System.out.println("sub");
                        break;
                    case STAR:
                        System.out.println("mul");
                        break;
                    case SLASH:
                        System.out.println("div");
                        break;
                    case STORE:
                        System.out.println("store " + token.lexeme);
                        break;
                    case PRINT_CMD:
                        System.out.println("print");
                        break;
                    default:
                        // Ignora outros tokens.
                }
            }
        }
    }
}
