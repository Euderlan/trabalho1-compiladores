package com.example.translator.interpreter;

import com.example.translator.lexer.Token;
import com.example.translator.lexer.TokenType;

import java.util.ArrayList;
import java.util.List;

/**
 * Interpretador que recebe uma lista de tokens em ordem pós‑fixa (RPN) e gera
 * as instruções correspondentes para uma máquina de pilha simples.
 * Cada instrução é representada como uma string, por exemplo:
 *   "push 5", "add", "sub", "mul", "div".
 */
public class Interpreter {
    /**
     * Converte a lista de tokens RPN em instruções da máquina de pilha.
     *
     * @param rpnTokens lista de tokens na ordem pós‑fixa
     * @return lista de instruções (uma por linha) que podem ser executadas por um
     *         interpretador de pilha
     */
    public List<String> interpret(List<Token> rpnTokens) {
        List<String> instructions = new ArrayList<>();
        for (Token token : rpnTokens) {
            switch (token.type) {
                case NUMBER:
                    // Empilha o valor literal.
                    instructions.add("push " + token.literal);
                    break;
                case PLUS:
                    instructions.add("add");
                    break;
                case MINUS:
                    instructions.add("sub");
                    break;
                case STAR:
                    instructions.add("mul");
                    break;
                case SLASH:
                    instructions.add("div");
                    break;
                default:
                    // Ignora outros tipos (parênteses não devem aparecer aqui).
                    break;
            }
        }
        return instructions;
    }
}
