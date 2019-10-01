# Compiler

TP da disciplina Compiladores - CEFET/MG

Alunos:
- [Gabriel Dutra](https://gabrieldutra.github.io/)
- [Marcelo Cândido](https://github.com/MarceloFCandido)
- [Rubio Torres](https://github.com/rubiotorres)

## Objetivo do trabalho
O	objetivo	desse	trabalho	é	desenvolver	um	interpretador	para	uma	nova
linguagem	 de	 programação.

## Exemplo de código .soo

```JavaScript
/* iniciar o array com nenhum elemento e tamanho 0. */
array.size = 0;

/* adicionar um novo elemento ao arranjo. */
array.add = function {
  system.set(self, ("element" + self.size), args.arg1);
  self.size = self.size + 1;
};

/* obter um elemento através de seu índice. */
array.get = function {
  return system.get(self, ("element" + args.arg1));
};

/* imprimir o arranjo. */
array.print = function {
  system.print("[ ");
  i = 0;
  while (i < self.size) {
    system.print(system.get(self, ("element" + i)) + " ");
    i = i + 1;
  }
  system.println("]");
};

/* entrar c/ numeros no arranjo. */
n = system.read("Entre c/ a qtd de numeros: ");
while (array.size < n) {
  x = system.random(0, 9);
  array.add(x);
}

/* criar uma copia do arranjo. */
array2 = system.clone(array);

/* adicionar mais um numero aleatorio a ele. */
y = system.random(0, 9);
array2.add(y);

/* imprimir o arranjo original. */
array.print();

/* mostrar qual elemento foi adicionado. */
system.println(y);

/* adicionar o método ordenar no novo arranjo utilizando o metodo bolha. */
array2.sort = function {
  a = 0;
  while (a < self.size) {
    b = a + 1;
    while (b < self.size) {
      x = system.get(self, ("element" + a));
      y = system.get(self, ("element" + b));
      if (y < x) {
        system.set(self, ("element" + a), y);
        system.set(self, ("element" + b), x);
      }
      b = b + 1;
    }
    a = a + 1;
  }
};

/* ordenar o novo arranjo e depois imprimir. */
array2.sort();
array2.print();

```

## O Interpretador
Foi desenvolvido um interpretador na linguagem **Java**, recomenda-se o uso de uma IDE (NetBeans ou IntelliJ preferencialmente) para fazer a compilação e execução. O arquivo `.soo` a ser compilado é passado como argumento na execução.
```
java sooi exemplo.soo
```
