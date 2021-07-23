package com.bnext.agenda.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bnext.agenda.data.dto.PhoneNeutrinoValidateDTO;

@FeignClient(value = "neutrino", url = "${neutrino.api.baseUrl}")
public interface NeutrinoClient {

	@GetMapping("/phone-validate")
	PhoneNeutrinoValidateDTO validatePhone(@RequestParam("number") String number, @RequestParam("country-code") String countryCode);

	@GetMapping("/ip-info")
	PhoneNeutrinoValidateDTO ipInfo(@RequestParam("ip") String ip);
   
}
