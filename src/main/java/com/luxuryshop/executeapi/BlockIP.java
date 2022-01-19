package com.luxuryshop.executeapi;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luxuryshop.entities.BlockedIP;
import com.luxuryshop.repositories.BlockedIPRepository;


@Service
public class BlockIP {
	
	@Autowired
	BlockedIPRepository ipRepository;
	
	private final String LOCALHOST_IPV4 = "127.0.0.1";
	private final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";
	private static final long FAILED_LOGIN_TIMEOUT_PERIOD = 60000L;
	
	@Transactional
	public Boolean checkIp(HttpServletRequest request) {
		String ip = getClientIp(request);
		if(!ipRepository.findById(ip).isEmpty()) {
			BlockedIP bIP = ipRepository.getById(ip);
			Long now = System.currentTimeMillis();
			if (now - bIP.getLastLogin() >= FAILED_LOGIN_TIMEOUT_PERIOD) {
				bIP.setNumWrong(0);
				ipRepository.save(bIP);
				return true;
			}
			if (now - bIP.getLastLogin() < FAILED_LOGIN_TIMEOUT_PERIOD && bIP.getNumWrong() == 3 ) {
				return false;
			}
		} 
		return true;
	}

	@Transactional
	public void blockip(HttpServletRequest request) {
		String ip = getClientIp(request);
		if(!ipRepository.findById(ip).isEmpty()) {
			BlockedIP bIP = ipRepository.getById(ip);
			bIP.setNumWrong(bIP.getNumWrong() + 1);
			bIP.setLastLogin(System.currentTimeMillis());
		} else {
			BlockedIP bIP = new BlockedIP();
			bIP.setIp(ip);
			bIP.setLastLogin(System.currentTimeMillis());
			bIP.setNumWrong(1);
			ipRepository.save(bIP);
		}
	}
	
	private String getClientIp(HttpServletRequest request) {
		String ipAddress = request.getHeader("X-Forwarded-For");
		if(ipAddress == null || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		
		if(ipAddress == null || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		
		if(ipAddress == null || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if(LOCALHOST_IPV4.equals(ipAddress) || LOCALHOST_IPV6.equals(ipAddress)) {
				try {
					InetAddress inetAddress = InetAddress.getLocalHost();
					ipAddress = inetAddress.getHostAddress();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}
		}
		
		if(ipAddress != null 
				&& ipAddress.length() > 15
				&& ipAddress.indexOf(",") > 0) {
			ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
		}
		
		return ipAddress;
	}
}
