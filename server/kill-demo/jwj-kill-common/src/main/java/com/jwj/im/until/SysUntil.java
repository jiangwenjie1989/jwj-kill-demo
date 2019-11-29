package com.jwj.im.until;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SysUntil {
	private static final Logger logger = LoggerFactory.getLogger(SysUntil.class);
	private static String OS = System.getProperty("os.name").toLowerCase();  

	public static boolean isLinux(){  
		return OS.indexOf("linux")>=0;  
	}  

	public static boolean isMacOS(){  
		return OS.indexOf("mac")>=0&&OS.indexOf("os")>0&&OS.indexOf("x")<0;  
	}  

	public static boolean isMacOSX(){  
		return OS.indexOf("mac")>=0&&OS.indexOf("os")>0&&OS.indexOf("x")>0;  
	}  

	public static boolean isWindows(){  
		return OS.indexOf("windows")>=0;  
	} 

 



	/**
	 * 身份证中间变成 ****
	 * @param idNumber
	 * @return
	 */
	public static String centerForeFillStar(String idNumber){
		if(idNumber == null || "".equals(idNumber) ){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		sb.append(idNumber.substring(0, 6));
		sb.append("*************");
		sb.append(idNumber.substring(idNumber.length()-2, idNumber.length()));
		return sb.toString();
	} 


	/**
	 * 获取交易流水号
	 * @return
	 */
	public static String createTransactionNo() {
		String str = DateUntil.getNowTime(DateUntil.FORMAT_YYYMMDDHHMMSS_SSS);
		long id = Thread.currentThread().getId();
		String randomNum = ToolRandoms.getRandomNum(4);
		return str + id + randomNum;
	}


	public static boolean stringIsBlank(String str) {
		boolean flag=false;
		if("".equals(str)||str==null){
			flag=true;
		}
		return flag;
	}

	public static boolean IDCardValidate(String IDStr) throws Exception {          
		boolean tipInfo = true;// 记录错误信息  
		String Ai = "";  
		// 判断号码的长度 15位或18位  
		if (IDStr.length() != 15 && IDStr.length() != 18) {  
			// tipInfo = "身份证号码长度应该为15位或18位。";  
			return false;  
		}  


		// 18位身份证前17位位数字，如果是15位的身份证则所有号码都为数字  
		if (IDStr.length() == 18) {  
			Ai = IDStr.substring(0, 17);  
		} else if (IDStr.length() == 15) {  
			Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);  
		}  
		if (isNumeric(Ai) == false) {  
			//tipInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";  
			return false;  
		}  


		// 判断出生年月是否有效   
		String strYear = Ai.substring(6, 10);// 年份  
		String strMonth = Ai.substring(10, 12);// 月份  
		String strDay = Ai.substring(12, 14);// 日期  
		if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {  
			//tipInfo = "身份证出生日期无效。";  
			return false;  
		}  
		GregorianCalendar gc = new GregorianCalendar();  
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");  
		try {  
			if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150  
					|| (gc.getTime().getTime() - s.parse(  
							strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {  
				// tipInfo = "身份证生日不在有效范围。";  
				return false;  
			}  
		} catch (NumberFormatException e) {  
			e.printStackTrace();  
		} catch (java.text.ParseException e) {  
			e.printStackTrace();  
		}  
		if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {  
			//tipInfo = "身份证月份无效";  
			return false;  
		}  
		if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {  
			//tipInfo = "身份证日期无效";  
			return false;  
		}  


		// 判断地区码是否有效   
		Hashtable areacode = GetAreaCode();  
		//如果身份证前两位的地区码不在Hashtable，则地区码有误  
		if (areacode.get(Ai.substring(0, 2)) == null) {  
			// tipInfo = "身份证地区编码错误。";  
			return false;  
		}  

		if(isVarifyCode(Ai,IDStr)==false){  
			// tipInfo = "身份证校验码无效，不是合法的身份证号码";  
			return false;  
		}  


		return tipInfo;  
	}  


	/* 
	 * 判断第18位校验码是否正确 
	 * 第18位校验码的计算方式：  
	        　　1. 对前17位数字本体码加权求和  
	        　　公式为：S = Sum(Ai * Wi), i = 0, ... , 16  
	        　　其中Ai表示第i个位置上的身份证号码数字值，Wi表示第i位置上的加权因子，其各位对应的值依次为： 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2  
	        　　2. 用11对计算结果取模  
	        　　Y = mod(S, 11)  
	        　　3. 根据模的值得到对应的校验码  
	        　　对应关系为：  
	        　　 Y值：     0  1  2  3  4  5  6  7  8  9  10  
	        　　校验码： 1  0  X  9  8  7  6  5  4  3   2 
	 */  
	private static boolean isVarifyCode(String Ai,String IDStr) {  
		String[] VarifyCode = { "1", "0", "X", "9", "8", "7", "6", "5", "4","3", "2" };  
		String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7","9", "10", "5", "8", "4", "2" };  
		int sum = 0;  
		for (int i = 0; i < 17; i++) {  
			sum = sum + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);  
		}  
		int modValue = sum % 11;  
		String strVerifyCode = VarifyCode[modValue];  
		Ai = Ai + strVerifyCode;  
		if (IDStr.length() == 18) {  
			if (Ai.equals(IDStr) == false) {  
				return false;  

			}  
		}   
		return true;  
	}  


	/** 
	 * 将所有地址编码保存在一个Hashtable中     
	 * @return Hashtable 对象 
	 */  

	private static Hashtable GetAreaCode() {  
		Hashtable hashtable = new Hashtable();  
		hashtable.put("11", "北京");  
		hashtable.put("12", "天津");  
		hashtable.put("13", "河北");  
		hashtable.put("14", "山西");  
		hashtable.put("15", "内蒙古");  
		hashtable.put("21", "辽宁");  
		hashtable.put("22", "吉林");  
		hashtable.put("23", "黑龙江");  
		hashtable.put("31", "上海");  
		hashtable.put("32", "江苏");  
		hashtable.put("33", "浙江");  
		hashtable.put("34", "安徽");  
		hashtable.put("35", "福建");  
		hashtable.put("36", "江西");  
		hashtable.put("37", "山东");  
		hashtable.put("41", "河南");  
		hashtable.put("42", "湖北");  
		hashtable.put("43", "湖南");  
		hashtable.put("44", "广东");  
		hashtable.put("45", "广西");  
		hashtable.put("46", "海南");  
		hashtable.put("50", "重庆");  
		hashtable.put("51", "四川");  
		hashtable.put("52", "贵州");  
		hashtable.put("53", "云南");  
		hashtable.put("54", "西藏");  
		hashtable.put("61", "陕西");  
		hashtable.put("62", "甘肃");  
		hashtable.put("63", "青海");  
		hashtable.put("64", "宁夏");  
		hashtable.put("65", "新疆");  
		hashtable.put("71", "台湾");  
		hashtable.put("81", "香港");  
		hashtable.put("82", "澳门");  
		hashtable.put("91", "国外");  
		return hashtable;  
	}

	private static Pattern ISNUMERIC_PATTERN = Pattern.compile("[0-9]+");
	/**
	 * 判断字符串是否为数字,0-9重复0次或者多次
	 * @param strnum
	 * @return
	 */
	private static boolean isNumeric(String strnum) {
		Matcher isNum = ISNUMERIC_PATTERN.matcher(strnum);
		if (isNum.matches()) {  
			return true;  
		} else {  
			return false;  
		}  
	}

	private static Pattern ISDATE_PATTERN =  Pattern
			.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))?$");
	/** 
	 * 功能：判断字符串出生日期是否符合正则表达式：包括年月日，闰年、平年和每月31天、30天和闰月的28天或者29天 
	 *  
	 * @param strDate
	 * @return 
	 */  
	public static boolean isDate(String strDate) {  
		Matcher m = ISDATE_PATTERN.matcher(strDate);
		if (m.matches()) {  
			return true;  
		} else {  
			return false;  
		}  
	} 

	/**
	 * @Title: discernSex  
	 * @Description: 根据身份证识别男女 
	 * @param idCard 身份证号码
	 * @return (true 男 false 女)
	 * @return boolean
	 * @author ycs 
	 * @date 2018年9月20日
	 */
	public static boolean discernSex(String idCard){
		boolean flag = true;//男
		idCard = idCard.trim();
		if ( idCard !=null && !"".equals(idCard) &&  (idCard.length() == 15 || idCard.length() == 18)) {
			if (idCard.length() == 15 || idCard.length() == 18) {
				String lastidCard = idCard.substring(idCard.length() - 1, idCard.length());
				int sex;
				if (lastidCard.trim().toLowerCase().equals("x")||lastidCard.trim().toLowerCase().equals("e")) {
					flag = true;//男
				} else {
					sex = Integer.parseInt(lastidCard) % 2;
					if ( sex ==0 ) {
						flag = false;
					}
				}
			}
		}
		return flag;
	}

}
