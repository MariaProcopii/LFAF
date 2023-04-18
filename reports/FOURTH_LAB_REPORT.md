# Topic: Lexer & Scanner

### Course: Formal Languages & Finite Automata
### Author: Procopii Maria

----

## Theory:

Context-free grammars can be written in a particular form called Chomsky Normal Form (CNF).

All grammar production rules in CNF should have one of two forms:
In CNF, all production rules of a grammar should be in one of two forms:
A → BC, where A, B, and C are non-terminal symbols.
A → a, where A is a non-terminal symbol and a is a terminal symbol.

We use Chomsky Normal Form (CNF) for:
Simplification: CNF simplifies the structure of a context-free grammar;
Algorithmic efficiency;
Language recognition;

To modify a Context-free grammar into CNF we need to perform several steps:[2]
1. Eliminate ε productions. A -> ϵ
2. Eliminate any renaming ( Unit-productions ). Production in form of A -> B, where A, B are non-termial.
3. Eliminate inaccessible symbols. Symbol is called inaccessible, if it doesn’t exist S -> α1xα2, namely x doesn't appear in any deviation from the start symbol.
4. Eliminate the non productive symbols. Non-terminal symbols A  VN are called non-productive, if it doesn’t exist A -> y, y ϵ VT*
5. Obtain the Chomsky Normal Form. All production rules of a grammar should be in one of two forms:
A → BC, where A, B, and C are non-terminal symbols.
A → a, where A is a non-terminal symbol and a is a terminal symbol.

## Objectives:
1. Learn about Chomsky Normal Form (CNF) [1].
2. Get familiar with the approaches of normalizing a grammar.
3. Implement a method for normalizing an input grammar by the rules of CNF.
    1. The implementation needs to be encapsulated in a method with an appropriate signature (also ideally in an appropriate class/type).
    2. The implemented functionality needs executed and tested.
    3. A BONUS point will be given for the student who will have unit tests that validate the functionality of the project.
    4. Also, another BONUS point would be given if the student will make the aforementioned function to accept any grammar, not only the one from the student's variant.

## Implementation Description:


## Example:

Input code:

Result:

## Conclusion:

## References:

[1] [Chomsky Normal Form Wiki](https://en.wikipedia.org/wiki/Chomsky_normal_form)
[2] [LFPC Guide](https://else.fcim.utm.md/pluginfile.php/110458/mod_resource/content/0/LFPC_Guide.pdf)
 
