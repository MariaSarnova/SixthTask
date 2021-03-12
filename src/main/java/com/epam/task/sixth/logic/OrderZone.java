package com.epam.task.sixth.logic;

import com.epam.task.sixth.entities.Visitor;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OrderZone {
    private final static Logger LOGGER = Logger.getRootLogger();
    private List<Cashier> cashiers;
    private static final int CASHIERS_NUMBER = 3;
    private Queue<Visitor> visitors;

    private final static AtomicReference<OrderZone> INSTANCE = new AtomicReference<>();
    private static AtomicBoolean isInstanceCreated = new AtomicBoolean();
    private final static Lock instanceLock = new ReentrantLock();
    private final static Semaphore SEMAPHORE = new Semaphore(CASHIERS_NUMBER);
    private final Lock cashierLock = new ReentrantLock();
    private final Lock visitorLock = new ReentrantLock();

    private OrderZone(){
        this.cashiers = IntStream.range(0, CASHIERS_NUMBER)
                .mapToObj(cashier -> new Cashier())
                .collect(Collectors.toList());
        this.visitors = new PriorityQueue<>((first, second) ->
                Boolean.compare(second.getPreOrder(), first.getPreOrder()));
    }

    public static OrderZone getInstance(){
        if (!isInstanceCreated.get()){
            instanceLock.lock();
            try{
                if (!isInstanceCreated.get()){
                    OrderZone orderZone = new OrderZone();
                    INSTANCE.set(orderZone);
                    isInstanceCreated.set(true);

                    LOGGER.debug("Created OrderZone instance");
                }

            } finally {
                instanceLock.unlock();
            }
        }

        return INSTANCE.get();
    }

    public void process(Visitor visitor){
        try {
            SEMAPHORE.acquire();

            visitorLock.lock();
            visitors.add(visitor);
            visitorLock.unlock();

            cashierLock.lock();
            Cashier cashier = cashiers.remove(0);

            visitorLock.lock();
            Visitor currentVisitor = visitors.poll();
            visitorLock.unlock();

            cashier.process(currentVisitor);
            cashiers.add(cashier);

        } catch (InterruptedException e) {
            LOGGER.fatal(e.getMessage(), e);

        } finally {
            SEMAPHORE.release();
            cashierLock.unlock();
        }
    }
}
