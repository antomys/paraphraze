import java.io.*;

public class TxtLoader {

    static BufferedReader br;
    public static String readFile2(String file) throws FileNotFoundException, IOException {

        StringBuffer ss = new StringBuffer();
        br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        int a; char[] buf = new char[1024];

        while (true){
            a = br.read(buf);
            if (a==-1) {br.close(); break;}
            if (a==1024) {ss.append(buf);} else {ss.append(buf, 0, a);}
        }
        return ss.substring(0);
    }
}