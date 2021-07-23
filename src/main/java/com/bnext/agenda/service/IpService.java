package com.bnext.agenda.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.springframework.stereotype.Service;

import com.bnext.agenda.exception.BusinessException;

@Service
public class IpService {
	
	public String findPublicIp() {
        try
        {
            URL url_name = new URL("http://bot.whatismyipaddress.com");
            BufferedReader sc =  new BufferedReader(new InputStreamReader(url_name.openStream()));
            return sc.readLine().trim();
        }
        catch (Exception e)
        {
            throw new BusinessException("Error ip public");
        }
	}

}
