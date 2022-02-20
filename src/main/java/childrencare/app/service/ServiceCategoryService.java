package childrencare.app.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import childrencare.app.model.ReservationServiceModel;
import childrencare.app.model.ServiceCategoryModel;
import childrencare.app.model.ServiceModel;
import childrencare.app.repository.ServiceCategoryRepository;

@Service
public class ServiceCategoryService {
	private final ServiceCategoryRepository serviceCategoryRepository;
	
	public ServiceCategoryService(ServiceCategoryRepository serviceCategoryRepository) {
		this.serviceCategoryRepository = serviceCategoryRepository;
	}
	
	public List<ServiceCategoryModel> findAll(){
		List<ServiceCategoryModel> categories = serviceCategoryRepository.findAll();
		for(ServiceCategoryModel category : categories) {
			double totalRevenue = 0;
			for(ServiceModel service : category.getServices()) {
				for(ReservationServiceModel reservationService : service.getReservationServices()) {
					totalRevenue += reservationService.getPrice();
				}
			}
			category.setTotalRevenue(totalRevenue);
		}
		
		return categories;
	}
	
	public List<ServiceCategoryModel> findByDate(Calendar calendar){
		int month = calendar.get(Calendar.MONTH) + 1; // 0 - based
		int year = calendar.get(Calendar.YEAR);
		List<ServiceCategoryModel> categories = serviceCategoryRepository.findAll();
		for(ServiceCategoryModel category : categories) {
			double totalRevenue = serviceCategoryRepository.getServiceRevenueByDate(month, year, category.getServiceCategoryId());
			category.setTotalRevenue(totalRevenue);
		}
		
		return categories;
	}
	
	public ServiceCategoryModel findById(int id){
		return serviceCategoryRepository.findById(id).get();
	}
}
