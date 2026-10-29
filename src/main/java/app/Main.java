package app;

import app.model.Book;
import app.model.SmsMessage;
import app.model.Visitor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args){
        Gson gson = new Gson();
        List<Visitor> visitors = null;

        try(FileReader reader = new FileReader("books.json")){
            Type listType = new TypeToken<List<Visitor>>()  {}.getType();
            visitors = gson.fromJson(reader, listType);
        } catch (IOException e){
            System.out.println("Ошибка: не найден файл book.json в корне проекта.");
            return;
        }

        //Задание 1
        System.out.println("======= Задание №1 ========");
        visitors.forEach(System.out::println);
        System.out.println("Количество поситителей: " + visitors.size() + "\n");

        //Задание 2
        System.out.println("======= Задание №2 ========");
        List<Book> uniqueBooks = visitors.stream().flatMap(v -> v.getFavoriteBooks().stream()).distinct().collect(Collectors.toList());
        uniqueBooks.forEach(System.out::println);
        System.out.println("Количество уникальных книг: " + uniqueBooks.size() + "\n");

        //Задание 3
        System.out.println("======= Задание №3 ========");
        uniqueBooks.stream().sorted(Comparator.comparingInt(Book::getPublishingYear)).forEach(System.out::println);
        System.out.println();

        //Задание 4
        System.out.println("======= Задание №4 ========");
        boolean hasJaneAusten = visitors.stream().flatMap(v -> v.getFavoriteBooks().stream()).anyMatch(b -> "Jane Austen".equalsIgnoreCase(b.getAuthor()));
        System.out.println("Есть ли книга Jane Austen: " + hasJaneAusten + "\n");

        //Задание 5
        System.out.println("======= Задание №5 ========");
        int maxBooks = visitors.stream().mapToInt(v -> v.getFavoriteBooks().size()).max().orElse(0);
        System.out.println("Максимальное число книг у одного посетителя: " + maxBooks + "\n");

        //Задание 6
        System.out.println("======= Задание №6 ========");
        double averageBooks = visitors.stream().mapToInt(v -> v.getFavoriteBooks().size()).average().orElse(0.0);



        List<SmsMessage> messages = visitors.stream().filter(Visitor::isSubscribed).map(v -> {
            int count = v.getFavoriteBooks().size();
            String text = count > averageBooks ? "you are a bookworm" :
                    (count < averageBooks ? "read more" : "fine");
            return new SmsMessage(v.getPhone(), text);
        }).collect(Collectors.toList());
    }
}