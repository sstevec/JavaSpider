package pivixproject.demo.processor;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.Set;

@Slf4j
public class loginPageProcessor {

    private final WebDriver driver;
    private Set<Cookie> cookies;

    public loginPageProcessor(){
        this.driver = new ChromeDriver();
    }


    //use selenium to simulate login
    public void login()
    {
        this.driver.get("https://accounts.pixiv.net/login?lang=zh");
        try {

            // fill user name field
            WebDriverWait wait = new WebDriverWait(this.driver, 10);
            WebElement userNameElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@autocomplete=\"username\"]")));
            userNameElement.click();
            userNameElement.clear();
            userNameElement.sendKeys("Your user name");


            // fill password field
            WebDriverWait wait2 = new WebDriverWait(this.driver, 10);
            WebElement passwordElement = wait2.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@autocomplete=\"current-password\"]")));
            passwordElement.click();
            passwordElement.clear();
            passwordElement.sendKeys("Your password");


            // click login button
            WebDriverWait wait3 = new WebDriverWait(this.driver, 10);
            WebElement buttonElement = wait3.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='LoginComponent']/form/button[@class=\"signup-form__submit\"]")));
            buttonElement.click();

            Thread.sleep(15000);
        }catch (Exception e){
            log.info(e.getMessage());
            return;
        }
        // get cookie
        cookies = driver.manage().getCookies();


    }

    public void logOut(){
        driver.close();
    }

    public Set<Cookie> getCookies() {
        return cookies;
    }

    public void setCookies(Set<Cookie> cookies) {
        this.cookies = cookies;
    }
}
