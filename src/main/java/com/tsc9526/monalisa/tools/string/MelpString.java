/*******************************************************************************************
 *	Copyright (c) 2016, zzg.zhou(11039850@qq.com)
 * 
 *  Monalisa is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU Lesser General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.

 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU Lesser General Public License for more details.

 *	You should have received a copy of the GNU Lesser General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************************/
package com.tsc9526.monalisa.tools.string;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.google.gson.Gson;
import com.tsc9526.monalisa.tools.datatable.DataMap;
import com.tsc9526.monalisa.tools.json.MelpJson;
import com.tsc9526.monalisa.tools.xml.XMLDocument;
import com.tsc9526.monalisa.tools.xml.XMLObject;


/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class MelpString {
	public static boolean isEmpty(String s) {
		return s == null || s.length() == 0;
	}

	
	public static String toString(Object bean){
		if(bean==null){
			return "<NULL>";
		}
		
		if(bean instanceof Throwable){
			StringWriter w = new StringWriter();
	
			((Throwable)bean).printStackTrace(new PrintWriter(w));
	
			return w.toString();
		}else if(MelpTypes.isPrimitiveOrString(bean) || bean.getClass().isEnum()){
			return bean.toString();
		}else{
			return MelpJson.getGson().toJson(bean);
		}
	}
	 
	
	public static String toJson(Object bean) {
		Gson gson=MelpJson.getGson();
		
		return MelpJson.toJson(gson,bean); 
	}
	 
	public static String repeat(String x,int times){
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<times;i++){
			sb.append(x);
		}
		return sb.toString();
	}
 
	public static String toXml(Object bean){
		return new XMLObject(bean).toString();
	}
	
	public static String toXml(Object bean,boolean withXmlHeader, boolean ignoreNullFields) {
		return new XMLObject(bean)
			.setWithXmlHeader(withXmlHeader)
			.setIgnoreNullFields(ignoreNullFields)
			.toString();	
	}	
	 
	public static DataMap json2Map(String json){
		return MelpJson.parseToDataMap(json);
	}
	
	public static String normalizeXml(String xml){
		xml=xml.trim();
		
		//check xml tag
		int p1=xml.indexOf("<xml>");
		int p2=xml.indexOf("</xml>");
		if(p2>=p1 && p1>0){
			xml="<root>"+xml.substring(p1+"<xml>".length(),p2)+"</root>";
		}
		
		//check xml header
		if(!xml.startsWith("<?xml")){
			xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"+xml;
		}
		return xml;
	}
	
	public static DataMap xml2Map(String xml){
		xml=normalizeXml(xml); 
		
		try{
			XMLDocument p= new XMLDocument(); 
			Document doc=p.parseDocument(new InputSource(new StringReader(xml)));
			return p.toMap(doc.getChildNodes().item(0));  
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 分隔字符串， 可能的分隔符号有3个： 逗号(,) 分号(;) 竖号(|)
	 * 
	 * @param ms 待分隔的字符串
	 * @return 分隔后的数组
	 */
	public static String[] splits(String... ms) {
		List<String> xs = new ArrayList<String>();
		for (String s : ms) {
			if(s!=null){
				for (String x : s.split(",|;|\\|")) {
					if (x != null && x.trim().length() > 0) {
						xs.add(x.trim());
					}
				}
			}
		}

		return xs.toArray(new String[0]);
	}

	public static String escapeStringValue(String v) {
		if (v == null) {
			return null;
		}

		StringBuffer r = new StringBuffer();
		for (int i = 0; i < v.length(); i++) {
			char c = v.charAt(i);
			if (c == '\\' && (i + 1) < v.length()) {
				i++;
				c = v.charAt(i);
			}
			r.append(c);
		}
		return r.toString();
	}

	 

	public static String join(String[] vs, int from, int len, String joinString) {
		StringBuffer sb = new StringBuffer();
		for (int i = from; i < (from + len) && i < vs.length; i++) {
			if (sb.length() > 0) {
				sb.append(joinString);
			}
			sb.append(vs[i]);
		}
		return sb.toString();
	}

	public static String join(List<String> vs, int from, int len, String joinString) {
		StringBuffer sb = new StringBuffer();
		for (int i = from; i < (from + len) && i < vs.size(); i++) {
			if (sb.length() > 0) {
				sb.append(joinString);
			}
			sb.append(vs.get(i));
		}
		return sb.toString();
	}

	public static String[] shiftLeft(String[] vs, int len) {
		if (vs.length > len) {
			return Arrays.copyOfRange(vs, len, vs.length - 1);
		} else {
			return null;
		}
	}

	public static URL[] toURLs(String[] classPath) {
		List<URL> urls = new ArrayList<URL>();
		try {
			for (String x : classPath) {
				File file = new File(x);
				if (file.exists()) {
					urls.add(file.toURI().toURL());
				}
			}

			return urls.toArray(new URL[0]);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	 

	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (hexCharToByte(hexChars[pos]) << 4 | hexCharToByte(hexChars[pos + 1]));

		}
		return d;
	}
 	
	public static byte hexCharToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public static String intToBytesString(int i) {
		byte[] b = intToBytes(i);
		return bytesToHexString(b);
	}

	public static byte[] intToBytes(int i) {
		byte[] b = new byte[4];
		b[0] = (byte) ((i >> 24) & 0xFF);
		b[1] = (byte) ((i >> 16) & 0xFF);
		b[2] = (byte) ((i >> 8) & 0xFF);
		b[3] = (byte) ((i) & 0xFF);
		return b;
	}

	public static String bytesToHexString(byte[] src) {
		return bytesToHexString(src, null);
	}

	public static String bytesToHexString(byte[] src, String bytePrefix) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;

			if (bytePrefix != null && bytePrefix.length() > 0) {
				stringBuilder.append(bytePrefix);
			}

			String hv = Integer.toHexString(v).toUpperCase();
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
	
	public static byte[] toBytesUtf8(String data){
		return toBytes(data, "utf-8");
	}
	
	public static byte[] toBytes(String data,String charset){
		if(data==null){
			return null;
		}else if(data.length()==0){
			return new byte[0];
		}else{
			try{
				return data.getBytes(charset);
			}catch(UnsupportedEncodingException e){
				throw new RuntimeException(e);
			}
		}
	}
}
