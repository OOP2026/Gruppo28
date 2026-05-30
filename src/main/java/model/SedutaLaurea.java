package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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

    public String getLuogo() {
        return luogo;
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return data.format(fmt) + " - Ore " + ora + " presso " + luogo;
    }
}