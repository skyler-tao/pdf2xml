package skyler.tao.pdf2xml.xml;

import java.util.List;

public class TestRead {
	public static void main(String args[]) {
		StaXParser read = new StaXParser();
		List<Item> readConfig = read.readConfig("config.xml");
		for (Item item : readConfig) {
			System.out.println(item);
		}
	}
}
