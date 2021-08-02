package shop.cashregister.model.items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.InvalidNameException;

import java.util.List;

import static java.text.MessageFormat.format;

@Service
public class OfferService{
    @Autowired
    private OfferRepository repository;

    public Offer save(Offer offer){
        return repository.save(offer);
    }

    public void delete(Offer offer){
        repository.delete(offer);
    }

    public List<Offer> getAll(){
        return repository.getAll();
    }

}
