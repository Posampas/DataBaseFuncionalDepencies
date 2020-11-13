package pl.posmapas.key_dependency.model;

import java.util.*;

public class SchemaDecompose {


    public static void decomposeStatic(Schema mainSchema, List<Schema> schemas,Schema originSchema) {
        mainSchema.setOrginSchema(originSchema);
        schemas.add(mainSchema);
        Queue<FunctionalDependency> functionalDependencyQueue = queueOfNonKeyDependency(mainSchema);
        if (!functionalDependencyQueue.isEmpty()) {
            Schema[] derivedSchemas = decomposeSchema(mainSchema, functionalDependencyQueue.poll());
            decomposeStatic(derivedSchemas[0], schemas, mainSchema);
            decomposeStatic(derivedSchemas[1], schemas, mainSchema);
        }


    }

    private static Queue<FunctionalDependency> queueOfNonKeyDependency(Schema schema) {
        Queue<FunctionalDependency> dependenciesToWorkOn = new LinkedList<>();
        for (FunctionalDependency dep : schema.getDependencies()) {
            DependencyType type = FunctionalDependency.dependencyType(dep, schema);
            if ((type != DependencyType.KEY) && (type != DependencyType.TRIVIAL)) {
                dependenciesToWorkOn.add(dep);
            }
        }
        return dependenciesToWorkOn;
    }

    private static Schema[] decomposeSchema(Schema mainSchema, FunctionalDependency decomposeDependecy) {


        Schema first = new Schema(decomposeDependecy.getAllAttributes());
        Schema second = new Schema(populateSecondSchema(mainSchema, decomposeDependecy));
        rewriteFunctionalDependencies(mainSchema, first);
        rewriteFunctionalDependencies(mainSchema, second);


        return new Schema[]{first, second};

    }

    private static void rewriteFunctionalDependencies(Schema mainSchema, Schema derivedSchema) {
        FunctionalDependency[] mainDependencies = mainSchema.getDependencies();
        for (FunctionalDependency dep : mainDependencies) {
            if (Schema.consist(derivedSchema.getAttributes(), dep.getLeftSide())
                    && Schema.consist(derivedSchema.getAttributes(), dep.getRightSide())) {
                FunctionalDependency nowe = new FunctionalDependency(dep.getLeftSide(),dep.getRightSide());
                derivedSchema.addDependency(nowe);
            }
        }

    }

    private static Attribute[] populateSecondSchema(Schema mainSchema, FunctionalDependency decomposeDependecy) {
        Attribute[] secondAttributes =
                new Attribute[mainSchema.getAttributes().length - decomposeDependecy.getRightSide().length];
        int conuter = 0;

        for (Attribute a : mainSchema.getAttributes()) {
            if (!consist(a, decomposeDependecy.getRightSide())) {
                secondAttributes[conuter++] = a;
            }
        }
        return secondAttributes;
    }

    public static boolean consist(Attribute atr, Attribute[] attributes) {
        for (Attribute a : attributes) {
            if (atr.equals(a)) {
                return true;
            }
        }
        return false;
    }

}
