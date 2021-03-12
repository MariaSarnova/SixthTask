package com.epam.task.sixth.logic;

import com.epam.task.sixth.entities.Visitor;
import org.apache.log4j.Logger;

public class Cashier {
    private final static Logger LOGGER = Logger.getRootLogger();

    public void process(Visitor visitor){
        boolean receivedAnOrder=visitor.getReceivedAnOrder();
        if(!receivedAnOrder){
            visitor.setReceivedAnOrder(true);
            LOGGER.debug(visitor + " processed");
        }
    }
}
