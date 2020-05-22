import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FindPathInputReaderStdIn extends  AbstractFindPathInputReader{

    public String readInput() {
        String foundPath = "";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter maze line by line," +
                "\nentering an empty line finishes the maze.");
        try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            String line; //= bufferedReader.readLine();
            List<String> stringArrayList = new ArrayList<String>();

            while ((line = bufferedReader.readLine()) != null && !line.equals("")){
                //System.out.print(line);
                stringArrayList.add(line);
            }
            int rows = stringArrayList.size(), columns = stringArrayList.get(0).length();
            //System.out.println(stringArrayList.toString()+rows+columns);
            foundPath = handleMap(rows, columns, stringArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        scanner.close();
        return foundPath;
    }
}