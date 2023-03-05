# Topic: Intro to formal languages. Regular grammars. Finite Automata.

### Course: Formal Languages & Finite Automata
### Author: Procopii Maria

----

## Theory
A `language` is a set of strings form some finite, nonempty set of symbols.
It is specified by some rules. It is designed to be used in situations where a natural language can not be utilized.
`grammar.Grammar` is formed from a group of non-terminal symbols, terminal symbols, start symbol and a set of productions (rules).

grammar.Grammar can be classified if three types:

- Type 0 Recursively Enumerable grammar.Grammar
- Type 1 Context-Sensitive Grammars
- Type 2 Context-Free grammar.Grammar
- Type 3 Regular grammar.Grammar

Finite automata is a model which reads the string, one symbol per time, and we need it to recognize the pattern.
It will accept of reject the input string.
It has a finite number of states.

Finite automaton is formed of 5 tuples:
* **Q** is a set of states
* **Σ** is an input alphabet
* **δ** is a set of transitions 
* **q0** is the initial state
* **F** is a set of final states 


## Objectives:

1. Understand what a language is and what it needs to have in order to be considered a formal one.

2. Provide the initial setup for the evolving project that you will work on during this semester. I said project because usually at lab works, I encourage/impose students to treat all the labs like stages of development of a whole project. Basically you need to do the following:

   a. Create a local && remote repository of a VCS hosting service (let us all use GitHub to avoid unnecessary headaches);

   b. Choose a programming language, and my suggestion would be to choose one that supports all the main paradigms;

   c. Create a separate folder where you will be keeping the report. This semester I wish I won't see reports alongside source code files, fingers crossed;

3. According to your variant number (by universal convention it is register ID), get the grammar definition and do the following tasks:

   a. Implement a type/class for your grammar;

   b. Add one function that would generate 5 valid strings from the language expressed by your given grammar;

   c. Implement some functionality that would convert and object of type grammar.Grammar to one of type Finite Automaton;

   d. For the Finite Automaton, please add a method that checks if an input string can be obtained via the state transition from it;


## Implementation description

First of all I started by implementing the `grammar.Grammar` class which contains a set of terminal, non-terminal variables, hashMap of productions and start symbol. To make an instance of the grammar class you should provide a char list of mentioned above elements. Methods `genNonTerminalVariables()` and `genTerminalVariables()` just take the provided char list and add it in a hashSet. Method `genProductions()` makes a hashMap which contains left side of production ( the key ) and the arraylist of possible right side elements ( the value ). It checks if the passed element form the list exists in hashMap and add it by key. If it is not present, it adds it and make a new arraylist. 

```java
 public void genProductions(char[] prodKey, String[] prodVal) {
     for(int i = 0; i < prodKey.length; i++){

         if(!productions.containsKey(prodKey[i])){
             productions.put(prodKey[i], new ArrayList<>());
         }
         productions.get(prodKey[i]).add(prodVal[i]);
         }
 }
```

For generating the words is used the `generateWords()` method. It takes the amount of words we want to create and returns the list of strings created. Also, it will print how the strings was created:
```
Process of words formation:

S ---> aB ---> abB ---> abaC ---> ababB ---> ababbB ---> ababbbB ---> ababbbaC ---> ababbbaaS ---> ababbbaaaB ---> ababbbaaaaC ---> ababbbaaaac ---> [ababbbaaaac]
S ---> aB ---> abB ---> abbB ---> abbaC ---> abbabB ---> abbabbB ---> abbabbaC ---> abbabbac ---> [abbabbac]
S ---> aB ---> aaC ---> aabB ---> aabaC ---> aababB ---> aababaC ---> aababaaS ---> aababaaaB ---> aababaaaaC ---> aababaaaabB ---> aababaaaabaC ---> aababaaaabac ---> [aababaaaabac]
S ---> aB ---> abB ---> abbB ---> abbbB ---> abbbaC ---> abbbaaS ---> abbbaaaB ---> abbbaaabB ---> abbbaaabbB ---> abbbaaabbbB ---> abbbaaabbbaC ---> abbbaaabbbaaS ---> abbbaaabbbaaaB ---> abbbaaabbbaaaaC ---> abbbaaabbbaaaabB ---> abbbaaabbbaaaabaC ---> abbbaaabbbaaaabac ---> [abbbaaabbbaaaabac]
S ---> aB ---> aaC ---> aac ---> [aac]

Final set of words: [ababbbaaaac, abbabbac, aababaaaabac, abbbaaabbbaaaabac, aac]
```
This method contains an arraylist with results, an object of random class.
While the set of results does not contain the amount of strings we need, I create a stack object in which place the start symbol. Next while the stack is not empty ( the string formation is not ready ), I take the symbol from stack, using pop(), and verify if it is a terminal of a non-terminal symbol. If it is not, I use it to get the arraylist from production HashMap ( using it as a key ), randomly select a variant from possible right side elements and add this symbols in stack in reverse order. If it is terminal, I just add it in stringBuilder object ( I used the StringBuilder because I need to modify the string and don't want to make a new String in the heap memory ).
```java
 if(nonTerminalVariables.contains(term)){
     ArrayList<String> tempArrayRes = productions.get(term);
     String tempRes = tempArrayRes.get(random.nextInt(tempArrayRes.size()));
     System.out.print(stringBuilder + tempRes + " ---> ");

     for(int i = tempRes.length() - 1; i >= 0; i--){
         stack.add(tempRes.charAt(i));
     }
 }
 else{
     stringBuilder.append(term);
 }
 ```
 
Method `toFiniteAutomaton()` uses fields of `grammar.Grammar` object to make a `FiniteAutomaton`. Detailed description of this method will be presented later.

`Transition` object is used to store the info about transitions (currentState, transitionLabel, nextState ). Also, I override the `toString()` method ( for printing the transitions list ). For example using the production list of my variant (23) I need to obtain the following transitions set:
```
δ (S, a) = {B}
δ (B, b) = {B}
δ (B, a) = {C}
δ (C, b) = {B}
δ (C, a) = {S}
δ (C, c) = {F}
```
![photo_2023-02-17_12-56-09](https://user-images.githubusercontent.com/77497709/219628406-a556aa06-cf79-48ca-b030-0d4243692d75.jpg)

Another production list can be used if needed.
Obtained arrayList of transition can be visualized using the `printTransition()` method from FiniteAutomaton class.

`FiniteAutomaton` abject can be formed by providing the possibleStates ( non-terminal variables ), alphabet ( terminal var ), initial state and final state ( also we add it in possibleStates ). Method `setTransition()` adds the provided `Transition` abject to the transitioned arrayList. I use it in method `toFiniteAutomaton()` from grammar.Grammar class. This method uses the productions hash map to form the `Transition` objects. It uses each key and all the possible right side production. If the ride side element length is smaller than 2, I create `Transition` object with provided key for currentState, first character of right side element as transitionLabel,  and finalState of automaton as nextState. If the element length is 2, nextState will be  the second character of element.

```java
 if(element.length() < 2){
     finiteAutomaton.setTransitions(new Transition(key,element.charAt(0),
                                    finiteAutomaton.getFinalState()));
 }
 else{
     finiteAutomaton.setTransitions(new Transition(key,element.charAt(0),
                                    element.charAt(1)));
 }
```

`wordIsvalid()` is a method that checks if an input string can be obtained via the state transition from FiniteAutomaton. It goes through the input string, character by character and, using the arrayList of `Transition` object, checks if exist a transition with the same current state ( first `currentState` is `initialState` of FiniteAutomaton ) and the same transition label ( the analyzed character ). If exist such transition, it takes the currentState as next state of found transition, mark the valid field as true and repeat the process for the next character. The string is considered a valid one in the case when after it goes through each character, the `valid` field is marked as true and the `currentState` matches `finalState` of automaton.
```java
 for(int i = 0; i < word.length(); i++){
     for(Transition tr : transitions){
         if(tr.getCurrentState() == currentState &&
            tr.getTransitionLabel() == word.charAt(i)){

             currentState = tr.getNextState();
             valid = true;
             break;
         }
         else{
             valid = false;
         }
     }
 }
 ```


## Conclusions
In the end of this laboratory work I would like to mention that after completing this lab assignment, I researched what defines a language and what characteristics a language must possess to be regarded as formal. I was able to make an implementation of grammar.Grammar and Finite Automaton classes which include the necessary methods to produce acceptable strings, convert an object of type grammar.Grammar to one of type Finite Automaton, and determine if an input string can be obtained via the state transition.

After running the program we have the following results:
```
Process of strings formation:

S ---> aB ---> aaC ---> aabB ---> aabaC ---> aabaaS ---> aabaaaB ---> aabaaaaC ---> aabaaaac ---> [aabaaaac]
S ---> aB ---> aaC ---> aabB ---> aabaC ---> aababB ---> aababbB ---> aababbbB ---> aababbbaC ---> aababbbabB ---> aababbbabbB ---> aababbbabbaC ---> aababbbabbabB ---> aababbbabbabaC ---> aababbbabbabaaS ---> aababbbabbabaaaB ---> aababbbabbabaaaaC ---> aababbbabbabaaaabB ---> aababbbabbabaaaabbB ---> aababbbabbabaaaabbbB ---> aababbbabbabaaaabbbaC ---> aababbbabbabaaaabbbabB ---> aababbbabbabaaaabbbabaC ---> aababbbabbabaaaabbbababB ---> aababbbabbabaaaabbbababbB ---> aababbbabbabaaaabbbababbbB ---> aababbbabbabaaaabbbababbbaC ---> aababbbabbabaaaabbbababbbac ---> [aababbbabbabaaaabbbababbbac]
S ---> aB ---> aaC ---> aac ---> [aac]
S ---> aB ---> aaC ---> aac ---> [aac]
S ---> aB ---> aaC ---> aabB ---> aabaC ---> aababB ---> aababaC ---> aabababB ---> aabababaC ---> aabababaaS ---> aabababaaaB ---> aabababaaaaC ---> aabababaaaabB ---> aabababaaaabaC ---> aabababaaaabac ---> [aabababaaaabac]

Final set of strings: [aabaaaac, aababbbabbabaaaabbbababbbac, aac, aac, aabababaaaabac]

Word [abababaaaac] is valid

Word [abababaaaa] is not valid
```

## References

1) *Formal Languages and Finite Automata* [online]. [Accessed: 11.02.2023].
Available: https://else.fcim.utm.md/pluginfile.php/110458/mod_resource/content/0/LFPC_Guide.pdf
2) Homenda Pedrycz *Automata Theory and Formal Languages* [pdf]. [Accessed: 11.02.2023].
Available:
[Homenda Pedrycz Automata Theory.pdf](https://github.com/MariaProcopii/LFAF/files/10757468/Homenda.Pedrycz.Automata.Theory.pdf)
3) *Introduction to Finite Automaton* [online]. [Accessed: 13.02.2023].
Available: https://www.geeksforgeeks.org/introduction-of-finite-automata/
4) *Plantuml State Diagram* [online]. [Accessed: 14.02.2023].
Available: https://plantuml.com/state-diagram



















