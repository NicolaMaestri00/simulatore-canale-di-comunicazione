import java.util.Scanner;

public class Progetto{

    public static int[] carattereToASCII(char car){
        int carToInt = (int) car;
        String binary = Integer.toBinaryString(carToInt);
        int[] a = {0, 0, 0, 0, 0, 0, 0};
        for(int i=0; i<binary.length(); i++){
            String s = String.valueOf(binary.charAt(i));
            int b = Integer.parseInt(s);
            a[7-binary.length()+i] = b;
        }
        return a;
    }

    public static int[][] fraseToASCII(String messaggio){
        int[][] infoWords= new int[messaggio.length()][7];
        for(int i=0; i<messaggio.length(); i++){
            infoWords[i] = carattereToASCII(messaggio.charAt(i));
        }
        return infoWords;
    }

    public static int[] codificaCarattere(int[] info){
        int[][] C = {{1, 1, 0, 1, 0, 0, 0},
                     {0, 1, 1, 0, 1, 0, 0},
                     {0, 0, 1, 1, 0, 1, 0},
                     {0, 0, 0, 1, 1, 0, 1},

                     {1, 1, 0, 1, 1, 1, 0},
                     {0, 1, 1, 0, 1, 1, 1},
                     {1, 1, 1, 0, 0, 1, 1},
                     {1, 0, 1, 0, 0, 0, 1},

                     {1, 0, 0, 0, 0, 0, 0},
                     {0, 1, 0, 0, 0, 0, 0},
                     {0, 0, 1, 0, 0, 0, 0},
                     {0, 0, 0, 1, 0, 0, 0},
                     {0, 0, 0, 0, 1, 0, 0},
                     {0, 0, 0, 0, 0, 1, 0},
                     {0, 0, 0, 0, 0, 0, 1}};
        int[] codeword = new int[15];
        int sum = 0;
        for(int i=0; i<15; i++){
            sum = 0;
            for(int j=0; j<7; j++){
                sum += C[i][j]*info[j];
            }
            codeword[i] = sum%2;
        }
        return codeword;
    }

    public static int[][] codificaFrase(int[][] messaggio){
        int[][] messaggioCodificato = new int[messaggio.length][15];
        for(int i=0; i<messaggio.length; i++){
            messaggioCodificato[i] = codificaCarattere(messaggio[i]);
        }
        return messaggioCodificato;
    }

    public static int[] erroreTrasmissione(int[] codeWord){
        int randomNumber = (int)(Math.random()*3);
        if(randomNumber==0){
            //System.out.println("No errori.");
            return codeWord;
        }else if(randomNumber==1){
            int error = (int)(Math.random()*15);
            codeWord[error] = (codeWord[error]+1)%2;
            //System.out.println("1 errore: " + error);
            return codeWord;
        }else{           
            int err1 = (int)(Math.random()*15);
            int err2 = (int)(Math.random()*15);
            codeWord[err1] = (codeWord[err1]+1)%2;
            codeWord[err2] = (codeWord[err2]+1)%2;
            //System.out.println("2 errori: " + err1 + " e " + err2);
            return codeWord;
        }
    }

    public static int[][] rumoreCanale(int[][] messaggioTrasmesso){
        for(int i=0; i<messaggioTrasmesso.length; i++){
            messaggioTrasmesso[i]=erroreTrasmissione(messaggioTrasmesso[i]);
        }
        return messaggioTrasmesso;
    }

    public static int[][] computaSindrome(int[][] recivedWord){
        int[][] H = {{1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 1, 0, 1, 1, 1},
        {0, 1, 0, 0, 1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 0},
        {0, 0, 1, 0, 0, 1, 1, 0, 1, 0, 1, 1, 1, 1, 0},
        {0, 0, 0, 1, 0, 0, 1, 1, 0, 1, 0, 1, 1, 1, 1},

        {1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1},
        {0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1},
        {0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1},
        {0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1}};

        int[][] sindrome = new int[recivedWord.length][8];
        for(int i=0; i<recivedWord.length; i++){
            int[] controllo = new int[8];
            for(int j=0; j<8; j++){
                int sum = 0;
                for(int k=0; k<15; k++){
                    sum += H[j][k]*recivedWord[i][k];
                }
                controllo[j]=sum%2;
            }
            sindrome[i]=controllo;
        }
        return sindrome;
    }

    public static boolean checkVettori(int[] vettore, int[] sindrome){
        for(int i=0; i<sindrome.length; i++){
            if(sindrome[i]!=vettore[i]) return false;
        }
        return true;
    }

    public static int checkColonne(int[] sindrome){
        int[][] Ht = {  {1, 0, 0, 0, 1, 0, 0, 0},
                        {0, 1, 0, 0, 0, 0, 0, 1},
                        {0, 0, 1, 0, 0, 0, 1, 1},
                        {0, 0, 0, 1, 0, 1, 0, 1},
                        {1, 1, 0, 0, 1, 1, 1, 1},

                        {0, 1, 1, 0, 1, 0, 0, 0},
                        {0, 0, 1, 1, 0, 0, 0, 1},
                        {1, 1, 0, 1, 0, 0, 1, 1},
                        {1, 0, 1, 0, 0, 1, 0, 1},
                        {0, 1, 0, 1, 1, 1, 1, 1},
                        
                        {1, 1, 1, 0, 1, 0, 0, 0},
                        {0, 1, 1, 1, 0, 0, 0, 1},
                        {1, 1, 1, 1, 0, 0, 1, 1},
                        {1, 0, 1, 1, 0, 1, 0, 1},
                        {1, 0, 0, 1, 1, 1, 1, 1}};
        for(int i=0; i<15; i++){
            boolean control = false;
            control = checkVettori(Ht[i], sindrome);
            if(control) return i;
        }
        return -1;
    }

    public static int rendAlphaPower(int[] vettore){
        int[][] alphaPower = {  {1, 0, 0, 0},
                                {0, 1, 0, 0},
                                {0, 0, 1, 0},
                                {0, 0, 0, 1},
                                {1, 1, 0, 0},
                                {0, 1, 1, 0},
                                {0, 0, 1, 1},
                                {1, 1, 0, 1},
                                {1, 0, 1, 0},
                                {0, 1, 0, 1},                                
                                {1, 1, 1, 0},
                                {0, 1, 1, 1},
                                {1, 1, 1, 1},
                                {1, 0, 1, 1},
                                {1, 0, 0, 1}};
        for(int i=0; i<15; i++){
            if(checkVettori(alphaPower[i], vettore)) return i;
        }
        return -1;
    }

    public static int[] dueErrori(int[] sindrome){
        int[][] alphaPower = {  {1, 0, 0, 0},
                                {0, 1, 0, 0},
                                {0, 0, 1, 0},
                                {0, 0, 0, 1},
                                {1, 1, 0, 0},
                                {0, 1, 1, 0},
                                {0, 0, 1, 1},
                                {1, 1, 0, 1},
                                {1, 0, 1, 0},
                                {0, 1, 0, 1},                                
                                {1, 1, 1, 0},
                                {0, 1, 1, 1},
                                {1, 1, 1, 1},
                                {1, 0, 1, 1},
                                {1, 0, 0, 1}};

        int[] s_1 = new int[4];
        int[] s_3 = new int[4];
        for(int i=0; i<4; i++){
            s_1[i] = sindrome[i];
            s_3[i] = sindrome[i+4];
        }
                        
        int powerS_1 = rendAlphaPower(s_1);
        int powerS_3 = rendAlphaPower(s_3);
        
        int r = 1;
        if(powerS_3==-1) r=0;

        int C3 = (15-powerS_1+powerS_3)%15;
        int C4 = (powerS_1+powerS_1)%15;
        int[] a = {-1, -1};
        int k = 0;
        int[] zeros = {0, 0, 0, 0};
        for(int i=0; i<15; i++){
            int[] vett = new int[4];
            for(int j=0; j<4; j++){
                vett[j] = (alphaPower[(i+i)%15][j]+alphaPower[(i+powerS_1)%15][j]+r*alphaPower[C3][j]+alphaPower[C4][j])%2;
            }
            if(checkVettori(vett, zeros)){
                a[k] = i;
                k++;
            }
        }
        return a;
    }

    public static int[] rendiErrori(int[] sindrome){
        int[] a = {-1, -1};
        int sum = 0;
        for(int i=0; i<sindrome.length; i++){
            sum += sindrome[i];
        }
        if(sum==0) return a;
        a[0]=checkColonne(sindrome);
        if(a[0]!=-1) return a;
        a = dueErrori(sindrome);
        return a;
    }

    public static String codiceToStringa(int[] codeword){
        String a = "";
        for(int i=0; i<codeword.length; i++){
            Integer b = codeword[i];
            a = a + b.toString();
        }
        return a;
    }

    public static String codiceToCarattere(int[] codeword){
        String a = codiceToStringa(codeword);
        switch(a){
            case "110011100100000": return " ";
            case "110110010100001": return "!";
            case "111000000100010": return "\"";
            case "111101110100011": return "#";
            case "100100100100100": return "$";
            case "100001010100101": return "%";
            case "101111000100110": return "&";
            case "101010110100111": return "'";
            case "011101100101000": return "(";
            case "011000010101001": return ")";
            case "010110000101010": return "*";
            case "010011110101011": return "+";
            case "001010100101100": return ",";
            case "001111010101101": return "-";
            case "000001000101110": return ".";
            case "000100110101111": return "/";
            case "101010010110000": return "0";
            case "101111100110001": return "1";
            case "100001110110010": return "2";
            case "100100000110011": return "3";
            case "111101010110100": return "4";
            case "111000100110101": return "5";
            case "110110110110110": return "6";
            case "110011000110111": return "7";
            case "000100010111000": return "8";
            case "000001100111001": return "9";
            case "001111110111010": return ":";
            case "001010000111011": return ";";
            case "010011010111100": return "<";
            case "010110100111101": return "=";
            case "011000110111110": return ">";
            case "011101000111111": return "?";
            case "100010111000000": return "@";
            case "100111001000001": return "A";
            case "101001011000010": return "B";
            case "101100101000011": return "C";
            case "110101111000100": return "D";
            case "110000001000101": return "E";
            case "111110011000110": return "F";
            case "111011101000111": return "G";
            case "001100111001000": return "H";
            case "001001001001001": return "I";
            case "000111011001010": return "J";
            case "000010101001011": return "K";
            case "011011111001100": return "L";
            case "011110001001101": return "M";
            case "010000011001110": return "N";
            case "010101101001111": return "O";
            case "111011001010000": return "P";
            case "111110111010001": return "Q";
            case "110000101010010": return "R";
            case "110101011010011": return "S";
            case "101100001010100": return "T";
            case "101001111010101": return "U";
            case "100111101010110": return "V";
            case "100010011010111": return "W";
            case "010101001011000": return "X";
            case "010000111011001": return "Y";
            case "011110101011010": return "Z";
            case "011011011011011": return "[";
            case "000010001011100": return "\\";
            case "000111111011101": return "]";
            case "001001101011110": return "^";
            case "001100011011111": return "_";
            case "010001011100000": return "'";
            case "010100101100001": return "a";
            case "011010111100010": return "b";
            case "011111001100011": return "c";
            case "000110011100100": return "d";
            case "000011101100101": return "e";
            case "001101111100110": return "f";
            case "001000001100111": return "g";
            case "111111011101000": return "h";
            case "111010101101001": return "i";
            case "110100111101010": return "j";
            case "110001001101011": return "k";
            case "101000011101100": return "l";
            case "101101101101101": return "m";
            case "100011111101110": return "n";
            case "100110001101111": return "o";
            case "001000101110000": return "p";
            case "001101011110001": return "q";
            case "000011001110010": return "r";
            case "000110111110011": return "s";
            case "011111101110100": return "t";
            case "011010011110101": return "u";
            case "010100001110110": return "v";
            case "010001111110111": return "w";
            case "100110101111000": return "x";
            case "100011011111001": return "y";
            case "101101001111010": return "z";
            case "101000111111011": return "{";
            case "110001101111100": return "|";
            case "110100011111101": return "}";
            case "111010001111110": return "~";
        }
        return "<Err>";
    }

    public static void stampaMatrice(int[][]matrice){
        for(int i=0; i<matrice.length; i++){
            for(int j=0; j<matrice[i].length; j++){
                System.out.print(matrice[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Messaggio da trasmettere:");
        String messaggio = sc.nextLine();
        sc.close();

        System.out.println();
        System.err.println();
        System.err.println("FASE DI CODIFICA");
        System.out.println();
        int[][] messaggioToASCII = fraseToASCII(messaggio);
        System.out.println("SOURCE ENCODER: Traduzione in codice ASCII del messaggio");
        //stampaMatrice(messaggioToASCII);
        for(int i=0; i<messaggioToASCII.length; i++){
            System.out.print(messaggio.charAt(i) + " -> ");
            for(int j=0; j<messaggioToASCII[i].length; j++){
                System.out.print(messaggioToASCII[i][j]);
            }
            System.out.println();
        }

        System.out.println();
        int[][] messaggioCodificato = codificaFrase(messaggioToASCII);
        System.out.println("CHANNEL ENCODER: Codifica delle sequenze in parole del codice");
        //stampaMatrice(messaggioCodificato);
        for(int i=0; i<messaggioCodificato.length; i++){
            for(int k=0; k<messaggioToASCII[i].length; k++){
                System.out.print(messaggioCodificato[i][k]);
            }
            System.out.print(" -> ");
            for(int j=0; j<messaggioCodificato[i].length; j++){
                System.out.print(messaggioCodificato[i][j]);
            }
            System.out.println();
        }

        System.out.println();
        System.out.println();
        System.out.println("FASE DI TRASMISSIONE");
        System.out.println("......................................................");
        System.out.println("......................................................");
        int[][] recivedWord = rumoreCanale(messaggioCodificato);
        System.out.println("......................................................");
        System.out.println("......................................................");
        System.out.println();
        System.out.println();

        System.out.println("FASE DI DECODIFICA");
        System.out.println();
        System.out.println("Messaggio ricevuto dal decoder");       
        stampaMatrice(recivedWord);
        System.out.println();
        System.out.println("Computo della sindrome");
        int[][] sindromi = computaSindrome(recivedWord);
        stampaMatrice(sindromi);

        System.out.println();
        System.out.println("Rilevamento degli errori"); 
        for(int i=0; i<sindromi.length; i++){
            int[] a = rendiErrori(sindromi[i]);
            if(a[0]==-1){
                System.out.println("Il messaggio " + i + " è corretto.");
            }else if(a[1]== -1){
                System.out.println("Il messaggio " + i + " ha un errore nel bit " + a[0]);
                recivedWord[i][a[0]] = (recivedWord[i][a[0]]+1)%2;
            }else{
                System.out.println("Il messaggio " + i + " ha due errori nei bit " + a[0] + " e " + a[1]);
                recivedWord[i][a[0]] = (recivedWord[i][a[0]]+1)%2;
                recivedWord[i][a[1]] = (recivedWord[i][a[1]]+1)%2;
            }
        }

        System.out.println();
        System.out.println("Il messaggio corretto è:");
        stampaMatrice(recivedWord);

        System.out.println();
        System.out.println("SOURCE DECODER");
        for(int i=0; i<recivedWord.length; i++){
            for(int j=0; j<recivedWord[i].length; j++){
                System.out.print(recivedWord[i][j]);
            }
            System.out.println(" -> " + codiceToCarattere(recivedWord[i]));
        }

        System.out.println();
        System.out.println("Il messaggio spedito è:");
        for(int i=0; i<recivedWord.length; i++){
            System.out.print(codiceToCarattere(recivedWord[i]));
        }

        for(int i=0; i<4; i++){
            System.out.println();
        }
        
    }
}