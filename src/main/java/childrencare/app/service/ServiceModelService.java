package childrencare.app.service;


import java.util.List;
import java.util.Optional;

import childrencare.app.model.FeedbackModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
	public Page<ServiceModel> getServicesPaginated(int page,int size, int start , int end , String search , String sortProperty)  {
		if (page < 0) {
				page = 0; 
		}
		Page<ServiceModel> servicesPageable = serviceRepository.findByTitleOrBriefInfoLikeAndStatus(
				"%"+search+"%", start, end,PageRequest.of(page, size , Sort.by(Direction.ASC, sortProperty))
				);
		if(servicesPageable.getTotalPages() > 0 && page >= servicesPageable.getTotalPages()) {
			page = servicesPageable.getTotalPages() - 1;
			servicesPageable = serviceRepository.findByTitleOrBriefInfoLikeAndStatus(
				"%"+search+"%", start, end,PageRequest.of(page, size , Sort.by(Sort.Direction.ASC,sortProperty))
					);
		}
		return servicesPageable;
	}
	
	public Page<ServiceModel> getServicesPaginated(int page , int size , String search) {
		if (page < 0) {
			page = 0; 
		}
		Page<ServiceModel> servicesPageable = serviceRepository.findByTitleOrBriefInfoLike("%"+search+"%",PageRequest.of(page, size));
		if(servicesPageable.getTotalPages() > 0 && page >= servicesPageable.getTotalPages()) {
			page = servicesPageable.getTotalPages() - 1;
			servicesPageable = serviceRepository.findByTitleOrBriefInfoLike("%"+search+"%",PageRequest.of(page, size));
		}
		return servicesPageable;
	}
	
	public Page<ServiceModel> getServicesPaginated(int page , int size , int serviceCategoryId , String search) {
		if (page < 0) {
			page = 0; 
		}
		Page<ServiceModel> servicesPageable = serviceRepository.findByTitleLikeAndCategory("%"+search+"%", serviceCategoryId, PageRequest.of(page, size));
		if( servicesPageable.getTotalPages() > 0 && page >= servicesPageable.getTotalPages()) {
			page = servicesPageable.getTotalPages() - 1;
			servicesPageable = serviceRepository.findByTitleLikeAndCategory("%"+search+"%", serviceCategoryId, PageRequest.of(page, size));
		}
		return servicesPageable;
	}
	
	public ServiceModel getServicesById(int id){
		return serviceRepository.findById(id).get();
	}
	
	public void addNewService(ServiceModel service) {
		serviceRepository.save(service);
	}
	
	public Optional<ServiceModel> getServiceById(int id){
		return serviceRepository.findById(id);
	}

	public List<ServiceModel> getServices(){
		return serviceRepository.findAll();
	}
	
	public List<ServiceModel> getHighestRatedStarServices(int size){
		return serviceRepository.findRatedServiceDescending(size);
	}
	
	public void editService(ServiceModel service) {
		if(service.getServiceCategory() == null) {
			serviceRepository.updateStatus(service.isStatus(), service.getServiceId());
		}else {
			serviceRepository.save(service);
		}
	}

}
