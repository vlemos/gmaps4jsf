/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.googlecode.gmaps4jsf.component.map;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import com.googlecode.gmaps4jsf.util.ComponentUtils;
import com.googlecode.gmaps4jsf.util.FileReaderUtils;
import com.googlecode.gmaps4jsf.util.ComponentConstants;

/**
 * @author Hazem Saleh
 * @date Jul 13, 2008
 * last modified at Jul 31, 2008
 * The (MapRenderer) renders a google map.
 */
public final class MapRenderer extends Renderer {
    private static final String UNDEFINED = "var temp";

    public boolean getRendersChildren() {
        return true;
    }

    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ComponentUtils.assertValidContext(context);
        ResponseWriter writer = context.getResponseWriter();

        Map map = (Map) component;
        encodeCommonJavascriptCode(map, writer);
        encodeHTMLModel(context, map, writer);
        startEncodingMapWorld(context, map, writer);
    }

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ComponentUtils.assertValidContext(context);
        ResponseWriter writer = context.getResponseWriter();
        writer.write("\t\t});\n\t});\n}) (window);");
        writer.endElement(ComponentConstants.HTML_SCRIPT);
    }
    
    public void decode(FacesContext context, UIComponent component) {
        Map    map            = (Map)    component;
        String submittedValue = (String) context.getExternalContext().getRequestParameterMap().get(
                                         ComponentUtils.getMapStateHiddenFieldId(map));

        map.setSubmittedValue(submittedValue);
    }    

    /**
     * Writes the generic (not binded to a specific component) JS code.
     */
    protected void encodeCommonJavascriptCode(Map map, ResponseWriter writer) throws IOException {
        writer.startElement(ComponentConstants.HTML_SCRIPT, map);
        writer.writeAttribute(ComponentConstants.HTML_SCRIPT_TYPE, ComponentConstants.HTML_SCRIPT_LANGUAGE, ComponentConstants.HTML_SCRIPT_TYPE);
        String debug = "true".equals(map.getDebug()) ? "\n" : "";
        writer.write(FileReaderUtils.getResourceContent("gmaps4jsf.js", debug));
        writer.write(debug);
        writer.write(FileReaderUtils.getResourceContent("gmaps4jsf-map.js", debug));
        writer.endElement(ComponentConstants.HTML_SCRIPT);
    }

    private void encodeHTMLModel(FacesContext context, Map map, ResponseWriter writer) throws IOException {
        writer.startElement(ComponentConstants.HTML_DIV, map);
        writer.writeAttribute(ComponentConstants.HTML_ATTR_ID, map.getClientId(context), ComponentConstants.HTML_ATTR_ID);
        writer.writeAttribute(ComponentConstants.HTML_ATTR_STYLE, "width: " + ComponentUtils.getMapWidth(map) + "; height: " + ComponentUtils.getMapHeight(map), ComponentConstants.HTML_ATTR_STYLE);
        writer.endElement(ComponentConstants.HTML_DIV);
        
        // encode map state holder.
        Object mapState = ComponentUtils.getValueToRender(context, map);

        writer.startElement(ComponentConstants.HTML_INPUT, map);

        writer.writeAttribute(ComponentConstants.HTML_ATTR_ID, ComponentUtils.getMapStateHiddenFieldId(map),
                              ComponentConstants.HTML_ATTR_ID);
        writer.writeAttribute(ComponentConstants.HTML_ATTR_NAME, ComponentUtils.getMapStateHiddenFieldId(map),
                              ComponentConstants.HTML_ATTR_NAME);
        writer.writeAttribute(ComponentConstants.HTML_ATTR_TYPE, ComponentConstants.HTML_ATTR_TYPE_HIDDEN,
                              ComponentConstants.HTML_ATTR_TYPE);
        
        if (null != mapState) {
            writer.writeAttribute(ComponentConstants.HTML_ATTR_VALUE, mapState, ComponentConstants.HTML_ATTR_VALUE);
        }        

        writer.endElement(ComponentConstants.HTML_INPUT);        
    }

    private void startEncodingMapWorld(FacesContext context, Map map, ResponseWriter writer) throws IOException {
        writer.startElement(ComponentConstants.HTML_SCRIPT, map);
        writer.writeAttribute(ComponentConstants.HTML_SCRIPT_TYPE, ComponentConstants.HTML_SCRIPT_LANGUAGE, ComponentConstants.HTML_SCRIPT_TYPE);
        writer.write("(function(window) {\n\twindow.gmaps4jsf.addOnLoad(function() {\n\t\twindow.gmaps4jsf.createMap(" + convertToJavascriptObject(context, map) + ", function (parent) {\n");
        
        // encode map client side events ...
        EventEncoder.encodeEventListeners(context, map, writer, "parent");
    }

    protected String convertToJavascriptObject(FacesContext context, Map map) {
        StringBuffer buffer = new StringBuffer("{");
        buffer.append("id: '").append(map.getClientId(context)).append("',");
        buffer.append("enableScrollWheelZoom: ").append(map.getEnableScrollWheelZoom()).append(",");
        buffer.append("zoom: ").append(map.getZoom()).append(",");
        buffer.append("location: {latitude: ").append(map.getLatitude())
            .append(", longitude: ").append(map.getLongitude())
            .append(", address: '").append(ComponentUtils.unicode(map.getAddress()));
        buffer.append("'}, jsVariable: '").append(ComponentUtils.unicode(map.getJsVariable()));
        buffer.append("', autoReshape: ").append(map.getAutoReshape());
        return buffer.append("}").toString();
    }

}