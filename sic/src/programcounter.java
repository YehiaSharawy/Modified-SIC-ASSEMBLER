public class programcounter {
    opcode opcode_ = new opcode();
    private int pcCounter;
    public String getPcCounter() {
        return Integer.toString(pcCounter);
    }
    public void initializePC(int startingAddress){
        pcCounter = startingAddress;
    }
    public void incrementPC(String opCode){
        if(opcode_.getOpFormat(opCode)==1)
            pcCounter+=1;
        else if(opcode_.getOpFormat(opCode)==3)
            pcCounter+=3;
    }
    public void incrementPC_Base(int length){
        pcCounter+=length;
    }
    public void incrementPC_Word(){
        pcCounter+=3;
    }
    public void incrementPC_RSW(int numOfWords){
        pcCounter+=numOfWords;
    }
}
