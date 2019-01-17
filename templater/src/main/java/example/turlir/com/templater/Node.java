package example.turlir.com.templater;

import example.turlir.com.templater.check.Checker;
import example.turlir.com.templater.check.MinLimit;
import example.turlir.com.templater.check.NotEmpty;
import example.turlir.com.templater.check.TrueCheck;

class Node {

    final String type;

    final String name;

    final String hint;

    final String example;

    final String tag;

    final int position;

    private Checker checker;

    Node(String type, String name, String hint, String example, String tag, int index, boolean required) {
        this(type, name, hint, example, tag, index);
        if (required) {
            checker = new NotEmpty();
        } else {
            checker = new TrueCheck();
        }
    }

    Node(String type, String name, String hint, String example, String tag, int index, int min, int max) {
        this(type, name, hint, example, tag, index);
        checker = new MinLimit(min);
    }

    Node(String type, String name, String hint, String example, String tag, int index, String regex) {
        this(type, name, hint, example, tag, index);
        checker = new TrueCheck();
    }

    Node(String type, String name, String hint, String example, String tag, int index, Checker checker) {
        this(type, name, hint, example, tag, index);
        this.checker = checker;
    }

    private Node(String type, String name, String hint, String example, String tag, int index) {
        this.type = type;
        this.name = name;
        this.hint = hint;
        this.example = example;
        this.tag = tag;
        this.position = index;
    }

    Checker getChecker() {
        return checker;
    }
}
