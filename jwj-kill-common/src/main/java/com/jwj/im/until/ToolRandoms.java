package com.jwj.im.until;

import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 随机数类
 * 
 */
public abstract class ToolRandoms {

	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(ToolRandoms.class);

	private static final Random random = new Random();

	// 定义验证码字符.去除了O、I、l、、等容易混淆的字母
	public static final char authCode[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'G', 'K', 'M', 'N', 'P', 'Q', 'R',
			'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'a', 'c', 'd', 'e', 'f', 'g', 'h', 'k', 'm', 'n', 'p', 'r', 's', 't',
			'u', 'v', 'w', 'x', 'y', '3', '4', '5', '7', '8' };

	// 定义数字验证码字符
	public static final char authNumCode[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	public static final int length = authCode.length;

	public static final int numLength = authNumCode.length;

	/**
	 * 生成验证码
	 * 
	 * @return
	 */
	public static char getAuthCodeChar() {
		return authCode[number(0, length)];
	}

	public static char getAuthNumCodeChar() {
		return authNumCode[number(0, numLength)];
	}

	/**
	 * 生成验证码
	 * 
	 * @return
	 */
	public static String getAuthCode(int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(authCode[number(0, length)]);
		}
		return sb.toString();
	}

	public static String getAuthNumCode(int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(authNumCode[number(0, length)]);
		}
		return sb.toString();
	}

	/**
	 * 获取UUID by jdk 2012-9-7 下午2:22:18
	 * 
	 * @return
	 */
	public static String getUuid(boolean is32bit) {
		String uuid = UUID.randomUUID().toString();
		if (is32bit) {
			return uuid.toString().replace("-", "");
		}
		return uuid;
	}

	/**
	 * 产生0--number的随机数,不包括num
	 * 
	 * @param number
	 *            数字
	 * @return int 随机数字
	 */
	public static int number(int number) {
		return random.nextInt(number);
	}

	/**
	 * 生成RGB随机数
	 * 
	 * @return
	 */
	public static int[] getRandomRgb() {
		int[] rgb = new int[3];
		for (int i = 0; i < 3; i++) {
			rgb[i] = random.nextInt(255);
		}
		return rgb;
	}

	/**
	 * 生成指定长度数字随机数
	 * 
	 * @param len
	 * @return
	 */
	public static String getRandomNum(int len) {
		String sRand = "";
		for (int i = 0; i < len; i++) {
			char tmp = getAuthNumCodeChar();
			sRand += tmp;
		}
		return sRand;
	}

	
	public static String getRandomNum() {
		return getRandomNum(4);
	}

	/*
	 *
	 * @Author ycs
	 * @Description 产生两个数之间的随机小数
	 * @Date 9:44 2018/12/7 0007
	 * @Param [min 最小值, max 最大值, scl 小数位]
	 * @return double
	 **/
	public static double getRandomDouble(double min,  double max, int scl){
		int pow = (int) Math.pow(10, scl);//指定小数位
		double one = Math.floor((Math.random() * (max - min) + min) * pow) / pow;
		return  one;
	}

	/*
	 *
	 * @Author ycs
	 * @Description  产生两个数之间的随机数
	 * @Date 9:49 2018/12/7 0007
	 * @Param [min 最小值, max 最大值]
	 * @return int
	 **/
	public static int number(int min, int max) {
		return min + random.nextInt(max - min);
	}

}
