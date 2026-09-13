# Simple Arithmetic Translator

Este projeto implementa um tradutor aritmético simples que lê uma expressão e gera uma sequência de instruções para uma máquina de pilha (`push`, `add`, `sub`, `mul`, `div`).

### Como funciona
1. **Scanner** – tokeniza a entrada (inteiros e os operadores `+ - * / ( )`).
2. **Parser** – analisa a expressão usando descida recursiva (ou *shunting‑yard*) e gera as instruções.
3. **Interpreter** – percorre os tokens e produz/avalia as instruções da máquina de pilha.

---

### Compilação e Execução (Windows)

#### No PowerShell:
```powershell
# 1. Compilar todos os arquivos considerando codificação UTF-8
javac -encoding utf-8 (Get-ChildItem -Recurse -Filter *.java).FullName

# 2. Executar a classe de teste do Parser diretamente
java -cp src/main/java com.example.translator.parser.Parser

# 3. Executar a classe principal da aplicação (Main)
java -cp src/main/java com.example.translator.Main

# 1. Compilar direcionando a saída para a pasta bin/out
javac -encoding utf-8 -d out src\main\java\com\example\translator\Main.java src\main\java\com\example\translator\lexer\*.java src\main\java\com\example\translator\parser\*.java src\main\java\com\example\translator\interpreter\*.java

# 2. Executar a classe Parser
java -cp out com.example.translator.parser.Parser

# 3. Executar o programa completo via arquivo de entrada
java -cp out com.example.translator.Main test_input.txt

3 + 4 * (2 - 1)

java -cp src/main/java com.example.translator.Main test_input.txt

push 3
push 4
push 2
push 1
sub
mul
add
Result: 8

javac -encoding utf-8 (Get-ChildItem -Recurse -Filter *.java).FullName
java -cp src/main/java com.example.translator.lexer.Scanner

Saída esperada:<NUMBER>45</NUMBER>
<PLUS>+</PLUS>
<IDENT>preco</IDENT>
<MINUS>-</MINUS>
<NUMBER>876</NUMBER>

### 4. Interpretador (Execução em Máquina de Pilha)
O **Interpretador** processa a representação intermediária gerada pelo `Parser` simulando o comportamento de uma máquina virtual[cite: 9]:
* **Pilha (`Stack<Integer>`)**: Avalia as operações aritméticas e armazena os operandos temporários[cite: 9].
* **Tabela de Símbolos (`Map<String, Integer>`)**: Armazena as variáveis e recupera seus valores durante a execução[cite: 9].
* **Instruções Suportadas**: `PUSH`, `POP`, `ADD`, `SUB` e `PRINT`[cite: 9].

---

### Execução Completa do Interpretador (Parte 8)

#### No PowerShell:
```powershell
# Compilar todos os pacotes com codificação UTF-8
javac -encoding utf-8 (Get-ChildItem -Recurse -Filter *.java).FullName

# Executar o fluxo completo (Scanner -> Parser -> Interpretador)
java -cp src/main/java com.example.translator.Main

let a = 42 + 2;
let b = 15 + 3;
print a + b;

Saida final:
62