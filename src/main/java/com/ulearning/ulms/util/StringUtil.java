package com.ulearning.ulms.util;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class StringUtil
{
	private final static String RAND_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";//随机产生的字符串
	private final static String RAND_Integer = "0123456789";//随机产生的数字 
		/**
         * Escapes any html characters in the input string.
         *
         * @param in
         * @return
         */
        public static String escape(String in)
        {
                StringBuffer out = new StringBuffer();

                for (int i = 0; i < in.length(); i++)
                {
                        char c = in.charAt(i);

                        switch (c)
                        {
                                case '<':
                                        out.append("&lt;");

                                        break;

                                case '>':
                                        out.append("&gt;");

                                        break;

                                case '&':
                                        out.append("&amp;");

                                        break;

                                case '"':
                                        out.append("&quot;");

                                        break;

                                case '\n':
                                        out.append("<br>");

                                        break;

                                default:
                                        out.append(c);

                                        break;
                        }
                }

                return out.toString();
        }

        /**
         * Create By: xie jianhua
         * Filter javascript and java characters in the input string
         * fit input for type=text and type=hidden
         *
         * @param strInput
         * @return
         */
        public static String checkInputText(String strInput)
        {
                String res = "";

                if (strInput != null)
                {
                        res = checkInput(strInput.trim());
                }

                return res;
        }

        /**
         * Create By: xie jianhua
         * Filter javascript and java characters in the input string
         * fit TEXTAREA
         *
         * @param strInput
         * @return
         */
        public static String checkInputTextArea(String strInput)
        {
                return checkInput(strInput);
        }

        /**
         * Create By: xie jianhua
         * Filter javascript and java characters in the input string.
         *
         * @param strInput
         * @return
         */
        public static String checkInput(String strInput)
        {
                String res = "";

                if (strInput != null)
                {
                        StringBuffer out = new StringBuffer();
                        String in = strInput;

                        for (int i = 0; i < in.length(); i++)
                        {
                                char c = in.charAt(i);

                                switch (c)
                                {
                                        case '<':
                                                out.append("&#060;");

                                                break;

                                        case '>':
                                                out.append("&#062;");

                                                break;

                                        case '&':
                                                out.append("&#038;");

                                                break;

                                        case '"':
                                                out.append("&#034;");

                                                break;

                                        case '\'':
                                                out.append("&#039;");

                                                break;

                                        case '\\':
                                                out.append("&#034;");

                                                break;

                                        case '%':
                                                out.append("&#037;");

                                                break;

                                        default:
                                                out.append(c);

                                                break;
                                }
                        }

                        res = out.toString();
                }

                return res;
        }

        /**
         * Create By: xie jianhua
         * Fliter the input content from client.
         * example:result=htmlFilter("   test test.")
         * the value of [result] is "&nbsp; &nbsp;test test."
         */
        public static String htmlFilter(String strContent)
        {
                String strHtmlFilter = ((strContent == null) ? "" : strContent);

                String VBCRLF = "\n";
                String VBCRLF1 = "\n ";
                String ASPACE = " ";
                String ASPACE1 = "  ";
                String BR = "<br>";
                String BR1 = "<br>&nbsp;";
                String SPACETAG = "&nbsp;";
                int P = -1;

                if (strHtmlFilter.length() != 0)
                {
                        strHtmlFilter = replaceString(strHtmlFilter, VBCRLF1, BR1);
                        strHtmlFilter = replaceString(strHtmlFilter, VBCRLF, BR);

                        if (strHtmlFilter.substring(0, 1).equals(ASPACE))
                        {
                                strHtmlFilter = SPACETAG + strHtmlFilter.substring(1);
                        }

                        P = strHtmlFilter.indexOf(ASPACE1);

                        while (P > 0)
                        {
                                strHtmlFilter = strHtmlFilter.substring(0, P) + SPACETAG +
                                        ASPACE + strHtmlFilter.substring(P + 2);
                                P = strHtmlFilter.indexOf(ASPACE + ASPACE);
                        }
                }

                return strHtmlFilter;
        }

        public static String htmlFilter2(String strContent)
        {
                String strHtmlFilter = ((strContent == null) ? "" : strContent);

                String VBCRLF = "\n";
                String VBCRLF1 = "\n ";
                String ASPACE = " ";
                String ASPACE1 = "  ";
                String BR = "<br>";
                String BR1 = "<br>&nbsp;";
                String SPACETAG = "&nbsp;";
                int P = -1;

                if (strHtmlFilter.length() != 0)
                {
                        //strHtmlFilter = replaceString(strHtmlFilter, VBCRLF1, BR1);
                        //strHtmlFilter = replaceString(strHtmlFilter, VBCRLF, BR);
                        if (strHtmlFilter.substring(0, 1).equals(ASPACE))
                        {
                                strHtmlFilter = SPACETAG + strHtmlFilter.substring(1);
                        }

                        P = strHtmlFilter.indexOf(ASPACE1);

                        while (P > 0)
                        {
                                strHtmlFilter = strHtmlFilter.substring(0, P) + SPACETAG +
                                        ASPACE + strHtmlFilter.substring(P + 2);
                                P = strHtmlFilter.indexOf(ASPACE + ASPACE);
                        }
                }

                return strHtmlFilter;
        }

        public static String htmlFilter(String strContent, int lineMaxLength)
        {
                return htmlFilter(dealLongStr(strContent, lineMaxLength));
        }

        //将字符串str按space分成数组
        public static String[] splitString(String str, String space)
        {
                String str1 = str.trim();
                int i;
                int s;
                int k;
                k = space.length();

                for (i = 0; str1.indexOf(space) != -1; i++)
                {
                        str1 = str1.substring(str1.indexOf(space) + k, str1.length());
                }

                int bound = i + 1;
                str1 = str;

                String[] array1 = new String[bound];
                array1[0] = "";

                for (i = 0; str1 != ""; i++)
                {
                        s = str1.indexOf(space);

                        if (s != -1)
                        {
                                array1[i] = str1.substring(0, s);
                                str1 = str1.substring(s + k, str1.length());
                        }
                        else
                        {
                                array1[i] = str1;
                                str1 = "";
                        }
                }

                return array1;
        }

        /**
         * Create By: xie jianhua
         * add Enter after long string  while long string exceed  lineMaxLength
         * user in html page
         *
         * @param longStr lineMaxLength
         * @return
         */
        public static String dealLongStr(String longStr, int lineMaxLength)
        {
                String segment = longStr;

                if ((segment != null) && !segment.trim().equals("") &&
                        !segment.trim().equals("null") && (lineMaxLength > 0))
                {
                        int length = 0;
                        int flag = 0;

                        for (int n = 0; n < segment.length(); n++)
                        {
                                if ((segment.charAt(n) <= 32) || (segment.charAt(n) > 126))
                                {
                                        length = 0;
                                }
                                else
                                {
                                        length++;

                                        if ((segment.charAt(n) == 38) &&
                                                (segment.charAt(n + 1) == 35) &&
                                                (segment.charAt(n + 5) == 59))
                                        {
                                                boolean bn2 = (segment.charAt(n + 2) >= 48) &&
                                                        (segment.charAt(n + 2) <= 57);
                                                boolean bn3 = (segment.charAt(n + 3) >= 48) &&
                                                        (segment.charAt(n + 3) <= 57);
                                                boolean bn4 = (segment.charAt(n + 4) >= 48) &&
                                                        (segment.charAt(n + 4) <= 57);

                                                if (bn2 && bn3 && bn4)
                                                {
                                                        n = n + 5;
                                                        flag = -5;
                                                }
                                        }
                                        else
                                        {
                                                flag = 0;
                                        }
                                }

                                if ((length > 1) && ((length % lineMaxLength) == 1))
                                {
                                        segment = segment.substring(0, n + flag) + "\r\n" +
                                                segment.substring(n + flag);
                                        flag = 0;
                                        n = n + 2;
                                }
                        }
                }

                return segment;
        }

        /**
         * Replaces all occurances of oldString in mainString with newString
         *
         * @param mainString The original string
         * @param oldString  The string to replace
         * @param newString  The string to insert in place of the old
         * @return mainString with all occurances of oldString replaced by newString
         */
        public static String replaceString(String mainString, String oldString,
                                           String newString)
        {
                if (mainString == null)
                {
                        return null;
                }

                if ((oldString == null) || (oldString.length() == 0))
                {
                        return mainString;
                }

                if (newString == null)
                {
                        newString = "";
                }

                int i = mainString.lastIndexOf(oldString);

                if (i < 0)
                {
                        return mainString;
                }

                StringBuffer mainSb = new StringBuffer(mainString);

                while (i >= 0)
                {
                        mainSb.replace(i, i + oldString.length(), newString);
                        i = mainString.lastIndexOf(oldString, i - 1);
                }

                return mainSb.toString();
        }

        /**
         * Creates a single string from a List of strings seperated by a delimiter.
         *
         * @param list  a list of strings to join
         * @param delim the delimiter character(s) to use. (null value will join with no delimiter)
         * @return a String of all values in the list seperated by the delimiter
         */
        public static String join(List list, String delim)
        {
                if ((list == null) || (list.size() < 1))
                {
                        return null;
                }

                StringBuffer buf = new StringBuffer();
                Iterator i = list.iterator();

                while (i.hasNext())
                {
                        buf.append((String) i.next());

                        if (i.hasNext())
                        {
                                buf.append(delim);
                        }
                }

                return buf.toString();
        }

        /**
         * Splits a String on a delimiter into a List of Strings.
         *
         * @param str   the String to split
         * @param delim the delimiter character(s) to join on (null will split on whitespace)
         * @return a list of Strings
         */
        public static List<String> split(String str, String delim)
        {
                List<String> splitList = null;
                StringTokenizer st = null;

                if (str == null)
                {
                        return splitList;
                }

                if (delim != null)
                {
                        st = new StringTokenizer(str, delim);
                }
                else
                {
                        st = new StringTokenizer(str);
                }

                if ((st != null) && st.hasMoreTokens())
                {
                        splitList = new ArrayList<String>();

                        while (st.hasMoreTokens())
                        {
                                splitList.add(st.nextToken());
                        }
                }

                return splitList;
        }

        /**
         * Encloses each of a List of Strings in quotes.
         *
         * @param list List of String(s) to quote.
         */
        public static List quoteStrList(List list)
        {
                List tmpList = list;

                list = new ArrayList();

                Iterator i = tmpList.iterator();

                while (i.hasNext())
                {
                        String str = (String) i.next();

                        str = "'" + str + "''";
                        list.add(str);
                }

                return list;
        }

        /**
         * Removes all spaces from a string
         */
        public static String removeSpaces(String str)
        {
                StringBuffer newString = new StringBuffer();

                for (int i = 0; i < str.length(); i++)
                {
                        if (str.charAt(i) != ' ')
                        {
                                newString.append(str.charAt(i));
                        }
                }

                return newString.toString();
        }

        /**
         * null -> "" and trim(str)
         */
        public static String nullToStr(String str)
        {
                String tmp = str;

                if (tmp == null)
                {
                        tmp = "";
                }
                else
                {
                        tmp = tmp.trim();

                        if (tmp.equals("null"))
                        {
                                tmp = "";
                        }
                }

                return tmp;
        }

        /**
         * 得到随机字符
         */
        public static String randomStr(int n)
        {
                String str1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
                String str2 = "";
                int len = str1.length() - 1;
                double r;

                for (int i = 0; i < n; i++)
                {
                        r = (Math.random()) * len;
                        str2 = str2 + str1.charAt((int) r);
                }

                return str2;
        }

        public static String checkEmpty(String string)
        {
                if ((string != null) && (string.length() > 0))
                {
                        return string;
                }
                else
                {
                        return "";
                }
        }

        
        /**
         * 验证String 是否为null 或者是空串 
         * 空 返回false 不为空 返回true
         * @param String
         * @return
         * @author zhangheng
         */
        public static boolean checkStringNotNull(String String){
        	return (String != null && String.length()>0)?true:false;
        }
        public static String checkEmpty(String string1, String string2)
        {
                if ((string1 != null) && (string1.length() > 0))
                {
                        return string1;
                }
                else if ((string2 != null) && (string2.length() > 0))
                {
                        return string2;
                }
                else
                {
                        return "";
                }
        }

        public static String checkEmpty(String string1, String string2,
                                        String string3)
        {
                if ((string1 != null) && (string1.length() > 0))
                {
                        return string1;
                }
                else if ((string2 != null) && (string2.length() > 0))
                {
                        return string2;
                }
                else if ((string3 != null) && (string3.length() > 0))
                {
                        return string3;
                }
                else
                {
                        return "";
                }
        }

        public static boolean checkBoolean(String str)
        {
                return checkBoolean(str, false);
        }

        public static boolean checkBoolean(String str, boolean defaultValue)
        {
                if (defaultValue)
                {
                        //default to true, ie anything but false is true
                        return !"false".equals(str);
                }
                else
                {
                        //default to false, ie anything but true is false
                        return "true".equals(str);
                }
        }

        /**
         * ISO_8559_1---------->GBK
         *
         * @param strIn
         * @return GBK
         */
        public static String IsoToGBK(String strIn)
        {
                String strOut = null;

                if (strIn == null)
                {
                        return "";
                }

                try
                {
                        byte[] b = strIn.getBytes("ISO8859_1");
                        strOut = new String(b, "GBK");
                }
                catch (UnsupportedEncodingException e)
                {
                }

                return strOut;
        }

        /**
         * GBK------------>ISO_8559_1
         *
         * @param strIn
         * @return ISO_8559_1
         */
        public static String GBKToIso(String strIn)
        {
                byte[] b;
                String strOut = null;

                if (strIn == null)
                {
                        return "";
                }

                try
                {
                        b = strIn.getBytes("GBK");
                        strOut = new String(b, "ISO8859_1");
                }
                catch (UnsupportedEncodingException e)
                {
                }

                return strOut;
        }
        
        public static String IsoToUtf(String strvalue) {
        	try {
                if (strvalue == null) {
                    return "";
                } else {
                    strvalue = new String(strvalue.getBytes("ISO8859_1"), "UTF-8").trim();
                    return strvalue;
                }
            } catch (Exception e) {
                return "";
            }
        }
        
        /**
         * parse a string to a integer
         */
        public static int parseInt(String str)
        {
                int i = 0;

                try
                {
                        if (str != null)
                        {
                                i = Integer.parseInt(str);
                        }
                }
                catch (Exception ex)
                {
                        ex.printStackTrace();
                }

                return i;
        }

        /**
         * parse a string to a Float
         */
        public static float parseFloat(String str)
        {
                float i = 0;

                try
                {
                        if (str != null)
                        {
                                i = Float.parseFloat(str);
                        }
                }
                catch (Exception ex)
                {
                        ex.printStackTrace();
                }

                return i;
        }

        /**
         * parse a string to a long value.
         */
        public static long parseLong(String str)
        {
                long i = 0;

                try
                {
                        if (str != null)
                        {
                                i = Long.parseLong(str);
                        }
                }
                catch (Exception ex)
                {
                        ex.printStackTrace();
                }

                return i;
        }

        /**
         * return  the string truncated,<br>
         * if size>str.length,then  truncat the str.
         *
         * @param str
         * @param size
         * @return
         */
        public static String truncate(String str, int size)
        {
                if (str == null)
                {
                        return "";
                }

                if ((size < 0) || (size >= str.length()))
                {
                        return str;
                }
                else
                {
                        return str.substring(0, size) + "...";
                }
        }

        //start: add for answerquestion

        /**
         * 过滤掉所有HTML标签。
         * 如过滤字符串为"<p class=dd>ffffj</p>",,则得到:"ffffj"
         *
         * @param in
         * @return
         */
        public static String filterByHTMLTag(String in)
        {
                if (in == null)
                {
                        return "";
                }

                replaceString(in, "<br>", "\n");
                replaceString(in, "<BR>", "\n");
                replaceString(in, "<br/>", "\n");
                replaceString(in, "<BR/>", "\n");
                replaceString(in, "<p>", "\n");
                replaceString(in, "<P>", "\n");

                StringBuffer out = new StringBuffer();
                boolean isOK = true;

                for (int i = 0; i < in.length(); i++)
                {
                        char c = in.charAt(i);

                        switch (c)
                        {
                                case '<':
                                        isOK = false;

                                        break;

                                case '>':
                                        isOK = true;

                                        break;

                                default:

                                        if (isOK)
                                        {
                                                out.append(c);
                                        }
                        }
                }

                return out.toString();
        }

        public static String getRandomCode(int length)
        {
        	Random r = new Random();
			int i = 0;
			int c;
			String code = "";
			while (code.length()<length) {
				if ((c = r.nextInt(100)) >= 64) {
					if(i%2==0)
					{
						if(((char)c >= 'a' && (char)c <= 'z') || ((char)c >= 'A' && (char)c <= 'Z')){
							if(((char)c != 'O' && (char)c !='o') && ((char)c != 'I' && (char)c != 'i'))
							{
								code += ((char)c);
							}else{
								code += c;
							}
						}
					}else{
						if((c+"").indexOf("1")<0 && (c+"").indexOf("0")<0)
							code += c;
					}
				}
				i++;
			}
			if(code.length() > length)
			{
				code = code.substring(0,length);
			}
			return code;
        }
        
        /**
         * 处理中文文件名中的空格并返回一个Unicode字符串，如果不超出15个中文，则返回UTF-8，否则返回GB2312
         * @param fileName 文件名
         * @return
         */
        public static String encodingFileName(String fileName) {
            String returnFileName = "";
            try {
                returnFileName = URLEncoder.encode(fileName, "UTF-8");
                returnFileName = replaceString(returnFileName, "+", "%20");
                if (returnFileName.length() > 150) {
                    returnFileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
                    returnFileName = replaceString(returnFileName, " ", "%20");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return returnFileName;
        }
        
        /**
         * 判断是否有中文
         * @param ch
         * @return
         */
        public static boolean isChinesrChar(char[] ch) {
	        for(int i = 0 ;i<ch.length;i++) {
		        int v = (int) ch[i]; 
		        if(v>=19968 && v <= 171941) { 
		        	return true;
		        } 
	        }
	        return false;
        }

        /**
         * 判断字符串中是否包含中文
         * @param str
         * 待校验字符串
         * @return 是否为中文
         * @warn 不能校验是否为中文标点符号
         */
        public static boolean isContainChinese(String str) {
                Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
                Matcher m = p.matcher(str);
                if (m.find()) {
                        return true;
                }
                return false;
        }

        /**
         * 匹配是否为数字
         * @param str 可能为中文，也可能是-19162431.1254，不使用BigDecimal的话，变成-1.91624311254E7
         */
        public static boolean isNumeric(String str) {
                // 该正则表达式可以匹配所有的数字 包括负数
                Pattern pattern = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
                String bigStr;
                try {
                        bigStr = new BigDecimal(str).toString();
                } catch (Exception e) {
                        return false;//异常 说明包含非数字。
                }

                Matcher isNum = pattern.matcher(bigStr); // matcher是全匹配
                if (!isNum.matches()) {
                        return false;
                }
                return true;
        }

        /**
         * 判断是否由字母
         * @param name
         * @return
         */
        public static boolean isLetter(String name) {
            if (name == null) {
                return false;
            }
            // 定义两个正则表达式
            String regex1 = "[a-zA-Z_0-9][a-zA-Z_0-9._% ]*[a-zA-Z0-9]";
            String regex2 = "[a-zA-Z]";
            Pattern p = null;
            Matcher m = null;
            if (name.length() > 1) {
                // 串的长度大于1时用regex1去匹配
                p = Pattern.compile(regex1);
                m = p.matcher(name);
                return m.matches();
            } else if (name.length() == 1) {
                // 串的长度为1时用regex2去匹配
                p = Pattern.compile(regex2);
                m = p.matcher(name);
                return m.matches();
            }
            return false;
        }
        
        //全角转半角
        public static String ToDBC(String input) {    
        	char[] c = input.toCharArray();    
        	for (int i = 0; i < c.length; i++) {    
        		if (c[i] == 12288) {    
        			c[i] = (char) 32;    
        			continue;    
        	   }    
        	   if (c[i] > 65280 && c[i] < 65375)    
        	    c[i] = (char) (c[i] - 65248);    
        	}    
        	return new String(c);    
    	}    
        
        public static String ToSBC(String input) {    
        	  // 半角转全角：    
        	char[] c = input.toCharArray();    
        	for (int i = 0; i < c.length; i++) {    
        		if (c[i] == 32) {    
        			c[i] = (char) 12288;    
        			continue;    
        		}    
        		if (c[i] < 127)    
        			c[i] = (char) (c[i] + 65248);    
    		}    
        	return new String(c);    
        }   
        //获取ip地址
        public static String getIpAddr(HttpServletRequest request) {
        	// 参考http://dpn525.iteye.com/blog/1132318
        	 String IP = request.getHeader("x-forwarded-for"); 
        	 if(IP == null || IP.length() == 0 || "unknown".equalsIgnoreCase(IP)) { 
        	   IP = request.getHeader("Proxy-Client-IP"); 
        	  } 
        	 if(IP == null || IP.length() == 0 || "unknown".equalsIgnoreCase(IP)) { 
        	   IP = request.getHeader("WL-Proxy-Client-IP"); 
        	  } 
        	  if(IP == null || IP.length() == 0 || "unknown".equalsIgnoreCase(IP)) { 
        	   IP = request.getRemoteAddr(); 
        	  }
        	  if (IP != null) {
        		  if (IP.equals("0:0:0:0:0:0:0:1")) {
        			  IP = "127.0.0.1";
        		  } else {
	        		  String[] ips = IP.split(",");
		        	  for (String ip: ips){// 第一个就应该是客户端真实IP
		        		  IP = ip.trim();
	        			  if (IP.length()<=15) {
	        				  IP = ip;
	        			  }
		        		  if (!(IP.startsWith("192.")) && !(IP.startsWith("10."))) {
		        			  break;
		        		  }
		        	  }
        		  }
        	  }
        	  return IP;
        	}

    /**
      * 方法描述：验证字符串是否无效。如果字符串为null或者剪切首尾空格后为空字符串，那么返回true；否则返回false。
      * @param str
      * @return
      * @author: Huyihui
      * @version: 2012-10-15 下午02:29:48
      */
    public static boolean invalid(String str) {
    	if (str != null && !str.trim().equals("")) {
    		return false;
    	}
    	return true;
    }
    
    /**
      * 方法描述：验证字符串是否有效。如果字符串不为null或者剪切首尾空格后不为空字符串，那么返回true；否则返回false。
      * @param str
      * @return
      * @author: Huyihui
      * @version: 2012-10-17 下午05:42:13
      */
    public static boolean valid(String str) {
    	return !invalid(str);
    }
    
    /**
      * 方法描述：验证一个字符串数组是否同时有效
      * @param str
      * @return
      * @author: Huyihui
      * @version: 2012-12-25 下午06:05:13
      */
    public static boolean valid(String... str) {
    	if (str != null && str.length > 0) {
    		for (String s : str) {
    			if (invalid(s)) {
    				return false;
    			}
    		}
    		return true;
    	} else {
    		return false;
    	}
    }
    
    /**
	  * 方法描述：当getOld为true时，返回在old中但不在current中的元素；反之返回在current但不在old中的元素
	  * @author: Huyihui
	  * @version: 2012-12-14 上午10:19:16
	  */
	public static List<String> getOldOrNew(String[] old, String[] current, boolean getOld) {
		if (old != null && current != null) {
			if (getOld) {
				if (current.length == 0) {
					return asList(old);
				} else {
					List<String> list = new ArrayList<String>();
					for (String o : old) {
						boolean inC = false;
						for (String c : current) {
							if (c.equals(o)) {
								inC = true;
								break;
							}
						}
						if (!inC) {
							list.add(o);
						}
					}
					return list;
				}
			} else {
				if (old.length == 0) {
					return asList(current);
				} else {
					List<String> list = new ArrayList<String>();
					for (String c : current) {
						boolean inO = false;
						for (String o : old) {
							if (o.equals(c)) {
								inO = true;
								break;
							}
						}
						if (!inO) {
							list.add(c);
						}
					}
					return list;
				}
			}
		}
		/*if (old != null && current != null) {
			List<String> oldList = asList(old);
			List<String> currList = asList(current);
			
			if (getOld) {
				oldList.removeAll(currList);
				return oldList;
			} else {
				currList.removeAll(oldList);
				return currList;
			}
		}*/
		return null;
	}
	
	/**
	  * 方法描述：将数组转换成java.util.ArrayList。注：使用Arrays.asList返回的是Arrays的内部类，不支持一些操作。
	  * @param <E>
	  * @param s
	  * @return
	  * @author: Huyihui
	  * @version: 2012-12-14 下午04:24:52
	  */
	public static <E> List<E> asList(E[] s) {
		if (s == null) {
			throw new NullPointerException();
		}
		List<E> list = new ArrayList<E>();
		for (E e : s) {
			list.add(e);
		}
		return list;
	}
	
	/**
	 * 
	  * 方法描述：判断是否为可转化int类型
	  * @param: 
	  * @return: 
	  * @author: wangw
	  * @version: 2012-12-18 下午06:04:54
	 */
	public static boolean isStringInt(String str){
	   try{
	       Integer.parseInt(str);
	     }catch(Exception e){
	       return false;
	    }
	  return true;
	}
	
	/**
	  * 方法描述：将HTML中的特殊字符转换为html字符实体
	  * @param html
	  * @return
	  * @author: Huyihui
	  * @version: 2012-12-29 下午04:38:36
	  */
	public static String convertHtml(String html) {
		String str = html;
		str = str.replaceAll("&", "&amp;");
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("\"", "&quot;");
		return str;
	}
	
	/**
	  * 方法描述：将字符串解析为整数。如果字符串无效（为null或者空字符串或者不是一个有效的整数形式）返回0。
	  * @param str
	  * @return
	  * @author: Huyihui
	  * @version: 2013-3-26 下午03:38:02
	  */
	public static int getInt(String str) {
		int value = 0;
		if (valid(str) && !"null".equals(str) && !"undefined".equals(str)) {
			try {
				value = Integer.parseInt(str);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return value;
	}
	
	/**
	  * 方法描述：将字符串解析为整数。如果字符串无效（为null或者空字符串或者不是一个有效的整数形式）返回defVal。
	  * @param str
	  * @param defVal
	  * @return
	  * @author: Huyihui
	  * @version: 2013-3-26 下午03:41:35
	  */
	public static int getInt(String str, int defVal) {
		int value = defVal;
		if (valid(str) && !"null".equals(str)) {
			try {
				value = Integer.parseInt(str);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return value;
	}
	
	/**
	  * 方法描述：将字符串解析为浮点数。如果字符串无效（为null或者空字符串或者不是一个有效的浮点数形式）返回0。
	  * @param str
	  * @return
	  * @author: Huyihui
	  * @version: 2013-3-26 下午03:42:01
	  */
	public static float getFloat(String str) {
		float value = 0f;
		if (valid(str)) {
			try {
				value = Float.parseFloat(str);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return value;
	}
	/**
	  * 方法描述：将字符串解析为长整数。如果字符串无效（为null或者空字符串或者不是一个有效的长整数形式）返回0。
	  * @param str
	  * @return
	  * @author: Huyihui
	  * @version: 2013-3-26 下午03:46:49
	  */
	public static long getLong(String str) {
		long value = 0L;
		if (valid(str)) {
			try {
				value = Long.parseLong(str);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return value;
	}
	/**
	  * 方法描述：将字符串按照指定的分隔符分割成数组
	  * @param str
	  * @param separator
	  * @return
	  * @author: Huyihui
	  * @version: 2013-3-26 下午03:51:58
	  */
	public static String[] getArray(String str, String separator) {
		if (valid(str)) {
			return str.split(separator);
		} else {
			return null;
		}
	}
	/**
	  * 方法描述：返回参数字符串。如果参数字符串无效（为null或者是空字符串）则返回传入的默认值。
	  * @param str
	  * @param defVal
	  * @return
	  * @author: Huyihui
	  * @version: 2013-3-26 下午03:52:22
	  */
	public static String getString(String str, String defVal) {
		if (invalid(str)) {
			return defVal;
		} else {
			return str;
		}
	}
	
	/**
	 * 方法描述： 判断是否是有效的email地址
	 * @param emailAddr
	 * @return
	 * @author WangJialu
	 * @version Feb 26, 2013 2:59:40 PM
	 */
	public static boolean isEmailAddr(String emailAddr) {
		if (emailAddr == null || emailAddr.trim().length() == 0) {
			return false;
		}
		Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher m = p.matcher(emailAddr);
		return m.matches();
	}
	
	/**
	 * 方法描述：判断是否是手机号码
	 * @param mobiles
	 * @return
	 * @author WangJialu
	 * @version May 22, 2013 10:07:35 AM
	 */
	public static boolean isMobileNO(String mobiles) {
		if (null == mobiles || mobiles.length() != 11) {
			return false;
		}
		Pattern p = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9])|(14[0-9])|(17[0-9])|(19[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	public static boolean iscard(String card){
		Pattern p = Pattern.compile("(^\\d{15}$)|(^\\d{17}(\\d|X)$)");
		Matcher m = p.matcher(card);
		return m.matches();
	}
	/**
	  * 方法描述：将数组转换成以指定分隔符隔开的字符串
	  * @param list
	  * @return
	  * @author: Huyihui
	  * @version: 2013-3-2 下午01:06:58
	  */
	public static String convert(List<Integer> list, String separator) {
		StringBuffer sb = new StringBuffer();
		if (list != null) {
			for (Integer i : list) {
				sb.append(i);
				sb.append(separator);
			}
		}
		return sb.toString();
	}

	/**
	 * 方法描述：TODO
	 * @param sb
	 * @return
	 * @author WangJialu
	 * @version May 27, 2013 3:43:20 PM
	 */
	public static void formatSQLValue(StringBuilder sb) {
		String s = sb.toString();
		s=s.replaceAll("'", "''").replaceAll("\\\\", "\\\\\\\\");
		sb.replace(0, sb.length(), s);
	}

	/**
	 * 方法描述：回避JSON的异常字符
	 * @param string
	 * @return
	 * @author WangJialu
	 * @version Jun 4, 2013 3:37:02 PM
	 */
	public static String escapeExceptionJSONChar(String string) {
		if (null == string) {
			return "";
		}
		return string.replace("\\", "\\\\").replace("\t", " ").replace("\r", " ").replace("\n", "<br>").replace("\"", "\\\"");
	}
	public static String removeExceptionJSONChar(String string) {
		if (null == string) {
			return "";
		}
		return string.replace("\\", "\\\\").replace("\t", "").replace("\r", "").replace("\n", "").replace("\"", "\\\"");
	}
	
	/**
	 * 方法描述：获取随机字符串
	 * @param length 随机字符串的长度
	 * @return
	 * @author WangJialu
	 * @version Jul 9, 2013 9:47:10 AM
	 */
	public static String getRandCode(int length) {
		if (length < 4) {
			return null;
		}
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		int size = StringUtil.RAND_STRING.length();
		for (int i = 0; i < length ;i++){
			sb.append(StringUtil.RAND_STRING.charAt(random.nextInt(size)));
		}
		return sb.toString();
	}
	
	/**
	 * 方法描述：获取随机字符串
	 * @param length 随机字符串的长度
	 * @return
	 * @author WangJialu
	 * @version Jul 9, 2013 9:47:10 AM
	 */
	public static String getRandNumCode(int length) {
		if (length < 4) {
			return null;
		}
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		int size = 10;
		for (int i = 0; i < length ;i++){
			sb.append(StringUtil.RAND_STRING.charAt(random.nextInt(size)));
		}
		return sb.toString();
	}

	/**
	 * 方法描述：获得某天的日期
	 * @param day 负数代表几天前，正数代表几天后
	 * @return
	 * @author WangJialu
	 * @version Jul 25, 2013 11:53:50 AM
	 */
	public static String getDateByDay(int day) {
		java.util.Calendar date = java.util.Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date.add(java.util.Calendar.DATE, day);
        return sdf.format(date.getTime());
	}
	
	/**
	 * 方法描述：验证符合yyyy-MM-dd格式的日期字符串
	 * @param date
	 * @return
	 * @author WangJialu
	 * @version Sep 9, 2013 5:33:52 PM
	 */
	public static boolean isDate(String date) {
		if (null == date || date.length() > 10|| date.length() < 8) {
			return false;
		}
		Pattern p = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))");
		Matcher m = p.matcher(date);
		return m.matches();
	}
	
	/**
	 * 方法描述：获得秒数
	 * @param time hh:mm:ss
	 * @return
	 * @author WangJialu
	 * @version Sep 9, 2013 5:33:52 PM
	 */
	public static int getSecond(String time) {
		String[] times = time.split(":");
		int second = ( getInt(times[0].trim())*60 + getInt(times[1].trim()) )*60 + getInt(times[2].trim());
		return second;
	}

	/**
	 * TODO 删除非法字符后的合法文件名
	 * @param fileName
	 * @return
	 * @author: WangJialu
	 * 2014-5-22 下午01:50:17
	 */
	public static String formatFileName(String fileName) {
		if (null != fileName) {
			fileName = stringFilter(fileName);
			fileName = fileName.replaceAll("ldquo", "").replaceAll("rdquo", "").replaceAll("quot", "")
					.replaceAll("lsquo", "").replaceAll("rsquo", "");
		}
		return fileName;
	}
	
	public static String stringFilter(String   str)   throws   PatternSyntaxException   {     
        // 只允许字母和数字       
        // String   regEx  =  "[^a-zA-Z0-9]";                     
        // 清除掉所有特殊字符  
		String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";  
		Pattern   p   =   Pattern.compile(regEx);     
		Matcher   m   =   p.matcher(str);     
		return   m.replaceAll("").trim();     
	}  
	
	/**
	 * 
	 * formatFolderName:格式化文件夹字符串. <br/>
	 *
	 * @author WangHuayong
	 * @param folderName
	 * @return
	 * Date:2015-2-11上午10:57:20
	 * @since JDK 1.6
	 */
	public static String formatFolderName(String folderName) {
		if (null != folderName) {
			folderName = folderName.replaceAll("([\\s\\a\\e\\<>\\|:\"\\*\\?\\\\/])", "");
		}
		return folderName;
	}

		/**
    	 * 方法描述：获取随机数字
    	 * @param length 随机字符串的长度
    	 */
    	public static String getRandomNumber(int length) {
    		if (length < 4) {
    			return null;
    		}
    		Random random = new Random();
    		StringBuilder sb = new StringBuilder();
    		int size = StringUtil.RAND_Integer.length();
    		for (int i = 0; i < length ;i++){
    			sb.append(StringUtil.RAND_Integer.charAt(random.nextInt(size)));
    		}
    		return sb.toString();
    	}
    	
    	/**
    	 * 获取url的主域名，比如 tjus.ulearing.cn，返回 ulearning.cn
    	 * @param url
    	 * @return
    	 */
    	public static String getDomain(String url) {
			String domain = null;
			Pattern p = Pattern.compile("(?<=http://|\\.)[^.]*?\\.(com|cn|net|org)",Pattern.CASE_INSENSITIVE);
			Matcher matcher = p.matcher(url);
			while (matcher.find()) {
				domain = matcher.group();
			}
			return domain;
		}
    	
    	public static String filterXSSUnlawfulChar(String crossSiteParam){
    		String result="";
    		if(crossSiteParam == null) return "";
    		if (crossSiteParam.length() == 0) return "";
    		result = crossSiteParam.replace("&", "");
    		result = result.replace(">", "");
    		result = result.replace("<", "");
    		result = result.replace("\"", "");
    		result = result.replace("'", "");
    		result = result.replace("(", "");
    		result = result.replace(")", "");
    		result = result.replace("=", "");		
    		return result;
    	}

        public static String filterXss(String value) {
                if (value != null) {
                        Pattern scriptPattern = Pattern.compile("<[\r\n| | ]*script[\r\n| | ]*>(.*?)</[\r\n| | ]*script[\r\n| | ]*>", Pattern.CASE_INSENSITIVE);
                        value = scriptPattern.matcher(value).replaceAll("");
                        scriptPattern = Pattern.compile("</[\r\n| | ]*script[\r\n| | ]*>", Pattern.CASE_INSENSITIVE);
                        value = scriptPattern.matcher(value).replaceAll("");
                        // Remove any lonesome <script ...> tag
                        scriptPattern = Pattern.compile("<[\r\n| | ]*script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                        value = scriptPattern.matcher(value).replaceAll("");
                        // Avoid eval(...) expressions
                        scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                        value = scriptPattern.matcher(value).replaceAll("");
                        // Avoid e-xpression(...) expressions
                        scriptPattern = Pattern.compile("e-xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                        value = scriptPattern.matcher(value).replaceAll("");
                        // Avoid javascript:... expressions
                        scriptPattern = Pattern.compile("javascript[\r\n| | ]*:[\r\n| | ]*", Pattern.CASE_INSENSITIVE);
                        value = scriptPattern.matcher(value).replaceAll("");
                        // Avoid vbscript:... expressions
                        scriptPattern = Pattern.compile("vbscript[\r\n| | ]*:[\r\n| | ]*", Pattern.CASE_INSENSITIVE);
                        value = scriptPattern.matcher(value).replaceAll("");
                        // Avoid onload= expressions
                        scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                        value = scriptPattern.matcher(value).replaceAll("");
                }
                return value;
        }

  		public static String FilterContent(String content){
  			String flt ="'|and|exec|insert|select|delete|update|count|*|%|chr|mid|master|truncate|char|declare|; |or|-|+|,"; 
  			String filter[] = flt.split("\\|"); 
  			for(int i=0; i<filter.length;i++ ){
  				content = content.replace(filter[i], ""); 
  			}
  			return content; 
  		}

		public static Boolean getBoolean(String string) {
			return Boolean.parseBoolean(string);
		}
		public static Boolean ValidURL(String str){
			Boolean valid=false;
			if(valid(str)){
				URL url;
				try {
					url = new URL(str);
					InputStream in = url.openStream();
					valid=true;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return valid;
		}


        public static String getNetFileSizeDescription(long size) {
                StringBuffer bytes = new StringBuffer();
                DecimalFormat format = new DecimalFormat("###.0");
                if (size >= 1024 * 1024 * 1024) {
                        double i = (size / (1024.0 * 1024.0 * 1024.0));
                        bytes.append(format.format(i)).append("GB");
                }
                else if (size >= 1024 * 1024) {
                        double i = (size / (1024.0 * 1024.0));
                        bytes.append(format.format(i)).append("MB");
                }
                else if (size >= 1024) {
                        double i = (size / (1024.0));
                        bytes.append(format.format(i)).append("KB");
                }
                else if (size < 1024) {
                        if (size <= 0) {
                                bytes.append("0B");
                        }
                        else {
                                bytes.append((int) size).append("B");
                        }
                }
                return bytes.toString();
        }

        public static String replaceBlank(String str) {
                String dest = "";
                if (str!=null) {
                        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                        Matcher m = p.matcher(str);
                        dest = m.replaceAll("");
                }
                return dest;
        }
       
        public static int[] StringToInt(String[] arrs){
        	int[] ints = new int[arrs.length];
        	for(int i=0;i<arrs.length;i++){
        		ints[i] = Integer.parseInt(arrs[i]);
        	}
        	return ints;
        }

        public static double strToDouble(String str){
                double num = 0;
                if(str != null && str != ""){
                        num = Double.parseDouble(str);
                }
                return num;
        }

        /**
         * 从字符串中提取数字
         * */
        public static String getNumberFromStr(String str) throws PatternSyntaxException {
                String regEx="[^0-9]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(str);
                return m.replaceAll("").trim();
        }
        
        /**
         * 过滤不可见的字符
         * @param content
         * @return
         */
        public static String filterSightlessStr(String content) {
                if (content != null && content.length() > 0) {
                        content = content.replaceAll("\\p{C}", "");//去掉unicode字符串中的不可见字符
                        char[] contentCharArr = content.toCharArray();
                        for (int i = 0; i < contentCharArr.length; i++) {
                                if (contentCharArr[i] < 0x20 || contentCharArr[i] == 0x7F) {
                                        contentCharArr[i] = 0x20;
                                }
                        }
                       return new String(contentCharArr);
                }
                return "";
        }

        public static String getExcelSheetName(String oldName){
                return oldName.replaceAll(":","").
                        replaceAll("/","").
                        replaceAll("\\\\","").
                        replaceAll("\\?","").
                        replaceAll("\\*","").
                        replaceAll("\\[","").
                        replaceAll("]","").
                        replaceAll("'","");
        }

        public static String getFileName(String oldName){
                return oldName.replaceAll(":","").
                        replaceAll("/","").
                        replaceAll("\\\\","").
                        replaceAll("\\?","").
                        replaceAll("\\*","").
                        replaceAll("\\.","").
                        replaceAll("]","").
                        replaceAll(" ","_");
        }
}
