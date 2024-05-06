package com.asma.asma.service;


import com.asma.asma.entities.typeseance;

import java.util.List;

public interface ITypeSeance <T>{

    void addtypeseance(typeseance typeseance);

    void supprimer(typeseance typeseance);

    List<typeseance> afficher();

    void modifier(typeseance typeseance);
}
