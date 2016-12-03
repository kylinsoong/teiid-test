package sample.tree;

public class AVLNode<T> {
    T element;
    AVLNode<T> left;
    AVLNode<T> right;
    int height;
    
    AVLNode(T element) {
        this(element, null, null);
    }
    
    AVLNode(T element, AVLNode<T> left, AVLNode<T> right) {
        this.element = element;
        this.left = left;
        this.right = right;
        this.height = 0;
    }

    @Override
    public String toString() {
        return String.valueOf(element);
    }
}
