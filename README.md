#  Gerador Automático de Classes - Versão Java

##  Sobre o Projeto
Este projeto é um utilitário de desktop desenvolvido em **Java Swing** focado em automatizar a criação de classes. A ideia original surgiu de um desafio proposto em sala de aula (originalmente para ser feito em C#), mas decidi ir além e fazer engenharia reversa da minha própria solução para recriá-la no ecossistema Java.

Como meus estudos atuais estão fortemente direcionados para o **desenvolvimento Backend com Java**, recriar essa ferramenta foi um excelente exercício de fixação de sintaxe, manipulação de strings e construção de interfaces gráficas nativas com Swing.

##  Funcionalidades
* Interface gráfica intuitiva para entrada de dados.
* Adição dinâmica de propriedades (atributos) a uma lista em memória.
* Validações de regras de negócio (ex: impedir propriedades duplicadas, exigir nome de classe válido).
* **Geração de Código:** Cria automaticamente a estrutura completa de uma classe Java, incluindo:
    * Atributos privados.
    * Construtor padrão e construtor com parâmetros.
    * Métodos `Get` e `Set` (com validações de string vazia embutidas).
    * Sobrescrita do método `@Override public String toString()`.
* Visualização do código gerado com botão de "Copiar para a Área de Transferência".

##  Tecnologias Utilizadas
* **Linguagem:** Java (JDK 8+)
* **Interface Gráfica:** Java Swing (Bibliotecas nativas `javax.swing.*` e `java.awt.*`)
* **Paradigma:** Orientação a Objetos

## Como Executar
1. Certifique-se de ter o Java (JDK) instalado em sua máquina.
2. Clone este repositório:
   ```bash
   git clone [https://github.com/leozinlsb/GeradorDeClasses-Java.git](https://github.com/leozinlsb/GeradorDeClasses-Java.git)
