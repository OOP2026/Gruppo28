package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class SedutaLaurea {
    private int id;
    private LocalDate data; // Usa il pacchetto moderno java.time
    private LocalTime ora;
    private String luogo; // Corretto da int a String

    public SedutaLaurea(int id, LocalDate data, LocalTime ora, String luogo) {
        this.id = id;
        this.data = data;
        this.ora = ora;
        this.luogo = luogo;
    }

    public int getId() { return id; }
    public String getLuogo() { return luogo; }
}