package com.github.icebear67;

import net.mamoe.mirai.message.GroupMessage;
import net.mamoe.mirai.message.data.MessageUtils;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;


public class MessageHandler {
    private Vex vex;
    private HashMap<Long, Long> lastUsed = new HashMap<>();//ago.
    private HashMap<Long, Integer> messagesCount = new HashMap<>();//ago.
    private Timer cleaner = new Timer();

    public MessageHandler(Vex v) {
        vex = v;
        cleaner.schedule(new Reset(), 0, Vex.conf.getInt("Cycle") * 60 * 1000);
    }

    public void onMessage(GroupMessage message) {
        AtomicBoolean contains = new AtomicBoolean(false);
        Vex.conf.getStringList("keywords").forEach(s -> {
            if (message.getMessage().toString().matches(s)) {
                contains.set(true);
            }
        });
        if (!contains.get()) return;
        if (System.currentTimeMillis() - lastUsed.getOrDefault(message.getSender().getId(), 0L) < 10 * 1000) {
            message.getGroup().sendMessage("Re-try later!You're in coolDown");
            return;
        }
        lastUsed.put(message.getSender().getId(), System.currentTimeMillis());
        message.getGroup().sendMessage(MessageUtils.newImage(vex.getFoodImg().calculateImageResourceId()));
    }

    public void tryKill(GroupMessage message) {
        messagesCount.put(message.getSender().getId(), messagesCount.getOrDefault(message.getSender().getId(), 0) + 1);
        int counter = messagesCount.get(message.getSender().getId());
        if (counter == Vex.conf.getInt("Trigger")) {
            message.getGroup().sendMessage(MessageUtils.newImage(vex.getFoodImg().calculateImageResourceId()));
        }
    }

    class Reset extends TimerTask {
        @Override
        public void run() {
            messagesCount.clear();
        }
    }
}
