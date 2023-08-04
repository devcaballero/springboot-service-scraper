package com.scraper.service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import io.ous.jtoml.ParseException;

@Service
public class ScraperService {

	public String obtenerCotizacionDolarVenta() {

		StringBuilder retorno = new StringBuilder();

		try {
			// bloque dolar hoy

			// Obtener el documento HTML de la página web
			Document document = Jsoup.connect("https://dolarhoy.com/").get();

			// Encontrar el elemento que contiene el precio del dólar venta
			Element precioDolarElement = document.selectFirst(".venta .val");

			// Obtener el texto del elemento que contiene el precio
			String precioDolarBlueTexto = precioDolarElement.text();

			// Imprimir el precio del dólar venta
			// System.out.println("Precio del dólar venta: " + precioDolarTexto);

			// ------

			// bloque banco nacion
			Document doc = Jsoup.connect("https://www.bna.com.ar/").get();

			// Buscar la tabla "cotizacion"
			Element table = doc.select("table.table.cotizacion").first();

			// Buscar la fila correspondiente al dólar oficial
			Elements rows = table.select("tr");
			String cotizacionDolarOficial = null;
			for (Element row : rows) {
				if (row.text().contains("Dolar U.S.A")) {
					Elements cells = row.select("td");
					cotizacionDolarOficial = cells.get(2).text();
					break;
				}
			}

			// -----
			if (cotizacionDolarOficial != null) {
				String cotizacionDolarOficialVenta = cotizacionDolarOficial.replace(",", ".");
				double cotizacionDouble = Double.parseDouble(cotizacionDolarOficialVenta);
				DecimalFormat df = new DecimalFormat("#");
				String cotizacion = "$" + df.format(cotizacionDouble);
				System.out.println("Cotización del Dólar Oficial Venta: " + cotizacion);
				retorno.append(cotizacion);
			} else {
				System.out.println("No se encontró la cotización del dólar oficial.");
			}
			System.out.println("Cotización del Dólar Blue Venta: " + precioDolarBlueTexto);
			retorno.append("@" + precioDolarBlueTexto);

		} catch (Exception e) {
			e.printStackTrace();
		} 
		return retorno.toString();
	}
	
	
	
	public String obtenerTemperatura() {

		StringBuilder retorno = new StringBuilder();

		// seteamos el webdriver de selenium para hacer scrap a smn.gob
		ChromeOptions optionsDriver = new ChromeOptions();
		optionsDriver.addArguments("--headless");
		optionsDriver.addArguments("--disable-features=VizDisplayCompositor"); // Evitar mostrar ventanas
		WebDriver driver = new ChromeDriver(optionsDriver);

		try {
			// parte de temperatura
			String estadoTempDesc = null;
			driver.get("https://www.smn.gob.ar/");

			// Esperar 3 segundos para que se cargue la información
			Thread.sleep(3000);

			WebElement tempElement = driver
					.findElement(By.cssSelector("div.col-xs-6.col-sm-6.col-md-8 label.forecast_high"));
			if (tempElement != null) {
				estadoTempDesc = tempElement.getText();
			}

			System.out.println("Temperatura actual: " + estadoTempDesc);
			retorno.append(estadoTempDesc);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Cerrar el navegador en el bloque finally para asegurarte de que se cierre
			// incluso si ocurre una excepción
			if (driver != null) {
				driver.quit();
			}
		}
		return retorno.toString();
	}
	
	public String obtenerCotizacionOro() {
	      String url = "https://www.kitco.com/charts/livegold.html"; // URL de la página
	      String retorno = "";
	        try {
	            // Obtener el contenido HTML de la página
	            Document doc = Jsoup.connect(url).get();

	            // Buscar el elemento que contiene la cotización del oro en dólares
	            Element goldPriceElement = doc.select("span#sp-ask").first();

	            if (goldPriceElement != null) {
	                String goldPrice = goldPriceElement.text();
	                System.out.println(goldPrice);
	                // Formatear la cotización si es necesario
	                goldPrice = goldPrice.replace(",", "");

	                retorno = goldPrice.replace(".", ",");
	                System.out.println("Cotización del oro en dólares: " + goldPrice);
	            } else {
	                System.out.println("No se encontró la cotización del oro en la página.");
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	            // Manejo de errores en caso de falla de web scraping
	        }
	    	return retorno;
	    }
	
 	

    public String obtenerPrecioNaftaSuper() {
        String url = "https://surtidores.com.ar/precios/";
        String fuelType = "Super";
        try {
            Document document = Jsoup.connect(url).get();

            // Obtener la fecha actual
            Date today = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(today);
            int currentMonth = cal.get(Calendar.MONTH) + 1; // Sumamos 1 para obtener el mes en formato numérico
            int currentDay = cal.get(Calendar.DAY_OF_MONTH);

            // Restar 1 al mes actual si todavía no hemos pasado del día 15
            if (currentDay <= 15) {
                currentMonth--;
            }

            // Buscar la tabla con los precios
            Element table = document.selectFirst("table");
            Elements rows = table.select("tr");

            // Buscar la fila correspondiente al tipo de combustible
            Element fuelRow = null;
            for (Element row : rows) {
                if (row.text().contains(fuelType)) {
                    fuelRow = row;
                    break;
                }
            }

            if (fuelRow == null) {
                System.out.println("No se encontró información para el tipo de combustible: " + fuelType);
                return null;
            }

            // Obtener los precios del combustible 'Super' de la fila correspondiente
            Elements priceCells = fuelRow.select("td");

            // Verificar que el índice no esté fuera de los límites
            if (currentMonth < 1 || currentMonth >= priceCells.size()) {
                System.out.println("No se encontró información para el mes anterior.");
                return null;
            }

            // Obtener el precio del combustible 'Super' del mes anterior
            String price = priceCells.get(currentMonth).text().trim();

            if (price.isEmpty()) {
                System.out.println("El precio de la nafta 'Super' del mes anterior está vacío.");
                return null;
            }

            return price.replace(".", ",");
            
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    
    public String obtenerPrecioBigMac() {
        String url = "https://www.expatistan.com/es/precio/big-mac/buenos-aires";

        try {
            Document document = Jsoup.connect(url).get();

            // Buscar el elemento que contiene el precio del Big Mac
            Element precioElement = document.selectFirst("span.city-1");

            if (precioElement != null) {
                String precio = precioElement.text().trim();
                int startIndex = precio.indexOf("$") + 1;
                int endIndex = precio.length();
                precio = precio.substring(startIndex, endIndex).trim().replace(".", "");
                return precio;
            } else {
                System.out.println("No se encontró el precio del Big Mac en Buenos Aires");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    
    
    public String obtenerPrecioHeinekenLitro() {
        String url = "https://www.carrefour.com.ar/cerveza-rubia-heineken-retornable-1-l/p";

        try {
            Document document = Jsoup.connect(url).get();

            // Buscar el elemento que contiene el precio de la Heineken de litro
            Element precioElement = document.selectFirst("span.valtech-carrefourar-product-price-0-x-currencyInteger");

            if (precioElement != null) {
                String precio = precioElement.text().trim();
                return precio;
            } else {
                System.out.println("No se encontró el precio de la Heineken de litro en Carrefour");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    
    public String obtenerPrecioCocaColaLitroYMedio() {
        String url = "https://diaonline.supermercadosdia.com.ar/gaseosa-coca-cola-sabor-original-15-lts-16861/p";

        try {
            Document document = Jsoup.connect(url).get();

            // Buscar el elemento que contiene el precio de la Coca-Cola de 1.5 litros
            Element precioElement = document.selectFirst("span.vtex-product-price-1-x-currencyInteger");

            if (precioElement != null) {
                String precio = precioElement.text().trim();
                return precio;
            } else {
                System.out.println("No se encontró el precio de la Coca-Cola de 1.5 litros en DIA");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    public String obtenerPrecioForrosPrime() {
        String url = "https://www.farmalife.com.ar/prime-preserv-ultra-fino-x-3-/p";

        try {
            Document document = Jsoup.connect(url).get();

            // Buscar el elemento que contiene el precio del producto
            Element precioElement = document.selectFirst("strong.skuBestPrice");

            if (precioElement != null) {
                String precio = precioElement.text().trim();
                return precio.replace("$", "").trim();
            } else {
                System.out.println("No se encontró el precio del producto");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    public String obtenerTarifaMinimaSube() {
        String url = "https://www.argentina.gob.ar/redsube/tarifas-de-transporte-publico-amba-2021";

        try {
            Document document = Jsoup.connect(url).get();

            // Buscar el elemento que contiene la tarifa
            Elements tarifaElements = document.select("table.table tbody tr td");

            // Iterar sobre los elementos de la tabla y encontrar el que contiene la tarifa
            for (Element element : tarifaElements) {
                if (element.text().contains("$")) {
                    String tarifa = element.text().trim();
                    return tarifa.replace("$", "").trim();
                }
            }

            System.out.println("No se encontró la tarifa en la tabla");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    public String obtenerInflacionAnualizada() {
        String url = "http://estudiodelamo.com/inflacion-argentina-anual-mensual/";

        try {
            // Realizar la solicitud HTTP y obtener el contenido HTML de la página
            Document document = Jsoup.connect(url).get();

            // Buscar el elemento que contiene la información de inflación anualizada
            Element infoElement = document.select("div:containsOwn(y la anualizada es de)").first();

            // Obtener el texto dentro del elemento
            String infoText = infoElement.text();

            // Extraer la inflación anualizada del texto
            String inflation = extractInflacionAnualizada(infoText);

            return inflation;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String extractInflacionAnualizada(String text) {
        // Buscar la posición del porcentaje y extraer el valor numérico
        int startIndex = text.indexOf("anualizada es de") + 16;
        int endIndex = text.indexOf("%", startIndex);
        return text.substring(startIndex, endIndex).trim();
    }
    
    
    public String obtenerPrecioPhillipBox() {
    	try {
            // Definir la URL de la página a la que quieres hacer web scraping
            String url = "https://www.tarducciytordini.com.ar/nv/public/precios-de-cigarrillos";

            // Conectar a la página y obtener el contenido HTML
            Document doc = Jsoup.connect(url).get();

            // Buscar el elemento que contiene el nombre "PHILIP MORRIS BOX 20"
            Element philipMorrisBox20Element = doc.select("td:contains(PHILIP MORRIS BOX 20)").first();

            // Buscar el elemento que contiene el precio
            Element priceElement = philipMorrisBox20Element.nextElementSibling();

            // Obtener el texto del elemento que contiene el precio
            String precio = priceElement.text();

            return precio.replace("$", "").trim();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al obtener el precio";
        }
    }
    
    
}




	


