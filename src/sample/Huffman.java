package sample;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Comparator;

public class Huffman {
    private final String originalMessage;
    private String encodedStr;
    private String decodedStr;
    public HashMap<Character, Integer> hMapWordCount;  // for occurrence count
    public HashMap<Character, String> hMapCode; // for code(character/code)
    public HashMap<String, Character> hMapCodeR; // for code(code/character)
    private final PriorityQueue<Node> priorityQueue;  // for MinHeap
    private int counter;  // Unique id assigned to each node
    private int treeSize;  // # of total nodes in the tree
    private Node root;

    // Inner class
    private class Node {
        int uid, weight;
        char ch;
        Node left, right;

        // Constructor for class node
        private Node(Character ch, Integer weight, Node left, Node right){
            uid = ++counter;
            this.weight = weight;
            this.ch = ch;
            this.left = left;
            this.right = right;
        }
    }

    // Constructor for class Huffman
    public Huffman(String originalMessage){
        this.counter = 0;
        this.treeSize = 0;
        this.originalMessage = originalMessage;
        hMapWordCount = new HashMap<Character, Integer>();
        hMapCode = new HashMap<Character, String>();
        hMapCodeR = new HashMap<String, Character>();
        priorityQueue = new PriorityQueue<Node>(1, new Comparator<Node>() {
            @Override
            public int compare(Node n1, Node n2) {
                if (n1.weight < n2.weight)
                    return -1;
                else if (n1.weight > n2.weight)
                    return 1;
                return 0;
            }
        });

        countWord();  // Częstotliwość słów
        buildTree();  // Drzewo na podstawie częstotliwości występowania słów
        buildCodeTable();  // tabele kodujące
//        printCodeTables();
    }

    private void buildCodeTable(){
        String code = "";
        Node n = root;
        buildCodeRecursion(n, code);  // Recursion
    }

    private void buildCodeRecursion(Node n, String code){
        if (n != null){
            if (! isLeaf(n)){  // n = internal node
                buildCodeRecursion(n.left, code + '0');
                buildCodeRecursion(n.right, code + '1');
            }
            else{  // n = Leaf node
                hMapCode.put(n.ch, code); // for {character:code}
                hMapCodeR.put(code, n.ch); // for {code:character}
            }
        }
    }




    private void buildTree(){
        buildMinHeap();  // Set all leaf nodes into MinHeap
        Node left, right;
        while (! priorityQueue.isEmpty()){
            left = priorityQueue.poll(); treeSize++;
            if (priorityQueue.peek() != null){
                right = priorityQueue.poll();  treeSize++;
                root = new Node('\0', left.weight + right.weight, left, right);
            }
            else{  // only left child. right=null
                root = new Node('\0', left.weight, left, null);
            }

            if (priorityQueue.peek() != null){
                priorityQueue.offer(root);
            }
            else{  // = Top root. Finished building the tree.
                treeSize++;
                break;
            }
        }
    }

    private void buildMinHeap(){
        for (Map.Entry<Character, Integer> entry: hMapWordCount.entrySet()){
            Character ch = entry.getKey();
            Integer weight = entry.getValue();
            Node n = new Node(ch, weight, null, null);
            priorityQueue.offer(n);
        }
    }

    private void countWord(){
        Character ch;
        Integer weight;
        for (int i = 0; i< originalMessage.length(); i++){
            ch = new Character(originalMessage.charAt(i));
            if (hMapWordCount.containsKey(ch) == false)
                weight = new Integer(1);
            else
                weight = hMapWordCount.get(ch) + 1;
            hMapWordCount.put(ch, weight);
        }
    }

    private boolean isLeaf(Node n) {
        return (n.left == null) && (n.right == null);
    }

    public String encode(){
        StringBuilder sb = new StringBuilder();
        Character ch;
        for(int i = 0; i< originalMessage.length(); i++){
            ch = originalMessage.charAt(i);
            sb.append(hMapCode.get(ch));
        }
        encodedStr = sb.toString();
        return encodedStr;
    }

    public String decode(HashMap<String, Character> decodingTable, String encodedString){
        StringBuilder sb = new StringBuilder();
        String t = "";

        for(int i=0; i<encodedString.length(); i++){
            t += encodedString.charAt(i);
            if (decodingTable.containsKey(t)){
                sb.append(decodingTable.get(t));
                t = "";
            }
        }
        decodedStr = sb.toString();
        return decodedStr;
    }



    public static String readFile(String fname){
        StringBuilder sb = new StringBuilder();
        File filename = new File(fname);
        try (BufferedReader in = new BufferedReader(new FileReader(filename))){
            String line = in.readLine();
            while (line != null){
                sb.append(line + "\n");
                line = in.readLine();
            }
        }
        catch (IOException e){
            System.out.println(e);
        }
        return sb.toString();
    }
}
