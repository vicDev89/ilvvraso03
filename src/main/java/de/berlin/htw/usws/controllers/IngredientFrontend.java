package de.berlin.htw.usws.controllers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class IngredientFrontend {

    private String name;

    private List<String> measures;
}
