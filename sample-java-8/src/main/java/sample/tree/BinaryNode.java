package sample.tree;

public class BinaryNode<T> {
    T element;
    BinaryNode<T> left;
    BinaryNode<T> right;
    
    BinaryNode(T element){
        this(element, null, null);
    }
    
    BinaryNode(T element, BinaryNode<T> left, BinaryNode<T> right){
        this.element = element;
        this.left = left;
        this.right = right;
    }
}
