package com.jwj.im.redis;

import org.springframework.data.redis.connection.RedisZSetCommands.Tuple;

public class SimpleTuple implements Tuple {

	private byte[] value;
	
	private Double score;
	
	public SimpleTuple(byte[] value, Double score) {
		super();
		this.value = value;
		this.score = score;
	}

	@Override
	public int compareTo(Double o) {
		return getScore().compareTo(o);
	}

	@Override
	public byte[] getValue() {
		return value;
	}

	@Override
	public Double getScore() {
		return score;
	}
}
