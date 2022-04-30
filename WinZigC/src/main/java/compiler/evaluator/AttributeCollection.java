package compiler.evaluator;

public class AttributeCollection {
    private int next;
    private int top;
    private DataType type;

    public AttributeCollection(int next, int top, DataType type) {
        this.next = next;
        this.top = top;
        this.type = type;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }
}
