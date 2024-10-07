Feature: Reserva de viajes en PeruRail

Scenario: Búsqueda y reserva de tren en la ruta Puno > Ciudad de Cusco con el tren Andean Explorer, A Belmond Train
    Given El usuario está en la página de reserva de viajes de PeruRail
    When El usuario selecciona el origen "Puno" y el destino "Ciudad de Cusco"
    And Elige el tren "Andean Explorer, A Belmond Train"
    And Selecciona la fecha "11/6/2024"
    And El usuario hace clic en buscar
    Then debería ser redirigido a la página de reservas
    And elijo "SUITE CABIN" y 1 cabina con 2 adultos y 0 niño
    And confirmo la selección de cabinas
    Then debo llenar la información de 2 adultos y 0 niños
    And hago clic en el botón CONTINUE , aqui se verificará los datos


Scenario: Búsqueda y reserva de tren en la ruta Arequipa > Ciudad de Cusco con el tren Andean Explorer, A Belmond Train
    Given El usuario está en la página de reserva de viajes de PeruRail
    When El usuario selecciona el origen "Arequipa" y el destino "Ciudad de Cusco"
    And Selecciona la fecha "11/2/2024"
    And El usuario hace clic en buscar
    Then debería ser redirigido a la página de reservas
    And elijo "SUITE CABIN" y 1 cabina con 2 adultos y 0 niño
    And confirmo la selección de cabinas
    Then debo llenar la información de 2 adultos y 0 niños
    And hago clic en el botón CONTINUE , aqui se verificará los datos
	

Scenario: Busqueda y reserva de tren sin disponibilidad
    Given El usuario está en la página de reserva de viajes de PeruRail
    When El usuario selecciona el origen "Puno" y el destino "Ciudad de Cusco"
    And Elige el tren "Andean Explorer, A Belmond Train"
    And El usuario hace clic en buscar
    Then debería ser redirigido a la página de reservas y verificar que no hay disponibilidad

