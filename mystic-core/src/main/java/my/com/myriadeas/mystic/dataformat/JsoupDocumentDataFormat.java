package my.com.myriadeas.mystic.dataformat;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.camel.Exchange;
import org.apache.camel.spi.DataFormat;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JsoupDocumentDataFormat implements DataFormat {

	@Override
	public void marshal(Exchange exchange, Object graph, OutputStream stream)
			throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public Object unmarshal(Exchange exchange, InputStream stream)
			throws Exception {
		Document document = Jsoup.parse(stream, "UTF-8", "www.google.com");
		
		return document;
	}
	
	
}
