package com.github.icebear67;

import net.mamoe.mirai.console.events.Events;
import net.mamoe.mirai.console.plugins.Config;
import net.mamoe.mirai.console.plugins.PluginBase;
import net.mamoe.mirai.message.GroupMessage;
import org.nutz.http.Http;
import org.nutz.http.Sender;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Vex extends PluginBase {
    public static Config conf;
    private MessageHandler messageHandler;
    private List<String> cachedImgUrls;
    private long lastUpdate;
    private Random rand=new Random();

    public void onLoad() {
        Sender.setup(Executors.newCachedThreadPool());
    }

    public void onEnable() {
        getLogger().info("Loading..");
        conf = this.getResourcesConfig("config.yml");
        messageHandler = new MessageHandler(this);
        Events.subscribeAlways(GroupMessage.class, messageHandler::onMessage);
        Events.subscribeAlways(GroupMessage.class, (GroupMessage gm) -> {
            if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 6) {
                messageHandler.tryKill(gm);
            }
        });
    }

    public URL getFoodImg() {
        if (!(System.currentTimeMillis() - lastUpdate < Vex.conf.getInt("CacheDelay") * 1000)) {
            cachedImgUrls = getImgUrls(Http.get(Vex.conf.getString("Site")).getContent());
            lastUpdate = System.currentTimeMillis();
        }
        try {
            getLogger().info("Sending: https://" + cachedImgUrls.get(rand.nextInt(cachedImgUrls.size())));
            return new URL("https://" + cachedImgUrls.get(rand.nextInt(cachedImgUrls.size())));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        getLogger().info("CRAWLER FAILED");
        return null;
    }
    public List<String> getImgUrls(String html){
        Matcher matcher = Pattern.compile("(<img src=\\\"\\/\\/)(.*jpg?)").matcher(html);
        List<String> a=new ArrayList<>();
        while(matcher.find()){
            a.add(matcher.group(2));
        }
        return a;
    }
}        