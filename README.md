<h1 align="center">
   PaymentAPI - API para realizar pagamentos.
</h1>


  <a href="https://www.linkedin.com/in/danielobara/">
    <img alt="Made by Elismar13" src="https://img.shields.io/badge/made%20by-Elismar13-%2304D361">
  </a>

<p>
<a aria-label="In progress" href="">
  <img alt="License" src="https://img.shields.io/badge/license-MIT-brightgreen">
</p>

<p align="center">
  <a href="#-projeto">Project</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#rocket-tecnologias">Technologies</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#-layout">Layout</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#-como-contribuir">How to contribute</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#memo-license">License</a>
</p>

## 💻 Projeto

PaymentAPI é uma interface de programação que tem como principal objetivo simplificar e gerenciar o processo de pagemento com carteiras digitais.

## :rocket: Tecnologias

Este projeto foi desenvolvido com estas tecnologias:

- [Java 11](https://openjdk.org/projects/jdk/11/)
- [PostgreSQL](https://www.postgresql.org/) - sistema gerenciador de banco de dados relacional, que está em constante evolução, é consolidado e presente em diversas aplicações no mercado.
- [Spring Framework](https://spring.io/) - Spring é um framework de desenvolvimento de aplicações Java corporativo mais popular para do mercado. Milhões de desenvolvedores em todo o mundo usam o Spring Framework para criar código de alto desempenho, facilmente testável e reutilizável. O Spring também oferece várias ferramentas para facilitar o processo de desenvolvimento, desde utensílios para injeção de dependência até submódulos para controle de sessão, repositórios, segurança, etc. Por ser de código aberto, também podemos encontrar mais instrumentos criados e integrados pela comunidade.
- [Moneta](https://javamoney.github.io/ri.html) - Uma interface de programação para realizar operações com valores monetários, fornecendo uma camada de proteção com estas operações que sofrem com a suscetividade à valores finitos do computador. Ao utilizarmos, podemos realizar operações entre com capitais financeiros, desde operações simples como adição e substração até o cálculo de juros.
- [Flyway](https://flywaydb.org) - Ferramenta de versionamento de banco de dados. Utilizado para manter a consistência da aplicação cada vez que a mesma for reiniciada.
- [Mockito](https://site.mockito.org) - Biblioteca que fornece uma interface para simular respostas de diferentes domínios da aplicação. Usado nos testes unitários e de integração.
- [Hamcrest](http://hamcrest.org) - Biblioteca que fornece interfaces de testes unitários aprimoradas, com a possibilidade de integração com o Junit. Oferece um novo meio de realizar testes unitários, podendo realizar queries complexas que garamtem a integridade da aplicaçãa.
- [Junit](https://junit.org/junit5/) - Biblioteca de testes consolidada no Java. Usado para escrita de testes unitários. 
- [Lombok](https://github.com/yaronn/blessed-contrib) - Biblioteca que oferece várias interfaces para realizar o tratamento de campos dos mais diversos tipos, tamanhos, possíveis valores, dentre outras validações. Usada para garantir a intergridade e segurança dos dados da aplicação.
- [Docker]() - Ferramenta de conteinerização de aplicações. Muito utilizada na indústria, pois possibilita a automatizado do processo de build da aplicação dentre outras vantagens. 

## :worker: Requisitos

- ### 🐧 :wind_chime: Linux ou Windows (Validado apenas no Linux) 
  - Java 11
  - Apache Maven
  - Docker
  - Docker-Compose: Para instalar, siga o [tutorial](https://docs.docker.com/engine/install/) oficial para sua plataforma.

## :wrench: Como realizar a instalação

- Após os requisitos forem devidamente instalado, basta clonar o repositório:
    ```console
  foo@bar:~$ git clone https://github.com/Elismar13/PaymentAPI.git
  ```
- Entre na pasta raíz do projeto:
  ```console
   foo@bar:~$ cd PaymentAPI
  ```
- Em seguida rode o seguinte comando para gerar o .jar da aplicação:
  ```console
  foo@bar:~$ mvn spring-boot:run
  ```
- Por fim, basta executar o docker-compose para configurar os containers necessários para executar a aplicação, além de fazer o deploy local da aplicação na sua máquina.
  ```console
  foo@bar:~$ docker-compose up
  ```

## :memo: License

This project is under the MIT license. See the [LICENSE](LICENSE.md) for details.

---

Feito com amor ♥ por Elismar Silva Pereira :wave: [Vamos nos conectar!](https://www.linkedin.com/in/elismar-silva-644272191/)
