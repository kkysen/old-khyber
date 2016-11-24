package sen.khyber.crypto;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import sen.khyber.crypto.ByteArrays;
import sen.khyber.crypto.ciphers.Cipher;
import sen.khyber.crypto.ciphers.NullCipher;
import sen.khyber.crypto.ciphers.substitution.monoalphabetic.AtbashCipher;
import sen.khyber.crypto.ciphers.substitution.monoalphabetic.CaesarCipher;
import sen.khyber.crypto.ciphers.substitution.monoalphabetic.MixedAlphabetCipher;
import sen.khyber.crypto.ciphers.substitution.polyalphabetic.AutokeyCipher;
import sen.khyber.crypto.ciphers.substitution.polyalphabetic.VigenereCipher;
import sen.khyber.crypto.ciphers.transposition.ColumnCipher;
import sen.khyber.crypto.ciphers.transposition.RailFenceCipher;
import sen.khyber.crypto.ciphers.transposition.ReverseCipher;
import sen.khyber.crypto.padders.Padder;
import sen.khyber.io.MyFiles;

public class Cryptor {
    
    private static final Charset charset = StandardCharsets.UTF_8;
    private static final byte[] encryptedTag = "<encrypted>".getBytes(charset);
    
    private List<Cipher> ciphers;
    private Padder padder;
    
    public Cryptor(Padder padder, Cipher... ciphers) {
        this.padder = padder;
        this.ciphers = Arrays.asList(ciphers);
    }
    
    public Cryptor(Padder padder, Collection<? extends Cipher> ciphers) {
        this.padder = padder;
        this.ciphers = new ArrayList<>(ciphers); 
    }
    
    public Cryptor(Cipher... ciphers) {
        this(new PKCS7(), ciphers);
    }
    
    public Cryptor(Collection<? extends Cipher> ciphers) {
        this(new PKCS7(), ciphers);
    }
    
    public Cryptor(Cryptor... cryptors) {
        padder = cryptors[0].getPadder();
        ciphers = new ArrayList<>();
        for (Cryptor cryptor : cryptors) {
            ciphers.addAll(cryptor.getCiphers());
        }
    }
    
    /*public Cryptor(String password) {
        this(    new AES(password));
    }*/
    
    public Padder getPadder() {
        return padder;
    }

    public List<Cipher> getCiphers() {
        return ciphers;
    }

    public String[] getNames() {
        String[] cipherNames = new String[ciphers.size()];
        for (int i = 0; i < cipherNames.length; i++) {
            cipherNames[i] = ciphers.get(i).getClass().getSimpleName();
        }
        return cipherNames;
    }

    public String getName() {
        return String.join("_", getNames());
    }

    private static byte[] addEncryptedTag(byte[] bytes) {
        return ByteArrays.concat(encryptedTag, bytes);
    }

    private static byte[] removeEncryptedTag(byte[] taggedBytes) {
        return ByteArrays.slice(taggedBytes, encryptedTag.length);
    }

    private static boolean isEncrypted(byte[] bytes) {
        for (int i = 0; i < encryptedTag.length; i++) {
            if (bytes[i] != encryptedTag[i]) {
                return false;
            }
        }
        return true;
    }

    private byte[] encryptBytes(byte[] plainbytes, Cipher cipher) {
        int blockSize = cipher.getBlockSize();
        byte[] paddedBytes = (blockSize == 0) ? plainbytes : padder.pad(plainbytes, blockSize);
        return cipher.encrypt(paddedBytes);
    }

    private byte[] decryptBytes(byte[] cipherbytes, Cipher cipher) {
        int blockSize = cipher.getBlockSize();
        byte[] paddedBytes = cipher.decrypt(cipherbytes);
        return (blockSize == 0) ? paddedBytes : padder.unpad(paddedBytes, blockSize);
    }

    private byte[] encryptBytes(byte[] plainbytes) {
        for (int i = 0; i < ciphers.size(); i++) {
            plainbytes = encryptBytes(plainbytes, ciphers.get(i));
        }
        return addEncryptedTag(plainbytes);
    }

    private byte[] decryptBytes(byte[] cipherbytes) {
        cipherbytes = removeEncryptedTag(cipherbytes);
        for (int i = ciphers.size() - 1; i >= 0; i--) {
            cipherbytes = decryptBytes(cipherbytes, ciphers.get(i));
        }
        return cipherbytes;
    }

    private byte[] cryptBytes(byte[] bytes) {
        if (isEncrypted(bytes)) {
            return decryptBytes(bytes);
        } else {
            return encryptBytes(bytes);
        }
    }

    public String cryptString(String s) {
        return new String(cryptBytes(s.getBytes(charset)), charset);
    }

    public void cryptFile(Path inPath, Path outPath) throws IOException {
        Files.write(outPath, cryptBytes(Files.readAllBytes(inPath)));
    }

    public void cryptFile(Path path) throws IOException {
        cryptFile(path, path);
    }
    
    public void cryptFiles(List<Path> paths) {
        paths.parallelStream().forEach(file -> {
            try {
                cryptFile(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    public void cryptFiles(Path... paths) {
        cryptFiles(Arrays.asList(paths));
    }

    public void cryptDirectory(Path dir, int maxDepth) throws IOException {
        if (dir.toString().contains("src")) {
            throw new IOException("don't delete source files");
        }
        cryptFiles(MyFiles.walkDirectory(dir, maxDepth));
    }

    public void cryptDirectory(Path dir) throws IOException {
        cryptDirectory(dir, Integer.MAX_VALUE);
    }

    private static String parseStringPath(String pathAsString, boolean isPlainString) {
        if (isPlainString) {
            return "str";
        }
        Path path = Paths.get(pathAsString);
        if (Files.isDirectory(path)) {
            return "dir";
        }
        if (Files.exists(path)) {
            return "file";
        }
        return "str";
    }

    public String crypt(String s, boolean isPlainString) throws IOException {
        String methodChoice = parseStringPath(s, isPlainString);
        String result = "";
        switch (methodChoice) {
            case "str":
                result = cryptString(s);
                break;
            case "file":
                cryptFile(Paths.get(s));
                break;
            case "dir":
                cryptDirectory(Paths.get(s));
                break;
        }
        return result;
    }
    
    public String crypt(String s) throws IOException {
        return crypt(s, false);
    }

    public void crypt(String inPath, String outPath) throws IOException {
        cryptFile(Paths.get(inPath), Paths.get(outPath));
    }

    public void crypt(String dir, int maxDepth) throws IOException {
        cryptDirectory(Paths.get(dir), maxDepth);
    }

    public static void main(String[] args) throws Exception {
        
        String dirLocation = "src/sen.khyber.crypto/tests/";
        
        Cipher nothing = new NullCipher();
        Cipher rot13 = new CaesarCipher(13);
        Cipher reverser = new ReverseCipher();
        Cipher vigenere = new VigenereCipher("password");
        Cipher columner = new ColumnCipher("password", 100);
        Cipher autokey = new AutokeyCipher("password");
        Cipher atbash = new AtbashCipher();
        Cipher mixed = new MixedAlphabetCipher("password");
        Cipher fence = new RailFenceCipher(50);
        //Cipher polybius = new PolybiusSquareCipher("password");
        //Cipher rijndael = new RijndaelCipher(new ECB(), 256, "passwordpassword");
        
        Cipher[] ciphers = {nothing, rot13, reverser, vigenere, columner, autokey, atbash, mixed, fence, /*polybius,*/ /*rijndael*/};
        for (Cipher cipher : ciphers) {
            Cryptor cryptor = new Cryptor(cipher);
            String cryptorName = cryptor.getName();
            System.out.println("using " + cryptorName);
            cryptor.crypt("testFile.txt", dirLocation + cryptorName + "Encrypted.txt");
            cryptor.crypt(dirLocation + cryptorName + "Encrypted.txt", dirLocation + cryptorName + "Decrypted.txt");
        }
        
        Cryptor multiCryptor = new Cryptor(ciphers);
        String cryptorName = multiCryptor.getName();
        multiCryptor.crypt("testFile.txt", dirLocation + cryptorName + "Encrypted.txt");
        multiCryptor.crypt(dirLocation + cryptorName + "Encrypted.txt", dirLocation + cryptorName + "Decrypted.txt");
    }
    
}
