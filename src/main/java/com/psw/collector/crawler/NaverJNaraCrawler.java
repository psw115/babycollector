package com.psw.collector.crawler;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NaverJNaraCrawler {
	// WebDriver
	private WebDriver driver;

	// Properties
	public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
	public static final String WEB_DRIVER_PATH = "s:/chromedriver.exe";

	// 크롤링 할 URL
	private String base_url;

	public NaverJNaraCrawler() {
		super();
		base_url = "https://www.daum.net";
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

		// System Property SetUp
		ChromeOptions options = new ChromeOptions();
		// options.setHeadless(true);
		// options.addArguments("headless");
		// options.addArguments("--headless");
		// Driver SetUp
		options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36");
		options.addArguments("disable-gpu");
		options.addArguments("window-size=1920x1080");
		options.addArguments("lang=ko_KR");
		options.addArguments("--blink-settings=imagesEnabled=false");//image do not download
		driver = new ChromeDriver(options);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("Object.defineProperty(navigator, 'plugins', {get: function() {return[1, 2, 3, 4, 5];},});");
		js.executeScript("Object.defineProperty(navigator, 'languages', {get: function() {return ['ko-KR', 'ko']}})");
		js.executeScript("const getParameter = WebGLRenderingContext.getParameter;WebGLRenderingContext.prototype.getParameter = function(parameter) {if (parameter === 37445) {return 'NVIDIA Corporation'} if (parameter === 37446) {return 'NVIDIA GeForce GTX 980 Ti OpenGL Engine';}return getParameter(parameter);};");

	}
	
	private void printInit() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		System.out.println(js.executeScript("return navigator.userAgent;"));
//		System.out.println(driver.findElement(By.cssSelector("#user-agent")).getText());
//		System.out.println(driver.findElement(By.cssSelector("#plugins-length")).getText());
//		System.out.println(driver.findElement(By.cssSelector("#languages")).getText());
//		System.out.println(driver.findElement(By.cssSelector("#webgl-vendor")).getText());
//		System.out.println(driver.findElement(By.cssSelector("#webgl-renderer")).getText());
	}
	
	public void crawl(String w) {

		try {
			//암시적대기
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

			// get page (= 브라우저에서 url을 주소창에 넣은 후 request 한 것과 같다)
			driver.get(base_url);
			//System.out.println(driver.getPageSource());
			JavascriptExecutor js = (JavascriptExecutor) driver;
			printInit();			
			//중고나라로 이동
			driver.navigate().to("https://cafe.naver.com/joonggonara");
			String searchInputEl = "#topLayerQueryInput";
			
			//검색필드 찾기
			WebElement el = driver.findElement(By.cssSelector(searchInputEl));
			if(el == null) {
				System.out.println("Can't Find " + searchInputEl);
				return;
			}
			
			//검색어 + 엔터
			el.sendKeys(w);
			el.sendKeys(Keys.RETURN);
			
			//결과 확인
//			Thread.sleep(5000);
//			new WebDriverWait(driver, 4).until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));			
			driver.switchTo().frame("cafe_main"); // 결과 iframe으로 전환
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(ExpectedConditions.visibilityOfElementLocated((By.cssSelector("#query")))); // iframe 로딩이 완료 될 때 까지 기다리기

 /*
 		css selector
 		* #main-area > div:nth-child(7) > table > tbody > tr:nth-child(2) > td.td_article > div.board-list > div > a.article
 		xpath
 		//*[@id="main-area"]/div[5]/table/tbody/tr[2]/td[1]/div[2]/div/a[1]
 		full xpath
 		/html/body/div[1]/div/div[5]/table/tbody/tr[2]/td[1]/div[2]/div/a[1]			
 */
//			List<WebElement> list = driver.findElements(By.xpath(("//*[@id=\"main-area\"]/div[5]/table/tbody//a[@class='article']")));
			List<WebElement> list = driver.findElements(By.xpath(("//*[@id=\"main-area\"]/div[5]/table/tbody/child::tr")));
			
			if(list == null || list.size() == 0) {
				System.out.println("No Result for " + w);
				return;
			}
			System.out.println("SIZE : " + list.size());
			for(int i = 0; i < list.size(); i++) {				
//				el = list.get(i);
				el = list.get(i).findElement(By.xpath("//*[@id=\"main-area\"]/div[5]/table/tbody/tr["+(i+1)+"]/td[1]/div[2]/div/a[1]"));
				System.out.println(el.getAttribute("href"));
				System.out.println(el.getText());
			}
			
//			Thread.sleep(5000);
		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			driver.close();
		}

	}
	

	
	public static void main(String args[]) {
		System.out.println("STARTING....1");
		NaverJNaraCrawler ex = new NaverJNaraCrawler();
		ex.crawl("xnote");
		

	}

}
