import java.util.NoSuchElementException;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.Random;


public class RedBlackBST {

    private static final boolean RED   = true;
    private static final boolean BLACK = false;

    private Node root;     // root of the BST

    private int red;       //number of red nodes

    // BST helper node data type
    private class Node {
        private int key;           // key
        private Node left, right;  // links to left and right subtrees
        private boolean color;     // color of parent link
        private int size;          // subtree count

        public Node(int key, boolean color, int size) {
            this.key = key;
            this.color = color;
            this.size = size;
        }
    }

	public RedBlackBST() {
	}
   /***************************************************************************
    *  Node helper methods.
    ***************************************************************************/
    // is node x red; false if x is null ?
    private boolean isRed(Node x) {
        if (x == null) return false;
        return x.color == RED;
    }

    // number of node in subtree rooted at x; 0 if x is null
    private int size(Node x) {
        if (x == null) return 0;
        return x.size;
    } 


    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return size(root);
    }

   /**
     * Is this symbol table empty?
     * @return {@code true} if this symbol table is empty and {@code false} otherwise
     */
    public boolean isEmpty() {
        return root == null;
    }

   /***************************************************************************
    *  Red-black tree insertion.
    ***************************************************************************/

    /**
     * Inserts the specified key-value pair into the symbol table, overwriting the old 
     * value with the new value if the symbol table already contains the specified key.
     * Deletes the specified key (and its associated value) from this symbol table
     * if the specified value is {@code null}.
     *
     * @param key the key
     * @param val the value
     * @throws NullPointerException if {@code key} is {@code null}
     */
    public void put(int key) {

        root = put(root, key);
        //if root is red decrement number of red nodes
        if(root.color){
            red--;
        }
        root.color = BLACK;
    }

    // insert the key-value pair in the subtree rooted at h
    private Node put(Node h, int key) { 
        if (h == null){
            //increment number of red nodes
            red++;
            return new Node(key, RED, 1);
        }
        int cmp = key - h.key;
        if      (cmp < 0) h.left  = put(h.left,  key); 
        else if (cmp > 0) h.right = put(h.right, key); 
        else              h.key   = key;

        // fix-up any right-leaning links
        if (isRed(h.right) && !isRed(h.left))      h = rotateLeft(h);
        if (isRed(h.left)  &&  isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left)  &&  isRed(h.right))     flipColors(h);
        h.size = size(h.left) + size(h.right) + 1;

        return h;
    }

    /***************************************************************************
    *  Red-black tree percent red function.
    ***************************************************************************/



        //calculates percentage of red nodes in O(1)//

    public double percentRed(){
        double p1 = 100;
        double p2 = red;
        double p3 = size();

        return p1*p2/p3;
    }



   /***************************************************************************
    *  Red-black tree helper functions.
    ***************************************************************************/

    // make a left-leaning link lean to the right
    private Node rotateRight(Node h) {
        // assert (h != null) && isRed(h.left);
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = x.right.color;
        x.right.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }

    // make a right-leaning link lean to the left
    private Node rotateLeft(Node h) {
        // assert (h != null) && isRed(h.right);
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = x.left.color;
        x.left.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }

    // flip the colors of a node and its two children
    private void flipColors(Node h) {
        // h must have opposite color of its two children
        // assert (h != null) && (h.left != null) && (h.right != null);
        // assert (!isRed(h) &&  isRed(h.left) &&  isRed(h.right))
        //    || (isRed(h)  && !isRed(h.left) && !isRed(h.right));

        //adjust red count
        red--;

        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }


    /**
     * Unit tests the {@code RedBlackBST} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) { 
        RedBlackBST st = new RedBlackBST();
        
        if(args.length>0){
            try{
                File file = new File(args[0]);
                Scanner input = new Scanner(file);
                System.out.println("reading input from: "+args[0]);
                while (input.hasNextInt()) {
                    int in = input.nextInt();
                    st.put(in);
                }
                System.out.println("Percent of Red Nodes: "+st.percentRed()+"%");
            } catch (FileNotFoundException e){
                System.out.printf("file not found");  
                return;              
               
            }

        }else{

                Random rand = new Random();
                double hund = 100;

                //general test//
                double generallowest =100;
                double generalhighest =0;
                double generalaverage=0;


                //test 1//
                double test1lowest = 100;
                double test1highest=0;
                double test1average=0;
                for(int k=0; k<100; k++){
                    
                    st = new RedBlackBST();
                    while(st.size()<10000){
                        st.put(rand.nextInt());
                    }
                    if(st.percentRed()<test1lowest)test1lowest = st.percentRed();
                    if(st.percentRed()>test1highest)test1highest = st.percentRed();
                    test1average = test1average+st.percentRed();
                    
                }
                test1average = test1average/hund;
                generalaverage = generalaverage+test1average;
                if(test1lowest<generallowest)generallowest = test1lowest;
                if(test1highest>generalhighest)generalhighest = test1highest;
                System.out.println("for a tree of size 10^4 the lowest percent red is: "+test1lowest+"%");
                System.out.println("for a tree of size 10^4 the highest percent red is: "+test1highest+"%");
                System.out.println("for a tree of size 10^4 the average percent red is: "+test1average+"%");
                System.out.println();
                System.out.println();

                //test 2//
                double test2lowest = 100;
                double test2highest=0;
                double test2average=0;
                for(int k = 0; k<100; k++){
                    st = new RedBlackBST();
                    while(st.size()<100000){
                        
                        st.put(rand.nextInt());

                    }
                    if(st.percentRed()<test2lowest)test2lowest = st.percentRed();
                    if(st.percentRed()>test2highest)test2highest = st.percentRed();
                    test2average = test2average+st.percentRed();    
                }
                test2average = test2average/hund;
                generalaverage = generalaverage+test2average;
                if(test2lowest<generallowest)generallowest = test2lowest;
                if(test2highest>generalhighest)generalhighest = test2highest;

                System.out.println("for a tree of size 10^5 the lowest percent red is: "+test2lowest+"%");
                System.out.println("for a tree of size 10^5 the highest percent red is: "+test2highest+"%");
                System.out.println("for a tree of size 10^5 the average percent red is: "+test2average+"%");
                System.out.println();
                System.out.println();


                //test 3//
                double test3lowest = 100;
                double test3highest=0;
                double test3average=0;
                for(int k =0; k<100; k++){
                    st = new RedBlackBST();
                    while(st.size()<1000000){
                        st.put(rand.nextInt());

                    }
                    if(st.percentRed()<test3lowest)test3lowest = st.percentRed();
                    if(st.percentRed()>test3highest)test3highest = st.percentRed();
                    test3average = test3average+st.percentRed();
                }
                test3average = test3average/hund;
                generalaverage = generalaverage+test3average;
                if(test3lowest<generallowest)generallowest = test3lowest;
                if(test3highest>generalhighest)generalhighest = test3highest;
                System.out.println("for a tree of size 10^6 the lowest percent red is: "+test3lowest+"%");
                System.out.println("for a tree of size 10^6 the highest percent red is: "+test3highest+"%");
                System.out.println("for a tree of size 10^6  the average percent red is: "+test3average+"%");
                System.out.println();
                System.out.println();
                double t = 3;
                generalaverage = generalaverage/t;
                System.out.println("The lowest percentage throughout all tests is: "+generallowest+"%");
                System.out.println("The highest percentage throughout all tests is: "+generalhighest+"%");
                System.out.println("The average percentage throughout all tests is: "+generalaverage+"%");

        }

        
    }
        
 
}

/******************************************************************************
 *  Copyright 2002-2016, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
