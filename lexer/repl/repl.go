package repl

import (
	"bufio"
	"fmt"
	"io"

	"github.com/MariaProcopii/GPL/lexer"
	"github.com/MariaProcopii/GPL/token"
)

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
