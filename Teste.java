import java.util.Scanner;

public class Teste {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int seed, numeroDeFrames, numeroDeProcessos, timeQuantum;

        seed = in.nextInt();
        System.out.println("------------------- seed: " + seed);
        numeroDeFrames = in.nextInt();
        System.out.println("------------------- numeroDeFrames: " + numeroDeFrames);
        numeroDeProcessos = in.nextInt();
        System.out.println("------------------- numeroDeProcessos: " + numeroDeProcessos);
        timeQuantum = in.nextInt();
        System.out.println("------------------- timeQuantum: " + timeQuantum);
        // --------------------- Capturando processos ---------------------

        int identificacaoProcesso, numeroDePaginas, tempoDeChegada, tempoDeBurst;

        while (numeroDeProcessos > 0) {
            identificacaoProcesso = in.nextInt();
            numeroDePaginas = in.nextInt();
            tempoDeChegada = in.nextInt();
            tempoDeBurst = in.nextInt();

            numeroDeProcessos--;

        }
        in.close();

    }

}