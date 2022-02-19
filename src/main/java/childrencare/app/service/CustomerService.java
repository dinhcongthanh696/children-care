package childrencare.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import childrencare.app.repository.CustomerRepository;

@Service
public class CustomerService {
	@Autowired
	private CustomerRepository customerRepository;
	
	public int getNewCustomerRegisterByLastDays(int numberOfDays) {
		return customerRepository.countNewCustomerRegisterByLastDays(numberOfDays);
	}
	
	public int getNewCustomerReservedByLastDays(int numberOfDays) {
		return customerRepository.countNewCustomerReservedByLastDays(numberOfDays);
	}
}
