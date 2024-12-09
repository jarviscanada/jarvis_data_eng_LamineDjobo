package ca.jrvs.practice;

import javax.xml.soap.Node;

public class LinkedList {
    Node head;

    class Node {
        int data;
        Node next;

        public Node (int data) {
            this.data = data;
            this.next = null;
        }
    }

    public Node reverseList(Node head){
        if(head == null) return head;

        Node current = head;
        Node previous = null;
        Node next = null;

        while(current != null) {
            next = current.next;
            current = previous;
            previous = current.next;
            current = next;
        }
        return previous;
    }
}
