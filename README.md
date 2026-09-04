# Simple Arithmetic Translator

Este projeto implementa um tradutor aritmético simples que lê uma expressão e gera uma sequência de instruções para uma máquina de pilha (`push`, `add`, `sub`, `mul`, `div`).

### Como funciona
1. **Scanner** – tokeniza a entrada (inteiros e os operadores `+ - * / ( )`).
2. **Parser** – utiliza o algoritmo *shunting‑yard* para transformar a expressão infixa em Notação Polonesa Reversa (RPN, pós‑fixa).
3. **Interpreter** – percorre a lista de tokens em RPN e produz as instruções da máquina de pilha.

### Compilação
```bash
# Compila os arquivos fonte (requere JDK 11+). No PowerShell o padrão "**/*.java" não é expandido,
# então use a lista explícita de arquivos como no exemplo abaixo:
javac -d out src\\main\\java\\com\\example\\translator\\Main.java \
    src\\main\\java\\com\\example\\translator\\lexer\\*.java \
    src\\main\\java\\com\\example\\translator\\parser\\*.java \
    src\\main\\java\\com\\example\\translator\\interpreter\\*.java
```
A pasta `out` conterá as classes compiladas.

### Execução
```bash
# Executa usando um arquivo de entrada com uma única expressão
java -cp out com.example.translator.Main caminho/para/arquivo.txt

# Ou lê a expressão via stdin (pipe)
 echo "3 + 4 * (2 - 1)" | java -cp out com.example.translator.Main
```

#### Exemplo
Arquivo de entrada (`test_input.txt`):
```
3 + 4 * (2 - 1)
```
Comando:
```
java -cp out com.example.translator.Main test_input.txt
```
Saída:
```
push 3
push 4
push 2
push 1
sub
mul
add
Result: 8
```

### Estrutura do projeto
```
├─ pom.xml                # Stub Maven (opcional, para IDEs)
├─ .gitignore             # Ignora artefatos de compilação
├─ src/main/java/…       # Pacotes Java
│   ├─ com/example/translator/
│   │   ├─ Main.java
│   │   ├─ interpreter/Interpreter.java
│   │   ├─ lexer/Scanner.java, Token.java, TokenType.java
│   │   └─ parser/Parser.java
└─ test_input.txt         # Exemplo de entrada
```

Sinta‑se à vontade para estender a linguagem (números de ponto flutuante, variáveis, etc.) ou conectar a saída a uma máquina virtual real.
