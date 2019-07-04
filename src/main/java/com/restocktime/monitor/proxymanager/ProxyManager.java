package com.restocktime.monitor.proxymanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.config.model.Proxies;
import com.restocktime.monitor.config.model.ProxyConfig;
import com.restocktime.monitor.helper.url.UrlHelper;
import com.restocktime.monitor.proxymanager.model.ProxyList;
import com.restocktime.monitor.proxymanager.model.ProxyModel;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class ProxyManager {

    private final String PROXY_ENDPOINT = "%s/api/v2/proxies?accessToken=%s";

    private Map<String, SiteProxyManager> proxyLists;

    public ProxyManager(ProxyConfig proxyConfig, String apiKey, String baseUrl){
        proxyLists = new HashMap<>();
        if(proxyConfig == null || proxyConfig.getProxiesList() == null)
            return;
        initCache(String.format(PROXY_ENDPOINT, baseUrl, apiKey));
    }

    private void initCache(String filePath){
        while(true) {
            try {
                String proxies = performGet(filePath);
                ProxyModel proxyModel = new ObjectMapper().readValue(proxies, ProxyModel.class);
                for(String key : proxyModel.getProxies().keySet()){
                    ProxyList proxyList = proxyModel.getProxies().get(key);
                    Collections.shuffle(proxyList.getProxies());
                    SiteProxyManager siteProxyManager = new SiteProxyManager(proxyList.getProxies(), proxyList.getListSize());
                    proxyLists.put(key, siteProxyManager);
                }
                break;
            } catch (Exception e) {

            }
        }
    }

    public List<String> getProxies(String site){
        if(proxyLists.containsKey(site))
            return proxyLists.get(site).getProxyList();
        return proxyLists.get("aio").getProxyList();
    }


    private String performGet(String url){
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        String resp = "";
        try{
            HttpResponse httpResponse = httpClient.execute(httpGet);
            resp = EntityUtils.toString(httpResponse.getEntity());

        } catch (Exception e){

        } finally {
            httpGet.releaseConnection();
        }
        return resp;
    }


}
