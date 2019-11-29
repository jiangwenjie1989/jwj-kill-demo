package com.jwj.im.until.id;

import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class ObjectId implements Comparable<ObjectId>,Serializable{

	private static final Logger logger = LoggerFactory.getLogger(ObjectId.class);
			
	private static final long serialVersionUID = -4668474344178934085L;
	
	private final int time;
	private final int machine;
	private final int  inc;
	private boolean news;
	private static final int genmachine;
	
	private static AtomicInteger nextInt=new AtomicInteger(new Random().nextInt());
	
    public ObjectId() {
		time=(int) (System.currentTimeMillis()/1000);
		machine=genmachine;
		inc=nextInt.getAndIncrement();
		news=true;
	}
    
    /**
     * 获取一个新的  object id
     * @return the new id
     */
    public static ObjectId get(){
    	return new ObjectId();
    }
    
    /**
     * 检查  字符串 是否是一个 {@link ObjectId}
     * @param s
     * @return
     */
    public static boolean isValid(String s){
    	if(s==null){
    		return false;
    	}
    	final int len=s.length();
    	
    	for (int i = 0; i < len; i++) {
			char c=s.charAt(i);
			if (c >= '0' && c <= '9') {
	                continue;
			}
            if (c >= 'a' && c <= 'f') {
                continue;
            }
            if (c >= 'A' && c <= 'F') {
                continue;
            }
            return false;
		}
    	return false;
    }
    
    /**
     * Converts this instance into a 24-byte hexadecimal string representation.
     *
     * @return a string representation of the ObjectId in hexadecimal format
     */
    public String toHexString() {
        final StringBuilder buf = new StringBuilder(24);
        for (final byte b : toByteArray()) {
            buf.append(String.format("%02x", b & 0xff));
        }
        return buf.toString();
    }
    
    /**
     * Convert to a byte array.  Note that the numbers are stored in big-endian order.
     *
     * @return the byte array
     */
    public byte[] toByteArray() {
        byte b[] = new byte[12];
        ByteBuffer bb = ByteBuffer.wrap(b);
        // by default BB is big endian like we need
        bb.putInt(time);
        bb.putInt(machine);
        bb.putInt(inc);
        return b;
    }
	
    private int compareUnsigned(int i, int j) {
        long li = 0xFFFFFFFFL;
        li = i & li;
        long lj = 0xFFFFFFFFL;
        lj = j & lj;
        long diff = li - lj;
        if (diff < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        if (diff > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int) diff;
    }
    
	@Override
	public int compareTo(ObjectId id) {
		if (id == null) {
            return -1;
		}
        int x = compareUnsigned(time, id.time);
        if (x != 0){
            return x;
        }
        x = compareUnsigned(machine, id.machine);
        if (x != 0){
            return x;
        }
        return compareUnsigned(inc, id.inc);
	}
	
	/**
     * Gets the timestamp (number of seconds since the Unix epoch).
     *
     * @return the timestamp
     */
	public int getTimestamp(){
		return time;
	}
	
	/**
     * Gets the timestamp as a {@code Date} instance.
     *
     * @return the Date
     */
    public Date getDate() {
        return new Date(time * 1000L);
    }
	
    /**
     * Gets the current value of the auto-incrementing counter.
     *
     * @return the current counter value.
     */
    public static int getCurrentCounter() {
        return nextInt.get();
    }
	
    static {
    	 
        try {
            // build a 2-byte machine piece based on NICs info
            int machinePiece;
            {
                try {
                    StringBuilder sb = new StringBuilder();
                    Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
                    while (e.hasMoreElements()) {
                        NetworkInterface ni = e.nextElement();
                        sb.append(ni.toString());
                    }
                    machinePiece = sb.toString().hashCode() << 16;
                } catch (Throwable e) {
                    // exception sometimes happens with IBM JVM, use random
                    logger.error(e.getMessage(), e);
                    machinePiece = (new Random().nextInt()) << 16;
                }
                logger.info("machine piece post: " + Integer.toHexString(machinePiece));
            }
 
            // add a 2 byte process piece. It must represent not only the JVM but the class loader.
            // Since static var belong to class loader there could be collisions otherwise
            final int processPiece;
            {
                int processId = new Random().nextInt();
                try {
                    processId = java.lang.management.ManagementFactory.getRuntimeMXBean().getName().hashCode();
                } catch (Throwable t) {
                }
 
                ClassLoader loader = ObjectId.class.getClassLoader();
                int loaderId = loader != null ? System.identityHashCode(loader) : 0;
 
                StringBuilder sb = new StringBuilder();
                sb.append(Integer.toHexString(processId));
                sb.append(Integer.toHexString(loaderId));
                processPiece = sb.toString().hashCode() & 0xFFFF;
                logger.info("process piece: " + Integer.toHexString(processPiece));
            }
 
            genmachine = machinePiece | processPiece;
            logger.info("machine : " + Integer.toHexString(genmachine));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
 
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
 
        ObjectId that = (ObjectId) o;
 
        return Objects.equal(ObjectId.serialVersionUID, ObjectId.serialVersionUID) &&
                Objects.equal(ObjectId.logger, ObjectId.logger) &&
                Objects.equal(this.time, that.time) &&
                Objects.equal(this.machine, that.machine) &&
                Objects.equal(this.inc, that.inc) &&
                Objects.equal(this.news, that.news) &&
                Objects.equal(ObjectId.nextInt, ObjectId.nextInt) &&
                Objects.equal(ObjectId.genmachine, ObjectId.genmachine);
    }
 
    @Override
    public int hashCode() {
        return Objects.hashCode(serialVersionUID, logger, time, machine, inc, news,
                nextInt, genmachine);
    }
}
