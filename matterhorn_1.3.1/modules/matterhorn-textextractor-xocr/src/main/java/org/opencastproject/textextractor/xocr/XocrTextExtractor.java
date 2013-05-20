/**
 *  Copyright 2009, 2010 The Regents of the University of California
 *  Licensed under the Educational Community License, Version 2.0
 *  (the "License"); you may not use this file except in compliance
 *  with the License. You may obtain a copy of the License at
 *
 *  http://www.osedu.org/licenses/ECL-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an "AS IS"
 *  BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *  or implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 */

package org.opencastproject.textextractor.xocr;

import org.opencastproject.textanalyzer.api.TextAnalyzerException;
import org.opencastproject.textextractor.api.TextExtractor;
import org.opencastproject.textextractor.api.TextExtractorException;
import org.opencastproject.textextractor.api.TextFrame;
import org.opencastproject.util.ProcessExcecutorException;
import org.opencastproject.util.ProcessExecutor;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Dictionary;

/**
 * Commandline wrapper around tesseract' <code>tesseract</code> command.
 */
public class XocrTextExtractor implements TextExtractor, ManagedService {

  /** The logging facility */
  private static final Logger logger = LoggerFactory.getLogger(XocrTextExtractor.class);

  /** Default name of the tesseract binary */
  public static final String XOCR_BINARY_DEFAULT = "/usr/local/bin/XOCR";

  /** The configuration admin property that defines the path to the tesseract binary */
  public static final String XOCR_BINARY_CONFIG_KEY = "org.opencastproject.textanalyzer.xocr.path";

  /** Binary of the tesseract command */
  protected String binary = null;

  /**
   * Creates a new tesseract command wrapper that will be using the default binary.
   */
  public XocrTextExtractor() {
    this(XOCR_BINARY_DEFAULT);
  }

  /**
   * Creates a new tesseract command wrapper that will be using the given binary.
   * 
   * @param binary
   *          the tesseract binary
   */
  public XocrTextExtractor(String binary) {
    this.binary = binary;
  }

  /**
   * Returns the path to the <code>tesseract</code> binary.
   * 
   * @return path to the binary
   */
  public String getBinary() {
    return binary;
  }

  /**
   * Sets the path to the <code>tesseract</code> binary.
   * 
   * @param binary
   */
  public void setBinary(String binary) {
    this.binary = binary;
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.textextractor.api.TextExtractor#extract(java.io.File)
   */
  public TextFrame extract(File image) throws TextExtractorException {
    if (binary == null)
      throw new IllegalStateException("Binary is not set");

    final TextFrame textFrame;
    File outputFileBase = new File(new File("/home/languagelab"),FilenameUtils.getBaseName(image.getName()));//new File(image.getParentFile(), FilenameUtils.getBaseName(image.getName()));

    /** Output of the commandline process */
    final StringBuffer ocrocmdOutput = new StringBuffer();

    // Run xocr
    try {
      ProcessExecutor<TextAnalyzerException> analyzer = new ProcessExecutor<TextAnalyzerException>(binary,
              getAnalysisOptions(image, outputFileBase)) {
      @Override
      protected boolean onLineRead(String line) {
        logger.trace(line);
	if(!line.equals("recognize"))
        	ocrocmdOutput.append(line).append('\n');
        return true;
      }

        @Override
        protected void onProcessFinished(int exitCode) throws TextAnalyzerException {
          // Windows binary will return -1 when queried for options
          // if (exitCode != -1 && exitCode != 0 && exitCode != 255) {
          // throw new TextAnalyzerException("Text analyzer " + binary + " exited with code " + exitCode);
          // }
        }
      };
      analyzer.execute();
    } catch (ProcessExcecutorException e) {
      throw new TextExtractorException("Error running text extractor " + binary, e);
    }

    // Read the xocr output file
    InputStream is = null;
    try {
      is = IOUtils.toInputStream(ocrocmdOutput.toString(), "UTF-8");
      textFrame = XocrTextFrame.parse(is);
//    InputStream is = null;
//    File outputFile = new File(outputFileBase.getAbsolutePath());
//    try {
//      is = new FileInputStream(outputFile);
 //     textFrame = XocrTextFrame.parse(is);
    } catch (IOException e) {
      throw new TextExtractorException(e);
    } finally {
      IOUtils.closeQuietly(is);
    }

    return textFrame;
  }

  /**
   * The only parameter to <code>XOCR recognize</code> is the filename, so this is what this method returns.
   * 
   * @param image
   *          the image file
   * @return the options to run analysis on the image
   */
  protected String getAnalysisOptions(File image, File outputFile) {
    StringBuilder options = new StringBuilder();
    options.append("recognize").append(" ");
    options.append(image.getAbsolutePath()); //.append(" -output_file ").append(outputFile.getAbsolutePath());
//	System.out.println("\n\n\ngetAnalysisOptions\n" + options.toString() + "\n\n\n\n");
    return options.toString();
  }

  @Override
  public void updated(@SuppressWarnings("rawtypes") Dictionary properties) throws ConfigurationException {
    String path = (String) properties.get(XOCR_BINARY_CONFIG_KEY);
    if (path != null) {
      this.binary = path;
    }
  }
}
