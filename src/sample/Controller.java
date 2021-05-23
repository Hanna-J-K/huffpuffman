package sample;

import javafx.event.ActionEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Controller {

    boolean show = true;
    String orgFile;
    HashMap<String, Character> decodingTableMap = new HashMap<>();
    String encodedFile;
    String decodedFile;
    Huffman createdHuffman;


    public void ButtonReadMessageAction(ActionEvent actionEvent) {

        System.out.println("\nJESZCZE RAZ:");
        System.out.println("#######");
        printCodeTables();
        System.out.println("No i co :(");
        decodedFile = createdHuffman.decode(decodingTableMap, encodedFile);

        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            SaveFile(decodedFile, file);
        }
    }


    public void ButtonSelectFileAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        String fileContent;


        if (selectedFile != null) {
            fileContent = selectedFile.getAbsolutePath();
            orgFile = Huffman.readFile(fileContent);
            System.out.println("##################");
            System.out.println("File content");
            System.out.println(orgFile);
            System.out.println("#############");
            createdHuffman = createHuffmanTree(orgFile, show, decodingTableMap);
            System.out.println("decoding table: ");
            printCodeTables();
            System.out.println("#######");

            System.out.println("Encoded message");
            encodedFile = createdHuffman.encode();
            System.out.println(encodedFile);
        }


//

    }


    public Huffman createHuffmanTree(String originalMessage, boolean show, HashMap<String, Character> decodingTableMap) {
        Huffman huffman = new Huffman(originalMessage);

        if (show) {
            System.out.println("\n============= Word Frequency =============");
            for (Map.Entry<Character, Integer> entry : huffman.hMapWordCount.entrySet()) {
                String key = entry.getKey().toString();
                int val = entry.getValue();
                if (key.equals("\n"))
                    key = "\\n";
                System.out.println(key + " occurs " + val + " times");
            }

            System.out.println("\n========== Huffman Code for each character =============");
            for (Map.Entry<Character, String> entry : huffman.hMapCode.entrySet()) {
                String key = entry.getKey().toString();
                String val = entry.getValue();
                if (key.equals("\n"))
                    key = "\\n";
                System.out.println(key + ": " + val);
            }
            System.out.println();
        }


        decodingTableMap.putAll(huffman.hMapCodeR);

        return huffman;

    }

    public static void executeHuffman(String orgStr, boolean show, HashMap<String, Character> decodingTableMap) {
        System.out.print("* Builiding Huffman Tree and Code Tables...");
        Huffman h = new Huffman(orgStr);
        System.out.println(" DONE");

        if (show) {
            System.out.println("\n============= Word Frequency =============");
            for (Map.Entry<Character, Integer> entry : h.hMapWordCount.entrySet()) {
                String key = entry.getKey().toString();
                int val = entry.getValue();
                if (key.equals("\n"))
                    key = "\\n";
                System.out.println(key + " occurs " + val + " times");
            }

            System.out.println("\n========== Huffman Code for each character =============");
            for (Map.Entry<Character, String> entry : h.hMapCode.entrySet()) {
                String key = entry.getKey().toString();
                String val = entry.getValue();
                if (key.equals("\n"))
                    key = "\\n";
                System.out.println(key + ": " + val);
            }
            System.out.println();
        }
        System.out.println("Original string: ");
        System.out.println(orgStr);
        System.out.println("#####################");

        System.out.print("* Encoding the text...");
        System.out.println("Encoded text:");
        String e = h.encode();

        System.out.println(e);
        System.out.println("######################");
        System.out.println(" DONE");

        System.out.print("* Decoding the encoded text...");
        String d = h.decode(decodingTableMap, e);
        myAssert(orgStr.equals(d));   // Check if original text and decoded text is exactly same
        System.out.println("Decoded text:");
        System.out.println(d);
        System.out.println("##################");
        System.out.println(" DONE");

        double sl = orgStr.length() * 7;
        double el = e.length();
        System.out.println("\n========== RESULT ==========");
        System.out.println("Original string cost = " + (int) sl + " bits");
        System.out.println("Encoded  string cost = " + (int) el + " bits");
        double r = ((el - sl) / sl) * 100;
        System.out.println("% reduction = " + (-r));
    }

    public static void myAssert(boolean x) {
        if (!x) {
            throw new IllegalArgumentException("Assert fail");
        }
    }

    private void SaveFile(String content, File file) {
        try {
            FileWriter fileWriter;

            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void printCodeTables() {
        System.out.println("\nHASH CODE RR");
        decodingTableMap.forEach((key, value) -> System.out.println(key + " " + value));
        System.out.println("############");

        //implementacje encoding drzewa
//    public class Tree {
//
//        }
//
//        public class Node {
//
//        }
//
//        public class Leaf {
//
//        }
    }
}
