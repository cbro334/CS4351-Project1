package Parse;
import ErrorMsg.ErrorMsg;


class Yylex implements Lexer {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final char YYEOF = '\uFFFF';

private ErrorMsg errorMsg;
private StringBuilder sb = new StringBuilder();
private int start = -1;
private int commentStart = -1;
private boolean invalid = false;
private void newline() {
  errorMsg.newline(yychar + yylength() - 1);
}
private void err(int pos, String s) {
  errorMsg.error(pos,s);
}
private void err(String s) {
  err(yychar,s);
}
private java_cup.runtime.Symbol tok(int kind) {
  return tok(kind, null);
}
private java_cup.runtime.Symbol tok(int kind, Object value) {
  return new java_cup.runtime.Symbol(kind, yychar, yychar+yylength(), value);
}
private java_cup.runtime.Symbol literal(int kind) {
  return new java_cup.runtime.Symbol(kind, start, yychar+yylength(), sb.toString());
}
private void startLiteral() {
  start = yychar;
  sb.setLength(0);
  invalid = false;
}
private void escape(String s) {
  char c = s.charAt(1);
  if (c == 'x') {
    int value = 0;
    for (int i = 2; i < s.length(); i++) {
      value = (value * 16 + Character.digit(s.charAt(i), 16)) & 255;
    }
    sb.append((char)value);
    return;
  }
  if (c >= '0' && c <= '7') {
    int value = 0;
    for (int i = 1; i < s.length(); i++) {
      value = value * 8 + (s.charAt(i) - '0');
    }
    sb.append((char)(value & 255));
    return;
  }
  switch (c) {
    case 'a': sb.append('\007'); break;
    case 'b': sb.append('\b'); break;
    case 'f': sb.append('\f'); break;
    case 'n': sb.append('\n'); break;
    case 'r': sb.append('\r'); break;
    case 't': sb.append('\t'); break;
    case 'v': sb.append('\013'); break;
    case '\\': sb.append('\\'); break;
    case '\'': sb.append('\''); break;
    case '"': sb.append('"'); break;
    case '?': sb.append('?'); break;
    default:
      err("Invalid escape sequence: " + s);
      invalid = true;
  }
}
Yylex(java.io.InputStream s, ErrorMsg e) {
  this(s);
  errorMsg=e;
}
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yychar;
	private int yy_lexical_state;

	Yylex (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	Yylex (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private Yylex () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yychar = 0;
		yy_lexical_state = YYINITIAL;
	}

	private boolean yy_eof_done = false;
	private final int STRING = 2;
	private final int YYINITIAL = 0;
	private final int CHARACTER = 3;
	private final int COMMENT = 1;
	private final int yy_state_dtrans[] = {
		0,
		120,
		125,
		132
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private char yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YYEOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YYEOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_start () {
		++yychar;
		++yy_buffer_start;
	}
	private void yy_pushback () {
		--yy_buffer_end;
	}
	private void yy_mark_start () {
		yychar = yychar
			+ yy_buffer_index - yy_buffer_start;
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
private int [][] unpackFromString(int size1, int size2, String st)
    {
      int colonIndex = -1;
      String lengthString;
      int sequenceLength = 0;
      int sequenceInteger = 0;
      int commaIndex;
      String workString;
      int res[][] = new int[size1][size2];
      for (int i= 0; i < size1; i++)
	for (int j= 0; j < size2; j++)
	  {
	    if (sequenceLength == 0) 
	      {	
		commaIndex = st.indexOf(',');
		if (commaIndex == -1)
		  workString = st;
		else
		  workString = st.substring(0, commaIndex);
		st = st.substring(commaIndex+1);
		colonIndex = workString.indexOf(':');
		if (colonIndex == -1)
		  {
		    res[i][j] = Integer.parseInt(workString);
		  }
		else 
		  {
		    lengthString = workString.substring(colonIndex+1);  
		    sequenceLength = Integer.parseInt(lengthString);
		    workString = workString.substring(0,colonIndex);
		    sequenceInteger = Integer.parseInt(workString);
		    res[i][j] = sequenceInteger;
		    sequenceLength--;
		  }
	      }
	    else 
	      {
		res[i][j] = sequenceInteger;
		sequenceLength--;
	      }
	  }
      return res;
    }
	private int yy_acpt[] = {
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR
	};
	private int yy_cmap[] = {
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 1, 2, 1, 1, 3, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		4, 5, 6, 7, 0, 8, 9, 10,
		11, 12, 13, 14, 15, 16, 17, 18,
		19, 20, 20, 20, 20, 20, 20, 20,
		21, 21, 22, 23, 24, 25, 26, 27,
		0, 28, 28, 28, 28, 28, 28, 29,
		29, 29, 29, 29, 30, 29, 29, 29,
		29, 29, 29, 29, 29, 31, 29, 29,
		32, 29, 29, 33, 34, 35, 36, 29,
		0, 37, 38, 39, 40, 41, 42, 43,
		44, 45, 29, 46, 47, 48, 49, 50,
		51, 29, 52, 53, 54, 55, 56, 57,
		58, 59, 60, 61, 62, 63, 64, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0 
	};
	private int yy_rmap[] = {
		0, 1, 2, 1, 3, 4, 1, 5,
		6, 7, 1, 1, 1, 8, 9, 1,
		10, 11, 12, 13, 14, 1, 1, 15,
		16, 17, 1, 18, 1, 1, 19, 1,
		20, 1, 1, 1, 1, 1, 1, 1,
		1, 1, 1, 1, 1, 1, 1, 1,
		21, 22, 1, 1, 1, 23, 1, 24,
		18, 1, 1, 1, 25, 1, 1, 18,
		18, 18, 18, 18, 18, 18, 18, 18,
		18, 18, 18, 18, 18, 18, 18, 18,
		18, 18, 18, 18, 18, 18, 18, 18,
		18, 18, 18, 18, 18, 18, 18, 26,
		1, 27, 1, 28, 1, 1, 1, 1,
		29, 30, 31, 1, 32, 33, 34, 35,
		36, 37, 38, 39, 40, 30, 41, 1,
		42, 43, 44, 45, 46, 47, 21, 21,
		48, 21, 49, 50, 51, 52, 53, 54,
		55, 56, 57, 58, 59, 60, 61, 62,
		63, 64, 65, 66, 67, 68, 69, 70,
		71, 72, 73, 74, 75, 76, 77, 78,
		79, 80, 81, 82, 83, 84, 85, 86,
		87, 88, 89, 90, 91, 92, 93, 94,
		95, 96, 97, 98, 99, 100, 101, 102,
		103, 104, 105, 106, 107, 108, 109, 110,
		111, 112, 113, 114, 115, 116, 117, 118,
		119, 120, 121, 122, 123, 124, 125, 126,
		127, 128, 129, 130, 131, 132, 133, 134,
		135, 136, 137, 138, 139, 140, 141, 142,
		143, 144, 145, 146, 147, 148, 149, 150,
		151, 152, 153, 154, 155, 156, 157, 158,
		159, 160 
	};
	private int yy_nxt[][] = unpackFromString(161,65,
"1,2,3,109,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20:2,21,22,23,24,25,26,27:5,28,1,29,30,195,220,196,112,197,164,198,27,123,27,199,27:4,235,221,241,222,166,223,27:3,31,32,33,34,-1:66,2,-1:67,4,-1:85,35,-1:46,36,-1:82,37,-1:48,38,-1:15,39,-1:64,40,-1:53,41,-1:10,42,-1:55,43,-1:8,44,45,-1:55,108,-1:60,46,-1:11,47,-1:58,163:2,48,-1:6,48:2,110,121,113,-1:4,48:10,110,48:7,121,48:2,113,48:2,-1:23,20:3,-1:6,48:2,111,122,48,-1:4,48:10,111,48:7,122,48:5,-1:28,49,50,-1:64,51,-1:64,52,53,-1:57,27:3,-1:6,27:5,-1:4,27:24,-1:29,54,-1:64,57,-1:36,58,-1:21,48:3,-1:6,48:5,-1:4,48:24,-1:29,61,-1:64,62,-1:58,27:3,-1:6,27:5,-1:4,27:18,206,27:5,-1:23,60:3,-1:6,60,48,114,124,48,-1:4,60:6,48:4,114,48:7,124,48:5,-1:4,95:2,-1:2,95:9,-1,95:51,-1:18,98,-1:46,99:2,-1:2,99:2,-1,99:27,-1,99:30,-1:19,165:2,-1:63,105:3,-1:6,105,-1:8,105:6,-1:22,106:2,-1:2,106:6,-1,106:23,-1,106:30,-1:17,59,-1:49,3,-1:81,48:3,-1:6,48:3,126,48,-1:4,48:18,126,48:5,-1:23,48:3,-1:6,48:3,127,48,-1:4,48:18,127,48:5,-1:23,27:3,-1:6,27:5,-1:4,27:4,236,27:8,55,27:10,-1:23,60:3,-1:6,60,48:4,-1:4,60:6,48:18,-1:23,48:3,-1:6,48:3,129,48,-1:4,48:18,129,48:5,-1:6,96,-1:64,100,-1:64,103,-1:62,95:2,96,115,95:9,97,95:51,-1:19,48:3,-1:6,48:2,126,48:2,-1:4,48:10,126,48:13,-1:23,48:3,-1:6,48:2,127,48:2,-1:4,48:10,127,48:13,-1:23,27:3,-1:6,27:5,-1:4,27:5,56,27:6,133,27:11,-1:23,48:3,-1:6,48:2,129,48:2,-1:4,48:10,129,48:13,-1:4,99:2,100,116,99:2,101,99:27,130,99:30,-1:19,27:3,-1:6,27:5,-1:4,27:15,63,27:8,-1:4,102:2,103,118,102:15,104:2,102:37,117,102:6,-1:19,27:3,-1:6,27:5,-1:4,27:12,64,27:11,-1:4,106:2,100,116,106:6,107,106:23,130,106:30,-1:19,27:3,-1:6,27:5,-1:4,27:17,65,27:6,-1:23,27:3,-1:6,27:5,-1:4,27:15,66,27:8,-1:23,27:3,-1:6,27:5,-1:4,27:13,67,27:10,-1:23,27:3,-1:6,27:5,-1:4,27:4,68,27:19,-1:23,27:3,-1:6,27:5,-1:4,27:15,69,27:8,-1:23,27:3,-1:6,27:5,-1:4,27:4,70,27:19,-1:23,27:3,-1:6,27:5,-1:4,27:11,71,27:12,-1:23,27:3,-1:6,27:5,-1:4,27:13,72,27:10,-1:23,27:3,-1:6,27:5,-1:4,27:6,73,27:17,-1:23,27:3,-1:6,27:5,-1:4,27:3,74,27:20,-1:23,27:3,-1:6,27:5,-1:4,27:9,75,27:14,-1:23,27:3,-1:6,27:5,-1:4,27:17,76,27:6,-1:23,27:3,-1:6,27:5,-1:4,27:17,77,27:6,-1:23,27:3,-1:6,27:5,-1:4,27:17,78,27:6,-1:23,27:3,-1:6,27:5,-1:4,27:12,79,27:11,-1:23,27:3,-1:6,27:5,-1:4,27:4,80,27:19,-1:23,27:3,-1:6,27:5,-1:4,27:4,81,27:19,-1:23,27:3,-1:6,27:5,-1:4,27:12,82,27:11,-1:23,27:3,-1:6,27:5,-1:4,27:12,83,27:11,-1:23,27:3,-1:6,27:5,-1:4,27:3,84,27:20,-1:23,27:3,-1:6,27:5,-1:4,27:5,85,27:18,-1:23,27:3,-1:6,27:5,-1:4,27:2,86,27:21,-1:23,27:3,-1:6,27:5,-1:4,27:17,87,27:6,-1:23,27:3,-1:6,27:5,-1:4,27:7,88,27:16,-1:23,27:3,-1:6,27:5,-1:4,27:17,89,27:6,-1:23,27:3,-1:6,27:5,-1:4,27:5,90,27:18,-1:23,27:3,-1:6,27:5,-1:4,27:4,91,27:19,-1:23,27:3,-1:6,27:5,-1:4,27:15,92,27:8,-1:23,27:3,-1:6,27:5,-1:4,27:3,93,27:20,-1:23,27:3,-1:6,27:5,-1:4,27:4,94,27:19,-1:23,163:2,48,-1:6,48:2,110,121,48,-1:4,48:10,110,48:7,121,48:5,-1:23,27:3,-1:6,27:5,-1:4,27:10,202,27:2,128,27:4,131,27:5,-1:23,119:2,-1:63,27:3,-1:6,27:5,-1:4,134,27:12,174,27:10,-1:23,27:3,-1:6,27:5,-1:4,27:17,135,27:6,-1:23,27:3,-1:6,27:5,-1:4,27:16,136,27:7,-1:23,27:3,-1:6,27:5,-1:4,137,27:23,-1:23,27:3,-1:6,27:5,-1:4,27:16,138,27:7,-1:23,27:3,-1:6,27:5,-1:4,27:18,139,27:5,-1:23,27:3,-1:6,27:5,-1:4,27:17,140,27:6,-1:23,27:3,-1:6,27:5,-1:4,27:12,141,27:11,-1:23,27:3,-1:6,27:5,-1:4,27:8,142,27,240,27:13,-1:23,27:3,-1:6,27:5,-1:4,143,27:23,-1:23,27:3,-1:6,27:5,-1:4,27:16,144,231,27:6,-1:23,27:3,-1:6,27:5,-1:4,145,27:23,-1:23,27:3,-1:6,27:5,-1:4,27:15,146,27:8,-1:23,27:3,-1:6,27:5,-1:4,27:13,147,27:10,-1:23,27:3,-1:6,27:5,-1:4,27:10,148,27:13,-1:23,27:3,-1:6,27:5,-1:4,27:10,149,27:13,-1:23,27:3,-1:6,27:5,-1:4,27:15,150,27:8,-1:23,27:3,-1:6,27:5,-1:4,27:15,151,27:8,-1:23,27:3,-1:6,27:5,-1:4,27:4,152,27:19,-1:23,27:3,-1:6,27:5,-1:4,27:13,153,27:10,-1:23,27:3,-1:6,27:5,-1:4,27:8,154,27:15,-1:23,27:3,-1:6,27:5,-1:4,27:2,155,27:21,-1:23,27:3,-1:6,27:5,-1:4,27:2,156,27:21,-1:23,27:3,-1:6,27:5,-1:4,27:10,157,27:13,-1:23,27:3,-1:6,27:5,-1:4,27:4,158,27:19,-1:23,27:3,-1:6,27:5,-1:4,27:18,159,27:5,-1:23,27:3,-1:6,27:5,-1:4,27:4,160,27:19,-1:23,27:3,-1:6,27:5,-1:4,27:4,161,27:19,-1:23,27:3,-1:6,27:5,-1:4,27:10,162,27:13,-1:23,27:3,-1:6,27:5,-1:4,27:18,167,27:5,-1:23,27:3,-1:6,27:5,-1:4,168,27:6,169,27:5,201,27:10,-1:23,27:3,-1:6,27:5,-1:4,27:10,170,27,171,27:8,224,27:2,-1:23,27:3,-1:6,27:5,-1:4,27:13,172,27:10,-1:23,27:3,-1:6,27:5,-1:4,27:13,173,27:10,-1:23,27:3,-1:6,27:5,-1:4,27:4,175,27:19,-1:23,27:3,-1:6,27:5,-1:4,27:12,176,27:11,-1:23,27:3,-1:6,27:5,-1:4,27:13,177,27:10,-1:23,27:3,-1:6,27:5,-1:4,27:13,178,27:10,-1:23,27:3,-1:6,27:5,-1:4,27:8,179,27:7,239,27:7,-1:23,27:3,-1:6,27:5,-1:4,27:8,180,27:15,-1:23,27:3,-1:6,27:5,-1:4,27,181,27:22,-1:23,27:3,-1:6,27:5,-1:4,27:4,182,27:19,-1:23,27:3,-1:6,27:5,-1:4,27:18,183,27:5,-1:23,27:3,-1:6,27:5,-1:4,27:12,184,27:11,-1:23,27:3,-1:6,27:5,-1:4,27:4,185,27:19,-1:23,27:3,-1:6,27:5,-1:4,27:17,186,27:6,-1:23,27:3,-1:6,27:5,-1:4,27:18,187,27:5,-1:23,27:3,-1:6,27:5,-1:4,27:17,188,27:6,-1:23,27:3,-1:6,27:5,-1:4,27:18,189,27:5,-1:23,27:3,-1:6,27:5,-1:4,27:3,190,27:20,-1:23,27:3,-1:6,27:5,-1:4,27:12,191,27:11,-1:23,27:3,-1:6,27:5,-1:4,27:17,192,27:6,-1:23,27:3,-1:6,27:5,-1:4,27:12,193,27:11,-1:23,27:3,-1:6,27:5,-1:4,27:8,194,27:15,-1:23,27:3,-1:6,27:5,-1:4,27:15,200,27:8,-1:23,27:3,-1:6,27:5,-1:4,27:7,203,226,27:8,227,27:2,228,27:3,-1:23,27:3,-1:6,27:5,-1:4,27:12,204,27:11,-1:23,27:3,-1:6,27:5,-1:4,27:7,205,27:16,-1:23,27:3,-1:6,27:5,-1:4,27:17,207,27:6,-1:23,27:3,-1:6,27:5,-1:4,27:6,238,27:10,208,27:6,-1:23,27:3,-1:6,27:5,-1:4,27:6,209,27:16,210,-1:23,27:3,-1:6,27:5,-1:4,211,27:14,212,27:8,-1:23,27:3,-1:6,27:5,-1:4,27:8,213,27:15,-1:23,27:3,-1:6,27:5,-1:4,214,27:23,-1:23,27:3,-1:6,27:5,-1:4,27:4,215,27:19,-1:23,27:3,-1:6,27:5,-1:4,27:8,216,27:15,-1:23,27:3,-1:6,27:5,-1:4,27:16,217,27:7,-1:23,27:3,-1:6,27:5,-1:4,27:6,218,27:17,-1:23,27:3,-1:6,27:5,-1:4,27:17,219,27:6,-1:23,27:3,-1:6,27:5,-1:4,27:4,225,27:19,-1:23,27:3,-1:6,27:5,-1:4,27:5,229,27:18,-1:23,27:3,-1:6,27:5,-1:4,27:14,230,27:9,-1:23,27:3,-1:6,27:5,-1:4,27:8,232,27:15,-1:23,27:3,-1:6,27:5,-1:4,27:8,233,27:15,-1:23,27:3,-1:6,27:5,-1:4,234,27:23,-1:23,27:3,-1:6,27:5,-1:4,27:22,237,27,-1:4");
	public java_cup.runtime.Symbol nextToken ()
		throws java.io.IOException {
		char yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			if (YYEOF != yy_lookahead) {
				yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YYEOF == yy_lookahead && true == yy_initial) {

  if (commentStart >= 0) {
    err(commentStart, "Unterminated comment");
    commentStart = -1;
  }
  if (start >= 0) {
    err(start, "Unterminated literal");
    start = -1;
  }
  return tok(sym.EOF, null);
				}
				else if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_to_mark();
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_pushback();
					}
					if (0 != (YY_START & yy_anchor)) {
						yy_move_start();
					}
					switch (yy_last_accept_state) {
					case 1:
						{err("Illegal character: " + yytext());}
					case -2:
						break;
					case 2:
						{}
					case -3:
						break;
					case 3:
						{newline();}
					case -4:
						break;
					case 4:
						{}
					case -5:
						break;
					case 5:
						{return tok(sym.NOT, null);}
					case -6:
						break;
					case 6:
						{startLiteral(); yybegin(STRING);}
					case -7:
						break;
					case 7:
						{return tok(sym.HASH, null);}
					case -8:
						break;
					case 8:
						{return tok(sym.MODULUS, null);}
					case -9:
						break;
					case 9:
						{return tok(sym.BITWISEAND, null);}
					case -10:
						break;
					case 10:
						{startLiteral(); yybegin(CHARACTER);}
					case -11:
						break;
					case 11:
						{return tok(sym.LPAREN, null);}
					case -12:
						break;
					case 12:
						{return tok(sym.RPAREN, null);}
					case -13:
						break;
					case 13:
						{return tok(sym.TIMES, null);}
					case -14:
						break;
					case 14:
						{return tok(sym.PLUS, null);}
					case -15:
						break;
					case 15:
						{return tok(sym.COMMA, null);}
					case -16:
						break;
					case 16:
						{return tok(sym.MINUS, null);}
					case -17:
						break;
					case 17:
						{return tok(sym.PERIOD, null);}
					case -18:
						break;
					case 18:
						{return tok(sym.DIVIDE, null);}
					case -19:
						break;
					case 19:
						{return tok(sym.DECIMAL_LITERAL, yytext());}
					case -20:
						break;
					case 20:
						{return tok(sym.DECIMAL_LITERAL, yytext());}
					case -21:
						break;
					case 21:
						{return tok(sym.COLON, null);}
					case -22:
						break;
					case 22:
						{return tok(sym.SEMICOLON, null);}
					case -23:
						break;
					case 23:
						{return tok(sym.LT, null);}
					case -24:
						break;
					case 24:
						{return tok(sym.ASSIGN, null);}
					case -25:
						break;
					case 25:
						{return tok(sym.GT, null);}
					case -26:
						break;
					case 26:
						{return tok(sym.QUESTION, null);}
					case -27:
						break;
					case 27:
						{return tok(sym.ID, yytext());}
					case -28:
						break;
					case 28:
						{return tok(sym.LBRACK, null);}
					case -29:
						break;
					case 29:
						{return tok(sym.RBRACK, null);}
					case -30:
						break;
					case 30:
						{return tok(sym.BWISEXOR, null);}
					case -31:
						break;
					case 31:
						{return tok(sym.LBRACE, null);}
					case -32:
						break;
					case 32:
						{return tok(sym.BWISEOR, null);}
					case -33:
						break;
					case 33:
						{return tok(sym.RBRACE, null);}
					case -34:
						break;
					case 34:
						{return tok(sym.TILDE, null);}
					case -35:
						break;
					case 35:
						{return tok(sym.NEQ, null);}
					case -36:
						break;
					case 36:
						{return tok(sym.HASHHASH, null);}
					case -37:
						break;
					case 37:
						{return tok(sym.MODASSIGN, null);}
					case -38:
						break;
					case 38:
						{return tok(sym.AND, null);}
					case -39:
						break;
					case 39:
						{return tok(sym.BWISEANDASSIGN, null);}
					case -40:
						break;
					case 40:
						{return tok(sym.MULASSIGN, null);}
					case -41:
						break;
					case 41:
						{return tok(sym.INCREMENT, null);}
					case -42:
						break;
					case 42:
						{return tok(sym.ADDASSIGN, null);}
					case -43:
						break;
					case 43:
						{return tok(sym.DECREMENT, null);}
					case -44:
						break;
					case 44:
						{return tok(sym.SUBASSIGN, null);}
					case -45:
						break;
					case 45:
						{return tok(sym.ARROW, null);}
					case -46:
						break;
					case 46:
						{commentStart = yychar; yybegin(COMMENT);}
					case -47:
						break;
					case 47:
						{return tok(sym.DIVASSIGN, null);}
					case -48:
						break;
					case 48:
						{err("Invalid integer literal: " + yytext());}
					case -49:
						break;
					case 49:
						{return tok(sym.LSHIFT, null);}
					case -50:
						break;
					case 50:
						{return tok(sym.LE, null);}
					case -51:
						break;
					case 51:
						{return tok(sym.EQ, null);}
					case -52:
						break;
					case 52:
						{return tok(sym.GE, null);}
					case -53:
						break;
					case 53:
						{return tok(sym.RSHIFT, null);}
					case -54:
						break;
					case 54:
						{return tok(sym.BWISEXORASSIGN, null);}
					case -55:
						break;
					case 55:
						{return tok(sym.DO, null);}
					case -56:
						break;
					case 56:
						{return tok(sym.IF, null);}
					case -57:
						break;
					case 57:
						{return tok(sym.BWISEORASSIGN, null);}
					case -58:
						break;
					case 58:
						{return tok(sym.OR, null);}
					case -59:
						break;
					case 59:
						{return tok(sym.ELIPSES, null);}
					case -60:
						break;
					case 60:
						{return tok(sym.DECIMAL_LITERAL, yytext());}
					case -61:
						break;
					case 61:
						{return tok(sym.LSHIFTASSIGN, null);}
					case -62:
						break;
					case 62:
						{return tok(sym.RSHIFTASSIGN, null);}
					case -63:
						break;
					case 63:
						{return tok(sym.FOR, null);}
					case -64:
						break;
					case 64:
						{return tok(sym.FUN, null);}
					case -65:
						break;
					case 65:
						{return tok(sym.INT, null);}
					case -66:
						break;
					case 66:
						{return tok(sym.VAR, null);}
					case -67:
						break;
					case 67:
						{return tok(sym.AUTO, null);}
					case -68:
						break;
					case 68:
						{return tok(sym.CASE, null);}
					case -69:
						break;
					case 69:
						{return tok(sym.CHAR, null);}
					case -70:
						break;
					case 70:
						{return tok(sym.ELSE, null);}
					case -71:
						break;
					case 71:
						{return tok(sym.ENUM, null);}
					case -72:
						break;
					case 72:
						{return tok(sym.GOTO, null);}
					case -73:
						break;
					case 73:
						{return tok(sym.LONG, null);}
					case -74:
						break;
					case 74:
						{return tok(sym.VOID, null);}
					case -75:
						break;
					case 75:
						{return tok(sym.BREAK, null);}
					case -76:
						break;
					case 76:
						{return tok(sym.CONST, null);}
					case -77:
						break;
					case 77:
						{return tok(sym.FLOAT, null);}
					case -78:
						break;
					case 78:
						{return tok(sym.SHORT, null);}
					case -79:
						break;
					case 79:
						{return tok(sym.UNION, null);}
					case -80:
						break;
					case 80:
						{return tok(sym.WHILE, null);}
					case -81:
						break;
					case 81:
						{return tok(sym.DOUBLE, null);}
					case -82:
						break;
					case 82:
						{return tok(sym.EXTERN, null);}
					case -83:
						break;
					case 83:
						{return tok(sym.RETURN, null);}
					case -84:
						break;
					case 84:
						{return tok(sym.SIGNED, null);}
					case -85:
						break;
					case 85:
						{return tok(sym.SIZEOF, null);}
					case -86:
						break;
					case 86:
						{return tok(sym.STATIC, null);}
					case -87:
						break;
					case 87:
						{return tok(sym.STRUCT, null);}
					case -88:
						break;
					case 88:
						{return tok(sym.SWITCH, null);}
					case -89:
						break;
					case 89:
						{return tok(sym.DEFAULT, null);}
					case -90:
						break;
					case 90:
						{return tok(sym.TYPEDEF, null);}
					case -91:
						break;
					case 91:
						{return tok(sym.CONTINUE, null);}
					case -92:
						break;
					case 92:
						{return tok(sym.REGISTER, null);}
					case -93:
						break;
					case 93:
						{return tok(sym.UNSIGNED, null);}
					case -94:
						break;
					case 94:
						{return tok(sym.VOLATILE, null);}
					case -95:
						break;
					case 95:
						{}
					case -96:
						break;
					case 96:
						{newline();}
					case -97:
						break;
					case 97:
						{}
					case -98:
						break;
					case 98:
						{commentStart = -1; yybegin(YYINITIAL);}
					case -99:
						break;
					case 99:
						{sb.append(yytext());}
					case -100:
						break;
					case 100:
						{
  err(start, "Unterminated literal");
  start = -1;
  newline();
  yybegin(YYINITIAL);
}
					case -101:
						break;
					case 101:
						{
  yybegin(YYINITIAL);
  if (!invalid) {
    java_cup.runtime.Symbol result = literal(sym.STRING_LITERAL);
    start = -1;
    return result;
  }
  start = -1;
}
					case -102:
						break;
					case 102:
						{escape(yytext());}
					case -103:
						break;
					case 103:
						{newline();}
					case -104:
						break;
					case 104:
						{escape(yytext());}
					case -105:
						break;
					case 105:
						{escape(yytext());}
					case -106:
						break;
					case 106:
						{sb.append(yytext());}
					case -107:
						break;
					case 107:
						{
  yybegin(YYINITIAL);
  if (!invalid && sb.length() == 1) {
    java_cup.runtime.Symbol result = literal(sym.CHAR_LITERAL);
    start = -1;
    return result;
  }
  if (!invalid) err(start, "Invalid character literal");
  start = -1;
}
					case -108:
						break;
					case 109:
						{newline();}
					case -109:
						break;
					case 110:
						{return tok(sym.DECIMAL_LITERAL, yytext());}
					case -110:
						break;
					case 111:
						{return tok(sym.DECIMAL_LITERAL, yytext());}
					case -111:
						break;
					case 112:
						{return tok(sym.ID, yytext());}
					case -112:
						break;
					case 113:
						{err("Invalid integer literal: " + yytext());}
					case -113:
						break;
					case 114:
						{return tok(sym.DECIMAL_LITERAL, yytext());}
					case -114:
						break;
					case 115:
						{newline();}
					case -115:
						break;
					case 116:
						{
  err(start, "Unterminated literal");
  start = -1;
  newline();
  yybegin(YYINITIAL);
}
					case -116:
						break;
					case 117:
						{escape(yytext());}
					case -117:
						break;
					case 118:
						{newline();}
					case -118:
						break;
					case 119:
						{escape(yytext());}
					case -119:
						break;
					case 121:
						{return tok(sym.DECIMAL_LITERAL, yytext());}
					case -120:
						break;
					case 122:
						{return tok(sym.DECIMAL_LITERAL, yytext());}
					case -121:
						break;
					case 123:
						{return tok(sym.ID, yytext());}
					case -122:
						break;
					case 124:
						{return tok(sym.DECIMAL_LITERAL, yytext());}
					case -123:
						break;
					case 126:
						{return tok(sym.DECIMAL_LITERAL, yytext());}
					case -124:
						break;
					case 127:
						{return tok(sym.DECIMAL_LITERAL, yytext());}
					case -125:
						break;
					case 128:
						{return tok(sym.ID, yytext());}
					case -126:
						break;
					case 129:
						{return tok(sym.DECIMAL_LITERAL, yytext());}
					case -127:
						break;
					case 131:
						{return tok(sym.ID, yytext());}
					case -128:
						break;
					case 133:
						{return tok(sym.ID, yytext());}
					case -129:
						break;
					case 134:
						{return tok(sym.ID, yytext());}
					case -130:
						break;
					case 135:
						{return tok(sym.ID, yytext());}
					case -131:
						break;
					case 136:
						{return tok(sym.ID, yytext());}
					case -132:
						break;
					case 137:
						{return tok(sym.ID, yytext());}
					case -133:
						break;
					case 138:
						{return tok(sym.ID, yytext());}
					case -134:
						break;
					case 139:
						{return tok(sym.ID, yytext());}
					case -135:
						break;
					case 140:
						{return tok(sym.ID, yytext());}
					case -136:
						break;
					case 141:
						{return tok(sym.ID, yytext());}
					case -137:
						break;
					case 142:
						{return tok(sym.ID, yytext());}
					case -138:
						break;
					case 143:
						{return tok(sym.ID, yytext());}
					case -139:
						break;
					case 144:
						{return tok(sym.ID, yytext());}
					case -140:
						break;
					case 145:
						{return tok(sym.ID, yytext());}
					case -141:
						break;
					case 146:
						{return tok(sym.ID, yytext());}
					case -142:
						break;
					case 147:
						{return tok(sym.ID, yytext());}
					case -143:
						break;
					case 148:
						{return tok(sym.ID, yytext());}
					case -144:
						break;
					case 149:
						{return tok(sym.ID, yytext());}
					case -145:
						break;
					case 150:
						{return tok(sym.ID, yytext());}
					case -146:
						break;
					case 151:
						{return tok(sym.ID, yytext());}
					case -147:
						break;
					case 152:
						{return tok(sym.ID, yytext());}
					case -148:
						break;
					case 153:
						{return tok(sym.ID, yytext());}
					case -149:
						break;
					case 154:
						{return tok(sym.ID, yytext());}
					case -150:
						break;
					case 155:
						{return tok(sym.ID, yytext());}
					case -151:
						break;
					case 156:
						{return tok(sym.ID, yytext());}
					case -152:
						break;
					case 157:
						{return tok(sym.ID, yytext());}
					case -153:
						break;
					case 158:
						{return tok(sym.ID, yytext());}
					case -154:
						break;
					case 159:
						{return tok(sym.ID, yytext());}
					case -155:
						break;
					case 160:
						{return tok(sym.ID, yytext());}
					case -156:
						break;
					case 161:
						{return tok(sym.ID, yytext());}
					case -157:
						break;
					case 162:
						{return tok(sym.ID, yytext());}
					case -158:
						break;
					case 163:
						{return tok(sym.DECIMAL_LITERAL, yytext());}
					case -159:
						break;
					case 164:
						{return tok(sym.ID, yytext());}
					case -160:
						break;
					case 165:
						{escape(yytext());}
					case -161:
						break;
					case 166:
						{return tok(sym.ID, yytext());}
					case -162:
						break;
					case 167:
						{return tok(sym.ID, yytext());}
					case -163:
						break;
					case 168:
						{return tok(sym.ID, yytext());}
					case -164:
						break;
					case 169:
						{return tok(sym.ID, yytext());}
					case -165:
						break;
					case 170:
						{return tok(sym.ID, yytext());}
					case -166:
						break;
					case 171:
						{return tok(sym.ID, yytext());}
					case -167:
						break;
					case 172:
						{return tok(sym.ID, yytext());}
					case -168:
						break;
					case 173:
						{return tok(sym.ID, yytext());}
					case -169:
						break;
					case 174:
						{return tok(sym.ID, yytext());}
					case -170:
						break;
					case 175:
						{return tok(sym.ID, yytext());}
					case -171:
						break;
					case 176:
						{return tok(sym.ID, yytext());}
					case -172:
						break;
					case 177:
						{return tok(sym.ID, yytext());}
					case -173:
						break;
					case 178:
						{return tok(sym.ID, yytext());}
					case -174:
						break;
					case 179:
						{return tok(sym.ID, yytext());}
					case -175:
						break;
					case 180:
						{return tok(sym.ID, yytext());}
					case -176:
						break;
					case 181:
						{return tok(sym.ID, yytext());}
					case -177:
						break;
					case 182:
						{return tok(sym.ID, yytext());}
					case -178:
						break;
					case 183:
						{return tok(sym.ID, yytext());}
					case -179:
						break;
					case 184:
						{return tok(sym.ID, yytext());}
					case -180:
						break;
					case 185:
						{return tok(sym.ID, yytext());}
					case -181:
						break;
					case 186:
						{return tok(sym.ID, yytext());}
					case -182:
						break;
					case 187:
						{return tok(sym.ID, yytext());}
					case -183:
						break;
					case 188:
						{return tok(sym.ID, yytext());}
					case -184:
						break;
					case 189:
						{return tok(sym.ID, yytext());}
					case -185:
						break;
					case 190:
						{return tok(sym.ID, yytext());}
					case -186:
						break;
					case 191:
						{return tok(sym.ID, yytext());}
					case -187:
						break;
					case 192:
						{return tok(sym.ID, yytext());}
					case -188:
						break;
					case 193:
						{return tok(sym.ID, yytext());}
					case -189:
						break;
					case 194:
						{return tok(sym.ID, yytext());}
					case -190:
						break;
					case 195:
						{return tok(sym.ID, yytext());}
					case -191:
						break;
					case 196:
						{return tok(sym.ID, yytext());}
					case -192:
						break;
					case 197:
						{return tok(sym.ID, yytext());}
					case -193:
						break;
					case 198:
						{return tok(sym.ID, yytext());}
					case -194:
						break;
					case 199:
						{return tok(sym.ID, yytext());}
					case -195:
						break;
					case 200:
						{return tok(sym.ID, yytext());}
					case -196:
						break;
					case 201:
						{return tok(sym.ID, yytext());}
					case -197:
						break;
					case 202:
						{return tok(sym.ID, yytext());}
					case -198:
						break;
					case 203:
						{return tok(sym.ID, yytext());}
					case -199:
						break;
					case 204:
						{return tok(sym.ID, yytext());}
					case -200:
						break;
					case 205:
						{return tok(sym.ID, yytext());}
					case -201:
						break;
					case 206:
						{return tok(sym.ID, yytext());}
					case -202:
						break;
					case 207:
						{return tok(sym.ID, yytext());}
					case -203:
						break;
					case 208:
						{return tok(sym.ID, yytext());}
					case -204:
						break;
					case 209:
						{return tok(sym.ID, yytext());}
					case -205:
						break;
					case 210:
						{return tok(sym.ID, yytext());}
					case -206:
						break;
					case 211:
						{return tok(sym.ID, yytext());}
					case -207:
						break;
					case 212:
						{return tok(sym.ID, yytext());}
					case -208:
						break;
					case 213:
						{return tok(sym.ID, yytext());}
					case -209:
						break;
					case 214:
						{return tok(sym.ID, yytext());}
					case -210:
						break;
					case 215:
						{return tok(sym.ID, yytext());}
					case -211:
						break;
					case 216:
						{return tok(sym.ID, yytext());}
					case -212:
						break;
					case 217:
						{return tok(sym.ID, yytext());}
					case -213:
						break;
					case 218:
						{return tok(sym.ID, yytext());}
					case -214:
						break;
					case 219:
						{return tok(sym.ID, yytext());}
					case -215:
						break;
					case 220:
						{return tok(sym.ID, yytext());}
					case -216:
						break;
					case 221:
						{return tok(sym.ID, yytext());}
					case -217:
						break;
					case 222:
						{return tok(sym.ID, yytext());}
					case -218:
						break;
					case 223:
						{return tok(sym.ID, yytext());}
					case -219:
						break;
					case 224:
						{return tok(sym.ID, yytext());}
					case -220:
						break;
					case 225:
						{return tok(sym.ID, yytext());}
					case -221:
						break;
					case 226:
						{return tok(sym.ID, yytext());}
					case -222:
						break;
					case 227:
						{return tok(sym.ID, yytext());}
					case -223:
						break;
					case 228:
						{return tok(sym.ID, yytext());}
					case -224:
						break;
					case 229:
						{return tok(sym.ID, yytext());}
					case -225:
						break;
					case 230:
						{return tok(sym.ID, yytext());}
					case -226:
						break;
					case 231:
						{return tok(sym.ID, yytext());}
					case -227:
						break;
					case 232:
						{return tok(sym.ID, yytext());}
					case -228:
						break;
					case 233:
						{return tok(sym.ID, yytext());}
					case -229:
						break;
					case 234:
						{return tok(sym.ID, yytext());}
					case -230:
						break;
					case 235:
						{return tok(sym.ID, yytext());}
					case -231:
						break;
					case 236:
						{return tok(sym.ID, yytext());}
					case -232:
						break;
					case 237:
						{return tok(sym.ID, yytext());}
					case -233:
						break;
					case 238:
						{return tok(sym.ID, yytext());}
					case -234:
						break;
					case 239:
						{return tok(sym.ID, yytext());}
					case -235:
						break;
					case 240:
						{return tok(sym.ID, yytext());}
					case -236:
						break;
					case 241:
						{return tok(sym.ID, yytext());}
					case -237:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
					}
				}
			}
		}
	}
}
