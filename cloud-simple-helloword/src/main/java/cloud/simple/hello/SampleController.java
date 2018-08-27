package cloud.simple.hello;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@SpringBootApplication
public class SampleController  {
	
	volatile static int n=0;
	volatile static int m=0;
	
	Executor executor = Executors.newFixedThreadPool(10); 

    @ResponseBody
    @RequestMapping(value = "/hello/{nPath}")
    String home(@PathVariable(value = "nPath") int nPath ) {   
    	
    	final int sPath=nPath;
    	
    	Runnable task = new Runnable() {  
    	    @Override  
    	    public void run() {  
    	    	System.out.println("hello当前线程："+Thread.currentThread().getName());
    	    	System.out.println("hello请求数量："+(sPath));
    	    	try {
    				Thread.sleep(500);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    	    	System.out.println("hello处理完成："+(sPath));
    	    }  
    	};  
    	executor.execute(task); 
    	
    	
        return "Hello World!"+n;
    }
    
    @ResponseBody
    @RequestMapping(value = "/user")
    String user() {    
    	
    	System.out.println("user当前线程："+Thread.currentThread().getName());
    	System.out.println("user请求数量："+(++m));
    	
    	System.out.println("user处理完成："+(m));
        return "Hello User!"+m;
    }
    
    @ResponseBody
    @RequestMapping(value = "/test")
    String test() throws IOException { 
    	String text = null;
//    	String text = getOneHtml("http://www.sci99.com/");
//    	baidu();
    	text = test1();
        return text;
    }

//    public static void main(String[] args) throws Exception {
//        SpringApplication.run(SampleController.class, args);
//    }
    
    public String test1(){
    	Document document = null;
        try {
            //解析Url获取Document对象
        	document = Jsoup.connect("http://www.sci99.com/").get();
//        	System.out.println(document);
//------------------------------------------------------------------------------------------
        	 Element singerListDiv = document.getElementsByAttributeValue("class", "tab_spas").first(); 
             Elements spanElemet = singerListDiv.getElementsByTag("span"); 
              
             List<String> tempElementList = new ArrayList<String>();
             for (Element link: spanElemet) { 
                 String linkText = link.text().trim(); 
//                 System.out.print(linkText+"            ");
                 tempElementList.add(linkText);
             }
//             System.out.println("------------------商品价格信息--------------------------");             
             Element priceListDiv = document.getElementsByAttributeValue("class", "tab_spas1").first(); 
             Elements spanPrice = priceListDiv.getElementsByTag("span"); 
             
             List<String> tempPriceInfoList = new ArrayList<String>();
             for (Element link: spanPrice) { 
                 String linkText = link.text().trim(); 
//                 System.out.println(linkText);
                 tempPriceInfoList.add(linkText);
             }
             
             Element price2ListDiv = document.getElementsByAttributeValue("class", "tab_spas1").get(1); 
             Elements spanPrice2 = price2ListDiv.getElementsByTag("span"); 
             for (Element link: spanPrice2) { 
                 String linkText = link.text().trim(); 
//                 System.out.println(linkText);
                 tempPriceInfoList.add(linkText);
             }
             
             for(int i=0;i<tempElementList.size();i++){
            	 String m = tempElementList.get(i);
            	 for(int j=0;j<tempPriceInfoList.size();j++){
            		 String n = tempPriceInfoList.get(i);
            		 String z = m+":"+n;
            		 System.out.println(z);
            	 }
             }
             
        } catch (IOException e) {
            System.out.println("解析出错！");
            e.printStackTrace();
        }
		return document.toString();
    }
    
    /** *//**
     * 读取一个网页全部内容
     */
    public String getOneHtml(String htmlurl) throws IOException{
      URL url;
      String temp;
      StringBuffer sb = new StringBuffer();
      try {
        url = new URL(htmlurl);
        BufferedReader in = new BufferedReader(new InputStreamReader(url
            .openStream(), "utf-8"));// 读取网页全部内容
        while ((temp = in.readLine()) != null) {
          sb.append(temp);
        }
        in.close();
      }catch(MalformedURLException me){
        System.out.println("你输入的URL格式有问题！请仔细输入");
        me.getMessage();
        throw me;
      }catch (IOException e) {
        e.printStackTrace();
        throw e;
      }
      return sb.toString();
    }
    /** *//**
     *
     * @param s
     * @return 获得网页标题
     */
    public String getTitle(String s) {
      String regex;
      String title = "";
      List<String> list = new ArrayList<String>();
      regex = "<title>.*?</title>";
      Pattern pa = Pattern.compile(regex, Pattern.CANON_EQ);
      Matcher ma = pa.matcher(s);
      while (ma.find()) {
        list.add(ma.group());
      }
      for (int i = 0; i < list.size(); i++) {
        title = title + list.get(i);
      }
      return outTag(title);
    }
    /** *//**
     *
     * @param s
     * @return 获得链接
     */
    public List<String> getLink(String s) {
      String regex;
      List<String> list = new ArrayList<String>();
//      regex = "<a[^>]*href=("([^"]*)"|'([^']*)'|([^s>]*))[^>]*>(.*?)</a>";
//      Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
//      Matcher ma = pa.matcher(s);
//      while (ma.find()) {
//        list.add(ma.group());
//      }
      return list;
    }
    /** *//**
     *
     * @param s
     * @return 获得脚本代码
     */
    public List<String> getScript(String s){
      String regex;
      List<String> list = new ArrayList<String>();
      regex = "<script.*?</script>";
      Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
      Matcher ma = pa.matcher(s);
      while (ma.find()){
        list.add(ma.group());
      }
      return list;
    }
    /** *//**
     *
     * @param s
     * @return 获得CSS
     */
    public List<String> getCSS(String s){
      String regex;
      List<String> list = new ArrayList<String>();
      regex = "<style.*?</style>";
      Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
      Matcher ma = pa.matcher(s);
      while (ma.find()) {
        list.add(ma.group());
      }
      return list;
    }
    /** *//**
     *
     * @param s
     * @return 去掉标记
     */
    public String outTag(String s) {
      return s.replaceAll("<.*?>", "");
    }
    
    public String baidu() throws IOException{
    	URL url= new URL("http://www.baidu.com/s?wd=000897");  
    	URLConnection urlConnection = url.openConnection();  
    	BufferedReader br=new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));  
    	String str="";  
    	StringBuffer content=new StringBuffer();  
    	while((str=br.readLine())!=null){  
    		content.append(str);  
    	}  
    	br.close();  
    	String regx1="<b style=\"font-size: 1.4em; text-align:center;color:#.*;\">(.*?)</b>(.*?)<b style=\"font-size: 1.1em; color:#.*;\">(.*?)</b>(.*?)开盘:</td> <td style=\"(.*?)\">(.*?)</td>(.*?)<td style=\"  color:#.*;width:91px;\">(.*?)</td>(.*?)<td style=\"  color:(.*?);width:91px;\">(.*?)</td>";  
    	Pattern p= Pattern.compile(regx1);  
    	String text=content.toString();  
    	Matcher  macher =p.matcher(text);  
    	while(macher.find()){  
    		System.out.println("现价："+macher.group(1).trim());  
    		System.out.println("幅度："+macher.group(3).trim());  
    		System.out.println("开盘价："+macher.group(6).trim());  
    		System.out.println("最高："+macher.group(8).trim());  
    		System.out.println("最低："+macher.group(11).trim());  
    	}
		return null;  
    }


}
