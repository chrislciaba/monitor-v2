package com.restocktime.monitor.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.restocktime.monitor.util.url.UrlHelper;
import com.restocktime.monitor.config.model.BasicMonitorConfig;
import com.restocktime.monitor.config.model.Links;
import com.restocktime.monitor.config.model.MonitorConfig;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class Config {
    final static Logger logger = Logger.getLogger(Config.class);
    private BasicMonitorConfig basicMonitorConfig;
    private ObjectMapper objectMapper;
    private Map<String, List<String>> proxyCache;

    public Config(){
        objectMapper = new ObjectMapper();
        proxyCache = new HashMap<>();
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("config.yml").getFile());
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            basicMonitorConfig = mapper.readValue(file, BasicMonitorConfig.class);

        } catch (Exception e) {
            // TODO Auto-generated catch block

        }
    }
    public String getStr(){
        String s = "";
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("response.json").getFile());

            BufferedReader b = new BufferedReader(new FileReader(file));

            String readLine;

            while ((readLine = b.readLine()) != null) {
                s = s+ readLine;
            }
        } catch(Exception e){

        }
        return s;
    }



    public List<String> readProxyList(String filePath){
        if(proxyCache.containsKey(filePath)){
            return new ArrayList<>(proxyCache.get(filePath));
        }

        List<String> x = new ArrayList<String>();
        try {
            if(UrlHelper.isValidUrl(filePath)){

                String proxies = performGet(filePath + "?accessToken=" + basicMonitorConfig.getApiKey()).get();
                proxies.replaceAll("\r", "");
                x = Arrays.asList(proxies.split("\n"));
            } else {
                ClassLoader classLoader = getClass().getClassLoader();
                File file = new File(classLoader.getResource(filePath).getFile());

                BufferedReader b = new BufferedReader(new FileReader(file));

                String readLine;

                while ((readLine = b.readLine()) != null) {
                    x.add(readLine);
                }
            }

        } catch (Exception e) {

        }
        proxyCache.put(filePath, new ArrayList<>(x));
        return x;
    }

    public List<NakedLogin> readNakedLogins(String filePath){
        List<NakedLogin> x = new ArrayList<NakedLogin>();
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(filePath).getFile());

            BufferedReader b = new BufferedReader(new FileReader(file));

            String readLine;

            while ((readLine = b.readLine()) != null) {
                String[] parts = readLine.split(":");
                if(parts.length != 2){
                    logger.error("bad login - " + readLine);
                }
                NakedLogin nakedLogin = new NakedLogin(parts[0], parts[1]);

                x.add(nakedLogin);
            }

        } catch (Exception e) {

        }

        return x;
    }

    public Optional<MonitorConfig> loadConfigData(String updatedAt){
        String url = basicMonitorConfig.getUrl() + "?accessToken=" + basicMonitorConfig.getApiKey() + "&updated_at=" + updatedAt;
        Optional<MonitorConfig> result = Optional.empty();
        try {
            Optional<String> resp = performGet(url);
            if(resp.isPresent()) {
                result = Optional.of(objectMapper.readValue(resp.get(), MonitorConfig.class));
            }
        } catch (Exception e){
            logger.error(EXCEPTION_LOG_MESSAGE, e);

        }

        return result;

    }

    public Links getLinks(){
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("links.yml").getFile());
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            Links links = mapper.readValue(file, Links.class);
            return links;
        } catch (Exception e) {
            logger.error(EXCEPTION_LOG_MESSAGE, e);
            return null;
        }
    }

    protected Optional<String> performGet(String url){
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        Optional<String> resp = Optional.empty();
        try{
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if(httpResponse.getStatusLine().getStatusCode() == 200){
                resp = Optional.of(EntityUtils.toString(httpResponse.getEntity()));
            }

        } catch (Exception e){
            logger.error(EXCEPTION_LOG_MESSAGE, e);
        } finally {
            httpGet.releaseConnection();
        }
        return resp;
    }

    public BasicMonitorConfig getBasicMonitorConfig() {
        return basicMonitorConfig;
    }
}
