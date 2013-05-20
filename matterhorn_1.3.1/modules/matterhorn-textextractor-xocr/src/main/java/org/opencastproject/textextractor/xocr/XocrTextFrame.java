package org.opencastproject.textextractor.xocr;

import org.opencastproject.textextractor.api.TextFrame;
import org.opencastproject.textextractor.api.TextLine;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a tesseract output frame that holds a number of lines found on an image. Note that Tesseract
 * does not inlcude positioning or other information with the text output.
 */
public class XocrTextFrame implements TextFrame {

  /** Words found on an output frame */
  protected ArrayList<TextLine> lines = new ArrayList<TextLine>();

  /**
   * Parses the tesseract output file and extracts the text information contained therein.
   * 
   * @param is
   *          the input stream
   * @return the ocropus text information
   * @throws IOException
   *           if reading the ocropus output fails
   */
  public static TextFrame parse(InputStream is) throws IOException {
    String ocropusFrame = new String(IOUtils.toByteArray(is));
    String[] lines = StringUtils.split(ocropusFrame, '\n');
    XocrTextFrame textFrame = new XocrTextFrame();
    for (String line : lines) {
      List<String> words = new ArrayList<String>();
      for (String word : StringUtils.split(line)) {
        String result = word.replaceAll("^[\\W]*|[\\W]*$", "");
        if (StringUtils.isNotBlank(result)) {
          words.add(result);
        }
      }
      if (words.size() == 0) {
        continue;
      }
      XocrLine ocrLine = new XocrLine(StringUtils.join(words.toArray(new String[words.size()]), ' '));
      textFrame.lines.add(ocrLine);
    }

    return textFrame;
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.textextractor.api.TextFrame#hasText()
   */
  @Override
  public boolean hasText() {
    return lines.size() > 0;
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.textextractor.api.TextFrame#getLines()
   */
  @Override
  public TextLine[] getLines() {
    return lines.toArray(new XocrLine[lines.size()]);
  }

}
