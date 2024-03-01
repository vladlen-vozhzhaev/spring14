package com.example.demo.controllers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

@Controller
public class AlfaBank {
    @GetMapping("/alfa")
    public String alfa(Model model){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        int month = calendar.get(Calendar.MONTH)+1;
        String yesterday = calendar.get(Calendar.YEAR)+"-0"+month+"-"+calendar.get(Calendar.DAY_OF_MONTH);
        double todayRUB = getRub(null);
        double yesterdayRUB = getRub(yesterday);
        System.out.println("Курс на сегодня: "+todayRUB);
        System.out.println("Курс на вчера: "+yesterdayRUB);
        String result = getGif("broke");
        if(todayRUB > yesterdayRUB){
            result = getGif("rich");
        }
        model.addAttribute("result", result);
        return "alfaBank";
    }
    double getRub(String date){
        double rub = 0;
        String endPoint = "latest.json";
        try {
            if(date != null){
                endPoint = "/historical/"+date+".json";
            }
            URL url = new URL("https://openexchangerates.org/api/"+endPoint+"?app_id=ad1c3a856f32428db34c670bfefe84c1");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            String result = "";
            while ((line = br.readLine()) != null){
                result += line;
            }
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            String rates = jsonObject.get("rates").toString();
            jsonObject = (JSONObject) jsonParser.parse(rates);
            rub = Double.parseDouble(jsonObject.get("RUB").toString());
            System.out.println(rub);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return rub;
    }
    String getGif(String q){
        String result = "";
        try {
            URL url = new URL("https://api.giphy.com/v1/gifs/search?api_key=EyyvmTSTaQArT1v0nqvg1c8tnOMcqgHL&q="+q);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null){
                result += line;
            }
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            JSONArray jsonArray = (JSONArray) jsonObject.get("data");
            int random = getIntRandom(0, 24);
            System.out.println(random);
            jsonObject = (JSONObject) jsonArray.get(random);
            String imageId = jsonObject.get("id").toString();
            result = ("https://i.giphy.com/"+imageId+".gif");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    int getIntRandom(int min, int max){
        return (int) Math.floor(Math.random()*(max-min)+min);
    }
}
