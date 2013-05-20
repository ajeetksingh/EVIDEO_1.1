package org.opencastproject.analysis.text;

import static org.junit.Assert.assertEquals;

import org.opencastproject.textextractor.api.TextLine;
import org.opencastproject.textextractor.xocr.XocrLine;

import org.junit.Before;
import org.junit.Test;

/**
 * Test case for class {@link XocrLine}.
 */
public class XocrTextLineTest {

  /** The text item */
  protected TextLine textItem = null;

  /** The text */
  protected String text = "Hello world";

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
    textItem = new XocrLine(text);
  }

  /**
   * Test method for {@link org.opencastproject.textextractor.xocr.XocrLine#getText()}.
   */
  @Test
  public void testGetText() {
    assertEquals(text, textItem.getText());
  }

}
