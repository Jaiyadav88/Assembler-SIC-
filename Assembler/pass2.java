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
 * Author: Jai
 * BTech CSE 3rd Year
 * Semester V
 * 
 */

class pass2 {
    static int text_length=0;
    static String text_start="";
    static String text_end="";
    static boolean immediate;
    static boolean indirect;
    static int base=Integer.MAX_VALUE;
    public static void getinput(String a[],BufferedReader intermediate) throws Exception{
        String s="";
        for(int i=0;i<6;i++){
            s=intermediate.readLine();
            if(s==null||s.equals(".")) a[i]=" ";
            else a[i]=s;
        }
        //intermediate.close();
    } 
    public static String extendaddress(int dig,String s)
    {
        String ans="";
        for(int i=0;i<dig-s.length();i++) ans+="0";
        return ans+s;
    }
    public static void modify_object_file(BufferedWriter object) throws Exception{
        BufferedReader modification=new BufferedReader(new FileReader("modification.txt"));
        String s="";
        while(true)
        {
            s=modification.readLine();
            if(s==null||s.equals("")) break;
            object.write(s+"\n");
        }
        modification.close();
    }
    public static void main(String args[]) throws Exception{

        final int LINE_WIDTH = 5;
        final int LOC_WIDTH = 6;
        final int BLOCK_WIDTH = 6;
        final int STATEMENT_WIDTH = 25;
        final int OBJECT_CODE_WIDTH = 12;


        String start=""; //to store the starting address
        pass1 p1 = new pass1();
        p1.main(args);//calling pass1 assembler
        BufferedReader intermediate=new BufferedReader(new FileReader("intermediate.txt"));
        BufferedWriter object=new BufferedWriter(new FileWriter("object.txt"));
        BufferedWriter list=new BufferedWriter(new FileWriter("list.txt"));
        BufferedWriter modification=new BufferedWriter(new FileWriter("modification.txt"));
        BufferedWriter error=new BufferedWriter(new FileWriter("error.txt"));
        list.write(String.format(
        "%-" + LINE_WIDTH + "s %-"+ LOC_WIDTH +"s %-"+ BLOCK_WIDTH +"s %-"+ STATEMENT_WIDTH +"s %-"+ OBJECT_CODE_WIDTH +"s\n",
        "Line", "Loc", "Block", "Source Statement", "Object Code"
         ));
        list.write("-----------------------------------------------------------------------\n");
        //list.close();
        String a[]=new String[6]; //to extract information from intermediate file
        getinput(a, intermediate);
        // for(String s:a) System.out.println(s+" ");
        pass1.curr_block="DEFAULT";
        pass1.block_number=0;
        while(a[1].equals("$"))
        {
            list.write(String.format(
                "%-" + LINE_WIDTH + "s %-"+ LOC_WIDTH +"s %-"+ BLOCK_WIDTH +"s %-"+ STATEMENT_WIDTH +"s %-"+ OBJECT_CODE_WIDTH +"s\n",
                a[0], "", "", a[2], ""
            ));
            getinput(a, intermediate);
        }
        if(a[2].equals("START")){
            object.write("H^");
            list.write(String.format(
                "%-" + LINE_WIDTH + "s %-"+ LOC_WIDTH +"s %-"+ BLOCK_WIDTH +"s %-"+ STATEMENT_WIDTH +"s %-"+ OBJECT_CODE_WIDTH +"s\n",
                a[0], extendaddress(4, a[3]), pass1.block_number, a[1] + " " + a[2], ""
            ));
            int i=0;
            for(;i<a[1].length();i++) object.write(a[1].charAt(i));
            for(;i<6;i++) object.write(" ");
            ArrayList<String> al=new ArrayList<String>(); 
            for(Map.Entry<String, block_info> entry : pass1.t.BLOCK.entrySet())
                al.add(entry.getKey());
            block_info b1=pass1.t.BLOCK.get(al.get(al.size()-1));
            String len=pass1.dectohex(pass1.hextodec(b1.start_address)+pass1.hextodec(b1.length)-pass1.hextodec(a[3]));
            object.write("^");
            for(int j=0;j<a[3].length();j++) object.write(Character.toUpperCase(a[3].charAt(j)));
            for(int k=0;k<6-a[3].length();k++) object.write("0");
            object.write("^");
            for(int l=0;l<6-len.length();l++) object.write("0");
            for(int m=0;m<len.length();m++) object.write(Character.toUpperCase(len.charAt(m)));
            object.write("\n");
            start=a[3];
       }
       else{
          pass1.error_status=true;
          error.write("Error: START not found\n");
       }

       while(true)
       {    
            getinput(a, intermediate);
            if(a[1].equals("$"))
            {
                list.write(String.format(
                "%-" + LINE_WIDTH + "s %-"+ LOC_WIDTH +"s %-"+ BLOCK_WIDTH +"s %-"+ STATEMENT_WIDTH +"s %-"+ OBJECT_CODE_WIDTH +"s\n",
                a[0], "", "", a[2], ""
                ));
                continue;
            }
            if(a[2].equals("END"))
            {
                list.write(String.format(
                    "%-"+LINE_WIDTH+"s %-"+ LOC_WIDTH +"s %-"+ BLOCK_WIDTH +"s %-"+ STATEMENT_WIDTH +"s %-"+ OBJECT_CODE_WIDTH +"s\n",
                    a[0],"","",a[1]+" "+a[2],""
                ));
                if(text_length>0)
                {
                    object.write(text_start+"^"+extendaddress(2,pass1.dectohex(text_length/2))+"^"+text_end+"\n");
                }
                text_length=0;
                text_start="";
                text_end="";
                modification.close();
                modify_object_file(object);
                object.write("E^"+extendaddress(6,start)+"\n");
                break;
            }
            assemble(a, object, list, modification, error);
       }
        list.close();
        object.close();
        error.close();
    }
    public static void assemble(String a[], BufferedWriter object, BufferedWriter list, BufferedWriter modification, BufferedWriter error) throws Exception{
        String object_code;
        String locctr="";
        int format;
        if(a[2].equals("USE")){
            pass1.curr_block=a[3];
            pass1.block_number=pass1.t.BLOCK.get(a[3]).block_number;
            list.write(String.format(
            "%-" + 5 + "s %-"+ 6 +"s %-"+ 6 +"s %-"+ 25 +"s %-"+ 12 +"s\n",
            a[0], "0000", pass1.block_number, a[2] + " " + a[3], ""
            ));
            if(text_length>0)
                object.write(text_start+"^"+extendaddress(2,pass1.dectohex(text_length/2))+"^"+text_end+"\n");
            text_length=0;
            text_start="";
            text_end="";
            return;
        }
        if(a[2].equals("RESB")||a[2].equals("RESW")){
            list.write(String.format(
                "%-" + 5 + "s %-"+ 6 +"s %-"+ 6 +"s %-"+ 25 +"s %-"+ 12 +"s\n",
                a[0], extendaddress(4, a[4]), pass1.block_number, a[2] + " " + a[3], ""
            ));
            if(text_length>0)
                object.write(text_start+"^"+extendaddress(2,pass1.dectohex(text_length/2).toUpperCase())+"^"+text_end+"\n");
            text_length=0;
            text_start="";
            text_end="";
            return;
        }
        immediate=false;
        indirect=false;
        object_code=gen_code(a,modification);
        object_code=object_code.toUpperCase();
        if(a[2].equals("BYTE")||a[2].equals("WORD"))
            list.write(String.format(
                "%-5s %-6s %-6s %-25s %-12s\n",
                a[0], extendaddress(4, a[4]), pass1.block_number,
                (a[1] + " " + a[2] + " " + a[3]).trim(), "" // Trim removes extra spaces if a[1] is empty
            ));
        else{
            if(immediate) 
                list.write(String.format(
                    "%-5s %-6s %-6s %-25s %-12s\n",
                    a[0], extendaddress(4, a[4]), pass1.block_number,
                    (a[1] + " " + a[2] + " #" + a[3]).trim(), object_code
                ));
            else if(indirect) 
                list.write(String.format(
                    "%-5s %-6s %-6s %-25s %-12s\n",
                    a[0], extendaddress(4, a[4]), pass1.block_number,
                    (a[1] + " " + a[2] + " @" + a[3]).trim(), object_code
                ));
            else
                list.write(String.format(
                    "%-5s %-6s %-6s %-25s %-12s\n",
                    a[0], extendaddress(4, a[4]), pass1.block_number,
                    (a[1] + " " + a[2] + " " + a[3]).trim(), object_code
                ));
        }
        if(text_start.equals("")){
            locctr=pass1.dectohex(pass1.hextodec(pass1.t.BLOCK.get(pass1.curr_block).start_address)+pass1.hextodec(a[4]));
            text_start="T^"+extendaddress(6,locctr).toUpperCase();
            text_end=object_code;
            text_length=object_code.length();
        }
        else if(text_length+object_code.length()<=60){
            text_end+="^"+object_code;
            text_length+=object_code.length();
        }
        else{
            object.write(text_start+"^"+extendaddress(2,pass1.dectohex(text_length/2).toUpperCase())+"^"+text_end+"\n");
            locctr=pass1.dectohex(pass1.hextodec(pass1.t.BLOCK.get(pass1.curr_block).start_address)+pass1.hextodec(a[4]));
            text_start="T^"+extendaddress(6,locctr).toUpperCase();
            text_end=object_code;
            text_length=object_code.length();
        }
        if(a[2].equals("LDB")){
            if (a[3].charAt(0)=='#'){
                int immediateValue = Integer.parseInt(a[3].substring(1));
                base = immediateValue;
            } else { 
                symtab_info symbol = pass1.t.SYMTAB.get(a[3]);
                if (symbol == null) {
                    System.err.println("Error: Undefined symbol " + a[3]);
                    pass1.error_status = true; // Set an error flag
                    return; // Skip processing
                }
                // Calculate the base using symbol's address and block's start address
                base = pass1.hextodec(symbol.label_address) +
                       pass1.hextodec(pass1.t.BLOCK.get(symbol.block).start_address);
            }
        }
    }
    public static String gen_code(String a[],BufferedWriter modification) throws Exception
    {
        String ob1="",ob2="",ob3="";
        String operand_add="";
        String program_counter="";
        int format=3;
        if(a[2].equals("BYTE"))
        {
            int i;
            ob1="";
            if(a[3].charAt(0)=='X') for(i=2;i<a[3].length()-1;i++) ob1+=a[3].charAt(i);
            else for(i=2;i<a[3].length()-1;i++) ob1+=Integer.toHexString(a[3].charAt(i));
            return ob1;
        }
        if(a[2].equals("WORD"))
        {
           ob1=pass1.dectohex(Integer.parseInt(a[3]));
           return ob1;
        }
        if(a[2].equals("RSUB")){
            return "4F0000";
        }
        if(a[2].equals("+RSUB")){
            return "4F000000";
        }
        if(a[2].charAt(0)=='+')
        {
            format=4;
            a[2]=a[2].substring(1);//trim for opcode
        }
        else{
            // System.out.println(a[2]);
            format=pass1.t.OPTAB.get(a[2]).format;
        }
        if(format==1){
            ob1=pass1.t.OPTAB.get(a[2]).opcode;
            return ob1;
        }
        if(format==2){
            ob1=pass1.t.OPTAB.get(a[2]).opcode;
            ob2="";
            if(a[3].length()==3){
                ob2=pass1.t.REGISTERS.get(a[3].charAt(0)+"")+"";
                if(Character.isDigit(a[3].charAt(2))) ob2+=pass1.dectohex(Integer.parseInt(a[3].charAt(2)+"")-1);
                else ob2+=pass1.t.REGISTERS.get(a[3].charAt(2)+"");
            }
            else{
                if(Character.isDigit(a[3].charAt(0))) ob2=pass1.dectohex(Integer.parseInt(a[3].charAt(0)+""));
                else ob2=pass1.t.REGISTERS.get(a[3].charAt(0)+"")+"0";
            }
            return ob1+extendaddress(2,ob2);
        }
        if(format==3){
            ob1=pass1.t.OPTAB.get(a[2]).opcode;
            if(a[3].charAt(0)=='#'){
                immediate=true;
                ob1=pass1.dectohex(pass1.hextodec(ob1)+1);
                a[3]=a[3].substring(1);
                if(Character.isDigit(a[3].charAt(0))){
                    ob2="0";
                    ob3=pass1.dectohex(Integer.parseInt(a[3]));
                    return extendaddress(2,ob1)+ob2+extendaddress(3,ob3);
                }
            }
            else if(a[3].charAt(0)=='@'){
                indirect=true;
                ob1=pass1.dectohex(pass1.hextodec(ob1)+2);
                a[3]=a[3].substring(1);
            }
            else
                ob1=pass1.dectohex(pass1.hextodec(ob1)+3);
            ob2="0";
            boolean x=false;
            if(a[3].charAt(a[3].length()-2)==','){
                x=true;
                ob2=pass1.dectohex(pass1.hextodec(ob2)+8);
                a[3]=a[3].substring(0,a[3].length()-2);
            }
            program_counter=pass1.dectohex(pass1.hextodec(pass1.t.BLOCK.get(pass1.curr_block).start_address)+pass1.hextodec(a[5]));
            operand_add=pass1.dectohex(pass1.hextodec(pass1.t.SYMTAB.get(a[3]).label_address)+pass1.hextodec(pass1.t.BLOCK.get(pass1.t.SYMTAB.get(a[3]).block).start_address));
            if(x) a[3]+=",X";
            int disp=pass1.hextodec(operand_add)-pass1.hextodec(program_counter);
            if(disp>=-2048&&disp<=2047){
                ob2=pass1.dectohex(pass1.hextodec(ob2)+2);
                if(disp<0) disp+=4096;
                ob3=pass1.dectohex(disp);
                return extendaddress(2,ob1)+extendaddress(1,ob2)+extendaddress(3,ob3);
            }
            disp=pass1.hextodec(operand_add)-base;
            if(disp>=-2048&&disp<=2047){
                ob2=pass1.dectohex(pass1.hextodec(ob2)+4);
                if(disp<0) disp+=4096;
                ob3=pass1.dectohex(disp);
                return extendaddress(2,ob1)+extendaddress(1,ob2)+extendaddress(3,ob3);
            }
            pass1.error_status=true; 
        }  
        if(format==4){
            ob1=pass1.t.OPTAB.get(a[2]).opcode;
            if(a[3].charAt(0)=='#'){
                immediate=true;
                ob1=pass1.dectohex(pass1.hextodec(ob1)+1);
                a[3]=a[3].substring(1);
                if(Character.isDigit(a[3].charAt(0))){
                    ob2="0";
                    ob3=pass1.dectohex(Integer.parseInt(a[3]));
                    return extendaddress(2,ob1)+ob2+extendaddress(5,ob3);
                }
            }
            else if(a[3].charAt(0)=='@'){
                indirect=true;
                ob1=pass1.dectohex(pass1.hextodec(ob1)+2);
                a[3]=a[3].substring(1);
            }
            else
                ob1=pass1.dectohex(pass1.hextodec(ob1)+3);
            boolean x=false;
            ob2="1";
            if(a[3].charAt(a[3].length()-2)==','){
                x=true;
                ob2=pass1.dectohex(pass1.hextodec(ob2)+8);
                a[3]=a[3].substring(0,a[3].length()-2);
            }
            operand_add=pass1.dectohex(pass1.hextodec(pass1.t.SYMTAB.get(a[3]).label_address)+pass1.hextodec(pass1.t.BLOCK.get(pass1.t.SYMTAB.get(a[3]).block).start_address));
            if(x) a[3]+=",X";
            ob3=pass1.dectohex(pass1.hextodec(operand_add));
            a[2]+="+"+a[2];
            String loc_ctr=pass1.dectohex(pass1.hextodec(pass1.t.BLOCK.get(pass1.curr_block).start_address)+pass1.hextodec(a[4])+1);
            modification.write("M^"+extendaddress(6,loc_ctr)+"^05\n");
            return extendaddress(2,ob1)+extendaddress(1,ob2)+extendaddress(5,ob3);
        }
        return "";
    }
}

