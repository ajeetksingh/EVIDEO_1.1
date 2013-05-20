/**
 *  Copyright 2009-2011 The Regents of the University of California
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

var Opencast = Opencast || {};

/**
 * @namespace the global Opencast namespace segments_Plugin
 */
Opencast.segments_Plugin = (function ()
{
    // The Template to process
    var template = '{for s in segment}' +
                        '<div id="panel_${s.index}" class="panel" style="float: left; position: relative;">' +
                            '<div role="button" class="inside" ' +
                                'onmouseover="Opencast.segments_ui.hoverSegment(${parseInt(s.hoverSegmentIndex)}, ${parseInt(s.index)})" ' +
                                'onmouseout="Opencast.segments_ui.hoverOutSegment(${parseInt(s.index)})">' +
                                    '<a href="javascript:Opencast.Watch.seekSegment(${parseInt(s.time) / 1000})">' +
                                        '<img alt="Slide ${parseInt(s.index) + 1} of ${segment.length}" ' +
                                            'src="${s.previews.preview.$}">' +
                                    '</a>' +
                            '</div>' +
                        '</div>' +
                    '{forelse}' +
                        'No Segments available' +
                    '{/for}';

    // The Element to put the div into
    var element;
    // Data to process
    var segments_data;
    // Processed Data
    var processedTemplateData = false;

    /**
     * @memberOf Opencast.segments_Plugin
     * @description Add As Plug-in
     * @param elem Element to fill with the Data (e.g. a div)
     * @param data Data to fill the Element with
     */
    function addAsPlugin(elem, data)
    {
        element = elem;
        segments_data = data;
        createSegments();
    }

    /**
     * @memberOf Opencast.segments_Plugin
     * @description Tries to work with the cashed data
     * @return true if successfully processed, false else
     */
    function createSegmentsFromCashe()
    {
        if ((processedTemplateData !== false) && (element !== undefined) && (segments_data.segment !== undefined) && (segments_data.segment.length > 0))
        {
            $.log("Series Plugin: Data available, processing template");
            element.html(processedTemplateData);
            return true;
        }
        else
        {
            $.log("Series Plugin: No data available");
            return false;
        }
    }

    /**
     * @memberOf Opencast.segments_Plugin
     * @description Processes the Data and puts it into the Element
     */
    function createSegments()
    {
        if ((element !== undefined) && (segments_data.segment !== undefined) && (segments_data.segment.length > 0))
        {
            processedTemplateData = template.process(segments_data);
            element.html(processedTemplateData);
        }
    }

    return {
        createSegmentsFromCashe: createSegmentsFromCashe,
        addAsPlugin: addAsPlugin
    };
}());

Opencast.segments_text_PluginforVideos = (function ()
{
    // The Template to process
    var template1 =  '<table border=1 cellspacing="5" cellpadding="0" width="100%">' +
                        '{for s in segment}' +
                            // Accessibility Feature - Comment in if you want to display only the Segments after the current Slider-Position
                            // '{if s.durationIncludingSegment >= currentTime}' +
                                '<tr>' +
                                    '<td width="15%" class="oc-segments-preview" style="cursor:pointer;font:bold;background:black;color:white;">' +
                                        '<a style="font:bold;background:black;color:white;" onclick="${Math.floor(parseInt(s.time) / 1000)}"></a>' +
                                    '</td>' +
                                    '<td width="85%" align="left" onclick="" style="cursor:pointer;">' +
                                        '&nbsp;<label class="segments-time"' +
                                            'onclick="">' +
                                            '${$.formatSeconds(Math.floor(parseInt(s.time) / 1000))}' +
                                        '</label>' +
                                        '&nbsp;<label onclick="">${s.text}</a>' +
                                    '</td>' +
				  '</tr>' +

                            // '{/if}' +
                        '{forelse}' +
                            'No Segment Text available' +
			'<hr />' +
                        '{/for}' +

		    '</table>';

    // The Element to put the div into
    var element1;
    // Data to process
    var segments_data1;
    // Precessed Data
    var processedTemplateData1 = false;

    /**
     * @memberOf Opencast.segments_text_Plugin
     * @description Add As Plug-in
     * @param elem Element to fill with the Data (e.g. a div)
     * @param data Data to fill the Element with
     */
    function addAsPlugin2(elem, data)
    {
        element1 = elem;
        segments_data1 = data;
        createSegments2();
    }

    /**
     * @memberOf Opencast.segments_text_Plugin
     * @description Tries to work with the cashed data
     * @return true if successfully processed, false else
     */
    function createSegmentsTextFromCashe2()
    {
        if ((processedTemplateData1 !== false) && (element1 !== undefined) && (segments_data1.segment !== undefined) && (segments_data1.segment.length > 0))
        {
            element1.html(processedTemplateData1);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * @memberOf Opencast.segments_text_Plugin
     * @description Processes the Data and puts it into the Element
     */
    function createSegments2()
    {
        if ((element1 !== undefined) && (segments_data1.segment !== undefined) && (segments_data1.segment.length > 0))
        {
            $.log("Segments Text Plugin: Data available, processing template");
            processedTemplateData1 = template1.process(segments_data1);
            element1.html(processedTemplateData1);
        } else
        {
            $.log("Segments Text Plugin: No data available");
        }
    }

    return {
        createSegmentsTextFromCashe2: createSegmentsTextFromCashe2,
        addAsPlugin2: addAsPlugin2
    };
}());
