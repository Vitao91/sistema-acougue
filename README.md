# 🥩 Sistema de Gestão & PDV de Açougue

Sistema em **Java** com interface gráfica **Swing**, arquitetura em camadas (**MVC + DAO**) e persistência em banco de dados relacional. Desenvolvido para gerenciar estoques de cortes de carne e automatizar o cálculo de vendas por peso (balança).

---

## 🚀 Funcionalidades

- **Ponto de Venda (PDV / Caixa):**
  - Seleção de cortes de carne cadastrados.
  - Exibição de preço por quilo e estoque disponível em tempo real.
  - **Cálculo automático de valor:** `Preço / Kg × Peso na Balança = Total (R$)`.
  - Baixa automática no estoque ao finalizar a venda.
  - Emissão de comprovante de venda digital.
- **Cadastro e Gestão de Carnes:**
  - Cadastro de novos cortes de carne.
  - Definição de preço por quilo e estoque em quilos.
  - Tabela com listagem em tempo real e opção de exclusão.
- **Histórico e Faturamento:**
  - Relatório completo de vendas realizadas.
  - Cards com indicadores: Total Faturado (R$), Kilos Vendidos (Kg) e Total de Atendimentos.
- **Banco de Dados Descomplicado:**
  - Por padrão, utiliza **SQLite local** (`acougue.db`), criando as tabelas e dados de teste automaticamente na primeira execução (não requer nenhuma instalação de servidor).
  - Também possui suporte pronto para **MySQL** com script de banco incluído em `database/schema-mysql.sql`.

---

## 🏗️ Arquitetura do Projeto

O projeto segue boas práticas de divisão de responsabilidades:

```
src/main/java/com/acougue/
│
├── modelo/        # Classes de Domínio (Produto, Venda)
├── dao/           # Persistência e comunicação com BD (Conexao, ProdutoDao, VendaDao)
├── service/       # Regras de Negócio e Cálculos (ProdutoService, VendaService)
├── view/          # Telas Gráficas Swing (TelaPrincipal, CadastrarProdutoView, VendaView, HistoricoVendasView)
└── Main.java      # Ponto de entrada com tema moderno FlatLaf
```

---

## 🛠️ Tecnologias Utilizadas

- **Java 21**
- **Java Swing** com biblioteca de estilo **FlatLaf**
- **JDBC** (Java Database Connectivity)
- **SQLite** & **MySQL**
- **Apache Maven** para gerenciamento de dependências

---

## ▶️ Como Executar o Projeto

### Pelo IntelliJ IDEA:
1. Abra o IntelliJ.
2. Clique em **File > Open** e selecione a pasta `projeto-acougue`.
3. Aguarde o Maven carregar as dependências.
4. Abra o arquivo `src/main/java/com/acougue/Main.java`.
5. Clique no botão verde de **Run (Play)** ao lado do método `main`.

### Pelo Terminal (com Maven):
```bash
mvn clean compile exec:java
```

---

## 👤 Autor
Desenvolvido por **Victor**.
