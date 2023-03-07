# Topic: Determinism in Finite Automata. Conversion from NDFA 2 DFA. Chomsky Hierarchy.

### Course: Formal Languages & Finite Automata
### Author: Procopii Maria

----

## Theory

Finite automata is a model which reads the string, one symbol per time, and we need it to recognize the pattern.
It will accept of reject the input string.
It has a finite number of states.

Finite automaton is formed of 5 tuples:
* **Q** is a set of states
* **Σ** is an input alphabet
* **δ** is a set of transitions 
* **q0** is the initial state
* **F** is a set of final states 

Depending on an automaton's structure, there are instances where numerous states can be reached with a single transition, leading to the appearance of nondeterminism. Determinism often refers to how predictable a system is while discussing systems theory. The system becomes stochastic or non-deterministic if random variables are included. Nonetheless, the automata may be categorized as non-/deterministic, and it is possible to achieve determinism by adhering to algorithms that change the automaton's structure.

## Objectives:
1. Understand what an automaton is and what it can be used for.

2. Continuing the work in the same repository and the same project, the following need to be added:
    a. Provide a function in your grammar type/class that could classify the grammar based on Chomsky hierarchy.

    b. For this you can use the variant from the previous lab.

3. According to your variant number (by universal convention it is register ID), get the finite automaton definition and do the following tasks:

    a. Implement conversion of a finite automaton to a regular grammar.

    b. Determine whether your FA is deterministic or non-deterministic.

    c. Implement some functionality that would convert an NDFA to a DFA.
    
    d. Represent the finite automaton graphically (Optional, and can be considered as a __*bonus point*__):
      
    - You can use external libraries, tools or APIs to generate the figures/diagrams.
        
    - Your program needs to gather and send the data about the automaton and the lib/tool/API return the visual representation.

## Implementation Description
First I created a new function in grammar class called `grammarType()`. This method use the Chomsky clasification rules to classify the provided grammar. The main set of rules is:
If the right element is bigger than 2 it mean that the provided grammar cannot be of type 3, so I mark isRegular = false. When the right element is equal to 2, we verify if the simbol from right contains one terminal and one non-terminal symbol. If not it is not of type 3. In the chase when the right word length is one, it should be terminal symbol, if not - isRegular = false. Also if the right element is an empty string, I clasify the grammar as Type 0 and terminate the programm.

```java
public void grammarType(){
        boolean isRegular = true;
        boolean isContextFree = true;

        //Check if production is not of the form A -> BaB or q0 -> q01q0
        for(String key : productions.keySet()){
            if(key.length() > 1 && !nonTerminalVariables.contains(key)){
                isContextFree = false;
                isRegular = false;
            }
        }

        for(ArrayList<String> list : productions.values()){
            for(String element : list){

                if(element.length() == 0){
                    System.out.println("Unrestricted grammar Type 0");
                    return;
                }

                //check the chase when we have non-terminal of the form q1, q0 -> aq1
                boolean isTwoSymbol = nonTerminalVariables.contains(element.substring(1));

                // Check if production is not of the form A -> aB or A -> a
                if(element.length() > 2 && !isTwoSymbol){
                    isRegular = false;
                }

                else if(element.length() == 2 || isTwoSymbol){
                    String first = String.valueOf(element.charAt(0));
                    String second = element.substring(1);
                    if(!terminalVariables.contains(first) || !nonTerminalVariables.contains(second)){
                        isRegular = false;
                    }
                }

                else {
                    String symbol = String.valueOf(element.charAt(0));

                    // Check if production is not of the form A -> a
                    if(!terminalVariables.contains(symbol)){
                        isRegular = false;
                    }
                }
            }
        }

        if(isRegular){
            System.out.println("Regular Type 3");
        }
        else if(isContextFree){
            System.out.println("Context-free Type 2");
        }
        else {
            System.out.println("Context-sensitive Type 1");
        }
    }
}

```
