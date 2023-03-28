package token

type TokenType string

type Token struct {
	Type    TokenType
	Literal string
}

const (
	ILLEGAL = "ILLEGAL"
	EOF     = "EOF"

	// Identifiers + literals
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

	// Delimiters
	COMMA     = ","
	SEMICOLON = ";"
	DBLQUOTE  = "\""
	LPAREN    = "("
	RPAREN    = ")"
	LBRACE    = "{"
	RBRACE    = "}"
	LSQBRA    = "["
	RSQBRA    = "]"
	NEWLINE   = "\n"

	// Comments
	COMMENT = "//"

	// Keywords
	FUNCTION = "FUNCTION"
	TRUE     = "TRUE"
	FALSE    = "FALSE"
	IF       = "IF"
	UNLESS   = "UNLESS"
	THEN     = "THEN"
	ELSE     = "ELSE"
	FOR      = "FOR"
	RETURN   = "RETURN"
	VAR      = "VAR"
)

var keywords = map[string]TokenType{
	"def":    FUNCTION,
	"true":   TRUE,
	"false":  FALSE,
	"if":     IF,
	"unless": UNLESS,
	"then":   THEN,
	"else":   ELSE,
	"for":    FOR,
	"return": RETURN,
	"none":   NONE,
	"var":    VAR,
}

func LookupIdent(ident string) TokenType {
	if tok, ok := keywords[ident]; ok {
		return tok
	}
	return IDENT
}
