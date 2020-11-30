package com.practical.Task.controllers;

import com.practical.Task.entities.ObjectEntity;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Abro on 11/30/2020.
 */
@Controller
public class HelloController {

    @GetMapping(value = "/")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;
    }

    @GetMapping(value = "/get-data")
    @ResponseBody
    public Map<String, Object> getData(){
        Map<String, Object> map = new HashMap<>();
        List<ObjectEntity> objectEntities = new ArrayList<>();
        //First from REST API
        for(int i=0; i < 10; i++){
            JSONObject obj = getRESTData();
            ObjectEntity entity = new ObjectEntity();
            try{
                entity.setTitle(obj.getString("title"));
                entity.setDescription(obj.getString("transcript"));
                entity.setPictureUrl(obj.getString("img"));
                String sDate = obj.getString("day")+"/"+obj.getString("month")+"/"+obj.getString("year");
                Date date = new SimpleDateFormat("dd/MM/yyyy").parse(sDate);
                entity.setDate(date);
            }catch (Exception ex){
                ex.printStackTrace();
            }

            objectEntities.add(entity);
        }

        // Second from RSS feed
        List<SyndEntry> listFeed = readRSSFeed();
        listFeed.forEach((x)->{
            ObjectEntity entity = new ObjectEntity();
            try{
                entity.setTitle(x.getTitle());
                entity.setWebUrl(x.getLink());
                entity.setPictureUrl(null);
                entity.setDescription(x.getDescription().getValue());
                entity.setPictureUrl(x.getAuthor());
                entity.setDate(x.getPublishedDate());
                String content = x.getContents().get(0).getValue();
                content = content.substring(content.indexOf("src")+5);
                String imgURL = content.substring(0,content.indexOf("\""));
                entity.setPictureUrl(imgURL);
            }catch (Exception ex){
                ex.printStackTrace();
            }
            objectEntities.add(entity);
        });

        // Sort by date
        Collections.sort(objectEntities);
        map.put("data", objectEntities);
        return map;
    }

    private List<SyndEntry> readRSSFeed(){
        SyndFeedInput input = new SyndFeedInput();
        List<SyndEntry> list = new ArrayList<>();
        try{
            URL feedSource = new URL("http://feeds.feedburner.com/PoorlyDrawnLines");
            SyndFeed feed = input.build(new XmlReader(feedSource));
            list = feed.getEntries();
        } catch (IOException ex){
            ex.printStackTrace();
        } catch (FeedException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    private JSONObject getRESTData(){
        Random rn = new Random();
        Integer rno = rn.nextInt(600 - 1 + 1) + 1;

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://xkcd.com/"+rno+"/info.0.json")
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            String jsonData = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonData);
            return jsonObject;
        }catch (Exception exception){
            exception.getMessage();
        }
        return null;
    }
}
