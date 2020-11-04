package pl.posmapas.key_dependency.model;

import java.util.Arrays;

public class TestMain {

    public static void main(String[] args) {
        Schema schema = new Schema(
                new Attribute[]{new Attribute("f"), new Attribute("g"),
                        new Attribute("h"), new Attribute("i")});

        schema.addDependency(
                new FunctionalDependency(
                        new Attribute[]{schema.getAttribute("f"), schema.getAttribute("h"), schema.getAttribute("i")},
                        new Attribute[]{schema.getAttribute("g")}));

/*        schema.addDependency(
                new FunctionalDependency(
                        new Attribute[]{schema.getAttribute("g")},
                        new Attribute[]{schema.getAttribute("h")}));*/

        schema.addDependency(
                new FunctionalDependency(
                        new Attribute[]{schema.getAttribute("g"),schema.getAttribute("i")},
                        new Attribute[]{schema.getAttribute("f")}));


        System.out.println(schema);
        System.out.println("--------------------------");
        System.out.println(schema.allInfo());


    }

}
