package com.restocktime.monitor.transformer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.config.Config;
import com.restocktime.monitor.config.model.GlobalSettings;
import com.restocktime.monitor.config.model.NotificationsFormatConfig;
import com.restocktime.monitor.config.model.Page;
import com.restocktime.monitor.config.model.notifications.NotificationConfig;
import com.restocktime.monitor.monitors.ingest.Http2DefaultMonitor;
import com.restocktime.monitor.monitors.ingest.important.mesh.MeshAppMonitor;
import com.restocktime.monitor.monitors.parse.aio.grosbasket.GrosBasketResponseParser;
import com.restocktime.monitor.monitors.parse.aio.sneakerbarber.SneakerbarberResponseParser;
import com.restocktime.monitor.monitors.parse.aio.sportowysklep.SportowysklepResponseParser;
import com.restocktime.monitor.monitors.parse.aio.suppastore.SuppaStoreResponseParser;
import com.restocktime.monitor.monitors.parse.important.nike.desktop.NikeDesktopResponseParser;
import com.restocktime.monitor.monitors.parse.important.nike.snkrs.parse.helper.ParseV2Helper;
import com.restocktime.monitor.monitors.parse.important.panagora.api.PanagoraProductResponseParser;
import com.restocktime.monitor.monitors.parse.important.svd.parse.SvdAppResponseParser;
import com.restocktime.monitor.util.http.client.builder.ClientBuilder;
import com.restocktime.monitor.util.http.client.builder.model.BasicRequestClient;
import com.restocktime.monitor.util.bot.protection.hawk.Hawk;
import com.restocktime.monitor.util.http.request.AbstractHttpRequestHelper;
import com.restocktime.monitor.util.http.request.headers.FirefoxHeaderDecorator;
import com.restocktime.monitor.util.http.request.headers.GoogleChromeHeaderDecorator;
import com.restocktime.monitor.util.http.request.headers.useragent.FirefoxUserAgentGenerator;
import com.restocktime.monitor.util.http.request.headers.useragent.GoogleChromeUserAgentGenerator;
import com.restocktime.monitor.util.http.request.wrapper.CloudflareRequestWrapper;
import com.restocktime.monitor.util.http.request.Http2RequestHelper;
import com.restocktime.monitor.util.http.request.HttpRequestHelper;
import com.restocktime.monitor.util.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.util.helper.keywords.helper.KeywordFormatHelper;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.ingest.DefaultMonitor;
import com.restocktime.monitor.monitors.ingest.important.shopify.*;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.aio.acronym.AcronymParser;
import com.restocktime.monitor.monitors.parse.important.adidas.parse.AdidasResponseParser;
import com.restocktime.monitor.monitors.parse.funko.amazon.AmazonResponseParser;
import com.restocktime.monitor.monitors.parse.aio.antonioli.AntonioliResponseParser;
import com.restocktime.monitor.monitors.ingest.aio.backdoor.BackDoor;
import com.restocktime.monitor.monitors.parse.aio.backdoor.parse.BackdoorSearchResponseParser;
import com.restocktime.monitor.monitors.ingest.funko.barnesandnoble.BarnesAndNoble;
import com.restocktime.monitor.monitors.parse.funko.barnesandnoble.parse.BarnesAndNobleResponseParser;
import com.restocktime.monitor.monitors.parse.aio.bestbuy.BestBuyParseProductAbstractResponse;
import com.restocktime.monitor.monitors.parse.aio.bhvideo.BhVideoParseAbstractResponse;
import com.restocktime.monitor.monitors.ingest.important.bstn.BSTN;
import com.restocktime.monitor.monitors.parse.important.bstn.parse.BSTNParseProductAbstractResponse;
import com.restocktime.monitor.monitors.parse.important.bstn.parse.BSTNParseSearchAbstractResponse;
import com.restocktime.monitor.monitors.parse.important.bstn.parse.BstnParsePageResponse;
import com.restocktime.monitor.monitors.parse.important.panagora.caliroots.Caliroots;
import com.restocktime.monitor.monitors.parse.aio.citygear.CitygearAbstractResponseParser;
import com.restocktime.monitor.monitors.parse.aio.citygear.CitygearProductResponseParser;
import com.restocktime.monitor.monitors.parse.aio.complexcon.parse.ComplexconResponseParser;
import com.restocktime.monitor.monitors.ingest.aio.ccs.CCS;
import com.restocktime.monitor.monitors.parse.aio.parse.CCSResponseParser;
import com.restocktime.monitor.monitors.parse.funko.daveyboystoys.DaveyboystoysResponseParser;
import com.restocktime.monitor.monitors.ingest.funko.disney.DisneyRestock;
import com.restocktime.monitor.monitors.ingest.funko.disney.DisneySitemap;
import com.restocktime.monitor.monitors.parse.funko.parse.DisneyRestockResponseParser;
import com.restocktime.monitor.monitors.parse.funko.parse.DisneySitemapResponseParser;
import com.restocktime.monitor.monitors.parse.important.shopify.dsm.parse.DsmProductResponseParser;
import com.restocktime.monitor.monitors.parse.important.shopify.dsm.parse.ParseDSMAbstractResponse;
import com.restocktime.monitor.monitors.ingest.important.footdistrict.FootDistrict;
import com.restocktime.monitor.monitors.parse.important.footdistrict.helper.BotBypass;
import com.restocktime.monitor.monitors.parse.important.footdistrict.parse.FootdistrictParseProductAbstractResponse;
import com.restocktime.monitor.monitors.parse.important.footdistrict.parse.FootdistrictParseSearchAbstractResponse;
import com.restocktime.monitor.monitors.parse.important.footdistrict.parse.FootdistrictParseSitemapResponse;
import com.restocktime.monitor.monitors.parse.important.mesh.parse.MeshApp;
import com.restocktime.monitor.monitors.parse.important.mesh.parse.MeshSearchResponseParser;
import com.restocktime.monitor.monitors.parse.important.mesh.parse.MeshFrontEndStockResponseParser;
import com.restocktime.monitor.monitors.ingest.important.footsites.Footsites;
import com.restocktime.monitor.monitors.parse.important.footsites.parse.FootsitesResponseParser;
import com.restocktime.monitor.monitors.parse.aio.frenzy.parse.FrenzyResponseParser;
import com.restocktime.monitor.monitors.parse.funko.funimation.FunimationResponseParser;
import com.restocktime.monitor.monitors.parse.funko.fye.FyeSearchResponseParser;
import com.restocktime.monitor.monitors.parse.funko.gamestop.parse.GamestopResponseParser;
import com.restocktime.monitor.monitors.parse.funko.gemini.parse.GeminiResponseParser;
import com.restocktime.monitor.monitors.parse.funko.gemini.parse.GeminiRestockResponseParser;
import com.restocktime.monitor.monitors.ingest.social.instagram.InstagramStory;
import com.restocktime.monitor.monitors.parse.social.instagram.parse.InstagramResponseParser;
import com.restocktime.monitor.monitors.parse.social.instagram.parse.InstagramStoryResponseParser;
import com.restocktime.monitor.monitors.ingest.aio.jimmyjazz.JimmyJazz;
import com.restocktime.monitor.monitors.parse.aio.parse.JimmyJazzResponseParser;
import com.restocktime.monitor.monitors.ingest.aio.lvr.LVR;
import com.restocktime.monitor.monitors.parse.aio.lvr.parse.LvrResponseParser;
import com.restocktime.monitor.monitors.parse.important.panagora.naked.NakedResponseParser;
import com.restocktime.monitor.monitors.parse.aio.nittygritty.NittyGrittyAbstractResponseParser;
import com.restocktime.monitor.monitors.parse.important.offspring.Offspring;
import com.restocktime.monitor.monitors.parse.important.offwhite.OffWhite;
import com.restocktime.monitor.monitors.parse.important.offwhite.OffWhiteAll;
import com.restocktime.monitor.monitors.parse.important.offwhite.OffWhiteAtc;
import com.restocktime.monitor.monitors.parse.important.offwhite.OffWhitePage;
import com.restocktime.monitor.monitors.parse.important.offwhite.helper.GetLinksFromPage;
import com.restocktime.monitor.monitors.parse.important.offwhite.parse.*;
import com.restocktime.monitor.monitors.parse.aio.onygo.OnygoSitemapResponseParser;
import com.restocktime.monitor.monitors.parse.aio.patta.PattaAbstractResponseParser;
import com.restocktime.monitor.monitors.parse.aio.patta.PattaProductResponseParser;
import com.restocktime.monitor.monitors.parse.funko.popcultcha.parse.PopCultchaResponseParser;
import com.restocktime.monitor.monitors.ingest.aio.porter.Porter;
import com.restocktime.monitor.monitors.parse.aio.porter.helper.PorterHelper;
import com.restocktime.monitor.monitors.parse.aio.porter.parse.ApiAbstractResponseParser;
import com.restocktime.monitor.monitors.parse.aio.porter.parse.AtcResponseParser;
import com.restocktime.monitor.monitors.parse.aio.rimowa.RimowaResponseParser;
import com.restocktime.monitor.monitors.parse.aio.sevenhills.SevenHillsResponseParser;
import com.restocktime.monitor.monitors.parse.important.shoepalace.ShoepalaceResponseParser;
import com.restocktime.monitor.monitors.parse.important.shopify.helper.ShopifyFrontendHelper;
import com.restocktime.monitor.monitors.parse.important.shopify.helper.linkchecker.LinkCheckStarter;
import com.restocktime.monitor.util.helper.stocktracker.StockTracker;
import com.restocktime.monitor.util.helper.url.UrlHelper;
import com.restocktime.monitor.config.model.notifications.SiteNotificationsConfig;
import com.restocktime.monitor.monitors.ingest.aio.demandware.DemandwareGet;
import com.restocktime.monitor.monitors.parse.aio.demandware.parse.DemandwareGetResponseParser;
import com.restocktime.monitor.monitors.parse.important.shopify.parse.*;
import com.restocktime.monitor.monitors.ingest.important.snkrs.NikeScratch;
import com.restocktime.monitor.monitors.parse.important.nike.snkrs.parse.HuntResponseParser;
import com.restocktime.monitor.monitors.parse.important.nike.snkrs.parse.ProductFeedV2ResponseParser;
import com.restocktime.monitor.monitors.parse.important.panagora.sns.SNSProductResponseParser;
import com.restocktime.monitor.monitors.parse.important.panagora.sns.SnsResponseParser;
import com.restocktime.monitor.monitors.parse.important.solebox.SoleboxProductPageResponseParser;
import com.restocktime.monitor.monitors.parse.important.solebox.SoleboxResponseParser;
import com.restocktime.monitor.monitors.parse.aio.soto.SotoResponseParser;
import com.restocktime.monitor.monitors.ingest.important.ssense.Ssense;
import com.restocktime.monitor.monitors.parse.important.ssense.parse.PageResponseParser;
import com.restocktime.monitor.monitors.parse.important.ssense.parse.SearchResponseParser;
import com.restocktime.monitor.monitors.parse.important.supreme.parse.SupremeAllProductResponseParser;
import com.restocktime.monitor.monitors.parse.important.supreme.parse.SupremePageResponseParser;
import com.restocktime.monitor.monitors.parse.important.supreme.parse.SupremeProductParseAbstractResponse;
import com.restocktime.monitor.monitors.ingest.important.svd.SVD;
import com.restocktime.monitor.monitors.parse.important.svd.parse.SvdProductResponseParser;
import com.restocktime.monitor.monitors.parse.important.svd.parse.SvdSearchAbstractResponseParser;
import com.restocktime.monitor.monitors.parse.funko.target.Target;
import com.restocktime.monitor.monitors.parse.funko.target.parse.TargetAbstractResponseParser;
import com.restocktime.monitor.monitors.parse.aio.thenextdoor.TheNextDoorResponseParser;
import com.restocktime.monitor.monitors.ingest.aio.titolo.Titolo;
import com.restocktime.monitor.monitors.parse.aio.parse.TitoloProductResponseParser;
import com.restocktime.monitor.monitors.parse.aio.parse.TitoloSearchResponseParser;
import com.restocktime.monitor.monitors.ingest.social.twitter.Twitter;
import com.restocktime.monitor.monitors.parse.social.twitter.parse.TwitterAbstractResponseParser;
import com.restocktime.monitor.monitors.parse.funko.walgreens.WalgreensResponseParser;
import com.restocktime.monitor.monitors.parse.funko.walgreens.WalgreensSearchResponseParser;
import com.restocktime.monitor.monitors.parse.funko.walmart.Walmart;
import com.restocktime.monitor.monitors.parse.funko.walmart.WalmartTerra;
import com.restocktime.monitor.monitors.parse.funko.walmart.parse.WalmartResponseParser;
import com.restocktime.monitor.monitors.parse.funko.walmart.parse.WalmartTerraResponseParser;
import com.restocktime.monitor.monitors.parse.important.panagora.yme.YmeResponseParser;
import com.restocktime.monitor.monitors.parse.important.shopify.ys.parse.YsResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.attachments.transformer.NotificationsConfigTransformer;
import com.restocktime.monitor.proxymanager.ProxyManager;
import okhttp3.ConnectionPool;
import org.apache.http.impl.client.HttpClients;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class ConfigDataTransformer {

    public static AbstractMonitor transformMonitor(Page page, GlobalSettings globalSettings, NotificationsFormatConfig notificationsFormatConfig, SiteNotificationsConfig siteNotificationsConfig, ProxyManager proxyManager){
        String[] apiKeys = globalSettings.getApiKeys().split(",");
        String defaultKw = globalSettings.getDefaultShopifyKw();

        String site = page.getSite();
        String url = page.getUrls().get(0).trim();

        if(site.equals("solebox")){
            SoleboxResponseParser soleboxResponseParser = new SoleboxResponseParser(new StockTracker(new HashMap<>(), 0), url,  NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getSolebox()));
            return createDefault(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getSolebox(), notificationsFormatConfig), new HttpRequestHelper(), soleboxResponseParser);
        } else if(site.equals("bstn")){
            BSTNParseSearchAbstractResponse bstnParseSearchResponse = new BSTNParseSearchAbstractResponse(new StockTracker(new HashMap<>(), 0), url, NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getBstn()));
            BSTNParseProductAbstractResponse bstnParseProductResponse = new BSTNParseProductAbstractResponse(new StockTracker(new HashMap<>(), 0), url, NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getBstn()));
            BstnParsePageResponse bstnParsePageResponse = new BstnParsePageResponse(new StockTracker(new HashMap<>(), 0), new KeywordSearchHelper(page.getSku()), url, NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getBstn()));
            return new BSTN(url, page.getSku(), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getBstn(), notificationsFormatConfig), new CloudflareRequestWrapper(apiKeys, new HttpRequestHelper(), HttpClients.createDefault()), bstnParseProductResponse, bstnParseSearchResponse, bstnParsePageResponse);
        } else if(site.equals("naked")){

            AbstractResponseParser parseNakedResponse = new NakedResponseParser(new StockTracker(new HashMap<>(), 500000), url, NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getNaked()));
            return createDefault(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getNaked(), notificationsFormatConfig), new CloudflareRequestWrapper(apiKeys, new HttpRequestHelper(), HttpClients.createDefault()), parseNakedResponse);
        } else if(site.equals("shopify")){
            NotificationConfig notificationConfig = getShopifyConfig(url, siteNotificationsConfig);
            ShopifyAbstractResponseParser shopifyResponseParser = new ShopifyAbstractResponseParser(new StockTracker(new HashMap<>(), 0), url, NotificationsConfigTransformer.transformNotifications(notificationConfig));
            return new Shopify(url, page.getDelay(), new AttachmentCreater(notificationConfig, notificationsFormatConfig), new HttpRequestHelper() , shopifyResponseParser);
        } else if(site.equals("shopifyresi")){
            NotificationConfig notificationConfig = getShopifyConfig(url, siteNotificationsConfig);
            ShopifyAbstractResponseParser shopifyResponseParser = new ShopifyAbstractResponseParser(new StockTracker(new HashMap<>(), 0), url, NotificationsConfigTransformer.transformNotifications(notificationConfig));
            return new Shopify(url, page.getDelay(), new AttachmentCreater(notificationConfig, notificationsFormatConfig), new HttpRequestHelper() , shopifyResponseParser);
        } else if(site.equals("shopifyallproducts")){
            NotificationConfig notificationConfig = getShopifyConfig(url, siteNotificationsConfig);

            ShopifyProductListingsResponseParser shopifyProductListingsResponseParser =
                    new ShopifyProductListingsResponseParser(new StockTracker(new HashMap<>(), 0), new KeywordSearchHelper(KeywordFormatHelper.getKeywordString(defaultKw, page.getSku())), url, NotificationsConfigTransformer.transformNotifications(notificationConfig));
            return new ShopifyProductListings(url, page.getDelay(), new AttachmentCreater(notificationConfig, notificationsFormatConfig), new HttpRequestHelper(), shopifyProductListingsResponseParser);
        } else if(site.equals("shopifyproducts")){
            NotificationConfig notificationConfig = getShopifyConfig(url, siteNotificationsConfig);

            ShopifyProductsResponseParser shopifyProductsResponseParser =
                    new ShopifyProductsResponseParser(new StockTracker(new HashMap<>(), 0), new KeywordSearchHelper(KeywordFormatHelper.getKeywordString(defaultKw, page.getSku())), url, NotificationsConfigTransformer.transformNotifications(notificationConfig));
            return new ShopifyProducts(url, page.getDelay(), new AttachmentCreater(notificationConfig, notificationsFormatConfig), new HttpRequestHelper(), shopifyProductsResponseParser);
        } else if(site.equals("shopifyproductsresi")){
            NotificationConfig notificationConfig = getShopifyConfig(url, siteNotificationsConfig);

            ShopifyProductsResponseParser shopifyProductsResponseParser =
                    new ShopifyProductsResponseParser(new StockTracker(new HashMap<>(), 0), new KeywordSearchHelper(KeywordFormatHelper.getKeywordString(defaultKw, page.getSku())), url, NotificationsConfigTransformer.transformNotifications(notificationConfig));
            return new ShopifyProducts(url, page.getDelay(), new AttachmentCreater(notificationConfig, notificationsFormatConfig), new HttpRequestHelper(), shopifyProductsResponseParser);
        } else if(site.equals("shopifyatom")){
            Config c = new Config();
            NotificationConfig notificationConfig = getShopifyConfig(url, siteNotificationsConfig);

            List<String> proxies = proxyManager.getProxies("shopify");
            ClientBuilder clientBuilder = new ClientBuilder();
            List<BasicRequestClient> clients = clientBuilder.buildClients(UrlHelper.deriveBaseUrl(url), proxies, page.getSite());
            LinkCheckStarter linkCheckStarter = new LinkCheckStarter(clients);
            ShopifyAtomResponseParser shopifyAtomResponseParser =
                    new ShopifyAtomResponseParser(
                            url,
                            new StockTracker(new HashMap<>(), 0),
                            new KeywordSearchHelper(KeywordFormatHelper.getKeywordString(defaultKw, page.getSku())),
                            false,
                            UrlHelper.getHost(url),
                            linkCheckStarter,
                            true,
                            NotificationsConfigTransformer.transformNotifications(notificationConfig));
            return new ShopifyAtom(UrlHelper.deriveBaseUrl(url) + "/collections/all.atom", page.getDelay(), new AttachmentCreater(notificationConfig, notificationsFormatConfig), new HttpRequestHelper(), shopifyAtomResponseParser);
        }  else if(site.equals("shopifykw")){
            NotificationConfig notificationConfig = getShopifyConfig(url, siteNotificationsConfig);

            List<String> proxies = proxyManager.getProxies("shopify");
            ClientBuilder clientBuilder = new ClientBuilder();
            List<BasicRequestClient> clients = clientBuilder.buildClients(UrlHelper.deriveBaseUrl(url), proxies, page.getSite());
            LinkCheckStarter linkCheckStarter = new LinkCheckStarter(clients);

            ShopifyKwAbstractResponseParser shopifyKwResponseParser = new ShopifyKwAbstractResponseParser(
                    url,
                    new StockTracker(new HashMap<>(), 0),
                    new KeywordSearchHelper(KeywordFormatHelper.getKeywordString(defaultKw, page.getSku())),
                    false,
                    UrlHelper.getHost(url),
                    linkCheckStarter,
                    true,
                    new AttachmentCreater(notificationConfig, notificationsFormatConfig),
                    NotificationsConfigTransformer.transformNotifications(notificationConfig));
            return new ShopifyKw(url,
                    page.getDelay(),
                    new AttachmentCreater(notificationConfig, notificationsFormatConfig),
                    new HttpRequestHelper(),
                    shopifyKwResponseParser
            );
        } else if(site.equals("end")){
            return  null;/*

            return new EndClothing(url, page.getDelay(), slackConfig.getEndclothing(), discordConfig.getEndclothing());*/
        } else if(site.equals("offwhite")){

            OffWhiteSearchAbstractResponseParser offWhiteSearchResponseParser = new OffWhiteSearchAbstractResponseParser(new StockTracker(new HashMap<>(), -1), UrlHelper.deriveBaseUrl(url), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getOffwhite()));
            OffWhiteProductAbstractResponseParser offWhiteProductResponseParser = new OffWhiteProductAbstractResponseParser(new StockTracker(new HashMap<>(), 10000), url, page.getName(), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getOffwhite()));

            return new OffWhite(url + ".json", page.getLocale(), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getOffwhite(), notificationsFormatConfig), new CloudflareRequestWrapper(apiKeys, new HttpRequestHelper(), HttpClients.createDefault()), offWhiteProductResponseParser, offWhiteSearchResponseParser);
        } else if(site.equals("ssense")){
            PageResponseParser pageResponseParser = new PageResponseParser(new StockTracker(new HashMap<>(), 30000), url,  NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getSsense()));
            SearchResponseParser searchResponseParser = new SearchResponseParser(page.getLocale(), url, page.getName(), new StockTracker(new HashMap<>(), -1), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getSsense()));
            return new Ssense(url,
                    page.getDelay(),
                    page.getLocale(),
                    new AttachmentCreater(siteNotificationsConfig.getSsense(), notificationsFormatConfig),
                    new HttpRequestHelper(),
                    pageResponseParser,
                    searchResponseParser
            );
        } else if(site.equals("snkrs")){

            NotificationConfig notificationConfig;

            if(page.getLocale().equals("US")){
                notificationConfig = siteNotificationsConfig.getSnkrs();
            } else if(page.getLocale().equals("UK")){
                notificationConfig = siteNotificationsConfig.getSnkrsGb();
            } else if(page.getLocale().equals("JP")) {
                notificationConfig = siteNotificationsConfig.getSnkrsJp();
            } else if(page.getLocale().equals("CN")) {
                notificationConfig = siteNotificationsConfig.getSnkrsCn();
            } else {
                notificationConfig = siteNotificationsConfig.getSnkrsOther();
            }

            List<String> formatNames = NotificationsConfigTransformer.transformNotifications(notificationConfig);

            ProductFeedV2ResponseParser productFeedV2ResponseParser = new ProductFeedV2ResponseParser(new ObjectMapper(), new StockTracker(new HashMap<>(), -1), page.getLocale(), formatNames, url);

            return createDefault(url, page.getDelay(), new AttachmentCreater(notificationConfig, notificationsFormatConfig), new HttpRequestHelper(), productFeedV2ResponseParser);
        } else if(site.equals("porter")){
            ApiAbstractResponseParser apiResponseParser = new ApiAbstractResponseParser(new StockTracker(new HashMap<>(), 500000), PorterHelper.getKey(url), page.getLocale(), page.getSku(), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getPorter()));
            AtcResponseParser atcResponseParser = new AtcResponseParser(new StockTracker(new HashMap<>(), 500000), PorterHelper.getKey(url), page.getLocale(), page.getSku(), url, page.getName());
            return new Porter(
                    url,
                    page.getLocale(),
                    page.getDelay(),
                    new AttachmentCreater(siteNotificationsConfig.getPorter(), notificationsFormatConfig),
                    new HttpRequestHelper(),
                    apiResponseParser,
                    atcResponseParser
            );
        } else if(site.equals("adidas")){
            AdidasResponseParser adidasResponseParser = new AdidasResponseParser(new StockTracker(new HashMap<>(), 0), page.getSku(), url, page.getName(), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getAdidas()));

            return createDefault(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getAdidas(), notificationsFormatConfig), new HttpRequestHelper(), adidasResponseParser);
        } else if(site.equals("svd")){
            SvdProductResponseParser svdProductResponseParser = new SvdProductResponseParser(new StockTracker(new HashMap<>(), 0), url, NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getSvd()));
            SvdSearchAbstractResponseParser svdSearchResponseParser = new SvdSearchAbstractResponseParser(new StockTracker(new HashMap<>(), 0), url, page.getName(), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getSvd()));
            return new SVD(url, page.getSku(), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getSvd(), notificationsFormatConfig), new HttpRequestHelper(), svdProductResponseParser, svdSearchResponseParser);
        } else if(site.equals("titolo")){
            TitoloProductResponseParser titoloProductResponseParser = new TitoloProductResponseParser(page.getName(), new StockTracker(new HashMap<>(), 500*1000), url, NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getTitolo()));
            TitoloSearchResponseParser titoloSearchResponseParser = new TitoloSearchResponseParser(new StockTracker(new HashMap<>(), -1), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getTitolo()));
            return new Titolo(url, page.getSku(), page.getName(), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getTitolo(), notificationsFormatConfig), new HttpRequestHelper(), titoloProductResponseParser, titoloSearchResponseParser);
        } else if(site.equals("yme")){
            YmeResponseParser ymeResponseParser = new YmeResponseParser(new StockTracker(new HashMap<>(), 0), url, NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getYme()));
            return createDefault(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getOneblockdown(), notificationsFormatConfig), new CloudflareRequestWrapper(apiKeys, new HttpRequestHelper(), HttpClients.createDefault()), ymeResponseParser);
        } else if(site.equals("supreme")){
            List<String> formats = null;
            NotificationConfig notificationConfig = null;
            if(page.getLocale().equals("US")) {
                formats = NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getSupreme());
                notificationConfig = siteNotificationsConfig.getSupreme();
            }
            else if(page.getLocale().equals("UK")) {
                formats = NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getSupremeuk());
                notificationConfig = siteNotificationsConfig.getSupreme();
            }
            else
                return null;
            SupremeProductParseAbstractResponse supremeProductParseResponse = new SupremeProductParseAbstractResponse(new StockTracker(new HashMap<>(), 0), url, page.getName(), page.getLocale(), formats);
            return createDefault(url, page.getDelay(), new AttachmentCreater(notificationConfig, notificationsFormatConfig), new HttpRequestHelper(), supremeProductParseResponse);
        } else if(site.equals("DSM")){
            List<String> proxies = proxyManager.getProxies("shopify");
            ClientBuilder clientBuilder = new ClientBuilder();
            List<BasicRequestClient> clients = clientBuilder.buildClients(UrlHelper.deriveBaseUrl(url), proxies, page.getSite());
            LinkCheckStarter linkCheckStarter = new LinkCheckStarter(clients);

            ParseDSMAbstractResponse parseDSMResponse = new ParseDSMAbstractResponse(
                    new StockTracker(new HashMap<>(), -1),
                    url,
                    page.getLocale(),
                    linkCheckStarter,
                    new KeywordSearchHelper(new KeywordSearchHelper(KeywordFormatHelper.getKeywordString(defaultKw, page.getSku()))),
                    NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getDsm()));
            return createDefault(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getDsm(), notificationsFormatConfig), new HttpRequestHelper(), parseDSMResponse);
        } else if(site.equals("toytokyo")){
            //return new ToyTokyo(url, page.getDelay(), slackConfig.getToytokyo(), discordConfig.getToytokyo(), new AttachmentCreater(siteNotificationsConfig.getShopify(), notificationsFormatConfig), new HttpRequestHelper());
        } else if(site.equals("target")){
            StockTracker stockTracker = new StockTracker(new HashMap<>(), 360000);
            TargetAbstractResponseParser targetResponseParser = new TargetAbstractResponseParser(new ObjectMapper(), stockTracker, NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getTarget()), page.getSku());
            return new Target(page.getSku(), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getTarget(), notificationsFormatConfig), new HttpRequestHelper(), targetResponseParser);
        }/* else if(site.equals("hottopic")){
            Notifications notifications = new Notifications(discordConfig.getHottopic(), slackConfig.getHottopic());
            ParseHotTopicAbstractResponse parseHotTopicResponse = new ParseHotTopicAbstractResponse(new StockTracker(new HashMap<>(), 0), url);
            return new HotTopic(url, page.getDelay(), notifications, new AttachmentCreater(siteNotificationsConfig.getShopify(), notificationsFormatConfig), new HttpRequestHelper(), parseHotTopicResponse);
        }*/ else if(site.equals("bhvideo")){
            BhVideoParseAbstractResponse bhVideoParseResponse = new BhVideoParseAbstractResponse(new StockTracker(new HashMap<>(), 0), url, NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getTarget()));
            return createDefault(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getTarget(), notificationsFormatConfig), new HttpRequestHelper(), bhVideoParseResponse);
        } else if(site.equals("bestbuy")){
            BestBuyParseProductAbstractResponse bestBuyParseProductResponse = new BestBuyParseProductAbstractResponse(
                    new StockTracker(new HashMap<>(), 0),
                    url,
                    NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getBestbuy())
            );
            return createDefault(
                    url,
                    page.getDelay(),
                    new AttachmentCreater(siteNotificationsConfig.getBestbuy(), notificationsFormatConfig),
                    new HttpRequestHelper(),
                    bestBuyParseProductResponse
            );
        } else if(site.equals("scratcher")){

            HuntResponseParser huntResponseParser = new HuntResponseParser(new StockTracker(new HashMap<>(), -1), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getSnkrs()));
            return new NikeScratch(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getSnkrs(), notificationsFormatConfig), new HttpRequestHelper(), huntResponseParser);
        } else if(site.equals("acronym")){
            AcronymParser acronymParseResponse = new AcronymParser(new StockTracker(new HashMap<>(), -1),  new KeywordSearchHelper(page.getSku()), url, NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getAcronym()));
            return createDefault(url,
                    page.getDelay(),
                    new AttachmentCreater(siteNotificationsConfig.getAcronym(), notificationsFormatConfig),
                    new HttpRequestHelper(),
                    acronymParseResponse
            );
        } else if(site.equals("sjs")){
           // return new SJS(url, page.getDelay(), slackConfig.getAcronym(), discordConfig.getAcronym(), new AttachmentCreater(siteNotificationsConfig.getShopify(), notificationsFormatConfig), new HttpRequestHelper());
        } else if(site.equals("footdistrict")){
            AbstractResponseParser abstractResponseParser;
            if(url.contains("sitemap")) {
                abstractResponseParser = new FootdistrictParseSitemapResponse(new StockTracker(new HashMap<>(), 0), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getFootdistrict()));
            } else {
                abstractResponseParser = new FootdistrictParseProductAbstractResponse(new StockTracker(new HashMap<>(), -1), url, NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getFootdistrict()));
            }

            BotBypass botBypass = new BotBypass();
            return new FootDistrict(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getFootdistrict(), notificationsFormatConfig), new HttpRequestHelper(), botBypass, abstractResponseParser);
        } else if(site.equals("footdistrictsearch")){
            try {
                AbstractResponseParser abstractResponseParser = new FootdistrictParseSearchAbstractResponse(new StockTracker(new HashMap<>(), -1), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getFootdistrict()));
                String url1 = "https://eu1-search.doofinder.com/5/search?hashid=5d2f6b5036133b0035d084057bbc695d&query_counter=6&page=1&rpp=30&transformer=basic&query="
                        + URLEncoder.encode(page.getSku(), StandardCharsets.UTF_8.toString());
                return createDefault(url1,
                        page.getDelay(),
                        new AttachmentCreater(siteNotificationsConfig.getFootdistrict(), notificationsFormatConfig),
                        new HttpRequestHelper(),
                        abstractResponseParser
                );

            } catch ( Exception e) {
                return null;
            }

        } else if(site.equals("patta")){
            PattaAbstractResponseParser pattaResponseParser = new PattaAbstractResponseParser(new StockTracker(new HashMap<>(), -1), new KeywordSearchHelper(defaultKw), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getPatta()));
            return createDefault(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getPatta(), notificationsFormatConfig), new CloudflareRequestWrapper(apiKeys, new HttpRequestHelper(), HttpClients.createDefault()), pattaResponseParser);
        } else if(site.equals("pattaproduct")) {
            PattaProductResponseParser pattaProductResponseParser = new PattaProductResponseParser(url, new StockTracker(new HashMap<>(), 0), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getPatta()));
            return createDefault(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getPatta(), notificationsFormatConfig), new CloudflareRequestWrapper(apiKeys, new HttpRequestHelper(), HttpClients.createDefault()), pattaProductResponseParser);
        } else if(site.equals("thenextdoor")){
            TheNextDoorResponseParser theNextDoorResponseParser = new TheNextDoorResponseParser(new StockTracker(new HashMap<>(), 0), new KeywordSearchHelper(page.getSku()), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getThenextdoor()));
            return createDefault(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getThenextdoor(), notificationsFormatConfig), new HttpRequestHelper(), theNextDoorResponseParser);
        } else if(site.equals("nittygritty")){
            KeywordSearchHelper keywordSearchHelper = new KeywordSearchHelper(page.getSku());
            StockTracker stockTracker = new StockTracker(new HashMap<>(), -1);
            NittyGrittyAbstractResponseParser nittyGrittyResponseParser = new NittyGrittyAbstractResponseParser(stockTracker, keywordSearchHelper, NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getNittygritty()));
            return createDefault(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getNittygritty(), notificationsFormatConfig), new HttpRequestHelper(), nittyGrittyResponseParser);
        } else if(site.equals("backdoor")){
            StockTracker stockTracker = new StockTracker(new HashMap<>(), -1);
            BackdoorSearchResponseParser backdoorSearchResponseParser = new BackdoorSearchResponseParser(page.getSku(), url, stockTracker, NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getBackdoor()));
            return new BackDoor(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getBackdoor(), notificationsFormatConfig), new HttpRequestHelper(), backdoorSearchResponseParser);
        } else if(site.equals("offwhiteatc")){
            StockTracker stockTracker = new StockTracker(new HashMap<>(), 30000);
            OffWhiteAtcResponseParser offWhiteAtcResponseParser = new OffWhiteAtcResponseParser(url, stockTracker, new ObjectMapper(), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getOffwhite()));
            OffWhiteAtcIncrementResponseParser offWhiteAtcIncrementResponseParser = new OffWhiteAtcIncrementResponseParser(page.getName(),stockTracker, new ObjectMapper(), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getOffwhite()));
            return new OffWhiteAtc(url, page.getSku(), page.getLocale(), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getOffwhite(), notificationsFormatConfig), new CloudflareRequestWrapper(apiKeys, new HttpRequestHelper(), HttpClients.createDefault()), offWhiteAtcResponseParser);
        } else if(site.equals("frenzy")){
            StockTracker stockTracker = new StockTracker(new HashMap<>(), -1);
            FrenzyResponseParser frenzyResponseParser = new FrenzyResponseParser(stockTracker, new ObjectMapper(), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getFrenzy()));
            return createDefault(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getFrenzy(), notificationsFormatConfig), new HttpRequestHelper(), frenzyResponseParser);
        } else if(site.equals("rimowa")){
            StockTracker stockTracker = new StockTracker(new HashMap<>(), 0);
            RimowaResponseParser rimowaResponseParser = new RimowaResponseParser(stockTracker, url,  NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getRimowa()));
            return createDefault(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getRimowa(), notificationsFormatConfig), new HttpRequestHelper(), rimowaResponseParser);
        } else if(site.equals("antonioli")){
            StockTracker stockTracker = new StockTracker(new HashMap<>(), -1);
            AttachmentCreater attachmentCreater = new AttachmentCreater(siteNotificationsConfig.getAntonioli(), notificationsFormatConfig);
            HttpRequestHelper httpRequestHelper = new HttpRequestHelper();
            AntonioliResponseParser antonioliResponseParser = new AntonioliResponseParser(url, page.getName(), stockTracker, NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getAntonioli()));

            return createDefault(url, page.getDelay(),  attachmentCreater, httpRequestHelper, antonioliResponseParser);
        } else if(site.equals("twitter") || site.equals("ftlcodes")){
            StockTracker stockTracker = new StockTracker(new HashMap<>(), -1);
            KeywordSearchHelper keywordSearchHelper = new KeywordSearchHelper(page.getSku());
            NotificationConfig notificationConfig;
            if(site.equals("twitter"))
                notificationConfig = siteNotificationsConfig.getTwitter();
            else
                notificationConfig = siteNotificationsConfig.getFtlcodes();

            TwitterAbstractResponseParser rimowaResponseParser = new TwitterAbstractResponseParser(stockTracker, url, keywordSearchHelper, NotificationsConfigTransformer.transformNotifications(notificationConfig));
            return new Twitter(url, page.getDelay(), new AttachmentCreater(notificationConfig, notificationsFormatConfig), new HttpRequestHelper(), rimowaResponseParser);
        } else if(site.equals("sns")){
            StockTracker stockTracker = new StockTracker(new HashMap<>(), -1);
            SnsResponseParser snsResponseParser = new SnsResponseParser(stockTracker, new KeywordSearchHelper(page.getSku()), url, NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getSns()));
            return Http2DefaultMonitor.builder()
                    .url(url)
                    .delay(page.getDelay())
                    .attachmentCreater(new AttachmentCreater(siteNotificationsConfig.getSns(), notificationsFormatConfig))
                    .httpRequestHelper(new HttpRequestHelper())
                    .abstractResponseParser(snsResponseParser)
                    .addUUID(false)
                    .build();
        } else if(site.equals("offwhitepage")){
            OffWhiteSoldOutTagResponseParser offWhiteSoldOutTagResponseParser = new OffWhiteSoldOutTagResponseParser(page.getSku(), new StockTracker(new HashMap<>(), 10000),  NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getOffwhite()));
            return new OffWhitePage(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getOffwhite(), notificationsFormatConfig), new CloudflareRequestWrapper(apiKeys, new HttpRequestHelper(), HttpClients.createDefault()), offWhiteSoldOutTagResponseParser);
        } else if(site.equals("complexcon")){
            StockTracker stockTracker = new StockTracker(new HashMap<>(), 0);
            ComplexconResponseParser complexconResponseParser = new ComplexconResponseParser(stockTracker, new ObjectMapper(), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getComplexcon()));
            return createDefault(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getComplexcon(), notificationsFormatConfig), new HttpRequestHelper(), complexconResponseParser);
        } else if(site.equals("meshstock")){
            StockTracker stockTracker = new StockTracker(new HashMap<>(), 30000);
            MeshFrontEndStockResponseParser meshFrontEndStockResponseParser = new MeshFrontEndStockResponseParser(stockTracker, url, page.getName(), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getFootpatrol()));
            return createDefault(url + "/quickview/stock", page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getFootpatrol(), notificationsFormatConfig), new HttpRequestHelper(), meshFrontEndStockResponseParser);
        } else if(site.equals("footpatrolapp")){
            return createMeshAppMonitor(page.getSku(), "footpatrolgb", page.getDelay(), siteNotificationsConfig, notificationsFormatConfig);
        } else if(site.equals("sizeapp")){
            return createMeshAppMonitor(page.getSku(), "size", page.getDelay(), siteNotificationsConfig, notificationsFormatConfig);
        } else if(site.equals("jdsportsapp")){
            return createMeshAppMonitor(page.getSku(), "jdsportsuk", page.getDelay(), siteNotificationsConfig, notificationsFormatConfig);
        } else if(site.equals("soto")){
            StockTracker stockTracker = new StockTracker(new HashMap<>(), -1);
            KeywordSearchHelper keywordSearchHelper = new KeywordSearchHelper(page.getSku());
            SotoResponseParser sotoResponseParser = new SotoResponseParser(stockTracker, keywordSearchHelper, NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getSoto()));
            return createDefault(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getSoto(), notificationsFormatConfig), new HttpRequestHelper(), sotoResponseParser);
        } else if(site.equals("soleboxproduct")){
            KeywordSearchHelper keywordSearchHelper = new KeywordSearchHelper(page.getSku());
            SoleboxProductPageResponseParser soleboxProductPageResponseParser = new SoleboxProductPageResponseParser(keywordSearchHelper, NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getSolebox()));
            return createDefault(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getSolebox(), notificationsFormatConfig), new HttpRequestHelper(), soleboxProductPageResponseParser);
        } else if(site.equals("citygear")){
            KeywordSearchHelper keywordSearchHelper = new KeywordSearchHelper(page.getSku());
            StockTracker stockTracker = new StockTracker(new HashMap<>(),0);
            CitygearAbstractResponseParser citygearResponseParser = new CitygearAbstractResponseParser(stockTracker, keywordSearchHelper, NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getCitygear()));
            return createDefault(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getCitygear(), notificationsFormatConfig), new HttpRequestHelper(), citygearResponseParser);
        } else if(site.equals("citygearproduct")){
            StockTracker stockTracker = new StockTracker(new HashMap<>(),0);
            CitygearProductResponseParser citygearProductResponseParser = new CitygearProductResponseParser(url, stockTracker, NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getCitygear()));
            return createDefault(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getCitygear(), notificationsFormatConfig), new HttpRequestHelper(), citygearProductResponseParser);
        } else if(site.equals("ccs")){
            StockTracker stockTracker = new StockTracker(new HashMap<>(),0);
            KeywordSearchHelper keywordSearchHelper = new KeywordSearchHelper(page.getSku());
            CCSResponseParser ccsResponseParser = new CCSResponseParser(stockTracker, keywordSearchHelper);
            return new CCS(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getCcs(), notificationsFormatConfig), new HttpRequestHelper(), ccsResponseParser);
        } else if(site.equals("dsmproduct")){
            NotificationConfig notificationConfig = getShopifyConfig(url, siteNotificationsConfig);
            StockTracker stockTracker = new StockTracker(new HashMap<>(),0);
            DsmProductResponseParser dsmProductResponseParser = new DsmProductResponseParser(stockTracker, url, page.getLocale(), new ObjectMapper(), NotificationsConfigTransformer.transformNotifications(notificationConfig));
            return createDefault(url, page.getDelay(), new AttachmentCreater(notificationConfig, notificationsFormatConfig), new HttpRequestHelper(), dsmProductResponseParser);
        } else if(site.equals("offwhiteall")){
            OffWhiteAllResponseParser offWhiteAllResponseParser = new OffWhiteAllResponseParser(new StockTracker(new HashMap<>(), 10000), new KeywordSearchHelper(page.getSku()),  NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getOffwhite()));
            GetLinksFromPage getLinksFromPage = new GetLinksFromPage(page.getSku());
            return new OffWhiteAll(url, page.getDelay(),new AttachmentCreater(siteNotificationsConfig.getOffwhite(), notificationsFormatConfig),  new CloudflareRequestWrapper(apiKeys, new HttpRequestHelper(), HttpClients.createDefault()), offWhiteAllResponseParser);
        } else if(site.equals("jimmyjazz")){
            JimmyJazzResponseParser jimmyJazzResponseParser = new JimmyJazzResponseParser(new StockTracker(new HashMap<>(), 0), url,  NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getJimmyjazz()));
            return new JimmyJazz(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getJimmyjazz(), notificationsFormatConfig), new CloudflareRequestWrapper(apiKeys, new HttpRequestHelper(), HttpClients.createDefault()), jimmyJazzResponseParser);

        } else if(site.equals("supremekw")){
            SupremeAllProductResponseParser supremeProductParseResponse = new SupremeAllProductResponseParser(new StockTracker(new HashMap<>(), 0), new KeywordSearchHelper(page.getSku()), page.getLocale(), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getSupreme()));
            return createDefault(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getSupreme(), notificationsFormatConfig), new HttpRequestHelper(), supremeProductParseResponse);
        }
        else if(site.equals("demandwareatc")){
            NotificationConfig notificationConfig;
            if(url.contains("fye"))
                notificationConfig = siteNotificationsConfig.getFye();
            else
                notificationConfig = siteNotificationsConfig.getDemandware();

            DemandwareGetResponseParser shopifyResponseParser = new DemandwareGetResponseParser(new StockTracker(new HashMap<>(), 60000), NotificationsConfigTransformer.transformNotifications(notificationConfig));
            return new DemandwareGet(url, page.getSku(), page.getDelay(), new AttachmentCreater(notificationConfig, notificationsFormatConfig), new HttpRequestHelper(),  shopifyResponseParser);
        } else if(site.equals("walmart")) {
            WalmartResponseParser shopifyResponseParser = new WalmartResponseParser(new ObjectMapper(), new StockTracker(new HashMap<>(), 60000), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getWalmart()), page.getSku());
            return new Walmart(page.getSku(), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getWalmart(), notificationsFormatConfig), new HttpRequestHelper(), shopifyResponseParser);
        } else if(site.equals("walmartterra")) {
            WalmartTerraResponseParser shopifyResponseParser = new WalmartTerraResponseParser(new ObjectMapper(), new StockTracker(new HashMap<>(), 60000), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getWalmart()), page.getSku());
            return new WalmartTerra(page.getSku(), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getWalmart(), notificationsFormatConfig), new HttpRequestHelper(), shopifyResponseParser);
        } else if(site.equals("amazon")){
            AmazonResponseParser amazonResponseParser = new AmazonResponseParser(
                    url,
                    new StockTracker(new HashMap<>(), 60000),
                    NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getAmazon()));
            return createDefault(
                    url,
                    page.getDelay(),
                    new AttachmentCreater(siteNotificationsConfig.getAmazon(),notificationsFormatConfig),
                    new HttpRequestHelper(),
                    amazonResponseParser);

        } else if(site.equals("barnesandnoble")){
            BarnesAndNobleResponseParser barnesAndNobleResponseParser = new BarnesAndNobleResponseParser( new StockTracker(new HashMap<>(), 60000), page.getSku(), new KeywordSearchHelper(""), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getBarnesandnoble()));
            return new BarnesAndNoble(page.getSku(),
                    page.getDelay(),
                    new AttachmentCreater(siteNotificationsConfig.getBarnesandnoble(),notificationsFormatConfig),
                    new HttpRequestHelper(),
                    barnesAndNobleResponseParser);
        } else if(site.equals("daveyboystoys")){
            DaveyboystoysResponseParser daveyboystoysResponseParser = new DaveyboystoysResponseParser(
                    new StockTracker(new HashMap<>(), 60000),
                    new KeywordSearchHelper(page.getSku()),
                    NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getDaveyboystoys())
            );
            return createDefault(url,
                    page.getDelay(),
                    new AttachmentCreater(siteNotificationsConfig.getDaveyboystoys(),notificationsFormatConfig),
                    new HttpRequestHelper(),
                    daveyboystoysResponseParser);

        } else if(site.equals("disneyrestock")){
            DisneyRestockResponseParser disneyRestockResponseParser = new DisneyRestockResponseParser(
                    new StockTracker(new HashMap<>(), 60000),
                    url,
                    NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getDisney()));
            return new DisneyRestock(url,
                    page.getDelay(),
                    new AttachmentCreater(siteNotificationsConfig.getDisney(),notificationsFormatConfig),
                    new HttpRequestHelper(),
                    disneyRestockResponseParser);
        } else if(site.equals("disneysitemap")){
            DisneySitemapResponseParser disneySitemapResponseParser = new DisneySitemapResponseParser(
                    new StockTracker(new HashMap<>(), 60000),
                    url,
                    new KeywordSearchHelper(page.getSku()),
                    NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getDisney())
            );
            return new DisneySitemap(url,
                    page.getDelay(),
                    new AttachmentCreater(siteNotificationsConfig.getDisney(),notificationsFormatConfig),
                    new HttpRequestHelper(),
                    disneySitemapResponseParser);
        } else if(site.equals("funimation")){
            FunimationResponseParser funimationResponseParser = new FunimationResponseParser(
                    url,
                    new StockTracker(new HashMap<>(), 60000),
                    NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getFunimation())
            );
            return createDefault(url,
                    page.getDelay(),
                    new AttachmentCreater(siteNotificationsConfig.getFunimation(),notificationsFormatConfig),
                    new HttpRequestHelper(),
                    funimationResponseParser);
        } else if(site.equals("fyesearch")){
            FyeSearchResponseParser fyeSearchResponseParser = new FyeSearchResponseParser(
                    new StockTracker(new HashMap<>(), 60000),
                    url,
                    new KeywordSearchHelper(page.getSku()),
                    NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getFye())
            );
            return createDefault(url,
                    page.getDelay(),
                    new AttachmentCreater(siteNotificationsConfig.getFye(),notificationsFormatConfig),
                    new HttpRequestHelper(),
                    fyeSearchResponseParser);
        } else if(site.equals("gamestop")){
            GamestopResponseParser gamestopResponseParser = new GamestopResponseParser(
                    url,
                    new StockTracker(new HashMap<>(), 60000),
                    NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getGamestop()), new HttpRequestHelper());
            return createDefault(
                    url,
                    page.getDelay(),
                    new AttachmentCreater(siteNotificationsConfig.getGamestop(),notificationsFormatConfig),
                    new HttpRequestHelper(),
                    gamestopResponseParser
            );
        } else if(site.equals("gemini")){
            GeminiResponseParser geminiResponseParser = new GeminiResponseParser(
                    new StockTracker(new HashMap<>(), 60000),
                    url,
                    new KeywordSearchHelper(page.getSku()),
                    NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getGemini())
            );
            return createDefault(url,
                    page.getDelay(),
                    new AttachmentCreater(siteNotificationsConfig.getGemini(),notificationsFormatConfig),
                    new HttpRequestHelper(),
                    geminiResponseParser
                    );
        } else if(site.equals("geminirestock")){
            GeminiRestockResponseParser geminiRestockResponseParser = new GeminiRestockResponseParser(new StockTracker(new HashMap<>(), 60000),
                    url,
                    NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getGemini()));
            return createDefault(url,
                    page.getDelay(),
                    new AttachmentCreater(siteNotificationsConfig.getGemini(),notificationsFormatConfig),
                    new HttpRequestHelper(),
                    geminiRestockResponseParser);
        } else if(site.equals("popcultcha")){
            PopCultchaResponseParser popCultchaResponseParser = new PopCultchaResponseParser(   new StockTracker(new HashMap<>(), 60000),
                    new KeywordSearchHelper(page.getSku()),
                    NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getPopcultcha()));
            return createDefault(url,
                    page.getDelay(),
                    new AttachmentCreater(siteNotificationsConfig.getPopcultcha(), notificationsFormatConfig),
                    new HttpRequestHelper(),
                    popCultchaResponseParser);
        } else if(site.equals("walgreens")) {
            WalgreensResponseParser walgreensResponseParser = new WalgreensResponseParser(
                    new StockTracker(new HashMap<>(), 60000),
                    url,
                    NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getWalgreens()));
            return createDefault(url,
                    page.getDelay(),
                    new AttachmentCreater(siteNotificationsConfig.getWalgreens(),notificationsFormatConfig),
                    new HttpRequestHelper(),
                    walgreensResponseParser);
        } else if(site.equals("walgreenssearch")) {
            WalgreensSearchResponseParser walgreensSearchResponseParser = new WalgreensSearchResponseParser(
                new StockTracker(new HashMap<>(), 60000),
                new KeywordSearchHelper(page.getSku()),
                NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getWalgreens())
            );
            return createDefault(url,
                    page.getDelay(),
                    new AttachmentCreater(siteNotificationsConfig.getWalgreens(),notificationsFormatConfig),
                    new HttpRequestHelper(),
                    walgreensSearchResponseParser);
        } else if(site.equals("instagram")) {
            InstagramResponseParser instagramResponseParser = new InstagramResponseParser(
                    new ObjectMapper(),
                    new KeywordSearchHelper(defaultKw),
                    new StockTracker(new HashMap<>(), 60000),
                    NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getInstagram()),
                    url
            );
            return createDefault(url,
                    page.getDelay(),
                    new AttachmentCreater(siteNotificationsConfig.getInstagram(), notificationsFormatConfig),
                    new HttpRequestHelper(),
                    instagramResponseParser);
        } else if(site.equals("lvr")){
            LvrResponseParser lvrResponseParser = new LvrResponseParser(new StockTracker(new HashMap<>(), -1), url,  NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getLvr()));
            return new LVR(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getLvr(), notificationsFormatConfig), new HttpRequestHelper(), lvrResponseParser);
        } else if(site.equals("meshsearch")) {
            MeshSearchResponseParser meshSearchResponseParser = new MeshSearchResponseParser(UrlHelper.deriveBaseUrl(page.getUrls().get(0)), new StockTracker(new HashMap<>(), 60000),  NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getFootpatrol()));
            return createDefault(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getFootpatrol(), notificationsFormatConfig), new HttpRequestHelper(), meshSearchResponseParser);
        } else if(site.equals("footsites")){
            FootsitesResponseParser footsitesResponseParser = new FootsitesResponseParser(new StockTracker(new HashMap<>(), 60000), url, page.getName(), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getFootsites()));
            return new Footsites(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getFootsites(), notificationsFormatConfig), new HttpRequestHelper(), footsitesResponseParser);
        } else if(site.equals("onygositemap")){
            OnygoSitemapResponseParser onygoSitemapResponseParser = new OnygoSitemapResponseParser(new StockTracker(new HashMap<>(), -1), new KeywordSearchHelper(page.getSku()), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getOnygo()));
            return createDefault(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getOnygo(), notificationsFormatConfig), new HttpRequestHelper(), onygoSitemapResponseParser);
        } else if(site.equals("shoepalace")){
            ShoepalaceResponseParser shoepalaceResponseParser = new ShoepalaceResponseParser(new StockTracker(new HashMap<>(), 6000), url, NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getShoepalace()));
            return createDefault(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getShoepalace(), notificationsFormatConfig), new HttpRequestHelper(), shoepalaceResponseParser);
        } else if(site.equals("supremepage")){
            SupremePageResponseParser supremePageResponseParser = new SupremePageResponseParser(new StockTracker(new HashMap<>(), 0), new KeywordSearchHelper(defaultKw), page.getLocale(), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getSupreme()));
            return createDefault(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getSupreme(), notificationsFormatConfig), new HttpRequestHelper(), supremePageResponseParser);
        } else if(site.equals("snsproduct")){
            SNSProductResponseParser snsProductResponseParser = new SNSProductResponseParser(new StockTracker(new HashMap<>(), 120000), url, NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getSns()), new ObjectMapper());
            return Http2DefaultMonitor.builder()
                    .url(url)
                    .delay(page.getDelay())
                    .attachmentCreater(new AttachmentCreater(siteNotificationsConfig.getSns(), notificationsFormatConfig))
                    .httpRequestHelper(
                            new CloudflareRequestWrapper(apiKeys, new Http2RequestHelper(new GoogleChromeHeaderDecorator(GoogleChromeUserAgentGenerator.generateUserAgent()), new ConnectionPool(1, 1, TimeUnit.MINUTES)), HttpClients.createDefault())
                    )
                    .abstractResponseParser(snsProductResponseParser)
                    .addUUID(true)
                    .build();
            //return createDefault(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getSns(), notificationsFormatConfig), new CloudflareRequestWrapper(apiKeys, new HttpRequestHelper(), HttpClients.createDefault()), snsProductResponseParser);
        } else if(site.equals("yeezysupply")){
            NotificationConfig notificationConfig = getShopifyConfig(url, siteNotificationsConfig);
            ShopifyAbstractResponseParser shopifyAbstractResponseParser =
                    new ShopifyAbstractResponseParser(new StockTracker(new HashMap<>(), 0), url, NotificationsConfigTransformer.transformNotifications(notificationConfig));
            YsResponseParser ysResponseParser = new YsResponseParser(url, NotificationsConfigTransformer.transformNotifications(notificationConfig), shopifyAbstractResponseParser);
            return createDefault(url, page.getDelay(), new AttachmentCreater(notificationConfig, notificationsFormatConfig), new HttpRequestHelper(), ysResponseParser);
        } else if(site.equals("instagramstory")){
            InstagramStoryResponseParser instagramStoryResponseParser =
                    new InstagramStoryResponseParser(new ObjectMapper(), new StockTracker(new HashMap<>(), 0), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getInstagram()), page.getName());
             return new InstagramStory(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getInstagram(), notificationsFormatConfig), new HttpRequestHelper(),instagramStoryResponseParser, page.getSku());
        } else if(site.contains("frontend")){
            NotificationConfig notificationConfig = getShopifyConfig(url, siteNotificationsConfig);
            ShopifyAbstractResponseParser shopifyAbstractResponseParser = new ShopifyAbstractResponseParser(new StockTracker(new HashMap<>(), 0), url, NotificationsConfigTransformer.transformNotifications(notificationConfig));
            ShopifyFrontendHelper shopifyFrontendHelper = new ShopifyFrontendHelper(url);
            return new ShopifyFrontend(url, page.getDelay(), new AttachmentCreater(notificationConfig, notificationsFormatConfig), new HttpRequestHelper(), shopifyAbstractResponseParser, shopifyFrontendHelper);
        } else if(site.equals("offspring")){
            return createDefault(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getOffspring(), notificationsFormatConfig), new HttpRequestHelper(), new Offspring(new StockTracker(new HashMap<>(), 0), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getOffspring()), new ObjectMapper()));
        } else if(site.equals("caliroots")){
            return createDefault(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getOffspring(), notificationsFormatConfig), new CloudflareRequestWrapper(apiKeys, new HttpRequestHelper(), HttpClients.createDefault()), new Caliroots(new StockTracker(new HashMap<>(), 0), url, NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getOffspring())));
        } else if(site.equals("7hills")) {
            return createDefault(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getOffspring(), notificationsFormatConfig), new HttpRequestHelper(), new SevenHillsResponseParser(new StockTracker(new HashMap<>(), 0), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getOffspring())));
        } else if(site.equals("panagora")) {
            String jsonUrl = String.format("%s/product/view/%s?view_override=json&skip_layout=1", url, page.getSku().trim());

            PanagoraProductResponseParser panagoraProductResponseParser = new PanagoraProductResponseParser(new StockTracker(new HashMap<>(), 0), UrlHelper.deriveBaseUrl(url), new ObjectMapper(), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getSns()));
            return Http2DefaultMonitor.builder()
                    .url(jsonUrl)
                    .delay(page.getDelay())
                    .attachmentCreater(new AttachmentCreater(siteNotificationsConfig.getSns(), notificationsFormatConfig))
                    .httpRequestHelper(
                            new CloudflareRequestWrapper(apiKeys, new HttpRequestHelper(), HttpClients.createDefault())
                    )
                    .abstractResponseParser(panagoraProductResponseParser)
                    .addUUID(false)
                    .build();
        } else if (site.equals("svd-app")) {
            String svdUrl = String.format("https://ms-api.sivasdescalzo.com/api/itemDetail/%s", page.getSku().trim());
            SvdAppResponseParser svdAppResponseParser =
                    new SvdAppResponseParser(new StockTracker(new HashMap<>(), 0), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getSvd()), new ObjectMapper());
            return createDefault(svdUrl, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getSvd(), notificationsFormatConfig), new HttpRequestHelper(), svdAppResponseParser);
        } else if (site.equals("nike-desktop")){
            String nikeUrl = String.format("https://api.nike.com/product_feed/threads/v2?filter=exclusiveAccess(true,false)&filter=channelId(d9a5bc42-4b9c-4976-858a-f159cf99c647)&filter=marketplace(US)&filter=language(en)&filter=publishedContent.subType(soldier,officer)&filter=productInfo.merchProduct.styleColor(%s)", page.getSku());
            NikeDesktopResponseParser nikeDesktopResponseParser =
                    new NikeDesktopResponseParser(new ObjectMapper(), new StockTracker(new HashMap<>(), 0), new ParseV2Helper(),  NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getNikeDesktop()));
            return createDefault(nikeUrl, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getNikeDesktop(), notificationsFormatConfig), new HttpRequestHelper(), nikeDesktopResponseParser);
        } else if (site.equals("grosbasket")) {
            GrosBasketResponseParser grosBasketResponseParser =
                    new GrosBasketResponseParser(new StockTracker(new HashMap<>(), 10000), url, NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getOffspring()));
            return createDefault(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getOffspring(), notificationsFormatConfig), new HttpRequestHelper(), grosBasketResponseParser);
        } else if (site.equals("sklepkoszykarza")) {
            SportowysklepResponseParser grosBasketResponseParser =
                    new SportowysklepResponseParser(new StockTracker(new HashMap<>(), 10000), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getOffspring()));
            return createDefault(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getOffspring(), notificationsFormatConfig), new HttpRequestHelper(), grosBasketResponseParser);
        } else if (site.equals("suppastore")) {
            SuppaStoreResponseParser grosBasketResponseParser =
                    new SuppaStoreResponseParser(new StockTracker(new HashMap<>(), 10000), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getOffspring()));
            return createDefault(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getOffspring(), notificationsFormatConfig), new HttpRequestHelper(), grosBasketResponseParser);
        } else if (site.equals("sneakerbarber")) {
            SneakerbarberResponseParser sneakerbarberResponseParser =
                    new SneakerbarberResponseParser(new StockTracker(new HashMap<>(), 10000), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getOffspring()));
            return createDefault(url, page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getOffspring(), notificationsFormatConfig), new HttpRequestHelper(), sneakerbarberResponseParser);
        }

        return null;
    }
    
    private static AbstractMonitor createDefault(String url, int delay, AttachmentCreater attachmentCreater, AbstractHttpRequestHelper abstractHttpRequestHelper, AbstractResponseParser abstractResponseParser) {
        return new DefaultMonitor(
                url,
                delay,
                attachmentCreater,
                abstractHttpRequestHelper,
                abstractResponseParser,
                "");
    }

    private static NotificationConfig getShopifyConfig(String url, SiteNotificationsConfig siteNotificationsConfig){
        NotificationConfig notificationConfig = siteNotificationsConfig.getShopify();
        if(url.contains("kith")){
            notificationConfig = siteNotificationsConfig.getKith();
        } else if(url.contains("undefeated")){
            notificationConfig = siteNotificationsConfig.getUndefeated();
        } else if(url.contains("cncpts")){
            notificationConfig = siteNotificationsConfig.getConcepts();
        } else if(url.contains("bdga")){
            notificationConfig = siteNotificationsConfig.getBodega();
        } else if(url.contains("yeezysupply")){
            notificationConfig = siteNotificationsConfig.getYeezysupply();
        } else if(url.contains("travis")){
            notificationConfig = siteNotificationsConfig.getTravis();
        } else if(url.contains("china")){
            notificationConfig = siteNotificationsConfig.getChinatown();
        }

        return notificationConfig;
    }


    private static AbstractMonitor createMeshAppMonitor(String url, String site, int delay, SiteNotificationsConfig siteNotificationsConfig, NotificationsFormatConfig notificationsFormatConfig){
        StockTracker stockTracker = new StockTracker(new HashMap<>(), 30000);
        String base = "";
        if (url.contains("size")){
            base = "https://www.size.co.uk";
        } else if(url.contains("footpatrol")){
            base = "https://www.footpatrol.com";
        } else {
            base  = "https://www.jdsports.co.uk";
        }
        MeshApp meshApp =
                new MeshApp(
                        stockTracker,
                        NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getFootpatrol()),
                        base
                );
        return new MeshAppMonitor(url, site, delay, new AttachmentCreater(siteNotificationsConfig.getFootpatrol(), notificationsFormatConfig), new HttpRequestHelper(), new Hawk(), meshApp);
    }
    
    
    private static Http2RequestHelper getHttp2RequestHelper() {
        long randNum = Math.round(Math.random());

        return new Http2RequestHelper(
                ThreadLocalRandom.current().nextInt(0, 2) == 1 ?
                        new FirefoxHeaderDecorator(FirefoxUserAgentGenerator.generateUserAgent()):
                        new GoogleChromeHeaderDecorator(GoogleChromeUserAgentGenerator.generateUserAgent()),
                new ConnectionPool()
        );
    }
}
