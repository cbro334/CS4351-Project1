
# CSC 4351 Project 1: Lexical Analysis

Team Members:
- Minseo Lee
- Courtney Platt
- Aaron Meche
- Christopher Brown

## Implementation

Implemented a lexical analyzer for C89 using Java and JLex. The lexer recognizes keywords, identifiers, operators, punctuators, integer literals, character literals, and string literals. It also supports the additional var and fun keywords.

Whitespace and block comments are skipped, and newlines are tracked for error reporting. String and character literals support escape sequences, including control characters, octal escapes, and hexadecimal escapes. Integer literals retain their original text.

## Testing

Tested the lexer on the LSU classes server using valid and invalid C input. Tests covered keywords, identifiers, operators, integer formats, escape sequences, comments, and error handling. The lexer successfully reports invalid literals, illegal characters, and unterminated comments or strings without crashing.

Floating-point constants, header names, preprocessing numbers, trigraphs, and multibyte characters are not implemented, as permitted by the project specification.

## AI Usage

AI tools assisted with generating test cases to validate our implementation
