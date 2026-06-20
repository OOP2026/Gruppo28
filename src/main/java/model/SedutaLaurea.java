package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Rappresenta una seduta di laurea programmata nel sistema.
 * Contiene le informazioni relative alla data, all'orario e al luogo fisico
 * (es. un'aula specifica) in cui si svolgerà la discussione delle tesi.
 */
public class SedutaLaurea {
    private int id;
    private LocalDate data;
    private LocalTime ora;
    private String luogo;

    public SedutaLaurea(int id, LocalDate data, LocalTime ora, String luogo) {
        this.id = id;
        this.data = data;
        this.ora = ora;
        this.luogo = luogo;
    }

    public int getId() {
        return id;
    }

    public LocalDate getData() {
        return data;
    }

    public LocalTime getOra() {
        return ora;
    }

    public String getLuogo() {
        return luogo;
    }

    /**
     * Fornisce una rappresentazione testuale formattata della seduta,
     * convertendo la data in un formato più leggibile (giorno-mese-anno).
     * Ideale per l'inserimento in menu a tendina (ComboBox) o liste nella GUI.
     *
     * @return Una stringa riassuntiva con data, ora e luogo della seduta
     */
    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return data.format(fmt) + " - Ore " + ora + " presso " + luogo;
    }
}