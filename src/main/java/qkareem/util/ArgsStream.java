package qkareem.util;

import qkareem.classes.Arg;

class InputStream {
    private int pos;
    private String _input;

    public InputStream(String input) {
        _input = input;
    }

    public char peek() {
        if (_input.length() <= pos)
            return '\0';
        return _input.charAt(pos);
    }

    public char next() {
        char ch = peek();
        pos++;
        return ch;
    }

    public boolean eof() {
        return peek() == '\0';
    }
}

interface Predicate {
    boolean check(char ch);
}

public class ArgsStream {
    private String _input;
    private InputStream _inst;
    private Arg current;

    public ArgsStream(String input) {
        _input = input;
        _inst = new InputStream(input);
    }

    public String getOriginalContent() {
        return _input;
    }

    private String read(Predicate predicate) {
        String result = "";
        while (!_inst.eof() && predicate.check(_inst.peek()))
            result += _inst.next();
        return result;
    }

    public void skipWhitespaces() {
        read(new Predicate() {
            @Override
            public boolean check(char ch) {
                return Character.isWhitespace(ch);
            }
        });
    }

    public Arg readNext() {
        String value, type;
        skipWhitespaces();
        if (_inst.eof())
            return null;
        char ch = _inst.peek();
        if (ch == '"' || ch == '\'') {
            value = readEscaped(ch);
            type = "escaped";
            _inst.next();
        } else {
            value = read(new Predicate() {
                @Override
                public boolean check(char ch) {
                    return !Character.isWhitespace(ch);
                }
            });
            type = "plain";
        }

        return new Arg(type, value);
    }

    private String readEscaped(char end) {
        boolean escaped = false;
        String str = "";
        _inst.next();
        while (!_inst.eof()) {
            char ch = _inst.next();
            if (escaped) {
                str += ch;
                escaped = false;
            } else if (ch == '\\') {
                escaped = true;
            } else if (ch == end) {
                break;
            } else {
                str += ch;
            }
        }
        return str;
    }

    public Arg next() {
        Arg arg = current;
        current = null;
        return arg != null ? arg : readNext();
    };

    public Arg peek() {
        current = current != null ? current : readNext();
        return current;
    };

    public boolean eof() {
        return peek() == null;
    }
}