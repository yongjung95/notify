package com.jung.notify.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class HolidaySchedule {

    static boolean isHoliday;

    public void holidaySchedule() throws Exception {
        isHoliday = false;

        LocalDate localDate = LocalDate.now();

        Date date = new Date();

        SimpleDateFormat todayFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");


        String year = String.valueOf(localDate.getYear());
//        String today = todayFormat.format(date);
        String today = "20230123";
        String month = monthFormat.format(date);

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=KLdvPxSGAauZtev%2FsdbbzYhBgSO5x3dVEer%2Bawauu4hAzWDdT%2BSLkGmIWX1tdQdBhJz0XHBk94pmSfg%2Bz86%2BsQ%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("solYear", "UTF-8") + "=" + URLEncoder.encode(year, "UTF-8")); /*연*/
        urlBuilder.append("&" + URLEncoder.encode("solMonth", "UTF-8") + "=" + URLEncoder.encode(month, "UTF-8")); /*월*/

        URL url = new URL(urlBuilder.toString());

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd;

        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }

        rd.close();
        conn.disconnect();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(new InputSource(new StringReader(sb.toString())));

        NodeList nodeList = document.getElementsByTagName("locdate");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i).getChildNodes().item(0);
            String Tag = node.getNodeValue();

            if (today.equals(Tag)) {
                isHoliday = true;
            }
        }
    }

    public boolean isIsHoliday() {
        return isHoliday;
    }
}
