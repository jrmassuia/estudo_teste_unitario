
### IntelliJ / Eclipse:
- Botão direito em `src/test/java` → **Run Tests**  
- Execute individualmente se desejar

---

# 📚 Tecnologias utilizadas

| Tecnologia | Função |
|-----------|--------|
| **Java 7/8** | Linguagem utilizada |
| **JUnit 4 / 5** | Framework de testes |
| **Maven** | Gerenciamento de build |
| **Mockito (quando aplicável)** | Mocks e isolamento |

---

# 🏛️ Documentação das Principais Classes do Projeto

A seguir, uma visão geral das classes principais do domínio e seu propósito dentro das regras de negócio usadas nos testes.

---

## 🟦 **Classe Filme**

**Pacote:** `br.ce.wcaquino.entidades`

### Função:
Representa um filme disponível para locação.

### Atributos principais:
- `String nome`
- `Integer estoque`
- `Double precoLocacao`

### Responsabilidade:
Garantir que o filme tenha dados válidos antes de ser locado.

### Exemplos de validação:
✔ Estoque deve ser maior que zero  
✔ Preço não pode ser negativo  

---

## 🟦 **Classe Usuario**

**Pacote:** `br.ce.wcaquino.entidades`

### Função:
Representa o cliente que realizará a locação.

### Atributos principais:
- `String nome`

### Responsabilidade:
Ser a entidade mínima para validar regras da locação.  
Os testes garantem que o usuário seja criado e comparado corretamente.

---

## 🟦 **Classe Locacao**

**Pacote:** `br.ce.wcaquino.entidades`

### Função:
Representa o ato da locação realizada.

### Atributos principais:
- `Usuario usuario`
- `List<Filme> filmes`
- `Date dataLocacao`
- `Date dataRetorno`
- `Double valor`

### Responsabilidade:
Área central da regra de negócio, registrando:
- quais filmes foram locados  
- quando a locação ocorreu  
- quando será a devolução  
- qual o valor total  

---

## 🟦 **Classe LocacaoService**

**Pacote:** `br.ce.wcaquino.servicos`

### Função:
**Coração das regras de negócio do projeto.**

### Responsabilidades:
- Validar entrada (filmes, usuário, estoque)  
- Calcular preço total  
- Aplicar descontos progressivos  
- Ajustar data de devolução com base no dia da semana  
- Criar o objeto Locacao  
- Tratar exceções específicas  

### Motivos para ser a classe mais testada:
- Contém regras complexas  
- Manipula datas  
- Pode lançar diversas exceções  
- Depende de serviços auxiliares (ex: calendário)  

---

## 🟦 **Classe SPCService (quando utilizada)**

### Função:
Simular uma consulta externa de restrição financeira.

### Responsabilidade:
Retornar se o usuário está negativado ou não.

### Importância nos testes:
- Depende de mock, pois é um serviço externo  
- Afeta o fluxo da locação (usuário negativado não pode alugar)

---

## 🟦 **Class LocacaoDAO / DAOFake**

### Função:
Simular operações de persistência.

### Responsabilidade:
Salvar informações da locação.

### Importância nos testes:
- Evitar dependência com banco de dados real  
- Demonstrar testes com stubs/mocks  

---

# 🏁 Conclusão

Este repositório é um estudo completo sobre **testes unitários em Java**, aplicando:

- Regras de negócio reais  
- Exceções  
- Datas  
- Descontos  
- Testes parametrizados  
- Mocks  
- Boas práticas de organização  

Serve como excelente base para quem deseja aprender qualidade de software, TDD e testes automatizados no ecossistema Java.

---