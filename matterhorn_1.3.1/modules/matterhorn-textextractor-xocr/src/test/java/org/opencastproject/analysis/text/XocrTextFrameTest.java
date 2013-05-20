package org.opencastproject.analysis.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.opencastproject.textextractor.api.TextFrame;
import org.opencastproject.textextractor.xocr.XocrTextFrame;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

/**
 * Test case for class {@link XocrTextFrame}.
 */
public class XocrTextFrameTest {

  /** Path to the test frame */
  protected String testFile = "/image.txt";

  /** The test frame */
  protected TextFrame textFrame = null;
  
  /** The text without punctuation */
  protected String text = "Land and Vegetation Key players on the";
  
  /** Number of lines on the frame */
  protected int linesOnFrame = 2;
  
  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
    InputStream is = null;
    try {
      is = getClass().getResourceAsStream(testFile);
      textFrame = XocrTextFrame.parse(is);
    } finally {
      IOUtils.closeQuietly(is);
    }
  }

  /**
   * Test method for {@link org.opencastproject.textextractor.xocr.XocrTextFrame#getLines()}.
   */
  @Test
  public void testGetText() {
    assertEquals(linesOnFrame, textFrame.getLines().length);
    assertEquals(text, textFrame.getLines()[0].getText());
  }

  /**
   * Test method for {@link org.opencastproject.textextractor.xocr.XocrTextFrame#hasText()}.
   */
  @Test
  public void testHasText() {
    assertTrue(textFrame.hasText());
    assertFalse((new XocrTextFrame()).hasText());
  }

}
