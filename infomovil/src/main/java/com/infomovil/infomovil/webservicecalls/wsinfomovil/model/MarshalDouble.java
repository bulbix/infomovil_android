/**
 * 
 */
package com.infomovil.infomovil.webservicecalls.wsinfomovil.model;

import java.io.IOException;

import org.ksoap2.serialization.Marshal;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

/**
 * @author Ignaki Dominguez
 *
 */
public class MarshalDouble implements Marshal
{
    @Override
    public Object readInstance(XmlPullParser parser, String namespace, String name, 
            PropertyInfo expected) throws IOException, XmlPullParserException
    { return Double.parseDouble(parser.nextText()); }

    public void register(SoapSerializationEnvelope soapSerial)
    { soapSerial.addMapping(soapSerial.xsd, "double", Double.class, this); }

    @Override
    public void writeInstance(XmlSerializer writer, Object obj) throws IOException
    { writer.text(obj.toString()); }
}
