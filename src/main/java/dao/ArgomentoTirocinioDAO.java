package dao;

import model.ArgomentoTirocinio;
import java.util.List;

public interface ArgomentoTirocinioDAO {
    void salvaArgomento(ArgomentoTirocinio argomento, int docenteId);
    ArgomentoTirocinio getArgomentoById(int id);
    List<ArgomentoTirocinio> getArgomentiByDocente(int docenteId);
    List<ArgomentoTirocinio> getAllArgomenti();
}