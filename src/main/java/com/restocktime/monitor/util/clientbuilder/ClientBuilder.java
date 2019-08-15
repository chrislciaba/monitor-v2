package com.restocktime.monitor.util.clientbuilder;

import com.restocktime.monitor.util.clientbuilder.model.BasicRequestClient;
import com.restocktime.monitor.util.clientbuilder.model.HttpProxy;
import com.restocktime.monitor.util.url.UrlHelper;
import com.restocktime.monitor.monitors.ingest.backdoor.BackDoor;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.*;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.log4j.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.util.ArrayList;
import java.util.List;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class ClientBuilder {
    private static final Logger log = Logger.getLogger(BackDoor.class);

    public List<BasicRequestClient> buildClients(String url, List<String> proxyList, String site){
        List<BasicRequestClient> basicRequestClients = new ArrayList<>();
        for(int i = 0; i < proxyList.size(); i++){
            List<Header> headerList = getDefaultHeaders(url, site);

            HttpProxy httpProxy = transformToProxy(proxyList.get(i));

            if(httpProxy == null){
                continue;
            }

            CredentialsProvider credsProvider = new BasicCredentialsProvider();;
            if(httpProxy.getUsername() != null){
                credsProvider.setCredentials(
                        new AuthScope(httpProxy.getHost(), httpProxy.getPort()),
                        new UsernamePasswordCredentials(httpProxy.getUsername(), httpProxy.getPassword()));
            }


            HttpHost httpHost = new HttpHost(httpProxy.getHost(), httpProxy.getPort(), "http");


            RequestConfig config = RequestConfig.custom()
                    .setProxy(httpHost)
                    .setConnectTimeout(10 * 1000)
                    .setConnectionRequestTimeout(10 * 1000)
                    .setSocketTimeout(10 * 1000)
                    .setCookieSpec(CookieSpecs.STANDARD)
                    .build();

            RequestConfig noRedirectconfig = RequestConfig.custom()
                    .setProxy(httpHost)
                    .setConnectTimeout(10 * 1000)
                    .setRedirectsEnabled(false)
                    .setConnectionRequestTimeout(10 * 1000)
                    .setSocketTimeout(10 * 1000)
                    .setCookieSpec(CookieSpecs.STANDARD)
                    .build();

            CloseableHttpClient httpclient = null;

            try {
                SSLContext sslContext = SSLContextBuilder
                        .create()
                        .loadTrustMaterial(new TrustSelfSignedStrategy())
                        .build();

                // we can optionally disable hostname verification.
                // if you don't want to further weaken the security, you don't have to include this.
                HostnameVerifier allowAllHosts = new NoopHostnameVerifier();

                // create an SSL Socket Factory to use the SSLContext with the trust self signed certificate strategy
                // and allow all hosts verifier.
                SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext, allowAllHosts);

                BasicCookieStore cookieStore = new BasicCookieStore();

                if (url.contains("hottopic")) {
                    headerList.add(new BasicHeader("Content-Type", "application/x-www-form-urlencoded"));
                }

                if (site.contains("shopify") || site.contains("frontend") || url.contains("porter") || url.contains("toytokyo") || url.contains("pacsun") || url.contains("barneys") || url.contains("mesh")) {
                    if (url.contains("barneys")) {
                        headerList.add(new BasicHeader("Cookie", "mf_user=2d34bd3a1b58a619a54940b8477dc6cb|; ftrnd_cid=eyJjb250ZW50IjoiZmUxNzIwNDBiNjgxODkwNDkxZDZmMTg5OGZlYzQ3NGQ4MzZjNzY0MzBjY2Y5MWYxMDRmMjllNTIyZWRkZTliMzM0ZTJkMmM1NmQ1OTA2YTRhMTRmMTY5NzkyNGNmOGI0MzU2MWY0MzVmZTkzOTEzZjFiZmQxNDI4NTExZjc2ZGZhMmZjN2E3Y2Y5ZjA4NzdlZDI0NTY5N2FlMmU0MDM2NDFkNmZlZjk1NmYzNTExYzY1NDc2NDU2ODQ4OTJkMiIsInRhZyI6eyJ0eXBlIjoiQnVmZmVyIiwiZGF0YSI6WzEwOSw1Myw3MSwyNDQsMTIwLDE1MCwyMywzNywxNDIsOTksMjQzLDE5Myw4MSwyNiwxODcsMTldfX0=; DYN_USER_ID=1768670574; DYN_USER_CONFIRM=e80a943acdb71578ead5ec757e528027; userPrefLanguage=en_US; JSESSIONID=AJmtlhVZSyJlqVelCSjm6RRt8TBob8UNc0C-YrUidX6ovLo-iLVP!-2128259667!763427-prodapp2!20880!-1; PRECOG_USER_ID=34dca7ef59924bbcdd259bc0b2d8b406fc28bd9ad6d10fb1415ef8788b14110c; ak_bmsc=FF57479085A06A9C11F9CA4D6F2C932C17DC947FD87A00007A53D25BA908095F~pllcWBD9M1dVjJDpNut6mtwdm40tj25gYM/0KvXnnWZNM9Uao2uxjJ+ptmbGlBD08MAhCpXUJ/CSwSbSTzi3868VzRxAJNUfQDQlCYQ2oJBD0WIviNbaZtTH+mL8wJHeNUH3fxsWVozSuq4Qf/fZ0SGxq5Hl0wQ4G+hdx9gDqKRJoEgq+assV+HQ+Svn0242XJakv/tdARb+8wf+f7TaosFOW+M2+XaJlsjWU4PnF630YTGcCb3TdSvRqZW0K+uRFQgKcAPH/Fs+BvND/umak5ncBcjCsUuVmRbb9TNg04SWMQaXZMPFthlzUgGv4QGesa3yok5f6mFHx4I7VwRBLBmg==; instanceID=prodapp2!20880; _sdsat_precogID=34dca7ef59924bbcdd259bc0b2d8b406fc28bd9ad6d10fb1415ef8788b14110c; __psrw=a713e05e-cf4a-4d82-a985-cff341eb72b9; usr_currency=US-USD; dw=1; productFindingMethod=External; AMCVS_94AA483F53B6BF2B0A490D44%40AdobeOrg=1; prevPage=Homepage: Barneys New York; _ga=GA1.2.1514388928.1540510591; _gid=GA1.2.505907680.1540510591; _gcl_au=1.1.67674251.1540510591; AMCV_94AA483F53B6BF2B0A490D44%40AdobeOrg=-1758798782%7CMCIDTS%7C17830%7CMCMID%7C91800957235991879146554234864341430392%7CMCAID%7CNONE%7CMCOPTOUT-1540517791s%7CNONE%7CMCAAMLH-1541115391%7C6%7CMCAAMB-1541115391%7Cj8Odv6LonN4r3an7LhD3WZrU1bUpAkFkkiY1ncBR96t2PTI; numberOfItems=; dslv_s=First%20Visit; s_cc=true; gdpr_consent=9999; _sp_ses.9d59=*; _fbp=fb.1.1540510592397.789601327; _gat_fe821b5d299b68da6915087bb7cc0516=1; _gat_BNY=1; rr_rcs=eF5j4cotK8lM4TMxMdA11DVkKU32SLRINDZNSjTRNbFIAxKWSSa6pqlmFropZqaWiUmmKckpaaYAipcOnQ; stc112113=tsa:1540510591313.1423758191.3738766.4389959771039076.:20181026001117|env:1%7C20181125233631%7C20181026001117%7C3%7C1020419:20191025234117|uid:1540510591313.2000073148.5627017.112113.413597089.:20191025234117|srchist:1020419%3A1%3A20181125233631:20191025234117; dslv=1540510877293; sc_fb_session={%22start%22:1540510592316%2C%22p%22:3}; s_pvpg=Homepage%3A%20Barneys%20New%20York; _sp_id.9d59=06f8c737-9046-4eb3-b324-453966c8d23a.1540510592.1.1540510878.1540510592.b9700861-f239-49bd-87a4-f984033c9395; s_ppvl=Homepage%253A%2520Barneys%2520New%2520York%2C29%2C29%2C806%2C657%2C806%2C1680%2C1050%2C2%2CL; s_ppv=Homepage%253A%2520Barneys%2520New%2520York%2C15%2C15%2C806%2C657%2C806%2C1680%2C1050%2C2%2CL; mf_12ca041e-13fd-4dd5-b794-cf38db44dfe5=af2abc3934602555777f2ed945282546|102513749f4e473ad2249af80fd30f0eacaf8142.47.1540510573878,102532459eed555559dd67386d05a415959bb032.47.1540510592448,1025082523b8047634ecbd69651b4e77f8ec08ed.47.1540510868530,102517452b76f1134c4ee69da5942003623ef100.47.1540510877862|1540510890452|-2278004788_3283536449.-617513071_0.6611529550_-8999751209|4|||0|16.01; _4c_=fVPZbtswEPyVgg95ciTehwGjcA4ULpAYbtLnQCKp2IguUErVIPC%2FZ2lLqOG01QNNLoezs7PrdzRsfY3mRHAsCNYGc0Fn6MW%2FdWj%2Bjmwb119xeQ0lmqNt37fdPE2HYUjyLNSAS2xTpWiGbOM8IIhJRELg%2FLB7rr1bATm6X8ezz4LdPvpQATWCQP1a5T6si1Xvp1APt8ddHpqh8wEO19vQVP6LwhAtQA%2FKmSycpor6vLBCU13kzGbYMWZFzokBXBP57jIL2%2BALH8KB6T%2Fiu10fxZ9ExyA48Sc%2BKhzFru8efzxd3S6v1%2Fcn5BOFo0lnk6bqg7NJ7fs0T7tuuq398NaEl9l4fC6bPCvb0LiUpN8fLkm08PJGbe7TziijoEijDZVKf11urhbkotq5heaUCcyJVopgiamUGlMqpFJGM8wZJpJzerHc3C5iO1roIuKwKRublbFY6PsMfVs%2B%2FVzdxL5JxjgHBpyMwyAUA4CLDXS%2ByF7LHu1n6Pc4LEZRZSQjYEkPk6Elx%2FEDRNi5cWoQ9lJxzUzBcuqY0VJ664SwGWTLmI%2F8Rz7oohQKkBIIWuA7vAfyKR3lRmn4HdMRkDqmi4Ud0PxcHDWfxR3navT9308Z%2FAnOn4IJn3VFOGZa%2FQUOxh1dmNw7LVYrQrkG2G5Ctef3TNL9fv8B; sc_fb={%22v%22:0.3%2C%22t%22:101%2C%22p%22:3%2C%22s%22:1%2C%22b%22:[]%2C%22pv%22:[]%2C%22tr%22:0%2C%22e%22:[]}; RT=\"dm=barneys.com&si=6e6e207f-faf2-4c7e-983c-6a5cdbb8c6ec&ss=1540508699023&sl=14&tt=60192&obo=1&sh=1540510878840%3D14%3A1%3A60192%2C1540510870178%3D13%3A1%3A55431%2C1540510593536%3D12%3A1%3A51065%2C1540510588579%3D11%3A1%3A45981%2C1540510578021%3D10%3A1%3A43412&bcn=%2F%2F36cc248b.akstat.io%2F&ld=1540510878841&r=https%3A%2F%2Fwww.barneys.com%2F&ul=1540510890440&hd=1540510890698\"; mmapi.store.p.0=%7B%22mmparams.d%22%3A%7B%7D%2C%22mmparams.p%22%3A%7B%22pd%22%3A%221572046891834%7C%5C%22850744496%7CBQAAAApVAwC9PaZg2xAxDgABEQABQgUqOYABAOsGYmTTOtZIwJUdr9I61kgAAAAA%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F8ABkRpcmVjdAHbEAEAAAAAAAAAAAD%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F8AAAAAAAAAAUU%3D%5C%22%22%2C%22srv%22%3A%221572046891839%7C%5C%22nycvwcgus02%5C%22%22%7D%2C%22mmengine%22%3A%7B%7D%7D; mmapi.store.s.0=%7B%22mmparams.d%22%3A%7B%7D%2C%22mmparams.p%22%3A%7B%7D%2C%22mmengine%22%3A%7B%7D%7D"));
                    } else if (url.contains("porter")) {
                        headerList.add(new BasicHeader("Cookie", "lang_iso=en; channel=am; country_iso=GB; deviceType=Desktop;"));
                    } else if(url.contains("sneakersnstuff.com")){
                        headerList.add(new BasicHeader("Cookie", "cbar_lvt=; cbar_sess_pv=3; cbar_uid=1427162171585; _fbp=fb.1.1549509310096.574135714; _ga=GA1.2.1380702250.1513441768; _gid=GA1.2.1932714286.1555645899; _dc_gtm_UA-1918066-1=1; _gat=1; _gat_UA-1918066-1=1; _gcl_au=1.1.559566277.1555645899; cbar_sess=31; prev_view_cbar_site=en_usd; __cf_bm=c2575ffd57f8bf4d4802caff2a3b9f961e81880d-1555645898-1800-AfNGJTh5/vD8WsFDF9zS2wTtg5Na73sDdbp43sCCT//s/Ann1+I7BZei+ZZdwwV8IgV3fw2xY/63b5w9o+EKVSU=; AntiCsrfToken=46aac77457034ef0b508cb82d3997c91; Raffle Visitors=true; 2c.cId=5a8be69260b2dd1544e8b4d7; frosmo_quickContext=%7B%22VERSION%22%3A%221.1.0%22%2C%22UID%22%3A%22nfn0fd.jru1ujux%22%2C%22origin%22%3A%22sneakersnstuff_com%22%2C%22lastDisplayTime%22%3A%7B%22314%22%3A1549509331%7D%2C%22lastRevisionId%22%3A%7B%22314%22%3A1%7D%2C%22lastPageView%22%3A%7B%22time%22%3A1549509331210%7D%2C%22states%22%3A%7B%22session%22%3A%7B%7D%7D%7D; __cfduid=d17c2043324016a727d4d90781d2a27051545895123; png.state=4vI9/mhbf5LgnPK0XSotfAqpDwUL5sW4DWszDV7bJVXnLLsWTB7WNIWka+cyWEaLcNco0NI0npnTEq2GEKMMamLytmNl9e8HR9b7lCLsa4uEhMsa; _iub_cs-66151059=%7B%22consent%22%3Atrue%2C%22timestamp%22%3A%222018-09-10T18%3A53%3A25.029Z%22%2C%22version%22%3A%221.2.4%22%2C%22id%22%3A66151059%7D"));
                    }
                    httpclient = HttpClients.custom()
                            .setRedirectStrategy(new LaxRedirectStrategy())
                            .setDefaultCredentialsProvider(credsProvider)
                            .setDefaultHeaders(headerList)
                            .disableCookieManagement()
                            .setSSLSocketFactory(connectionFactory)
                            .build();
                } else {
                    if(url.contains("sneakersnstuff.com")){
                        BasicClientCookie cf_bm = new BasicClientCookie("__cf_bm", "e29aae30f5507f463b76dd618cf3d5b4ca72268b-1556525006-1800-Aemm9W8U2sXiYgq2k4f+2eptbmE0Z4BqQ0wkFBfM6rKNTWsyfMPnB3FsviHTm1CrG+gQ9+wqUZX03+upJfxTUEw=");
                        cf_bm.setDomain("www.sneakersnstuff.com");
                        //cookieStore.addCookie(cf_bm);

                        BasicClientCookie cfuid = new BasicClientCookie("__cfduid", "debe2f6f47faf32e9a8e59d496fb286681555646391");
                        cfuid.setDomain("www.sneakersnstuff.com");
                       // cookieStore.addCookie(cfuid);

//                        "cbar_lvt=;" +
//                                "cbar_sess_pv=3;" +
//                                "cbar_uid=1427162171585;" +
//                                "_fbp=fb.1.1549509310096.574135714;" +
//                                "_ga=GA1.2.1380702250.1513441768;" +
//                                "_gid=GA1.2.1932714286.1555645899;" +
//                                "_dc_gtm_UA-1918066-1=1;" +
//                                "_gat=1; _gat_UA-1918066-1=1;" +
//                                "_gcl_au=1.1.559566277.1555645899;" +
//                                "cbar_sess=31;" +
//                                "prev_view_cbar_site=en_usd;" +
//                                "__cf_bm=c2575ffd57f8bf4d4802caff2a3b9f961e81880d-1555645898-1800-AfNGJTh5/vD8WsFDF9zS2wTtg5Na73sDdbp43sCCT//s/Ann1+I7BZei+ZZdwwV8IgV3fw2xY/63b5w9o+EKVSU=; AntiCsrfToken=46aac77457034ef0b508cb82d3997c91; Raffle Visitors=true; 2c.cId=5a8be69260b2dd1544e8b4d7; frosmo_quickContext=%7B%22VERSION%22%3A%221.1.0%22%2C%22UID%22%3A%22nfn0fd.jru1ujux%22%2C%22origin%22%3A%22sneakersnstuff_com%22%2C%22lastDisplayTime%22%3A%7B%22314%22%3A1549509331%7D%2C%22lastRevisionId%22%3A%7B%22314%22%3A1%7D%2C%22lastPageView%22%3A%7B%22time%22%3A1549509331210%7D%2C%22states%22%3A%7B%22session%22%3A%7B%7D%7D%7D;" +
//                                "__cfduid=d17c2043324016a727d4d90781d2a27051545895123;" +
//                                "png.state=4vI9/mhbf5LgnPK0XSotfAqpDwUL5sW4DWszDV7bJVXnLLsWTB7WNIWka+cyWEaLcNco0NI0npnTEq2GEKMMamLytmNl9e8HR9b7lCLsa4uEhMsa; _iub_cs-66151059=%7B%22consent%22%3Atrue%2C%22timestamp%22%3A%222018-09-10T18%3A53%3A25.029Z%22%2C%22version%22%3A%221.2.4%22%2C%22id%22%3A66151059%7D"));

                    } else if(url.contains("oneblockdown.it")){
                        BasicClientCookie cfuid = new BasicClientCookie("__cfduid", "df6bcd6f83e26504593b8f11c735954e91555884668");
                        cfuid.setDomain("www.sneakersnstuff.com");
                        cookieStore.addCookie(cfuid);
                    }
                    httpclient = HttpClients.custom()
                            .setRedirectStrategy(new LaxRedirectStrategy())
                            .setDefaultCredentialsProvider(credsProvider)
                            .setDefaultHeaders(headerList)
                            .setDefaultCookieStore(cookieStore)
                            .setSSLSocketFactory(connectionFactory)
                            .build();
                }
                basicRequestClients.add(new BasicRequestClient(httpclient, config, new ArrayList<>(), null, cookieStore, httpHost, noRedirectconfig));

            } catch(Exception e){
                log.error(EXCEPTION_LOG_MESSAGE, e);
            }
        }

        return basicRequestClients;
    }

    private List<Header> getDefaultHeaders(String url, String site){

        List<Header> h = new ArrayList<>();
        if(url.contains("sneakersnstuff.com") || url.contains("oneblockdown.it") || url.contains("https://www.nakedcph.com") || url.contains("bstn")){
            h.add(new BasicHeader(HttpHeaders.HOST, UrlHelper.getHost(url)));
            h.add(new BasicHeader("Upgrade-Insecure-Requests", "1"));
            h.add(new BasicHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (iPhone; CPU iPhone OS 12_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/73.0.3683.68 Mobile/15E148 Safari/605.1"));
            h.add(new BasicHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3"));
            h.add(new BasicHeader(HttpHeaders.REFERER, url));
            h.add(new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate"));
            h.add(new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "en-US,en;q=0.9"));
            h.add(new BasicHeader(HttpHeaders.CACHE_CONTROL, "max-age"));
            h.add(new BasicHeader(HttpHeaders.CONNECTION, "keep-alive"));

            return h;
        } else if(url.contains("doofinder")){
            h.add(new BasicHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (iPhone; CPU iPhone OS 12_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/73.0.3683.68 Mobile/15E148 Safari/605.1"));
            h.add(new BasicHeader("Origin", "https://footdistrict.com"));
            return h;
        } else if(url.contains("mesh")){
            h.add(new BasicHeader(HttpHeaders.HOST, UrlHelper.getHost(url)));
            if(url.contains("footpatrolgb")){
                h.add(new BasicHeader("X-API-Key", "AD60F89E1BB248F388B9FC671851A2B8"));
            } else if(url.contains("size")){
                h.add(new BasicHeader("X-API-Key", "07B7D4BBF1F646EDBEB251594045A2D8"));
            } else if(url.contains("jdsports")){
                h.add(new BasicHeader("X-API-Key", "81B99BC3CE5B4889BE6C9DC6A2309BCA"));
            }
            h.add(new BasicHeader(HttpHeaders.USER_AGENT, "size/6.3.2.1446 (iphone-app; iOS 12.3.1)"));
            h.add(new BasicHeader(HttpHeaders.ACCEPT, "*/*"));
            h.add(new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate"));
            h.add(new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "en-gb"));
            h.add(new BasicHeader(HttpHeaders.CONNECTION, "keep-alive"));
            h.add(new BasicHeader("MESH-Commerce-Channel", "android-app"));
            h.add(new BasicHeader("mesh-version", "cart=4"));
            System.out.println("HERE");
            return h;
        } else {
            h.add(new BasicHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"));
            h.add(new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "en-US,en;q=0.9"));
            if (url.contains("search.jimmyjazz")) {
                h.add(new BasicHeader("Origin", "http://www.jimmyjazz.com"));
                h.add(new BasicHeader(HttpHeaders.REFERER, "http://www.jimmyjazz.com/footwear/nike-react-element-55/BQ6166-006"));
            } else {
                h.add(new BasicHeader("Origin", UrlHelper.deriveBaseUrl(url)));
                h.add(new BasicHeader(HttpHeaders.REFERER, url));
            }

            h.add(new BasicHeader("Upgrade-Insecure-Requests", "1"));
            if (url.contains("bstn"))
                h.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.66 Safari/537.36"));
            else
                h.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Linux; Android 8.0.0; Pixel 2 XL Build/OPD1.170816.004) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Mobile Safari/537.36"));

            h.add(new BasicHeader("Cache-Control", "no-cache, no-store, must-revalidate"));
            h.add(new BasicHeader("Pragma", "no-cache"));
            h.add(new BasicHeader("Expires", "0"));
            return h;
        }
    }

    private HttpProxy transformToProxy(String proxyStr){
        String proxyParts[] = proxyStr.trim().split(":");
        if(proxyParts.length == 4) {
            return new HttpProxy(proxyParts[0], Integer.parseInt(proxyParts[1]), proxyParts[2], proxyParts[3]);

        } else if(proxyParts.length == 2){
            return new HttpProxy(proxyParts[0], Integer.parseInt(proxyParts[1]), null, null);
        }

        return null;
    }


}
