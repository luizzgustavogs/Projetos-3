# 🚀 Guia de Execução — Projeto Edenred

Este guia contém as instruções necessárias para configurar o ambiente e rodar a aplicação localmente em sua máquina.

---

## ✅ Pré-requisitos

Antes de começar, você precisará ter instalado em sua máquina as seguintes ferramentas:
* [Git](https://git-scm.com/)
* [Java JDK 25](https://www.oracle.com/java/)

Você pode verificar se já possui as ferramentas instaladas utilizando os comandos no terminal:
```bash
java -version
git --version

```

> **💡 Observações Importantes:**
> * **Banco de Dados:** Não é necessário instalar ou configurar um banco de dados externo. O projeto utiliza armazenamento em memória, facilitando a execução inicial.
> * **Maven:** Não é necessário instalar o Maven globalmente. O repositório já inclui o *Maven Wrapper* (`mvnw`), que baixa automaticamente a versão correta necessária para o projeto.
> 
> 

---

## 🛠️ Passo a Passo para Execução

### 1. Clonar o Repositório

Abra o seu terminal (ou prompt de comando) e execute o comando abaixo para clonar o projeto:

```bash
git clone [https://github.com/luizzgustavogs/Projetos-3.git](https://github.com/luizzgustavogs/Projetos-3.git)
cd Projetos-3

```

### 2. Executar a Aplicação

Escolha o comando ideal baseado no sistema operacional que você está utilizando:

* **No Linux ou macOS:**
```bash
./mvnw spring-boot:run

```


* **No Windows (Prompt de Comando ou PowerShell):**
```cmd
mvnw.cmd spring-boot:run

```



*(Caso você possua o Maven instalado localmente na sua máquina e prefira utilizá-lo, o comando equivalente é `mvn spring-boot:run`)*

### 3. Acessar a Aplicação

Após o terminal indicar que a aplicação inicializou com sucesso, abra o seu navegador e acesse o endereço:

👉 **[http://localhost:8080](https://www.google.com/search?q=http://localhost:8080)**

---

## ⚙️ Configurações Opcionais e Resolução de Problemas

Se você já tiver outro serviço rodando na porta 8080, o Spring Boot não irá iniciar. Para resolver:

1. Abra o arquivo `src/main/resources/application.properties`.
2. Adicione ou altere a seguinte linha para definir uma nova porta (ex: 8081):
```properties
server.port=8081

```


3. Salve o arquivo e execute o projeto novamente. A aplicação passará a responder em `http://localhost:8081`.

O projeto conta com o **Spring Boot Actuator** para monitoramento local. Com a aplicação rodando, você pode acessar:

👉 [http://localhost:8080/actuator/health](https://www.google.com/search?q=http://localhost:8080/actuator/health)

**Resposta JSON esperada:**

```json
{ 
  "status": "UP" 
}

```

Se preferir rodar o projeto utilizando uma IDE de desenvolvimento:

1. Abra a sua IDE (ex: IntelliJ IDEA).
2. Selecione a opção **Open** ou **Import Project**.
3. Escolha a pasta raiz do projeto (`Projetos-3`) e certifique-se de que a IDE a reconheça como um projeto **Maven**.
4. Aguarde a IDE baixar as dependências descritas no arquivo `pom.xml`.
5. Configure a IDE para usar o **JDK 25** nas configurações do projeto.
6. Localize a classe principal de inicialização (com a anotação `@SpringBootApplication`) e clique no botão de **Run** (Play verde).

---

⬅️ [Voltar para a página inicial do projeto](https://www.google.com/search?q=./README.md)


```
