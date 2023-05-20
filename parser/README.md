# Topic: Parser & Building an Abstract Syntax Tree

### Course: Formal Languages & Finite Automata
### Author: Procopii Maria

----
## Overview
In computer languages, a parser is an element or tool that examines the syntax and structure of a program written in a particular programming language. Its primary responsibility is to read the program's source code and translate it into an abstract syntax tree (AST), a more organized representation. The parser checks that the program follows the language's grammar rules and finds any syntax mistakes or ambiguities. The parser prepares the code for additional processing, such as interpretation or compilation into executable instructions, by parsing it.

## Objectives:
1. Get familiar with parsing, what it is and how it can be programmed [1].
2. Get familiar with the concept of AST [2].
3. In addition to what has been done in the 3rd lab work do the following:
   1. In case you didn't have a type that denotes the possible types of tokens you need to:
      1. Have a type __*TokenType*__ (like an enum) that can be used in the lexical analysis to categorize the tokens. 
      2. Please use regular expressions to identify the type of the token.
   2. Implement the necessary data structures for an AST that could be used for the text you have processed in the 3rd lab work.
   3. Implement a simple parser program that could extract the syntactic information from the input text.

## Implementation
The whole program uses the `main.go` file as the entry point, where REPL is started. 

**REPL**, which stands for **R**eapeat **E**valuate **P**rint **L**oop, is a part of the language, which lets the user interact with it. Jumping from the entry point into the `Start` function located in the REPL package, language is doing the following things:
1. Declares the `scanner` which will take the instructions from the user, and declares the environment, which helps later to define the scope of user declared variables, functions, etc.
```go
	scanner := bufio.NewScanner(in)
	env := object.NewEnvironment()
```
2. Afterwards joining into a infinite loop it prints the prompt and scannes for the input instructions from the user, if no instruction or input was given to the REPL, the program is ended.
```go
	fmt.Print(PROMPT)
	scanned := scanner.Scan()
	if !scanned {
		return
	}
```
3. Then the raw input is taken and assigned to the `line` variable. An instance of the lexer and the parser is made. For the sake of keeping it all short, all the work that the lexer does under the hood is skipped.  
```go
	line := scanner.Text()
	l := lexer.New(line)
	p := parser.New(l)
```
4. After creating an instance of the parser, the user's input is parsed. In the case of parsing errors, the program is throwing them at the user and continues with the next iteration of the REPL. 
```go
	program := p.ParseProgram()
	if len(p.Errors()) != 0 {
		printParserErrors(out, p.Errors())
		continue
	}
```
5. Finally, the REPL is evaluating the parsed elements, and printing the results to the user's prompt. 
```go
	evaluated := evaluator.Eval(program, env)
	if evaluated != nil {
		io.WriteString(out, evaluated.Inspect())
		io.WriteString(out, "\n")
	}
```

The parser is basically a data structure which holds the following things:
1. An instance of the lexer
2. Possible errors
3. The current and the next Token 
4. Two maps for the prefix operators and for the infix operators
```go
type Parser struct {
	l *lexer.Lexer

	errors []string

	curToken  token.Token
	peekToken token.Token

	prefixParseFns map[token.TokenType]prefixParseFn
	infixParseFns  map[token.TokenType]infixParseFn
}
```

This data structure, has lots and lots of methods. One of the important onces, is the `New` method, which returns a pointer to an instance of the `Parser` type. In order to accomplish that, it first of all creates an instace of the `Parser` struct:
```go
	p := &Parser{
		l:      l,
		errors: []string{},
	}
```

Then, to set up everything it runs two times the `nextToken` function, which sets up the `curToken` and `peekToken` fields in the struct. 

Afterwards, it declares two maps, the first map is for all the prefix operators, and the second one is for the infix operators. As the parser is a bottom-up parser, a slight variation of the it was chosen, and more precisely the `operator-precedence parser` type. Following the guidelines of this type of parsers, a playload of functions for each operator has to be declared and mapped, which is exactly for what these two maps where made in the first place.
```go
	p.prefixParseFns = make(map[token.TokenType]prefixParseFn)
	p.registerPrefix(token.IDENT, p.parseIdentifier)
	p.registerPrefix(token.STRING, p.parseString)
	p.registerPrefix(token.INT, p.parseIntegerLiteral)
	p.registerPrefix(token.FLOAT, p.parseFloatLiteral)
	p.registerPrefix(token.BANG, p.parsePrefixExpression)
	p.registerPrefix(token.MINUS, p.parsePrefixExpression)
	p.registerPrefix(token.INCREMENT, p.parsePrefixExpression)
	p.registerPrefix(token.TRUE, p.parseBoolean)
	p.registerPrefix(token.FALSE, p.parseBoolean)
	p.registerPrefix(token.LPAREN, p.parseGroupedExpression)
	p.registerPrefix(token.IF, p.parseIfExpression)
	p.registerPrefix(token.FUNCTION, p.parseFunctionLiteral)

	p.infixParseFns = make(map[token.TokenType]infixParseFn)
	p.registerInfix(token.PLUS, p.parseInfixExpression)
	p.registerInfix(token.MINUS, p.parseInfixExpression)
	p.registerInfix(token.SLASH, p.parseInfixExpression)
	p.registerInfix(token.ASTERISK, p.parseInfixExpression)
	p.registerInfix(token.EQ, p.parseInfixExpression)
	p.registerInfix(token.NOT_EQ, p.parseInfixExpression)
	p.registerInfix(token.LT, p.parseInfixExpression)
	p.registerInfix(token.GT, p.parseInfixExpression)
	p.registerInfix(token.LPAREN, p.parseCallExpression)
```

And finally, the function is returning the instance of ther `Parser` type.


The next widely used function of the parser is the `ParseProgram` function. The purpose of the function is to build up an AST and return it for further processing, i.e. evaluation step. 
To achieve that, the program,is firstly declaring an instance of the `Program` struct from the `ast` package, and setting it up. 
```go
	program := &ast.Program{}
	program.Statements = []ast.Statement{}
```
Next, it is iteratting over the tokens, until the `EOF` token is found. At each iteration the parser is finding a statement and appending it to the `Program` instance, the moving to the next token.
```go
	for p.curToken.Type != token.EOF {
		stmt := p.parseStatement()
		if stmt != nil {
			program.Statements = append(program.Statements, stmt)
		}
		p.nextToken()
	}
```
Finally it returns the ast. 

The `parseStatement` function is quite simple it just matches 3 of the possible statements. 
```go
	switch p.curToken.Type {
	case token.VAR:
		return p.parseVarStatement()
	case token.RETURN:
		return p.parseReturnStatement()
	default:
		return p.parseExpressionStatement()
	}
```

The `parseVarStatement` and `parseReturnStatement` are trivial functions, that match their pattern, and place the tokens in the right fields of the appropriate structs, which afterwards are placed in the AST. 

The more interesting function is the `parseExpressionStatement`, which has to parse and expression. The parser has a few levels of precedences:
```go
const (
	_ int = iota
	LOWEST
	EQUALS      // ==
	LESSGREATER // > or <
	SUM         // +
	PRODUCT     // *
	PREFIX      // -X or !X
	CALL        // myfunc(x)
)

var precedences = map[token.TokenType]int{
	token.EQ:       EQUALS,
	token.NOT_EQ:   EQUALS,
	token.LT:       LESSGREATER,
	token.GT:       LESSGREATER,
	token.PLUS:     SUM,
	token.MINUS:    SUM,
	token.SLASH:    PRODUCT,
	token.ASTERISK: PRODUCT,
	token.LPAREN:   CALL,
}
```

These levels determine the order of parsing and eventually the evaluation order too. 
First the parser tries to check for a prefix expression, when parsing an expression:
```go
	prefix := p.prefixParseFns[p.curToken.Type]
	if prefix == nil {
		if p.curToken.Type != token.NEWLINE {
			p.noPrefixParseFnError(p.curToken.Type)
		}
		return nil
	}
	leftExp := prefix()
```
Then it tries to check for an infix operator, if that is not found, then the returened expression will be just an prefix expression, otherwise an infix one. 
```go
	for !p.peekTokenIs(token.NEWLINE) && precedence < p.peekPrecedence() {
		infix := p.infixParseFns[p.peekToken.Type]
		if infix == nil {
			return leftExp
		}

		p.nextToken()

		leftExp = infix(leftExp)
	}
```
In the end the function is returning the expression. 

To properly parse the expression, the parser is calling the mapped function to the found operator, which is the key idea behind the `operator-precedence parsers`. 

The evaluation stage is also implemented, but its explanation is outer of the scope of this laboratory work. 


## Example of a result
```
Hello maria!

>> var x = 42
>> var y = 234
>> x + y 
276

>> var u = 3.123
>> var z = 52.23
>> u + z
55.352999999999994

>> def add(x, y) { x + y }
def add (x, y) {
(x + y)}

>> add(543, 723)
1266

>> var name = "mike"
>> var surname = "vazofski"
>> name + " " + surname
mike vazofski

>> if (x < 100) { "x is less than 100" }     
x is less than 100
>> if (x > 100) { "x is greater than 100" } else { "x is less than 100" }
x is less than 100

>> var t = true 
>> var f = false
>> t == f
false
>> t == t
true
>> f == f
true

>> var name = "masha"
>> var x = 20
>> name + x 
ERROR: type mismatch: STRING + INTEGER
```

## Conclusion
In conclusion, the bottom-up parser laboratory work involved implementing a parsing algorithm that constructs the parse tree in a bottom-up manner. It required understanding bottom-up parsing algorithms (e.g., LR(0), SLR(1), LALR(1)), implementing data structures (e.g., stack, parse table), and writing the parsing algorithm. The laboratory work provided practical experience in parsing techniques and improved our understanding of bottom-up parsers, including shift-reduce actions, parse table conflicts, and parse tree construction. It contributed to our knowledge of programming language implementation.

## References:
[1] [Parsing Wiki](https://en.wikipedia.org/wiki/Parsing)

[2] [Abstract Syntax Tree Wiki](https://en.wikipedia.org/wiki/Abstract_syntax_tree)
 
