# Topic: Intro to formal languages. Regular grammars. Finite Automata.

### Course: Formal Languages & Finite Automata
### Author: Procopii Maria

----

## Theory
A `language` is a set of strings form some finite, nonempty set of symbols.
It is specified by some rules. Is is designed to be used in situations where a natural language can not be utilized.
`Grammar` is formed from a group of non-terminal symbols, terminal symbols, start symbol and a set of productions (rules).

Grammar can be classified if three types:

- Type 0 Recursively Enumerable Grammar
- Type 1 Context-Sensitive Grammars
- Type 2 Context-Free Grammar
- Type 3 Regular Grammar

Finite automata is a model which reads the string, one symbol per time and we need it to recognize the pattern.
It will accept of reject the input string.
It has a finite number of states.


## Objectives:

1. Understand what a language is and what it needs to have in order to be considered a formal one.

2. Provide the initial setup for the evolving project that you will work on during this semester. I said project because usually at lab works, I encourage/impose students to treat all the labs like stages of development of a whole project. Basically you need to do the following:

   a. Create a local && remote repository of a VCS hosting service (let us all use Github to avoid unnecessary headaches);

   b. Choose a programming language, and my suggestion would be to choose one that supports all the main paradigms;

   c. Create a separate folder where you will be keeping the report. This semester I wish I won't see reports alongside source code files, fingers crossed;

3. According to your variant number (by universal convention it is register ID), get the grammar definition and do the following tasks:

   a. Implement a type/class for your grammar;

   b. Add one function that would generate 5 valid strings from the language expressed by your given grammar;

   c. Implement some functionality that would convert and object of type Grammar to one of type Finite Automaton;

   d. For the Finite Automaton, please add a method that checks if an input string can be obtained via the state transition from it;


## Implementation description

First of all I started by implementing the `Grammar` class wich contains a set of terminal, non-terminal variables, hashMap of roductions and start symbol. To make an instance of the grammar class you should provide a char list of mentioned above elements. Methods `genNonTerminalVariables()` and `genTerminalVariables()` just take the provided char list and add it in a hashSet. Method `genProductions()` makes a hashMap wich contains left side of production ( the key ) and the arraylist of possible right side ellements ( the value ). It checkes if the passed ellement form the list exists in hashMap and add it by key. If it is not pressent, it adds it and make a new arraylist. 

```
 public void genProductions(char[] prodKey, String[] prodVal) {
     for(int i = 0; i < prodKey.length; i++){

         if(!productions.containsKey(prodKey[i])){
             productions.put(prodKey[i], new ArrayList<>());
         }
         productions.get(prodKey[i]).add(prodVal[i]);
         }
 }
```

For generating the words is used the `generateWords()` method. It takes the amount of words we want to create and returns the list of strings created. Also it will print how the strings was created:
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
While the set of results does not contain the amount of strings we need, I create a stack abject in wich plase the start symbol. Next while the stack is not empty ( the string formation is not ready ), I take the symbor from stack, using pop(), and verify if it is a terminal of a non-terminal symbol. If it is not, I use it to get the arraylist from production HashMap ( using it as a key ), randomly select a variant from possible right side ellements and add this symbols in stack in reverse order. If it is terminal,, I just add it in stringbuilder object ( I used the StringBuilder because I need to modify the string and dont want to make a new String in the heap memory ).
```
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
 


* If needed, screenshots.


## Conclusions / Screenshots / Results


## References

1) *Formal Languages and Finite Automata* [online]. [Accessed: 11.02.2023].
Available: https://else.fcim.utm.md/pluginfile.php/110458/mod_resource/content/0/LFPC_Guide.pdf
2) *Introduction to Finite Automaton* [online]. [Accessed: 13.02.2023].
Available: https://www.geeksforgeeks.org/introduction-of-finite-automata/
3) *Plantuml State Diagram* [online]. [Accessed: 14.02.2023].
Available: https://plantuml.com/state-diagram



















