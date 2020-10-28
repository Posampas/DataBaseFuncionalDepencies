package pl.posmapas.key_dependency.model;

import java.util.Arrays;

public class TestMain {

    public static void main(String[] args) {
        Schema schema = new Schema(
                new Attribute[]{new Attribute("A"), new Attribute("B"),
                        new Attribute("C"), new Attribute("D")});
        /*new Attribute("E"), new Attribute("F")*//*});

         *//*        schema.addDependency(
                new FunctionalDependency(
                        new Attribute[]{schema.getAttribiute("A"),schema.getAttribiute("B") },
                        new Attribute[]{schema.getAttribiute("D")}));
        schema.addDependency(
                new FunctionalDependency(
                        new Attribute[]{schema.getAttribiute("B"),schema.getAttribiute("D") },
                        new Attribute[]{schema.getAttribiute("B")}));
        schema.addDependency(
                new FunctionalDependency(
                        new Attribute[]{schema.getAttribiute("A"),schema.getAttribiute("C") },
                        new Attribute[]{schema.getAttribiute("D")}));
        schema.addDependency(
                new FunctionalDependency(
                        new Attribute[]{schema.getAttribiute("D") },
                        new Attribute[]{schema.getAttribiute("B")}));

        schema.addDependency(
                new FunctionalDependency(
                        new Attribute[]{schema.getAttribiute("B"),schema.getAttribiute("A") },
                        new Attribute[]{schema.getAttribiute("C")}));*/

        schema.addDependency(
                new FunctionalDependency(
                        new Attribute[]{schema.getAttribute("B")},
                        new Attribute[]{schema.getAttribute("D")}));


        schema.addDependency(
                new FunctionalDependency(
                        new Attribute[]{schema.getAttribute("A")},
                        new Attribute[]{schema.getAttribute("C")}));


        schema.addDependency(
                new FunctionalDependency(
                        new Attribute[]{schema.getAttribute("C"), schema.getAttribute("B")},
                        new Attribute[]{schema.getAttribute("A")}));
        schema.addDependency(
                new FunctionalDependency(
                        new Attribute[]{schema.getAttribute("D"), schema.getAttribute("C")},
                        new Attribute[]{schema.getAttribute("B")}));

        System.out.println(schema.toString());

        System.out.println("Super keys");

        for (Attribute[] key : schema.findSuperKeys()){
            System.out.println(Arrays.toString(key));
        }

        System.out.println("Keys");
           for (Attribute[] key : schema.findKeys()){
               System.out.println(Arrays.toString(key));
           }


    }

}
