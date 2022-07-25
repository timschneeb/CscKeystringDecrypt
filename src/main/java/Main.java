import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;

public class Main {
    private static final String TAG = "KeystringDecrypt";

    public static void main(String[] args) throws IOException {


        if(args.length != 1)
        {
            System.out.println("Usage: CscKeystringDecrypt <encrypted file>");
            return;
        }

        PrivateKey readPrivateKey = Encrypt64.readPrivateKey();
        if (readPrivateKey == null) {
            SemLog.secD(TAG, "key file not found");
            return;
        }

        File file = new File(args[0]);
        if (file.exists()) {
            ByteArrayInputStream fileInputStream = new ByteArrayInputStream(new FileInputStream(file).readAllBytes());
            byte[] decrypt = Encrypt64.decrypt(convertIStoByte(fileInputStream), readPrivateKey);
            if (decrypt != null) {
                System.out.println(new String(decrypt, StandardCharsets.UTF_8));

                FileOutputStream out = new FileOutputStream("keystrings.xml");
                out.write(decrypt);
                out.close();
                SemLog.secI(TAG, "Saved as keystrings.xml");
            }
            else
            {
                SemLog.secI(TAG, args[0] + ": failed to decrypt");
            }
            fileInputStream.close();
        } else {
            SemLog.secI(TAG, args[0] + ": file not found");
        }
    }

    private static byte[] convertIStoByte(InputStream inputStream) {
        int i;
        try {
            i = inputStream.available();
        } catch (IOException e) {
            e.printStackTrace();
            i = 0;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[i];
        while (true) {
            try {
                int read = inputStream.read(bArr);
                if (read == -1) {
                    break;
                }
                byteArrayOutputStream.write(bArr, 0, read);
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return byteArrayOutputStream.toByteArray();
    }
}
