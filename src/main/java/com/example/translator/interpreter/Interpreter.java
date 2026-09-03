package com.example.translator.interpreter;

import com.example.translator.lexer.Token;
import com.example.translator.lexer.TokenType;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntBinaryOperator;

// Classe que interpreta e executa instruções em notação pós‑fixa (RPN).
public class Interpreter {
    private final Deque<Integer> stack = new ArrayDeque<>();
    private final Map<String, Integer> variables = new HashMap<>();

    /**
     * Executa a lista de instruções produzidas pelo parser.
     */
    public void interpret(List<List<Token>> statements) {
        for (List<Token> stmt : statements) {
            for (Token token : stmt) {
                switch (token.type) {
                    case NUMBER:
                        // Empilha literal numérico.
                        stack.push((Integer) token.literal);
                        break;
                    case IDENTIFIER:
                        // Busca valor da variável.
                        Integer value = variables.get(token.lexeme);
                        if (value == null) {
                            runtimeError(token, "Undefined variable.");
                        }
                        stack.push(value);
                        break;
                    case PLUS:
                        binaryOp((a, b) -> a + b);
                        break;
                    case MINUS:
                        binaryOp((a, b) -> a - b);
                        break;
                    case STAR:
                        binaryOp((a, b) -> a * b);
                        break;
                    case SLASH:
                        binaryOp((a, b) -> {
                            if (b == 0) {
                                throw new RuntimeException("Division by zero");
                            }
                            return a / b;
                        });
                        break;
                    case STORE:
                        // Armazena valor na variável.
                        Integer val = stack.pop();
                        variables.put(token.lexeme, val);
                        break;
                    case PRINT_CMD:
                        // Imprime valor no topo da pilha.
                        System.out.println(stack.pop());
                        break;
                    default:
                        // Nenhuma ação para outros tipos de token.
                }
            }
        }
    }

    // Executa operação binária entre dois operandos da pilha.
    private void binaryOp(IntBinaryOperator op) {
        int right = stack.pop();
        int left = stack.pop();
        stack.push(op.applyAsInt(left, right));
    }

    // Lança exceção de erro de execução.
    private void runtimeError(Token token, String message) {
        throw new RuntimeException("[line " + token.line + "] Runtime error: " + message);
    }
}
