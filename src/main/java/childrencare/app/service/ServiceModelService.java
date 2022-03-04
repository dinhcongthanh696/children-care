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
import childrencare.app.repository.FeedbackRepository;
import childrencare.app.repository.ServiceRepository;

@Service
public class ServiceModelService {
	private final ServiceRepository serviceRepository;
	private final FeedbackRepository feedbackRepository;

	public ServiceModelService(ServiceRepository serviceRepository,FeedbackRepository feedbackRepository) {
		this.serviceRepository = serviceRepository;
		this.feedbackRepository = feedbackRepository;
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
		
		for(ServiceModel service : servicesPageable.toList()) {
			service.setBase64ThumbnailEncode(service.getThumbnail());
			double averageStars = 0;
			boolean isHavingFeedback = false;
			for(FeedbackModel feedback : service.getFeedbacks()) {
				averageStars += feedback.getRatedStart();
				isHavingFeedback = true;
			}
			if(averageStars != 0) {
				averageStars /= service.getFeedbacks().size(); 
				service.setAvg_star(averageStars);
			}else if(isHavingFeedback) {
				service.setAvg_star(0);
			}
		}
		return servicesPageable;
	}
	
	public Page<ServiceModel> getServicesPaginated(int page , int size , String search , String... sortProperties) {
		if (page < 0) {
			page = 0; 
		}
		Page<ServiceModel> servicesPageable = 
		serviceRepository.findByTitleOrBriefInfoLike("%"+search+"%",PageRequest.of(page, size,Sort.by(Sort.Direction.DESC,sortProperties)));
		if(servicesPageable.getTotalPages() > 0 && page >= servicesPageable.getTotalPages()) {
			page = servicesPageable.getTotalPages() - 1;
			servicesPageable = serviceRepository.findByTitleOrBriefInfoLike("%"+search+"%",PageRequest.of(page, size));
		}
		for(ServiceModel service : servicesPageable.toList()) {
			service.setBase64ThumbnailEncode(service.getThumbnail());
			double averageStars = 0;
			boolean isHavingFeedback = false;
			for(FeedbackModel feedback : service.getFeedbacks()) {
				averageStars += feedback.getRatedStart();
				isHavingFeedback = true;
			}
			if(averageStars != 0) {
				averageStars /= service.getFeedbacks().size(); 
				service.setAvg_star(averageStars);
			}else if(isHavingFeedback) {
				service.setAvg_star(0);
			}
		}
		
		return servicesPageable;
	}
	
	public Page<ServiceModel> getServicesPaginatedAndFeedbacksByLastDays(int page , int size , int numberOfDays , String... sortProperties) {
		if (page < 0) {
			page = 0; 
		}
		Page<ServiceModel> servicesPageable = 
		serviceRepository.findByTitleOrBriefInfoLike("%%",PageRequest.of(page, size,Sort.by(Sort.Direction.DESC,sortProperties)));
		if(servicesPageable.getTotalPages() > 0 && page >= servicesPageable.getTotalPages()) {
			page = servicesPageable.getTotalPages() - 1;
			servicesPageable = serviceRepository.findByTitleOrBriefInfoLike("%%",PageRequest.of(page, size));
		}
		List<FeedbackModel> feedbacks;
		for(ServiceModel service : servicesPageable.toList()) {
			service.setBase64ThumbnailEncode(service.getThumbnail());
			double averageStars = 0;
			boolean isHavingFeedback = false;
			feedbacks = feedbackRepository.findByServiceByLastDays(service.getServiceId(), numberOfDays);
			service.setFeedbacks(feedbacks);
			for(FeedbackModel feedback : service.getFeedbacks()) {
				averageStars += feedback.getRatedStart();
				isHavingFeedback = true;
			}
			if(averageStars != 0) {
				averageStars /= feedbacks.size(); 
				service.setAvg_star(averageStars);
			}else if(isHavingFeedback) {
				service.setAvg_star(0);
			}
		}
		
		return servicesPageable;
	}
	public Page<ServiceModel> getServicesPaginated(int page , int size , int serviceCategoryId ,int startBitRange , int endBitRange, String search) {
		if (page < 0) {
			page = 0; 
		}
		Page<ServiceModel> servicesPageable = serviceRepository.findByTitleLikeAndCategory("%"+search+"%", serviceCategoryId,startBitRange , endBitRange , PageRequest.of(page, size));
		if( servicesPageable.getTotalPages() > 0 && page >= servicesPageable.getTotalPages()) {
			page = servicesPageable.getTotalPages() - 1;
			servicesPageable = serviceRepository.findByTitleLikeAndCategory("%"+search+"%", serviceCategoryId, startBitRange , endBitRange , PageRequest.of(page, size));
		}
		
		for(ServiceModel service : servicesPageable.toList()) {
			service.setBase64ThumbnailEncode(service.getThumbnail());
			double averageStars = 0;
			boolean isHavingFeedback = false;
			for(FeedbackModel feedback : service.getFeedbacks()) {
				averageStars += feedback.getRatedStart();
				isHavingFeedback = true;
			}
			if(averageStars != 0) {
				averageStars /= service.getFeedbacks().size(); 
				service.setAvg_star(averageStars);
			}else if(isHavingFeedback) {
				service.setAvg_star(0);
			}
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
		List<ServiceModel> services =  serviceRepository.findRatedServiceDescending(size);
		for(ServiceModel service : services) {
			service.setBase64ThumbnailEncode(service.getThumbnail());
		}
		return services;
	}
	
	public void editService(ServiceModel service) {
		if(service.getServiceCategory() == null) {
			serviceRepository.updateStatus(service.isStatus(), service.getServiceId());
		}else {
			serviceRepository.save(service);
		}
	}

	public List<ServiceModel> getServicesByReservationId(int reserID){
		return serviceRepository.findListServiceByReservationID(reserID);
	}

}
