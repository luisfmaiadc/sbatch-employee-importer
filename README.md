<h1 align="center">sbatch-employee-importer</h1>

<p align="center" style="margin-bottom: 20;">
  <img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 25" />
  <img src="https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Boot 4.0.0" />
  <img src="https://img.shields.io/badge/Spring%20Batch-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Batch 6.0.0" />
  <img src="https://img.shields.io/badge/apache%20maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Maven" />
  <img src="https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL" />
</p>

<p align="center">O <b>sbatch-employee-importer</b> é uma aplicação de processamento em lote (Batch Processing) desenvolvida com <b>Java 25</b> e <b>Spring Boot 4.0.0</b>. O projeto tem como objetivo demonstrar a importação performática de grandes volumes de dados de funcionários, aplicando regras de validação e persistência eficiente em banco de dados relacional.</p>

<h2>📌 Visão Geral</h2>
<p align="justify">
Este projeto foi desenvolvido para fins de aprendizado das capacidades do framework <b>Spring Batch 6.0.0</b>. O fluxo consiste na leitura de um arquivo CSV contendo 5.000 registros de funcionários, processamento com validações de negócio e escrita no banco de dados MySQL.
</p>
<p align="justify">
A arquitetura do Job foi desenhada para processar os dados em <b>chunks de 500 itens</b>, garantindo eficiência de memória e transacionalidade. Durante a etapa de processamento, é utilizado um <b>CompositeItemProcessor</b> que orquestra múltiplas validações antes que os dados sejam submetidos à escrita via <code>JdbcBatchItemWriter</code>.
</p>

<h2>🚀 Tecnologias Utilizadas</h2>

* **Java 25**
* **Spring Boot 4.0.0** + **Spring Batch 6.0.0**
* **Spring Validation** + **Spring Batch JDBC**
* **MySQL**
* **Lombok**

<h2>⚙️ Lógica de Processamento e Validação</h2>

O diferencial deste projeto está na robustez da etapa de `Processor`. Foi implementado um `CompositeItemProcessorBuilder` que encadeia dois tipos de validadores:

1.  **BeanValidatingItemProcessor:** Realiza validações estruturais básicas baseadas em anotações na classe de domínio `Funcionario`.
2.  **ValidatingItemProcessor:** Executa regras de negócio customizadas implementadas manualmente:
    * **Unicidade de E-mail:** Verifica se o e-mail já foi processado no contexto atual (usando um `Set` em memória para o scopo do step).
    * **Data de Admissão:** Garante que a data de admissão não seja futura em relação à data atual.
    * **Faixa Salarial:** Valida se o salário é positivo e não excede o teto de 100.000,00.

Registros que falham na validação são logados e descartados, não impedindo o processamento do restante do lote.

<h2>🏗️ Estrutura do Projeto</h2>

```bash
sbatch-employee-importer
│-- src/main/java/com/portfolio/luisfmdc/sbatch_employee_importer
│   ├── config/               # Configurações do Datasource
│   ├── domain/               # Entidade Funcionario
│   ├── job/                  # Configurações do Job
│   ├── step/                 # Configurações do Step
│   ├── reader/               # Configurações do Reader
│   ├── processor/            # Lógica de validação (CompositeItemProcessor)
│   ├── writer/               # Configuração de escrita
│   └── Application.java
│-- src/main/resources
│   ├── application.properties # Configurações de banco e batch
│   ├── file
│     └── funcionarios.csv     # Arquivo fonte de dados
│-- pom.xml                    # Dependências Maven
```

<h2>🛠️ Configuração e Execução</h2>

<h3>📌 Pré-requisitos</h3>

-   Java 25 instalado.
-   Apache Maven instalado.
-   MySQL Server instalado e em execução (porta 3306).

<h3>🗄️ Configuração do Banco de Dados</h3>

O projeto utiliza dois bancos de dados distintos: um para os metadados do Spring Batch e outro para os dados de negócio. Execute os scripts abaixo no seu cliente MySQL antes de rodar a aplicação.

**1. Criação dos Bancos de Dados:**
```sql
CREATE DATABASE sbatch_execution; -- Metadados do Spring Batch
CREATE DATABASE sbatch_employee;  -- Dados da aplicação
```

**2. Criação da Tabela de Funcionários (no banco `sbatch_employee`):**
```sql
USE sbatch_employee;

CREATE TABLE TbFuncionario (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(200) NOT NULL,
    departamento VARCHAR(100) NOT NULL,
    salario DECIMAL(10,2) NOT NULL,
    dataAdmissao DATE NOT NULL,
    dataCriacao TIMESTAMP NOT NULL
);
```

<h3>📜 Configuração da Aplicação (<code>application.properties</code>)</h3>

Configure as variáveis de ambiente ou edite o arquivo src/main/resources/application.properties com suas credenciais:

```properties
spring.application.name=sbatch-employee-importer

# Configuração do Banco de Metadados do Batch
spring.datasource.jdbcUrl=jdbc:mysql://localhost:3306/sbatch_execution
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
spring.batch.jdbc.initialize-schema=always

# Configuração do Banco de Dados da Aplicação (Negócio)
app.datasource.jdbcUrl=jdbc:mysql://localhost:3306/sbatch_employee
app.datasource.username=${APP_DATASOURCE_USERNAME}
app.datasource.password=${APP_DATASOURCE_PASSWORD}
```

<h3>🚀 Executando o Job</h3>

1. Clone o repositório:
```bash
git clone https://github.com/luisfmaiadc/sbatch-employee-importer.git
cd sbatch-employee-importer
```

2. Compile o projeto:
```bash
mvn clean install
```

3. Execute a aplicação:
```bash
mvn spring-boot:run
```

Ao iniciar, o Spring Boot executará o Job automaticamente, lendo o arquivo CSV e populando a tabela `TbFuncionario`.

<h2>📚 Aprendizados</h2>

Este projeto permitiu consolidar conhecimentos em:

<ul> 
  <li>Leitura e mapeamento de arquivos delimitados (.csv) utilizando <b>FlatFileItemReader</b>.</li> 
  <li>Configuração de Jobs, Steps e Chunks no Spring Batch 6.</li> 
  <li>Uso de <b>CompositeItemProcessor</b> para encadear lógicas de validação.</li> 
  <li>Escrita eficiente em banco de dados com <b>JdbcBatchItemWriter</b>.</li> 
  <li>Gerenciamento de múltiplos DataSources (separação entre dados de negócio e metadados de execução).</li> 
</ul>