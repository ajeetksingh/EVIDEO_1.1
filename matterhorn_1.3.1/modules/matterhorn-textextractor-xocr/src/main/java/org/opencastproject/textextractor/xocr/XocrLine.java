package org.opencastproject.textextractor.xocr;

import org.opencastproject.textextractor.api.TextLine;

import java.awt.Rectangle;

/**
 * Representation of a line of text extracted from an image.
 */
public class XocrLine implements TextLine {

  /** The text */
  protected String text = null;

  /**
   * Creates a representation for a piece of text
   * 
   * @param word
   *          the extracted text
   */
  public XocrLine(String line) {
    this.text = line;
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.textextractor.api.TextLine#getText()
   */
  @Override
  public String getText() {
    return text;
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.textextractor.api.TextLine#getBoundaries()
   */
  @Override
  public Rectangle getBoundaries() {
    return null;
  }

  /**
   * {@inheritDoc}
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return text;
  }

}
