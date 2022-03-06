package childrencare.app.service;

import childrencare.app.model.CustomerModel;
import childrencare.app.model.ReservationModel;
import childrencare.app.repository.UserRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;

import childrencare.app.repository.CustomerRepository;

@Service
public class CustomerService {
	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private UserRepository userRepository;
	
	public int getNewCustomerRegisterByLastDays(int numberOfDays) {
		return customerRepository.countNewCustomerRegisterByLastDays(numberOfDays);
	}
	
	public int getNewCustomerReservedByLastDays(int numberOfDays) {
		return customerRepository.countNewCustomerReservedByLastDays(numberOfDays);
	}

	public CustomerModel findCustomerByEmail(String email){
		return customerRepository.findByEmail(email);
	}

	public void addNewCustomer(String email){
		customerRepository.addNewCustomer(email);
	}


	public void updateInfo(String fullName, String mobile, boolean gender, String email){
		userRepository.updateInfo(fullName, mobile, gender, email);
	}
	
	public Page<CustomerModel> getCustomerPageinately(String search,int page , int size,
			int startBitRange,int endBitRange,List<String> sortProperties , Direction direction){
		if(page < 0) {
			page = 0;
		}
		Page<CustomerModel> customersPageable = customerRepository.findCustomerByStatusAndSearchQuery("%"+search+"%",
				startBitRange, endBitRange, PageRequest.of(page, size, Sort.by(direction,(String []) sortProperties.toArray()) ) );
		if(customersPageable.getTotalPages() > 0 && page >= customersPageable.getTotalPages()) {
			page = customersPageable.getTotalPages() - 1;
			customersPageable = customerRepository.findCustomerByStatusAndSearchQuery("%"+search+"%",
					startBitRange, endBitRange, PageRequest.of(page, size, Sort.by(direction,(String []) sortProperties.toArray()) ) );
		}
		
		return customersPageable;
	}

	//nghia's code

	public int saveCustomer(CustomerModel cus) {
		return customerRepository.save(cus).getCustomer_id();
	}

	public void insertToCus(int cStatus, String email) {
		customerRepository.insertToCus(cStatus, email);
	}


	public int lastIDCus() {
		return customerRepository.lastIDCus();
	}
}
