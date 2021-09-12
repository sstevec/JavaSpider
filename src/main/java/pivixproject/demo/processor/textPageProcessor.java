package pivixproject.demo.processor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import pivixproject.demo.pageModel.pageData;
import pivixproject.demo.util.myHttpClientDownloader;
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

@Slf4j
public class textPageProcessor implements PageProcessor {

    // 当前处理的url
    private String url;

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100).setTimeOut(10000);

    private String id;

    public String getUrl() {
        return url;
    }

    public String textData;

    public void setUrl(String newId) {
        this.url = "https://www.bookbao.org/book/51/51193/" + newId+ ".html";
        this.id = newId;
    }

    public textPageProcessor(String id){
        this.id= id;
        this.url = "https://www.bookbao.org/book/51/51193/" + id + ".html";
    }

    // active the spider
    public void collect(){
        Request request = new Request(url);
        Spider.create(this).setDownloader(new myHttpClientDownloader()).thread(1).addRequest(request).run();
    }


    // process the page
    @Override
    public void process(Page page) {
        String text = page.getRawText();
        text = text.substring(text.indexOf("id=\"content\"")+13,text.length()-1);
        text = text.substring(0,text.indexOf("</div>"));
        text = StrUtil.replace(text,"<br/>","\n");
        text = StrUtil.replace(text,"&nbsp;","");
        textData = text;
    }

    @Override
    public Site getSite() {
        return this.site;
    }

}
