package childrencare.app.service;

import childrencare.app.model.CustomerModel;
import childrencare.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

}
