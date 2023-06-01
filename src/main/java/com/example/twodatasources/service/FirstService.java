package com.example.twodatasources.service;


import com.example.twodatasources.model.first.First;
import com.example.twodatasources.model.second.Second;
import com.example.twodatasources.repository.first.FirstRepository;
import com.example.twodatasources.repository.second.SecondRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FirstService {

    @Autowired
    FirstRepository firstRepository;

    @Autowired
    SecondRepository secondRepository;

    public String add() {

        First first = new First( 0L,"Blessing");
        Second second = new Second( 0L,"Blessing", "Manjozi");

        firstRepository.save(first);
        secondRepository.save(second);

        return "ADDED";
    }

}
