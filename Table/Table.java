/*
 * Author:Jai Yadav
 * BTech CSE 3rd Year
 * Semester V
 */
package Table;
import Table.optab_info;
import Table.symtab_info;
import Table.block_info;
import java.util.HashMap;
import java.util.*;

public  class Table {
    public static LinkedHashMap<String, optab_info> OPTAB = new LinkedHashMap<String, optab_info>();
    /*----OPTAB-----*/

    public LinkedHashMap<String, symtab_info> SYMTAB = new LinkedHashMap<String, symtab_info>();
    /*----SYMTAB-----*/

    public LinkedHashMap<String, Integer> REGISTERS = new LinkedHashMap<String, Integer>();

    public LinkedHashMap<String, block_info> BLOCK = new LinkedHashMap<String, block_info>();

    /*----INITIALIZE----*/
    public void init() {
        OPTAB.put("ADD", new optab_info(3, "18"));
        OPTAB.put("ADDF", new optab_info(3, "58"));
        OPTAB.put("ADDR", new optab_info(2, "90"));
        OPTAB.put("AND", new optab_info(3, "4"));
        OPTAB.put("CLEAR", new optab_info(2, "B4"));
        OPTAB.put("COMP", new optab_info(3, "28"));
        OPTAB.put("COMPF", new optab_info(3, "88"));
        OPTAB.put("COMPR", new optab_info(2, "A0"));
        OPTAB.put("DIV", new optab_info(3, "24"));
        OPTAB.put("DIVF", new optab_info(3, "64"));
        OPTAB.put("DIVR", new optab_info(2, "9C"));
        OPTAB.put("JEQ", new optab_info(3, "30"));
        OPTAB.put("JGT", new optab_info(3, "34"));
        OPTAB.put("JLT", new optab_info(3, "38"));
        OPTAB.put("JSUB", new optab_info(3, "48"));
        OPTAB.put("LDA", new optab_info(3, "00"));
        OPTAB.put("LDB", new optab_info(3, "68"));
        OPTAB.put("LDCH", new optab_info(3, "50"));
        OPTAB.put("LDF", new optab_info(3, "70"));
        OPTAB.put("LDL", new optab_info(3, "08"));
        OPTAB.put("LDS", new optab_info(3, "6C"));
        OPTAB.put("LDT", new optab_info(3, "74"));
        OPTAB.put("LDX", new optab_info(3, "04"));
        OPTAB.put("LPS", new optab_info(3, "D0"));
        OPTAB.put("MUL", new optab_info(3, "20"));
        OPTAB.put("MULF", new optab_info(3, "60"));
        OPTAB.put("MULR", new optab_info(2, "98"));
        OPTAB.put("NORM", new optab_info(1, "C8"));
        OPTAB.put("OR", new optab_info(3, "44"));
        OPTAB.put("RD", new optab_info(3, "D8"));
        OPTAB.put("RMO", new optab_info(2, "AC"));
        OPTAB.put("RSUB", new optab_info(3, "4C"));
        OPTAB.put("SHIFTL", new optab_info(2, "A4"));
        OPTAB.put("SHIFTR", new optab_info(2, "A8"));
        OPTAB.put("SIO", new optab_info(1, "F0"));
        OPTAB.put("SSK", new optab_info(3, "EC"));
        OPTAB.put("STA", new optab_info(3, "0C"));
        OPTAB.put("STB", new optab_info(3, "78"));
        OPTAB.put("STCH", new optab_info(3, "54"));
        OPTAB.put("STF", new optab_info(3, "80"));
        OPTAB.put("STI", new optab_info(3, "D4"));
        OPTAB.put("STL", new optab_info(3, "14"));
        OPTAB.put("STS", new optab_info(3, "7C"));
        OPTAB.put("STSW", new optab_info(3, "E8"));
        OPTAB.put("STT", new optab_info(3, "84"));
        OPTAB.put("STX", new optab_info(3, "10"));
        OPTAB.put("SUB", new optab_info(3, "1C"));
        OPTAB.put("SUBF", new optab_info(3, "5C"));
        OPTAB.put("SUBR", new optab_info(2, "94"));
        OPTAB.put("SVC", new optab_info(2, "B0"));
        OPTAB.put("TD", new optab_info(3, "E0"));
        OPTAB.put("TIO", new optab_info(1, "F8"));
        OPTAB.put("TIX", new optab_info(3, "2C"));
        OPTAB.put("TIXR", new optab_info(2, "B8"));
        OPTAB.put("WD", new optab_info(3, "DC"));

        /*----REGISTERS INIT---- */
        REGISTERS.put("A", 0);
        REGISTERS.put("X", 1);
        REGISTERS.put("L", 2);
        REGISTERS.put("B", 3);
        REGISTERS.put("S", 4);
        REGISTERS.put("T", 5);
        REGISTERS.put("F", 6);
        REGISTERS.put("PC", 8);
        REGISTERS.put("SW", 9);
    }
}
