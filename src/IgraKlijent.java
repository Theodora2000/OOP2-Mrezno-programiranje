import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class IgraKlijent {
    public static void main(String args[]){
    //koektujemo se na soket
        try {
            Socket s = new Socket("localhost", 1234);

            try{
                System.out.println("Povezali smo se na server");
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));//citanje
                PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);//pisanje

                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));//citanje

                System.out.println("Kako se zoves");
                String nadimak = stdIn.readLine();
                boolean kraj = false;
                out.println(nadimak);
                while(!kraj){
                    System.out.println("Unesite broj: ");
                    String linija = stdIn.readLine();
                    out.println(linija);
                    String odgovor = in.readLine();
                    if(odgovor == null || "=".equals(odgovor)){
                        kraj = true;
                    }
                    System.out.println(odgovor);
                }
                String mojRang = in.readLine();
                System.out.println(mojRang);
            }
        finally {
            s.close();
        }
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
