package pivixproject.demo.processor;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import pivixproject.demo.pageModel.pageData;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class picturePageProcessor implements PageProcessor {

    // 当前处理的url
    private String url;

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100).setTimeOut(10000);

    private pageData data;

    private String id;

    public String getUrl() {
        return url;
    }

    public void setUrl(String newId) {
        this.url = "https://www.pixiv.net/artworks/"+newId;
        this.id = newId;
    }

    public picturePageProcessor(String id){
        this.id= id;
        this.url = "https://www.pixiv.net/artworks/"+id;
        data = new pageData();
    }

    // 激活处理器
    public void collect(){
        Request request = new Request(url);
        Spider.create(this).thread(1).addRequest(request).run();
    }

    public pageData getPageData(){
        return this.data;
    }


    // process the page
    @Override
    public void process(Page page) {
        String text = page.getRawText();
        text = text.substring(text.indexOf("urls")+6,text.length()-1);
        text = text.substring(0,text.indexOf("}")+1);
        HashMap<String,Object> content = JSON.parseObject(text, HashMap.class);
        data.setRawData(text);
        data.setOriginalSizeUrl(content.get("original").toString());
        data.setRegularSizeUrl(content.get("regular").toString());
    }

    @Override
    public Site getSite() {
        return this.site;
    }


    public void download(String urlString ,String savePath){
        try{
            String fileType = urlString.substring(urlString.length()-3,urlString.length());
            downloadCore(urlString,id,id+"."+fileType,savePath);
        }catch (Exception e){
            log.warn(e.getMessage());
        }
    }

    private void downloadCore(String urlString,String id ,String filename,String savePath) throws Exception {
        // build URL
        URL url = new URL(urlString);
        // open the link
        URLConnection con = url.openConnection();
        // set timeout interval
        con.setConnectTimeout(5*1000);
        con.setRequestProperty("Referer","http://www.pixiv.net/member_illust.php"+"?mode=medium&illust_id="+id);
        // input stream
        InputStream is = con.getInputStream();

        // buffer
        byte[] bs = new byte[1024];
        // data length
        int len;
        // file stream
        File sf=new File(savePath);
        if(!sf.exists()){
            sf.mkdirs();
        }
        OutputStream os = new FileOutputStream(sf.getPath()+"\\"+filename);
        // start reading
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // close all the stream
        os.close();
        is.close();
    }
}
