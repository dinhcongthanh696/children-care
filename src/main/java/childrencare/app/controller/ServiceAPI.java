package childrencare.app.controller;

import java.lang.reflect.Array;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import childrencare.app.model.ServiceModel;
import childrencare.app.service.ServiceModelService;

@RestController
@RequestMapping(value = "/service/api")
public class ServiceAPI {
	private final ServiceModelService serviceModelService;

	@Autowired
	public ServiceAPI(ServiceModelService serviceModelService) {
		this.serviceModelService = serviceModelService;
	}
	
	
	
	
}
