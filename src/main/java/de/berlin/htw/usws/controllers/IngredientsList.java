package de.berlin.htw.usws.controllers;

import lombok.Getter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="ingredientsList")
@Getter
public class IngredientsList {

    @XmlElement
    private List<String> ingredients;
}
