package com.example.translator;

import com.example.translator.parser.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        String source;

        if (args.length > 0) {
            try {
                source = Files.readString(Path.of(args[0]));
            } catch (IOException e) {
                System.err.println("Falha ao ler o arquivo: " + e.getMessage());
                return;
            }
        } else {
            try {
                source = new String(System.in.readAllBytes());
            } catch (IOException e) {
                System.err.println("Falha ao ler a entrada padrao: " + e.getMessage());
                return;
            }
        }

        // Instancia o Parser com o texto lido e realiza o parsing
        Parser parser = new Parser(source);
        parser.parse();
    }
}