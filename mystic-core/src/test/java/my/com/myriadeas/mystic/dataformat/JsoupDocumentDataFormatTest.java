package my.com.myriadeas.mystic.dataformat;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;

import org.junit.Test;

public class JsoupDocumentDataFormatTest {

	@Test
	public void test() {
		JsoupDocumentDataFormat jsoupFormatter = new JsoupDocumentDataFormat();
		ByteArrayInputStream stream = new ByteArrayInputStream("abc".getBytes());
		try {
			System.out.println(jsoupFormatter.unmarshal(null, stream));
			fail("Should fail.");
		} catch (Exception e) {
			if (!e.getClass().equals(IllegalArgumentException.class)){
				fail("Should be IllegalArgumentException.");
			}
		}
	}


}
