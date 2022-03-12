package childrencare.app.service;

import childrencare.app.model.Payment;
import childrencare.app.repository.PayRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PayService {
    private final PayRepository payRepository;

    public PayService(PayRepository payRepository){
        this.payRepository = payRepository;
    }

    public List<Payment> findAll(){
        return payRepository.findAll();
    }
}
