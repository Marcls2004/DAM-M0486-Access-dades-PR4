package com.project.pr14;

import com.project.objectes.Llibre;
import jakarta.json.*;
import jakarta.json.stream.JsonGenerator;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Iterator;


/**
 * Classe principal que gestiona la lectura i el processament de fitxers JSON per obtenir dades de llibres
 * utilitzant Jakarta JSON Processing (JSON-P).
 */
public class PR14GestioLlibreriaJakartaMain {

    private final File dataFile;

    /**
     * Constructor de la classe PR14GestioLlibreriaJakartaMain.
     *
     * @param dataFile Fitxer on es troben els llibres.
     */
    public PR14GestioLlibreriaJakartaMain(File dataFile) {
        this.dataFile = dataFile;
    }

    public static void main(String[] args) {
        File dataFile = new File(System.getProperty("user.dir"), "data/pr14" + File.separator + "llibres_input.json");
        PR14GestioLlibreriaJakartaMain app = new PR14GestioLlibreriaJakartaMain(dataFile);
        app.processarFitxer();
    }

    /**
     * Processa el fitxer JSON per carregar, modificar, afegir, esborrar i guardar les dades dels llibres.
     */
    public void processarFitxer() {
        List<Llibre> llibres = carregarLlibres();
        if (llibres != null) {
            modificarAnyPublicacio(llibres, 1, 1995);
            afegirNouLlibre(llibres, new Llibre(4, "Històries de la ciutat", "Miquel Soler", 2022));
            esborrarLlibre(llibres, 2);
            guardarLlibres(llibres);
        }
    }

    /**
     * Carrega els llibres des del fitxer JSON.
     * Si el fitxer no existeix o no conté un JSON vàlid, mostra un missatge d'error i retorna null
     * (no ha de propagar cap excepció).
     *
     * @return Llista de llibres o null si hi ha hagut un error en la lectura.
     */
    public List<Llibre> carregarLlibres() {
        // *************** CODI PRÀCTICA **********************/
        try (JsonReader jsonReader = Json.createReader(
            new FileReader(dataFile, StandardCharsets.UTF_8))) {
            // Inicialitza la col·lecció en memòria on s'acumularan els llibres llegits
            ArrayList<Llibre> llibres = new ArrayList<>();
            // Llegeix l'estructura del JSON sencer directamente com un array arrel
            JsonArray jsonArray = jsonReader.readArray();
            
            // Recorre cada valor individual de l'array de JSON actuant com a objecte
            for (JsonValue jsonValue : jsonArray) {
                JsonObject jsonObject = jsonValue.asJsonObject();
                // Extreu manualment cada camp basant-se en la clau exacta del JSON
                int id = jsonObject.getInt("id");
                String titol = jsonObject.getString("titol");
                String autor = jsonObject.getString("autor");
                int anyPublicacio = jsonObject.getInt("any");
                // Instancia l'objecte del model de dades i el desa a la llista
                llibres.add(new Llibre(id, titol, autor, anyPublicacio));
            }
            // Retorna la llista completada en memòria
            return llibres; 

        } catch (IOException e) {
            // Captura errors físics o d'accés (per exemple, si el fitxer no existeix)
            System.err.println("Error al llegir el fitxer: " + e.getMessage());
        } catch (JsonException e) {
            // Captura errors sintàctics de format intern del JSON
            System.err.println("Error al llegir el fitxer JSON: " + e.getMessage());
        }
        // Retorna un valor nul controlat si ha saltat qualsevol de les excepcions anteriors
        return null;
    }

    /**
     * Modifica l'any de publicació d'un llibre amb un id específic.
     *
     * @param llibres Llista de llibres.
     * @param id Identificador del llibre a modificar.
     * @param nouAny Nou any de publicació.
     */
    public void modificarAnyPublicacio(List<Llibre> llibres, int id, int nouAny) {
        // *************** CODI PRÀCTICA **********************/
        // Recorre els elements de la llista buscant coincidents
        for (Llibre llibre : llibres) {
            // En cas que l'ID de l'element llibre actual sigui igual al passat com a filtre
            if (llibre.getId() == id) {
                // Modifica l'estat intern de l'objecte assignant-li el nou any
                llibre.setAny(nouAny);
                // Atura el bucle immediatament per evitar processaments de més al sistema
                break;
            }
        }
    }

    /**
     * Afegeix un nou llibre a la llista de llibres.
     *
     * @param llibres Llista de llibres.
     * @param nouLlibre Nou llibre a afegir.
     */
    public void afegirNouLlibre(List<Llibre> llibres, Llibre nouLlibre) {
        // *************** CODI PRÀCTICA **********************/
        // Afegeix de manera directa l'objecte Llibre a la col·lecció de dades
        llibres.add(nouLlibre);
    }

    /**
     * Esborra un llibre amb un id específic de la llista de llibres.
     *
     * @param llibres Llista de llibres.
     * @param id Identificador del llibre a esborrar.
     */
    public void esborrarLlibre(List<Llibre> llibres, int id) {
        // *************** CODI PRÀCTICA **********************/
        // Crea un objecte Iterador de llibres per poder fer un borrat segur sense errors de concurrència
        Iterator<Llibre> iterator = llibres.iterator();
        while (iterator.hasNext()) {
            Llibre llibre = iterator.next();
            // Comprova si l'ID d'aquest element de la volta és el triat per ser eliminat
            if (llibre.getId() == id) {
                // Elimina el registre directament a través de l'operació de l'iterador
                iterator.remove();
                // Abandona l'execució del mètode en haver enllestit la tasca
                break;
            }
        }
    }

    /**
     * Guarda la llista de llibres en un fitxer nou anomenat llibres_output_jakarta.json,
     * dins del mateix directori que el fitxer d'entrada.
     *
     * @param llibres Llista de llibres a guardar.
     */
    public void guardarLlibres(List<Llibre> llibres) {
        // *************** CODI PRÀCTICA **********************/
        // Obre els escriptors canalitzats garantint el tancament automàtic gràcies al bloc try-with-resources
        try (Writer writer = Files.newBufferedWriter(Path.of(dataFile.getParent(), "llibres_output_jakarta.json"), StandardCharsets.UTF_8);
             JsonWriter jsonWriter = Json.createWriter(writer)) {
            // Inicialitza un constructor per anar muntant la matriu d'elements JSON a guardar
            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            for (Llibre llibre : llibres) {
                // Construeix manualment l'estructura de parelles clau-valor de cada llibre
                JsonObject jsonObject = Json.createObjectBuilder()
                        .add("id", llibre.getId())
                        .add("titol", llibre.getTitol())
                        .add("autor", llibre.getAutor())
                        .add("any", llibre.getAny())
                        .build();
                // Acumula el nou objecte creat al constructor general de la matriu
                jsonArrayBuilder.add(jsonObject);
            }
            // Envia l'array arrel construït final cap a l'escriptor per segellar-lo al disc
            jsonWriter.writeArray(jsonArrayBuilder.build());
        } catch (IOException e) {
            // Enregistra errors del sistema operatiu si la persistència del document no és viable
            System.err.println("Error al escriure el fitxer: " + e.getMessage());
        }
    }
}
