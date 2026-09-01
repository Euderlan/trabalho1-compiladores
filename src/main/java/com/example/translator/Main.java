package com.example.translator;

import com.example.translator.lexer.Scanner;
import com.example.translator.parser.Parser;
import com.example.translator.interpreter.Interpreter;
import com.example.translator.lexer.Token;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            String source;
            if (args.length > 0) {
                source = new String(Files.readAllBytes(Paths.get(args[0])));
            } else {
                System.out.println("Enter source code (terminate with EOF):");
                source = new String(System.in.readAllBytes());
            }

            // Lexical analysis
            Scanner scanner = new Scanner(source);
            List<Token> tokens = scanner.scanTokens();

            // Parsing (produces postfix statements)
            Parser parser = new Parser(tokens);
            List<List<Token>> statements = parser.parse();

            // Interpretation (executes the postfix commands)
            Interpreter interpreter = new Interpreter();
            interpreter.interpret(statements);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
