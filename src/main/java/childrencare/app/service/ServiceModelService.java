package childrencare.app.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import childrencare.app.model.ServiceModel;
import childrencare.app.repository.ServiceRepository;

@Service
public class ServiceModelService {
	private final ServiceRepository serviceRepository;

	public ServiceModelService(ServiceRepository serviceRepository) {
		this.serviceRepository = serviceRepository;
	}

	// 0-based page
	public Page<ServiceModel> getServicesPaginated(int page, int size) throws Exception {
		if (page < 0) {
				throw new Exception("page must not be negative");
		}
		Page<ServiceModel> servicesPageable = serviceRepository.findAll(PageRequest.of(page, size));
		if(page > servicesPageable.getTotalPages()) {
			throw new Exception("over pages");
		}
		return servicesPageable;
	}
	
	public Page<ServiceModel> getServicesPaginated(int page , int size , String title) {
		Page<ServiceModel> servicesPageable = serviceRepository.findByTitleLike("%"+title+"%",PageRequest.of(page, size));
		return servicesPageable;
	}
	
	public Page<ServiceModel> getServicesPaginated(int page , int size , int serviceCategoryId , String title) {
		Page<ServiceModel> servicesPageable = serviceRepository.findByTitleLikeAndCategory("%"+title+"%", serviceCategoryId, PageRequest.of(page, size));
		return servicesPageable;
	}
	
	public Page<ServiceModel> getServicesPaginated(int page , int size , int serviceCategoryId) {
		Page<ServiceModel> servicesPageable = serviceRepository.findByTitleLikeAndCategory(serviceCategoryId, PageRequest.of(page, size));
		return servicesPageable;
	}
	
}
