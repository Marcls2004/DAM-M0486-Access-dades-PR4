package com.project.pr14;

import com.project.objectes.Llibre;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.io.File;
import java.util.List;
import java.util.Iterator;

/**
 * Classe principal que gestiona la lectura i el processament de fitxers JSON per obtenir dades de llibres
 * utilitzant Jackson 3.
 */
public class PR14GestioLlibreriaJacksonMain {

    private final File dataFile;
    private final JsonMapper jsonMapper;

    /**
     * Constructor de la classe PR14GestioLlibreriaJacksonMain.
     *
     * @param dataFile Fitxer on es troben els llibres.
     */
    public PR14GestioLlibreriaJacksonMain(File dataFile) {
        this.dataFile = dataFile;
        this.jsonMapper = JsonMapper.builder().build();
    }

    public static void main(String[] args) {
        File dataFile = new File(System.getProperty("user.dir"), "data/pr14" + File.separator + "llibres_input.json");
        PR14GestioLlibreriaJacksonMain app = new PR14GestioLlibreriaJacksonMain(dataFile);
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
        try {
            // Mapeja de manera automàtica el fitxer JSON a una llista d'objectes Llibre en memòria
            return jsonMapper.readValue(dataFile, new TypeReference<List<Llibre>>() {});
        } catch (JacksonException e) {
            // Captura errors de format o fitxer inexistent i els mostra sense penjar l'aplicació
            System.err.println("Error al llegir el fitxer JSON: " + e.getMessage());
        }
        // Retorna null si la lectura ha fallat per qualsevol motiu
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
        // Recorre tota la col·lecció de llibres un per un
        for (Llibre llibre : llibres) {
            // Comprova si coincideix amb l'ID del llibre que busquem
            if (llibre.getId() == id) {
                // Actualitza l'any mitjançant el mètode setAny de l'objecte
                llibre.setAny(nouAny);
                // Atura la cerca immediatament per optimitzar el rendiment
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
        // Afegeix el nou objecte llibre rebut al final de la llista existent
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
        // Crea un iterador per recórrer i eliminar de la llista sense errors de modificació concurrent
        Iterator<Llibre> iterator = llibres.iterator();
        while (iterator.hasNext()) {
            Llibre llibre = iterator.next();
            // Si l'ID coincideix amb el llibre a eliminar
            if (llibre.getId() == id) {
                // Elimina l'element de la llista de manera completament segura
                iterator.remove();
                // Surt del bucle ja que l'element ha estat eliminat
                break;
            }
        }
    }

    /**
     * Guarda la llista de llibres en un fitxer nou anomenat llibres_output_jackson.json,
     * dins del mateix directori que el fitxer d'entrada.
     *
     * @param llibres Llista de llibres a guardar.
     */
    public void guardarLlibres(List<Llibre> llibres) {
        // *************** CODI PRÀCTICA **********************/
        // Defineix el fitxer de sortida a la mateixa carpeta pare que el fitxer d'entrada
        File outputFile = new File(dataFile.getParent(), "llibres_output_jackson.json");
        try {
            // Guarda la llista convertint-la a JSON i li aplica un format visual net (Pretty Print)
            jsonMapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, llibres);
        } catch (JacksonException e) {
            // Imprimeix un error si hi ha problemes d'escriptura o permisos al disc dur
            System.err.println("Error al escriure el fitxer JSON: " + e.getMessage() + "with" + outputFile.getName());
        }
    }
}
