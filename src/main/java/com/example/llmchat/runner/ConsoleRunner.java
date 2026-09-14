package com.example.llmchat.runner;

import com.example.llmchat.service.ChatService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.Scanner;

@Component
public class ConsoleRunner implements CommandLineRunner {
    private final ChatService chatService;

    public ConsoleRunner(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner=new Scanner(System.in);
        System.out.println("LLM Chat запущен (введите /clear для сброса, /exit для выхода)");

        while (true){
            System.out.println("\nВы: ");
            String input=scanner.nextLine().trim();

            if (input.isEmpty()){
                continue;
            }

            if("/exit".equalsIgnoreCase(input)){
                System.out.println("Завершение работы...");
                break;
            }

            if("/clear".equalsIgnoreCase(input)){
                chatService.resetChat();
                System.out.println("История диалога очищена");
                continue;
            }

            try{
                String reply= chatService.sendUserMessage(input);
                System.out.println("\nAssistant: " + reply);
            }catch (ResourceAccessException exception){
                System.err.println("\n[Ошибка]: Не удалось подключиться к Ollama. Убедитесь, что сервер запущен.");
            }catch (HttpClientErrorException exception){
                System.err.println("\n[Ошибка]: Указанная модель не найдена. Выполните: ollama pull llama3.2:1b");
            }catch (Exception exception){
                System.err.println("\n[Ошибка при запросе к LLM]: " + exception.getMessage());
            }
        }
    }
}
