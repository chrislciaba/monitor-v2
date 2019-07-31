package com.restocktime.monitor.transformer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restocktime.monitor.config.Config;
import com.restocktime.monitor.config.model.GlobalSettings;
import com.restocktime.monitor.config.model.NotificationsFormatConfig;
import com.restocktime.monitor.config.model.Page;
import com.restocktime.monitor.config.model.notifications.NotificationConfig;
import com.restocktime.monitor.helper.botstarters.QuicktaskConfig;
import com.restocktime.monitor.helper.clientbuilder.ClientBuilder;
import com.restocktime.monitor.helper.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.helper.debug.DiscordLog;
import com.restocktime.monitor.helper.httprequests.AbstractHttpRequestHelper;
import com.restocktime.monitor.helper.httprequests.CloudflareRequestHelper;
import com.restocktime.monitor.helper.httprequests.HttpRequestHelper;
import com.restocktime.monitor.helper.keywords.KeywordSearchHelper;
import com.restocktime.monitor.helper.keywords.helper.KeywordFormatHelper;
import com.restocktime.monitor.helper.taskstatus.TaskStatus;
import com.restocktime.monitor.monitors.ingest.AbstractMonitor;
import com.restocktime.monitor.monitors.ingest.DefaultMonitor;
import com.restocktime.monitor.monitors.ingest.shopify.*;
import com.restocktime.monitor.monitors.parse.AbstractResponseParser;
import com.restocktime.monitor.monitors.parse.acronym.AcronymParser;
import com.restocktime.monitor.monitors.parse.adidas.parse.AdidasResponseParser;
import com.restocktime.monitor.monitors.parse.amazon.AmazonResponseParser;
import com.restocktime.monitor.monitors.parse.antonioli.AntonioliResponseParser;
import com.restocktime.monitor.monitors.ingest.backdoor.BackDoor;
import com.restocktime.monitor.monitors.parse.backdoor.parse.BackdoorSearchAbstractResponseParser;
import com.restocktime.monitor.monitors.ingest.barnesandnoble.BarnesAndNoble;
import com.restocktime.monitor.monitors.parse.barnesandnoble.parse.BarnesAndNobleResponseParser;
import com.restocktime.monitor.monitors.parse.bestbuy.BestBuyParseProductAbstractResponse;
import com.restocktime.monitor.monitors.parse.bhvideo.BhVideoParseAbstractResponse;
import com.restocktime.monitor.monitors.ingest.bstn.BSTN;
import com.restocktime.monitor.monitors.parse.bstn.parse.BSTNParseProductAbstractResponse;
import com.restocktime.monitor.monitors.parse.bstn.parse.BSTNParseSearchAbstractResponse;
import com.restocktime.monitor.monitors.parse.bstn.parse.BstnParsePageResponse;
import com.restocktime.monitor.monitors.parse.caliroots.Caliroots;
import com.restocktime.monitor.monitors.parse.citygear.CitygearAbstractResponseParser;
import com.restocktime.monitor.monitors.parse.citygear.CitygearProductResponseParser;
import com.restocktime.monitor.monitors.parse.complexcon.parse.ComplexconResponseParser;
import com.restocktime.monitor.monitors.ingest.ccs.CCS;
import com.restocktime.monitor.monitors.parse.css.parse.CCSResponseParser;
import com.restocktime.monitor.monitors.parse.daveyboystoys.DaveyboystoysResponseParser;
import com.restocktime.monitor.monitors.ingest.disney.DisneyRestock;
import com.restocktime.monitor.monitors.ingest.disney.DisneySitemap;
import com.restocktime.monitor.monitors.parse.disney.parse.DisneyRestockResponseParser;
import com.restocktime.monitor.monitors.parse.disney.parse.DisneySitemapResponseParser;
import com.restocktime.monitor.monitors.parse.dsm.parse.DsmProductResponseParser;
import com.restocktime.monitor.monitors.parse.dsm.parse.ParseDSMAbstractResponse;
import com.restocktime.monitor.monitors.ingest.footdistrict.FootDistrict;
import com.restocktime.monitor.monitors.parse.footdistrict.helper.BotBypass;
import com.restocktime.monitor.monitors.parse.footdistrict.parse.FootdistrictParseProductAbstractResponse;
import com.restocktime.monitor.monitors.parse.footdistrict.parse.FootdistrictParseSearchAbstractResponse;
import com.restocktime.monitor.monitors.parse.footdistrict.parse.FootdistrictParseSitemapResponse;
import com.restocktime.monitor.monitors.parse.footpatrol.parse.FootpatrolAppResponseParser;
import com.restocktime.monitor.monitors.parse.footpatrol.parse.FootpatrolProductPageResponseParser;
import com.restocktime.monitor.monitors.parse.footpatrol.parse.FootpatrolResponseParser;
import com.restocktime.monitor.monitors.ingest.footsites.Footsites;
import com.restocktime.monitor.monitors.parse.footsites.parse.FootsitesResponseParser;
import com.restocktime.monitor.monitors.parse.frenzy.parse.FrenzyResponseParser;
import com.restocktime.monitor.monitors.parse.funimation.FunimationResponseParser;
import com.restocktime.monitor.monitors.parse.fye.FyeSearchResponseParser;
import com.restocktime.monitor.monitors.parse.gamestop.parse.GamestopResponseParser;
import com.restocktime.monitor.monitors.parse.gemini.parse.GeminiResponseParser;
import com.restocktime.monitor.monitors.parse.gemini.parse.GeminiRestockResponseParser;
import com.restocktime.monitor.monitors.ingest.instagram.InstagramStory;
import com.restocktime.monitor.monitors.parse.instagram.parse.InstagramResponseParser;
import com.restocktime.monitor.monitors.parse.instagram.parse.InstagramStoryResponseParser;
import com.restocktime.monitor.monitors.ingest.jimmyjazz.JimmyJazz;
import com.restocktime.monitor.monitors.parse.jimmyjazz.parse.JimmyJazzResponseParser;
import com.restocktime.monitor.monitors.ingest.lvr.LVR;
import com.restocktime.monitor.monitors.parse.lvr.parse.LvrResponseParser;
import com.restocktime.monitor.monitors.parse.naked.ParseNakedAbstractResponse;
import com.restocktime.monitor.monitors.parse.nittygritty.NittyGrittyAbstractResponseParser;
import com.restocktime.monitor.monitors.parse.offspring.Offspring;
import com.restocktime.monitor.monitors.parse.offwhite.OffWhite;
import com.restocktime.monitor.monitors.parse.offwhite.OffWhiteAll;
import com.restocktime.monitor.monitors.parse.offwhite.OffWhiteAtc;
import com.restocktime.monitor.monitors.parse.offwhite.OffWhitePage;
import com.restocktime.monitor.monitors.parse.offwhite.helper.GetLinksFromPage;
import com.restocktime.monitor.monitors.parse.offwhite.parse.*;
import com.restocktime.monitor.monitors.ingest.oneblockdown.OneBlockDown;
import com.restocktime.monitor.monitors.parse.oneblockdown.parse.OneBlockDownProductIndexParser;
import com.restocktime.monitor.monitors.parse.oneblockdown.parse.OneBlockDownProductPageResponseParser;
import com.restocktime.monitor.monitors.parse.oneblockdown.parse.OneBlockDownResponseParser;
import com.restocktime.monitor.monitors.parse.onygo.OnygoSitemapResponseParser;
import com.restocktime.monitor.monitors.parse.patta.PattaAbstractResponseParser;
import com.restocktime.monitor.monitors.parse.patta.PattaProductResponseParser;
import com.restocktime.monitor.monitors.parse.popcultcha.parse.PopCultchaResponseParser;
import com.restocktime.monitor.monitors.ingest.porter.Porter;
import com.restocktime.monitor.monitors.parse.porter.helper.PorterHelper;
import com.restocktime.monitor.monitors.parse.porter.parse.ApiAbstractResponseParser;
import com.restocktime.monitor.monitors.parse.porter.parse.AtcResponseParser;
import com.restocktime.monitor.monitors.parse.rimowa.RimowaResponseParser;
import com.restocktime.monitor.monitors.parse.shoepalace.ShoepalaceResponseParser;
import com.restocktime.monitor.monitors.parse.shopify.helper.ShopifyFrontendHelper;
import com.restocktime.monitor.monitors.parse.shopify.helper.linkchecker.LinkCheckStarter;
import com.restocktime.monitor.helper.stocktracker.StockTracker;
import com.restocktime.monitor.helper.url.UrlHelper;
import com.restocktime.monitor.config.model.notifications.SiteNotificationsConfig;
import com.restocktime.monitor.monitors.ingest.demandware.DemandwareGet;
import com.restocktime.monitor.monitors.parse.demandware.parse.DemandwareGetResponseParser;
import com.restocktime.monitor.monitors.parse.shopify.parse.*;
import com.restocktime.monitor.monitors.ingest.snkrs.NikeScratch;
import com.restocktime.monitor.monitors.parse.snkrs.parse.HuntResponseParser;
import com.restocktime.monitor.monitors.parse.snkrs.parse.ProductFeedV2ResponseParser;
import com.restocktime.monitor.monitors.parse.sns.SNSProductResponseParser;
import com.restocktime.monitor.monitors.parse.sns.SnsResponseParser;
import com.restocktime.monitor.monitors.parse.solebox.SoleboxProductPageResponseParser;
import com.restocktime.monitor.monitors.parse.solebox.SoleboxResponseParser;
import com.restocktime.monitor.monitors.parse.soto.SotoResponseParser;
import com.restocktime.monitor.monitors.ingest.ssense.Ssense;
import com.restocktime.monitor.monitors.parse.ssense.parse.PageResponseParser;
import com.restocktime.monitor.monitors.parse.ssense.parse.SearchResponseParser;
import com.restocktime.monitor.monitors.parse.supreme.parse.SupremeAllProductResponseParser;
import com.restocktime.monitor.monitors.parse.supreme.parse.SupremePageResponseParser;
import com.restocktime.monitor.monitors.parse.supreme.parse.SupremeProductParseAbstractResponse;
import com.restocktime.monitor.monitors.ingest.svd.SVD;
import com.restocktime.monitor.monitors.parse.svd.parse.SvdProductResponseParser;
import com.restocktime.monitor.monitors.parse.svd.parse.SvdSearchAbstractResponseParser;
import com.restocktime.monitor.monitors.parse.target.Target;
import com.restocktime.monitor.monitors.parse.target.parse.TargetAbstractResponseParser;
import com.restocktime.monitor.monitors.parse.thenextdoor.TheNextDoorResponseParser;
import com.restocktime.monitor.monitors.ingest.titolo.Titolo;
import com.restocktime.monitor.monitors.parse.titolo.parse.TitoloProductResponseParser;
import com.restocktime.monitor.monitors.parse.titolo.parse.TitoloSearchResponseParser;
import com.restocktime.monitor.monitors.ingest.twitter.Twitter;
import com.restocktime.monitor.monitors.parse.twitter.parse.TwitterAbstractResponseParser;
import com.restocktime.monitor.monitors.parse.walgreens.WalgreensResponseParser;
import com.restocktime.monitor.monitors.parse.walgreens.WalgreensSearchResponseParser;
import com.restocktime.monitor.monitors.parse.walmart.Walmart;
import com.restocktime.monitor.monitors.parse.walmart.WalmartTerra;
import com.restocktime.monitor.monitors.parse.walmart.parse.WalmartResponseParser;
import com.restocktime.monitor.monitors.parse.walmart.parse.WalmartTerraResponseParser;
import com.restocktime.monitor.monitors.parse.yme.YmeResponseParser;
import com.restocktime.monitor.monitors.parse.ys.parse.YsResponseParser;
import com.restocktime.monitor.notifications.attachments.AttachmentCreater;
import com.restocktime.monitor.notifications.attachments.transformer.NotificationsConfigTransformer;
import com.restocktime.monitor.proxymanager.ProxyManager;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

public class ConfigDataTransformer {

    public static AbstractMonitor transformMonitor(Page page, GlobalSettings globalSettings, NotificationsFormatConfig notificationsFormatConfig, SiteNotificationsConfig siteNotificationsConfig, ProxyManager proxyManager){
        String[] apiKeys = globalSettings.getApiKeys().split(",");
        String defaultKw = globalSettings.getDefaultShopifyKw();

        String site = page.getSite();

        if(site.equals("solebox")){
            SoleboxResponseParser soleboxResponseParser = new SoleboxResponseParser(new StockTracker(new HashMap<>(), 0), page.getUrls().get(0),  NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getSolebox()));
            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getSolebox(), notificationsFormatConfig), new HttpRequestHelper(), soleboxResponseParser);
        } else if(site.equals("bstn")){
            BSTNParseSearchAbstractResponse bstnParseSearchResponse = new BSTNParseSearchAbstractResponse(new StockTracker(new HashMap<>(), 0), page.getUrls().get(0), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getBstn()));
            BSTNParseProductAbstractResponse bstnParseProductResponse = new BSTNParseProductAbstractResponse(new StockTracker(new HashMap<>(), 0), page.getUrls().get(0), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getBstn()));
            BstnParsePageResponse bstnParsePageResponse = new BstnParsePageResponse(new StockTracker(new HashMap<>(), 0), new KeywordSearchHelper(page.getSku()), page.getUrls().get(0), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getBstn()));
            return new BSTN(page.getUrls().get(0), page.getSku(), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getBstn(), notificationsFormatConfig), new CloudflareRequestHelper(apiKeys), bstnParseProductResponse, bstnParseSearchResponse, bstnParsePageResponse);
        } else if(site.equals("naked")){

            AbstractResponseParser parseNakedResponse = new ParseNakedAbstractResponse(new StockTracker(new HashMap<>(), 500000), page.getUrls().get(0), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getNaked()));
            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getNaked(), notificationsFormatConfig), new HttpRequestHelper(), parseNakedResponse);
        } else if(site.equals("shopify")){
            NotificationConfig notificationConfig = getShopifyConfig(page.getUrls().get(0), siteNotificationsConfig);
            QuicktaskConfig quicktaskConfig = null;//new QuicktaskConfig(page.getQuicktask(), quicktasks);
            ShopifyAbstractResponseParser shopifyResponseParser = new ShopifyAbstractResponseParser(new StockTracker(new HashMap<>(), 0), page.getUrls().get(0), NotificationsConfigTransformer.transformNotifications(notificationConfig));
            return new Shopify(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(notificationConfig, notificationsFormatConfig), new HttpRequestHelper() , shopifyResponseParser, quicktaskConfig);
        } else if(site.equals("shopifyallproducts")){
            NotificationConfig notificationConfig = getShopifyConfig(page.getUrls().get(0), siteNotificationsConfig);

            ShopifyProductListingsResponseParser shopifyProductListingsResponseParser =
                    new ShopifyProductListingsResponseParser(new StockTracker(new HashMap<>(), 0), new KeywordSearchHelper(KeywordFormatHelper.getKeywordString(defaultKw, page.getSku())), page.getUrls().get(0), NotificationsConfigTransformer.transformNotifications(notificationConfig));
            return new ShopifyProductListings(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(notificationConfig, notificationsFormatConfig), new HttpRequestHelper(), shopifyProductListingsResponseParser);
        } else if(site.equals("shopifyproducts")){
            NotificationConfig notificationConfig = getShopifyConfig(page.getUrls().get(0), siteNotificationsConfig);

            ShopifyProductsResponseParser shopifyProductsResponseParser =
                    new ShopifyProductsResponseParser(new StockTracker(new HashMap<>(), 0), new KeywordSearchHelper(KeywordFormatHelper.getKeywordString(defaultKw, page.getSku())), page.getUrls().get(0), NotificationsConfigTransformer.transformNotifications(notificationConfig));
            return new ShopifyProducts(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(notificationConfig, notificationsFormatConfig), new HttpRequestHelper(), shopifyProductsResponseParser);
        } else if(site.equals("shopifyatom")){
            Config c = new Config();
            NotificationConfig notificationConfig = getShopifyConfig(page.getUrls().get(0), siteNotificationsConfig);

            List<String> proxies = proxyManager.getProxies("shopify");
            ClientBuilder clientBuilder = new ClientBuilder();
            List<BasicRequestClient> clients = clientBuilder.buildClients(UrlHelper.deriveBaseUrl(page.getUrls().get(0)), proxies, page.getSite());
            LinkCheckStarter linkCheckStarter = new LinkCheckStarter(clients);
            ShopifyAtomResponseParser shopifyAtomResponseParser =
                    new ShopifyAtomResponseParser(
                            page.getUrls().get(0),
                            new StockTracker(new HashMap<>(), 0),
                            new KeywordSearchHelper(KeywordFormatHelper.getKeywordString(defaultKw, page.getSku())),
                            false,
                            UrlHelper.getHost(page.getUrls().get(0)),
                            linkCheckStarter,
                            true,
                            NotificationsConfigTransformer.transformNotifications(notificationConfig));
            return new ShopifyAtom(UrlHelper.deriveBaseUrl(page.getUrls().get(0)) + "/collections/all.atom", page.getDelay(), new AttachmentCreater(notificationConfig, notificationsFormatConfig), new HttpRequestHelper(), shopifyAtomResponseParser);
        }  else if(site.equals("shopifykw")){
            NotificationConfig notificationConfig = getShopifyConfig(page.getUrls().get(0), siteNotificationsConfig);

            List<String> proxies = proxyManager.getProxies("shopify");
            ClientBuilder clientBuilder = new ClientBuilder();
            List<BasicRequestClient> clients = clientBuilder.buildClients(UrlHelper.deriveBaseUrl(page.getUrls().get(0)), proxies, page.getSite());
            LinkCheckStarter linkCheckStarter = new LinkCheckStarter(clients);

            ShopifyKwAbstractResponseParser shopifyKwResponseParser = new ShopifyKwAbstractResponseParser(
                    page.getUrls().get(0),
                    new StockTracker(new HashMap<>(), 0),
                    new KeywordSearchHelper(KeywordFormatHelper.getKeywordString(defaultKw, page.getSku())),
                    false,
                    UrlHelper.getHost(page.getUrls().get(0)),
                    linkCheckStarter,
                    true,
                    new AttachmentCreater(notificationConfig, notificationsFormatConfig),
                    NotificationsConfigTransformer.transformNotifications(notificationConfig));
            return new ShopifyKw(page.getUrls().get(0),
                    page.getDelay(),
                    new AttachmentCreater(notificationConfig, notificationsFormatConfig),
                    new HttpRequestHelper(),
                    shopifyKwResponseParser
            );
        } else if(site.equals("end")){
            return  null;/*

            return new EndClothing(page.getUrls().get(0), page.getDelay(), slackConfig.getEndclothing(), discordConfig.getEndclothing());*/
        } else if(site.equals("offwhite")){

            OffWhiteSearchAbstractResponseParser offWhiteSearchResponseParser = new OffWhiteSearchAbstractResponseParser(new StockTracker(new HashMap<>(), -1), UrlHelper.deriveBaseUrl(page.getUrls().get(0)), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getOffwhite()));
            OffWhiteProductAbstractResponseParser offWhiteProductResponseParser = new OffWhiteProductAbstractResponseParser(new StockTracker(new HashMap<>(), 10000), page.getUrls().get(0), page.getName(), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getOffwhite()));

            return new OffWhite(page.getUrls().get(0) + ".json", page.getLocale(), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getOffwhite(), notificationsFormatConfig), new CloudflareRequestHelper(apiKeys), offWhiteProductResponseParser, offWhiteSearchResponseParser);
        } else if(site.equals("oneblockdownatc")){
            OneBlockDownResponseParser oneBlockDownResponseParser = new OneBlockDownResponseParser(new StockTracker(new HashMap<>(), 10000), new ObjectMapper(), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getOneblockdown()));
           return new OneBlockDown(page.getUrls().get(0), page.getSku(), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getOneblockdown(), notificationsFormatConfig), new CloudflareRequestHelper(apiKeys), oneBlockDownResponseParser);
        }  else if(site.equals("obd")){
            OneBlockDownProductIndexParser oneBlockDownProductPageResponseParser = new OneBlockDownProductIndexParser(new StockTracker(new HashMap<>(), 10000), new KeywordSearchHelper(defaultKw), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getOneblockdown()));
            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getOneblockdown(), notificationsFormatConfig), new CloudflareRequestHelper(apiKeys), oneBlockDownProductPageResponseParser);
           // return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getOneblockdown(), notificationsFormatConfig), new CloudflareRequestHelper(apiKeys), oneBlockDownProductPageResponseParser);
        } else if(site.equals("obdproduct")){
            AbstractResponseParser obdProductParser = new OneBlockDownProductPageResponseParser(page.getUrls().get(0), new StockTracker(new HashMap<>(), 10000), new KeywordSearchHelper(defaultKw), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getOneblockdown()));
            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getOneblockdown(), notificationsFormatConfig), new CloudflareRequestHelper(apiKeys), obdProductParser);
            // return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getOneblockdown(), notificationsFormatConfig), new CloudflareRequestHelper(apiKeys), oneBlockDownProductPageResponseParser);
        } else if(site.equals("ssense")){
            PageResponseParser pageResponseParser = new PageResponseParser(new StockTracker(new HashMap<>(), 30000), page.getUrls().get(0),  NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getSsense()));
            SearchResponseParser searchResponseParser = new SearchResponseParser(page.getLocale(), page.getUrls().get(0), page.getName(), new StockTracker(new HashMap<>(), -1), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getSsense()));
            return new Ssense(page.getUrls().get(0),
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

            ProductFeedV2ResponseParser productFeedV2ResponseParser = new ProductFeedV2ResponseParser(new ObjectMapper(), new StockTracker(new HashMap<>(), -1), page.getLocale(), formatNames, page.getUrls().get(0));

            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(notificationConfig, notificationsFormatConfig), new HttpRequestHelper(), productFeedV2ResponseParser);
        } else if(site.equals("porter")){
            ApiAbstractResponseParser apiResponseParser = new ApiAbstractResponseParser(new StockTracker(new HashMap<>(), 500000), PorterHelper.getKey(page.getUrls().get(0)), page.getLocale(), page.getSku(), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getPorter()));
            AtcResponseParser atcResponseParser = new AtcResponseParser(new StockTracker(new HashMap<>(), 500000), PorterHelper.getKey(page.getUrls().get(0)), page.getLocale(), page.getSku(), page.getUrls().get(0), page.getName());
            return new Porter(
                    page.getUrls().get(0),
                    page.getLocale(),
                    page.getDelay(),
                    new AttachmentCreater(siteNotificationsConfig.getPorter(), notificationsFormatConfig),
                    new HttpRequestHelper(),
                    apiResponseParser,
                    atcResponseParser
            );
        } else if(site.equals("adidas")){
            AdidasResponseParser adidasResponseParser = new AdidasResponseParser(new StockTracker(new HashMap<>(), 0), page.getSku(), page.getUrls().get(0), page.getName(), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getAdidas()));

            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getAdidas(), notificationsFormatConfig), new HttpRequestHelper(), adidasResponseParser);
        } else if(site.equals("svd")){
            SvdProductResponseParser svdProductResponseParser = new SvdProductResponseParser(new StockTracker(new HashMap<>(), 0), page.getUrls().get(0), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getSvd()));
            SvdSearchAbstractResponseParser svdSearchResponseParser = new SvdSearchAbstractResponseParser(new StockTracker(new HashMap<>(), 0), page.getUrls().get(0), page.getName(), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getSvd()));
            return new SVD(page.getUrls().get(0), page.getSku(), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getSvd(), notificationsFormatConfig), new HttpRequestHelper(), svdProductResponseParser, svdSearchResponseParser);
        } else if(site.equals("titolo")){
            TitoloProductResponseParser titoloProductResponseParser = new TitoloProductResponseParser(page.getName(), new StockTracker(new HashMap<>(), 500*1000), page.getUrls().get(0), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getTitolo()));
            TitoloSearchResponseParser titoloSearchResponseParser = new TitoloSearchResponseParser(new StockTracker(new HashMap<>(), -1), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getTitolo()));
            return new Titolo(page.getUrls().get(0), page.getSku(), page.getName(), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getTitolo(), notificationsFormatConfig), new HttpRequestHelper(), titoloProductResponseParser, titoloSearchResponseParser);
        } else if(site.equals("yme")){
            YmeResponseParser ymeResponseParser = new YmeResponseParser(new StockTracker(new HashMap<>(), 0), page.getUrls().get(0), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getYme()));
            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getOneblockdown(), notificationsFormatConfig), new CloudflareRequestHelper(apiKeys), ymeResponseParser);
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
            SupremeProductParseAbstractResponse supremeProductParseResponse = new SupremeProductParseAbstractResponse(new StockTracker(new HashMap<>(), 0), page.getUrls().get(0), page.getName(), page.getLocale(), formats);
            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(notificationConfig, notificationsFormatConfig), new HttpRequestHelper(), supremeProductParseResponse);
        } else if(site.equals("DSM")){
            List<String> proxies = proxyManager.getProxies("shopify");
            ClientBuilder clientBuilder = new ClientBuilder();
            List<BasicRequestClient> clients = clientBuilder.buildClients(UrlHelper.deriveBaseUrl(page.getUrls().get(0)), proxies, page.getSite());
            LinkCheckStarter linkCheckStarter = new LinkCheckStarter(clients);

            ParseDSMAbstractResponse parseDSMResponse = new ParseDSMAbstractResponse(
                    new StockTracker(new HashMap<>(), -1),
                    page.getUrls().get(0),
                    page.getLocale(),
                    linkCheckStarter,
                    new KeywordSearchHelper(new KeywordSearchHelper(KeywordFormatHelper.getKeywordString(defaultKw, page.getSku()))),
                    NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getDsm()));
            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getDsm(), notificationsFormatConfig), new HttpRequestHelper(), parseDSMResponse);
        } else if(site.equals("toytokyo")){
            //return new ToyTokyo(page.getUrls().get(0), page.getDelay(), slackConfig.getToytokyo(), discordConfig.getToytokyo(), new AttachmentCreater(siteNotificationsConfig.getShopify(), notificationsFormatConfig), new HttpRequestHelper());
        } else if(site.equals("target")){
            StockTracker stockTracker = new StockTracker(new HashMap<>(), 360000);
            TargetAbstractResponseParser targetResponseParser = new TargetAbstractResponseParser(new ObjectMapper(), stockTracker, NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getTarget()), page.getSku());
            return new Target(page.getSku(), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getTarget(), notificationsFormatConfig), new HttpRequestHelper(), targetResponseParser);
        }/* else if(site.equals("hottopic")){
            Notifications notifications = new Notifications(discordConfig.getHottopic(), slackConfig.getHottopic());
            ParseHotTopicAbstractResponse parseHotTopicResponse = new ParseHotTopicAbstractResponse(new StockTracker(new HashMap<>(), 0), page.getUrls().get(0));
            return new HotTopic(page.getUrls().get(0), page.getDelay(), notifications, new AttachmentCreater(siteNotificationsConfig.getShopify(), notificationsFormatConfig), new HttpRequestHelper(), parseHotTopicResponse);
        }*/ else if(site.equals("bhvideo")){
            BhVideoParseAbstractResponse bhVideoParseResponse = new BhVideoParseAbstractResponse(new StockTracker(new HashMap<>(), 0), page.getUrls().get(0), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getTarget()));
            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getTarget(), notificationsFormatConfig), new HttpRequestHelper(), bhVideoParseResponse);
        } else if(site.equals("bestbuy")){
            BestBuyParseProductAbstractResponse bestBuyParseProductResponse = new BestBuyParseProductAbstractResponse(
                    new StockTracker(new HashMap<>(), 0),
                    page.getUrls().get(0),
                    NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getBestbuy())
            );
            return createDefault(
                    page.getUrls().get(0),
                    page.getDelay(),
                    new AttachmentCreater(siteNotificationsConfig.getBestbuy(), notificationsFormatConfig),
                    new HttpRequestHelper(),
                    bestBuyParseProductResponse
            );
        } else if(site.equals("scratcher")){

            HuntResponseParser huntResponseParser = new HuntResponseParser(new StockTracker(new HashMap<>(), -1), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getSnkrs()));
            return new NikeScratch(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getSnkrs(), notificationsFormatConfig), new HttpRequestHelper(), huntResponseParser);
        } else if(site.equals("acronym")){
            AcronymParser acronymParseResponse = new AcronymParser(new StockTracker(new HashMap<>(), -1),  new KeywordSearchHelper(page.getSku()), page.getUrls().get(0), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getAcronym()));
            return createDefault(page.getUrls().get(0),
                    page.getDelay(),
                    new AttachmentCreater(siteNotificationsConfig.getAcronym(), notificationsFormatConfig),
                    new HttpRequestHelper(),
                    acronymParseResponse
            );
        } else if(site.equals("sjs")){
           // return new SJS(page.getUrls().get(0), page.getDelay(), slackConfig.getAcronym(), discordConfig.getAcronym(), new AttachmentCreater(siteNotificationsConfig.getShopify(), notificationsFormatConfig), new HttpRequestHelper());
        } else if(site.equals("footdistrict")){
            AbstractResponseParser abstractResponseParser;
            if(page.getUrls().get(0).contains("sitemap")) {
                abstractResponseParser = new FootdistrictParseSitemapResponse(new StockTracker(new HashMap<>(), 0), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getFootdistrict()));
            } else {
                abstractResponseParser = new FootdistrictParseProductAbstractResponse(new StockTracker(new HashMap<>(), -1), page.getUrls().get(0), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getFootdistrict()));
            }

            BotBypass botBypass = new BotBypass();
            return new FootDistrict(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getFootdistrict(), notificationsFormatConfig), new HttpRequestHelper(), botBypass, abstractResponseParser);
        } else if(site.equals("footdistrictsearch")){
            try {
                AbstractResponseParser abstractResponseParser = new FootdistrictParseSearchAbstractResponse(new StockTracker(new HashMap<>(), -1), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getFootdistrict()));
                String url = "https://eu1-search.doofinder.com/5/search?hashid=5d2f6b5036133b0035d084057bbc695d&query_counter=6&page=1&rpp=30&transformer=basic&query="
                        + URLEncoder.encode(page.getSku(), StandardCharsets.UTF_8.toString());
                return createDefault(url,
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
            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getPatta(), notificationsFormatConfig), new CloudflareRequestHelper(apiKeys), pattaResponseParser);
        } else if(site.equals("pattaproduct")) {
            PattaProductResponseParser pattaProductResponseParser = new PattaProductResponseParser(page.getUrls().get(0), new StockTracker(new HashMap<>(), 0), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getPatta()));
            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getPatta(), notificationsFormatConfig), new CloudflareRequestHelper(apiKeys), pattaProductResponseParser);
        } else if(site.equals("thenextdoor")){
            TheNextDoorResponseParser theNextDoorResponseParser = new TheNextDoorResponseParser(new StockTracker(new HashMap<>(), 0), new KeywordSearchHelper(page.getSku()), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getThenextdoor()));
            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getThenextdoor(), notificationsFormatConfig), new HttpRequestHelper(), theNextDoorResponseParser);
        } else if(site.equals("nittygritty")){
            KeywordSearchHelper keywordSearchHelper = new KeywordSearchHelper(page.getSku());
            StockTracker stockTracker = new StockTracker(new HashMap<>(), -1);
            NittyGrittyAbstractResponseParser nittyGrittyResponseParser = new NittyGrittyAbstractResponseParser(stockTracker, keywordSearchHelper, NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getNittygritty()));
            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getNittygritty(), notificationsFormatConfig), new HttpRequestHelper(), nittyGrittyResponseParser);
        } else if(site.equals("backdoor")){
            StockTracker stockTracker = new StockTracker(new HashMap<>(), -1);
            BackdoorSearchAbstractResponseParser backdoorSearchResponseParser = new BackdoorSearchAbstractResponseParser(page.getSku(), page.getUrls().get(0), stockTracker, NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getBackdoor()));
            return new BackDoor(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getBackdoor(), notificationsFormatConfig), new HttpRequestHelper(), backdoorSearchResponseParser);
        } else if(site.equals("offwhiteatc")){
            StockTracker stockTracker = new StockTracker(new HashMap<>(), 30000);
            OffWhiteAtcResponseParser offWhiteAtcResponseParser = new OffWhiteAtcResponseParser(page.getUrls().get(0), stockTracker, new ObjectMapper(), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getOffwhite()));
            OffWhiteAtcIncrementResponseParser offWhiteAtcIncrementResponseParser = new OffWhiteAtcIncrementResponseParser(page.getName(),stockTracker, new ObjectMapper(), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getOffwhite()));
            return new OffWhiteAtc(page.getUrls().get(0), page.getSku(), page.getLocale(), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getOffwhite(), notificationsFormatConfig), new CloudflareRequestHelper(apiKeys), offWhiteAtcResponseParser);
        } else if(site.equals("frenzy")){
            StockTracker stockTracker = new StockTracker(new HashMap<>(), -1);
            FrenzyResponseParser frenzyResponseParser = new FrenzyResponseParser(stockTracker, new ObjectMapper(), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getFrenzy()));
            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getFrenzy(), notificationsFormatConfig), new HttpRequestHelper(), frenzyResponseParser);
        } else if(site.equals("rimowa")){
            StockTracker stockTracker = new StockTracker(new HashMap<>(), 0);
            RimowaResponseParser rimowaResponseParser = new RimowaResponseParser(stockTracker, page.getUrls().get(0),  NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getRimowa()));
            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getRimowa(), notificationsFormatConfig), new HttpRequestHelper(), rimowaResponseParser);
        } else if(site.equals("antonioli")){
            StockTracker stockTracker = new StockTracker(new HashMap<>(), -1);
            AttachmentCreater attachmentCreater = new AttachmentCreater(siteNotificationsConfig.getAntonioli(), notificationsFormatConfig);
            HttpRequestHelper httpRequestHelper = new HttpRequestHelper();
            AntonioliResponseParser antonioliResponseParser = new AntonioliResponseParser(page.getUrls().get(0), page.getName(), stockTracker, NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getAntonioli()));

            return createDefault(page.getUrls().get(0), page.getDelay(),  attachmentCreater, httpRequestHelper, antonioliResponseParser);
        } else if(site.equals("twitter") || site.equals("ftlcodes")){
            StockTracker stockTracker = new StockTracker(new HashMap<>(), -1);
            KeywordSearchHelper keywordSearchHelper = new KeywordSearchHelper(page.getSku());
            NotificationConfig notificationConfig;
            if(site.equals("twitter"))
                notificationConfig = siteNotificationsConfig.getTwitter();
            else
                notificationConfig = siteNotificationsConfig.getFtlcodes();

            TwitterAbstractResponseParser rimowaResponseParser = new TwitterAbstractResponseParser(stockTracker, page.getUrls().get(0), keywordSearchHelper, NotificationsConfigTransformer.transformNotifications(notificationConfig));
            return new Twitter(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(notificationConfig, notificationsFormatConfig), new HttpRequestHelper(), rimowaResponseParser);
        } else if(site.equals("sns")){
            StockTracker stockTracker = new StockTracker(new HashMap<>(), -1);
            SnsResponseParser snsResponseParser = new SnsResponseParser(stockTracker, new KeywordSearchHelper(page.getSku()), page.getUrls().get(0), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getSns()));
            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getSns(), notificationsFormatConfig), new CloudflareRequestHelper(apiKeys), snsResponseParser);
        } else if(site.equals("offwhitepage")){
            OffWhiteSoldOutTagResponseParser offWhiteSoldOutTagResponseParser = new OffWhiteSoldOutTagResponseParser(page.getSku(), new StockTracker(new HashMap<>(), 10000),  NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getOffwhite()));
            return new OffWhitePage(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getOffwhite(), notificationsFormatConfig), new CloudflareRequestHelper(apiKeys), offWhiteSoldOutTagResponseParser);
        } else if(site.equals("complexcon")){
            StockTracker stockTracker = new StockTracker(new HashMap<>(), 0);
            ComplexconResponseParser complexconResponseParser = new ComplexconResponseParser(stockTracker, new ObjectMapper(), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getComplexcon()));
            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getComplexcon(), notificationsFormatConfig), new HttpRequestHelper(), complexconResponseParser);
        } else if(site.equals("footpatrol")){
            StockTracker stockTracker = new StockTracker(new HashMap<>(), 30000);
            FootpatrolResponseParser footpatrolResponseParser = new FootpatrolResponseParser(stockTracker, page.getUrls().get(0), page.getName(), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getFootpatrol()));
            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getFootpatrol(), notificationsFormatConfig), new HttpRequestHelper(), footpatrolResponseParser);
        } else if(site.equals("footpatrol-app")){
            StockTracker stockTracker = new StockTracker(new HashMap<>(), 30000);
            FootpatrolAppResponseParser footpatrolAppResponseParser =
                    new FootpatrolAppResponseParser(
                            stockTracker,
                            NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getFootpatrol())
                    );
            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getFootpatrol(), notificationsFormatConfig), new HttpRequestHelper(), footpatrolAppResponseParser);
        }else if(site.equals("soto")){
            StockTracker stockTracker = new StockTracker(new HashMap<>(), -1);
            KeywordSearchHelper keywordSearchHelper = new KeywordSearchHelper(page.getSku());
            SotoResponseParser sotoResponseParser = new SotoResponseParser(stockTracker, keywordSearchHelper, NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getSoto()));
            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getSoto(), notificationsFormatConfig), new HttpRequestHelper(), sotoResponseParser);
        } else if(site.equals("soleboxproduct")){
            KeywordSearchHelper keywordSearchHelper = new KeywordSearchHelper(page.getSku());
            SoleboxProductPageResponseParser soleboxProductPageResponseParser = new SoleboxProductPageResponseParser(keywordSearchHelper, NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getSolebox()));
            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getSolebox(), notificationsFormatConfig), new HttpRequestHelper(), soleboxProductPageResponseParser);
        } else if(site.equals("citygear")){
            KeywordSearchHelper keywordSearchHelper = new KeywordSearchHelper(page.getSku());
            StockTracker stockTracker = new StockTracker(new HashMap<>(),0);
            CitygearAbstractResponseParser citygearResponseParser = new CitygearAbstractResponseParser(stockTracker, keywordSearchHelper, NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getCitygear()));
            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getCitygear(), notificationsFormatConfig), new HttpRequestHelper(), citygearResponseParser);
        } else if(site.equals("citygearproduct")){
            StockTracker stockTracker = new StockTracker(new HashMap<>(),0);
            CitygearProductResponseParser citygearProductResponseParser = new CitygearProductResponseParser(page.getUrls().get(0), stockTracker, NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getCitygear()));
            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getCitygear(), notificationsFormatConfig), new HttpRequestHelper(), citygearProductResponseParser);
        } else if(site.equals("ccs")){
            StockTracker stockTracker = new StockTracker(new HashMap<>(),0);
            KeywordSearchHelper keywordSearchHelper = new KeywordSearchHelper(page.getSku());
            CCSResponseParser ccsResponseParser = new CCSResponseParser(stockTracker, keywordSearchHelper);
            return new CCS(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getCcs(), notificationsFormatConfig), new HttpRequestHelper(), ccsResponseParser);
        } else if(site.equals("dsmproduct")){
            NotificationConfig notificationConfig = getShopifyConfig(page.getUrls().get(0), siteNotificationsConfig);
            StockTracker stockTracker = new StockTracker(new HashMap<>(),0);
            DsmProductResponseParser dsmProductResponseParser = new DsmProductResponseParser(stockTracker, page.getUrls().get(0), page.getLocale(), new ObjectMapper(), NotificationsConfigTransformer.transformNotifications(notificationConfig));
            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(notificationConfig, notificationsFormatConfig), new HttpRequestHelper(), dsmProductResponseParser);
        } else if(site.equals("offwhiteall")){
            OffWhiteAllResponseParser offWhiteAllResponseParser = new OffWhiteAllResponseParser(new StockTracker(new HashMap<>(), 10000), new KeywordSearchHelper(page.getSku()),  NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getOffwhite()));
            GetLinksFromPage getLinksFromPage = new GetLinksFromPage(page.getSku());
            return new OffWhiteAll(page.getUrls().get(0), page.getDelay(),new AttachmentCreater(siteNotificationsConfig.getOffwhite(), notificationsFormatConfig),  new CloudflareRequestHelper(apiKeys), offWhiteAllResponseParser);
        } else if(site.equals("jimmyjazz")){
            JimmyJazzResponseParser jimmyJazzResponseParser = new JimmyJazzResponseParser(new StockTracker(new HashMap<>(), 0), page.getUrls().get(0),  NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getJimmyjazz()));
            return new JimmyJazz(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getJimmyjazz(), notificationsFormatConfig), new CloudflareRequestHelper(apiKeys), jimmyJazzResponseParser);

        } else if(site.equals("supremekw")){
            SupremeAllProductResponseParser supremeProductParseResponse = new SupremeAllProductResponseParser(new StockTracker(new HashMap<>(), 0), new KeywordSearchHelper(page.getSku()), page.getLocale(), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getSupreme()));
            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getSupreme(), notificationsFormatConfig), new HttpRequestHelper(), supremeProductParseResponse);
        }
        else if(site.equals("demandwareatc")){
            NotificationConfig notificationConfig;
            if(page.getUrls().get(0).contains("fye"))
                notificationConfig = siteNotificationsConfig.getFye();
            else
                notificationConfig = siteNotificationsConfig.getDemandware();

            DemandwareGetResponseParser shopifyResponseParser = new DemandwareGetResponseParser(new StockTracker(new HashMap<>(), 60000), NotificationsConfigTransformer.transformNotifications(notificationConfig));
            return new DemandwareGet(page.getUrls().get(0), page.getSku(), page.getDelay(), new AttachmentCreater(notificationConfig, notificationsFormatConfig), new HttpRequestHelper(),  shopifyResponseParser);
        } else if(site.equals("walmart")) {
            WalmartResponseParser shopifyResponseParser = new WalmartResponseParser(new ObjectMapper(), new StockTracker(new HashMap<>(), 60000), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getWalmart()), page.getSku());
            return new Walmart(page.getSku(), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getWalmart(), notificationsFormatConfig), new HttpRequestHelper(), shopifyResponseParser);
        } else if(site.equals("walmartterra")) {
            WalmartTerraResponseParser shopifyResponseParser = new WalmartTerraResponseParser(new ObjectMapper(), new StockTracker(new HashMap<>(), 60000), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getWalmart()), page.getSku());
            return new WalmartTerra(page.getSku(), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getWalmart(), notificationsFormatConfig), new HttpRequestHelper(), shopifyResponseParser);
        } else if(site.equals("amazon")){
            AmazonResponseParser amazonResponseParser = new AmazonResponseParser(
                    page.getUrls().get(0),
                    new StockTracker(new HashMap<>(), 60000),
                    NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getAmazon()));
            return createDefault(
                    page.getUrls().get(0),
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
            return createDefault(page.getUrls().get(0),
                    page.getDelay(),
                    new AttachmentCreater(siteNotificationsConfig.getDaveyboystoys(),notificationsFormatConfig),
                    new HttpRequestHelper(),
                    daveyboystoysResponseParser);

        } else if(site.equals("disneyrestock")){
            DisneyRestockResponseParser disneyRestockResponseParser = new DisneyRestockResponseParser(
                    new StockTracker(new HashMap<>(), 60000),
                    page.getUrls().get(0),
                    NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getDisney()));
            return new DisneyRestock(page.getUrls().get(0),
                    page.getDelay(),
                    new AttachmentCreater(siteNotificationsConfig.getDisney(),notificationsFormatConfig),
                    new HttpRequestHelper(),
                    disneyRestockResponseParser);
        } else if(site.equals("disneysitemap")){
            DisneySitemapResponseParser disneySitemapResponseParser = new DisneySitemapResponseParser(
                    new StockTracker(new HashMap<>(), 60000),
                    page.getUrls().get(0),
                    new KeywordSearchHelper(page.getSku()),
                    NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getDisney())
            );
            return new DisneySitemap(page.getUrls().get(0),
                    page.getDelay(),
                    new AttachmentCreater(siteNotificationsConfig.getDisney(),notificationsFormatConfig),
                    new HttpRequestHelper(),
                    disneySitemapResponseParser);
        } else if(site.equals("funimation")){
            FunimationResponseParser funimationResponseParser = new FunimationResponseParser(
                    page.getUrls().get(0),
                    new StockTracker(new HashMap<>(), 60000),
                    NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getFunimation())
            );
            return createDefault(page.getUrls().get(0),
                    page.getDelay(),
                    new AttachmentCreater(siteNotificationsConfig.getFunimation(),notificationsFormatConfig),
                    new HttpRequestHelper(),
                    funimationResponseParser);
        } else if(site.equals("fyesearch")){
            FyeSearchResponseParser fyeSearchResponseParser = new FyeSearchResponseParser(
                    new StockTracker(new HashMap<>(), 60000),
                    page.getUrls().get(0),
                    new KeywordSearchHelper(page.getSku()),
                    NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getFye())
            );
            return createDefault(page.getUrls().get(0),
                    page.getDelay(),
                    new AttachmentCreater(siteNotificationsConfig.getFye(),notificationsFormatConfig),
                    new HttpRequestHelper(),
                    fyeSearchResponseParser);
        } else if(site.equals("gamestop")){
            GamestopResponseParser gamestopResponseParser = new GamestopResponseParser(
                    page.getUrls().get(0),
                    new StockTracker(new HashMap<>(), 60000),
                    NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getGamestop()), new HttpRequestHelper());
            return createDefault(
                    page.getUrls().get(0),
                    page.getDelay(),
                    new AttachmentCreater(siteNotificationsConfig.getGamestop(),notificationsFormatConfig),
                    new HttpRequestHelper(),
                    gamestopResponseParser
            );
        } else if(site.equals("gemini")){
            GeminiResponseParser geminiResponseParser = new GeminiResponseParser(
                    new StockTracker(new HashMap<>(), 60000),
                    page.getUrls().get(0),
                    new KeywordSearchHelper(page.getSku()),
                    NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getGemini())
            );
            return createDefault(page.getUrls().get(0),
                    page.getDelay(),
                    new AttachmentCreater(siteNotificationsConfig.getGemini(),notificationsFormatConfig),
                    new HttpRequestHelper(),
                    geminiResponseParser
                    );
        } else if(site.equals("geminirestock")){
            GeminiRestockResponseParser geminiRestockResponseParser = new GeminiRestockResponseParser(new StockTracker(new HashMap<>(), 60000),
                    page.getUrls().get(0),
                    NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getGemini()));
            return createDefault(page.getUrls().get(0),
                    page.getDelay(),
                    new AttachmentCreater(siteNotificationsConfig.getGemini(),notificationsFormatConfig),
                    new HttpRequestHelper(),
                    geminiRestockResponseParser);
        } else if(site.equals("popcultcha")){
            PopCultchaResponseParser popCultchaResponseParser = new PopCultchaResponseParser(   new StockTracker(new HashMap<>(), 60000),
                    new KeywordSearchHelper(page.getSku()),
                    NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getPopcultcha()));
            return createDefault(page.getUrls().get(0),
                    page.getDelay(),
                    new AttachmentCreater(siteNotificationsConfig.getPopcultcha(), notificationsFormatConfig),
                    new HttpRequestHelper(),
                    popCultchaResponseParser);
        } else if(site.equals("walgreens")) {
            WalgreensResponseParser walgreensResponseParser = new WalgreensResponseParser(
                    new StockTracker(new HashMap<>(), 60000),
                    page.getUrls().get(0),
                    NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getWalgreens()));
            return createDefault(page.getUrls().get(0),
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
            return createDefault(page.getUrls().get(0),
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
                    page.getUrls().get(0)
            );
            return createDefault(page.getUrls().get(0),
                    page.getDelay(),
                    new AttachmentCreater(siteNotificationsConfig.getInstagram(), notificationsFormatConfig),
                    new HttpRequestHelper(),
                    instagramResponseParser);
        } else if(site.equals("lvr")){
            LvrResponseParser lvrResponseParser = new LvrResponseParser(new StockTracker(new HashMap<>(), -1), page.getUrls().get(0),  NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getLvr()));
            return new LVR(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getLvr(), notificationsFormatConfig), new HttpRequestHelper(), lvrResponseParser);
        } else if(site.equals("footpatrolproductpage")) {
            FootpatrolProductPageResponseParser footpatrolProductPageResponseParser = new FootpatrolProductPageResponseParser(new StockTracker(new HashMap<>(), -1), new KeywordSearchHelper(defaultKw),  NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getFootpatrol()));
            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getFootpatrol(), notificationsFormatConfig), new HttpRequestHelper(), footpatrolProductPageResponseParser);
        } else if(site.equals("footsites")){
            FootsitesResponseParser footsitesResponseParser = new FootsitesResponseParser(new StockTracker(new HashMap<>(), 60000), page.getUrls().get(0), page.getName(), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getFootsites()));
            return new Footsites(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getFootsites(), notificationsFormatConfig), new HttpRequestHelper(), footsitesResponseParser);
        } else if(site.equals("onygositemap")){
            OnygoSitemapResponseParser onygoSitemapResponseParser = new OnygoSitemapResponseParser(new StockTracker(new HashMap<>(), -1), new KeywordSearchHelper(page.getSku()), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getOnygo()));
            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getOnygo(), notificationsFormatConfig), new HttpRequestHelper(), onygoSitemapResponseParser);
        } else if(site.equals("shoepalace")){
            ShoepalaceResponseParser shoepalaceResponseParser = new ShoepalaceResponseParser(new StockTracker(new HashMap<>(), 6000), page.getUrls().get(0), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getShoepalace()));
            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getShoepalace(), notificationsFormatConfig), new HttpRequestHelper(), shoepalaceResponseParser);
        } else if(site.equals("supremepage")){
            SupremePageResponseParser supremePageResponseParser = new SupremePageResponseParser(new StockTracker(new HashMap<>(), 0), new KeywordSearchHelper(defaultKw), page.getLocale(), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getSupreme()));
            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getSupreme(), notificationsFormatConfig), new HttpRequestHelper(), supremePageResponseParser);
        } else if(site.equals("snsproduct")){
            SNSProductResponseParser snsProductResponseParser = new SNSProductResponseParser(new StockTracker(new HashMap<>(), 0), page.getUrls().get(0), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getSns()));
            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getSns(), notificationsFormatConfig), new CloudflareRequestHelper(apiKeys), snsProductResponseParser);
        } else if(site.equals("yeezysupply")){
            NotificationConfig notificationConfig = getShopifyConfig(page.getUrls().get(0), siteNotificationsConfig);
            ShopifyAbstractResponseParser shopifyAbstractResponseParser =
                    new ShopifyAbstractResponseParser(new StockTracker(new HashMap<>(), 0), page.getUrls().get(0), NotificationsConfigTransformer.transformNotifications(notificationConfig));
            YsResponseParser ysResponseParser = new YsResponseParser(page.getUrls().get(0), NotificationsConfigTransformer.transformNotifications(notificationConfig), shopifyAbstractResponseParser);
            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(notificationConfig, notificationsFormatConfig), new HttpRequestHelper(), ysResponseParser);
        } else if(site.equals("instagramstory")){
            InstagramStoryResponseParser instagramStoryResponseParser =
                    new InstagramStoryResponseParser(new ObjectMapper(), new StockTracker(new HashMap<>(), 0), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getInstagram()), page.getName());
             return new InstagramStory(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getInstagram(), notificationsFormatConfig), new HttpRequestHelper(),instagramStoryResponseParser, page.getSku());
        } else if(site.contains("frontend")){
            NotificationConfig notificationConfig = getShopifyConfig(page.getUrls().get(0), siteNotificationsConfig);
            ShopifyAbstractResponseParser shopifyAbstractResponseParser = new ShopifyAbstractResponseParser(new StockTracker(new HashMap<>(), 0), page.getUrls().get(0), NotificationsConfigTransformer.transformNotifications(notificationConfig));
            ShopifyFrontendHelper shopifyFrontendHelper = new ShopifyFrontendHelper(page.getUrls().get(0));
            return new ShopifyFrontend(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(notificationConfig, notificationsFormatConfig), new HttpRequestHelper(), shopifyAbstractResponseParser, shopifyFrontendHelper);
        } else if(site.equals("offspring")){
            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getOffspring(), notificationsFormatConfig), new HttpRequestHelper(), new Offspring(new StockTracker(new HashMap<>(), 0), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getOffspring()), new ObjectMapper()));
        } else if(site.equals("caliroots")){
            return createDefault(page.getUrls().get(0), page.getDelay(), new AttachmentCreater(siteNotificationsConfig.getOffspring(), notificationsFormatConfig), new CloudflareRequestHelper(apiKeys), new Caliroots(new StockTracker(new HashMap<>(), 0), page.getUrls().get(0), NotificationsConfigTransformer.transformNotifications(siteNotificationsConfig.getOffspring())));
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
                "",
                new TaskStatus(0, 0, "", url, new DiscordLog("")));
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
}
