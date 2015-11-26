package skyler.tao.pdf2xml;

import java.util.HashMap;
import java.util.LinkedList;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.itextpdf.text.pdf.parser.Vector;

public class SemTextExtractionStrategy implements TextExtractionStrategy {

	private StringBuilder result = new StringBuilder();
//	private String text;
	private Vector lastBaseLine;
	private String lastFont;
	private float lastFontSize;
	private LinkedList<HashMap<String, HashMap<String, Float>>> resultList;

	private enum TextRenderMode {
		FillText(1), 
		StrokeText(2), 
		FillThenStrokeText(3), 
		Invisible(4), 
		FillTextAndAddToPathForClipping(5), 
		StrokeTextAndAddToPathForClipping(6), 
		FillThenStrokeTextAndAddToPathForClipping(7), 
		AddTextToPaddForClipping(8);
		
		private final int value;
		private TextRenderMode(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
	}

	public void beginTextBlock() {
	}

	public void renderText(TextRenderInfo renderInfo) {

		String curFont = renderInfo.getFont().getPostscriptFontName();  //font name
		// Check if faux bold is used
		if ((renderInfo.getTextRenderMode() == TextRenderMode.FillThenStrokeText.getValue())) {
			curFont += "-Bold";
		}

		// This code assumes that if the baseline changes then we're on a
		// newline
		Vector curBaseline = renderInfo.getBaseline().getStartPoint();
		Vector topRight = renderInfo.getAscentLine().getEndPoint();
		float llx = curBaseline.get(0);
		float lly = curBaseline.get(1);
		float urx = topRight.get(0);
		float ury = topRight.get(1);
		Rectangle rect = new Rectangle(llx, lly, urx, ury);
		float curFontSize = rect.getHeight();

		// See if something has changed, either the baseline, the font or the
		// font size
		if ((this.lastBaseLine == null)
				|| (curBaseline.I2 != lastBaseLine.I2)
				|| (curFontSize != lastFontSize) || (curFont != lastFont)) {
			// if we've put down at least one span tag close it
			if ((this.lastBaseLine != null)) {
				this.result.append("</span>\n");
			}
			// If the baseline has changed then insert a line break
			if ((this.lastBaseLine != null)
					&& curBaseline.I2 != lastBaseLine.I2) {
				this.result.append("<br />");
			}
			// Create an HTML tag with appropriate styles
			this.result.append(String.format(
					"<span style=\"font-family:{%s};font-size:{%f}\">", curFont,
					curFontSize));
		}

		// Append the current text
		this.result.append(renderInfo.getText());

		// Set currently used properties
		this.lastBaseLine = curBaseline;
		this.lastFontSize = curFontSize;
		this.lastFont = curFont;
//		text = renderInfo.getText();

//		System.out.println(renderInfo.getFont().getFontType());

//		System.out.print(text);
	}

	public void endTextBlock() {
	}

	/*
	 * public void renderImage(ImageRenderInfo renderInfo) { }
	 */

	public String getResultantText() {
		if (result.length() > 0) {
			result.append("</span>");
		}
		return result.toString();
	}

	public void renderImage(ImageRenderInfo renderInfo) {
		// TODO Auto-generated method stub

	}
}
