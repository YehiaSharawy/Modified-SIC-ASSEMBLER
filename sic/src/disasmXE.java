import java.io.*;
import java.util.ArrayList;

public class disasmXE {
    private opcode opc = new opcode();
    private programcounter pc = new programcounter();
    private symboltable sym = new symboltable();
    private String HTEFilePath = "src/hte.txt";
    private String programCodeFilePath = "src/assembly.txt";
    private String SYMTABFilePath = "src/symbolTable.txt";
    private String programLength;
    private String startingAddress;
    private ArrayList<String> hteRecords = new ArrayList<>();
    private BufferedReader br;
    private BufferedWriter programC = new BufferedWriter(new FileWriter(programCodeFilePath));
    private BufferedWriter symtabW= new BufferedWriter(new FileWriter(SYMTABFilePath));

    public disasmXE() throws IOException { //Due to the use of FileWriter
    }
    private void insertToSYMTAB(String mnemonic, String address) throws IOException {
        symtabW.write(mnemonic+"\t"+address+"\n");
        symtabW.flush();
    }
    private int disassembleInstruction(int row, int current) throws IOException {
        String opcodeHEX = hteRecords.get(row).substring(current,current+2); //acquiring first 2 hex for opcode instruction
        String objectcodeHEX = hteRecords.get(row).substring(current,current+6);
        String addressHEX = hteRecords.get(row).substring(current+2,current+6);// getting the last 4bits from the object code
        int instructionLength = opc.getOpFormat(opcodeHEX); //getting its format

        System.out.println("Opcode: " + opcodeHEX);
        System.out.println("ObjectCode: " + objectcodeHEX);
        System.out.println("Address: " + addressHEX);
        System.out.println("InstructionLength: " + instructionLength);

        switch (instructionLength) {
            case 1:
                System.out.println("1111");
                disassembleInFormat1(opc,opcodeHEX);
                break;
            case 3:
                System.out.println("3333");
                instructionLength = disassembleInFormat3(opc, opcodeHEX,objectcodeHEX,addressHEX,row, current);
                break;
            default:
                break;
        }
        return instructionLength*2;
    }
    private void disassembleInFormat1(opcode code, String opcodeHex) throws IOException {
        String opcodeMnemonic = code.getOpMnemonic(opcodeHex);
        System.out.println("pccounter is "+pc.getPcCounter());
        programC.write(pc.getPcCounter()+"\t"+sym.checkSYMTAB(pc.getPcCounter())+"\t"+opcodeMnemonic+"\t"+opcodeHex+"\n");
    }
    private int disassembleInFormat3(opcode code, String opcodeHex,String objectcodeHEX,String addressHEX,int row, int current) throws IOException{
        String opcodeMnemonic = code.getOpMnemonic(opcodeHex);
        String calculatedAddressH , symbol = null;
        if(opcodeMnemonic == "RSUB")
            programC.write(pc.getPcCounter()+"\t"+sym.checkSYMTAB(pc.getPcCounter())+"\t"+opcodeMnemonic+"\t\n");
        else if(code.getOpBit(Integer.parseInt(objectcodeHEX,16),15)==1){ //check if the index flag is 1
                calculatedAddressH = String.format("%04X", (Integer.parseInt(addressHEX,16) - 0x8000) & 0xFFFF);
                System.out.println("test "+Integer.toString(Integer.parseInt(calculatedAddressH,16)));
                symbol = sym.getSymbol(Integer.toString(Integer.parseInt(calculatedAddressH,16)));
                sym.addSymbol(calculatedAddressH);
                programC.write(pc.getPcCounter()+"\t"+sym.checkSYMTAB(pc.getPcCounter())+"\t"+opcodeMnemonic+"\t"+symbol+"\t"+objectcodeHEX+"\n");
                insertToSYMTAB(symbol,calculatedAddressH);
        }else if (code.getOpBit(Integer.parseInt(objectcodeHEX,16),16)==1) { // checks if the immediate flag is 1
            if(code.getOpFormat(Integer.toString(Integer.parseInt(opcodeHex,16)-1))!=0)
                programC.write(pc.getPcCounter()+"\t"+sym.checkSYMTAB(pc.getPcCounter())+"\t"+code.getOpMnemonic(Integer.toString(Integer.parseInt(opcodeHex,16)-1))+"\t"+symbol+"\t"+objectcodeHEX+"\n");
        }else {
            sym.addSymbol(addressHEX);
            symbol = sym.getSymbol(addressHEX);
            programC.write(pc.getPcCounter()+"\t"+sym.checkSYMTAB(pc.getPcCounter())+"\t"+opcodeMnemonic+"\t"+symbol+"\t"+objectcodeHEX+"\n");
            insertToSYMTAB(symbol,addressHEX);
        }
        return 3;
    }
    private void headerRecordDisassembler(int row) throws IOException {
        String programName = hteRecords.get(row).substring(1,7);
        startingAddress = hteRecords.get(row).substring(7,13);
        programLength = hteRecords.get(row).substring(13,19);

        pc.initializePC(Integer.parseInt(startingAddress));
        sym.getSymbolTable().put(programName,pc.getPcCounter());

        programC.write(pc.getPcCounter()+"\t"+programName+"\tSTART\t"+String.format("%04d",Integer.parseInt(startingAddress))+"\n");
        insertToSYMTAB(programName,pc.getPcCounter());
    }
    private void textRecordDisassembler(int row) throws IOException {
        opc.addInstructionsToTable();
        String locationCounter = hteRecords.get(row).substring(1, 7);
        String textRecordLength = hteRecords.get(row).substring(7, 9); //.1E.
        int textRecordEnd = Integer.parseInt(textRecordLength,16)+Integer.parseInt(locationCounter, 16);
        System.out.println("row "+row+"\t"+textRecordEnd);
        System.out.println("lc "+locationCounter+"\ttl"+textRecordLength);
        int current = 9; //after textrecord length .1E.0<--9TH bit place
        String objectCode = hteRecords.get(row).substring(current, 2 * Integer.parseInt(textRecordLength,16) + 9);
        while (current < (2 * Integer.parseInt(textRecordLength,16) + 9)) {
            int objectCodeSize = disassembleInstruction(row, current);
            startingAddress = Integer.toString(Integer.parseInt(startingAddress) + (objectCodeSize / 2));
            current += objectCodeSize;

            System.out.println("Current: " + current);
            System.out.println("ObjectCodeSize: " + objectCodeSize);
            System.out.println("StartingAddress: " + startingAddress);
        }

    }
    private void endRecordDisassembler(int row) throws IOException {
        String endAddress = hteRecords.get(row).substring(1,7);
        programC.write(Integer.toHexString(Integer.parseInt(programLength,16)+Integer.parseInt(startingAddress,16)).toUpperCase()+"\t\t"+"END");
    }
    private void openFile(){
        try{
            br = new BufferedReader(new FileReader(HTEFilePath));
            String line;
            while((line = br.readLine()) != null)
                hteRecords.add(line);
            br.close();
        }catch (FileNotFoundException e){
            System.out.println("File not found");
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    public void disassemble() throws IOException {
        openFile();
        System.out.println(hteRecords);
        try {
            for (int i=0; i<hteRecords.size();i++){
                switch(hteRecords.get(i).substring(0,1)){
                    case "H" -> headerRecordDisassembler(i);
                    case "T" -> textRecordDisassembler(i);
                    case "E" -> endRecordDisassembler(i);
                    default -> System.out.println("Invalid Record");
                }
            }
            System.out.println("Successfully disassembled input object code");
        } catch (Exception e) {
            System.out.println("Error in disassembling HTE file at "+e);
        }finally {
            try {
                if (programC != null)
                    programC.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(sym.getSymbolTable());
    }
}