package sen.khyber.crypto.ciphers.modern;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import sen.khyber.crypto.ECB;
import sen.khyber.crypto.ciphers.BlockCipher;
import sen.khyber.crypto.ciphers.substitution.SubstitutionCipher;
import sen.khyber.crypto.ciphers.transposition.TranspositionCipher;
import sen.khyber.crypto.modes.Mode;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class RijndaelCipher extends BlockCipher
        implements ModernCipher, TranspositionCipher, SubstitutionCipher {
    
    private static final Charset charset = StandardCharsets.UTF_8;
    
    private static final int MAX_ROUNDS = 14,
            MAX_Kc = 256 / 4;
    
    private static final byte[]
    
    logtable = {
        (byte) 26, (byte) 46, (byte) 114, (byte) 150, (byte) 161, (byte) 248, (byte) 19, (byte) 53,
        (byte) 95, (byte) 225, (byte) 56, (byte) 72, (byte) 216, (byte) 115, (byte) 149, (byte) 164,
        (byte) 247, (byte) 2, (byte) 6, (byte) 10, (byte) 30, (byte) 34, (byte) 102, (byte) 170,
        (byte) 229, (byte) 52, (byte) 92, (byte) 228, (byte) 55, (byte) 89, (byte) 235, (byte) 38,
        (byte) 106, (byte) 190, (byte) 217, (byte) 112, (byte) 144, (byte) 171, (byte) 230,
        (byte) 49,
        (byte) 83, (byte) 245, (byte) 4, (byte) 12, (byte) 20, (byte) 60, (byte) 68, (byte) 204,
        (byte) 79, (byte) 209, (byte) 104, (byte) 184, (byte) 211, (byte) 110, (byte) 178,
        (byte) 205,
        (byte) 76, (byte) 212, (byte) 103, (byte) 169, (byte) 224, (byte) 59, (byte) 77, (byte) 215,
        (byte) 98, (byte) 166, (byte) 241, (byte) 8, (byte) 24, (byte) 40, (byte) 120, (byte) 136,
        (byte) 131, (byte) 158, (byte) 185, (byte) 208, (byte) 107, (byte) 189, (byte) 220,
        (byte) 127,
        (byte) 129, (byte) 152, (byte) 179, (byte) 206, (byte) 73, (byte) 219, (byte) 118,
        (byte) 154,
        (byte) 181, (byte) 196, (byte) 87, (byte) 249, (byte) 16, (byte) 48, (byte) 80, (byte) 240,
        (byte) 11, (byte) 29, (byte) 39, (byte) 105, (byte) 187, (byte) 214, (byte) 97, (byte) 163,
        (byte) 254, (byte) 25, (byte) 43, (byte) 125, (byte) 135, (byte) 146, (byte) 173,
        (byte) 236,
        (byte) 47, (byte) 113, (byte) 147, (byte) 174, (byte) 233, (byte) 32, (byte) 96, (byte) 160,
        (byte) 251, (byte) 22, (byte) 58, (byte) 78, (byte) 210, (byte) 109, (byte) 183, (byte) 194,
        (byte) 93, (byte) 231, (byte) 50, (byte) 86, (byte) 250, (byte) 21, (byte) 63, (byte) 65,
        (byte) 195, (byte) 94, (byte) 226, (byte) 61, (byte) 71, (byte) 201, (byte) 64, (byte) 192,
        (byte) 91, (byte) 237, (byte) 44, (byte) 116, (byte) 156, (byte) 191, (byte) 218,
        (byte) 117,
        (byte) 159, (byte) 186, (byte) 213, (byte) 100, (byte) 172, (byte) 239, (byte) 42,
        (byte) 126,
        (byte) 130, (byte) 157, (byte) 188, (byte) 223, (byte) 122, (byte) 142, (byte) 137,
        (byte) 128,
        (byte) 155, (byte) 182, (byte) 193, (byte) 88, (byte) 232, (byte) 35, (byte) 101,
        (byte) 175,
        (byte) 234, (byte) 37, (byte) 111, (byte) 177, (byte) 200, (byte) 67, (byte) 197, (byte) 84,
        (byte) 252, (byte) 31, (byte) 33, (byte) 99, (byte) 165, (byte) 244, (byte) 7, (byte) 9,
        (byte) 27, (byte) 45, (byte) 119, (byte) 153, (byte) 176, (byte) 203, (byte) 70, (byte) 202,
        (byte) 69, (byte) 207, (byte) 74, (byte) 222, (byte) 121, (byte) 139, (byte) 134,
        (byte) 145,
        (byte) 168, (byte) 227, (byte) 62, (byte) 66, (byte) 198, (byte) 81, (byte) 243, (byte) 14,
        (byte) 18, (byte) 54, (byte) 90, (byte) 238, (byte) 41, (byte) 123, (byte) 141, (byte) 140,
        (byte) 143, (byte) 138, (byte) 133, (byte) 148, (byte) 167, (byte) 242, (byte) 13,
        (byte) 23,
        (byte) 57, (byte) 75, (byte) 221, (byte) 124, (byte) 132, (byte) 151, (byte) 162,
        (byte) 253,
        (byte) 28, (byte) 36, (byte) 108, (byte) 180, (byte) 199, (byte) 82, (byte) 246, (byte) 1,
        (byte) 3, (byte) 5, (byte) 15, (byte) 17, (byte) 51, (byte) 85, (byte) 255, (byte) 26,
        (byte) 46, (byte) 114, (byte) 150, (byte) 161, (byte) 248, (byte) 19, (byte) 53, (byte) 95,
        (byte) 225, (byte) 56, (byte) 72, (byte) 216, (byte) 115, (byte) 149, (byte) 164,
        (byte) 247,
        (byte) 2, (byte) 6, (byte) 10, (byte) 30, (byte) 34, (byte) 102, (byte) 170, (byte) 229,
        (byte) 52, (byte) 92, (byte) 228, (byte) 55, (byte) 89, (byte) 235, (byte) 38, (byte) 106,
        (byte) 190, (byte) 217, (byte) 112, (byte) 144, (byte) 171, (byte) 230, (byte) 49,
        (byte) 83,
        (byte) 245, (byte) 4, (byte) 12, (byte) 20, (byte) 60, (byte) 68, (byte) 204, (byte) 79,
        (byte) 209, (byte) 104, (byte) 184, (byte) 211, (byte) 110, (byte) 178, (byte) 205,
        (byte) 76,
        (byte) 212, (byte) 103, (byte) 169, (byte) 224, (byte) 59, (byte) 77, (byte) 215, (byte) 98,
        (byte) 166, (byte) 241, (byte) 8, (byte) 24, (byte) 40, (byte) 120, (byte) 136, (byte) 131,
        (byte) 158, (byte) 185, (byte) 208, (byte) 107, (byte) 189, (byte) 220, (byte) 127,
        (byte) 129,
        (byte) 152, (byte) 179, (byte) 206, (byte) 73, (byte) 219, (byte) 118, (byte) 154,
        (byte) 181,
        (byte) 196, (byte) 87, (byte) 249, (byte) 16, (byte) 48, (byte) 80, (byte) 240, (byte) 11,
        (byte) 29, (byte) 39, (byte) 105, (byte) 187, (byte) 214, (byte) 97, (byte) 163, (byte) 254,
        (byte) 25, (byte) 43, (byte) 125, (byte) 135, (byte) 146, (byte) 173, (byte) 236, (byte) 47,
        (byte) 113, (byte) 147, (byte) 174, (byte) 233, (byte) 32, (byte) 96, (byte) 160,
        (byte) 251,
        (byte) 22, (byte) 58, (byte) 78, (byte) 210, (byte) 109, (byte) 183, (byte) 194, (byte) 93,
        (byte) 231, (byte) 50, (byte) 86, (byte) 250, (byte) 21, (byte) 63, (byte) 65, (byte) 195,
        (byte) 94, (byte) 226, (byte) 61, (byte) 71, (byte) 201, (byte) 64, (byte) 192, (byte) 91,
        (byte) 237, (byte) 44, (byte) 116, (byte) 156, (byte) 191, (byte) 218, (byte) 117,
        (byte) 159,
        (byte) 186, (byte) 213, (byte) 100, (byte) 172, (byte) 239, (byte) 42, (byte) 126,
        (byte) 130,
        (byte) 157, (byte) 188, (byte) 223, (byte) 122, (byte) 142, (byte) 137, (byte) 128,
        (byte) 155,
        (byte) 182, (byte) 193, (byte) 88, (byte) 232, (byte) 35, (byte) 101, (byte) 175,
        (byte) 234,
        (byte) 37, (byte) 111, (byte) 177, (byte) 200, (byte) 67, (byte) 197, (byte) 84, (byte) 252,
        (byte) 31, (byte) 33, (byte) 99, (byte) 165, (byte) 244, (byte) 7, (byte) 9, (byte) 27,
        (byte) 45, (byte) 119, (byte) 153, (byte) 176, (byte) 203, (byte) 70, (byte) 202, (byte) 69,
        (byte) 207, (byte) 74, (byte) 222, (byte) 121, (byte) 139, (byte) 134, (byte) 145,
        (byte) 168,
        (byte) 227, (byte) 62, (byte) 66, (byte) 198, (byte) 81, (byte) 243, (byte) 14, (byte) 18,
        (byte) 54, (byte) 90, (byte) 238, (byte) 41, (byte) 123, (byte) 141, (byte) 140, (byte) 143,
        (byte) 138, (byte) 133, (byte) 148, (byte) 167, (byte) 242, (byte) 13, (byte) 23, (byte) 57,
        (byte) 75, (byte) 221, (byte) 124, (byte) 132, (byte) 151, (byte) 162, (byte) 253,
        (byte) 28,
        (byte) 36, (byte) 108, (byte) 180, (byte) 199, (byte) 82, (byte) 246, (byte) 1,
    },
            
            aLogtable = {
                (byte) 0, (byte) 3, (byte) 5, (byte) 15, (byte) 17, (byte) 51, (byte) 85,
                (byte) 255,
                (byte) 26, (byte) 46, (byte) 114, (byte) 150, (byte) 161, (byte) 248, (byte) 19,
                (byte) 53,
                (byte) 95, (byte) 225, (byte) 56, (byte) 72, (byte) 216, (byte) 115, (byte) 149,
                (byte) 164,
                (byte) 247, (byte) 2, (byte) 6, (byte) 10, (byte) 30, (byte) 34, (byte) 102,
                (byte) 170,
                (byte) 229, (byte) 52, (byte) 92, (byte) 228, (byte) 55, (byte) 89, (byte) 235,
                (byte) 38,
                (byte) 106, (byte) 190, (byte) 217, (byte) 112, (byte) 144, (byte) 171, (byte) 230,
                (byte) 49,
                (byte) 83, (byte) 245, (byte) 4, (byte) 12, (byte) 20, (byte) 60, (byte) 68,
                (byte) 204,
                (byte) 79, (byte) 209, (byte) 104, (byte) 184, (byte) 211, (byte) 110, (byte) 178,
                (byte) 205,
                (byte) 76, (byte) 212, (byte) 103, (byte) 169, (byte) 224, (byte) 59, (byte) 77,
                (byte) 215,
                (byte) 98, (byte) 166, (byte) 241, (byte) 8, (byte) 24, (byte) 40, (byte) 120,
                (byte) 136,
                (byte) 131, (byte) 158, (byte) 185, (byte) 208, (byte) 107, (byte) 189, (byte) 220,
                (byte) 127,
                (byte) 129, (byte) 152, (byte) 179, (byte) 206, (byte) 73, (byte) 219, (byte) 118,
                (byte) 154,
                (byte) 181, (byte) 196, (byte) 87, (byte) 249, (byte) 16, (byte) 48, (byte) 80,
                (byte) 240,
                (byte) 11, (byte) 29, (byte) 39, (byte) 105, (byte) 187, (byte) 214, (byte) 97,
                (byte) 163,
                (byte) 254, (byte) 25, (byte) 43, (byte) 125, (byte) 135, (byte) 146, (byte) 173,
                (byte) 236,
                (byte) 47, (byte) 113, (byte) 147, (byte) 174, (byte) 233, (byte) 32, (byte) 96,
                (byte) 160,
                (byte) 251, (byte) 22, (byte) 58, (byte) 78, (byte) 210, (byte) 109, (byte) 183,
                (byte) 194,
                (byte) 93, (byte) 231, (byte) 50, (byte) 86, (byte) 250, (byte) 21, (byte) 63,
                (byte) 65,
                (byte) 195, (byte) 94, (byte) 226, (byte) 61, (byte) 71, (byte) 201, (byte) 64,
                (byte) 192,
                (byte) 91, (byte) 237, (byte) 44, (byte) 116, (byte) 156, (byte) 191, (byte) 218,
                (byte) 117,
                (byte) 159, (byte) 186, (byte) 213, (byte) 100, (byte) 172, (byte) 239, (byte) 42,
                (byte) 126,
                (byte) 130, (byte) 157, (byte) 188, (byte) 223, (byte) 122, (byte) 142, (byte) 137,
                (byte) 128,
                (byte) 155, (byte) 182, (byte) 193, (byte) 88, (byte) 232, (byte) 35, (byte) 101,
                (byte) 175,
                (byte) 234, (byte) 37, (byte) 111, (byte) 177, (byte) 200, (byte) 67, (byte) 197,
                (byte) 84,
                (byte) 252, (byte) 31, (byte) 33, (byte) 99, (byte) 165, (byte) 244, (byte) 7,
                (byte) 9,
                (byte) 27, (byte) 45, (byte) 119, (byte) 153, (byte) 176, (byte) 203, (byte) 70,
                (byte) 202,
                (byte) 69, (byte) 207, (byte) 74, (byte) 222, (byte) 121, (byte) 139, (byte) 134,
                (byte) 145,
                (byte) 168, (byte) 227, (byte) 62, (byte) 66, (byte) 198, (byte) 81, (byte) 243,
                (byte) 14,
                (byte) 18, (byte) 54, (byte) 90, (byte) 238, (byte) 41, (byte) 123, (byte) 141,
                (byte) 140,
                (byte) 143, (byte) 138, (byte) 133, (byte) 148, (byte) 167, (byte) 242, (byte) 13,
                (byte) 23,
                (byte) 57, (byte) 75, (byte) 221, (byte) 124, (byte) 132, (byte) 151, (byte) 162,
                (byte) 253,
                (byte) 28, (byte) 36, (byte) 108, (byte) 180, (byte) 199, (byte) 82, (byte) 246,
                (byte) 1,
                (byte) 3, (byte) 5, (byte) 15, (byte) 17, (byte) 51, (byte) 85, (byte) 255,
                (byte) 26,
                (byte) 46, (byte) 114, (byte) 150, (byte) 161, (byte) 248, (byte) 19, (byte) 53,
                (byte) 95,
                (byte) 225, (byte) 56, (byte) 72, (byte) 216, (byte) 115, (byte) 149, (byte) 164,
                (byte) 247,
                (byte) 2, (byte) 6, (byte) 10, (byte) 30, (byte) 34, (byte) 102, (byte) 170,
                (byte) 229,
                (byte) 52, (byte) 92, (byte) 228, (byte) 55, (byte) 89, (byte) 235, (byte) 38,
                (byte) 106,
                (byte) 190, (byte) 217, (byte) 112, (byte) 144, (byte) 171, (byte) 230, (byte) 49,
                (byte) 83,
                (byte) 245, (byte) 4, (byte) 12, (byte) 20, (byte) 60, (byte) 68, (byte) 204,
                (byte) 79,
                (byte) 209, (byte) 104, (byte) 184, (byte) 211, (byte) 110, (byte) 178, (byte) 205,
                (byte) 76,
                (byte) 212, (byte) 103, (byte) 169, (byte) 224, (byte) 59, (byte) 77, (byte) 215,
                (byte) 98,
                (byte) 166, (byte) 241, (byte) 8, (byte) 24, (byte) 40, (byte) 120, (byte) 136,
                (byte) 131,
                (byte) 158, (byte) 185, (byte) 208, (byte) 107, (byte) 189, (byte) 220, (byte) 127,
                (byte) 129,
                (byte) 152, (byte) 179, (byte) 206, (byte) 73, (byte) 219, (byte) 118, (byte) 154,
                (byte) 181,
                (byte) 196, (byte) 87, (byte) 249, (byte) 16, (byte) 48, (byte) 80, (byte) 240,
                (byte) 11,
                (byte) 29, (byte) 39, (byte) 105, (byte) 187, (byte) 214, (byte) 97, (byte) 163,
                (byte) 254,
                (byte) 25, (byte) 43, (byte) 125, (byte) 135, (byte) 146, (byte) 173, (byte) 236,
                (byte) 47,
                (byte) 113, (byte) 147, (byte) 174, (byte) 233, (byte) 32, (byte) 96, (byte) 160,
                (byte) 251,
                (byte) 22, (byte) 58, (byte) 78, (byte) 210, (byte) 109, (byte) 183, (byte) 194,
                (byte) 93,
                (byte) 231, (byte) 50, (byte) 86, (byte) 250, (byte) 21, (byte) 63, (byte) 65,
                (byte) 195,
                (byte) 94, (byte) 226, (byte) 61, (byte) 71, (byte) 201, (byte) 64, (byte) 192,
                (byte) 91,
                (byte) 237, (byte) 44, (byte) 116, (byte) 156, (byte) 191, (byte) 218, (byte) 117,
                (byte) 159,
                (byte) 186, (byte) 213, (byte) 100, (byte) 172, (byte) 239, (byte) 42, (byte) 126,
                (byte) 130,
                (byte) 157, (byte) 188, (byte) 223, (byte) 122, (byte) 142, (byte) 137, (byte) 128,
                (byte) 155,
                (byte) 182, (byte) 193, (byte) 88, (byte) 232, (byte) 35, (byte) 101, (byte) 175,
                (byte) 234,
                (byte) 37, (byte) 111, (byte) 177, (byte) 200, (byte) 67, (byte) 197, (byte) 84,
                (byte) 252,
                (byte) 31, (byte) 33, (byte) 99, (byte) 165, (byte) 244, (byte) 7, (byte) 9,
                (byte) 27,
                (byte) 45, (byte) 119, (byte) 153, (byte) 176, (byte) 203, (byte) 70, (byte) 202,
                (byte) 69,
                (byte) 207, (byte) 74, (byte) 222, (byte) 121, (byte) 139, (byte) 134, (byte) 145,
                (byte) 168,
                (byte) 227, (byte) 62, (byte) 66, (byte) 198, (byte) 81, (byte) 243, (byte) 14,
                (byte) 18,
                (byte) 54, (byte) 90, (byte) 238, (byte) 41, (byte) 123, (byte) 141, (byte) 140,
                (byte) 143,
                (byte) 138, (byte) 133, (byte) 148, (byte) 167, (byte) 242, (byte) 13, (byte) 23,
                (byte) 57,
                (byte) 75, (byte) 221, (byte) 124, (byte) 132, (byte) 151, (byte) 162, (byte) 253,
                (byte) 28,
                (byte) 36, (byte) 108, (byte) 180, (byte) 199, (byte) 82, (byte) 246, (byte) 1,
            },
            
            // S-box
            S = {
                (byte) 99, (byte) 124, (byte) 119, (byte) 123, (byte) 242, (byte) 107, (byte) 111,
                (byte) 197,
                (byte) 48, (byte) 1, (byte) 103, (byte) 43, (byte) 254, (byte) 215, (byte) 171,
                (byte) 118,
                (byte) 202, (byte) 130, (byte) 201, (byte) 125, (byte) 250, (byte) 89, (byte) 71,
                (byte) 240,
                (byte) 173, (byte) 212, (byte) 162, (byte) 175, (byte) 156, (byte) 164, (byte) 114,
                (byte) 192,
                (byte) 183, (byte) 253, (byte) 147, (byte) 38, (byte) 54, (byte) 63, (byte) 247,
                (byte) 204,
                (byte) 52, (byte) 165, (byte) 229, (byte) 241, (byte) 113, (byte) 216, (byte) 49,
                (byte) 21,
                (byte) 4, (byte) 199, (byte) 35, (byte) 195, (byte) 24, (byte) 150, (byte) 5,
                (byte) 154,
                (byte) 7, (byte) 18, (byte) 128, (byte) 226, (byte) 235, (byte) 39, (byte) 178,
                (byte) 117,
                (byte) 9, (byte) 131, (byte) 44, (byte) 26, (byte) 27, (byte) 110, (byte) 90,
                (byte) 160,
                (byte) 82, (byte) 59, (byte) 214, (byte) 179, (byte) 41, (byte) 227, (byte) 47,
                (byte) 132,
                (byte) 83, (byte) 209, (byte) 0, (byte) 237, (byte) 32, (byte) 252, (byte) 177,
                (byte) 91,
                (byte) 106, (byte) 203, (byte) 190, (byte) 57, (byte) 74, (byte) 76, (byte) 88,
                (byte) 207,
                (byte) 208, (byte) 239, (byte) 170, (byte) 251, (byte) 67, (byte) 77, (byte) 51,
                (byte) 133,
                (byte) 69, (byte) 249, (byte) 2, (byte) 127, (byte) 80, (byte) 60, (byte) 159,
                (byte) 168,
                (byte) 81, (byte) 163, (byte) 64, (byte) 143, (byte) 146, (byte) 157, (byte) 56,
                (byte) 245,
                (byte) 188, (byte) 182, (byte) 218, (byte) 33, (byte) 16, (byte) 255, (byte) 243,
                (byte) 210,
                (byte) 205, (byte) 12, (byte) 19, (byte) 236, (byte) 95, (byte) 151, (byte) 68,
                (byte) 23,
                (byte) 196, (byte) 167, (byte) 126, (byte) 61, (byte) 100, (byte) 93, (byte) 25,
                (byte) 115,
                (byte) 96, (byte) 129, (byte) 79, (byte) 220, (byte) 34, (byte) 42, (byte) 144,
                (byte) 136,
                (byte) 70, (byte) 238, (byte) 184, (byte) 20, (byte) 222, (byte) 94, (byte) 11,
                (byte) 219,
                (byte) 224, (byte) 50, (byte) 58, (byte) 10, (byte) 73, (byte) 6, (byte) 36,
                (byte) 92,
                (byte) 194, (byte) 211, (byte) 172, (byte) 98, (byte) 145, (byte) 149, (byte) 228,
                (byte) 121,
                (byte) 231, (byte) 200, (byte) 55, (byte) 109, (byte) 141, (byte) 213, (byte) 78,
                (byte) 169,
                (byte) 108, (byte) 86, (byte) 244, (byte) 234, (byte) 101, (byte) 122, (byte) 174,
                (byte) 8,
                (byte) 186, (byte) 120, (byte) 37, (byte) 46, (byte) 28, (byte) 166, (byte) 180,
                (byte) 198,
                (byte) 232, (byte) 221, (byte) 116, (byte) 31, (byte) 75, (byte) 189, (byte) 139,
                (byte) 138,
                (byte) 112, (byte) 62, (byte) 181, (byte) 102, (byte) 72, (byte) 3, (byte) 246,
                (byte) 14,
                (byte) 97, (byte) 53, (byte) 87, (byte) 185, (byte) 134, (byte) 193, (byte) 29,
                (byte) 158,
                (byte) 225, (byte) 248, (byte) 152, (byte) 17, (byte) 105, (byte) 217, (byte) 142,
                (byte) 148,
                (byte) 155, (byte) 30, (byte) 135, (byte) 233, (byte) 206, (byte) 85, (byte) 40,
                (byte) 223,
                (byte) 140, (byte) 161, (byte) 137, (byte) 13, (byte) 191, (byte) 230, (byte) 66,
                (byte) 104,
                (byte) 65, (byte) 153, (byte) 45, (byte) 15, (byte) 176, (byte) 84, (byte) 187,
                (byte) 22,
            },
            
            // inv S-box
            Si = {
                (byte) 82, (byte) 9, (byte) 106, (byte) 213, (byte) 48, (byte) 54, (byte) 165,
                (byte) 56,
                (byte) 191, (byte) 64, (byte) 163, (byte) 158, (byte) 129, (byte) 243, (byte) 215,
                (byte) 251,
                (byte) 124, (byte) 227, (byte) 57, (byte) 130, (byte) 155, (byte) 47, (byte) 255,
                (byte) 135,
                (byte) 52, (byte) 142, (byte) 67, (byte) 68, (byte) 196, (byte) 222, (byte) 233,
                (byte) 203,
                (byte) 84, (byte) 123, (byte) 148, (byte) 50, (byte) 166, (byte) 194, (byte) 35,
                (byte) 61,
                (byte) 238, (byte) 76, (byte) 149, (byte) 11, (byte) 66, (byte) 250, (byte) 195,
                (byte) 78,
                (byte) 8, (byte) 46, (byte) 161, (byte) 102, (byte) 40, (byte) 217, (byte) 36,
                (byte) 178,
                (byte) 118, (byte) 91, (byte) 162, (byte) 73, (byte) 109, (byte) 139, (byte) 209,
                (byte) 37,
                (byte) 114, (byte) 248, (byte) 246, (byte) 100, (byte) 134, (byte) 104, (byte) 152,
                (byte) 22,
                (byte) 212, (byte) 164, (byte) 92, (byte) 204, (byte) 93, (byte) 101, (byte) 182,
                (byte) 146,
                (byte) 108, (byte) 112, (byte) 72, (byte) 80, (byte) 253, (byte) 237, (byte) 185,
                (byte) 218,
                (byte) 94, (byte) 21, (byte) 70, (byte) 87, (byte) 167, (byte) 141, (byte) 157,
                (byte) 132,
                (byte) 144, (byte) 216, (byte) 171, (byte) 0, (byte) 140, (byte) 188, (byte) 211,
                (byte) 10,
                (byte) 247, (byte) 228, (byte) 88, (byte) 5, (byte) 184, (byte) 179, (byte) 69,
                (byte) 6,
                (byte) 208, (byte) 44, (byte) 30, (byte) 143, (byte) 202, (byte) 63, (byte) 15,
                (byte) 2,
                (byte) 193, (byte) 175, (byte) 189, (byte) 3, (byte) 1, (byte) 19, (byte) 138,
                (byte) 107,
                (byte) 58, (byte) 145, (byte) 17, (byte) 65, (byte) 79, (byte) 103, (byte) 220,
                (byte) 234,
                (byte) 151, (byte) 242, (byte) 207, (byte) 206, (byte) 240, (byte) 180, (byte) 230,
                (byte) 115,
                (byte) 150, (byte) 172, (byte) 116, (byte) 34, (byte) 231, (byte) 173, (byte) 53,
                (byte) 133,
                (byte) 226, (byte) 249, (byte) 55, (byte) 232, (byte) 28, (byte) 117, (byte) 223,
                (byte) 110,
                (byte) 71, (byte) 241, (byte) 26, (byte) 113, (byte) 29, (byte) 41, (byte) 197,
                (byte) 137,
                (byte) 111, (byte) 183, (byte) 98, (byte) 14, (byte) 170, (byte) 24, (byte) 190,
                (byte) 27,
                (byte) 252, (byte) 86, (byte) 62, (byte) 75, (byte) 198, (byte) 210, (byte) 121,
                (byte) 32,
                (byte) 154, (byte) 219, (byte) 192, (byte) 254, (byte) 120, (byte) 205, (byte) 90,
                (byte) 244,
                (byte) 31, (byte) 221, (byte) 168, (byte) 51, (byte) 136, (byte) 7, (byte) 199,
                (byte) 49,
                (byte) 177, (byte) 18, (byte) 16, (byte) 89, (byte) 39, (byte) 128, (byte) 236,
                (byte) 95,
                (byte) 96, (byte) 81, (byte) 127, (byte) 169, (byte) 25, (byte) 181, (byte) 74,
                (byte) 13,
                (byte) 45, (byte) 229, (byte) 122, (byte) 159, (byte) 147, (byte) 201, (byte) 156,
                (byte) 239,
                (byte) 160, (byte) 224, (byte) 59, (byte) 77, (byte) 174, (byte) 42, (byte) 245,
                (byte) 176,
                (byte) 200, (byte) 235, (byte) 187, (byte) 60, (byte) 131, (byte) 83, (byte) 153,
                (byte) 97,
                (byte) 23, (byte) 43, (byte) 4, (byte) 126, (byte) 186, (byte) 119, (byte) 214,
                (byte) 38,
                (byte) 225, (byte) 105, (byte) 20, (byte) 99, (byte) 85, (byte) 33, (byte) 12,
                (byte) 125,
            };
    
    private static final int[] Rcon = {
        0x01, 0x02, 0x04, 0x08, 0x10,
        0x20, 0x40, 0x80, 0x1b, 0x36,
        0x6c, 0xd8, 0xab, 0x4d, 0x9a,
        0x2f, 0x5e, 0xbc, 0x63, 0xc6,
        0x97, 0x35, 0x6a, 0xd4, 0xb3,
        0x7d, 0xfa, 0xef, 0xc5, 0x91,
    };
    
    private static byte[][]
    
    shifts0 = {
        {0, 8, 16, 24},
        {0, 8, 16, 24},
        {0, 8, 16, 24},
        {0, 8, 16, 32},
        {0, 8, 24, 32},
    },
            
            shifts1 = {
                {0, 24, 16, 8},
                {0, 32, 24, 16},
                {0, 40, 32, 24},
                {0, 48, 40, 24},
                {0, 56, 40, 32},
            };
    
    private final int blockBits,
            bc,
            bc8,
            blockSize;
    
    private int numRounds;
    
    private final int numRounds1bc8;
    private final long bcMask;
    private final long[][] workingKey;
    private final long[] a = new long[4]; // factored
    private final byte[] shifts0SC;
    private final byte[] shifts1SC;
    
    public RijndaelCipher(final Mode mode, final int blockBits, final byte[] key) {
        super(mode);
        if (blockBits < 128
                || blockBits > 256
                || blockBits % 32 != 0) {
            throw new IllegalArgumentException("illegal blocksize");
        }
        this.blockBits = blockBits;
        bc = blockBits / 4;
        bc8 = bc / 8;
        numRounds1bc8 = (numRounds + 1) * bc8;
        blockSize = bc / 2;
        final int i = bc8 - 4;
        bcMask = 0xFFFFFFFFFFFFFFFFL >>> bc - 64;
        shifts0SC = shifts0[i];
        shifts1SC = shifts1[i];
        workingKey = generateWorkingKey(key);
    }
    
    // add extra methods with default mode
    
    public RijndaelCipher(final Mode mode, final int blockBits, final String key) {
        this(mode, blockBits, key.getBytes(charset));
    }
    
    @Override
    public int getBlockSize() {
        return blockSize;
    }
    
    /**
     * multiply two elements of GF(2^m)
     * needed for mixColumn and invMixColumn
     */
    
    // all muls factored
    
    private byte mul0x2(final int b) {
        if (b == 0) {
            return 0;
        }
        return aLogtable[25 + (logtable[b] & 0xFF)];
    }
    
    private byte mul0x3(final int b) {
        if (b == 0) {
            return 0;
        }
        return aLogtable[1 + (logtable[b] & 0xFF)];
    }
    
    private byte mul0x9(final int b) {
        if (b < 0) {
            return 0;
        }
        return aLogtable[104 + b];
    }
    
    private byte mul0xb(final int b) {
        if (b < 0) {
            return 0;
        }
        return aLogtable[104 + b];
    }
    
    private byte mul0xd(final int b) {
        if (b < 0) {
            return 0;
        }
        return aLogtable[238 + b];
    }
    
    private byte mul0xe(final int b) {
        if (b < 0) {
            return 0;
        }
        return aLogtable[223 + b];
    }
    
    // factored
    private void addRoundKey(final long[] rk) {
        for (int i = 0; i < 4; i++) {
            a[i] = rk[i];
        }
    }
    
    private long rotWord(final long r, final int shift) {
        return (r >>> shift | r << bc - shift) & bcMask;
    }
    
    // factored
    private void shiftRows(final byte[] shiftsSC) {
        for (int i = 1; i < 4; i++) {
            a[i] = rotWord(a[i], shiftsSC[i]);
        }
    }
    
    private long applyS(final long r, final byte[] state) {
        long res = 0;
        for (int i = 0; i < bc; i += 8) {
            res |= (long) (state[(int) (r >> i & 0xFF)] & 0xFF) << i;
        }
        return res;
    }
    
    private void subBytes(final byte[] state) {
        for (int i = 0; i < 4; i++) {
            a[i] = applyS(a[i], state);
        }
    }
    
    // factored
    private void mixColumns() {
        final long[] r = new long[4];
        for (int i = 0; i < bc; i += 8) {
            final int[] b = new int[4];
            for (int j = 0; j < 4; j++) {
                b[j] = (int) (a[j] >> i & 0xFF);
            }
            for (int j = 0; j < 4; j++) {
                r[j] |= (long) ((mul0x2(b[j])
                        ^ mul0x3(b[(j + 1) % 4])
                        ^ b[(j + 2) % 4]
                        ^ b[(j + 3) % 4]) & 0xFF) << i;
            }
        }
        for (int i = 0; i < 4; i++) {
            a[i] = r[i];
        }
    }
    
    private void invMixColumns() {
        final long[] r = new long[4];
        for (int i = 0; i < bc; i += 8) {
            final int[] b = new int[4];
            for (int j = 0; j < 4; j++) {
                b[j] = (int) (a[j] >> i & 0xFF);
            }
            
            // pre-lookup logtable
            for (int j = 0; j < 4; j++) {
                final int c = b[j];
                b[j] = c != 0 ? logtable[c & 0xFF] & 0xFF : -1;
            }
            
            for (int j = 0; j < 4; j++) {
                r[j] |= (long) ((mul0xe(b[j])
                        ^ mul0xb(b[(j + 1) % 4])
                        ^ mul0xd(b[(j + 2) % 4])
                        ^ mul0x9(b[(j + 3) % 4])) & 0xFF) << i;
            }
        }
        for (int i = 0; i < 4; i++) {
            a[i] = r[i];
        }
    }
    
    private long[][] generateWorkingKey(final byte[] key) {
        // Kc factored
        final int Kc = key.length / 4,
                keyBits = key.length * 8;
        final byte[][] tk = new byte[4][MAX_Kc];
        final long[][] W = new long[MAX_ROUNDS + 1][4];
        
        if (Kc < 4 || Kc > 8) {
            throw new IllegalArgumentException(
                    "illegal key length, must be between 16 and 32; key.length = " + key.length);
        }
        
        if (keyBits >= blockBits) {
            numRounds = Kc + 6;
        } else {
            numRounds = bc8 + 6;
        }
        
        // copy key into processing area
        int index = 0;
        for (int i = 0; i < key.length; i++) {
            tk[i % 4][i / 4] = key[index++];
        }
        
        // copy values into round key array
        int t = 0;
        for (int j = 0; j < Kc && t < numRounds1bc8; j++, t++) {
            for (int i = 0; i < 4; i++) {
                W[t / bc8][i] |= (tk[i][j] & 0xFF) << t * 8 % bc;
            }
        }
        
        // while not enough round key material calculated, 
        // calculate new values
        int Rconpointer = 0;
        while (t < numRounds1bc8) {
            for (int i = 0; i < 4; i++) {
                tk[i][0] ^= S[tk[(i + 1) % 4][Kc - 1] & 0xFF];
            }
            tk[0][0] ^= Rcon[Rconpointer++];
            
            // should be factored
            if (Kc <= 6) {
                for (int j = 1; j < Kc; j++) {
                    for (int i = 0; i < 4; i++) {
                        tk[i][j] ^= tk[i][j - 1];
                    }
                }
            } else {
                for (int j = 1; j < 4; j++) {
                    for (int i = 0; i < 4; i++) {
                        tk[i][j] ^= tk[i][j - 1];
                    }
                }
                for (int i = 0; i < 4; i++) {
                    tk[i][4] ^= S[tk[i][3] & 0xFF];
                }
                for (int j = 5; j < Kc; j++) {
                    for (int i = 0; i < 4; i++) {
                        tk[i][j] ^= tk[i][j - 1];
                    }
                }
            }
            
            // same as above, should be factored
            // copy values into round key array
            for (int j = 0; j < Kc && t < numRounds1bc8; j++, t++) {
                for (int i = 0; i < 4; i++) {
                    W[t / bc8][i] |= (tk[i][j] & 0xFF) << t * 8 % bc;
                }
            }
        }
        
        return W;
    }
    
    private void unpackBlock(final byte[] bytes) {
        System.out.println(Arrays.toString(bytes));
        int index = 0;
        for (int i = 0; i < 4; i++) {
            a[i] = bytes[index++] & 0xFF;
        }
        for (int j = 8; j != bc; j += 8) {
            for (int i = 0; i < 4; i++) {
                System.out.println(index + 1);
                a[i] |= (bytes[index++] & 0xFF) << j;
            }
        }
    }
    
    private byte[] packBlock() {
        int index = 0;
        final byte[] bytes = new byte[blockSize];
        for (int j = 0; j != bc; j += 8) {
            for (int i = 0; i < 4; i++) {
                bytes[index++] = (byte) (a[i] >> j);
            }
        }
        return bytes;
    }
    
    private void encryptBlock(final long[][] rk) {
        // first addRoundKey
        addRoundKey(rk[0]);
        
        // numRounds - 1 ordinary rounds
        for (int r = 1; r < numRounds; r++) {
            subBytes(S);
            shiftRows(shifts0SC);
            mixColumns();
            addRoundKey(rk[r]);
        }
        
        // last round with no mixColumns
        subBytes(S);
        shiftRows(shifts0SC);
        addRoundKey(rk[numRounds]);
    }
    
    private void decryptBlock(final long[][] rk) {
        // inverse of encryption
        
        // first round with no invMixColumns
        addRoundKey(rk[numRounds]);
        subBytes(Si);
        shiftRows(shifts1SC);
        
        // numRouds - 1 ordinary rounds
        for (int r = numRounds - 1; r > 0; r--) {
            addRoundKey(rk[r]);
            invMixColumns();
            subBytes(Si);
            shiftRows(shifts1SC);
        }
        
        // end with addRoundKey
        addRoundKey(rk[0]);
    }
    
    @Override
    public byte[] encryptBlock(final byte[] plainBlock) {
        unpackBlock(plainBlock);
        encryptBlock(workingKey);
        return packBlock();
    }
    
    @Override
    public byte[] decryptBlock(final byte[] cipherBlock) {
        unpackBlock(cipherBlock);
        decryptBlock(workingKey);
        return packBlock();
    }
    
    public static void main(final String[] args) throws Exception {
        
        /*Cryptor crypt = new Cryptor(new RijndaelCipher(new ElectronicCodebookMode(), 256, "passwordpassword"));
        String input = "hello, world";
        String ciphertext = crypt.crypt(input, true);
        String plaintext = crypt.crypt(ciphertext, true);
        
        System.out.println(input);
        System.out.println();
        
        System.out.println(ciphertext);
        System.out.println();
        
        System.out.println(plaintext);
        System.out.println();*/
        
        final RijndaelCipher rijn = new RijndaelCipher(new ECB(), 256, "passwordpassword");
        
        final byte[] plainIn = "super hello world and again super hello world".getBytes(charset);
        final byte[] cipherbytes = rijn.encrypt(plainIn);
        final byte[] plainOut = rijn.decrypt(cipherbytes);
        
        System.out.println(new String(plainIn));
        System.out.println();
        
        System.out.println(new String(cipherbytes));
        System.out.println();
        
        System.out.println(new String(plainOut));
        System.out.println();
        
    }
    
}
