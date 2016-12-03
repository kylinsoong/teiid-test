package sample.tree;

public class AVLTreeMain {

    public static void main(String[] args) {

        AVLTree<Integer> tree = new AVLTree<>();
        tree.insert(3);
        tree.insert(2);
        tree.insert(1);
        
        tree.insert(4);
        tree.insert(5);
        
        tree.insert(6);
        
        tree.insert(7);
        
        tree.insert(16);
        tree.insert(15);
        
        tree.insert(14);
        
        tree.insert(13);
        
        tree.insert(12);
        
        tree.insert(11);
    }

}
