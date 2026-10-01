import java.util.Scanner;

public class Programa
{
    public static void main (String[] args)
    {
        try
        {
            Scanner teclado = new Scanner (System.in);

            // ---------- montagem do vetor ----------

            int qtd=0;
            while (qtd<=0)
            {
                System.out.print ("Quantos elementos voce quer no vetor? ");
                try
                {
                    qtd = Integer.parseInt (teclado.nextLine());
                }
                catch (NumberFormatException erro)
                {} // qtd continua <= 0, e a pergunta se repete

                if (qtd<=0)
                    System.err.println ("Quantidade invalida!");
            }

            String opcao="";
            while (!opcao.equals("D") && !opcao.equals("A"))
            {
                System.out.print ("Voce quer (D)igitar os elementos ou gera-los (A)leatoriamente? ");
                opcao = teclado.nextLine().toUpperCase();
            }

            int[] vetor = new int [qtd];

            for (int i=0; i<qtd; i++)
                if (opcao.equals("A"))
                    vetor[i] = (int)(Math.random()*1000000); // de 0 a 999999
                else
                {
                    System.out.print ("Elemento "+i+": ");
                    try
                    {
                        vetor[i] = Integer.parseInt (teclado.nextLine());
                    }
                    catch (NumberFormatException erro)
                    {
                        System.err.println ("Numero invalido!");
                        i--; // pede o mesmo elemento de novo
                    }
                }

            // ---------- ordenacao ----------

            int qtdDeOrdenadoras = Runtime.getRuntime().availableProcessors()-1;

            if (qtdDeOrdenadoras<1)   // maquina com um so processador
                qtdDeOrdenadoras = 1;

            if (qtdDeOrdenadoras>qtd) // nao faz sentido haver pedaco vazio
                qtdDeOrdenadoras = qtd;

            System.out.println ();
            System.out.println ("Processadores: "+Runtime.getRuntime().availableProcessors());
            System.out.println ("Ordenadoras  : "+qtdDeOrdenadoras);

            long inicio = System.nanoTime();

            // divide o vetor em qtdDeOrdenadoras pedacos; quando a
            // divisao nao e exata, os primeiros pedacos ficam com 1
            // elemento a mais
            Ordenadora[] ordenadora = new Ordenadora [qtdDeOrdenadoras];
            int tamanho = qtd/qtdDeOrdenadoras;
            int resto   = qtd%qtdDeOrdenadoras;
            int posicao = 0;

            for (int i=0; i<qtdDeOrdenadoras; i++)
            {
                int[] pedaco = new int [i<resto?tamanho+1:tamanho];
                for (int j=0; j<pedaco.length; j++)
                    pedaco[j] = vetor[posicao++];

                ordenadora[i] = new Ordenadora (pedaco);
                ordenadora[i].start ();
            }

            for (int i=0; i<qtdDeOrdenadoras; i++)
                ordenadora[i].join ();

            // vetor de vetores: resultado[i] e o vetor ordenado
            // devolvido pela ordenadora i
            int[][] resultado = new int [qtdDeOrdenadoras][];
            for (int i=0; i<qtdDeOrdenadoras; i++)
                resultado[i] = ordenadora[i].getResultado ();

            // ---------- mistura ----------

            // a cada rodada, os resultados sao misturados 2 a 2, ate
            // sobrar um so; se a quantidade for impar, o ultimo fica
            // sem par e passa direto para a proxima rodada
            int rodada=0;
            while (resultado.length>1)
            {
                rodada++;

                Misturadora[] misturadora = new Misturadora [resultado.length/2];
                for (int i=0; i<misturadora.length; i++)
                {
                    misturadora[i] = new Misturadora (resultado[2*i], resultado[2*i+1]);
                    misturadora[i].start ();
                }

                // a proxima rodada so comeca quando esta terminar
                for (int i=0; i<misturadora.length; i++)
                    misturadora[i].join ();

                int[][] proxima = new int [(resultado.length+1)/2][];
                for (int i=0; i<misturadora.length; i++)
                    proxima[i] = misturadora[i].getResultado ();

                if (resultado.length%2==1)
                    proxima[proxima.length-1] = resultado[resultado.length-1];

                System.out.println ("Rodada "+rodada+": "+misturadora.length+" misturadora(s)"+
                                    (resultado.length%2==1?", 1 pedaco passou direto":""));

                resultado = proxima;
            }

            long termino = System.nanoTime();

            int[] ordenado = resultado[0];

            // ---------- consultas ----------

            int escolha=0;
            while (escolha!=3)
            {
                System.out.println ();
                System.out.println ("1 - Printar o vetor ordenado de uma posicao ate outra");
                System.out.println ("2 - Printar o tempo total de ordenacao");
                System.out.println ("3 - Terminar o programa");
                System.out.print   ("Opcao: ");

                try
                {
                    escolha = Integer.parseInt (teclado.nextLine());
                }
                catch (NumberFormatException erro)
                {
                    escolha = 0; // cai no "Opcao invalida!"
                }

                if (escolha==1)
                {
                    try
                    {
                        System.out.print ("Posicao inicial (de 0 a "+(qtd-1)+"): ");
                        int de = Integer.parseInt (teclado.nextLine());

                        System.out.print ("Posicao final (de 0 a "+(qtd-1)+"): ");
                        int ate = Integer.parseInt (teclado.nextLine());

                        if (de<0 || ate>qtd-1 || de>ate)
                            System.err.println ("Posicoes invalidas!");
                        else
                            for (int i=de; i<=ate; i++)
                                System.out.println ("["+i+"] "+ordenado[i]);
                    }
                    catch (NumberFormatException erro)
                    {
                        System.err.println ("Numero invalido!");
                    }
                }
                else if (escolha==2)
                    System.out.println ("Tempo total de ordenacao: "+(termino-inicio)/1000000.0+" ms");
                else if (escolha!=3)
                    System.err.println ("Opcao invalida!");
            }
        }
        catch (Exception erro)
        {
            System.err.println (erro.getMessage());
        }
    }
}
