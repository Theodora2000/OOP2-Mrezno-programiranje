import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class RangLista{//objekat koji koristi vise niti istovremeno
    private List<IgraStanje> lista = new ArrayList<>();
    public synchronized int dodaj(IgraStanje igra){

        lista.add(igra);
        lista.sort(Comparator.comparing(IgraStanje::getBrPokusaja));
        if(lista.size() > 10){
            lista.remove(10);
        }
        return 1+lista.indexOf(igra);
    }
}

//igra je samostalna nit koja se bahce sa citanjem pisanjem
public class IgraStanje extends Thread {

    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private final int broj;
    private String nadimak;
    private final RangLista rl;
    private int brPokusaja;

    public int getBrPokusaja() {
        return brPokusaja;
    }

    public IgraStanje(Socket klijent, RangLista rl) throws IOException {
        this.socket = klijent;//zapamtimo klijenta
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));//citanje
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);//pisanje
        this.broj = (int) (1.0 + Math.random()*100.0);
        this.rl = rl;
        this.brPokusaja = 0;
    }


    public void run(){
        try {
            nadimak = in.readLine();//citamo liniju nadiamk
            System.out.println("Primeljen nadimak " + nadimak);
            String linija = in.readLine();//onda dalje redom citamo
           boolean kraj = false;
            while(linija !=null && !kraj){
                try {
                    brPokusaja++;
                    System.out.println("Primili od klijenta " + linija);
                    int pokusaj = Integer.parseInt(linija);
                    if(pokusaj == broj){
                        out.println("=");
                        System.out.println("Pogodjen broj");
                        kraj = true;
                        int rang;
                        synchronized (rl) {
                             rang = rl.dodaj(this);
                        }
                        out.println(rang);
                    }else if(pokusaj<broj){
                        out.println(">");
                        System.out.println("veci broj");
                    }else{
                        out.println("<");
                        System.out.println("manji broj");
                    }
                }catch (NumberFormatException e){
                    out.println("!" + e.getMessage());
                }


                linija = in.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Igra je zavrsena!");
    }

}
