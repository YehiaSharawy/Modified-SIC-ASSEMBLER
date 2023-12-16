# Modi-SIC Disassembler

It is a program that translates machine code (HTE record) into an assembly code for the Modified Simplified Instructional Computer (modi-SIC).

# Instruction Set

<img width="424" alt="modisic" src="https://github.com/YehiaSharawy/Modified-SIC-ASSEMBLER/assets/65984199/c5533b21-0dae-42eb-ac25-7d68fb37bfc7">

# Instruction Format
## Format 1
|OPCODE (8 Bits)|
|---|

## Format 3
All Type 3 instruction could be immediate instructions this is done by a new division of bits of instructions of Type 3 (Format 3) as shown in following table.

|OPCODE (7 Bits)|Immediate Flag [i] (1 Bit)|Indexing Flag [x] (1 Bit)|Address (15 Bits)|
|---|---|---|---|

The modification applied on the opcode as
<ol>
<li>Only opcode is represented as 7 bits (not 8) as in SIC</li>
<li>The 8th bit of the opcode represents the immediate flag (i) which has two value</li>
<ul>
a. 0 if the instruction without immediate value (has an address) <br>
b. 1 if the instruction with immediate value
</ul>
</ol>

# Implementation

### Input
It takes as an input a text file (in.txt) that contains modi-SIC machine code (modi-SIC HTE record). <br>
Remember that The modi-SIC HTE record will be modified to accept also object code of Format 1 instruction of SIC/XE.

### Output
A generated symbol table file (symbolTable.txt) for all the symbols extracted from the HTE record.<br><br>
A generated assembly code file (assembly.txt) which contain three columns ordered from left as location counter, the assembly code, and object code.
