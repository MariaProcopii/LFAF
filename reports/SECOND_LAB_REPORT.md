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
Next step was to convert a finite automaton to grammar. For that I've created a method in FiniteAutomaton class, called `toGrammar()`. Here, to form the production for grammar I need to create two list - `proKey`, `prodVal`. For that I pass through transitions set and form 2 arrays with key list and value list. Value list will be formed by concatinating the transition label and next state from transition abject. In the end it returns the formed grammar. 

```java
public Grammar toGrammar(){

    ArrayList<String> prodKey = new ArrayList<>();
    ArrayList<String> prodVal = new ArrayList<>();
    for(Transition tr: transitions){
        prodKey.add(tr.getCurrentState());
        prodVal.add(tr.getTransitionLabel() + tr.getNextState());
    }

    String[] pk = prodKey.toArray(new String[0]);
    String[] pv = prodVal.toArray(new String[0]);
    String[] ps = possibleStates.toArray(new String[0]);
    String[] alph = alphabet.toArray(new String[0]);

    return new Grammar(ps, alph, pk, pv, initialState);
}
```
To verify if FA is deterministic or non-deterministic, in the method `isNFA()` I create the grammar from the finite automaton and check the values of production from that grammar. If the size of a list of possible state transitions is bigger than the alphabet lenght - the automaton is non-deterministic. For example we have this part of production: `q0 = [aq0, bq0, aq1]`. This list, for q0 has two option with the same label `"a"`, transition in `q0` and `q1`. So it makes this automaton non-deterministic.

```java
public void isNFA(){
    Grammar grammar = this.toGrammar();
    HashMap<String, ArrayList<String>> production = grammar.getProductions();

    for(ArrayList<String> states : production.values()){
        if(states.size() > alphabet.size()){
            System.out.println("Non-deterministic FA");
            return;
        }
    }
    System.out.println("Deterministic FA");
}
```
Final task was to implement some functionality that would convert an NDFA to a DFA. My variant was 23
```
Variant 23
Q = {q0,q1,q2},
∑ = {a,b},
F = {q2},
δ(q0,a) = q0,
δ(q0,a) = q1,
δ(q1,b) = q2,
δ(q0,b) = q0,
δ(q2,b) = q2,
δ(q1,a) = q0
```
For a beter understanding I created the graphical represintation of this NFA:

![image](https://user-images.githubusercontent.com/77497709/223445239-3e015790-4161-4452-b738-9e870caa4e3a.png)

To convert this NFA to DFA, we need to get rid of different transition option with the same label for the same state ( ex q0 can make a transition in q0 and q1 using "a" ).
In the end we need to obtain this DFA:

![image](https://user-images.githubusercontent.com/77497709/223445971-2da0525f-bb6f-4d0f-a52d-40469cf19136.png)

First I create the `grammar` from the given automaton, a `stack` were I will store the states which I want to analyze, array list of already analized states which will not be included in stack. Next I add the `initialState` as the first state in `stack` and while stack is not empty I extract the element from the stack and call it `term`. `prodList` will contain list of transition label and possible from that transition state. As I transform NFA to DFA I will have combined states like q1q2, so before inserting elements in `prodList` I need to verify if the state from stack is in `possibleStates` - if not it is a combined one. In that chase I use regex to extract from the provided string ( ex q1q2 ) all the states, then, for the found states, extract all the possible transition from the grammar and add them in a set named `unique` which will be a reunion between found terms tranitions. Also I need to sort the array to not have in the future states like ( q0q1 q1q0 ) which will be considered as different one but in the fact be the same thing.

After forming the `prodList`, I pass through each element, extract the label and transition and add them in the set `states`. Now I obtained from `[aq0, aq1, bq2] ---> {a=[q0, q1], b=[q2]}` for the state which we analyzed at the moment. I do that to find out if the state which we analyzed has dual choise transition. In the given exampe we can reach `q1 and q2` going through `"a"`. So our to get rid of that, I form a new state `q0q1`, form a new `transition` object, passing currently analyzed term as `currentState`, label as `transitionLabel`, new state as `nextState` and add it to `newTransitions` array. Next I add this state to the stack if it wasn't analized and in the end I change the old arrayList of `transitions` to new `newTransitions`.

```java
public void convertToDFA(){
    ArrayList<Transition> newTransitions = new ArrayList<>();
    Grammar grammar = this.toGrammar();
    Stack<String> stack = new Stack<>();
    ArrayList<String> analyzed = new ArrayList<>(); //already included in states hashMap
    stack.add(initialState);

    while (!stack.empty()){
        HashMap<String, ArrayList<String>> states = new HashMap<>();
        String term = stack.pop();
        analyzed.add(term);
        ArrayList<String> prodList = new ArrayList<>();

        if(possibleStates.contains(term)){ //state formed just from one term. Ex: q1
            prodList = grammar.getProductions().get(term); //list of transition label and possible from that transition state
        }
        else {
            HashSet<String> unique = new HashSet<>();
            String pattern = "q\\d+"; //  extract strings that match the pattern "q" followed by one or more digits
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(term);

            while (m.find()) {
                String match = m.group();
                unique.addAll(grammar.getProductions().get(match));  //reunion between found terms
            }
            prodList.addAll(unique);
            Collections.sort(prodList);
        }
        for(String element : prodList){
            String trLabel = element.substring(0, 1);
            String transition = element.substring(1); // now we can use q0 or A for representation of non-terminals

            if(!states.containsKey(trLabel)){
            states.put(trLabel, new ArrayList<>());
            }
            states.get(trLabel).add(transition);
        }

        for(String label : alphabet){
            String newNextState;
            if(!states.isEmpty()){
                newNextState = String.join("", states.get(label)); //form new state trans
            }
            else{
                newNextState = "empty";
            }
            newTransitions.add(new Transition(term, label , newNextState));

            if(!analyzed.contains(newNextState) && !newNextState.equals("empty")){ //add state to stack if it wasn't analyzed yet
                stack.add(newNextState);
                analyzed.add(newNextState);
            }
        }
    }
    transitions = newTransitions;
}
```

##Conclusion

After completing this laboratory work, I researched the Chomsky Hierarchy of Grammar Types. I also comprehended the distinction between a deterministic and nondeterministic FA and created the function that does so. I created a function to create the graphical representation of the Finite Automaton as well as the method to convert the NFA to the DFA.

The following outcomes are obtained after executing the project:
```

Production list of converted grammar: {q1=[bq2, aq0], q2=[bq2], q0=[aq0, bq0, aq1]}
Grammar type: Regular Type 3

-----Before conversion-----
FA type: Non-deterministic FA

-----After conversion-----
FA type: Deterministic FA
```

## References 

1. https://github.com/DrVasile/FLFA-Labs
2. https://github.com/DrVasile/FLFA-Labs-Examples
3. https://else.fcim.utm.md/pluginfile.php/110458/mod_resource/content/0/LFPC_Guide.pdf
4. https://www.tutorialspoint.com/automata_theory/chomsky_classification_of_grammars.htm
