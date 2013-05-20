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
package org.opencastproject.capture.pipeline.bins.consumers;

import java.io.File;
import java.util.Properties;
import org.gstreamer.Bus.STATE_CHANGED;
import org.gstreamer.Caps;
import org.gstreamer.Element;
import org.gstreamer.GstObject;
import org.gstreamer.Pad;
import org.gstreamer.PadDirection;
import org.gstreamer.State;
import org.opencastproject.capture.impl.monitoring.ConfidenceMonitorImpl;
import org.opencastproject.capture.impl.monitoring.MonitoringEntry;
import org.opencastproject.capture.pipeline.bins.CaptureDevice;
import org.opencastproject.capture.pipeline.bins.CaptureDeviceNullPointerException;
import org.opencastproject.capture.pipeline.bins.GStreamerElementFactory;
import org.opencastproject.capture.pipeline.bins.GStreamerElements;
import org.opencastproject.capture.pipeline.bins.GStreamerProperties;
import org.opencastproject.capture.pipeline.bins.UnableToCreateElementException;
import org.opencastproject.capture.pipeline.bins.UnableToCreateGhostPadsForBinException;
import org.opencastproject.capture.pipeline.bins.UnableToLinkGStreamerElementsException;
import org.opencastproject.capture.pipeline.bins.UnableToSetElementPropertyBecauseElementWasNullException;

/**
 * Video monitoring consumer bin.
 */
public class VideoMonitoringConsumer extends ConsumerBin {

  private Element decodebin;
  private Element ffmpegcolorspace;
  private Element timeoverlay;
  private Element videorate;
  private Element capsfilter;
  private Element jpegenc;
  private Element multifilesink;
  
  /**
   * Create an video monitoring consumer bin.
   * Save frames from raw video data as jpeg image at given interval.
   * 
   * @param captureDevice
   *          This is the properties such as codec, container, bitrate etc. of the capture device output.
   * 
   * @param properties
   *          This is the confidence monitoring properties
   * 
   * @throws UnableToLinkGStreamerElementsException
   *           If there is a problem linking any of the Elements together that make up the SinkBin this Exception will
   *           be thrown
   * @throws UnableToCreateGhostPadsForBinException
   *           If the getSrc function returns an Element that cannot be used to create a ghostpad for this SinkBin, then
   *           this Exception is thrown. This could be due to the src being null or one that doesn't have the pads
   *           available to ghost.
   * @throws UnableToSetElementPropertyBecauseElementWasNullException
   *           If the setElementPropeties is called before the createElements function then the Elements will not be
   *           created, show up as null and this Exception will be thrown.
   * @throws CaptureDeviceNullPointerException
   *           Because there are essential properties that we can't infer, such as output location, if the CaptureDevice
   *           parameter is null this exception is thrown.
   * @throws UnableToCreateElementException
   *           If the current setup is not able to create an Element because either that Element is specific to another
   *           OS or because the module for that Element is not installed this Exception is thrown.
   */
  public VideoMonitoringConsumer(CaptureDevice captureDevice, Properties properties) 
          throws UnableToLinkGStreamerElementsException, 
          UnableToCreateGhostPadsForBinException, 
          UnableToSetElementPropertyBecauseElementWasNullException, 
          CaptureDeviceNullPointerException, 
          UnableToCreateElementException {
            
    super(captureDevice, properties);
    setMonitoringHooks();
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.capture.pipeline.bins.consumers.ConsumerBin#getSrc() 
   */
  @Override
  public Element getSrc() {
    return queue;
  }

  /**
   * Create gstreamer elements.
   * 
   * @throws UnableToCreateElementException 
   *        Thrown if the Element cannot be created because the Element 
   *        doesn't exist on this machine.
   */
  @Override
  protected void createElements() throws UnableToCreateElementException {
    queue = GStreamerElementFactory.getInstance().createElement(captureDevice.getFriendlyName(),
            GStreamerElements.QUEUE, null);
    decodebin = GStreamerElementFactory.getInstance().createElement(captureDevice.getFriendlyName(),
            GStreamerElements.DECODEBIN, null);
    ffmpegcolorspace = GStreamerElementFactory.getInstance().createElement(captureDevice.getFriendlyName(),
            GStreamerElements.FFMPEGCOLORSPACE, null);
    timeoverlay = GStreamerElementFactory.getInstance().createElement(captureDevice.getFriendlyName(),
            "timeoverlay", null);
    videorate = GStreamerElementFactory.getInstance().createElement(captureDevice.getFriendlyName(),
            GStreamerElements.VIDEORATE, null);
    capsfilter = GStreamerElementFactory.getInstance().createElement(captureDevice.getFriendlyName(),
            GStreamerElements.CAPSFILTER, null);
    jpegenc = GStreamerElementFactory.getInstance().createElement(captureDevice.getFriendlyName(),
            "jpegenc", null);
    multifilesink = GStreamerElementFactory.getInstance().createElement(captureDevice.getFriendlyName(),
            "multifilesink", null);
  }
  
  /**
   * Set element property like interval and some other.
   * 
   * @throws IllegalArgumentException
   * @throws UnableToSetElementPropertyBecauseElementWasNullException 
   */
  @Override
  protected void setElementProperties() throws IllegalArgumentException,
          UnableToSetElementPropertyBecauseElementWasNullException {
            
    int interval = confidenceMonitoringProperties.getInterval();
    String location = confidenceMonitoringProperties.getImageloc();
    String device = confidenceMonitoringProperties.getDevice();
    
    capsfilter.set(GStreamerProperties.CAPS, Caps.fromString(GStreamerProperties.VIDEO_X_RAW_YUV + ", " 
            + GStreamerProperties.FRAMERATE + "=1/" + interval));
    multifilesink.set(GStreamerProperties.LOCATION, new File(location, device + ".jpg").getAbsolutePath());
    multifilesink.set("async", "true");
  }
  
  /**
   * Add all elements to the bin.
   */
  @Override
  protected void addElementsToBin() {
    bin.addMany(queue, decodebin, ffmpegcolorspace, timeoverlay, videorate, capsfilter, jpegenc, multifilesink);
  }
  
  /**
   * Link gstreamer elements together.
   * 
   * @throws UnableToLinkGStreamerElementsException 
   *            Will be thrown, if the elements can not be linked together.
   */
  @Override
  protected void linkElements() throws UnableToLinkGStreamerElementsException {
    
    decodebin.connect(new Element.PAD_ADDED() {
      public void padAdded(Element element, Pad pad) {
        if (pad.getDirection() == PadDirection.SRC) {
          pad.link(ffmpegcolorspace.getStaticPad("sink"));
        }
      }
    });
    
    if (!queue.link(decodebin)) {
      throw new UnableToLinkGStreamerElementsException(captureDevice, queue, decodebin);
    }

    if (confidenceMonitoringProperties.isDebug()) {
      if (!ffmpegcolorspace.link(timeoverlay)) {
        throw new UnableToLinkGStreamerElementsException(captureDevice, ffmpegcolorspace, timeoverlay);
      }
      if (!timeoverlay.link(videorate)) {
        throw new UnableToLinkGStreamerElementsException(captureDevice, timeoverlay, videorate);
      }
    } else {
      if (!ffmpegcolorspace.link(videorate)) {
        throw new UnableToLinkGStreamerElementsException(captureDevice, ffmpegcolorspace, videorate);
      }
    }
    if (!videorate.link(capsfilter)) {
      throw new UnableToLinkGStreamerElementsException(captureDevice, videorate, capsfilter);
    }
    if (!capsfilter.link(jpegenc)) {
      throw new UnableToLinkGStreamerElementsException(captureDevice, capsfilter, jpegenc);
    }
    if (!jpegenc.link(multifilesink)) {
      throw new UnableToLinkGStreamerElementsException(captureDevice, jpegenc, multifilesink);
    }
  }
  
  private void setMonitoringHooks() {
    
    multifilesink.getBus().connect(new STATE_CHANGED() {

      @Override
      public void stateChanged(GstObject source, State old, State current, State pending) {
        if (source != multifilesink) return;
        
        ConfidenceMonitorImpl monitoringService = ConfidenceMonitorImpl.getInstance();
        if (current == State.PLAYING) { 
          // add monitoring entry to ConfidenceMonitoring-Service
          if (monitoringService != null) {
            monitoringService.createMonitoringEntry(captureDevice.getFriendlyName(), MonitoringEntry.MONITORING_TYPE.VIDEO, 
                  new File(confidenceMonitoringProperties.getImageloc(), confidenceMonitoringProperties.getDevice() + ".jpg").getAbsolutePath());
          }
        } else if (current == State.NULL) {
          // remove monitoring entry from ConfidenceMonitoring-Service
          if (monitoringService != null) {
            monitoringService.removeMonitoringEntry(captureDevice.getFriendlyName(), MonitoringEntry.MONITORING_TYPE.VIDEO);
          }
        }
      }
    });
  }
}
