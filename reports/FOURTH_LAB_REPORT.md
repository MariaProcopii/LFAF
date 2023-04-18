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

To modify a Context-free grammar into CNF we need to perform several steps:[2] and [3]
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
First I created a new class named `ToCNF` which contains implementation of all steps needed to convert the grammar in CNF. This class can be used to convert any grammar, not only the one from my variant (variant 23). To create utility class that contain general-purpose methods, I made all methods private and static. Also this enforces encapsulation since they can only be called from within the class in which they are defined. Constructor is also private for not being able to create an instance of the class `ToCNF`. Methods `getCopyModGrammar` and `modifyGrammar`are the only one which are public. They receives the grammar and apply all the methos which converts this grammar to CNF.
`getCopyModGrammar` - can be used to work with a copy for the given grammar;
`modifyGrammar` - can be used to work directly with the given grammar ( and modify it );
In my `MainFLab3.java` I use `getCopyModGrammar` to be able to dysplay in the end the initial grammar and the modified one.
First I create the `grammar` using the terminal, non-terminal symbols, starting point and productions from the given variant (23).
![image](https://user-images.githubusercontent.com/77497709/232716284-1973bcdb-e33a-4ddc-a0d3-c617227498ce.png)
Obtain:
```
Non-terminal symbols: [A, B, S, C, E]
Terminal symbols: [a, b]
Starting symbol: S
Productions: {A=[a, aS, bCaCb], B=[AC, bS, aAa], S=[bAC, B], C=[, AB], E=[BA]}
```
Next I use `getCopyModGrammar` method described above to create a copy of the given grammar and modify it to CNF.
```java
Grammar grammar1 = ToCNF.getCopyModGrammar(grammar);
```
This method performs the modification by calling several functions:
```java
public static Grammar getCopyModGrammar(Grammar gr){
    grammar = new Grammar(gr);
    rmEmptyProd();
    rmUnitProd();
    rmInaccessibleProd();
    //rmNonProdSymbols() is used in rmInaccessibleProd()
    modProductoinCNF();
    return grammar;
}
```

`rmEmptyProd()` - Eliminates ε productions.
`rmUnitProd()` - Eliminates any renaming ( Unit-productions ).
`rmInaccessibleProd()` - Eliminates inaccessible productions.
//rmNonProdSymbols() - Eliminates the non productive symbols and is in rmInaccessibleProd()
`modProductoinCNF()` - Modifies the productions using Chomsky Normal Form standards.

### 1.rmEmptyProd()
The method `rmEmptyProd()` is the main method that is responsible for removing empty productions. 
It works by first identifying all the empty productions and then removing them from the list of productions. 
It then replaces all occurrences of empty productions in the remaining productions with all possible combinations 
of non-empty productions.
```java
for(String emptyP: emptyProd){
   for(String prodLeft : grammar.getProductions().keySet()){
       List<String> newProductions = new ArrayList<>(); //list with new production with all combination of found occurrences
       ArrayList<String> prodList = grammar.getProductions().get(prodLeft);
       for (int i = 0; i < prodList.size(); i++){
           String s = prodList.get(i);
           if(s.contains(emptyP)){
               prodList.remove(s);
               newProductions.addAll(newProdList(s, emptyP));
               i--;
           }
       }
       prodList.addAll(newProductions);
   }
}
```

The method `newProdList()` is used to generate a list of new productions with all possible combinations of non-empty
productions. It takes two parameters - the original production and the empty production that needs to be removed. 
It first identifies all occurrences of the empty production in the original production and then generates all possible 
combinations of non-empty productions that can replace these occurrences. It then returns a list of all these new 
productions.

The method `generateCombinations()` is a helper method that is used to generate all possible combinations of occurrences 
of the empty production in the original production. It uses a backtracking algorithm to generate these combinations.
```java
 private static List<List<Integer>> generateCombinations(ArrayList<Integer> nums) {
     List<List<Integer>> result = new ArrayList<>();
     backtrack(result, new ArrayList<>(), nums, 0);
     return result;
 }
```

The method `backtrack()` is a recursive method that is used by the `generateCombinations()` method to generate 
the combinations. It takes four parameters - a list of all the combinations that have been generated so far, 
a temporary list that is used to store the current combination being generated, a list of all the occurrences of 
the empty production in the original production, and a start index that is used to keep track of the position in the 
list of occurrences.
```java
for (int i = start; i < nums.size(); i++) {
            tempList.add(nums.get(i));
            backtrack(result, tempList, nums, i + 1);
            tempList.remove(tempList.size() - 1);
        }
```

The method `removeCharsAtPositions()` is a helper method that is used to remove the empty production from a 
given position in the original production. It takes two parameters - the original production and a list of positions 
where the empty production occurs. It then removes the empty production from each of these positions and returns 
the resulting string.

### 2.rmUnitProd()
This method removes all unit productions from a given context-free grammar.
The method first loops through all keys (non-terminal symbols) in the grammar's production map.
It then retrieves the list of productions corresponding to each key and loops through them.
For each production, it checks if its length is equal to 1 and its only symbol is an uppercase letter 
(indicating a non-terminal symbol). If both conditions are met, it removes the production and adds the 
set of productions corresponding to the deleted non-terminal symbol to the list of productions. 
This is done by retrieving the list of productions corresponding to the non-terminal symbol and adding 
them to the list of productions for the current key. This process continues until there are no more unit productions 
left in the grammar.
```java
 if(prod.length() == 1 && Character.isUpperCase(prod.charAt(0))){ //chase when we have unit A -> B
     prodList.remove(prod); // remove the found production Ex: B
     prodList.addAll(grammar.getProductions().get(prod)); // add the set of productions corresponding to deleted production B -> ...
 }
```

### 3.rmInaccessibleProd() and 4.rmNonProdSymbols()
The `rmInaccessibleProd()` method first identifies all productions that can be reached from the starting symbol 
and removes all non-reachable productions. It does this by first initializing a list `prodFromStartS` with the 
starting symbol and a stack `verifiedProd` with the same symbol. It then iteratively pops symbols from `verifiedProd`, 
retrieves their productions from the grammar object, and checks each symbol in the production. If the symbol 
is an uppercase non-terminal symbol and has not already been added to `prodFromStartS`, it is added to prodFromStartS 
and `verifiedProd`. If the symbol is a non-productive symbol, the corresponding production is removed.
```java
  for (String symbol : grammar.getNonTerminalVariables()) { //remove inaccessible productions
      if (!prodFromStartS.contains(symbol)) {
          grammar.getNonTerminalVariables().remove(symbol);
          grammar.getProductions().remove(symbol);
      }
  }
```

The `rmNonProdSymbols()` method is called from `rmInaccessibleProd()` to determine if a given symbol is non-productive. 
```java
boolean isNonProd = rmNonProdSymbols(leftProd, prod, prodList);

```
It does this by checking if the symbol is not present in any production in the grammar object. If it is not present, 
the corresponding production is removed, and the method returns true. If it is present, the method checks if the 
production can be terminated by checking if it has a terminal symbol or if it can be derived into another production. 
If it cannot be terminated, the corresponding production is removed, and the method returns true. If the symbol is 
productive, the method returns false.

### 5.modProductoinCNF()
The `modProductoinCNF()` first creates a HashMap called `terminalNewProd` to store the new productions that will be 
created to replace the terminal symbols. It iterates over the set of terminal variables in the input grammar and 
creates a new production for each terminal variable. The new production has a new left-hand side that is generated by 
calling the `newLP()` method, and the right-hand side consists of a single occurrence of the original terminal variable. 
The original terminal variable is then replaced with the new left-hand side in the `terminalNewProd` HashMap.
```java
...
  for(String vn : grammar.getTerminalVariables()){
      String key = newLP();
      grammar.getProductions().put(key, new ArrayList<>(Arrays.asList(vn)));
      terminalNewProd.put(vn, key);
  }
...
```

Next, the method iterates over each production in the input grammar and each occurrence of each terminal variable in the 
right-hand side of the production is replaced with the corresponding new left-hand side created in the previous step. 
This is done using a `StringBuilder` to create a new right-hand side for the production. The method then replaces the 
old right-hand side with the new one. After this substitution, the method calls the `groudProd()` method to modify the 
production to satisfy the requirements of CNF.
```java
for (int i = 0; i < prodList.size(); i++) {
    StringBuilder newRightProd = new StringBuilder();
    newRightProd.append(prodList.get(i));

    for(String vt : grammar.getTerminalVariables()){
        int index = newRightProd.indexOf(vt);

        if(newRightProd.length() > 1){ //don't replace single terminal symbols Ex: S -> a
            while(index >= 0) {
                newRightProd.replace(index, index + 1, terminalNewProd.get(vt));
                index = newRightProd.indexOf(vt);
            }
        }
    }
    prodList.set(i, newRightProd.toString());
}
```

The `groudProd()` method takes a String prod as input and returns a String representing a modified version of prod. 
The method is responsible for ensuring that the production satisfies the requirement of CNF that each production has 
either two non-terminal symbols or one terminal symbol. To achieve this, the method creates groups of two adjacent 
symbols in the right-hand side of the production until there are only two symbols left. If a group of two symbols has 
been seen before, it is replaced with the left-hand side of the new production that was created for that group.
Otherwise, a new left-hand side is generated by calling the `newLP()` method and a new production is created with the 
group of two symbols as the right-hand side. 
```java
String newG = newGroup.toString();
if(newProdLookUp.keySet().contains(newG)){
    prod = prod.replace(newG, newProdLookUp.get(newG));
}
else{
    String newLP = newLP();
    newProductions.put(newLP, new ArrayList<>(Arrays.asList(newG)));
    newProdLookUp.put(newG, newLP);
    grammar.getNonTerminalVariables().add(newLP);
    prod = prod.replace(newG, newLP);
}
```
The new left-hand side and the new production are added to the 
`newProductions` HashMap, and the old group of two symbols is replaced with the new left-hand side in the `newProdLookUp`
HashMap. Finally, the method replaces the old group of two symbols with the new left-hand side in the right-hand 
side of the production.

Once the `groudProd()` method has modified the production to satisfy the CNF requirement, the method returns the 
modified production. The `replaceAll()` method is called on the list of productions for each left-hand side to 
replace each production with its modified version created by the `groudProd()` method.
```java
prodList.replaceAll(ToCNF::groudProd);
```

At the end of the `modProductoinCNF()` method, the `newProductions` HashMap is added to the input grammar's productions.

## Example:
The following outcomes are obtained after executing the project:
```java
Old Grammar: 
{A=[a, aS, bCaCb], B=[AC, bS, aAa], S=[bAC, B], C=[, AB], E=[BA]}
        
Normalized Grammar: 
{ X8=[X1A], 
  A=[a, X0S, X3X1, X2X1, X5X1, X6X1], 
  B=[X1S, X7X0, AC, a, X0S, X3X1, X2X1, X5X1, X6X1], 
  C=[AB], S=[X1A, X8C, X1S, X7X0, AC, a, X0S, X3X1, X2X1, X5X1, X6X1], 
  X0=[a], 
  X1=[b], 
  X2=[X1X0], 
  X3=[X2C], 
  X4=[X1C], 
  X5=[X4X0], 
  X6=[X5C], 
  X7=[X0A]}

```

## Conclusion:
A context-free grammar can be written in standardized Chomsky Normal Form (CNF), which comprises a set 
of production rules. According to these rules, each production rule must take one of two forms, either 
A BC or A a, where A, B, and C are non-terminal symbols and an is a terminal sign. CNF is used to make 
context-free grammars simpler, increase algorithmic effectiveness, and recognize languages. A context-free 
grammar must be transformed into a CNF by removing epsilon productions, unit productions, inaccessible symbols, 
non-productive symbols, and acquiring the CNF, among other things. These procedures may be used to transform a 
context-free grammar to a CNF, which makes analysis and processing simpler.

## References:
[1] [Chomsky Normal Form Wiki](https://en.wikipedia.org/wiki/Chomsky_normal_form) <br />
[2] [LFPC Guide](https://else.fcim.utm.md/pluginfile.php/110458/mod_resource/content/0/LFPC_Guide.pdf)<br />
[3] [Chomsky's Normal Form (CNF)](https://www.javatpoint.com/automata-chomskys-normal-form)
 
