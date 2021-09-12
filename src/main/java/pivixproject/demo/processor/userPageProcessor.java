package pivixproject.demo.processor;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Cookie;
import pivixproject.demo.pageModel.pageData;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;


import java.io.File;
import java.util.*;


@Slf4j
public class userPageProcessor implements PageProcessor {

    // current url
    private String url;

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100).setTimeOut(10000);

    private ArrayList<String> data;

    private String userId;

    // cookie information
    private Set<Cookie> cookies;

    public final int REGULAR = 1;
    public final int ORIGINAL = 2;

    public final int DEFAULT_DOWNLOAD_LIMIT = 60000000;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public userPageProcessor(String userId, Set<Cookie> cookies) {
        this.url = "https://www.pixiv.net/ajax/user/" + userId + "/profile/all";
        data = new ArrayList<>();
        this.cookies = cookies;
        this.userId = userId;
    }

    public void changeUserId(String newId){
        this.url = "https://www.pixiv.net/ajax/user/" + newId + "/profile/all";
        this.data.clear();
        this.userId = newId;
    }


    // activate the spider
    public void collect() {
        Request request = new Request(url);
        Spider.create(this).thread(1).addRequest(request).run();
    }

    public ArrayList<String> getPageData() {
        return this.data;
    }

    // process the page
    @Override
    public void process(Page page) {
        String text = page.getRawText();
        text = text.substring(text.indexOf("illusts") + 9, text.length() - 1);
        text = text.substring(0, text.indexOf("}") + 1);
        HashMap<String, Object> dataMap = JSON.parseObject(text, HashMap.class);
        Set<String> keySet = dataMap.keySet();
        data.addAll(keySet);
        log.info("Get " + data.size() + " objects");
    }

    @Override
    public Site getSite() {
        for (Cookie cookie : cookies) {
            site.addCookie(cookie.getName().toString(),cookie.getValue().toString());
        }

        return site.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1");
    }

    public void downloadAll(int type, String savePath, int downLoadLimit) throws Exception {
        picturePageProcessor demo = new picturePageProcessor("0");
        savePath = savePath + "\\"+ userId;
        int count = 0;
        for (String datum : data) {
            if(Integer.parseInt(datum) <= downLoadLimit){
                continue;
            }
            demo.setUrl(datum);
            demo.collect();
            pageData data = demo.getPageData();
            switch (type) {
                case REGULAR:
                    demo.download(data.getRegularSizeUrl(), savePath);
                    break;
                case ORIGINAL:
                    demo.download(data.getOriginalSizeUrl(), savePath);
                    break;
            }

            Thread.sleep(1000);
            count++;
        }
        log.info("Download " + count + " pictures in total");
    }

    public void updateNewPicture(int type, String savePath) throws Exception{
        String dirPath = savePath + "\\" + userId;
        File file = new File(dirPath);

        File[] existPictures = file.listFiles();

        int maxLimit = 0;
        if(existPictures != null){
            for(File temp: existPictures){
                String fileName = temp.getName();
                fileName = fileName.substring(0,fileName.length()-4);
                int number = Integer.parseInt(fileName);

                if(number > maxLimit){
                    maxLimit = number;
                }
            }
        }
        downloadAll(type,savePath,maxLimit);
    }
}
