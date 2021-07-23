package com.bnext.agenda.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.springframework.stereotype.Service;

import com.bnext.agenda.client.NeutrinoClient;
import com.bnext.agenda.data.dto.PhoneNeutrinoValidateDTO;
import com.bnext.agenda.exception.BusinessException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NeutrinoService {
	
	private NeutrinoClient neutrinoClient;
	
	public boolean phoneIsNoValid(String number) {
		
		PhoneNeutrinoValidateDTO ipInfo = this.neutrinoClient.ipInfo(findPublicIp());
		PhoneNeutrinoValidateDTO validatePhone = this.neutrinoClient.validatePhone(number, ipInfo.getCountryCode());
		
		return !validatePhone.isValid();
		
	}
	
	private String findPublicIp() {
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
