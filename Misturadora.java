public class Misturadora extends Thread
{
    private int[] vetor1;
    private int[] vetor2;
    private int[] resultado;

    public Misturadora (int[] vet1, int[] vet2) throws Exception
    {
        if (vet1==null)
            throw new Exception ("Vetor 1 ausente");

        if (vet2==null)
            throw new Exception ("Vetor 2 ausente");

        // guardamos copias, e nao os proprios vetores recebidos
        // (vide Aluno e Data)
        this.vetor1 = new int [vet1.length];
        for (int i=0; i<vet1.length; i++)
            this.vetor1[i] = vet1[i];

        this.vetor2 = new int [vet2.length];
        for (int i=0; i<vet2.length; i++)
            this.vetor2[i] = vet2[i];

        this.resultado = new int [vet1.length+vet2.length];
    }

    @Override
    public void run ()
    {
        // vetor1 e vetor2 ja vem ordenados; a cada passo, o menor
        // dos dois elementos da vez vai para o resultado
        int i=0, j=0, k=0;

        while (i<this.vetor1.length && j<this.vetor2.length)
            if (this.vetor1[i]<=this.vetor2[j])
                this.resultado[k++] = this.vetor1[i++];
            else
                this.resultado[k++] = this.vetor2[j++];

        while (i<this.vetor1.length)
            this.resultado[k++] = this.vetor1[i++];

        while (j<this.vetor2.length)
            this.resultado[k++] = this.vetor2[j++];
    }

    // so deve ser chamado depois do join, quando a mistura terminou
    public int[] getResultado ()
    {
        // devolvemos uma copia, pelo mesmo motivo do construtor
        int[] ret = new int [this.resultado.length];
        for (int i=0; i<this.resultado.length; i++)
            ret[i] = this.resultado[i];

        return ret;
    }
}
