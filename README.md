# Assembler SIC

## Overview
This project is a **SIC Assembler** implementation that converts SIC assembly code into machine-readable object code. It processes input assembly files, generates intermediate files, listing files, and object files following the **SIC/XE machine architecture**.

## Features
- Supports SIC assembly instructions.
- Generates **intermediate files, object code, and listing files**.
- Implements **symbol table (SYMTAB) and opcode table (OPTAB)**.
- Uses **two-pass assembly process**.
- Generates SIC object file in **HTE format (Header, Text, End records)**.

## Project Structure
```
Assembler/
├── Makefile          # Build automation script
├── Table/            # Contains Table.java (Symbol & Opcode Tables)
├── Assembler/        # Main assembler implementation (Pass 1 & Pass 2)
├── input.txt         # Input SIC assembly source code
├── intermediate.txt  # Intermediate file (Pass 1 output)
├── list.txt          # Listing file (Final assembly result with object code)
├── object.txt        # Object file in HTE format
├── modification.txt  # Modification records (if applicable)
├── error.txt         # Error log file
```

## Installation & Compilation
To compile the project, ensure you have **Java installed** and run:
```sh
make
```

## Running the Assembler
To assemble the **input.txt** file:
```sh
make run
```
This will generate the **object code (object.txt) and listing file (list.txt)**.

## Output Files
### `object.txt` (HTE Format)
Example output:
```
H^COPY   ^100000^002077
T^002000^1E^17202D^03202A^2B2021^332003^032012^0F201E^03200F^0F2015^
T^003036^1E^^E3201B^332FFA^DB2015^^2F200A^3B2FE8^^4F0000
E^001000
```
- **H** - Header Record (Program Name, Start Address, Length)
- **T** - Text Record (Machine Instructions)
- **E** - End Record (Program Start Address)

### `list.txt` (Listing File)
Example output:
```
Line   Loc   Block   Source Statement         Object Code
------------------------------------------------------------
1      1000         COPY START
2      1000         FIRST STL RETADR          17202D
3      1003         CLoop JSUB RDREC          03202A
4      1006         LDA LENGTH                2B2021
5      1009         COMP ZERO                 332003
6      100C         JEQ ENDFIL                032012
7      100F         JSUB WRREC                0F201E
```
- Shows **address, block, source code, and object code**.

## Example Assembly Code (input.txt)
```
COPY    START 1000
FIRST   STL   RETADR
CLOOP   JSUB  RDREC
        LDA   LENGTH
        COMP  ZERO
        JEQ   ENDFIL
        JSUB  WRREC
ENDFIL  LDA   EOF
        STA   BUFFER
        LDA   THREE
        STA   LENGTH
        JSUB  WRREC
        LDL   RETADR
        RSUB
EOF     BYTE  C'EOF'
THREE   WORD  3
ZERO    WORD  0
RETADR  RESW  1
LENGTH  RESW  1
BUFFER  RESB  4096
```


## Author
**Jai Yadav**
**BTECH CSE**
**SEMESTER V**

