package childrencare.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import childrencare.app.model.ServiceCategoryModel;
import childrencare.app.repository.ServiceCategoryRepository;

@Service
public class ServiceCategoryService {
	private final ServiceCategoryRepository serviceCategoryRepository;
	
	public ServiceCategoryService(ServiceCategoryRepository serviceCategoryRepository) {
		this.serviceCategoryRepository = serviceCategoryRepository;
	}
	
	public List<ServiceCategoryModel> findAll(){
		return serviceCategoryRepository.findAll();
	}
	
	public ServiceCategoryModel findById(int id){
		return serviceCategoryRepository.findById(id).get();
	}
}
