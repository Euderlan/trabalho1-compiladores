package com.example.translator.parser;

import com.example.translator.lexer.Token;
import com.example.translator.lexer.TokenType;

import java.util.*;

// Analisador sintático (parser) que converte a lista de tokens produzida pelo Scanner
// de notação infixa para Notação Polonesa Reversa (RPN - postfix) usando o algoritmo
// da "shunting-yard". O resultado (lista de tokens em ordem pós-fixa) pode ser
// alimentado ao Interpreter para gerar as instruções da máquina de pilha.
public class Parser {
    // Tokens de entrada.
    private final List<Token> tokens;
    // Índice do token atual.
    private int current = 0;

    // Precedência dos operadores (valor maior = maior precedência).
    private static final Map<TokenType, Integer> PRECEDENCE = Map.of(
            TokenType.PLUS, 1,
            TokenType.MINUS, 1,
            TokenType.STAR, 2,
            TokenType.SLASH, 2
    );

    // Cria um novo parser.
    // @param tokens lista de tokens já lexicalmente analisados
    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    // Executa o parsing e devolve a lista de tokens em ordem pós-fixa (RPN).
    // @return lista de tokens na ordem que pode ser consumida por um interpretador
    public List<Token> parse() {
        List<Token> output = new ArrayList<>();
        Deque<Token> operators = new ArrayDeque<>();

        while (!isAtEnd()) {
            Token token = advance();
            switch (token.type) {
                case NUMBER:
                    output.add(token);
                    break;
                case LEFT_PAREN:
                    operators.push(token);
                    break;
                case RIGHT_PAREN:
                    // Desempilha ate encontrar o parenteses esquerdo correspondente.
                    while (!operators.isEmpty() && operators.peek().type != TokenType.LEFT_PAREN) {
                        output.add(operators.pop());
                    }
                    // Remove o parenteses esquerdo da pilha.
                    if (!operators.isEmpty() && operators.peek().type == TokenType.LEFT_PAREN) {
                        operators.pop();
                    }
                    break;
                case PLUS:
                case MINUS:
                case STAR:
                case SLASH:
                    // Enquanto houver operador de maior ou igual precedencia no topo da pilha, desempilha-o.
                    while (!operators.isEmpty()
                            && operators.peek().type != TokenType.LEFT_PAREN
                            && PRECEDENCE.getOrDefault(operators.peek().type, 0) >= PRECEDENCE.getOrDefault(token.type, 0)) {
                        output.add(operators.pop());
                    }
                    // Empilha o operador corrente.
                    operators.push(token);
                    break;
                default:
                    // Ignora outros tokens (por exemplo, EOF, que sera tratado apos o loop).
                    break;
            }
        }

        // Esvazia a pilha de operadores restante.
        while (!operators.isEmpty()) {
            output.add(operators.pop());
        }
        return output;
    }

    /** Verifica se ja consumimos todos os tokens ou se o proximo e EOF.*/
    private boolean isAtEnd() {
        return current >= tokens.size() || tokens.get(current).type == TokenType.EOF;
    }

    /**Consome o proximo token e avanca o cursor.*/
    private Token advance() {
        if (!isAtEnd()) {
            return tokens.get(current++);
        }
        // Caso nunca ocorra, devolve um token EOF ficticio.
        return new Token(TokenType.EOF, "", null, -1);
    }
}
