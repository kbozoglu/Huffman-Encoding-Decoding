package Jaq.finalHw;

import java.util.HashMap;

/**
 * Created by kbozoglu on 3/18/17.
 */
public class Hauffman {

    HashMap<String, Integer> letterCount = new HashMap<>();
    HashMap<String, Node> nodes = new HashMap<>();
    HashMap<String, String> encoderMap = new HashMap<>();
    Node root;
    String s;
    String encodedS;
    boolean show;

    public Hauffman(String s, boolean show){
        this.s = s;
        this.show = show;
    }

    protected class Node {
        int data;
        Node left;
        Node right;
        Node parent;
        String name;

        protected Node(int data, String name) {
            this.data = data;
            this.name = name;
        }
    }

    private String[] find2min(HashMap<String, Integer> map){
        int min1 = Integer.MAX_VALUE, min2 = Integer.MAX_VALUE;
        String[] mins = new String[2];

        for(String key : map.keySet()){
             int val = (int)map.get(key);
            if(val < min1){
                min2 = min1;
                min1 = val;
                mins[1] = mins[0];
                mins[0] = key;
            }
            else if(val < min2){
                min2 = val;
                mins[1] = key;
            }
        }
        return mins;
    }

    private void fillBeginingNodes(HashMap<String, Integer> map){
        for(String key : map.keySet()){
            Node node = new Node(map.get(key), key);
            nodes.put(key, node);
        }
    }

    private void printMaps(){
        System.out.println("letterCount: " + letterCount);
        System.out.print("nodes: ");
        for(String key: nodes.keySet()){
            System.out.print(key + ":" + nodes.get(key).data + " ");
        }
    }

    private void printTree(Node head){
        if(head == null) return;
        System.out.print(head.name + head.data + " ");
        printTree(head.left);
        printTree(head.right);
    }


    private Node buildHeap(){
        fillBeginingNodes(letterCount);
        String[] mins = find2min(letterCount);
        if(mins[1] == null){
            nodes.put(mins[0], new Node(letterCount.get(mins[0]), mins[0]));
            root = nodes.get(mins[0]);
        }
        while(!(mins[1] == null)){
//            printMaps();
            mins = find2min(letterCount);
            if(mins[1] != null){
                nodes.put(mins[0] + mins[1], new Node(letterCount.get(mins[0]) + letterCount.get(mins[1]), mins[0] + mins[1]));
                nodes.get(mins[0] + mins[1]).left = nodes.get(mins[0]);
                nodes.get(mins[0] + mins[1]).right = nodes.get(mins[1]);
                nodes.get(mins[0]).parent = nodes.get(mins[0] + mins[1]);
                nodes.get(mins[1]).parent = nodes.get(mins[0] + mins[1]);
                root = nodes.get(mins[0] + mins[1]);

                letterCount.put(mins[0] + mins[1], letterCount.get(mins[0]) + letterCount.get(mins[1]));
                letterCount.remove(mins[0]);
                letterCount.remove(mins[1]);
            }
        }
        createEncoderMap(root, "");
//        printTree(root);
        return root;
    }

    private void createEncoderMap(Node root, String s){
        if(root == null) return;
        if(root.left == null && root.right == null) encoderMap.put(root.name, s);
        else{
            if(root.left != null) createEncoderMap(root.left, s + "0");
            if(root.right != null) createEncoderMap(root.right, s + "1");
        }
    }

    private void createLetterCountMap(String s){
        for(int i = 0; i < s.length(); i++){
            if(letterCount.get("" + s.charAt(i)) == null){
                letterCount.put(""+ s.charAt(i), 1);
            }
            else{
                letterCount.put("" + s.charAt(i), (letterCount.get("" + s.charAt(i)) + 1));
            }
        }
    }

    public String encode(){
        createLetterCountMap(s);
        System.out.println("begining");
//        printMaps();
        buildHeap();
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < s.length(); i++){
            result.append(encoderMap.get("" + s.charAt(i)));
        }
        encodedS = result.toString();
        System.out.println("result: " + result.toString());
        return result.toString();
    }

    public String decode(){
        Node node = root;
        StringBuilder result = new StringBuilder();
        int i = 0;
        while( i < encodedS.length()){
            if(node.left == null && node.right == null){
                if(node == root) break;
                result.append(node.name);
                node = root;
            }
            else{
                if(encodedS.charAt(i) == '0') node = node.left;
                else if(encodedS.charAt(i) == '1') node = node.right;
                i++;
            }
        }
        result.append(node.name);
        System.out.println(result.toString());
        return result.toString();
    }

}
