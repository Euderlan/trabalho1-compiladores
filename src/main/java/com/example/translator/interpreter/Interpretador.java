package com.example.translator.interpreter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

/**Representa uma instrução individual da máquina de pilha (ex: PUSH 10, ADD, POP a)[cite: 16].*/
class Command {

    /**Enumeração dos tipos de instruções suportadas pela máquina de pilha[cite: 16].*/
    public enum Type {
        ADD,   // Soma os dois últimos valores da pilha
        SUB,   // Subtrai os dois últimos valores da pilha
        PUSH,  // Empilha um número constante ou o valor de uma variável
        POP,   // Desempilha o valor do topo e armazena em uma variável
        PRINT  // Desempilha o topo e exibe o valor no terminal
    }

    public Command.Type type; // Tipo de operação a ser realizada
    public String arg = "";   // Argumento associado à instrução (ex: nome da variável ou valor literal)

    /**
     * Constrói uma instrução a partir de um array de strings divididas por espaço[cite: 16].
     * 
     * @param command Array contendo a instrução no índice 0 e o argumento opcional no índice 1.
     */
    public Command(String[] command) {
        // Converte o nome do comando para maiúsculas e obtém o tipo enumerado correspondente
        type = Command.Type.valueOf(command[0].toUpperCase());
        
        // Se a instrução possuir um operando (ex: ["push", "42"]), captura o argumento
        if (command.length > 1) {
            arg = command[1];
        }
    }

    @Override
    public String toString() {
        return type.name() + " " + arg;
    }
}

/**Interpretador baseado em pilha que executa sequencialmente a representação intermediária[cite: 16].*/
public class Interpretador {

    private List<String[]> commands;               // Lista de comandos prontos para processamento[cite: 16]
    private Stack<Integer> stack = new Stack<>();  // Pilha de execução para avaliação de expressões aritméticas[cite: 16]
    private Map<String, Integer> variables = new HashMap<>(); // Tabela de símbolos para memória de variáveis[cite: 16]

    /**
     * Processa a String bruta de código intermediário gerado pelo Parser e a converte em instruções[cite: 16].
     * 
     * @param input Conjunto de instruções separadas por quebras de linha.
     */
    public Interpretador(String input) {
        final String eol = System.getProperty("line.separator");
        var output = input.split(eol);

        // Limpa espaços, ignora linhas de comentário ou vazias, e divide cada comando por tokens de espaço[cite: 16]
        commands = Arrays.stream(output)
                .map(String::strip)
                .filter(s -> !s.startsWith("//") && !s.isEmpty())
                .map(s -> s.split("\\s+"))
                .collect(Collectors.toList());
    }

    /** Verifica se ainda restam instruções na fila de execução[cite: 16].*/
    public boolean hasMoreCommands() {
        return !commands.isEmpty();
    }

    /**Retorna a próxima instrução a ser interpretada e a remove da fila[cite: 16].*/
    public Command nextCommand() {
        return new Command(commands.remove(0));
    }

    /** Executa o loop principal da máquina virtual, processando cada comando sequencialmente[cite: 16].*/
    public void run() {
        while (hasMoreCommands()) {
            var command = nextCommand();

            switch (command.type) {
                case ADD:
                    // Desempilha os operandos, realiza a adição e empilha o resultado[cite: 16]
                    var arg2 = stack.pop();
                    var arg1 = stack.pop();
                    stack.push(arg1 + arg2);
                    break;

                case SUB:
                    // Desempilha os operandos, realiza a subtração (ordem: primeiro_empilhado - segundo_empilhado) e empilha[cite: 16]
                    arg2 = stack.pop();
                    arg1 = stack.pop();
                    stack.push(arg1 - arg2);
                    break;

                case PUSH:
                    // Verifica se o operando é o nome de uma variável gravada[cite: 16]
                    var value = variables.get(command.arg);
                    if (value != null) {
                        stack.push(value); // Empilha o valor armazenado na variável[cite: 16]
                    } else {
                        stack.push(Integer.parseInt(command.arg)); // Empilha o literal numérico[cite: 16]
                    }
                    break;

                case POP:
                    // Retira o valor do topo da pilha e grava na tabela de símbolos na variável especificada[cite: 16]
                    value = stack.pop();
                    variables.put(command.arg, value);     
                    break;

                case PRINT:
                    // Desempilha o resultado final e exibe a saída no console[cite: 16]
                    var arg = stack.pop();
                    System.out.println(arg);
                    break;
            }
        }
    }
}