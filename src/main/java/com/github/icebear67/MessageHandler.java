package com.github.icebear67;

import net.mamoe.mirai.message.GroupMessage;
import net.mamoe.mirai.message.data.MessageUtils;

import java.util.HashMap;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;


public class MessageHandler {
    private Vex vex;
    private HashMap<Long,Long> lastUsed=new HashMap<>();//ago.
    private HashMap<Long,Integer> messagesCount=new HashMap<>();//ago.
    public MessageHandler(Vex v){
        vex=v;
    }
    public void onMessage(GroupMessage message){
        AtomicBoolean contains= new AtomicBoolean(false);
        Vex.conf.getStringList("keywords").forEach(s->{
            if(message.getMessage().toString().matches(s)){
                contains.set(true);
            }
        });
        if(!contains.get())return;
        if(System.currentTimeMillis()-lastUsed.getOrDefault(message.getSender().getId(),0L)<10*1000){
            message.getGroup().sendMessage("Re-try later!You're in coolDown");
            return;
        }
        lastUsed.put(message.getSender().getId(),System.currentTimeMillis());
        message.getGroup().sendMessage(MessageUtils.newImage(vex.getFoodImg().calculateImageResourceId()));
    }
    public void tryKill(GroupMessage message){
        messagesCount.put(message.getSender().getId(),messagesCount.getOrDefault(message.getSender().getId(),0)+1);
        int counter=messagesCount.get(message.getSender().getId());
        switch(counter){
            case 10:
                message.getGroup().sendMessage("这么晚了还不睡?");
                break;
            case 12:
                message.getGroup().sendMessage("=)");
                break;
            case 15:
                message.getGroup().sendMessage(MessageUtils.newImage(vex.getFoodImg().calculateImageResourceId()));
                counter=0;
                break;
        }
    }
    class Reset extends TimerTask {
        int counter=0;
        @Override
        public void run() {
            counter++;
            if(counter>3){
                messagesCount.clear();
            }
        }
    }
}
