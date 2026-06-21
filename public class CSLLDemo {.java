public class CSLLDemo {

    static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
            this.next = null;
        }
    }

    public static class CSLL {
        Node list;

        CSLL() {
            list = null;
        }

        public void addToEmpty(int data) {
            Node newNode = new Node(data);
            list = newNode;
            newNode.next = list;
        }

        public void addLast(int data) {
            Node newNode = new Node(data);

            if (list == null) {
                addToEmpty(data);
            } else {
                newNode.next = list.next;
                list.next = newNode;
                list = newNode;
            }
        }

        public void addFirst(int data) {
            Node newNode = new Node(data);

            if (list == null) {
                addToEmpty(data);
            } else {
                newNode.next = list.next;
                list.next = newNode;
            }
        }

        public void delFirst() {
            if (list == null) {
                System.out.println("List is empty");
            } else if (list.next == list) {
                list = null;
            } else {
                list.next = list.next.next;
            }
        }

        public void delLast() {
            if (list == null) {
                System.out.println("List is empty");
            } else if (list.next == list) {
                list = null;
            } else {
                Node ptr = list.next;

                while (ptr.next != list) {
                    ptr = ptr.next;
                }

                ptr.next = list.next;
                list = ptr;
            }
        }

        public void display() {
            if (list == null) {
                System.out.println("List is empty");
                return;
            }

            Node temp = list.next;

            do {
                System.out.print(temp.data + " ");
                temp = temp.next;
            } while (temp != list.next);

            System.out.println();
        }
    }

    public static void main(String[] args) {
        CSLL list = new CSLL();

        System.out.println("Add Last:");
        list.addLast(21);
        list.addLast(53);
        list.addLast(95);
        list.display();

        System.out.println("Add First:");
        list.addFirst(10);
        list.display();

        System.out.println("Delete First:");
        list.delFirst();
        list.display();

        System.out.println("Delete Last:");
        list.delLast();
        list.display();
    }
}