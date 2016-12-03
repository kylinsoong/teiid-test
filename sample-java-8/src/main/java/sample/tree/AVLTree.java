package sample.tree;

public class AVLTree<T extends Comparable<? super T>> {
    
    private static final int ALLOWED_IMBALANCE = 1;
    
    private AVLNode<T> root;
    
    public AVLTree() {
        this.root = null;
    }
    
    public void makeEmpty() {
        this.root = null;
    }
    
    public boolean isEmpty() {
        return this.root == null;
    }
    
    public void insert(T x) {
        this.root = insert(x, root);
    }

    public void remove(T x) {
        this.root = remove(x, root);
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
    
    

    public boolean contains(T x) {
        return contains(x, root);
    }
    
    public void printTree() {
        if(isEmpty())
            System.out.println("Empty tree");
        else
            printTree(root);
    }
    
    /**
     * Return the height of node t
     * @param t the node that roots the tree.
     * @return the height of node t, or -1, if null.
     */
    private int height(AVLNode<T> t) {
        return t == null ? -1 : t.height;
    }
    
    /**
     * balance a tree
     * @param t t is either balanced or within one of being balanced
     * @return the balanced tree
     */
    private AVLNode<T> balance(AVLNode<T> t) {
        
        if (t == null)
            return t;
        
        if (height(t.left) - height(t.right) > ALLOWED_IMBALANCE) {
            if (height(t.left.left) >= height(t.left.right)) {
                t = rotateWithLeftChild(t);
            } else {
                t = doubleWithLeftChild(t);
            }
        } else if(height(t.right) - height(t.left) > ALLOWED_IMBALANCE) {
            if (height(t.right.right) >= height(t.right.left)) {
                t = rotateWithRightChild(t);
            } else {
                t = doubleWithRightChild(t);
            }
        } 
        
        t.height = Math.max(height(t.left), height(t.right)) + 1 ;
        return t;
    }
    
    /**
     * Rotate binary tree node with left child.
     * For AVL trees, this is a single rotation for case 1.
     * Update heights, then return new root.
     */
    private AVLNode<T> rotateWithLeftChild(AVLNode<T> k2) {
        AVLNode<T> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        k2.height = Math.max(height(k2.left), height(k2.right)) + 1 ;
        k1.height = Math.max(height(k1.left), k2.height) + 1;
        return k1;
    }

    /**
     * Rotate binary tree node with right child.
     * For AVL trees, this is a single rotation for case 4.
     * Update heights, then return new root.
     */
    private AVLNode<T> rotateWithRightChild(AVLNode<T> k1) {
        AVLNode<T> k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        k1.height = Math.max(height(k1.left), height(k1.right)) + 1;
        k2.height = Math.max(height(k2.right), k1.height) + 1;
        return k2;
    }

    /**
     * Double rotate binary tree node: first left child
     * with its right child; then node k3 with new left child.
     * For AVL trees, this is a double rotation for case 2.
     * Update heights, then return new root.
     */
    private AVLNode<T> doubleWithLeftChild(AVLNode<T> k3) {
        k3.left = rotateWithRightChild(k3.left);
        return rotateWithLeftChild(k3);
    }

    /**
     * Double rotate binary tree node: first right child
     * with its left child; then node k1 with new right child.
     * For AVL trees, this is a double rotation for case 3.
     * Update heights, then return new root.
     */
    private AVLNode<T> doubleWithRightChild(AVLNode<T> k1) {
        k1.right = rotateWithLeftChild(k1.right);
        return rotateWithRightChild(k1);
    }

    /**
     * Internal method to insert into a subtree.
     * @param x the item to insert.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private AVLNode<T> insert(T x, AVLNode<T> t) {
        
        if( t == null) 
            return new AVLNode<>(x, null, null);
        
        int compareResult = x.compareTo(t.element);
        if(compareResult < 0) {
            t.left = insert(x, t.left);
        } else if(compareResult > 0) {
            t.right = insert(x, t.right);
        }
        
        return balance(t);
    }
    
    /**
     * Internal method to remove from a subtree.
     * @param x the item to remove.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private AVLNode<T> remove(T x, AVLNode<T> t) {
        
        if(t == null)
            return t;
        
        int compareResult = x.compareTo(t.element);
        if(compareResult < 0) {
            t.left = remove(x, t.left);
        } else if(compareResult > 0) {
            t.right = remove(x, t.right);
        } else if(t.left != null && t.right != null) {
            t.element = this.findMin(t.right).element;
            t.right = remove(t.element, t.right);
        } else {
            t = (t.left != null) ? t.left : t.right;
        }
        
        return balance(t);
    }
    
    /**
     * Internal method to find the smallest item in a subtree.
     * @param t the node that roots the tree.
     * @return node containing the smallest item.
     */
    private AVLNode<T> findMin(AVLNode<T> t) {
        
        if(t == null){
            return null;
        } else if(t.left == null){
            return t;
        }
        return findMin(t.left);
    }
    
    /**
     * Internal method to find the largest item in a subtree.
     * @param t the node that roots the tree.
     * @return node containing the largest item.
     */
    private AVLNode<T> findMax(AVLNode<T> t) {
        
        if(t == null) {
            return null ;
        } else if(t.right == null) {
            return t;
        }
        return findMax(t.right);
    }
    
    /**
     * Internal method to find an item in a subtree.
     * @param x is item to search for.
     * @param t the node that roots the tree.
     * @return true if x is found in subtree.
     */
    private boolean contains(T x, AVLNode<T> t) {
        
        if(t == null)
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
     * Internal method to print a subtree in sorted order.
     * @param t the node that roots the tree.
     */
    private void printTree(AVLNode<T> t) {

        if(t != null) {
            printTree(t.left);
            System.out.println(t.element);
            printTree(t.right);
        }
    }
}
