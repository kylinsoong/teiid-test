package sample.tree;

public class BinarySearchTree<T extends Comparable<? super T>> {

    private BinaryNode<T> root;
    
    public BinarySearchTree() {
        this.root = null;
    }
    
    public void makeEmpty() {
        this.root = null;
    }
    
    public boolean isEmpty() {
        return this.root == null ;
    }
    
    public boolean contains(T x) {
        return contains(x, root);
    }
    
    public T findMin() {
        if(isEmpty())
            throw new UnderflowException();
        return findMin(root).element;
    }

    public T findMax() {
        if(isEmpty())
            throw new UnderflowException();
        return findMax(root).element;
    }
    
    public void insert(T x) {
        this.root = insert(x, root);
    }

    public void remove(T x) {
        this.root = remove(x, root);
    }
    
    public void printTree() {
        
        if(isEmpty())
            System.out.println("Empty tree");
        else
            printTree(root);
    }

    /**
     * Internal method to find an item in a subtree
     * @param x is item to search for
     * @param t the node that roots the subtree
     * @return true if the item is found; false otherwise
     */
    private boolean contains(T x, BinaryNode<T> t) {
        
        if(null == t)
            return false;
        
        int compareResult = x.compareTo(t.element);
        if(compareResult < 0) {
            return contains(x, t.left);
        } else if(compareResult > 0) {
            return contains(x, t.right);
        } else {
            return true;
        }
    }
    
    /**
     * Internal method to find the smallest item in a subtree
     * @param t the node that roots the subtree
     * @return node contain the smallest item
     */
    private BinaryNode<T> findMin(BinaryNode<T> t) {
        
        if(t == null) {
            return null;
        } else if (t.left == null) {
            return t;
        }
        
        return findMin(t.left);
    }
    
    /**
     * Internal method to find the largest item in a subtree
     * @param t the node that roots the subtree
     * @return node contain the largest item
     */
    private BinaryNode<T> findMax(BinaryNode<T> t) {
        
        if(t == null) {
            return null;
        } else if(t.right == null) {
            return t;
        }
        
        return findMax(t.right);
    }
    
    /**
     * Internal method to insert into a subtree
     * @param x the item to insert
     * @param t the node that roots the subtree
     * @return the new root of the subtree
     */
    private BinaryNode<T> insert(T x, BinaryNode<T> t) {
        
        if(null == t)
            return new BinaryNode<>(x, null, null);
        
        int compareResult = x.compareTo(t.element);
        if(compareResult < 0) {
            t.left = insert(x, t.left);
        } else if(compareResult > 0) {
            t.right = insert(x, t.right);
        } 

        return t;
    }
    
    /**
     * Internal method to remove from a subtree
     * @param x the item to remove
     * @param t the node that roots the subtree
     * @return the new root of the subtree
     */
    private BinaryNode<T> remove(T x, BinaryNode<T> t) {
        
        if(null == t)
            return t;
        
        int compareResult = x.compareTo(t.element);
        if(compareResult < 0) {
            t.left = remove(x, t.left);
        } else if (compareResult > 0) {
            t.right = remove(x, t.right);
        } else if(t.left != null && t.right != null) {
            t.element = findMin(t.right).element;
            t.right = remove(t.element, t.right);
        } else {
            t = (t.left != null) ? t.left : t.right;
        }
        
        return t;
    }
    
    /**
     * Internal method to print a subtree in sorted order
     * @param t the node that roots the subtree
     */
    private void printTree(BinaryNode<T> t) {
        if(t != null) {
            printTree(t.left);
            System.out.println(t.element);
            printTree(t.right);
        }
    }
}
