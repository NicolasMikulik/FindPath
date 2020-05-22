import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FindPathInputReaderFile extends AbstractFindPathInputReader{

    public String readInput() {
        String foundPath = "";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the file path:");
        String filePath = scanner.nextLine();
        scanner.close();
        try{
            FileInputStream fileStream = new FileInputStream(filePath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileStream));

            String line; //= bufferedReader.readLine();
            List<String> stringArrayList = new ArrayList<String>();

            while ((line = bufferedReader.readLine()) != null){
                stringArrayList.add(line);
            }
            fileStream.close();
            int rows = stringArrayList.size(), columns = stringArrayList.get(0).length();
            //System.out.println(stringArrayList.toString()+rows+columns);
            foundPath = handleMap(rows, columns, stringArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return foundPath;
    }
}