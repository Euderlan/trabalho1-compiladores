package com.example.translator;

import com.example.translator.interpreter.Interpretador;
import com.example.translator.parser.Parser;

public class Main {
    public static void main(String[] args) {
        String input = """
            let a = 10 * 4 + 2;
            let b = 20/2;
            print a + b;        
                """;
        
        Parser p = new Parser(input.getBytes());
        p.parse();

        Interpretador i = new Interpretador(p.output());
        i.run();
    }
}