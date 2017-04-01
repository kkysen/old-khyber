package sen.khyber.io;

import java.nio.ByteBuffer;

/**
 * 
 * 
 * @author Khyber Sen
 */
public interface FastSerializable {
    
    public int serializedLength();
    
    public void serialize(ByteBuffer out);
    
}
