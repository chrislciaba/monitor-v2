package com.restocktime.monitor.util.hawk;

import com.restocktime.monitor.util.timeout.Timeout;
import org.apache.log4j.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.*;

public class Hawk {
    final static Logger logger = Logger.getLogger(Hawk.class);


    private Map<String, String> ids;
    private Map<String, String> keys;

    public Hawk() {
        this.ids = new HashMap<>();
        ids.put("size", "b5050ee5b3");
        ids.put("jdsportsuk", "81f03e6b68");
        ids.put("footpatrolgb", "1c88f5f855");
        this.keys = new HashMap<>();
        keys.put("size", "e11952ab6b3319c5e4cb203b2ec2939e");
        keys.put("jdsportsuk", "ba18206a86610f93432214428287123f");
        keys.put("footpatrolgb", "e705e8f04c662635f34962dfcac2af75");
    }

    public String createHawkHeader(final String store, final String url){
        String id = ids.get(store);
        String key = keys.get(store);
        String t = Long.toString(System.currentTimeMillis()/1000);
        String nonce = UUID.randomUUID().toString().substring(0, 6);
        try {
String s1 = a(
        url,
        t,
        nonce
);
logger.info(s1);
            String s = createHmac(key,
                    s1
            );

            String hawk =  "Hawk id=\"" + id + "\", mac=\"" + s + "\", ts=\"" + t + "\", nonce=\"" + nonce + "\"";
            Timeout.timeout(1000);
            return hawk;
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    private String a(/*url*/String str, /*time*/String j2, String str2/*uuid*/) throws Exception {
        String str3;
        String sb;
        String sb2 = null;
        String str4 = "";
        String[] split = str.split("[?][&]");
        if (split.length > 1) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(split[0]);
            sb3.append("?");
            sb3.append(split[1]);
            str = sb3.toString();
        }
        URL url = new URL(str);
        String c2 = c(str);
        StringBuilder sb4 = new StringBuilder();
        sb4.append(str4);
        sb4.append("hawk.1.header\n");
        String sb5 = sb4.toString();
        StringBuilder sb6 = new StringBuilder();
        sb6.append(sb5);
        sb6.append(j2);
        sb6.append("\n");
        str4 = sb6.toString();
        StringBuilder sb7 = new StringBuilder();
        sb7.append(str4);
        sb7.append(str2);
        sb7.append("\n");
        str3 = sb7.toString();
        StringBuilder sb8 = new StringBuilder();
        sb8.append(str3);
        sb8.append("GET");
        sb8.append("\n");
        str4 = sb8.toString();
        if (c2.equals("")) {
            StringBuilder sb9 = new StringBuilder();
            sb9.append(str4);
            sb9.append(url.getPath());
            sb9.append("\n");
            sb = sb9.toString();
        } else {
            StringBuilder sb10 = new StringBuilder();
            sb10.append(str4);
            sb10.append(url.getPath());
            sb10.append("?");
            sb10.append(c2);
            sb10.append("\n");
            sb = sb10.toString();
        }
        String str5 = sb;
        StringBuilder sb11 = new StringBuilder();
        sb11.append(str5);
        sb11.append(url.getHost());
        sb11.append("\n");
        sb2 = sb11.toString();


        StringBuilder sb12 = new StringBuilder();
        sb12.append(sb2);
        sb12.append("80\n");
        str4 = sb12.toString();
        StringBuilder sb13 = new StringBuilder();
        sb13.append(str4);
        sb13.append("\n");
        sb2 = sb13.toString();
        StringBuilder sb14 = new StringBuilder();
        sb14.append(sb2);
        sb14.append("\n");
        str3 = sb14.toString();


        return str3.replace(" ", "%20").replace(":", "%3A").replace("<", "%3C").replace(">", "%3E");
    }


    private String c(String str) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
      //  Uri parse = Uri.parse(str);
        Set<String> s= new HashSet();
        s.add("channel");
        s.add("expand");
        Map<String, String> m = new HashMap<>();
        m.put("channel", "android-app");
        m.put("expand", "variations,informationBlocks,customisations");
        ArrayList arrayList = new ArrayList(new TreeSet(s));
        Collections.sort(arrayList, new Comparator<String>() {
            /* renamed from: a */
            public int compare(String str, String str2) {
                return str.compareToIgnoreCase(str2);
            }
        });
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            String str2 = (String) arrayList.get(i2);
            String queryParameter = m.get(str2);
            if (queryParameter.length() > 0) {
                sb.append(str2);
                sb.append("=");
                sb.append(queryParameter);
            }
            if (i2 < arrayList.size() - 1 && queryParameter.length() > 0) {
                sb.append("&");
            }
        }
        return sb.toString();
    }

    private String createHmac(String str, String str2){
        String str3 = "";
        try {
            Mac instance = Mac.getInstance("HmacSHA256");
            instance.init(
                    new SecretKeySpec(
                            str.getBytes(),
                            "HmacSHA256")
            );
            return Base64.getEncoder().encode(instance.doFinal(
                    str2.getBytes()
            )).toString();

        } catch (Exception e) {
            //return str3;
        }
        return null;
    }



    public void mo17504a() {



        //((HERE4))
       // this.f6968i = m9165d(
         //       m9166e( //crypto bullshi
                        mo17511c( //more bullshit
                                mo17507b("gb==OdwNND/3s/fvyJ2FVHN/2ZfHSmLGOR5V2mrPIJv8RuWq4YT/o1ApLOSRWvKRUk8oYAuRKKN5qKeLxWLAcopg"));
    }

    /* renamed from: b */
    public String mo17507b(String str) {
        return new StringBuilder(str).reverse().toString();
    }


    /* renamed from: c */
    public String mo17511c(String str) {
        return m9161a(
                m9163a(
                        str,
                        m9162a(
                                m9169h(str) //list of two letter substrings
                        )
                )
        );
    }

    /* renamed from: a */
    private String[] m9163a(String str, int[] iArr) {
        String[] strArr = new String[iArr.length];
        int i = 0;
        for (String str2 : m9169h(str)) {
            strArr[iArr[i]] = str2;
            i++;
        }
        return strArr;
    }

    /* renamed from: h */
    private List<String> m9169h(String str) {
        ArrayList arrayList = new ArrayList();
        int length = str.length();
        int i = 0;
        while (i < length) {
            int i2 = i + 2;
            arrayList.add(str.substring(i, Math.min(length, i2)));
            i = i2;
        }
        return arrayList;
    }

    /* renamed from: a */
    private String m9161a(String[] strArr) {
        StringBuilder sb = new StringBuilder();
        for (String append : strArr) {
            sb.append(append);
        }
        return sb.toString();
    }

    /* renamed from: a */
    private int[] m9162a(List<String> list) {
        int[] iArr = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            if ((i & 1) != 0) { //odd
                iArr[i] = iArr[i - 1] - 1;
            } else if (i == 0) { //0
                iArr[i] = 1;
            } else { //even
                iArr[i] = iArr[i - 1] + 3;
            }
        }
        return iArr;
    }
}
