package sample.java8.interview;

public class ReverseListNode {
    
    public static class ListNode {
        Integer value;
        ListNode next;
        
        public ListNode(Integer value){
            this.value = value;
        }
        
        @Override
        public String toString() {
            if(null == value) {
                return "";
            } else {
                return value + formNext(next);
            }
        }

        private String formNext(ListNode next) {
            String result = "";
            if(next == null) {
                return result;
            } 
            while(true){
                result += ", ";
                result += next.value != null ? String.valueOf(next.value) : " " ; 
                if(next.next == null){
                    break;
                } else {
                    next = next.next;
                }
            }
            return result;
        }
    }

    public static void main(String[] args) {
        
        System.out.println(sampleNode());
                
        System.out.println(reverse(sampleNode()));
        
        System.out.println(partition(sampleNode(), 3));
    }
    
    public static ListNode partition(ListNode head, int n){
        if(head == null||n <= 0){
            return null;
        }
        ListNode first = head;
        ListNode second = null;
        for(int i = 0;i < n-1;i++){
            if(first.next != null){
                first = first.next;
            }else{
                return null;
            }
        }
        second = head;
        while(first.next != null){
            first = first.next;
            second = second.next;
        }
        return second;
    }

    public static ListNode reverse(ListNode head) {
        ListNode prev = null;
        ListNode node = null;
        ListNode current = head;
        while(current != null){
            ListNode next = current.next;
            if(next == null){
                node = current;
            }
            current.next = prev;
            prev = current;
            current = next;
        }
        return node;
    }
    
    public static ListNode reverse(int n, ListNode head) {
        ListNode node = null;
        
        return node;
    }

    private static ListNode sampleNode() {
        ListNode a = new ListNode(1);
        ListNode b = new ListNode(2);
        ListNode c = new ListNode(3);
        ListNode d = new ListNode(4);
        ListNode e = new ListNode(5);
        a.next = b;
        b.next = c;
        c.next = d;
        d.next = e;
        return a;
    }

}
