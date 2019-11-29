package com.jwj.im.until.id;

import java.text.SimpleDateFormat;

/**
 * 全局唯一的ID 工厂 使用的是 Twitter-Snowflake 算法 可以参考
 * -----http://yuanhsh.iteye.com/blog/2209696 经过测试 在4个线程 同事不间断生产 1000W主键的时候
 * 性能和准确性都能得到保障 每秒能产生26W+的唯一主键
 * 
 * @author wangbo
 *
 */
public class IdWorker {

	final static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private final long workerId;
	private final static long twepoch = 1361753741828L;
	private long sequence = 0L;
	private final static long workerIdBits = 10L;
	public final static long maxWorkerId = -1L ^ -1L << workerIdBits;
	private final static long sequenceBits = 12L;

	private final static long workerIdShift = sequenceBits;
	private final static long timestampLeftShift = sequenceBits + workerIdBits;
	public final static long sequenceMask = -1L ^ -1L << sequenceBits;

	private long lastTimestamp = -1L;

	public IdWorker(final long workerId) {
		super();
		if (workerId > IdWorker.maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(String.format(
					"worker Id can't be greater than %d or less than 0",
					IdWorker.maxWorkerId));
		}
		this.workerId = workerId;
	}

	public synchronized long nextId() {
		long timestamp = this.timeGen();
		if (this.lastTimestamp == timestamp) {
			this.sequence = (this.sequence + 1) & IdWorker.sequenceMask;
			if (this.sequence == 0) {
				//System.out.println("###########" + sequenceMask);
				timestamp = this.tilNextMillis(this.lastTimestamp);
			}
		} else {
			this.sequence = 0;
		}
		if (timestamp < this.lastTimestamp) {
			try {
				throw new Exception(
						String.format(
								"Clock moved backwards.  Refusing to generate id for %d milliseconds",
								this.lastTimestamp - timestamp));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		this.lastTimestamp = timestamp;
		long nextId = ((timestamp - twepoch << timestampLeftShift))
				| (this.workerId << IdWorker.workerIdShift) | (this.sequence);
//		 System.out.println("timestamp:" + timestamp + ",timestampLeftShift:"
//		 + timestampLeftShift + ",nextId:" + nextId + ",workerId:"
//		 + workerId + ",sequence:" + sequence);
		return nextId;
	}

	private long tilNextMillis(final long lastTimestamp) {
		long timestamp = this.timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = this.timeGen();
		}
		return timestamp;
	}

	private long timeGen() {
		return System.currentTimeMillis();
	}
}
