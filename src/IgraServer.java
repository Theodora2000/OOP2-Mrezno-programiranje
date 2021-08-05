import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class IgraServer {
    //server je apl koja osluskuje, ptavi igre
    public static void main(String[] args) {
        //serverski soket slusa na odgovarajucem portu
        //i ocekuje konekcije od strane klijenta
        try {
            ServerSocket ss = new ServerSocket(1234);
            RangLista rl = new RangLista();
            System.out.println("Server pokrenut");
            try {
                while (!Thread.interrupted()) {
                    //za svaku konekciju koju pronadje napravi soket
                    Socket klijent = ss.accept();//zapocnemo konekciju, pravimo soket za komunikaciju
                    //Socket sluzi za komunikaciju bas sa tim konkretnim klijentom koji smo dobili od accepta
                    System.out.println("Uspostvljen nova konekcija");
                    IgraStanje igra = new IgraStanje(klijent, rl);//ovo nam predstavlja konkretnu igru
                    //kad bi smo za svaku igru pravilu ranglistu onda nam ne treba synchronized
                    igra.start();//nova nit, za igranje , citanje
                    System.out.println("Pokrenuta nova igra");
                }
            }finally {
                ss.close();
            }
            } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
