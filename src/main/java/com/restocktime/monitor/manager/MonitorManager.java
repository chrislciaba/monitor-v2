package com.restocktime.monitor.manager;

import com.restocktime.monitor.Monitor;
import com.restocktime.monitor.config.Config;
import com.restocktime.monitor.config.model.MonitorConfig;
import com.restocktime.monitor.config.model.Page;
import com.restocktime.monitor.helper.debug.DiscordLog;
import com.restocktime.monitor.helper.timeout.Timeout;
import com.restocktime.monitor.helper.url.UrlHelper;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.proxymanager.ProxyManager;
import com.restocktime.monitor.transformer.ConfigDataTransformer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MonitorManager {

    ConcurrentMap<String, Monitor> monitorMap;
    private DiscordLog discordLog;

    public MonitorManager(){
        this.monitorMap = new ConcurrentHashMap<>();
        this.discordLog = new DiscordLog(MonitorManager.class);
    }

    public void run(){
        while(true) {
            try {
                System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");

                long updated_at = Long.MIN_VALUE;
                Config c = new Config();

                while (true) {
                    discordLog.debug("Check for updates");

                    try {
                        Optional<MonitorConfig> monitorConfigOptional = c.loadConfigData(Long.toString(updated_at));

                        if(monitorConfigOptional.isPresent()) {
                            MonitorConfig monitorConfig = monitorConfigOptional.get();
                            ProxyManager proxyManager = new ProxyManager(monitorConfig.getProxyConfig(), c.getBasicMonitorConfig().getApiKey(), UrlHelper.deriveBaseUrl(c.getBasicMonitorConfig().getUrl()));

                            if (monitorConfig != null) {
                                if (updated_at != Long.MIN_VALUE && updated_at != monitorConfig.getGlobalSettings().getUpdated_at()) {
                                    refresh(monitorConfig, c, proxyManager);
                                } else {
                                    update(monitorConfig, c, proxyManager);
                                }

                                updated_at = monitorConfig.getGlobalSettings().getUpdated_at();
                            }

                        }

                    } catch (Exception e) {
                        discordLog.error("non fatal error in run()");
                    }

                    Timeout.timeout(10000);

                }
            } catch (Exception e) {
                discordLog.error("FATAL ERROR IN RUN MonitorManager run()");
            }
        }
    }

    private void refresh(MonitorConfig monitorConfig, Config c, ProxyManager proxyManager){
        discordLog.info("Updating config");
        for (String key : monitorMap.keySet()) {
            monitorMap.get(key).stop();
        }
        for (Page page : monitorConfig.getPages()) {
            try {

                    AbstractMonitor abstractMonitor = ConfigDataTransformer
                            .transformMonitor(
                                    page,
                                    monitorConfig.getGlobalSettings(),
                                    c.getBasicMonitorConfig().getNotifications(),
                                    monitorConfig.getNotifications(),
                                    proxyManager
                            );

                if (abstractMonitor == null) {
                    continue;
                }

                List<String> proxyList = proxyManager.getProxies(page.getSite());

                Monitor m = new Monitor(
                        abstractMonitor,
                        proxyList,
                        abstractMonitor.getUrl(),
                        page.getSite());
                m.start();
                monitorMap.put(page.getId(), m);
            } catch (Exception e){
                String msg = "";
                if(page.getUrls() != null && page.getUrls().size() > 0)
                    msg = page.getUrls().get(0);
                discordLog.debug(e.getMessage() + " this failed to refresh() " + msg);

            }
        }
    }

    private void update(MonitorConfig monitorConfig, Config c, ProxyManager proxyManager){
        Map<String, Monitor> tempMap = new HashMap<>();
        Map<String, String> allKeys = new HashMap<>();
        for (Page page : monitorConfig.getPages()) {
            try {
                if (c.getBasicMonitorConfig().getMode() != null && c.getBasicMonitorConfig().getMode().equals("shopify") && !page.getSite().contains("shopify"))
                    continue;
                else if (c.getBasicMonitorConfig().getMode() != null && c.getBasicMonitorConfig().getMode().equals("aio") && (page.getSite().contains("shopify") || page.getSite().equals("sns")))
                    continue;
                else if (c.getBasicMonitorConfig().getMode() != null && c.getBasicMonitorConfig().getMode().equals("sns") && !page.getSite().contains("sns"))
                    continue;

                if (!monitorMap.containsKey(page.getId())) {
                    AbstractMonitor abstractMonitor = ConfigDataTransformer.transformMonitor(page, monitorConfig.getGlobalSettings(), c.getBasicMonitorConfig().getNotifications(), monitorConfig.getNotifications(), proxyManager);
                    if (abstractMonitor == null) {
                        continue;
                    }

                    List<String> proxyList = proxyManager.getProxies(page.getSite());

                    Monitor m = new Monitor(
                            abstractMonitor,
                            proxyList,
                            abstractMonitor.getUrl(),
                            page.getSite());
                    m.start();
                    tempMap.put(page.getId(), m); //new tasks started
                }
                allKeys.put(page.getId(), ""); //all tasks
            } catch (Exception e){
                String msg = "";
                if(page.getUrls() != null && page.getUrls().size() > 0)
                    msg = page.getUrls().get(0);
                discordLog.error(e.getMessage() + " this failed to update() " + msg);

            }
        }

        for (String oldKey : monitorMap.keySet()) {
            if (!allKeys.containsKey(oldKey)) {
                Monitor toStop = monitorMap.get(oldKey);
                monitorMap.remove(oldKey);
                toStop.stop();
            }
        }

        for (String key : tempMap.keySet()) {
            monitorMap.put(key, tempMap.get(key));
        }

        tempMap.clear();
        allKeys.clear();
    }
}