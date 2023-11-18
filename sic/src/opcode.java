import java.util.ArrayList;

public class opcode {
    private String mnemonic;
    private int opcode;
    private int format;
    public String getMnemonic() {
        return mnemonic;
    }
    public int getOpcode() {
        return opcode;
    }
    public int getFormat() {
        return format;
    }

    public opcode(){
    }
    public opcode(String mnemonic, int opcode, int format){
        this.mnemonic = mnemonic;
        this.opcode = opcode;
        this.format = format;
    }

    //INSTRUCTION SET
    private ArrayList<opcode> instructionSet = new ArrayList<>(32);
    public void addInstructionsToTable(){
        instructionSet.add(new opcode("ADD",0X18,3));
        instructionSet.add(new opcode("AND",0X40,3));
        instructionSet.add(new opcode("COMP",0X28,3));
        instructionSet.add(new opcode("DIV",0X24,3));
        instructionSet.add(new opcode("J",0X3C,3));
        instructionSet.add(new opcode("JEQ",0X30,3));
        instructionSet.add(new opcode("JGT",0X34,3));
        instructionSet.add(new opcode("JLT",0X38,3));
        instructionSet.add(new opcode("JSUB",0X48,3));
        instructionSet.add(new opcode("LDA",0X00,3));
        instructionSet.add(new opcode("LDCH",0X50,3));
        instructionSet.add(new opcode("LDL",0X08,3));
        instructionSet.add(new opcode("LDX",0X04,3));
        instructionSet.add(new opcode("MUL",0X20,3));
        instructionSet.add(new opcode("OR",0X44,3));
        instructionSet.add(new opcode("RD",0XD8,3));
        instructionSet.add(new opcode("RSUB",0X4C,3));
        instructionSet.add(new opcode("STA",0X0C,3));
        instructionSet.add(new opcode("STCH",0X54,3));
        instructionSet.add(new opcode("STL",0X14,3));
        instructionSet.add(new opcode("STSW",0XE8,3));
        instructionSet.add(new opcode("STX",0X10,3));
        instructionSet.add(new opcode("SUB",0X1C,3));
        instructionSet.add(new opcode("TD",0XE0,3));
        instructionSet.add(new opcode("TIX",0X2C,3));
        instructionSet.add(new opcode("WD",0XDC,3));
        instructionSet.add(new opcode("FIX",0XC4,1));
        instructionSet.add(new opcode("FLOAT",0XC0,1));
        instructionSet.add(new opcode("HIO",0XF4,1));
        instructionSet.add(new opcode("NORM",0XC8,1));
        instructionSet.add(new opcode("SIO",0XF0,1));
        instructionSet.add(new opcode("TIO",0XF8,1));
    }
    // Getting Opcode Mnemonic
    public String getOpMnemonic(int opcode){
        for(opcode op : instructionSet){
            if(op.getOpcode() == opcode)
                return op.getMnemonic();
        }
        return null;
    }
    // Getting Opcode format
    public int getOpFormat(int opcode){
        for(opcode op : instructionSet){
            if(op.getOpcode() == opcode)
                return op.getFormat();
        }
        return 0;
    }
}
