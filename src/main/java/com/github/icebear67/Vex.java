package com.github.icebear67;

import net.mamoe.mirai.console.events.Events;
import net.mamoe.mirai.console.plugins.Config;
import net.mamoe.mirai.console.plugins.PluginBase;
import net.mamoe.mirai.message.GroupMessage;
import net.mamoe.mirai.utils.ExternalImage;
import net.mamoe.mirai.utils.ExternalImageJvmKt;
import org.nutz.http.Request;
import org.nutz.http.Sender;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Vex extends PluginBase {
    public static Config conf;
    private MessageHandler messageHandler=new MessageHandler(this);
    private List<String> cachedImgUrls;
    private long lastUpdate;
    private Random rand=new Random();

    public void onLoad() {
    }

    public void onEnable() {
        getLogger().info("Loading..");
        conf = this.getResourcesConfig("config.yml");
        Events.subscribeAlways(GroupMessage.class,messageHandler::onMessage);
        Events.subscribeAlways(GroupMessage.class,(GroupMessage gm)->{
            if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)<6){
                messageHandler.tryKill(gm);
            }
        });
    }
    public ExternalImage getFoodImg() {
        if(!(System.currentTimeMillis()-lastUpdate<600)){
            Request req = Request.create("https://www.xinshipu.com", Request.METHOD.GET);
            Sender sender=Sender.create(req);
            sender.send(response -> {
                cachedImgUrls=getImgUrls(response.getContent());
            });
            lastUpdate=System.currentTimeMillis();
        }
        ExternalImage img= null;
        try {
            img = ExternalImageJvmKt.toExternalImage(new URL(cachedImgUrls.get(rand.nextInt(cachedImgUrls.size()))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }
    public List<String> getImgUrls(String html){
        Matcher matcher = Pattern.compile("(<img src=\\\"\\/\\/)(.*jpg?)").matcher(html);
        List<String> a=new ArrayList<>();
        while(matcher.find()){
            a.add("https://"+matcher.group(2));
        }
        return a;
    }
}        