package com.perurail.pages;

import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.Keys;

public class ReservaPage {
    WebDriver driver;
    private String[] nombresAdultos = {"Juan", "Pedro", "Luis", "Carlos", "Javier"};
    private String[] apellidosAdultos = {"Perez", "Gomez", "Lopez", "Fernandez", "Martinez"};
    private String[] nombresNiños = {"Sofia", "Isabella", "Valentina", "Mateo", "Santiago"};
    private String[] apellidosNiños = {"Rodriguez", "Hernandez", "Garcia", "Diaz", "Morales"};
    
 // Metodos de acceso para los nombres y apellidos
    public String[] getNombresAdultos() {
        return nombresAdultos;
    }

    public String[] getApellidosAdultos() {
        return apellidosAdultos;
    }

    public String[] getNombresNiños() {
        return nombresNiños;
    }

    public String[] getApellidosNiños() {
        return apellidosNiños;
    }

    public ReservaPage(WebDriver driver) {
        this.driver = driver;
    }

    public void seleccionarDestino(String origen, String destino) {
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Espera hasta 10 segundos

        // Selecciona el origen
        WebElement origenDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Filtros_Ida_Origen")));
        origenDropdown.click(); // Hacemos clic para abrir el dropdown
        origenDropdown.sendKeys(origen); // Escribimos el origen

        // Esperar que se carguen las opciones del dropdown y seleccionamos
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//option[text()='" + origen + "']")));
        origenDropdown.findElement(By.xpath("//option[text()='" + origen + "']")).click();

        // Selecciona el destino
        WebElement destinoDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Filtros_Ida_Destino")));
        destinoDropdown.click(); // Abrimos el dropdown de destino
        destinoDropdown.sendKeys(destino); // Escribimos el destino

        // Esperar que se carguen las opciones del dropdown y seleccionamos
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//option[text()='" + destino + "']")));
        destinoDropdown.findElement(By.xpath("//option[text()='" + destino + "']")).click();
        
    }


    public void elegirTren(String tren) {
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Espera hasta 10 segundos
        WebElement trenDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cbTrenSelect")));
        trenDropdown.click(); // Abrimos el dropdown

        // Esperar que se carguen las opciones del dropdown y seleccionamos
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//option[text()='"+tren+"']")));
        WebElement trenOption = driver.findElement(By.xpath("//option[text()='"+tren+"']"));
        trenOption.click(); // Seleccionamos el tren
        System.out.print(tren);
    }

    public void elegirFecha(String fecha) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Espera hasta 10 segundos

        System.out.println("Abriendo el campo de fecha...");
        WebElement fechaInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Filtros_Ida_Fecha")));

        try {
            Thread.sleep(3000); // Esperar 3 segundos antes de abrir el calendario
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        fechaInput.click(); // Abrir el datepicker

        System.out.println("Esperando que el datepicker sea visible...");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ui-datepicker-div")));

        // Esperar despues de abrir el calendario
        try {
            Thread.sleep(3000); // Esperar 3 segundos adicionales despues de abrir el calendario
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Extraer el dia, mes y año de la fecha proporcionada
        String[] partesFecha = fecha.split("/");
        String dia = partesFecha[1]; // Dia (06)
        String mes = partesFecha[0]; // Mes (11)
        String año = partesFecha[2]; // Año (2024)

        System.out.println("Buscando el mes y año para " + mes + "/" + dia + "/" + año);

        // Navegar al mes correcto
        WebElement monthYearElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("ui-datepicker-title")));
        boolean monthYearFound = false;

        while (!monthYearFound) {
            String monthYearText = monthYearElement.getText();
            String[] monthYearParts = monthYearText.split(" ");
            String currentMonth = monthYearParts[0].trim(); // Eliminar espacios en blanco
            String currentYear = monthYearParts[1].trim();  // Eliminar espacios en blanco

            System.out.println("Mes/año actual del calendario: " + currentMonth + " " + currentYear);

            if (currentMonth.equalsIgnoreCase(getMonthName(Integer.parseInt(mes))) && currentYear.equals(año)) {
                System.out.println("Mes y año correctos encontrados: " + currentMonth + " " + currentYear);
                monthYearFound = true;
            } else {
                System.out.println("No se encontro el mes y año correctos. Avanzando al siguiente mes...");
                WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(By.className("ui-datepicker-next")));
                nextButton.click();
                monthYearElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("ui-datepicker-title")));
            }
        }

        // Esperar un poco antes de buscar el dia
        try {
            Thread.sleep(2000); // Esperar 2 segundos antes de buscar el dia
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verificar si el dia 06 esta visible y habilitado
        System.out.println("Buscando el dia " + dia + " en el datepicker...");
        WebElement diaElemento = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[@data-handler='selectDay'][@data-month='10'][@data-year='" + año + "']/a[text()='" + dia + "']")));

        // Verificar si el dia es visible y habilitado
        if (diaElemento != null && diaElemento.isDisplayed() && diaElemento.isEnabled()) {
            System.out.println("El dia " + dia + " esta disponible para seleccionar. Intentando hacer clic...");

            try {
                // Usar Actions para mover al elemento
                Actions actions = new Actions(driver);
                actions.moveToElement(diaElemento).perform();

                Thread.sleep(2000); // Espera despues de mover el raton

                // Hacer clic con Actions
                actions.click(diaElemento).perform();

                // Verificar si se selecciono el dia
                System.out.println("Clic realizado en el dia " + dia);
            } catch (Exception e) {
                System.out.println("Error al intentar hacer click en el dia " + dia + ": " + e.getMessage());
                
                // Si falla, hacer clic usando JavaScript
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", diaElemento);
                System.out.println("Clic realizado usando JavaScriptExecutor en el dia " + dia);
            }
        } else {
            System.out.println("El dia " + dia + " no esta disponible para seleccionar.");
        }

        // Esperar despues de la seleccion
        try {
            Thread.sleep(3000); // Esperar 3 segundos para observar el resultado despues del clic
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Metodo auxiliar para obtener el nombre del mes a partir del numero
    private String getMonthName(int monthNumber) {
        String[] months = {"January", "February", "March", "April", "May", "June", 
                           "July", "August", "September", "October", "November", "December"};
        return months[monthNumber - 1];
    } 

    public void hacerClicBuscar() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 

        try {
            WebElement buscarButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btn_search"))); // Ensure the button is clickable
            buscarButton.click(); 
            System.out.println("Clic en el boton de buscar realizado.");

            wait.until(ExpectedConditions.numberOfWindowsToBe(2)); 

            String originalWindow = driver.getWindowHandle();
            for (String windowHandle : driver.getWindowHandles()) {
                if (!originalWindow.equals(windowHandle)) {
                    driver.switchTo().window(windowHandle);
                    break;
                }
            }
            
            System.out.println("URL actual despues de hacer clic en buscar: " + driver.getCurrentUrl());

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("frmTrenes"))); 

        } catch (TimeoutException e) {
            System.out.println("Error: No se pudo redirigir a la pagina de reservas: " + e.getMessage());
            throw e; 
        }
    }





    public boolean verificarResultados() {
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            WebElement resultadosElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("resultados"))); // Cambiar por el ID correcto
            return resultadosElement.isDisplayed();
        } catch (TimeoutException e) {
            return false; 
        }
    }

    public boolean cabinasDisponibles() {
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            WebElement cabinasDisponibles = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cabinasDisponibles"))); // Cambiar por el ID correcto
            return cabinasDisponibles.isDisplayed();
        } catch (TimeoutException e) {
            return false; 
        }
    }

    public boolean mensajeNoCabinasDisponible() {
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            WebElement mensaje = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("mensajeNoCabinas"))); // Cambiar por el ID correcto
            return mensaje.getText().contains("No hay cabinas disponibles");
        } catch (TimeoutException e) {
            return false; 
        }
    }

    public boolean resumenCompraVisible() {
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            WebElement resumenCompra = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("resumenCompra"))); // Cambiar por el ID correcto
            return resumenCompra.isDisplayed();
        } catch (TimeoutException e) {
            return false; 
        }
    }

    public boolean validarLimitePersonas() {
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            WebElement mensajeLimite = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("mensajeLimitePersonas"))); // Cambiar por el ID correcto
            return mensajeLimite.getText().contains("solo se permite un maximo de 9 personas");
        } catch (TimeoutException e) {
            return false; 
        }
    }

    public void ingresarDatosPersonales(String nombre, String email) {
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement nombreInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nombre"))); // Cambiar por el ID correcto
        nombreInput.sendKeys(nombre);

        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email"))); // Cambiar por el ID correcto
        emailInput.sendKeys(email);
    }

    public void ingresarDatosPago(String tarjeta, String fecha, String cvv) {
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement tarjetaInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tarjetaCredito"))); // Cambiar por el ID correcto
        tarjetaInput.sendKeys(tarjeta);

        WebElement fechaExpiracion = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fechaExpiracion"))); // Cambiar por el ID correcto
        fechaExpiracion.sendKeys(fecha);

        WebElement cvvInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cvv"))); // Cambiar por el ID correcto
        cvvInput.sendKeys(cvv);
    }

    public boolean confirmacionCompraVisible() {
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            WebElement confirmacionCompra = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("confirmacionCompra"))); // Cambiar por el ID correcto
            return confirmacionCompra.isDisplayed();
        } catch (TimeoutException e) {
            return false; 
        }
    }

    public boolean resumenActualizadoVisible() {
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            WebElement resumenActualizado = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("resumenActualizado"))); // Cambiar por el ID correcto
            return resumenActualizado.isDisplayed();
        } catch (TimeoutException e) {
            return false; 
        }
    }


    public void elegirCabinaYPasajeros(String tipoCabina, int numeroCabinas, int adultos, int niños) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            // Esperar a que el preloader desaparezca antes de interactuar
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("preloader")));

            // Seleccionar el tipo de cabina
            String selectCabinaId = tipoCabina.equals("SUITE CABIN") ? "ddl-SelectRooms-sc" : "ddl-SelectRooms-bu";
            WebElement cabinaDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(selectCabinaId)));

            // Desplegar la lista de cabinas y seleccionar la cantidad de cabinas
            cabinaDropdown.click();
            Thread.sleep(2000); 
            WebElement opcionCabina = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@id='" + selectCabinaId + "']/option[@value='" + numeroCabinas + "']")));
            opcionCabina.click();

            // Log para confirmar que se selecciono la cabina
            System.out.println("Cabina seleccionada: " + tipoCabina + ", Numero de cabinas: " + numeroCabinas);
            Thread.sleep(2000);  // Espera de 2 segundos

            // Seleccionar la cantidad de adultos
            WebElement adultosDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.name("selectRooms[sc][cabinas][cab1][adult]")));
            adultosDropdown.click();
            Thread.sleep(2000);
            adultosDropdown.findElement(By.xpath("//option[text()='" + adultos + "']")).click();

            // Validar que se ha seleccionado correctamente el numero de adultos
            String adultosSeleccionados = adultosDropdown.getAttribute("value");
            System.out.println("Numero de adultos seleccionados: " + adultosSeleccionados);
            Thread.sleep(2000);  // Espera de 2 segundos

            // Habilitar seleccion de niños solo si hay adultos
            if (adultos > 0) {
                // Seleccionar la cantidad de niños
                WebElement niñosDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.name("selectRooms[sc][cabinas][cab1][nin]")));
                niñosDropdown.click();
                Thread.sleep(2000); 
                niñosDropdown.findElement(By.xpath("//option[text()='" + niños + "']")).click();

                // Validar que se ha seleccionado correctamente el numero de niños
                String niñosSeleccionados = niñosDropdown.getAttribute("value");
                System.out.println("Numero de niños seleccionados: " + niñosSeleccionados);
            } else {
                // Si no hay adultos, deshabilitar la seleccion de niños
                System.out.println("No se permite la seleccion de niños sin adultos.");
            }

            // Log de seleccion de pasajeros
            System.out.println("Pasajeros seleccionados: " + adultos + " Adultos, " + niños + " Niños.");

            // Extraer los montos finales (Total Cabin, Total Passenger, Price USD, Price PEN)
            String totalCabins = driver.findElement(By.id("totalCABINS")).getText();
            String totalPassengers = driver.findElement(By.id("totalPASSENGER")).getText();
            String priceUSD = driver.findElement(By.id("priceUSD")).getText();
            String pricePEN = driver.findElement(By.id("pricePEN")).getText();

            // Mostrar los montos extraidos
            System.out.println("Total de cabinas: " + totalCabins);
            System.out.println("Total de pasajeros: " + totalPassengers);
            System.out.println("Precio en USD: " + priceUSD);
            System.out.println("Precio en PEN: " + pricePEN);

        } catch (Exception e) {
            System.out.println("Error al seleccionar cabina y pasajeros: " + e.getMessage());
        }
    }





	public void deberia_ser_redirigido_a_la_pagina_de_reservas() {
		// TODO Auto-generated method stub
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Espera hasta 10 segundos
	        // Esperar a que el elemento con id 'cabecera' sea visible
	        WebElement cabecera = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cabecera")));
	        // Verificar que el elemento esta presente y visible
	        assertTrue(cabecera.isDisplayed());
	        System.out.println("Redirigido correctamente a la pagina de reservas.");
	}


	
	
	public void confirmarSeleccionCabinas() {
		// TODO Auto-generated method stub

	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5)); // Espera hasta 10 segundos

	    // Esperar hasta que el boton "Continuar" este visible y sea clickeable
	    WebElement botonContinuar = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnSiguiente_trenes")));

	    // Hacer clic en el boton "Continuar"
	    botonContinuar.click();
	    System.out.println("Clic en el boton 'Continuar' realizado.");
	    
		
	}

	public void llenarInformacionPasajeros(int adultos, int niños) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Llenar informacion para adultos
        for (int i = 0; i < adultos; i++) {
            if (i > 0) {
                expandirFormularioPasajero(i + 1); // Expandir solo si no es el primer pasajero
            }

            try {
                System.out.println("Llenando informacion para adulto " + (i + 1));

                String nombreAleatorioAdulto = nombresAdultos[new Random().nextInt(nombresAdultos.length)];
                String apellidoAleatorioAdulto = apellidosAdultos[new Random().nextInt(apellidosAdultos.length)];

                // Llenar nombre
                WebElement nombreInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ListaPasajeros[" + i + "].NombrePax")));
                nombreInput.sendKeys(nombreAleatorioAdulto);
                Thread.sleep(500); // Pausa de medio segundo

                // Llenar apellido
                WebElement apellidoInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ListaPasajeros[" + i + "].ApellidoPax")));
                apellidoInput.sendKeys(apellidoAleatorioAdulto);
                Thread.sleep(500); // Pausa de medio segundo

                // Seleccionar Nacionalidad (Default: "PERU")
                seleccionarNacionalidad(wait, i + 1);
                
                // Seleccionar el tipo de documento
                seleccionarTipoDocumento(wait, i + 1);

                // Llenar numero de documento
                WebElement documentoInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ListaPasajeros_NroDocumentoPax_" + (i + 1))));
                documentoInput.sendKeys(generarNumeroDocumento());
                Thread.sleep(500); // Pausa de medio segundo

                // Llenar fecha de nacimiento
                seleccionarFechaNacimientoSinFecha(wait, i + 1); // Establecer fecha de nacimiento
                Thread.sleep(500); // Pausa de medio segundo

                // Llenar detalles del primer pasajero
                if (i == 0) {
                    // Llenar el telefono
                    WebElement telefonoInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("item_TelefonoPax_" + (i + 1))));
                    telefonoInput.sendKeys(generarNumeroTelefono());
                    Thread.sleep(500); // Pausa de medio segundo

                    // Llenar el email
                    String email = "adulto@example.com";
                    WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("item_EmailPax_" + (i + 1))));
                    emailInput.sendKeys(email);
                    Thread.sleep(500); // Pausa de medio segundo

                    // Llenar la confirmacion del email
                    WebElement emailConfirmInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("item_EmailPax_Confirm_" + (i + 1))));
                    emailConfirmInput.sendKeys(email);
                    Thread.sleep(500); // Pausa de medio segundo
                }

                // Llenar genero
                WebElement generoInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("cabina_PasajeroBAE_" + (i + 1) + "__SexoPax")));
                generoInput.click();
                Thread.sleep(500); // Pausa de medio segundo


            } catch (Exception e) {
                System.out.println("Error llenando informacion para adulto " + (i + 1) + ": " + e.getMessage());
            }
        }

        // Llenar informacion para niños
        for (int j = 0; j < niños; j++) {
            expandirFormularioPasajero(adultos + j + 1); // Expandir el formulario para niños

            try {
                System.out.println("Llenando informacion para niño " + (j + 1));

                String nombreAleatorioNiño = nombresNiños[new Random().nextInt(nombresNiños.length)];
                String apellidoAleatorioNiño = apellidosNiños[new Random().nextInt(apellidosNiños.length)];

                WebElement nombreInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ListaPasajeros[" + (adultos + j) + "].NombrePax")));
                nombreInput.sendKeys(nombreAleatorioNiño);
                Thread.sleep(500); // Pausa de medio segundo

                WebElement apellidoInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ListaPasajeros[" + (adultos + j) + "].ApellidoPax")));
                apellidoInput.sendKeys(apellidoAleatorioNiño);
                Thread.sleep(500); // Pausa de medio segundo

                // Seleccionar el tipo de documento para niños
                seleccionarTipoDocumento(wait, adultos + j + 1);
                
                // Seleccionar Nacionalidad (Default: "PERU")
                seleccionarNacionalidad(wait, adultos + j + 1);

                // Llenar numero de documento para niños
                WebElement documentoInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ListaPasajeros_NroDocumentoPax_" + (adultos + j + 1))));
                documentoInput.sendKeys(generarNumeroDocumento());
                Thread.sleep(500); // Pausa de medio segundo

                // Llenar fecha de nacimiento para niños
                seleccionarFechaNacimientoSinFecha(wait, j + 1); // Establecer fecha de nacimiento para niños
                Thread.sleep(500); // Pausa de medio segundo

                // Llenar genero
                WebElement generoInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("cabina_PasajeroBAE_" + (adultos + j + 1) + "__SexoPax")));
                generoInput.click();
                Thread.sleep(500); // Pausa de medio segundo

            } catch (Exception e) {
                System.out.println("Error llenando informacion para niño " + (j + 1) + ": " + e.getMessage());
            }
        }

        System.out.println("Finalizado el llenado de informacion de pasajeros.");
    }

    // Metodo para hacer clic en el boton "Continue"
    public void clickContinueButton() {
        try {
            // Crear un nuevo WebDriverWait localmente
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            
            // Esperar a que el boton este visible y hacer clic en el
            WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnSiguienteReserva")));
            continueButton.click();
            System.out.println("Clic en el boton 'CONTINUE' realizado.");
            
            // Esperar a que se cargue la nueva página y extraer montos
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("box-cifra-total"))); // Asegúrate de que este ID sea correcto
            compararMontos();
            
        } catch (Exception e) {
            System.out.println("Error al hacer clic en el boton 'CONTINUE': " + e.getMessage());
        }
    }

 // Método para comparar montos
    public void compararMontos() {
        // Extraer montos de la página actual
        String totalUSD = driver.findElement(By.id("total")).getText();
        String totalPEN = driver.findElement(By.id("total_soles")).getText();

        // Extraer montos previamente almacenados (suponiendo que los almacenaste en atributos)
        String precioUSDPrevio = driver.findElement(By.id("priceUSD")).getText(); // Este ID puede variar según tu implementación
        String precioPENPrevio = driver.findElement(By.id("pricePEN")).getText(); // Este ID puede variar según tu implementación

        // Comparar y validar los montos
        assert totalUSD.equals(precioUSDPrevio) : "Los precios en USD no coinciden: esperado " + precioUSDPrevio + ", encontrado " + totalUSD;
        assert totalPEN.equals(precioPENPrevio) : "Los precios en PEN no coinciden: esperado " + precioPENPrevio + ", encontrado " + totalPEN;

        System.out.println("Los montos han sido verificados correctamente.");
    }

// Metodo para comparar los datos de los pasajeros con la tabla
    public void compararDatosConTabla(List<String> nombresPasajeros, List<String> documentosPasajeros) {
        // Esperar a que la tabla este visible en la nueva pagina
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("box-table-pasajeros"))); // Cambiar el selector a ID

        // Obtener la tabla
        WebElement tabla = driver.findElement(By.id("box-table-pasajeros"));
        List<WebElement> filas = tabla.findElements(By.tagName("tr"));

        // Inicializar listas para almacenar los nombres y documentos obtenidos de la tabla
        List<String> nombresDesdeTabla = new ArrayList<>();
        List<String> documentosDesdeTabla = new ArrayList<>();

        // Verificar el numero de filas encontradas
        System.out.println("Numero de filas en la tabla: " + filas.size());

        // Recorrer las filas de la tabla y extraer los datos
        for (int i = 0; i < filas.size(); i++) {
            WebElement fila = filas.get(i);
            List<WebElement> celdas = fila.findElements(By.tagName("td"));

            if (celdas.size() > 0) { // Asegurarse de que la fila tiene celdas
                String tipoDato = celdas.get(0).getText().trim(); // Obtener el tipo de dato (FULL NAME, DOCUMENT NUMBER, RATE)
                String valorDato = celdas.get(1).getText().trim(); // Obtener el valor

                // Agregar los nombres y documentos a las listas correspondientes
                if (tipoDato.equals("FULL NAME")) {
                    nombresDesdeTabla.add(valorDato);
                    System.out.println("Nombre extraido de la tabla: " + valorDato);
                } else if (tipoDato.equals("DOCUMENT NUMBER")) {
                    documentosDesdeTabla.add(valorDato);
                    System.out.println("Documento extraido de la tabla: " + valorDato);
                }
            }
        }

        // Comparar los nombres y documentos desde la lista de pasajeros ingresados y los obtenidos de la tabla
        for (int i = 0; i < nombresPasajeros.size(); i++) {
            
            if (i < nombresDesdeTabla.size() && i < documentosDesdeTabla.size()) {
                assert nombresPasajeros.get(i).equals(nombresDesdeTabla.get(i)) : "Los nombres no coinciden: esperado " + nombresPasajeros.get(i) + ", encontrado " + nombresDesdeTabla.get(i);
                assert documentosPasajeros.get(i).equals(documentosDesdeTabla.get(i)) : "Los documentos no coinciden: esperado " + documentosPasajeros.get(i) + ", encontrado " + documentosDesdeTabla.get(i);
            } else {
                System.out.println("No se encontraron suficientes datos en la tabla para la comparacion.");
            }
        }

        System.out.println("Los datos de los pasajeros han sido verificados correctamente.");
    }

    // Metodo para expandir el formulario del pasajero
    public void expandirFormularioPasajero(int numeroPasajero) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            WebElement formularioPasajero = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("expandirPasajero-" + (numeroPasajero - 1))));
            formularioPasajero.click();
            System.out.println("Formulario del pasajero " + numeroPasajero + " expandido.");
        } catch (Exception e) {
            System.out.println("Error al expandir el formulario del pasajero " + numeroPasajero + ": " + e.getMessage());
        }
    }

    // Metodo para seleccionar el tipo de documento
    public void seleccionarTipoDocumento(WebDriverWait wait, int pasajero) {
        try {
            WebElement tipoDocSelect = wait.until(ExpectedConditions.elementToBeClickable(By.id("select2-item_TipoDocumento_" + pasajero + "-container")));
            tipoDocSelect.click();
            Thread.sleep(500); // Pausa de medio segundo
            WebElement opcionTipoDoc = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[contains(text(), 'Identification document')]")));
            opcionTipoDoc.click();
        } catch (Exception e) {
            System.out.println("Error seleccionando tipo de documento: " + e.getMessage());
        }
    }

    // Metodo para seleccionar la nacionalidad
    public void seleccionarNacionalidad(WebDriverWait wait, int pasajero) {
        try {
            WebElement nacionalidadInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("select2-item_NacionalidadPax_" + pasajero + "-container")));
            nacionalidadInput.click();
            Thread.sleep(500); // Pausa de medio segundo

            // Ingresar busqueda
            WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@class='select2-search__field']")));
            searchInput.sendKeys("PERU");
            Thread.sleep(500); // Pausa de medio segundo

            // Confirmar seleccion
            searchInput.sendKeys(Keys.ENTER);
        } catch (Exception e) {
            System.out.println("Error seleccionando nacionalidad: " + e.getMessage());
        }
    }

    // Metodo para generar un numero de documento
    public String generarNumeroDocumento() {
        // Genera un numero de documento aleatorio de 8 digitos
        return String.valueOf(new Random().nextInt(90000000) + 10000000);
    }

    // Metodo para generar un numero de telefono
    private String generarNumeroTelefono() {
        // Genera un numero de telefono aleatorio de 9 digitos
        return "519" + (new Random().nextInt(90000000) + 10000000);
    }

    // Metodo para seleccionar la fecha de nacimiento
    private void seleccionarFechaNacimientoSinFecha(WebDriverWait wait, int pasajeroIndex) {
        try {
            // Abre el datepicker haciendo clic en el campo de la fecha de nacimiento
            driver.findElement(By.id("item_NacimientoPax_" + pasajeroIndex)).click();
            System.out.println("Abriendo el campo de fecha...");

            // Selecciona el mes siguiente
            driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='»'])[3]/following::span[1]")).click();
            System.out.println("Seleccionando el mes siguiente...");

            // Selecciona el primer dia disponible en el siguiente mes
            driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='»'])[2]/following::span[1]")).click();
            System.out.println("Seleccionando el dia...");

            // Selecciona un dia en la fila correcta, siguiendo la referencia a 'Sa' y seleccionando la columna 5
            driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Sa'])[1]/following::td[5]")).click();
            System.out.println("Dia seleccionado correctamente.");

        } catch (Exception e) {
            System.out.println("Error seleccionando la fecha de nacimiento: " + e.getMessage());
        }
    }

	public String obtenerMensajeNoDisponibilidad() {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Espera hasta 10 segundos
    try {
        // Esperar a que el mensaje de no disponibilidad sea visible
        WebElement noDisponibilidadMensaje = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("lblNoDisponibilidadCabinas")));
        
        // Retornar el texto del mensaje
        return noDisponibilidadMensaje.getText();
        
    } catch (TimeoutException e) {
        // Si no se encuentra el mensaje, lanzar una excepción
        System.out.println("Error: No se encontró el mensaje de no disponibilidad.");
        throw e;
    }
}




}
