/*package com.chanting.mining;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.CollectionUtils;
import com.chanting.mining.common.Commons;
import com.chanting.mining.common.RedisCacheKeys;
import com.chanting.mining.common.SysConfigKeys;
import com.chanting.mining.domain.sys.SysConfig;
import com.chanting.mining.domain.user.ForceRecord;
import com.chanting.mining.domain.user.User;
import com.chanting.mining.domain.user.UserAccount;
import com.chanting.mining.domain.user.UserForce;
import com.chanting.mining.domain.user.UserInviteRelation;
import com.chanting.mining.platform.PlatformApplication;
import com.chanting.mining.platform.dao.ForceRecordDao;
import com.chanting.mining.platform.dao.SysConfigDao;
import com.chanting.mining.platform.dao.UserAccountDao;
import com.chanting.mining.platform.dao.UserDao;
import com.chanting.mining.platform.dao.UserForceDao;
import com.chanting.mining.platform.dao.UserInviteRelationDao;
import com.chanting.mining.redis.RedisStringCacheSupport;
import com.chanting.mining.sys.ForceRecordSourceTypeE;
import com.chanting.mining.sys.UserAuthStatusE;
import com.chanting.mining.sys.UserBuyDigE;
import com.chanting.mining.sys.UserOpenDigE;
import com.chanting.mining.sys.UserStatusE;
import com.chanting.mining.until.DateUntil;
import com.chanting.mining.until.id.IdUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PlatformApplication.class)
@WebAppConfiguration
public class ApplicationTests {
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationTests.class);
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserForceDao userForceDao;
	@Autowired
	private UserAccountDao userAccountDao;
	@Autowired
	private RedisStringCacheSupport cacheStr;
	@Autowired
	private SysConfigDao sysConfigDao;
	@Autowired
	private ForceRecordDao forceRecordDao;
	@Autowired
	private UserInviteRelationDao userInviteRelationDao;
	
	@Test
	public void contextLoads() {
		Timestamp startT = DateUntil.getNowTimestamp();
		logger.info("虚构虚假用户开始：-------start："+DateUntil.getNowTime(DateUntil.FORMAT_YYYY_MM_DD_HH_MM_SS));
		Optional<SysConfig> regGiveForceSC = sysConfigDao.findByKeyName(SysConfigKeys.regGiveForce);
		if(regGiveForceSC.isPresent()){
			//注册赠送(加持力)
			Integer regGiveForce = Integer.valueOf(regGiveForceSC.get().getVueDate());
			//初始化注册手机号
			Long phone = 13104939510L;
			for (int i = 0; i < 50; i++) {
				Random ra =new Random();
				int nextInt = ra.nextInt(2);
				User user = new User();
				Timestamp nowTimestamp = DateUntil.getNowTimestamp();
				String cmInviteCode = cacheStr.incr(RedisCacheKeys.cmInitSerialNumber);
				user.setUsername("莲友"+cmInviteCode);
				user.setPhone(phone+"");
				user.setPhoneAddress("广东湛江市");
				user.setPassword("5abd06d6f6ef0e022e11b8a41f57ebda");
				user.setIdNumber(IdUtils.getId()+"");
				user.setRealName("杨测试");
				user.setSerialNumber(cmInviteCode);

				String head = Commons.userDefaultBoyHead;
				boolean flag = false;
				if ( nextInt == 1) flag = true;
				if ( !flag ) head = Commons.userDefaultGirlHead;
				
				user.setHead(head);
				user.setIsOneUpdateName(0);//是否第一次修改用户名 0表示没有修改过 1表示修改过
				user.setIpAddress("183.238.48.54");
				user.setUserAddress("中国广东湛江");
				user.setCreateTime(nowTimestamp);
				user.setUpdateTime(nowTimestamp);
				user.setIsMessageGive(0);//是否提示注册赠送ymb   0表示没有提示过消息 1表示提示过消息 
				user.setIsOpenDig(UserOpenDigE.未开启.value);//自动挖矿是否开启 0表示没有开启 1表示开启 
				user.setIsBuyDig(UserBuyDigE.未购买.value);//是否购买自动挖矿 0表示没有购买 1表示购买了  只有购买了才能开启自动挖矿	
				user.setStatus(UserStatusE.正常.value);
				user.setCertificationStatus(UserAuthStatusE.未认证.value);
				user.setGivingSuperiorForce(0);
				//保存用户信息
				User userDB = userDao.save(user);
				Long userId = userDB.getId();
				
				//保存注册用户加持力记录信息
				ForceRecord forceRecord = new ForceRecord();
				forceRecord.setCreateTime(nowTimestamp);
				forceRecord.setForceNum(regGiveForce);
				forceRecord.setSourceType(ForceRecordSourceTypeE.注册赠送.value);
				forceRecord.setUserId(userId);
				forceRecord.setSourceUserId(0L);
				
				//保存注册用户加持力记录信息
				forceRecordDao.save(forceRecord);

				//保存注册用户加持力账号信息
				UserForce userForce = new UserForce();
				userForce.setCreateTime(nowTimestamp);
				userForce.setForceUnm(regGiveForce);
				userForce.setUserId(userId);
				userForceDao.save(userForce);

				//保存注册用户GD账号信息
				UserAccount userAccount = new UserAccount();
				userAccount.setCreateTime(nowTimestamp);
				userAccount.setUserId(userId);
				userAccount.setTokenAmount(new  BigDecimal("0"));
				userAccountDao.save(userAccount);
				System.out.println("用户：" + phone);
				phone ++;
			}
		}
		logger.info("虚构虚假用户结束：  ------end："+DateUntil.getNowTime(DateUntil.FORMAT_YYYY_MM_DD_HH_MM_SS));
		logger.info("虚构虚假用户所需时间："+(DateUntil.getNowTimestamp().getTime()-startT.getTime())+"毫秒");
	}
	
	@Test
	public void contextLoads() {
		Timestamp startT = DateUntil.getNowTimestamp();
		logger.info("返回用户加持力开始：-------start："+DateUntil.getNowTime(DateUntil.FORMAT_YYYY_MM_DD_HH_MM_SS));
		Long userId = 27233L;
		List<User> lists = userInviteRelationDao.findUserInviteLists(userId);
		if ( !CollectionUtils.isEmpty(lists)) {
			Optional<UserForce> optUF = userForceDao.findByUserId(userId);
			if ( !optUF.isPresent() ){
				return;
			}
			Integer inviteForceUnm = 0;
			for (User user : lists) {
				Long sourceUserId = user.getId();
				Optional<ForceRecord> optFR = forceRecordDao.findByUserIdAndSourceTypeAndSourceUserId(userId, ForceRecordSourceTypeE.邀请好友.value, sourceUserId);
				if ( !optFR.isPresent() ) {
					ForceRecord forceRecord = new ForceRecord();
					forceRecord.setCreateTime(user.getCreateTime());
					forceRecord.setForceNum(user.getGivingSuperiorForce());
					forceRecord.setSourceType(ForceRecordSourceTypeE.邀请好友.value);
					forceRecord.setSourceUserId(sourceUserId);
					forceRecord.setUserId(userId);
					forceRecordDao.save(forceRecord);
					inviteForceUnm += user.getGivingSuperiorForce();
				}
			}
			UserForce userForce = optUF.get();
			userForce.setForceUnm(userForce.getForceUnm()+inviteForceUnm);
			userForceDao.save(userForce);
		}
		logger.info("返回用户加持力结束：  ------end："+DateUntil.getNowTime(DateUntil.FORMAT_YYYY_MM_DD_HH_MM_SS));
		logger.info("返回用户加持力所需时间："+(DateUntil.getNowTimestamp().getTime()-startT.getTime())+"毫秒");
	}

}
*/