package com.epam.task.sixth;

import com.epam.task.sixth.entities.Visitor;
import com.epam.task.sixth.entities.Visitors;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class Main {
    private final static String FILE_PATH = "src/main/resources/input.json";

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        ObjectMapper objectMapper = new ObjectMapper();
        Visitors data = objectMapper.readValue(new File(FILE_PATH), Visitors.class);
        List<Visitor> visitors = data.getVisitors();

        visitors.stream().forEach(System.out::println);

        ExecutorService executorService = Executors.newFixedThreadPool(visitors.size());
        List<Future<?>> futures = visitors.stream()
                .map(van -> executorService.submit(van))
                .collect(Collectors.toList());
        executorService.shutdown();

        for (Future<?> future : futures){
            future.get();
        }

    }
}
