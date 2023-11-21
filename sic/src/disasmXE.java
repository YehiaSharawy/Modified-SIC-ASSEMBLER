import java.io.*;
import java.util.ArrayList;

public class disasmXE {

    programcounter pc = new programcounter();
    private String HTEFilePath = "src/hte.txt";
    private String programCodeFilePath = "src/ProgramCode.txt";
    private String SYMTABFilePath = "src/SYMTAB.txt";
    private String programLength;
    private String startingAddress;
    private ArrayList<String> hteRecords = new ArrayList<>();
    private BufferedReader br;
    private BufferedWriter programC = new BufferedWriter(new FileWriter(programCodeFilePath));
    private BufferedWriter symtabW= new BufferedWriter(new FileWriter(SYMTABFilePath));

    public disasmXE() throws IOException { //Due to the use of FileWriter
    }

    private int disassembleInstruction(int row, int current) throws IOException {
        opcode code = new opcode();
        code.addInstructionsToTable(); //initializing the instruction set
        String opCode = hteRecords.get(row).substring(current, current + 2); //getting the opcode from the T record at row
        int instructionLength = code.getOpFormat(opCode);
        switch (instructionLength) {
            case 1:
                disassembleInFormat1(code, opCode,row,current);
                break;
            case 3:
//                instructionLength = disassembleInFormat3(code, opCode, row, current);
                break;
            default:
                break;
        }
        return instructionLength * 2;
    }

    private void disassembleInFormat1(opcode code, String opcodeHex,int row,int current) throws IOException {
        String opcodeMnemonic = code.getOpMnemonic(opcodeHex);
//        programC.write(pc.getPcCounter()+"\t\t"+opcodeMnemonic+"\t"+objectCode+"\n");
        //Inserting it to SYMTAB
    }

    private void headerRecordDisassembler(int row) throws IOException {
        String programName = hteRecords.get(row).substring(1,7);
        startingAddress = hteRecords.get(row).substring(7,13);
        programLength = hteRecords.get(row).substring(13,19);
        pc.initializePC(Integer.parseInt(startingAddress));
        insertToSYMTAB(programName, pc.getPcCounter()); //Inserting the program name and starting address to SYMTAB
        programC.write(pc.getPcCounter()+"\t"+programName+"\tSTART\t"+startingAddress+"\n");
    }
    private void textRecordDisassembler(int row) throws IOException {
        String locationCounter = hteRecords.get(row).substring(1, 7);
        int textRecordLength = Integer.parseInt(hteRecords.get(row).substring(7, 9), 16); //.1E.
        int current = 9; //after textrecord length .1E.0<--9TH bit place
        String objectCode = hteRecords.get(row).substring(current, 2 * textRecordLength + 9);

        while (current < (2 * textRecordLength + 9)) {
            int objectCodeSize = disassembleInstruction(row, current);
            startingAddress = Integer.toString(Integer.parseInt(startingAddress) + (objectCodeSize / 2));
            current += objectCodeSize;
        }
    }

    private void endRecordDisassembler(int row) throws IOException {
        String endAddress = hteRecords.get(row).substring(1,7);
        programC.write(Integer.toHexString(Integer.parseInt(programLength,16)+Integer.parseInt(startingAddress,16)).toUpperCase()+"\t\t"+"END");
    }
    private void insertToSYMTAB(String mnemonic, String address) throws IOException {
        symtabW.write(mnemonic+"\t"+address+"\n");
        symtabW.close();
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
    }
}