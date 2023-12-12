import java.util.Hashtable;

import org.testng.annotations.Test;

import com.hm.annotations.FrameworkAnnotation;
import com.hm.seleniumFactory.BaseTest;
import com.hm.seleniumFactory.SeleniumWrapper;
import com.hm.utilities.DataUtil;

public class A extends BaseTest {

	@FrameworkAnnotation(dataSheetName = "TMF_MANAGER")
	@Test(dataProvider = "Data", dataProviderClass = DataUtil.class)
	public void name(Hashtable<String, Object> data) {

		SeleniumWrapper.openWebsite(
				"https://qa.kraios.tmf-group.com/login?id_token=eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJzYXRpc2h2bWFuanVuYXRoQGdtYWlsLmNvbSIsImlzcyI6Im9uZXRtZi1nYWx2YW5pemUiLCJleHAiOjE5Mjc1NDE5MTAsImlhdCI6MTY1NjUwNTExMCwiZW1haWwiOiJzYXRpc2h2bWFuanVuYXRoQGdtYWlsLmNvbSJ9.YTU-6TmFtcLqcUbmIPSfMu4sBUHBzTPjDdnsT0MzbYYQFxJNYyAgX3EYkuH-8I3LzY9x6YrNtGi1_G1Mq7ShLnKC6o2TQ1k_iHtuGKffQl3IWvo20JqNScOCHYq6K1HIpX1pl4H_jqI6l6H7qO9eDmMLu5vwkTHbpI7bg_zWSr3A1qI5U7kdLPWuN7Pcnse_1_tD11cPlBCRV9I5Z3295nUlwaXkdfRhkLfFJPJYkA2Si7E-4l_Osh4QUscunkYavDn7Vbh75P3XigwuCqOiOwqeroSpXAO_JBHChRUNnlCPFquwtFdZ82o7fH5p9eKwpPQ-9rb1Gesf61OtiJbnKg",
				"KraiosQA");

		SeleniumWrapper.isVisible("//p[@class='welcome-text']", "WelcomeText");

		SeleniumWrapper.isNotVisible("//li[@routerlink='/workflow']", "WorkFlow");
		
		SeleniumWrapper.isVisible("//li[@routerlink='/workflow']", "WorkFlow");
	}

}
