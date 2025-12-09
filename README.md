# 🏛️ Documentação das Principais Classes do Projeto

Abaixo estão listadas apenas as classes que realmente fazem parte do projeto, conforme a estrutura atual do repositório.

---

## 🟦 Classe Filme

**Pacote:** `br.ce.wcaquino.entidades`

### Função:
Representa um filme disponível para locação.

### Atributos principais:
- `String nome`
- `Integer estoque`
- `Double precoLocacao`

### Responsabilidade:
Manter informações essenciais sobre um filme a ser locado.

Os testes garantem:
✔ Estoque válido  
✔ Preço definido  
✔ Integridade dos dados da entidade  

---

## 🟦 Classe Usuario

**Pacote:** `br.ce.wcaquino.entidades`

### Função:
Representa o usuário que realiza a locação.

### Atributos:
- `String nome`

### Responsabilidade:
Servir como entidade básica para identificar quem está realizando uma locação.

Testes garantem:
✔ Criação correta  
✔ Comparação e integridade  

---

## 🟦 Classe Locacao

**Pacote:** `br.ce.wcaquino.entidades`

### Função:
Representa o ato da locação.

### Atributos principais:
- `Usuario usuario`
- `List<Filme> filmes`
- `Date dataLocacao`
- `Date dataRetorno`
- `Double valor`

### Responsabilidade:
Registrar e armazenar tudo o que envolve o processo de locação.

---

## 🟦 Classe LocacaoService

**Pacote:** `br.ce.wcaquino.servicos`

### Função:
**Classe principal de regras de negócio do projeto.**

### Responsabilidades:
- Validar usuário e filmes  
- Verificar estoque  
- Calcular o valor total  
- Criar a locação  
- Definir a data de devolução  
- Aplicar regras específicas conforme os testes  

Essa é a classe com maior cobertura de testes no projeto.

Os testes validam:
✔ Locação simples  
✔ Exceções (filme sem estoque, usuário nulo, filme nulo)  
✔ Descontos progressivos  
✔ Cálculo de valor  
✔ Datas de locação e devolução  

---

## 🟦 Classe Calculadora

**Pacote:** `br.ce.wcaquino.servicos`

### Função:
Realizar operações matemáticas simples.

### Métodos principais:
- `somar(int a, int b)`
- `subtrair(int a, int b)`

### Importância nos testes:
Serve como base para testes introdutórios usando JUnit.

---

## 🟦 Classe AssertTest

**Pacote:** `br.ce.wcaquino.servicos`

### Função:
Demonstra exemplos práticos de uso de asserts do JUnit.

Abrange:
- Comparações numéricas  
- Comparações de objetos  
- Testes de igualdade  
- Testes de condições booleanas  