package com.restocktime.monitor.util.http.client.builder;

import com.restocktime.monitor.util.http.client.builder.model.BasicRequestClient;
import com.restocktime.monitor.util.http.client.builder.model.HttpProxy;
import com.restocktime.monitor.util.helper.url.UrlHelper;
import com.restocktime.monitor.monitors.ingest.aio.backdoor.BackDoor;

import com.restocktime.monitor.util.http.client.builder.model.OkHttpClientConfig;
import okhttp3.*;
import okhttp3.Authenticator;
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
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.log4j.Logger;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.restocktime.monitor.constants.Constants.EXCEPTION_LOG_MESSAGE;

public class ClientBuilder {
    private static final Logger log = Logger.getLogger(BackDoor.class);

    public List<BasicRequestClient> buildClients(String url, List<String> proxyList, String site){
        List<BasicRequestClient> basicRequestClients = new ArrayList<>();
        for(int i = 0; i < proxyList.size(); i++){
            if ( (!site.contains("sns") && !site.contains("bstn") && !site.contains("naked") && !site.contains("panagora"))) {
                List<Header> headerList = getDefaultHeaders(url, site);

                HttpProxy httpProxy = transformToProxy(proxyList.get(i));

                if (httpProxy == null) {
                    continue;
                }

                CredentialsProvider credsProvider = new BasicCredentialsProvider();
                ;
                if (httpProxy.getUsername() != null) {
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
                        }
                        httpclient = HttpClients.custom()
                                .setRedirectStrategy(new LaxRedirectStrategy())
                                .setDefaultCredentialsProvider(credsProvider)
                                .setDefaultHeaders(headerList)
                                .disableCookieManagement()
                                .setSSLSocketFactory(connectionFactory)
                                .build();


                    } else {
                        httpclient = HttpClients.custom()
                                .setRedirectStrategy(new LaxRedirectStrategy())
                                .setDefaultCredentialsProvider(credsProvider)
                                .setDefaultHeaders(headerList)
                                .setDefaultCookieStore(cookieStore)
                                .setSSLSocketFactory(connectionFactory)
                                .build();
                    }
                    basicRequestClients.add(
                            BasicRequestClient.builder()
                                    .closeableHttpClient(Optional.of(httpclient))
                                    .requestConfig(config)
                                    .headerList(new ArrayList<>())
                                    .cookieStore(cookieStore)
                                    .httpHost(httpHost)
                                    .noRedirectrequestConfig(noRedirectconfig)
                                    .build()
                    );

                } catch (Exception e) {
                    log.error(EXCEPTION_LOG_MESSAGE, e);
                }
            } else {

                HttpProxy httpProxy = transformToProxy(proxyList.get(i));
                int proxyPort = httpProxy.getPort();
                String proxyHost = httpProxy.getHost();
                String username = httpProxy.getUsername();
                String password = httpProxy.getPassword();

               /* CookieJar cookieJar = new CookieJar() {
                    private final HashMap<String, Map<String, Cookie>> cookieStore = new HashMap<>();

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        Map<String, Cookie> cookieMap = new HashMap<>();
                        for (Cookie cookie : cookies) {
                            cookieMap.put(cookie.name(), cookie);
                        }

                        cookieStore.put(url.host(), cookieMap);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {

                        List<Cookie> cookies = new ArrayList<>();
                        if(cookieStore.get(url.host()) == null)
                            return cookies;
                        for(String key : cookieStore.get(url.host()).keySet()) {
                            cookies.add(cookieStore.get(url.host()).get(key));
                        }

                        return cookies;
                    }
                };

                OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .writeTimeout(5, TimeUnit.SECONDS)
                        .readTimeout(5, TimeUnit.SECONDS)
                        .cookieJar(cookieJar)
                        .connectionPool(
                                new ConnectionPool(1, 1, TimeUnit.MICROSECONDS)
                        )
                        .retryOnConnectionFailure(false)
                       // .addInterceptor(new LoggingInterceptor())
                        .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)));

                if(httpProxy.getUsername() != null && httpProxy.getPassword() != null) {
                    Authenticator proxyAuthenticator = new Authenticator() {
                        @Override
                        public Request authenticate(Route route, Response response) throws IOException {
                            String credential = Credentials.basic(username, password);
                            return response.request().newBuilder()
                                    .header("Proxy-Authorization", credential)
                                    .build();
                        }
                    };

                    okHttpClientBuilder.proxyAuthenticator(proxyAuthenticator);
                }

                OkHttpClient okHttpClient = okHttpClientBuilder.build();*/

                 basicRequestClients.add(BasicRequestClient.builder()
                        .closeableHttpClient(Optional.empty())
                        .okHttpClientConfig(Optional.of(OkHttpClientConfig.builder()
                                .password(password)
                                .username(username)
                                .proxyHost(proxyHost)
                                .proxyPort(proxyPort)
                                .cookies(new HashMap<>())
                                .connectionPool(new ConnectionPool(1, 1, TimeUnit.MINUTES))
                                .build()))
                        .build()
                 );
            }
        }

        return basicRequestClients;
    }

    private List<Header> getDefaultHeaders(String url, String site){

        List<Header> h = new ArrayList<>();
        if(url.contains("sneakersnstuff.com") || url.contains("oneblockdown.it") || url.contains("https://www.nakedcph.com") || url.contains("bstn")){
            h.add(new BasicHeader(HttpHeaders.HOST, UrlHelper.getHost(url)));
            h.add(new BasicHeader("Upgrade-Insecure-Requests", "1"));
            String[] s = {
                "Mozilla/5.0 (iPhone; CPU iPhone OS 12_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/76.0.3809.81 Mobile/15E148",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 10_3 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) CriOS/56.0.2924.75 Mobile/14E5239e Safari/602.1",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 11_4 like Mac OS X) AppleWebKit/604.1.34 (KHTML, like Gecko) CriOS/67.0.3396.59 Mobile/15F79 Safari/604.1",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 12_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) GSA/76.0.253539693 Mobile/16F203 Safari/604.1",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_3 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like Gecko) CriOS/65.0.3325.152 Mobile/14G60 Safari/602.1",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 9_3_5 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) GSA/18.1.132077863 Mobile/13G36 Safari/600.1.4",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_3 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like Gecko) GSA/58.0.212077146 Mobile/14G60 Safari/602.1",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 11_4 like Mac OS X) AppleWebKit/604.1.34 (KHTML, like Gecko) CriOS/69.0.3497.71 Mobile/15E148 Safari/604.1",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 11_3_1 like Mac OS X) AppleWebKit/604.1.34 (KHTML, like Gecko) GSA/49.0.195456936 Mobile/15E302 Safari/604.1",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_2 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) GSA/28.0.157793287 Mobile/14F89 Safari/602.1",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 8_1_2 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) GSA/5.1.42378 Mobile/12B440 Safari/600.1.4",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 9_3_5 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) GSA/41.0.178428663 Mobile/13G36 Safari/601.1",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 11_2_5 like Mac OS X) AppleWebKit/604.1.34 (KHTML, like Gecko) GSA/43.0.185608249 Mobile/15D60 Safari/604.1",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 12_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) GSA/78.0.257670029 Mobile/16F203 Safari/604.1",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 11_4_1 like Mac OS X) AppleWebKit/604.1.34 (KHTML, like Gecko) GSA/60.0.215960477 Mobile/15G77 Safari/604.1",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_3 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) GSA/51.0.198805899 Mobile/14G60 Safari/602.1",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_3 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like Gecko) CriOS/59.0.3071.102 Mobile/14G60 Safari/602.1",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 11_4 like Mac OS X) AppleWebKit/604.1.34 (KHTML, like Gecko) CriOS/68.0.3440.83 Mobile/15F79 Safari/604.1",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_3 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) GSA/46.0.189829128 Mobile/14G60 Safari/602.1",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 12_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) GSA/72.0.243649956 Mobile/15E148 Safari/605.1",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 8_1 like Mac OS X) AppleWebKit/537.51.2 (KHTML, like Gecko) GSA/4.2.2.38484 Mobile/12B411 Safari/9537.53",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0_3 like Mac OS X) AppleWebKit/604.1.34 (KHTML, like Gecko) CriOS/63.0.3239.73 Mobile/15A432 Safari/604.1",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 11_4_1 like Mac OS X) AppleWebKit/604.1.34 (KHTML, like Gecko) CriOS/68.0.3440.70 Mobile/15G77 Safari/604.1",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0_2 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) GSA/19.0.133715217 Mobile/14A456 Safari/600.1.4",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 12_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/73.0.3683.68 Mobile/15E148 Safari/605.1",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_3 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) GSA/41.0.178428663 Mobile/14G60 Safari/602.1",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_3 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) GSA/33.0.164895372 Mobile/14G60 Safari/602.1",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_3 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like Gecko) CriOS/62.0.3202.70 Mobile/14G60 Safari/602.1",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 10_2_1 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) GSA/22.0.141836113 Mobile/14D27 Safari/600.1.4",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_1 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) GSA/25.0.152548370 Mobile/14E304 Safari/602.1",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 12_0 like Mac OS X) AppleWebKit/604.1.34 (KHTML, like Gecko) GSA/60.0.215960477 Mobile/16A366 Safari/604.1",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_2 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) CriOS/58.0.3029.113 Mobile/14F89 Safari/602.1",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 8_3 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) CriOS/42.0.2311.47 Mobile/12F70 Safari/600.1.4",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 9_2 like Mac OS X) AppleWebKit/601.1 (KHTML, like Gecko) CriOS/47.0.2526.107 Mobile/13C75 Safari/601.1.46"
            };
            //"Mozilla/5.0 (iPhone; CPU iPhone OS 12_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/73.0.3683.68 Mobile/15E148 Safari/605.1"
            //h.add(new BasicHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (iPhone; CPU iPhone OS 12_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/73.0.3683.68 Mobile/15E148 Safari/605.1"));
            h.add(new BasicHeader(HttpHeaders.USER_AGENT, s[new Random().nextInt(s.length)]));
            h.add(new BasicHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3"));
            h.add(new BasicHeader(HttpHeaders.REFERER, url));
            h.add(new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate"));
            h.add(new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "en-US,en;q=0.9"));
            h.add(new BasicHeader(HttpHeaders.CACHE_CONTROL, "max-age"));
            h.add(new BasicHeader(HttpHeaders.CONNECTION, "keep-alive"));
            //h.add(new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));
            //h.add(new BasicHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 12_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/76.0.3809.81 Mobile/15E148 Safari/605.1"));
            //h.add(new BasicHeader("Accept-Language", "en-us"));
            //h.add(new BasicHeader("Accept-Encoding", "gzip, deflate"));
            //h.add(new BasicHeader("Connection", "keep-alive"));

            return h;
        } else if(url.contains("doofinder")){
            h.add(new BasicHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (iPhone; CPU iPhone OS 12_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/73.0.3683.68 Mobile/15E148 Safari/605.1"));
            h.add(new BasicHeader("Origin", "https://footdistrict.com"));
            return h;
        } else if(site.contains("svd-app")){
            h.add(new BasicHeader("device-os", "I-iOS 12.4"));
            h.add(new BasicHeader("device-id", "054FF9A6-4438-4105-B2EE-B28F1EC74657"));
            h.add(new BasicHeader("app-version", "1.0.145"));
            h.add(new BasicHeader("store-code", "en"));
            return h;
        } else if(url.contains("mesh")){
            h.add(new BasicHeader(HttpHeaders.HOST, UrlHelper.getHost(url)));

            h.add(new BasicHeader(HttpHeaders.USER_AGENT, "size/6.3.2.1446 (iphone-app; iOS 12.3.1)"));
            h.add(new BasicHeader(HttpHeaders.ACCEPT, "*/*"));
            h.add(new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate"));
            h.add(new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "en-gb"));
            h.add(new BasicHeader(HttpHeaders.CONNECTION, "keep-alive"));
            h.add(new BasicHeader("MESH-Commerce-Channel", "android-app"));
            h.add(new BasicHeader("mesh-version", "cart=4"));
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

    private Header getSizeHeader(String url) {
        if(url.contains("footpatrolgb")){
            return new BasicHeader("X-API-Key", "AD60F89E1BB248F388B9FC671851A2B8");
        } else if(url.contains("size")){
            return new BasicHeader("X-API-Key", "07B7D4BBF1F646EDBEB251594045A2D8");
        } else if(url.contains("sizefr")) {
            return new BasicHeader("X-API-Key", "EDA837174E054D849DF88D58D84BDCC0");
        } else if(url.contains("sizeie")) {
            return new BasicHeader("X-API-Key", "3BABAEC1678C4875996B64E6B2552D5A");
        } else if(url.contains("sizede")) {
            return new BasicHeader("X-API-Key", "A677793DE1F48B8A02C6D9FDD221B9AA");
        } else if(url.contains("sizese")) {
            return new BasicHeader("X-API-Key", "7A577A68772C4D479360749694090E19");
        } else if(url.contains("sizeit")) {
            return new BasicHeader("X-API-Key", "F18A400DECFE409883A6EBDC10CB8B79");
        } else if(url.contains("sizedk")) {
            return new BasicHeader("X-API-Key", "99560279316F4A11A2664CFC528D8A00");
        } else if(url.contains("sizees")) {
            return new BasicHeader("X-API-Key", "5FB102EE28EC4B53B6603F1CB3BF2338");
        } else if(url.contains("jdsportsuk")){
            return new BasicHeader("X-API-Key", "81B99BC3CE5B4889BE6C9DC6A2309BCA");
        } else if(url.contains("jdsportsie")){
            return new BasicHeader("X-API-Key", "30E9BDAD8D3A4CF398C74BFB3D89A157");
        } else if(url.contains("jdsportses")){
            return new BasicHeader("X-API-Key", "BEA6B9E15DD84E02BE8C692DEA77367A");
        } else if(url.contains("jdsportsfr")){
            return new BasicHeader("X-API-Key", "9328AEF56C454C3BAC119B48E520D5B4");
        } else if(url.contains("jdsportsnl")){
            return new BasicHeader("X-API-Key", "D1EB234D58DB4EFC9FD94B595BA3AEA7");
        } else if(url.contains("jdsportsde")){
            return new BasicHeader("X-API-Key", "28F995D1B0F14B37AFBF6232ED69CBEE");
        } else if(url.contains("jdsportsbe")){
            return new BasicHeader("X-API-Key", "8B6F8C80C8A640EEB26DC134B45A78B4");
        } else if(url.contains("jdsportsit")){
            return new BasicHeader("X-API-Key", "EE72E7144A614F128927831361DC0A47");
        } else if(url.contains("jdsportsdk")){
            return new BasicHeader("X-API-Key", "5F923029B56345A3B197468BDD33C053");
        } else if(url.contains("jdsportsse")){
            return new BasicHeader("X-API-Key", "990B84BE2EBA4BA885D4A99BBBD2DC15");
        } else if(url.contains("jdsportsmyf")){
            return new BasicHeader("X-API-Key", "12FD7BC7C4B942E0A814EDE92FD03CB2");
        } else if(url.contains("jdsportsau")){
            return new BasicHeader("X-API-Key", "663F1C0343F741C6A9825A6BC0A86873");
        }

        return null;
    }


}
