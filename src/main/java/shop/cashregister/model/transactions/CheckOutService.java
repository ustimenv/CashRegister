package shop.cashregister.model.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckOutService{
    @Autowired
    private CheckOutRepository repository;

    public CheckOut save(CheckOut checkOut){
        return repository.save(checkOut);
    }

    public void delete(CheckOut checkOut){
        repository.delete(checkOut);
    }
}
