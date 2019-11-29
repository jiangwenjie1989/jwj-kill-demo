package com.jwj.im.until;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

public class TokenUtil {

	/** token秘钥，请勿泄露，请勿随便修改 */
	public static final String SECRET = "NianFowakuangJwjKxf";
	/** token 过期时间: 一天 */
	public static final long EXPIRE_TIME = 24 * 60 * 60 * 1000;

	/**
	 * 生成token
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public static String createToken(Map<String, Object> hashMap) throws Exception {
		Date iatDate = new Date();
		// expire time
		Date expiresDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);

		// header Map
		Map<String, Object> map = new HashMap<>();
		map.put("alg", "HS256");
		map.put("typ", "JWT");

//		String token = JWT.create()
//				.withHeader(map) // header
//				.withClaim("iss", "Service") // payload
//				.withClaim("aud", "APP")
//				.withClaim("userId", userId.toString())
//				.withIssuedAt(iatDate) // sign time
//				.withExpiresAt(expiresDate) // expire time
//				.sign(Algorithm.HMAC256(SECRET)); // signature
		
		Builder withClaim = JWT.create()
		.withHeader(map) // header
		.withClaim("iss", "Service") // payload
		.withClaim("aud", "APP");
		for (String key : hashMap.keySet()) {
			withClaim.withClaim(key, hashMap.get(key).toString());
		}
		String token = withClaim.withIssuedAt(iatDate) // sign time
		.withExpiresAt(expiresDate) // expire time
		.sign(Algorithm.HMAC256(SECRET)); // signature
		return token;
	}

	/**
	 * 解密Token
	 * 
	 * @param token
	 * @return 不报错返回 true 报错返回false
	 * @throws Exception 
	 */
	public static boolean verifyToken(String token) {
		try {
			JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
			DecodedJWT jwt = verifier.verify(token);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * 根据Token获取解密信息
	 * 
	 * @param token
	 * @return key 键
	 */
	public static String getField(String token,String key) {
		try {
			JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
			DecodedJWT jwt = verifier.verify(token);
			Map<String, Claim> claims = jwt.getClaims();
			Claim user_id_claim = claims.get(key);
			return user_id_claim.asString();
		} catch (Exception e) {
			return null;
		}
	}



}
