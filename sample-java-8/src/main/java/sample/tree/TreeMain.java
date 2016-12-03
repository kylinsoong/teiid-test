package sample.tree;

public class TreeMain {

    public static void main(String[] args) {
        
        test_binarySearchTree_insert();
        
        test_binarySearchTree_contains();
        
        test_binarySearchTree_findMin();
        
        test_binarySearchTree_finaMax();
        
        test_binarySearchTree_remove_1();
        
        test_binarySearchTree_remove_2();
        
        test_binarySearchTree_remove_3();
        
        test_binarySearchTree_print();
        
    }
    
    static BinarySearchTree<Integer> sample2() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<Integer>();
        tree.insert(6);
        tree.insert(2);
        tree.insert(1);
        tree.insert(5);
        tree.insert(3);
        tree.insert(4);
        tree.insert(8);
        return tree;
    }
    
    static BinarySearchTree<Integer> sample1() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<Integer>();
        tree.insert(6);
        tree.insert(2);
        tree.insert(1);
        tree.insert(4);
        tree.insert(3);
        tree.insert(8);
        return tree;
    }

    static void test_binarySearchTree_insert() {

        BinarySearchTree<Integer> tree = sample1();
        tree.insert(5);
    }

    static void test_binarySearchTree_contains() {

        BinarySearchTree<Integer> tree = sample1();
        System.out.println(tree.contains(8));
        System.out.println(tree.contains(5));
    }

    static void test_binarySearchTree_findMin() {
        BinarySearchTree<Integer> tree = sample1();
        System.out.println(tree.findMin());   
    }

    static void test_binarySearchTree_finaMax() {

        BinarySearchTree<Integer> tree = sample1();
        System.out.println(tree.findMax());
    }

    static void test_binarySearchTree_remove_1() {
        BinarySearchTree<Integer> tree = sample1();
        tree.remove(4);      
    }
    
    static void test_binarySearchTree_remove_2() {
        BinarySearchTree<Integer> tree = sample1();
        tree.remove(2);      
    }
    
    static void test_binarySearchTree_remove_3() {
        BinarySearchTree<Integer> tree = sample2();
        tree.remove(2);      
    }

    static void test_binarySearchTree_print() {

        BinarySearchTree<Integer> tree = sample1();
        tree.printTree();
    }

}
