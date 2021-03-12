package com.epam.task.sixth.entities;

import com.epam.task.sixth.logic.OrderZone;

public class Visitor implements Runnable{
    private int id;
    private boolean receivedAnOrder;
    private boolean preOrder;

    public Visitor(){

    }

    public Visitor(int id,boolean receivedAnOrder,boolean preOrder){
        this.id=id;
        this.receivedAnOrder=receivedAnOrder;
        this.preOrder=preOrder;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setReceivedAnOrder(boolean receivedAnOrder) {
        this.receivedAnOrder = receivedAnOrder;
    }

    public boolean getReceivedAnOrder() {
        return receivedAnOrder;
    }

    public void setPreOrder(boolean preOrder) {
        this.preOrder = preOrder;
    }

    public boolean getPreOrder() {
        return preOrder;
    }

    @Override
    public String toString() {
        return "Visitor{" +
                "id=" + id +
                ", receivedAnOrder=" + receivedAnOrder +
                ", preOrder=" + preOrder +
                '}';
    }

    public void run() {
        OrderZone orderZone = OrderZone.getInstance();
        orderZone.process(this);
    }
}
