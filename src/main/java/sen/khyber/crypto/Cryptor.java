package sen.khyber.crypto;

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

/**
 * 
 * 
 * @author Khyber Sen
 */
public class Cryptor {
    
    private static final Charset charset = StandardCharsets.UTF_8;
    private static final byte[] encryptedTag = "<encrypted>".getBytes(charset);
    
    private final List<Cipher> ciphers;
    private final Padder padder;
    
    public Cryptor(final Padder padder, final Cipher... ciphers) {
        this.padder = padder;
        this.ciphers = Arrays.asList(ciphers);
    }
    
    public Cryptor(final Padder padder, final Collection<? extends Cipher> ciphers) {
        this.padder = padder;
        this.ciphers = new ArrayList<>(ciphers);
    }
    
    public Cryptor(final Cipher... ciphers) {
        this(new PKCS7(), ciphers);
    }
    
    public Cryptor(final Collection<? extends Cipher> ciphers) {
        this(new PKCS7(), ciphers);
    }
    
    public Cryptor(final Cryptor... cryptors) {
        padder = cryptors[0].getPadder();
        ciphers = new ArrayList<>();
        for (final Cryptor cryptor : cryptors) {
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
        final String[] cipherNames = new String[ciphers.size()];
        for (int i = 0; i < cipherNames.length; i++) {
            cipherNames[i] = ciphers.get(i).getClass().getSimpleName();
        }
        return cipherNames;
    }
    
    public String getName() {
        return String.join("_", getNames());
    }
    
    private static byte[] addEncryptedTag(final byte[] bytes) {
        return ByteArrays.concat(encryptedTag, bytes);
    }
    
    private static byte[] removeEncryptedTag(final byte[] taggedBytes) {
        return ByteArrays.slice(taggedBytes, encryptedTag.length);
    }
    
    private static boolean isEncrypted(final byte[] bytes) {
        for (int i = 0; i < encryptedTag.length; i++) {
            if (bytes[i] != encryptedTag[i]) {
                return false;
            }
        }
        return true;
    }
    
    private byte[] encryptBytes(final byte[] plainbytes, final Cipher cipher) {
        final int blockSize = cipher.getBlockSize();
        final byte[] paddedBytes = blockSize == 0 ? plainbytes : padder.pad(plainbytes, blockSize);
        return cipher.encrypt(paddedBytes);
    }
    
    private byte[] decryptBytes(final byte[] cipherbytes, final Cipher cipher) {
        final int blockSize = cipher.getBlockSize();
        final byte[] paddedBytes = cipher.decrypt(cipherbytes);
        return blockSize == 0 ? paddedBytes : padder.unpad(paddedBytes, blockSize);
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
    
    private byte[] cryptBytes(final byte[] bytes) {
        if (isEncrypted(bytes)) {
            return decryptBytes(bytes);
        } else {
            return encryptBytes(bytes);
        }
    }
    
    public String cryptString(final String s) {
        return new String(cryptBytes(s.getBytes(charset)), charset);
    }
    
    public void cryptFile(final Path inPath, final Path outPath) throws IOException {
        Files.write(outPath, cryptBytes(Files.readAllBytes(inPath)));
    }
    
    public void cryptFile(final Path path) throws IOException {
        cryptFile(path, path);
    }
    
    public void cryptFiles(final List<Path> paths) {
        paths.parallelStream().forEach(file -> {
            try {
                cryptFile(file);
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    public void cryptFiles(final Path... paths) {
        cryptFiles(Arrays.asList(paths));
    }
    
    public void cryptDirectory(final Path dir, final int maxDepth) throws IOException {
        if (dir.toString().contains("src")) {
            throw new IOException("don't delete source files");
        }
        cryptFiles(MyFiles.walkDirectory(dir, maxDepth));
    }
    
    public void cryptDirectory(final Path dir) throws IOException {
        cryptDirectory(dir, Integer.MAX_VALUE);
    }
    
    private static String parseStringPath(final String pathAsString, final boolean isPlainString) {
        if (isPlainString) {
            return "str";
        }
        final Path path = Paths.get(pathAsString);
        if (Files.isDirectory(path)) {
            return "dir";
        }
        if (Files.exists(path)) {
            return "file";
        }
        return "str";
    }
    
    public String crypt(final String s, final boolean isPlainString) throws IOException {
        final String methodChoice = parseStringPath(s, isPlainString);
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
    
    public String crypt(final String s) throws IOException {
        return crypt(s, false);
    }
    
    public void crypt(final String inPath, final String outPath) throws IOException {
        cryptFile(Paths.get(inPath), Paths.get(outPath));
    }
    
    public void crypt(final String dir, final int maxDepth) throws IOException {
        cryptDirectory(Paths.get(dir), maxDepth);
    }
    
    public static void main(final String[] args) throws Exception {
        
        final String dirLocation = "src/sen.khyber.crypto/tests/";
        
        final Cipher nothing = new NullCipher();
        final Cipher rot13 = new CaesarCipher(13);
        final Cipher reverser = new ReverseCipher();
        final Cipher vigenere = new VigenereCipher("password");
        final Cipher columner = new ColumnCipher("password", 100);
        final Cipher autokey = new AutokeyCipher("password");
        final Cipher atbash = new AtbashCipher();
        final Cipher mixed = new MixedAlphabetCipher("password");
        final Cipher fence = new RailFenceCipher(50);
        //Cipher polybius = new PolybiusSquareCipher("password");
        //Cipher rijndael = new RijndaelCipher(new ECB(), 256, "passwordpassword");
        
        final Cipher[] ciphers = {nothing, rot13, reverser, vigenere, columner, autokey, atbash,
            mixed, fence, /*polybius,*/ /*rijndael*/};
        for (final Cipher cipher : ciphers) {
            final Cryptor cryptor = new Cryptor(cipher);
            final String cryptorName = cryptor.getName();
            System.out.println("using " + cryptorName);
            cryptor.crypt("testFile.txt", dirLocation + cryptorName + "Encrypted.txt");
            cryptor.crypt(dirLocation + cryptorName + "Encrypted.txt",
                    dirLocation + cryptorName + "Decrypted.txt");
        }
        
        final Cryptor multiCryptor = new Cryptor(ciphers);
        final String cryptorName = multiCryptor.getName();
        multiCryptor.crypt("testFile.txt", dirLocation + cryptorName + "Encrypted.txt");
        multiCryptor.crypt(dirLocation + cryptorName + "Encrypted.txt",
                dirLocation + cryptorName + "Decrypted.txt");
    }
    
}
