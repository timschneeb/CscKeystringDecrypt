import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;

public class Encrypt64 {
    public static final String ALGORITHM = "RSA";
    private static final String TAG = "Encrypt64";

    static
    {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static byte[] decrypt(byte[] bArr, PrivateKey privateKey) {
        try {
            Cipher instance = Cipher.getInstance("RSA", "BC");
            instance.init(2, privateKey);
            int i = 64;
            long length = (long) bArr.length;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            long j = 0;
            while (true) {
                if (j >= length) {
                    break;
                }
                long j2 = length - j;
                long j3 = (long) i;
                if (j2 > j3) {
                    j2 = j3;
                }
                byte[] bArr2 = new byte[((int) j2)];
                for (long j4 = 0; j4 < j2; j4++) {
                    bArr2[(int) j4] = bArr[(int) (j4 + j)];
                }
                j += j2;
                byte[] doFinal = instance.doFinal(bArr2);
                if (doFinal == null) {
                    break;
                }
                byteArrayOutputStream.write(doFinal);
                if (j2 < j3) {
                    break;
                }
                length = length;
                i = 64;
            }
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            return byteArray;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static PrivateKey readPrivateKey() {
        byte[] privateMod = Base64.getDecoder().decode("AM8hujChFEBYDIby9qe1jysxm/p7ofwDUpHpgbaD9MCaWuuCQCBkGBeDRSW2DlEgTqQ2pvXECxeHBDGBlY3CYW0=");
        byte[] privateExp = Base64.getDecoder().decode("AIeTyvxjow7O0zxNsjdqNtEOAsu8uvVq81QApN/jlYOvFpnzjEMNAaVNJwz/IATCiUPSU1Y8ETCQAqvzphx/kj0=");

        BigInteger mod = new BigInteger(privateMod);
        BigInteger exp = new BigInteger(privateExp);
        SemLog.secD(TAG, "private mod:" + mod.toString());
        SemLog.secD(TAG, "private exp:" + exp.toString());

        try {
            return KeyFactory.getInstance("RSA").generatePrivate(new RSAPrivateKeySpec(mod, exp));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}