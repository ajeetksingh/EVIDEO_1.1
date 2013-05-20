package org.opencastproject.analysis.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.opencastproject.textextractor.api.TextFrame;
import org.opencastproject.textextractor.xocr.XocrTextExtractor;
import org.opencastproject.util.IoSupport;
import org.opencastproject.util.StreamHelper;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;

/**
 * Test case for class {@link XocrTextExtractor}.
 */
public class XocrTextExtractorTest {

  /** Path to the test image */
  protected String testPath = "/image.tiff";

  /** Test image */
  protected File testFile = null;

  /** Path to the xocreract binary */
  protected static String xocrbinary = XocrTextExtractor.XOCR_BINARY_DEFAULT;

  /** The xocr text analyzer */
  protected XocrTextExtractor analyzer = null;
  
  /** The text without punctuation */
  protected String text = "Land and Vegetation Key players on the";

  /** True to run the tests */
  private static boolean xocrInstalled = true;
  
  /** Logging facility */
  private static final Logger logger = LoggerFactory.getLogger(XocrTextExtractorTest.class);

  @BeforeClass
  public static void testXocr() {
    StreamHelper stdout = null;
    StreamHelper stderr = null;
    Process p = null;
    try {
      p = new ProcessBuilder(xocrbinary).start();
      stdout = new StreamHelper(p.getInputStream());
      stderr = new StreamHelper(p.getErrorStream());
      int status = p.waitFor();
      if (status != 1)
        throw new IllegalStateException();
    } catch (Throwable t) {
      logger.warn("Skipping text analysis tests due to unsatisifed XOCR installation");
      logger.warn(t.getMessage(), t);
      xocrInstalled = false;
    } finally {
      IoSupport.closeQuietly(stdout);
      IoSupport.closeQuietly(stderr);
      IoSupport.closeQuietly(p);
    }
  }

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
    URL imageUrl = this.getClass().getResource(testPath);
    testFile = File.createTempFile("ocrtest", ".jpg");
    FileUtils.copyURLToFile(imageUrl, testFile);
    analyzer = new XocrTextExtractor(xocrbinary);
  }

  /**
   * @throws java.io.File.IOException
   */
  @After
  public void tearDown() throws Exception {
    FileUtils.deleteQuietly(testFile);
  }

  /**
   * Test method for {@link org.opencastproject.textextractor.xocr.XocrTextExtractor#getBinary()}.
   */
  @Test
  public void testGetBinary() {
    assertEquals(xocrbinary, analyzer.getBinary());
  }

  /**
   * Test method for {@link org.opencastproject.textextractor.xocr.XocrTextExtractor#analyze(java.io.File)}.
   */
  @Test
  public void testAnalyze() throws Exception {
    if (!xocrInstalled)
      return;
    
    if (!new File(xocrbinary).exists())
      return;
    TextFrame frame = analyzer.extract(testFile);
    assertTrue(frame.hasText());
    assertEquals(text, frame.getLines()[0].getText());
  }

}
