package com.restocktime.monitor.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Akamai {

    private final String[] IMAGEDATA = {"-533479225","1190868050","-858804567","-478770949","34712964","-880781543","867322169","991305586","-316899420","1749902403","-958883221","-1474626848","-2000791231","-87268314","155229937","142006334","1246607029","1063218567","369994210","-574654566","233802077","-654616279","-1720288619","248066775","-1690343480","813579045","318510261","-2099981713","1916819915","426522241","1982873175","259558704","-1135446035","2123810049","1772694758","809855726","-644707227","1459695271","98083533","1765877848","-1551103271","-1914916132","1427706569","171353429","1336912234","653579005","-1028161146","1398744446","-547031256","-626036720","614558751","-602697365","1854921746","2085005153","-1843345038","1867726891","-573393068","-2043001444","-1466500821","-601805002","-427186192","1532988208","-1539748643","546785630","81521532","1192026764","-1757464415","-323512492","-1481594226","107746788","-858095953","363553961","-1759692542","1099214035","-2087355482","-197391300","-2097876531","2019372932","-330462884","1802059905","827112250","1440380285","467074714","228661045","527283370","-1107885465","1708523220","2121425108","145400717","2053165989","1991074146","-2059670094","911984436","-889775473","-311617566","-796487964","-905019286","-286402639","-16013481","1129656526","-1052621218","1681510179","-2042032971","258753656","-249539474","1995029405","1868201585","-191426787","1519756866","1526502928","-972855174","1853700035","1801350837","1934535310","1072274748","473072677","286672874","1290074182","-1856922812","-751522268","-5098406","356375750","1863300093","750250045","-638950352","1092940192","-587861234","478201070","-1428052155","-1695993807","-1618886839","-85270360","1049235916","-218798047","-824819736","-1208829223","-1763606687","-1055579251","-695583957","-2124327015","1338821594","2024394737","-1260392421","-1063918748","-782765713","109743002","-145873267","2055065344","-1705839794","-2006674797","-2062417103","202730061","951098531","-1898972102","-1168641565","1360719819","-255778355","-2004073000","-1079244743","457208623","-1614898741","-557395473","-1684730672","-561518103","-1000614991","901081754","-900767228","601426296","937614702","306461062","1948656569","1161873092","-951048736","-2079626833","2009426827","345761982","894304389","-1626697646","721107868","-1546260301","-314002814","1546549374","-1987137393","89434850","1384428534","347295556","352523830","1209486423","792832955","945789467","1481987152","-1330646784","-1495424737","165941514","849233153","-450936921","-306000977","-272260002","1166933750","-1835758318","1754539158","1912811729","-962968957","35491729","77086292","2032182834","-768831533","36895537","-1862872003","-813956440","1936119145","-1517947305","-1284864849","197088676","-1816834394","766141118","1704433804","-1286731005","-1366250393","-2105571876","-1319799889","-1952775740","780208014","-1868224405","1375713954","-2084843209","-1998788056","435680602","-226665783","1886686846","1141023512","-1048746225","-703537349","1898029715","-826544384","193015596","1289698732","1894753284","1077460565","-1413630782","-1128807590","-312650750","-441906433","812024957","1439759401","-111074295","-453317645","-688962502","736164362","1942858881","469223319","-741000212","1639272820","-384926886","1548079247","2005072441","-1377331724","1870633422","-420647417","-965368968","-1336963158","626254252","-1896967653","1104709886","1650305990","-109692439","905575854","-1018437694","1450131897","337549539","-1052131244","-1371148462","-1210879278","325829703","371811250","-1314100125","318722185","-1067219145","-454382934","1017004869","-673184472","215569038","1917072377","841612531","575096859","-1753887160","-655173093","-1077520139","-261905087","-139525991","-908097258","-1648619715","391106743","1197772952","-16553977","-1929234759","-1167717642","269520794","1181692381","2094371358","-1282496015","1642829565","2090504319","-1628632322","-855632870","-1998929305","-2005169686","-1444815886","-1635151002","522310787","1152247591","-1493472039","1943273938","-1090594941","1517780680","318350641","1642543420","-1873809268","858902114","-1429342033","-1936788517","-1910118381","-531252006","1514768229","2017068164","-311714590","-1453938274","1220277834","-1515201405","1492918427","-1321445854","824217737","69711112","344075329","-1156418781","-858234117","1510385381","1293887522","-419809639","805198488","1290155937","648169110","-1596135890","-1387098377","719737079","-1965727635","-352219242","-1179114679","113420698","-1100580052","1161806690","1798394793","-1287635506","1711319718","905970143","833498743","-1172861974","1696133848","1110677564","446283753","-1786110017","726074790","282466731","1300013972","-301585085","-717404832","330064591","-647904333","-476442234","221418729","-1433405406","1761512573","2036266399","-755055013","-515076464","-1025441582","1929424273","610575639","-574041051","-1771809176","1949280899","330438175","-1646628381","-1980914855","2031227420","-955729054","-452179264","-1922482972","526417380","-879823317","-1654196472","1971618492","364294544","-1808651648","1465862389","-586660593","-962265402","-1332157837","670969217","-596811292","-1766892945","1544647678","1504764514","1779720857","-796066866","-271080089","-1278141541","-735722508","195610834","1157960124","-293848541","-1425858879","-1986458541","-623467536","-797506645","362056146","212473745","-1689230474","-394425087","236803916","-663485973","382791623","-2110557961","-1783089878","605555237","-1934032292","-738960547","2082115192","1009535724","2058096498","-315510204","1967590074","1363950005","-2081587808","-1564304910","-1887221738","-513249529","1016201736","1745685937","2135880179","211905806","-344476266","1831693079","1141288276","-118984555","-2060149016","1159444987","168459664","153236124","1355368153","1152255436","-936371786","198972893","-539931264","867184916","-961913580","-121637175","711665406","620741722","1835568403","1303814692","-1941377173","-1182006658","1174059900","-1763035954","-1750857519","-1565579795","-779206992","177944597","-1396932992","726270712","-273068484","-726541808","-1701515442","510058953","-1534596372","-1327913956","280878654","-1324291206","1501903306","-617432212","-1432536440","-223344901","648272851","1049403460","1172066684","1440998377","-1657616677","-956272919","-963375195","1665642889","935769943","-227285553","-241673136","1484778263","1223459057","-1369090146","-1113477316","-857822524","-396575091","-1934805436","-1080460314","-654920445","-421460888","363895340","968329203","-934954940","-391250906","-1987081563","849626156","-508220569","-693708758","360287008","-980578635","-1023114224","-1178122384","1695913874","551421118","850857098","1118854806","496455002","1669927590","1178692316","-1883730260","917300359","200628049","1896873548","1474395826","27411342","940461528","2012576033","-481257752","-2088375209","-197191349","-1089176207","157562083","1047250240","652233055","-1868068007","-476240373","1262878945","405976126","1840798212","1640831637","-1481922735","697505007","2135664190","1245141298","-1895666662","-1664600460","394239754","347585382","-1487276530","381035314","1140790277","209178218","-1925146254","-1341135031","1473940103","2014241610","526381613","-686309414","-631580353","-1831426169","473611905","1002793996","701102146","66667019","1845832427","1683030166","-954685976","1409756506","-1665844891","-299911622","1674235834","-516388511","857219923","479618590","-1596197028","-486496326","1936913221","-1387911841","1178003068","1416486482","-1753084083","1941756424","-1780031839","631310713","1097125154","600937979","-1151984106","821131791","-1487899646","-9718845","1188428809","1179526470","1497410073","-450353559","1394686547","1689434288","-468243090","-1459390341","-499160796","-1426521368","525051908","-300640878","1893072528","-824541417","371701919","-1736253654","20573414","-801466790","1557792708","-867996866","-1454633379","1076138799","-1357611649","-1218451156","-101911350","1024649994","2064510389","-1361200787","250745475","-420070058","-2062939969","-1033489643","-420779329","1262162981","-1975462168","342504846","867790585","104534415","414173895","-903837451","320487152","691562486","1511444547","-824139370","-1687900118","1759405889","724419348","-2141826340","-734847174","1306390764","-1387519333","-1274503199","1644933566","-1782654497","-1207306799","1275759741","1119346737","600407037","-1037984361","1719141678","-872449395","1952815068","463640920","2003407205","-1050719844","880494744","979738192","2087682557","397274289","-1749298448","-1494363497","1536907502","-825375091","-1927365192","863691654","-1690567059","486847271","-1671045364","-235694007","-1242028238","519924606","-1024746180","-812516759","1587795424","226855760","-1959146050","-1778582851","-950964718","-644631463","-1390197917","1171883645","-422190023","-340438029","2052982681","-1977832966","-1843224151","1850519927","939966667","-1924095356","-1268955611","2057000467","-1560070169","1185885133","1151163132","-504020751","1117066095","-513373422","-828908883","1106318419","-390007157","-1110690587","-1585942551","-1291087701","148518137","1064759725","-1770022059","-645964021","-2070414920","-360038513","1974644765","-2087292635","-1723552797","-306772180","1203905298","-1908503090","-1006565309","717798204","1404581219","1471354530","-2142861998","-176305339","-2058853308","-885708772","-1187647843","-1098897499","505294809","873016398","1700638994","1292677676","-400009322","771871928","1583907455","-1170503765","2014944246","1715714187","-926555761","549531937","478492527","-898583714","631347641","1481846854","-166447365","1354458445","441638102","693321932","-1003330601","1142121597","559614801","-364428855","-1693202299","-286296443","-2067751109","1773786429","-861306983","347265191","-2043474358","1412861106","110335122","147240732","79697243","-65169755","-940507213","-732786088","1590535386","-1146805075","736434977","-1291404919","-1309608661","-735887547","-581097353","236726374","-1218530790","-2038950847","800074848","-965821767","1110904680","-1142088286","699234900","-206311305","1926286043","1121289573","2137631411","682558149","-688680219","1434125880","877498383","-482382150","1690388223","1757292811","-1082614189","2022631578","-1798036160","-1110541694","-217761131","-664609087","645354853","181159353","5179676","-906072299","-2030780670","-1375402202","1133466901","643146272","-687258138","1425984483","957113511","-279068014","-1579275590","-400738553","-35169951","1242709077","1576684620","-901182146","1958273577","-2031111477","-676273039","1519344680","1913066696","-2132576325","1496240465","-736571815","1811658424","-1216172679","-1660751546","1264657029","196766413","1437134335","-2141281336","35382391","-1260374368","-1303477343","152603613","-612784234","611664018","402586310","-751191676","1331166204","663754801","-1486351662","938275697","791339626","-2109085621","1732635166","1457031384","441281506","-430809018","684162697","424663432","1710839254","1643570932","844291532","-217307719","-1911468693","-1858068682","2116099367","-1906301027","865954035","903215997","1973851065","-459129557","2086634689","-1516832268","-1082178193","275490211","-818190931","-1815615777","839588370","1239995079","1523860565","1235718686","-796000342","291017469","-1444906757","1048973760","-6896623","-435979846","-103379579","1203128572","-85695905","-734513447","-1019769883","2098200122","1332470076","710580430","-945189960","1009459292","1434508088","461863800","1760909738","1849405969","702643278","462783532","-1744409804","-442210601","-257832395" };

    private Long futuredelay;
    private boolean trand;
    private Long start_ts;
    private String cookie;
    private String useragent;
    private String formInfo;
    private String url;
    private String screensize;
    private Long d3;
    private String mact;
    private int mval;
    private String doact;
    private int doval;
    private String dmact;
    private int dmval;
    private int totalAction;
    private long tst;

    private int kecnt;
    private int mecnt;
    private int kval;
    private String kact;
    private int mouseposx;
    private int mouseposy;
    private int doactimes;
    private int dmactimes;
    private List<Long> mactimes;
    private List<Long> kactimes;
    private String initialdata;
    private Integer z1;

    public Akamai(){
        this.z1 = null;
        this.futuredelay = 0L;
        this.trand = false;
        this.start_ts = get_cf_date();
        this.cookie = "2";
        this.useragent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36";
        this.formInfo = "0,0,0,0,630,630,0;0,0,0,0,"+AB(UUID.randomUUID().toString())+",1230,0;1,0,0,0,"+AB(UUID.randomUUID().toString())+",883,0;";
        this.url = "https://www.nike.com/us/en_us/";
        this.screensize = "(768,1366)";
        this.d3 = null;
        this.mact = "";
        this.mval = 0;
        this.doact = "";
        this.doval = 0;
        this.dmact = "";
        this.dmval = 0;
        this.totalAction = 0;
        this.tst = 8;

        this.mactimes = new ArrayList<>();
        this.kactimes = new ArrayList<>();

         /*
        self.futuredelay=0
        self.trand=False
        self.start_ts=self.get_cf_date()
        self.cookie="2"
        self.useragent="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36"
        self.forminfo="0,0,0,0,630,630,0;0,0,0,0,"+str(self.ab(str(uuid.uuid4())))+",1230,0;1,0,0,0,"+str(self.ab(str(uuid.uuid4())))+",883,0;"
        self.url="https://www.nike.com/us/en_us/"
        self.screensize=(768,1366)
        self.d3=None
        self.mact=""
        self.mval=0
        self.doact=""
        self.doval=0
        self.dmact=""
        self.dmval=0
        self.totalaction=0
        self.tst=8
     */

        this.kecnt = 0;
        this.mecnt = 0;
        this.kval = 0;
        this.kact = "";
        this.mouseposx = randInt(100, 500);
        this.mouseposy = randInt(10, 100);
        this.doactimes = randInt(40, 400);
        dmactimes = doactimes + randInt(1, 5);

        int sum = 0;
        for(int i = 0; i < randInt(12, 24); i++){
            long cur = (long)(randInt(5, 50));
            mactimes.add(cur);
            sum += cur;
        }

        this.futuredelay += this.dmactimes + 2 + sum;
        this.start_ts -= futuredelay;
        this.initialdata = generateSensorData();

    }

    /*

    def generatesensordata(self):
        start=self.get_cf_date()
        t=self.get_cf_date()
        e=self.update_t()
#        e=4
        c=self.cookie
        n=self.gd()
        i="do_en,dm_en,t_en"
        r=self.forminfo
        d=self.url
        s = "0,0"
        u = 25279115
        l = self.get_cf_date()-self.start_ts
#        l=9
        _ = int(int(self.z1/23) / 6)
        v=30261689
        m="0,0,0,0,0,0,0,"+str(e)+",0,"+str(self.start_ts)+",-999999,"+str(int(self.z1/23))+",0,0,"+str(_)+",0,0,"+str(l)+",0,0,"+c+","+str(self.ab(c))+",-1,-1,"+str(v)
        h="94"
        g="0,0,0,0,1,0,0"
        sensor_data="1.28-1,2,-94,-100," + n + "-1,2,-94,-101," + i + "-1,2,-94,-105," + "-1,2,-94,-102," + "-1,2,-94,-108,"+ "-1,2,-94,-110," + "-1,2,-94,-117," + "-1,2,-94,-111," + "-1,2,-94,-109," + "-1,2,-94,-114," + "-1,2,-94,-103," + "-1,2,-94,-112," + d + "-1,2,-94,-115," + m + "-1,2,-94,-106," + s
        sensor_data = sensor_data + "-1,2,-94,-119,-1-1,2,-94,-122," + g
        w = self.ab(sensor_data)
        sensor_data = sensor_data + "-1,2,-94,-70,-1-1,2,-94,-80," + h + "-1,2,-94,-116," + str(self.o9()) + "-1,2,-94,-118," + str(w) + "-1,2,-94,-121,"
        b=self.od("0a46G5m17Vrp4o4c","afSbep8yjnZUjq3aL010jO15Sawj2VZfdYK8uY90uxq")[:16]
        y=int(self.get_cf_date()/3600000)
        k=self.get_cf_date()
        C=b+self.od(y,b)+sensor_data
        sensor_data=C+";"+str(self.get_cf_date()-t)+";-1;0"
#        sensor_data=C+";7;-1;0"
        self.trand=True
        self.tst=self.get_cf_date()-start
        return sensor_data
     */

    public String generateSensorData(){
        long start = get_cf_date();
        long t = get_cf_date();
        long e = update_t();
        String c = this.cookie;
        String n = gd();
        String i = "do_en,dm_en,t_en";
        String r = this.formInfo;
        String d = this.url;
        String s = "0,0";
        String u = "25279115";
        Long l = get_cf_date() - this.start_ts;
        int underscore =  ((this.z1 / 23) / 6);
        String v = "30261689";
        String m = "0,0,0,0,0,0,0,"+ Long.toString(e) + ",0," + Long.toString(start_ts) + ",-999999," + Integer.toString(z1/23) + ",0,0," + Integer.toString(underscore) + ",0,0," + Long.toString(l) + ",0,0," + c + ","+ Long.toString(AB(c)) + ",-1,-1," + v;
        String h = "94";
        String g = "0,0,0,0,1,0,0";
        String sensor_data="1.28-1,2,-94,-100," + n + "-1,2,-94,-101," + i + "-1,2,-94,-105," + "-1,2,-94,-102," + "-1,2,-94,-108,"+ "-1,2,-94,-110," + "-1,2,-94,-117," + "-1,2,-94,-111," + "-1,2,-94,-109," + "-1,2,-94,-114," + "-1,2,-94,-103," + "-1,2,-94,-112," + d + "-1,2,-94,-115," + m + "-1,2,-94,-106," + s;
        sensor_data = sensor_data + "-1,2,-94,-119,-1-1,2,-94,-122," + g;
        long w = AB(sensor_data);
        sensor_data = sensor_data + "-1,2,-94,-70,-1-1,2,-94,-80," + h + "-1,2,-94,-116," + Long.toString(o9()) + "-1,2,-94,-118," + Long.toString(w) + "-1,2,-94,-121,";
        String b = od("0a46G5m17Vrp4o4c","afSbep8yjnZUjq3aL010jO15Sawj2VZfdYK8uY90uxq").substring(0, 16);
        Integer y = (int)(get_cf_date()/3600000);
        Long k = get_cf_date();
        String C = b + od(Integer.toString(y), b) + sensor_data;
        sensor_data = C + ";" + Long.toString(get_cf_date() - t) + ";-1;0";
        trand = true;
        this.tst = get_cf_date() - start;
        return sensor_data;
    }

    public void setCookie(String cookie){
        this.cookie = cookie;
    }

    /*
    def o9(self):
        t=e=self.d3
        for c in range(0,5):
            n = int(t / (10**c)) % 10
            a = n + 1
            mn=n%4
            if mn==0:
                e=e*a
            elif mn==1:
                e=e+a
            else:
                e=e-a
        return e
     */
    private long o9(){
        long t = d3;
        long e = d3;

        for(int i = 0; i < 6; i++){
            int n = (int)(t / Math.pow(10, i) % 10);
            int a = n + 1;
            int mn = n % 4;
            if(mn == 0){
                e = e* a;
            } else if(mn == 1){
                e = e + a;
            } else {
                e = e - a;
            }
        }

        return e;
    }

    /*def od(self,t,e):
        t=str(t)
        e=str(e)
        n=len(e)
        c=[]
        for a in range(0,len(t)):
            o=ord(t[a])
            f=t[a]
            i=ord(e[a%n])
            if (o>47)&(o<=57):
                o=o+(i%10)
                if o>57:
                    o=o-10
            if o!=ord(t[a]):
                f=chr(o)
            c.append(f)
        return "".join(c)
        */
    private String od(String t, String e){
        int n = e.length();
        String c = "";
        for(int a = 0; a < t.length(); a++){
            int o = (int)t.charAt(a);
            char f = t.charAt(a);
            int i = (int)(e.charAt(a % n));
            if( o > 47 && o <= 57){
                o = o + (i % 10);
                if ( o > 57){
                    o = o - 10;
                }
            }
            if(o != (int)t.charAt(a)){
                f = (char)o;
            }
            c = c + f;

        }

        return c;
    }

    private long update_t(){
        return System.currentTimeMillis() - (long)this.start_ts;
    }

    private int randInt(int start, int end){
        return (int)(Math.random()*(end - start) + start);
    }

    /*
        self.kecnt=0
        self.mecnt=0
        self.kval=0
        self.kact=""
        self.mouseposx=random.randint(100,500)
        self.mouseposy=random.randint(10,100)
        self.doactimes=random.randint(40,400)
        self.dmactimes=self.doactimes+random.randint(1,5)
        self.mactimes=[]
        for i in range(1,random.randint(12,24)):
            a=float(random.randint(5,50))
            self.mactimes.append(a)
        self.kactimes=[]
#        for i in range(0,random.randint(20,50)):
#            b=float(random.randint(1000,2000))
#            a=float(random.randint(20,100))
#            self.kactimes.append((a,b))
#            self.futuredelay+=a+b
        self.futuredelay+=self.dmactimes+2+sum(self.mactimes)
        self.start_ts-=int(self.futuredelay)
        self.initialdata=self.generatesensordata()
     */

     /*

    def gd(self):
        t=self.useragent
        e=str(self.ab(t))
        c=str(self.start_ts/2)
        if (self.start_ts%2)!=0:
            c+=".5"
        self.z1=int(self.start_ts/(2016*2016))
        ll=random.randint(500,999)
        l="0."+str(ll)+str(random.randint(100000,999999))+str(ll/2)
        if self.d3==None:
            self.d3=self.get_cf_date()%10000000
        #12147,4 depends on browser
        return t + ",uaend,12147,20030107,en-US,Gecko,3,0,0,0," + str(self.z1) + "," + str(self.d3) + ",1366,728,1366,768,1366,"+str(random.randint(550,720))+",1366,,cpen:0,i1:0,dm:0,cwen:1,non:1,opc:0,fc:0,sc:0,wrc:1,isc:0,vib:1,bat:1,x11:0,x12:1," + e + "," + l + "," + c + ",loc:"
     */


    private String gd(){
        String t = this.useragent;
        String e = Long.toString(AB(t));
        String c = Long.toString(start_ts / 2);
        if(start_ts % 2 != 0){
            c += ".5";
        }

        z1 = (int)(start_ts / (2016*2016));
        int ll = randInt(500, 999);
        String l = "0." + Integer.toString(ll) + Integer.toString(randInt(100000,999999)) + Integer.toString(ll/2);
        if(d3 == null){
            d3 = (get_cf_date() % 10000000);
        }

        return t + ",uaend,12147,20030107,en-US,Gecko,3,0,0,0," + Integer.toString(this.z1) + "," + Long.toString(this.d3) + ",1366,728,1366,768,1366,"+Integer.toString(randInt(550,720))+",1366,,cpen:0,i1:0,dm:0,cwen:1,non:1,opc:0,fc:0,sc:0,wrc:1,isc:0,vib:1,bat:1,x11:0,x12:1," + e + "," + l + "," + c + ",loc:";

    }




    /*

    def ab(self,t):
        if t==None:
            return -1
        a=0
        for p in t:
            if ord(p)<128:
                a+=ord(p)
        return a
     */

    private long AB(String t){
        if(t == null){
            return -1;
        }

        long a = 0;
        for(int i = 0; i < t.length(); i++){
            if((int)t.charAt(i) < 128){
                a += (int)t.charAt(i);
            }
        }

        return a;
    }




    private long get_cf_date(){
        return (System.currentTimeMillis() - this.futuredelay);
    }

    public void setUrl(String url){
        this.url = url;
    }

    /*
    def domouseaction(self):
        macttime=self.update_t()
        mousex=self.mouseposx+random.randint(2,10)
        mousey=self.mouseposy+random.randint(2,10)
        self.mact+=str(self.mecnt)+",1,"+str(macttime)+","+str(mousex)+","+str(mousey)+";"
        self.mval+=1+macttime+mousex+mousey+self.mecnt
        self.mecnt+=1
        self.totalaction+=macttime
    def domouseclick(self):
        macttime=self.update_t()
        mousex=self.mouseposx+random.randint(2,10)
        mousey=self.mouseposy+random.randint(2,10)
        self.mact+=str(self.mecnt)+",3,"+str(macttime)+","+str(mousex)+","+str(mousey)+",-1;"
        self.mval+=3+macttime+mousex+mousey+self.mecnt
        self.mecnt+=1
        self.totalaction+=macttime
    def dodeviceaction(self):
        doactime=self.doactimes
        self.doact+="0,"+str(doactime)+",-1,-1,-1;"
        self.doval+=doactime
        self.totalaction+=doactime
        dmactime=self.dmactimes
        self.dmact+="0,"+str(dmactime)+",-1,-1,-1,-1,-1,-1,-1,-1,-1;"
        self.dmval+=dmactime
        self.totalaction+=dmactime
        self.futuredelay-=dmactime
    def dokeydown(self):
        kactime=self.update_t()
        self.kact+=str(self.kecnt)+",1,"+str(kactime)+",-2,0,0,-1;"
        self.kval+=kactime+self.kecnt-2
        self.kecnt+=1
        self.kact+=str(self.kecnt)+",3,"+str(kactime)+",-2,0,0,-1;"
        self.kval+=kactime+self.kecnt
        self.kecnt+=1
    def dokeyup(self):
        kactime=self.update_t()
        self.kact+=str(self.kecnt)+",2,"+str(kactime)+",-2,0,0,-1;"
        self.kval+=kactime-1+self.kecnt
        self.kecnt+=1
     */

    private void doMouseAction(){
        Long macttime = update_t();
        int mousex = mouseposx + randInt(2, 10);
        int mousey = mouseposy + randInt(2, 10);
        this.mact += Integer.toString(this.mecnt)+",1,"+Long.toString(macttime)+","+Integer.toString(mousex)+","+Integer.toString(mousey)+";";
        this.mval += 1 + macttime + mousex + mousey + this.mecnt;
        this.mecnt += 1;
        this.totalAction += macttime;
    }

    private void doMouseClick(){
        Long macttime = update_t();
        int mousex = mouseposx + randInt(2, 10);
        int mousey = mouseposy + randInt(2, 10);
        this.mact = Integer.toString(this.mecnt)+",3,"+Long.toString(macttime)+","+Integer.toString(mousex)+","+Integer.toString(mousey)+",-1;";
        this.mval += 3 + macttime + mousex + mousey + this.mecnt;
        this.mecnt += 1;
        this.totalAction += macttime;
    }

    private void doDeviceAction(){
        int doactime =this.doactimes;
        this.doact += "0," + Integer.toString(doactime) + ",-1,-1,-1;";
        this.doval+=doactime;
        this.totalAction+=doactime;
        int dmactime = this.dmactimes;
        this.dmact+="0,"+Integer.toString(dmactime)+",-1,-1,-1,-1,-1,-1,-1,-1,-1;";
        this.dmval+=dmactime;
        this.totalAction+=dmactime;
        this.futuredelay-=dmactime;
    }

    private void doKeyDown(){
        Long kactime= update_t();
        this.kact+=Integer.toString(this.kecnt)+",1,"+Long.toString(kactime)+",-2,0,0,-1;";
        this.kval+=kactime+this.kecnt-2;
        this.kecnt+=1;
        this.kact+=Integer.toString(this.kecnt)+",3,"+Long.toString(kactime)+",-2,0,0,-1;";
        this.kval+=kactime+this.kecnt;
        this.kecnt+=1;
    }

    private void doKeyUp(){
        Long kactime= update_t();
        this.kact+=Integer.toString(this.kecnt)+",2,"+Long.toString(kactime)+",-2,0,0,-1;";
        this.kval+=kactime-1+this.kecnt;
        this.kecnt+=1;
    }

    /*
    def generatesensordata1(self):
        self.dodeviceaction()
        while True:
            i=random.randint(0,1)
            if i==0:
                if len(self.mactimes)>1:
                    b=self.mactimes.pop()
                    self.futuredelay-=b
                    self.domouseaction()
                    continue
            if len(self.kactimes)>0:
                a,b=self.kactimes.pop()
                self.futuredelay-=b
                self.dokeydown()
                self.futuredelay-=a
                self.dokeyup()
                continue
            if len(self.mactimes)>1:
                b=self.mactimes.pop()
                self.futuredelay-=b
                self.domouseaction()
                continue
            break
        b=self.mactimes.pop()
        self.futuredelay-=b
        self.domouseclick()
        return self._generatesensordata1()
     */

    public String generateSensorData1(){
        doDeviceAction();
        while(true){
            int i = randInt(0,1);
            if(i == 0) {
                if (this.mactimes.size() > 1) {
                    Long b = mactimes.get(0);
                    mactimes.remove(0);
                    this.futuredelay -= b;
                    this.doMouseAction();
                    continue;
                }
            }

            if(this.kactimes.size() > 0){

                continue;
            }

            if(this.mactimes.size() > 1){
                Long b = mactimes.get(0);
                mactimes.remove(0);
                this.futuredelay -= b;
                this.doMouseAction();
                continue;
            }
            break;
        }
        Long b = this.mactimes.get(0);
        this.futuredelay -= b;
        this.doMouseClick();
        return this._generateSensorDatat1();
    }

    /*
    def _generatesensordata1(self):
        n=self.gd()
        start=self.get_cf_date()
        t=self.get_cf_date()
        e=self.update_t()
        c=self.cookie
        i="do_en,dm_en,t_en"
        r=self.forminfo
        d=self.url
        s = "1,1"
        u = 25279115
        l = self.get_cf_date()-self.start_ts
        _ = int(int(self.z1/23) / 6)
        v=30261689
        imageval=random.randint(100,998)
        m=str(self.kval)+","+str(self.mval)+",0,"+str(self.doval)+","+str(self.dmval)+",0,"+str(self.mval+self.kval+self.doval+self.dmval)+","+str(e)+",0,"+str(self.start_ts)+","+str(random.randint(1,10))+","+str(int(self.z1/23))+","+str(self.kecnt)+","+str(self.mecnt)+","+str(_)+",1,0,"+str(l)+","+str(self.totalaction)+",0,"+c+","+str(self.ab(c))+","+str(imageval)+","+IMAGEDATA[imageval-100]+","+str(v)
        h="5014"
        g="0,0,0,0,1,0,0"
        sensor_data="1.28-1,2,-94,-100," + n + "-1,2,-94,-101," + i + "-1,2,-94,-105," + "-1,2,-94,-102," + r + "-1,2,-94,-108,"+self.kact+"-1,2,-94,-110," + self.mact + "-1,2,-94,-117," + "-1,2,-94,-111," + self.doact + "-1,2,-94,-109," + self.dmact + "-1,2,-94,-114," + "-1,2,-94,-103," + "-1,2,-94,-112," + d + "-1,2,-94,-115," + m + "-1,2,-94,-106," + s
        sensor_data = sensor_data + "-1,2,-94,-119,80,40,80,60,60,80,60,0,0,0,80,80,100,280,-1,2,-94,-122," + g
        w = self.ab(sensor_data)
        sensor_data = sensor_data + "-1,2,-94,-70,-999120978;dis;,7,8;true;true;true;-330;true;24;24;true;false;-1-1,2,-94,-80," + h + "-1,2,-94,-116," + str(self.o9()) + "-1,2,-94,-118," + str(w) + "-1,2,-94,-121,"
        b=self.od("0a46G5m17Vrp4o4c","afSbep8yjnZUjq3aL010jO15Sawj2VZfdYK8uY90uxq")[:16]
        y=int(self.get_cf_date()/3600000)
        k=self.get_cf_date()
        C=b+self.od(y,b)+sensor_data
        sensor_data=C+";"+str(self.tst)+";"+str(self.get_cf_date()-start+1)+";1"
        return sensor_data
     */

    private String _generateSensorDatat1(){
        String n = this.gd();
        Long start = this.get_cf_date();
        Long t = this.get_cf_date();
        Long e = this.update_t();
        String c = this.cookie;
        String i = "do_en,dm_en,t_en";
        String r = this.formInfo;
        String d = this.url;
        String s = "1,1";
        String u = "25279115";
        Long l = get_cf_date() - start_ts;
        int underscore = (z1/23) / 6;
        String v = "30261689";
        int imageval = randInt(100, 998);
        String m = Integer.toString(this.kval)+","+Integer.toString(this.mval)+",0,"+Integer.toString(this.doval)+","+Integer.toString(this.dmval)+",0,"+Integer.toString(this.mval+this.kval+this.doval+this.dmval)+","+Long.toString(e)+",0,"+Long.toString(this.start_ts)+","+Integer.toString(randInt(1,10))+","+Integer.toString((this.z1/23))+","+Integer.toString(this.kecnt)+","+Integer.toString(this.mecnt)+","+Integer.toString(underscore)+",1,0,"+Long.toString(l)+","+Integer.toString(this.totalAction)+",0,"+c+","+Long.toString(this.AB(c))+","+Integer.toString(imageval)+","+IMAGEDATA[imageval-100]+","+v;
        String h="5014";
        String g="0,0,0,0,1,0,0";

        String sensor_data="1.28-1,2,-94,-100," + n + "-1,2,-94,-101," + i + "-1,2,-94,-105," + "-1,2,-94,-102," + r + "-1,2,-94,-108,"+this.kact+"-1,2,-94,-110," + this.mact + "-1,2,-94,-117," + "-1,2,-94,-111," + this.doact + "-1,2,-94,-109," + this.dmact + "-1,2,-94,-114," + "-1,2,-94,-103," + "-1,2,-94,-112," + d + "-1,2,-94,-115," + m + "-1,2,-94,-106," + s;
        sensor_data = sensor_data + "-1,2,-94,-119,80,40,80,60,60,80,60,0,0,0,80,80,100,280,-1,2,-94,-122," + g;
        Long w = AB(sensor_data);
        sensor_data = sensor_data + "-1,2,-94,-70,-999120978;dis;,7,8;true;true;true;-330;true;24;24;true;false;-1-1,2,-94,-80," + h + "-1,2,-94,-116," + Long.toString(this.o9()) + "-1,2,-94,-118," + Long.toString(w) + "-1,2,-94,-121,";
        String b=this.od("0a46G5m17Vrp4o4c","afSbep8yjnZUjq3aL010jO15Sawj2VZfdYK8uY90uxq").substring(0, 16);
        int y=(int)(this.get_cf_date()/3600000);
        Long k=this.get_cf_date();
        String C=b+this.od(Integer.toString(y),b)+sensor_data;
        sensor_data=C+";"+Long.toString(this.tst)+";"+Long.toString(this.get_cf_date()-start+1)+";1";
        return sensor_data;

    }

}
