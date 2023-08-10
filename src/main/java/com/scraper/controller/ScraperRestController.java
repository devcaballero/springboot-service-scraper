package com.scraper.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scraper.service.ScraperService;


@RestController
public class ScraperRestController {
	
	@Autowired
	private ScraperService scraperService;


    public ScraperRestController(ScraperService scraperService) {
        this.scraperService = scraperService;
    }
    
    private int visitorCount = 0;

    @GetMapping("/visitors")
    public int getVisitorCount() {
        return visitorCount;
    }

    @PostMapping("/visitors")
    public int incrementVisitorCount() {
        visitorCount++;
        return visitorCount;
    }
	
    @GetMapping("/dolar")
    public ResponseEntity<?> obtenerInfoDolar() {
        String info = scraperService.obtenerCotizacionDolarVenta();
        String[] dataArray = info.split("\\@");  
        // Obtener los valores separados

        int value1 = Integer.parseInt(dataArray[0].replace("$", ""));
        int value2 = Integer.parseInt(dataArray[1].replace("$", ""));
        
        // Crear un objeto JSON con los datos
        Map<String, Object> response = new HashMap<>();
        response.put("oficial", value1);
        response.put("blue", value2);

        // Devolver el objeto JSON como ResponseEntity
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    
    @GetMapping("/temperatura")
    public ResponseEntity<?> obtenerInfoTemp() {
        String temperature = scraperService.obtenerTemperatura(); 
        // Obtener los valores separados

        // Crear un objeto JSON con los datos
        Map<String, Object> response = new HashMap<>();
        response.put("temperature", temperature);

        // Devolver el objeto JSON como ResponseEntity
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    
    @GetMapping("/oro")
    public ResponseEntity<?> obtenerCotizacionOro() {
        String oro = scraperService.obtenerCotizacionOro(); 
       
        // Crear un objeto JSON con los datos
        Map<String, Object> response = new HashMap<>();
        response.put("gold", oro);

        // Devolver el objeto JSON como ResponseEntity
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    
    @GetMapping("/nafta")
    public ResponseEntity<?> obtenerPrecioNafta() {
        String nafta = scraperService.obtenerPrecioNaftaSuper(); 
       
        // Crear un objeto JSON con los datos
        Map<String, Object> response = new HashMap<>();
        response.put("nafta", nafta);

        // Devolver el objeto JSON como ResponseEntity
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    
    @GetMapping("/bigmac")
    public ResponseEntity<?> obtenerPrecioBigMac() {
        String bigmac = scraperService.obtenerPrecioBigMac(); 
       
        // Crear un objeto JSON con los datos
        Map<String, Object> response = new HashMap<>();
        response.put("bigmac", bigmac);

        // Devolver el objeto JSON como ResponseEntity
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    
    
    @GetMapping("/heineken")
    public ResponseEntity<?> obtenerPrecioHeineken() {
        String heineken = scraperService.obtenerPrecioHeinekenLitro(); 
       
        // Crear un objeto JSON con los datos
        Map<String, Object> response = new HashMap<>();
        response.put("heineken", heineken);

        // Devolver el objeto JSON como ResponseEntity
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/cocacola")
    public ResponseEntity<?> obtenerPrecioCocaCola() {
        String cocaCola = scraperService.obtenerPrecioCocaColaLitroYMedio(); 
       
        // Crear un objeto JSON con los datos
        Map<String, Object> response = new HashMap<>();
        response.put("cocacola", cocaCola);

        // Devolver el objeto JSON como ResponseEntity
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/forrosprime")
    public ResponseEntity<?> obtenerPrecioForrosPrime() {
        String forros = scraperService.obtenerPrecioForrosPrime(); 
       
        // Crear un objeto JSON con los datos
        Map<String, Object> response = new HashMap<>();
        response.put("forros", forros);

        // Devolver el objeto JSON como ResponseEntity
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/minimosube")
    public ResponseEntity<?> obtenerTarifaMinimaSube() {
        String minimosube = scraperService.obtenerTarifaMinimaSube(); 
       
        // Crear un objeto JSON con los datos
        Map<String, Object> response = new HashMap<>();
        response.put("minimosube", minimosube);

        // Devolver el objeto JSON como ResponseEntity
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/inflacionanualizada")
    public ResponseEntity<?> obtenerInflacionAnualizada() {
        String inflacionanual = scraperService.obtenerInflacionAnualizada(); 
       
        // Crear un objeto JSON con los datos
        Map<String, Object> response = new HashMap<>();
        response.put("inflacionanual", inflacionanual);

        // Devolver el objeto JSON como ResponseEntity
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/phillipbox")
    public ResponseEntity<?> obtenerPrecioPhillipBox() {
        String phillipbox = scraperService.obtenerPrecioPhillipBox(); 
       
        // Crear un objeto JSON con los datos
        Map<String, Object> response = new HashMap<>();
        response.put("phillipbox", phillipbox);

        // Devolver el objeto JSON como ResponseEntity
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    
}
