package search;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

abstract class SearchEngine {

    protected Map<String, List<Integer>> mapper = new HashMap<>();
    protected List<String> lines;
    public void createMap(List<String> lines){
        this.lines = lines;
        for(int i = 0; i < lines.size(); i++){
            String[] words = lines.get(i).split(" ");
            for(String word: words){
                word = word.toLowerCase();
                if(mapper.containsKey(word)){
                    List<Integer> curr = mapper.get(word);
                    curr.add(i);
                    mapper.put(word, curr);
                } else {
                    List<Integer> curr = new ArrayList<>();
                    curr.add(i);
                    mapper.put(word, curr);
                }
            }
        }
    }

    public abstract void findOccurences(List<String> words);
}

class AllSearchEngine extends SearchEngine {


    List<List<Integer>> indexes = new ArrayList<>();
    Set<Integer> commonEle;
    @Override
    public void findOccurences(List<String> words) {
        for(String word: words){
            word = word.toLowerCase();
            if(this.mapper.containsKey(word)){
                indexes.add(this.mapper.get(word));
            }
        }

        if(indexes.size() > 0){
            commonEle = new HashSet<>(indexes.get(0));

            for(int i = 1; i < indexes.size(); i++){
                commonEle.retainAll(indexes.get(i));
            }

            for(Integer index: commonEle){
                System.out.println(this.lines.get(index));
            }

        }

    }
}

class AnySearchEngine extends SearchEngine {


    Set<Integer> indexSet = new HashSet<>();
    @Override
    public void findOccurences(List<String> words) {
        for(String word: words){
            word = word.toLowerCase();
            if(this.mapper.containsKey(word)){
                indexSet.addAll(this.mapper.get(word));
            }
        }

        for(int index: indexSet){
            System.out.println(this.lines.get(index));
        }
    }
}

class NoneSearchEngine extends SearchEngine{

    Set<Integer> indexSet = new HashSet<>();
    @Override
    public void findOccurences(List<String> words) {
        for(String word: words){
            word = word.toLowerCase();
            if(this.mapper.containsKey(word)){
                indexSet.addAll(this.mapper.get(word));
            }
        }

        for(int i = 0; i < this.lines.size(); i++){
            if(!indexSet.contains(i)){
                System.out.println(this.lines.get(i));
            }
        }
    }
}

class Menu {
    List<String> lines;
    Scanner scan = new Scanner(System.in);

    public Menu(List<String> lines){
        this.lines = lines;
    }

    public void getMenu(){
        System.out.println("=== Menu ===\n" +
                "1. Find a person\n" +
                "2. Print all people\n" +
                "0. Exit");
    }

    public void validateMenu(){
        boolean isEnd = false;
        while(!isEnd){
            getMenu();
            int option = scan.nextInt();
            scan.nextLine();
            if(option == 1){
                System.out.println("Select a matching strategy: ALL, ANY, NONE");
                String searchOption = scan.next();
                scan.nextLine();
                System.out.println("Enter a name or email to search all suitable people.");
                String word = scan.nextLine();
                List<String> words = List.of(word.split(" "));
                SearchEngine searchEngine;
                if(searchOption.equals("ALL")){
                    searchEngine = new AllSearchEngine();
                    searchEngine.createMap(this.lines);
                    searchEngine.findOccurences(words);
                } else if(searchOption.equals("ANY")){
                    searchEngine = new AnySearchEngine();
                    searchEngine.createMap(this.lines);
                    searchEngine.findOccurences(words);
                } else if(searchOption.equals("NONE")){
                    searchEngine = new NoneSearchEngine();
                    searchEngine.createMap(this.lines);
                    searchEngine.findOccurences(words);
                }
            } else if(option == 2){
                System.out.println("=== List of people ===");
                for(String line: this.lines){
                    System.out.println(line);
                }
            } else if(option == 0){
                System.out.println("Bye!");
                isEnd = true;
            } else {
                System.out.println("Incorrect option! Try again.");
            }
        }

    }
}
public class Main {
    public static void main(String[] args) {
        List<String> lines = new ArrayList<>();
        File file = new File(args[1]);
        try {
            Scanner fileScanner = new Scanner(file);
            while(fileScanner.hasNext()){
                lines.add(fileScanner.nextLine());
            }

        }

        catch (FileNotFoundException e){
            System.out.println("File Not found");
        }

        Menu menu = new Menu(lines);
        menu.validateMenu();


    }
}