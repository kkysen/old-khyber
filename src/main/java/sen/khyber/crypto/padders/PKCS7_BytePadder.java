package sen.khyber.crypto.padders;

public class PKCS7_BytePadder extends BytePadder {

    public PKCS7_BytePadder() {}
    
    @Override
    public byte getFillerByte(int bytesToAdd) {
        return (byte) bytesToAdd;
    }

}
