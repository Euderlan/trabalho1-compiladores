package com.example.translator;

import com.example.translator.lexer.Scanner;
import com.example.translator.lexer.Token;
import com.example.translator.parser.Parser;
import com.example.translator.interpreter.Interpreter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Classe principal do tradutor aritmetico simples.
 *
 * O programa le uma expressao aritmetica (de um arquivo passado como argumento
 * ou da entrada padrao), gera instrucoes de maquina de pilha (push, add, sub,
 * mul, div) e exibe o resultado numerico da expressao.
 */
public class Main {
    /**
     * Ponto de entrada da aplicacao.
     *
     * @param args argumentos da linha de comando; o primeiro (opcional) pode
     *             ser o caminho para um arquivo contendo a expressao a ser
     *             traduzida.
     */
    public static void main(String[] args) {
        String source;
        // ------------------------------------------------------------
        // Leitura da fonte (arquivo ou stdin).
        // ------------------------------------------------------------
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

        // ------------------------------------------------------------
        // Etapa 1 - Analise lexica.
        // ------------------------------------------------------------
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        // ------------------------------------------------------------
        // Etapa 2 - Conversao para Notacao Polonesa Reversa (RPN).
        // ------------------------------------------------------------
        Parser parser = new Parser(tokens);
        List<Token> rpn = parser.parse();

        // ------------------------------------------------------------
        // Etapa 3 - Geracao das instrucoes da maquina de pilha.
        // ------------------------------------------------------------
        Interpreter interpreter = new Interpreter();
        List<String> instructions = interpreter.interpret(rpn);

        // ------------------------------------------------------------
        // Saida das instrucoes uma por linha.
        // ------------------------------------------------------------
        for (String line : instructions) {
            System.out.println(line);
        }

        // ------------------------------------------------------------
        // Etapa 4 - Avaliacao da expressao usando a propria pilha.
        // ------------------------------------------------------------
        java.util.Deque<Integer> stack = new java.util.ArrayDeque<>();
        for (String instr : instructions) {
            if (instr.startsWith("push ")) {
                double value = Double.parseDouble(instr.substring(5).trim());
                stack.push((int)Math.round(value));
            } else {
                // Operacao binaria - requer ao menos dois operandos na pilha.
                if (stack.size() < 2) {
                    System.err.println("Numero insuficiente de valores na pilha para a operacao: " + instr);
                    break;
                }
                int b = stack.pop();
                int a = stack.pop();
                int res;
                switch (instr) {
                    case "add":
                        res = a + b;
                        break;
                    case "sub":
                        res = a - b;
                        break;
                    case "mul":
                        res = a * b;
                        break;
                    case "div":
                        res = a / b;
                        break;
                    default:
                        System.err.println("Instrucao desconhecida: " + instr);
                        continue;
                }
                stack.push(res);
            }
        }
        if (!stack.isEmpty()) {
            System.out.println("Result: " + stack.peek());
        }
    }
}
