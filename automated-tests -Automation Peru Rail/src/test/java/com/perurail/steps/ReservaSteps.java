package com.perurail.steps;

import com.perurail.pages.ReservaPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReservaSteps {
    WebDriver driver;
    ReservaPage reservaPage;
    List<String> nombresPasajeros;
    List<String> documentosPasajeros;

    // Default constructor for Cucumber
    public ReservaSteps() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/drivers/chromedriver.exe");
        driver = new ChromeDriver();
        reservaPage = new ReservaPage(driver);
        nombresPasajeros = new ArrayList<>();
        documentosPasajeros = new ArrayList<>();
    }

    @Given("El usuario está en la página de reserva de viajes de PeruRail")
    public void abrirPagina() {
        driver.get("https://www.perurail.com/");
    }

    @When("^El usuario selecciona el origen \"([^\"]*)\" y el destino \"([^\"]*)\"$")
    public void seleccionarDestinoRuta(String origen, String destino) {
        reservaPage.seleccionarDestino(origen, destino);
    }

    @When("^Elige el tren \"([^\"]*)\"$")
    public void elegirTren(String tren) {
        reservaPage.elegirTren(tren);
    }

    @And("^Selecciona la fecha \"([^\"]*)\"$")
    public void selecciona_la_fecha(String fecha) {
        reservaPage.elegirFecha(fecha);
    }

    @And("El usuario hace clic en buscar")
    public void hacerClicBuscar() {
        reservaPage.hacerClicBuscar();
    }

    @Then("El sistema muestra las cabinas disponibles")
    public void mostrarCabinas() {
        assertTrue(reservaPage.cabinasDisponibles());
    }

    @And("confirmo la selección de cabinas")
    public void confirmoSeleccionCabinas() {
        System.out.println("Confirmando la selección de cabinas.");
        reservaPage.confirmarSeleccionCabinas();
    }

    @Then("^El sistema muestra el mensaje \"No hay cabinas disponibles\"$")
    public void noHayCabinasDisponibles() {
        assertTrue(reservaPage.mensajeNoCabinasDisponible());
    }

    @Then("El sistema muestra el resumen de compra con la cabina seleccionada y el precio correspondiente")
    public void mostrarResumenCompra() {
        assertTrue(reservaPage.resumenCompraVisible());
    }

    @Then("El sistema muestra un mensaje indicando que solo se permite un máximo de 9 personas por reserva")
    public void validarLimitePersonas() {
        assertTrue(reservaPage.validarLimitePersonas());
    }

    @When("El usuario ingresa los datos personales y de contacto")
    public void ingresarDatosPersonales() {
        reservaPage.ingresarDatosPersonales("Juan Perez", "juan.perez@gmail.com");
    }

    @When("Ingresa los datos de pago")
    public void ingresarDatosPago() {
        reservaPage.ingresarDatosPago("4111111111111111", "12/25", "123");
    }

    @Then("El sistema procesa el pago y muestra la confirmación de la compra con los boletos")
    public void mostrarConfirmacionCompra() {
        assertTrue(reservaPage.confirmacionCompraVisible());
    }

    @Then("El sistema actualiza el resumen con la cantidad de pasajeros y el costo total")
    public void actualizarResumen() {
        assertTrue(reservaPage.resumenActualizadoVisible());
    }

    @Then("debería ser redirigido a la página de reservas")
    public void deberia_ser_redirigido_a_la_pagina_de_reservas() {
        reservaPage.deberia_ser_redirigido_a_la_pagina_de_reservas();
    }
    
    @Then("debería ser redirigido a la página de reservas y verificar que no hay disponibilidad")
    public void deberia_ser_redirigido_a_la_pagina_de_reservas_verificar_disponibilidad() {
        // Obtener el mensaje de no disponibilidad desde ReservaPage
        String mensajeNoDisponibilidad = reservaPage.obtenerMensajeNoDisponibilidad();
        
        // Verificar que el mensaje es el esperado
        assertEquals("We did not find availability for your combination of destinations, dates or discounts. Please try again by modifying your search.", mensajeNoDisponibilidad);
        
        // Confirmar la verificación
        System.out.println("Verificación del mensaje de no disponibilidad exitosa.");
    }


    @Then("elijo {string} y {int} cabina con {int} adulto(s) y {int} niño(s)")
    public void elijo_y_cabina_con_adultos_y_niños(String tipoCabina, Integer numeroCabinas, Integer adultos, Integer niños) {
        // Llamar al método que elige la cabina y los pasajeros
        reservaPage.elegirCabinaYPasajeros(tipoCabina, numeroCabinas, adultos, niños);
        
        // Llenar la información de los pasajeros después de seleccionar cabina y pasajeros
        llenarInformacionPasajeros(adultos, niños);
    }

    @Then("debo llenar la información de {int} adulto(s) y {int} niño(s)")
    public void deboLlenarInformacionDePasajeros(int adultos, int niños) {
        // Llama al método que llena la información de los pasajeros
        reservaPage.llenarInformacionPasajeros(adultos, niños);
        
        // Aquí puedes agregar más lógica si es necesario, como aserciones para verificar que la información se haya llenado correctamente
    }


    public void llenarInformacionPasajeros(int adultos, int niños) {
        // Lógica para llenar los datos de los pasajeros y añadirlos a las listas
        for (int i = 0; i < adultos; i++) {
            String nombreAleatorioAdulto = reservaPage.getNombresAdultos()[new Random().nextInt(reservaPage.getNombresAdultos().length)];
            String apellidoAleatorioAdulto = reservaPage.getApellidosAdultos()[new Random().nextInt(reservaPage.getApellidosAdultos().length)];
            String documento = reservaPage.generarNumeroDocumento(); // Método para generar documento
            // Agregar a las listas
            nombresPasajeros.add(nombreAleatorioAdulto + " " + apellidoAleatorioAdulto);
            documentosPasajeros.add(documento);
        }

        for (int j = 0; j < niños; j++) {
            String nombreAleatorioNiño = reservaPage.getNombresNiños()[new Random().nextInt(reservaPage.getNombresNiños().length)];
            String apellidoAleatorioNiño = reservaPage.getApellidosNiños()[new Random().nextInt(reservaPage.getApellidosNiños().length)];
            String documento = reservaPage.generarNumeroDocumento(); // Método para generar documento
            // Agregar a las listas
            nombresPasajeros.add(nombreAleatorioNiño + " " + apellidoAleatorioNiño);
            documentosPasajeros.add(documento);
        }
    }

    @Then("hago clic en el botón CONTINUE , aqui se verificará los datos")
    public void hagoClicEnElBotonContinue() {
        // Llamar al método que realiza clic en el botón "Continue"
        reservaPage.clickContinueButton();

        // Comparar los datos de los pasajeros con los datos en la tabla
        reservaPage.compararDatosConTabla(nombresPasajeros, documentosPasajeros);
    }
}
