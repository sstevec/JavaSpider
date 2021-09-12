package pivixproject.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pivixproject.demo.pageModel.pageData;
import pivixproject.demo.processor.loginPageProcessor;
import pivixproject.demo.processor.picturePageProcessor;
import pivixproject.demo.processor.textPageProcessor;
import pivixproject.demo.processor.userPageProcessor;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

@SpringBootTest
@Slf4j
class DemoApplicationTests {

    // 13992671    544479     23098486
    @Test
    void projectStart(){
        loginPageProcessor loginPageProcessor = new loginPageProcessor();
        loginPageProcessor.login();
        userPageProcessor userPage = new userPageProcessor("23098486",loginPageProcessor.getCookies());
        userPage.collect();
        try{
            //userPage.updateNewPicture(userPage.REGULAR,"D:\\pcrData\\autoCatch");
            userPage.downloadAll(userPage.REGULAR,"D:\\pcrData\\autoCatch",userPage.DEFAULT_DOWNLOAD_LIMIT);
        }catch (Exception e){
            log.warn(e.getMessage());
        }
        loginPageProcessor.logOut();

    }


    void downloadPictureById(String id){
        picturePageProcessor demo = new picturePageProcessor(id);
        demo.collect();
        pageData data = demo.getPageData();
        demo.download(data.getRegularSizeUrl(),"D:\\pcrData\\autoCatch");
    }

    // start 48741055
    @Test
    void downloadText(){
        File file = null;
        FileWriter fw = null;
        file = new File("D:\\pcrData\\丁长生.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fw = new FileWriter(file);
            long id = 48741055;
            textPageProcessor t = new textPageProcessor(""+id);

            for(int i = 1;i <=3000;i++){
               t.collect();

               fw.write("\n\n第" + i + "章\n\n");
               fw.write(t.textData);
               fw.flush();
//               try {
//                   Thread.sleep(1000);
//               }catch (Exception e){
//                   System.out.println("sleep error");
//               }
               t.setUrl("" + (id+i));
            }
            System.out.println("写数据成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(fw != null){
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
