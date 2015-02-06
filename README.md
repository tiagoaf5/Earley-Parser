Earley Parser
=============


Members
-------
**GROUP:** Turma 1 Grupo 4

**NAME1:** Ana Sousa, NR1: 201108026, GRADE1: 20, CONTRIBUTION: 25%  
**NAME2:** Augusto Silva, NR2: 201104201, GRADE2: 20, CONTRIBUTION: 25%  
**NAME3:** Diogo Nunes, NR3: 201108019, GRADE3: 20, CONTRIBUTION: 25%  
**NAME4:** Tiago Fernandes, NR4: 201106911, GRADE4: 20, CONTRIBUTION: 25%  

SUMMARY
-------

Ferramenta capaz de implementar um parser que utilize o Algoritmo de Earley em Java. A ferramenta tem como inputs uma lista de tokens e a gramática. A gramática sofre uma “meta-análise”.

OUR GRAMMAR:
------------

**As regras da gramática seguem o seguinte formato:**

    Produção ::= Body

Do lado da produção apenas existe um elemento enquanto que do lado do Body podem existir vários separados por espaço. 

**Temos suporte para:**
 

 - **|**  : Ou (Ex: A ::= “a” | “A”     - permite “a” ou “A”)

 - **( )*** : Zero ou mais ocorrências (Ex: A ::= (“1”)* “2”     - tanto permite “1 2” como “2” como “1 1 1 2”, etc)
 - **( )+** : Uma ou mais ocorrências (Ex: A ::= (“1”)+     - permite um ou mais uns)
 - **( )?** : Zero ou uma ocorrência (Ex: A ::= (“Olá”)? “tudo bem ?”     - permite “Olá tudo bem ?” ou “tudo bem ?”

DEALING WITH LEXICAL,SYNTACTIC AND SEMANTIC ERRORS
---------------------------------------------------

Os erros lexicais, sintáticos e semânticos da grámatica e os erros lexicais e sintáticos do input abortam a execução do algoritmo.
Sendo que na caixa do log da interface gráfica é possível ver a linha do erro bem como uma breve explicação do mesmo.

SEMANTIC ANALYSIS
------------------

A análise semântica da gramática permite que produções possam conter números ou letras sendo que o primeiro elemento tem de ser uma letra; os terminais têm de estar entre aspas.

VISUALIZATION
--------------

A ferramenta mostra a árvore sintáctica de forma gráfica.

OVERVIEW
--------

Parser para a gramática (incluindo análise lexical, sintática e semântica) e para a sequência de tokens resultando em representações intermédias recebidas pelo algoritmo de Earley.

 - **Tokens:** lê ficheiro em que os tokens estão separados por espaços e
   faz o parser da seguinte forma: cada linha é transformada numa lista
   de strings em que cada string corresponde a um token.
 - **Gramática:** lê ficheiro no qual, por linha, tem uma regra no formato:
   NaoTerminal ::= Corpo. Terminais estão entre aspas, tudo que não
   esteja entre aspas é encarado como um não terminal, isto é, uma
   produção. As produções por sua vez podem conter letras e números, no
   entanto têm de começar por letras. O algoritmo recebe a gramática na
   forma de uma hash sendo a chave o lado esquerdo da produção e o valor
   um vector com o lado direito da produção (cada elemento do vector é
   uma das possibilidades - separador “|”).
 - **Algoritmo:** 
   
   Earley recognizer 3 passos principais
   	- Predictor - expande a gramática (não terminais)
   	- Scanner - faz match com a string de entrada (terminais)
   	- Completer - parte de regras já processadas (B -> x) para avançar nas regras anteriores (A -> Bz)
   
   A cada palavra associa-se uma série de estados à espera (A->a@b,i), 
   @ indica a posição na regra da gramática e i a posição na frase, que
   vão ser processados por um dos passos. Os 3 passos referidos criam
   novos estados para a palavra atual ou para a seguinte.
   
   O input é aceite se no último token chegamos à produção inicial toda
   processada Exemplo: (_ROOT->P@,2) caso sejam 2 palavras
   
   Earley parser - A passagem de um reconhecedor para um parser faz-se
   pela extracção da árvore sintática concreta. Durante o passo
   Completer, onde se faz a junção de 2 regras, conseguimos estabelecer
   a relação de pai-filho e assim construir a árvore ligando os nós. No
   final, parte-se do estado _ROOT->P@ e vai-se recursivamente obtendo
   os filhos até chegar aos terminais.

TESTSUITE AND TEST INFRASTRUCTURE
------------------------------------

O conjunto de testes implementados em JUNIT permite verificar o parse correcto para diversas gramáticas diferentes como: recursividade à esquerda, à direita, produções nulas, uso de “?”, “+”, “*”, etc

TASK DISTRIBUTION
-----------------

**Ana Sousa:** input  e representação intermédia da sequência de tokens; interface gráfica; testes unitários  
**Augusto Silva:** implementação do algoritmo de earley; criação e preenchimento da estrutura interna da árvore sintática; testes unitários  
**Tiago Fernandes:** input, análise lexical, sintáctica e semântica e representação intermédia da gramática; testes unitários  
**Diogo Nunes:** input, leitura e análise lexical da gramática; visualização gráfica da árvore sintáctica  

PROS & CONS
-----
**Pros**

Implementação de um algoritmo que consegue lidar com a maior parte das grámaticas mesmo que ambíguas e construir uma árvore sintáctica. O acesso é feito por uma interface gráfica de fácil utilização e mostramos a árvore graficamente o que facilita a leitura.

**CONS**

A implementação efectuada obriga a que cada token de input seja separado por espaços
