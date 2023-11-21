import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        disasmXE d = new disasmXE();
        d.disassemble();

//        System.out.printf(String.format("%04X",2));

//        int opcodeHex = Integer.parseInt("14",16); //getting the opcode from the T record at row
//        System.out.println(opcodeHex);

//        System.out.println(Integer.parseInt("3C",16));
//
//        opcode o = new opcode();
//        o.addInstructionsToTable();
//        int y = o.getOpFormat("C0");
//        System.out.println(y);
    }
}
