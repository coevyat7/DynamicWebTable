import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class Main {
    static WebDriver driver;
    static String url="https://demo.opencart.com/admin/";
    public static void main(String[] args) throws InterruptedException {
        invokeBrowser();
        login("demo","demo");
        closeWarningMsg();
        gotoSalesPage();
       int x= getNumOfPages();
       printTable(x);



        Thread.sleep(2500);
    }

    private static void printTable(int pages) throws InterruptedException {
        for(int i=1;i<=pages;i++) {
            Thread.sleep(1500);
            WebElement page_active = driver.findElement(By.xpath("//ul[@class='pagination']//li//span"));
            System.out.println("Active Page: " + page_active.getText());
           page_active.click();
            int rows = driver.findElements(By.cssSelector("table.table-bordered>tbody>tr")).size();
            System.out.println("Num of Rows: " + rows);
            for (int r = 1; r <= rows; r++) {
                String orderID = driver.findElement(By.xpath("//table[@class]//tbody//tr[" + r + "]//td[2]")).getText();
                String customer = driver.findElement(By.xpath("//table[@class]//tbody//tr[" + r + "]//td[4]")).getText();
                String status = driver.findElement(By.xpath("//table[@class]//tbody//tr[" + r + "]//td[5]")).getText();
                System.out.println("Order Id:" + orderID + "," + "CustomerId: " + customer + "," + "status:" + status);
            }

            String pg = Integer.toString(i + 1);
            if(Integer.valueOf(pg)>pages){
                break;
            }
            driver.findElement(By.xpath("//ul[@class='pagination']//li//a[text()='" + pg + "']")).click();
        }


    }

    private static int getNumOfPages() {
       String page= driver.findElement(By.cssSelector("div[class*='col-sm-6 text-end']")).getText();
        int total_pages=Integer.valueOf(page.substring(page.indexOf("(")+1,page.indexOf("Pages")-1));
        return total_pages;

    }

    public  static void gotoSalesPage() {
        driver.findElement(By.id("menu-sale")).click();
        driver.findElement(By.cssSelector("ul#collapse-4>li:first-of-type>a")).click();
    }

    public static void closeWarningMsg() {
        WebElement element=driver.findElement(By.cssSelector("button[class='btn-close']"));
        new WebDriverWait(driver,Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(element));
        element.click();

    }

    private static void invokeBrowser() {
        try{
            driver= WebDriverManager.chromedriver().create();
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
            driver.manage().deleteAllCookies();
            driver.get(url);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void login(String val1,String val2){
        driver.findElement(By.id("input-username")).sendKeys(val1);
        driver.findElement(By.id("input-password")).sendKeys(val2);
        driver.findElement(By.cssSelector("button[type]")).click();
    }
}
