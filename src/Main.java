//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

            while (true) {
                System.out.println("Введите количество переменных (максимум 19): ");
                String input = sc.nextLine().trim();
                if(!input.matches("-?\\d+")){
                    System.out.println("Invalid input");
                    continue;
                }
                int count=Integer.parseInt(input);
                if (count>19){
                    System.out.println("Invalid input");
                    continue;
                }
                if (count<=0){
                    break;
                }

                System.out.println("Введите значения функции (двоичная строка, максимальная длина 524288)");
                String values = sc.nextLine().trim();

                if (!InputCheck(values, count)) {
                    System.out.println("Invalid input");
                    continue;

                }

                    //with fictious
                System.out.println("Таблица истинности:");
                System.out.println(TableBuilder(values, count));
                System.out.println("СДНФ:" + " " + BuildPDNF(values, count));
                System.out.println("СКНФ:" + " " + BuildPCNF(values, count));
                System.out.println(" ");


                    //not fictious
                String notFictiousValues = RemoveFictious(values, count);
                int countNew = 0;
                int len = notFictiousValues.length();
                int temp = 1;
                while (temp < len) {
                    temp *= 2;
                    countNew += 1;
                }
                if (IsConstant(notFictiousValues)) {
                    System.out.println("После удаления фиктивных переменных:");
                    System.out.println(countNew);
                    System.out.println(notFictiousValues);
                    System.out.println("Таблица истинности:");
                    System.out.println(" f(x)");
                    System.out.println("-----");
                    System.out.println("  "+notFictiousValues.charAt(0));
                } else {
                    System.out.println("После удаления фиктивных переменных:");
                    System.out.println(countNew);
                    System.out.println(notFictiousValues);
                    System.out.println("Таблица истинности:");
                    System.out.println(TableBuilder(notFictiousValues, countNew));
                }
                System.out.println("СДНФ:" + " " + BuildPDNF(notFictiousValues, countNew));
                System.out.println("СКНФ:" + " " + BuildPCNF(notFictiousValues, countNew));


            }

    }



    public static String BuildPDNF(String values, int cnt) {

        int len = values.length();
        int countOnes = 0;
        for (int i = 0; i < len; i += 1) {
            if (values.charAt(i) == '1') {
                countOnes += 1;
            }
        }
        if (countOnes == 0) {
            return "0";
        }
        if (countOnes == len) {
            return "1";
        }
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (int i = 0; i < len; i += 1) {
            if (values.charAt(i) == '1') {
                String bin = Integer.toBinaryString(i);
                while (bin.length() < cnt) {
                    bin = "0" + bin;
                }

                StringBuilder term = new StringBuilder();
                for (int j = 0; j < cnt; j += 1) {
                    if (j > 0) {
                        term.append(" ^ ");
                    }
                    if (bin.charAt(j) == '0') {
                        term.append("!x" + (j + 1));
                    } else {
                        term.append("x" + (j + 1));
                    }
                }
                if (!first) {
                    result.append(" v ");
                }
                    first = false;
                    result.append("(").append(term).append(")");

            }
        }
        return result.toString();
    }

    public static String BuildPCNF(String values, int cnt) {
        int len = values.length();
        int countOnes = 0;
        for (int i = 0; i < len; i += 1) {
            if (values.charAt(i) == '1') {
                countOnes += 1;
            }
        }
        if (countOnes == 0) {
            return "0";
        }
        if (countOnes == len) {
            return "1";
        }
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (int i = 0; i < len; i += 1) {
            if (values.charAt(i) == '0') {
                String bin = Integer.toBinaryString(i);
                while (bin.length() < cnt) {
                    bin = "0" + bin;
                }

                StringBuilder term = new StringBuilder();
                for (int j = 0; j < cnt; j += 1) {
                    if (j > 0) {
                        term.append(" v ");
                    }
                    if (bin.charAt(j) == '0') {
                        term.append("x" + (j + 1));
                    } else {
                        term.append("!x" + (j + 1));
                    }
                }
                if (!first) {
                    result.append(" ^ ");
                }
                    first = false;
                    result.append("(").append(term).append(")");

            }
        }
        return result.toString();
    }

    public static String RemoveFictious(String values, int cnt) {
        int len = values.length();
        boolean[] listEssential = new boolean[cnt + 1];
        for (int i = 1; i <= cnt; i += 1) {
            int diff = 1;
            for (int j = 0; j < cnt - i; j += 1) {
                diff *= 2;
            }
            boolean isEssential = false;
            for (int j = 0; j < len; j += 2 * diff) {
                for (int k = 0; k < diff; k += 1) {
                    if (values.charAt(j + k) != values.charAt(j + k + diff)) {
                        isEssential = true;
                        break;
                    }
                }
                if (isEssential) {
                    break;
                }
            }
            listEssential[i] = isEssential;
        }
        int n = 0;
        for (int i = 1; i <= cnt; i += 1) {
            if (listEssential[i]) {
                n += 1;
            }
        }
        int newCnt =n;

        int combinations=1;
        for (int i=0;i<newCnt;i+=1){
            combinations*=2;
        }
        StringBuilder newValues =new StringBuilder();
        for (int i = 0; i < combinations; i += 1) {
            String bin = Integer.toBinaryString(i);
            while (bin.length() < newCnt) {
                bin = "0" + bin;
            }
            int index = 0;
            StringBuilder tempVariblesString = new StringBuilder();
            for (int j = 1; j <= cnt; j += 1) {
                if (listEssential[j]) {
                    tempVariblesString.append(bin.charAt(index));
                    index += 1;
                } else {
                    tempVariblesString.append("0");
                }
            }
            newValues.append(values.charAt(Integer.parseInt(tempVariblesString.toString(), 2)));

        }

        return newValues.toString();
    }


    public static boolean InputCheck(String values, int cnt) {
        int len = values.length();
        if (len>524288){
            return false;
        }
        int expectedLength=1;
        for (int i=0;i<cnt;i+=1){
            expectedLength*=2;
        }

        if (len != expectedLength) {
            return false;
        }

        for (int i = 0; i < len; i += 1) {
            char c = values.charAt(i);
            if (c != '0' && c != '1') {
                return false;
            }
        }
        return true;
    }

    public static String TableBuilder(String values, int cnt) {
        StringBuilder table = new StringBuilder();


        for (int i = 0; i < cnt; i += 1) {
            table.append(" x").append(i + 1);
            if (i < 10) {
                table.append("  ");
            } else {
                table.append(" ");
            }

            table.append("|");
        }

        table.append(" f(x)\n");

        for (int i = 0; i < cnt; i += 1) {
            if (i < cnt - 1) {
                table.append("------");
            } else {
                table.append("-----------\n");
            }
        }


        int len = values.length();
        for (int i = 0; i < len; i += 1) {
            String bin = Integer.toBinaryString(i);
            while (bin.length() < cnt) {
                bin = "0" + bin;
            }
            table.append("  ");
            for (int j = 0; j < cnt; j += 1) {
                table.append(bin.charAt(j)).append("  ").append("|  ");
            }
            table.append(values.charAt(i));
            if (i<len-1){
                table.append("\n");
            }
        }

        return table.toString();
    }

    public static boolean IsConstant(String values) {
        char first = values.charAt(0);
        for (int i = 1; i < values.length(); i += 1) {
            if (values.charAt(i) != first) {
                return false;
            }
        }
        return true;
    }

}