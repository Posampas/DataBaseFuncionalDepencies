package pl.posmapas.key_dependency.model;

import java.util.ArrayList;
import java.util.List;

public class TestMain {

    public static void main(String[] args) {
        Schema schema = new Schema(
                new Attribute[]{new Attribute("Student"), new Attribute("University"),
                        new Attribute("Address"), new Attribute("Club")});

        schema.addDependency(
                new FunctionalDependency(
                        new Attribute[]{ schema.getAttribute("Student")},
                        new Attribute[]{schema.getAttribute("University")}));

        schema.addDependency(
                new FunctionalDependency(
                        new Attribute[]{schema.getAttribute("Club")},
                        new Attribute[]{schema.getAttribute("Address")}));

        schema.addDependency(
                new FunctionalDependency(
                        new Attribute[]{schema.getAttribute("University")},
                        new Attribute[]{schema.getAttribute("Address")}));



/*        System.out.println(schema.allInfo());

        SchemaDecompose schemaDecompose = new SchemaDecompose(schema);
        schemaDecompose.decompose();*/
        List<Schema> list = new ArrayList<>();
        SchemaDecompose.decomposeStatic(schema, list, null);

        for (Schema s: list){
            System.out.println(s.allInfo());
            System.out.println("-----------------------------");
        }



    }


}
