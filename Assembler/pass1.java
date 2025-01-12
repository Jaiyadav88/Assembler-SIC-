package Assembler;

import java.util.*;
import Table.optab_info;
import Table.symtab_info;
import Table.block_info;
import Table.Table;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * 
 * Author: Jai Yadav
 * BTech CSE 3rd Year
 * Semester V
 * 
 */
public class pass1 {
    public static int block_number;
    public static String curr_block;
    public static int line;
    public static String locctr;
    public static Table t;
    public static boolean error_status = false;

    public static String dectohex(int dec) { // convert decimal to hexadecimal
        return Integer.toHexString(dec);
    }

    static int hextodec(String hex) { // convert hexadecimal to decimal
        return Integer.parseInt(hex, 16);
    }

    // to extarct words from the input.txt ans storing it in array word
    public static void extract(String s, String[] word, int count[]) {
        int i = 0;
        count[0] = 0;
        for (int j = 0; j < 5; j++)
            word[j] = "";
        while (i < s.length() && s.charAt(i) == ' ')
            i++;
        if (i == s.length() || s.charAt(i) == '.')
            return;
        String wd = "";
        for (; i < s.length();) {
            while (i < s.length() && s.charAt(i) == ' ')
                i++;
            if (i == s.length())
                break;
            while (i < s.length() && s.charAt(i) != ' ')
                wd += s.charAt(i++);
            word[count[0]++] = wd;
            wd = "";
        }
        // for(i=0;i<count[0];i++){ System.out.print(word[i]+" ");}
        // System.out.println();
    }

    public static void main(String args[]) {
        t = new Table();
        t.init(); // initialize the data structures
        locctr = "0";
        line = 1;
        try {
            BufferedReader input = new BufferedReader(new FileReader("input.txt"));
            BufferedWriter intermediate = new BufferedWriter(new FileWriter("intermediate.txt"));
            BufferedWriter error = new BufferedWriter(new FileWriter("error.txt"));
            String s;
            String[] word = new String[5];
            int count[] = { 0 };
            s = input.readLine();
            extract(s, word, count);
            if (count[0] == 0) {
                intermediate.write(line + "\n");
                intermediate.write("$" + "\n");
                intermediate.write(s + "\n");
                intermediate.newLine();
                intermediate.newLine();
                line++;
            }
            if (word[0].equals("START")) {
                locctr = word[1];
                intermediate.write(line + "\n");
                intermediate.newLine();
                intermediate.write("START" + "\n");
                intermediate.write(locctr.toUpperCase() + "\n");
                intermediate.write(locctr.toUpperCase() + "\n");
                intermediate.newLine();
                System.out.println(locctr.toUpperCase() + "  " + s);
            } else if (word[1].equals("START")) {
                locctr = word[2];
                intermediate.write(line + "\n");
                intermediate.write(word[0] + "\n");
                intermediate.write(word[1] + "\n");
                intermediate.write(locctr.toUpperCase() + "\n");
                intermediate.write(locctr.toUpperCase() + "\n");
                intermediate.newLine();
                System.out.println(locctr.toUpperCase() + "  " + s);
            } else
                implement(word, count, intermediate, error);
            t.BLOCK.put("DEFAULT", new block_info("DEFAULT", 0, locctr, "0"));
            curr_block = "DEFAULT";
            line = 1;
            while (true) {
                s = input.readLine();
                System.out.println(locctr.toUpperCase() + "  " + s);
                extract(s, word, count);
                line++;
                intermediate.write(line + "\n");
                if (count[0] == 0) {
                    intermediate.write("$" + "\n");
                    s = s.trim();
                    intermediate.write(s + "\n");
                    intermediate.newLine();
                    intermediate.newLine();
                    intermediate.newLine();
                    continue;
                }
                if (word[0].equals("END")) {
                    t.BLOCK.get(curr_block).length = locctr;
                    intermediate.newLine();
                    intermediate.write(word[0] + "\n");
                    intermediate.write(word[1] + "\n");
                    intermediate.write(locctr.toUpperCase() + "\n");
                    intermediate.newLine();
                    break;
                }
                implement(word, count, intermediate, error);
            }
            String addr = "";
            block_info temp = t.BLOCK.get("DEFAULT");
            ArrayList<String> blockname = new ArrayList<>();
            for (Map.Entry<String, block_info> entry : t.BLOCK.entrySet()) {
                blockname.add(entry.getKey());
            }
            addr = temp.start_address;
            String len = t.BLOCK.get("DEFAULT").length;
            for (int i = 0; i < blockname.size(); i++) {
                if (blockname.get(i).equals("DEFAULT"))
                    continue;
                temp = t.BLOCK.get(blockname.get(i));
                t.BLOCK.get(blockname.get(i)).start_address = dectohex(hextodec(addr) + hextodec(len));
                addr = t.BLOCK.get(blockname.get(i)).start_address;
                len = t.BLOCK.get(blockname.get(i)).length;
            }
            intermediate.close();
            error.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void implement(String word[], int count[], BufferedWriter intermediate, BufferedWriter error)
            throws Exception { // new block detected
        if (word[0].equals("USE")) {

            if (word[1].equals("")) {
                t.BLOCK.get(curr_block).length = locctr;
                curr_block = "DEFAULT"; // if no name is specified then it is default block
            } else {
                if (t.BLOCK.containsKey(word[1])) {
                    t.BLOCK.get(curr_block).length = locctr;
                    curr_block = word[1]; // if block already exists
                    locctr = t.BLOCK.get(word[1]).length;
                } else {
                    t.BLOCK.put(word[1], new block_info(word[1], block_number, locctr.toUpperCase(), "0"));
                    curr_block = word[1];
                    locctr = "0";
                    block_number++;
                }
                intermediate.newLine();
                intermediate.write(word[0] + "\n");
                intermediate.write(word[1] + "\n");
                intermediate.write(locctr.toUpperCase() + "\n");
                intermediate.newLine();
                return;
            }
        }
        if (word[0].charAt(0) == '+') {
            if (t.OPTAB.containsKey(word[0].substring(1))) {
                intermediate.newLine();
                intermediate.write(word[0] + "\n");
                intermediate.write(word[1] + "\n");
                intermediate.write(locctr.toUpperCase() + "\n");
                intermediate.write(locctr.toUpperCase() + "\n");
                locctr = dectohex(hextodec(locctr) + 4);
                return;
            } else {
                error.write(line + " Invalid Opcode\n");
                error_status = true;
                return;
            }
        }
        if (t.OPTAB.containsKey(word[0])) {
            intermediate.newLine();
            intermediate.write(word[0] + "\n");
            intermediate.write(word[1] + "\n");
            intermediate.write(locctr.toUpperCase() + "\n");
            locctr = dectohex(hextodec(locctr) + t.OPTAB.get(word[0]).format);
            intermediate.write(locctr.toUpperCase() + "\n");
            return;
        }
        if (!t.OPTAB.containsKey(word[0])) {
            // if label is present
            if (t.SYMTAB.containsKey(word[0])) {
                error.write(line + " Duplicate Label\n");
                error_status = true;
            } else {
                t.SYMTAB.put(word[0], new symtab_info(word[0], locctr.toUpperCase(), curr_block));
                intermediate.write(word[0] + "\n");
                intermediate.write(word[1] + "\n");
                intermediate.write(word[2] + "\n");
                intermediate.write(locctr.toUpperCase() + "\n");
                if (word[1].charAt(0) == '+') {
                    locctr = dectohex(hextodec(locctr) + 4);
                } else if (t.OPTAB.containsKey(word[1])) {
                    locctr = dectohex(hextodec(locctr) + t.OPTAB.get(word[1]).format);
                } else if (word[1].equals("RESW")) {
                    locctr = dectohex(hextodec(locctr) + 3 * Integer.parseInt(word[2]));
                } else if (word[1].equals("RESB")) {
                    locctr = dectohex(hextodec(locctr) + Integer.parseInt(word[2]));
                } else if (word[1].equals("BYTE")) {
                    if (word[2].charAt(0) == 'X') {
                        locctr = dectohex(hextodec(locctr) + (word[2].length() - 3) / 2);
                    } else if (word[2].charAt(0) == 'C') {
                        locctr = dectohex(hextodec(locctr) + word[2].length() - 3);
                    }
                } else if (word[1].equals("WORD")) {
                    locctr = dectohex(hextodec(locctr) + 3);
                } else {
                    error_status = true;
                    error.write(line + " Invalid Opcode\n");
                }
                intermediate.write(locctr.toUpperCase() + "\n");
            }
        }

    }
}
