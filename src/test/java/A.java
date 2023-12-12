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

		
		

	}

}
