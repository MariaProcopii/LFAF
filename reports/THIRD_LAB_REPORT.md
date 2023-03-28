# Topic: Lexer & Scanner

### Course: Formal Languages & Finite Automata
### Author: Procopii Maria

----

## Theory:

Lexical analysis is carried out by the lexer, which entails scanning the source code to find the fundamental building elements such identifiers,
keywords, operators, and punctuation. Moreover, it eliminates comments and whitespace because they don't affect the syntax of the program.

The lexer produces a stream of tokens, unordered data, which are sent to a parser for additional processing. The tokens are used by the parser 
to create a parse tree or abstract syntax tree (AST), which reflects the program's structure and semantics.

Lexers are frequently employed in interpreters, compilers, and other source code processing tools.

## Objectives:

1. Understand what lexical analysis is.
2. Get familiar with the inner workings of a lexer/scanner/tokenizer.
3. Implement a sample lexer and show how it works.

## Implementation Description:

First of all, I want to note that the lexer was made in Go because it is the part of my PBL project and there I've made the lexer for General Purpose Language in go.
No Java for this lab. :(
My program contains a file named `repl.go` (repeat evaluate print loop) to allow the user to enter and execute commands or code snippets one at a time, and immediately
see the results of their actions. My this chase, the result will be the token list from the input code.
Here things are simple, it reads the input code, create an instance of lexer (giving the code, which will be passed as a string), and in the end it will output the result.
Token are composed of Type and the literal.
```go
const PROMPT = ">> "


func Start(in io.Reader, out io.Writer) {
	scanner := bufio.NewScanner(in)

	for {
		fmt.Print(PROMPT)
		scanned := scanner.Scan()
		if !scanned {
			return
		}

		line := scanner.Text()
		l := lexer.New(line)

		for tok := l.NextToken(); tok.Type != token.EOF; tok = l.NextToken() {
			fmt.Printf("Type:%v, Literal:%v\n", tok.Type, tok.Literal)
		}
	}
}
```
First step in creating the lexer was to make the token list, which contains every type of token, such as:

1. Identifiers
2. Literals
3. Operators
4. Comments
5. Keywords

As I mentioned before, tokens have two fields, the type of the token in instance, and the literal it encountered.
1. Identifiers are user-defined variables, function signatures and much more.
2. Literals are the digits, strings, and any other data primitive data types.
3. Operators, are all the symbols with which we can do math, and bit operations, for instance, the following symbols: +, -, /, *, &&, ||, etc.
4. Comments, are strings that come after the special symbol //.
5. Keywords are all the reserved words that have a special meaning for language, for example def keywords is used to define a function.
```go
....

IDENT  = "IDENT"
	INT    = "INT"
	BOOL   = "BOOL"
	NONE   = "NONE"
	STRING = "STRING"

	// Operators
	ASSIGN   = "="
	PLUS     = "+"
	MINUS    = "-"
	BANG     = "!"
	ASTERISK = "*"
	SLASH    = "/"

	LT = "<"
	GT = ">"

	EQ        = "=="
	NOT_EQ    = "!="
	AND       = "&&"
	OR        = "||"
	INCREMENT = "++"
	DECREMENT = "--"
  
  ....and much more
```

So, in `token.go` file I defined the structure of the token, tokenType 'list', a map of keywords and a special function
named `LookIdent` which will verify if the string is a keyword or just a IDENT ( user-defined variables, function signatures ).
```go
func LookupIdent(ident string) TokenType {
	if tok, ok := keywords[ident]; ok {
		return tok
	}
	return IDENT
}

```

The main functionality is in `lexer.go`. Here I have the lexer structure, which contains: `input` - the input string/'code', `position` - index 
of our char position at the moment, `readPossition` - index of next char, `ch` - the actual char of current position. We need to analyze two chars per time
because when we have situations like increment ++, decrement --, equal ==, and operation &&..., we need to assign one tokenType per these two symbols.
I have a lot of secondary functions like: `isLeter` - verify if the ch is a letter, `isDigit` - verify if it's a number, `isString`, `skipWhitespaces` to ignore the white spaces.
```go
...
func isLetter(ch byte) bool {
	return 'a' <= ch && ch <= 'z' || 'A' <= ch && ch <= 'Z' || ch == '_'
}

func isDigit(ch byte) bool {
	return '0' <= ch && ch <= '9'
}
...
```
Function `readChar` reads and assigns the current char, increase the index of the readPossition - index of next char.
`ReadIdentifier` we use to read the user-defined variables, function signatures, to assign the result as token Literal.
`ReadNumber` - same as the previous but with numbers.
`peekChar` - returns the char of the next char in the string. ( for readPosition )
`readString` - used to read the strings ( to take the whole string, not just one char )

```go
...
func (l *Lexer) peekChar() byte {
	if l.readPosition >= len(l.input) {
		return 0
	} else {
		return l.input[l.readPosition]
	}
}

func (l *Lexer) readChar() {
	if l.readPosition >= len(l.input) {
		l.ch = 0
	} else {
		l.ch = l.input[l.readPosition]
	}
	l.position = l.readPosition
	l.readPosition += 1
}
...
```
The main functionality is placed in switch case block. We analyze the character and for each case, we assign the required tokenType.
In the end we return the formed token.
In case such as '=', '+', '-', we should also analyze the next char, to check if we have increment, equal sign ..., here we use
functions `peekChar` and `readChar`.

```go
...
	case '=':
		if l.peekChar() == '=' {
			ch := l.ch
			l.readChar()
			tok = token.Token{
				Type:    token.EQ,
				Literal: string(ch) + string(l.ch),
			}
		} else {
			tok = newToken(token.ASSIGN, l.ch)
		}
	case '+':
		if l.peekChar() == '+' {
			ch := l.ch
			l.readChar()
			tok = token.Token{
				Type:    token.INCREMENT,
				Literal: string(ch) + string(l.ch),
			}
		} else {
			tok = newToken(token.PLUS, l.ch)
		}
    ...
```
The default case is when we have user-defined variables, function signatures, digits. In the case of a letter/string, we use `LookupIdent` to check
if it's not a predefined keyword.
```go
...
	default:
		if isLetter(l.ch) {
			tok.Literal = l.readIdentifier()
			tok.Type = token.LookupIdent(tok.Literal)
			return tok
		} else if isDigit(l.ch) {
			tok.Type = token.INT
			tok.Literal = l.readNumber()
			return tok
		} 
...
```
##Example:

Input code:
```
var x = 100
def add(x, y) {
return x + y
}
add(x, 10)
```

Result:
```
Type:VAR, Literal:var
Type:IDENT, Literal:x
Type:=, Literal:=
Type:INT, Literal:100
Type:FUNCTION, Literal:def
Type:IDENT, Literal:add
Type:(, Literal:(
Type:IDENT, Literal:x
Type:,, Literal:,
Type:IDENT, Literal:y
Type:), Literal:)
Type:{, Literal:{
Type:RETURN, Literal:return
Type:IDENT, Literal:x
Type:+, Literal:+
Type:IDENT, Literal:y
Type:}, Literal:}
Type:IDENT, Literal:add
Type:(, Literal:(
Type:IDENT, Literal:x
Type:,, Literal:,
Type:INT, Literal:10
Type:), Literal:)

```

##Conclusion

In conclusion, a lexer is a software element that does lexical analysis on a program's source code.
It disassembles the code into a series of tokens, which are significant chunks of syntax in programming languages.
The lexer's token list creates a structured representation of the code that the compiler or interpreter can readily process in later stages.
The performance and correctness of the final program can be greatly influenced by the lexer, which is an essential step in the compilation process.

##References:

1. https://github.com/DrVasile/FLFA-Labs
2. https://github.com/DrVasile/FLFA-Labs-Examples
3. https://en.wikipedia.org/wiki/Lexical_analysis
4. Writing an interpreter in go - https://en.wikipedia.org/wiki/Lexical_analysis

