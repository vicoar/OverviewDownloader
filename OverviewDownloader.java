import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.util.Map;
import java.net.URL;
import java.nio.channels.*;
import java.io.*;

class OverviewDownloader {

	public static void main(String [] args){
		try {
			String base = "http://www.overv.eu";
			Document doc = Jsoup.connect(base).get();
			Elements pages = doc.select("#footer-navigation li a");

			for(int i=0; i < pages.size(); i++ ) {
				Element page = pages.get(i);
				Attributes attrs = page.attributes();
				getImages(base + attrs.get("href"));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void getImages(String url){
		System.out.println("GETTING IMAGES FROM "+url);
		try {
			Document doc = Jsoup.connect(url).get();
			Elements images = doc.select(".lightbox img.thumb-image");
			for(int i=0; i < images.size(); i++ ) {
				Element img = images.get(i);
				Map<String,String> data = img.dataset();

				if ( data.containsKey("image") ) {
					//
					String filename = data.get("image-id") + ".jpg";
					File f = new File(filename);
					if( ! f.exists() ) {
						URL website = new URL(data.get("image")+"?format=1500w");
						ReadableByteChannel rbc = Channels.newChannel(website.openStream());
						FileOutputStream fos = new FileOutputStream(filename);
						fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
}

