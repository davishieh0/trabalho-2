public class Ordenadora extends Thread
{
    private int[] vetor;

    public Ordenadora (int[] pedaco) throws Exception
    {
        if (pedaco==null)
            throw new Exception ("Pedaco ausente");

        // guardamos uma copia, e nao o proprio vetor recebido,
        // para que quem nos forneceu o pedaco nao consiga mexer
        // nele enquanto o ordenamos (vide Aluno e Data)
        this.vetor = new int [pedaco.length];
        for (int i=0; i<pedaco.length; i++)
            this.vetor[i] = pedaco[i];
    }

    // junta this.vetor[inicio..meio] com this.vetor[meio+1..fim],
    // sabendo que cada uma dessas duas partes ja esta ordenada
    private void intercale (int inicio, int meio, int fim)
    {
        int[] aux = new int [fim-inicio+1];

        int i=inicio, j=meio+1, k=0;

        while (i<=meio && j<=fim)
            if (this.vetor[i]<=this.vetor[j])
                aux[k++] = this.vetor[i++];
            else
                aux[k++] = this.vetor[j++];

        while (i<=meio)
            aux[k++] = this.vetor[i++];

        while (j<=fim)
            aux[k++] = this.vetor[j++];

        for (k=0; k<aux.length; k++)
            this.vetor[inicio+k] = aux[k];
    }

    // ordena this.vetor[inicio..fim] de forma recursiva (merge sort)
    private void ordene (int inicio, int fim)
    {
        if (inicio>=fim) // 0 ou 1 elemento: ja esta ordenado
            return;

        int meio = (inicio+fim)/2;

        this.ordene    (inicio, meio);
        this.ordene    (meio+1, fim);
        this.intercale (inicio, meio, fim);
    }

    @Override
    public void run ()
    {
        this.ordene (0, this.vetor.length-1);
    }

    // so deve ser chamado depois do join, quando a ordenacao terminou
    public int[] getResultado ()
    {
        // devolvemos uma copia, pelo mesmo motivo do construtor
        int[] ret = new int [this.vetor.length];
        for (int i=0; i<this.vetor.length; i++)
            ret[i] = this.vetor[i];

        return ret;
    }
}
