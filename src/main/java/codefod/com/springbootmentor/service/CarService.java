package codefod.com.springbootmentor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarService {

    private final EngineService engineService;

    public CarService(EngineService engineService) { // DI qua constructor
        this.engineService = engineService;
    }

    public void drive() {
        engineService.start();
        System.out.println("Car is moving...");
    }
}
