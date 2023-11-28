public class programcounter {
    opcode op = new opcode();
    private int pcCounter;
    public String getPcCounter() {
        return Integer.toString(pcCounter,16).toUpperCase();
    }
    public void initializePC(int startingAddress){
        pcCounter = startingAddress;
    }
    public void incrementPC(String opCode){
        op.addInstructionsToTable();
        if(op.getOpFormat(opCode)==1)
            pcCounter+=1;
        else if(op.getOpFormat(opCode)==3)
            pcCounter+=3;
    }
    public void incrementPC_Byte(int length){
        pcCounter+=length;
    }
    public void incrementPC_Word(){
        pcCounter+=3;
    }
    public void incrementPC_RSW(int numOfWords){
        pcCounter+=numOfWords;
    }
}
