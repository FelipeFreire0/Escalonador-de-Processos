# Escalonador-de-Processos
Implementar um escalonador de tarefas para Time Sharing em uma m´aquina fict´ıcia com um ´unico processador, criando assim um sistema simples de multiprograma¸c˜ao. 
Essa m´aquina (fict´ıcia) foi criada especificamente para rodar pequenos programas, em que
cada processo pode contar, no m´aximo, com 2 registradores de uso geral (al´em do Contador
de Programa, como registrador de uso espec´ıfico). Esses registradores s˜ao conhecidos internamente como X e Y. Al´em disso, o processador para o qual vocˆes ir˜ao construir o escalonador
´e extremamente simples, possuindo apenas 4 instru¸c˜oes:
1. Atribui¸c˜ao: na forma X=<valor> ou Y=<valor>, onde <valor> ´e um n´umero inteiro e
X e Y s˜ao os registradores de uso geral usados pelo processo (note a ausˆencia de espa¸co
antes e depois do ‘=’).
2. Entrada e sa´ıda: representada pela instru¸c˜ao E/S (que faz as vezes de uma chamada ao
sistema)
3. Comando: a tarefa executada pela m´aquina, representada pela instru¸c˜ao COM
4. Fim de programa: chamada com a ´unica finalidade de remover o programa da mem´oria,
executando a limpeza final. Representada pela instru¸c˜ao SAIDA
