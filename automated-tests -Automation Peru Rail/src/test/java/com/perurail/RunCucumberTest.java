package com.perurail;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/main/resources/features",  // Ruta a los archivos .feature
    glue = {"com.perurail.steps"},             // Ruta a tus step definitions
    plugin = {
        "pretty", 
        "json:target/cucumber-reports/Cucumber.json",   // Reporte JSON
        "html:target/cucumber-reports/cucumber-html-report.html",  // Reporte HTML
        "junit:target/cucumber-reports/Cucumber.xml"    // Reporte JUnit XML
    },
    monochrome = true  // Para un reporte más legible en consola
)
public class RunCucumberTest {
}
