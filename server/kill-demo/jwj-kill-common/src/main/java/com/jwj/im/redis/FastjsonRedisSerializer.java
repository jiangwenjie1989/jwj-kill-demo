package com.jwj.im.redis;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;


public class FastjsonRedisSerializer<T> implements RedisSerializer<T>{

	
	@Override
	public byte[] serialize(T t) throws SerializationException {
		if (t == null) {
			return new byte[0];
		} 
		return JSON.toJSONBytes(t, 
				SerializerFeature.WriteBigDecimalAsPlain,
				SerializerFeature.WriteClassName,
				SerializerFeature.WriteNonStringValueAsString,
				SerializerFeature.IgnoreErrorGetter,
				SerializerFeature.IgnoreNonFieldGetter,
				SerializerFeature.WriteNullStringAsEmpty
			);
	}

	@Override
	public T deserialize(byte[] bytes) throws SerializationException {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
        
		return JSON.parseObject(bytes, new TypeReference<T>(){}.getType(), 
				Feature.IgnoreNotMatch,
				Feature.UseBigDecimal
				);
	}

}
