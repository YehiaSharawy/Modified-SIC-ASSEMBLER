import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class disasmXE {
    private ArrayList<String> hteRecords = new ArrayList<>();
    private BufferedWriter programC = new BufferedWriter(new FileWriter("src/ProgramCode.txt"));
    private BufferedWriter symtabW= new BufferedWriter(new FileWriter("src/SYMTAB.txt"));
    private BufferedReader br;
    private String programLength;
    private String startingAddress;

    public disasmXE() throws IOException { //Due to the use of FileWriter
    }

    private void headerRecordDisassembler(int row) throws IOException {
        String programName = hteRecords.get(row).substring(1,7);
        startingAddress = hteRecords.get(row).substring(7,13);
        programLength = hteRecords.get(row).substring(13,19);
        insertToSYMTAB(programName, startingAddress); //Inserting the program name and starting address to SYMTAB
        programC.write(startingAddress.substring(2)+"\t\t"+programName+"\t"+startingAddress+"\n");
    }
    private void textRecordDisassembler(int row) {
    }
    private void endRecordDisassembler(int row) throws IOException {
        String endAddress = hteRecords.get(row).substring(1,7);
        programC.write(Integer.toHexString(Integer.parseInt(programLength,16)+Integer.parseInt(startingAddress,16)).toUpperCase()+"\t\t"+"END"+"\t"+ "SYMTAB Label ADDRESS\n");
    }
    private void insertToSYMTAB(String label, String address) throws IOException {
        symtabW.write(label+"\t"+address+"\n");
        symtabW.close();
    }
    private void openFile(){
        try{
            br = new BufferedReader(new FileReader("src/hte.txt"));
            String line;
            while((line = br.readLine()) != null)
                hteRecords.add(line);
            br.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }
    public void disassemble() throws IOException {
        openFile();
        try {
            for (int i=0; i<hteRecords.size();i++){
                switch(hteRecords.get(i).substring(0,1)){
                    case "H" -> headerRecordDisassembler(i);
                    case "T" -> System.out.println("T"); // textRecordDisassembler(i);
                    case "E" -> endRecordDisassembler(i);
                    default -> System.out.println("Invalid Record");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }finally {
            try {
                if (programC != null)
                    programC.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
