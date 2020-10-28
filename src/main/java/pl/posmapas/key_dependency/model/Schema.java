package pl.posmapas.key_dependency.model;

import java.util.ArrayList;
import java.util.Arrays;

public class Schema {
    private Attribute[] schema;
    private FunctionalDependency[] dependencies;

    Schema(Attribute[] schema) {
        this.schema = schema;
    }


    Attribute getAttribute(String name) {

        for (Attribute s : schema) {
            if (name.equals(s.getName())) {
                return s;
            }
        }
        return null;
    }


    public FunctionalDependency[] getDependencies() {
        return dependencies;
    }

    public void addDependency(FunctionalDependency dependency) {
        // Jeżeli puste to dodaj do reszty
        ifDependenciesAreNull(dependency);

        if (!checkIfSuchDependencyIsAlreadyAdded(dependency)) {
            addDependencyToArray(dependency);
        }

    }


    private void ifDependenciesAreNull(FunctionalDependency dependency) {
        if (dependencies == null) {
            dependencies = new FunctionalDependency[]{dependency};
        }
    }


    private boolean checkIfSuchDependencyIsAlreadyAdded(FunctionalDependency dependency) {
        for (FunctionalDependency dep : dependencies) {
            if (dependency.equals(dep)) {
                return true;
            }
        }
        return false;
    }

    private void addDependencyToArray(FunctionalDependency dependency) {
        int length = dependencies.length;
        FunctionalDependency[] tmp = new FunctionalDependency[length + 1];
        System.arraycopy(dependencies, 0, tmp, 0, length);
        tmp[length] = dependency;
        dependencies = tmp;
    }

    public ArrayList<Attribute[]> findSuperKeys() {

        ArrayList<Attribute[]> allPossibleKeys = new ArrayList<>();

        findAllPossibleKeys(allPossibleKeys);

        return ifPossibleKeyIsKeyAddItToList(allPossibleKeys);
    }

    public ArrayList<Attribute[]> findKeys() {
        ArrayList<Attribute[]> superKeys = findSuperKeys();
        ArrayList<Attribute[]> keys = new ArrayList<>();

        for (int i = superKeys.size() -1; i >= 0  ; i--) {
            Attribute[] first = superKeys.get(i);
            boolean unique = true;
            for (int j = 0; j < i  ; j++) {
                Attribute[] second = superKeys.get(j);
                if(consist(superKeys.get(i),superKeys.get(j))){
                    unique = false;
                    break;
                }
            }

            if (unique && !keys.contains(superKeys.get(i))){
                keys.add(superKeys.get(i));
            }
        }

        return keys;
    }


    private void findAllPossibleKeys(ArrayList<Attribute[]> allPossibleKeys) {
        for (int i = 1; i < schema.length + 1; i++) { // ilość atrybutów 1,2,3

            combinations(schema, i, 0, new Attribute[i], allPossibleKeys);
        }
    }

    private ArrayList<Attribute[]> ifPossibleKeyIsKeyAddItToList(ArrayList<Attribute[]> allPossibleKeys) {
        ArrayList<Attribute[]> keys = new ArrayList<>();
        for (Attribute[] possible : allPossibleKeys) {
            if (isKey(possible)) {
                keys.add(possible);
            }
        }
        return keys;
    }


    private boolean isKey(Attribute[] possible) {
        ArrayList<Attribute> result = new ArrayList<>();

        addAttributeToTheResult(possible, result);

        expandResultWithDependencies(result);

        return consist(result, schema);
    }


    private void addAttributeToTheResult(Attribute[] attributes, ArrayList<Attribute> result) {
        for (Attribute a : attributes) {
            if (!result.contains(a)) {
                result.add(a);
            }
        }

    }

    private void expandResultWithDependencies(ArrayList<Attribute> result) {

        boolean[] wasDependencyUsed = new boolean[dependencies.length];
        boolean search = true;

        while (search) {

            if(!loopThroughDependenciesAddToResultAndConfirmIfAnythingHasChanged(result,wasDependencyUsed)){
                search = false;
            }
        }

    }
    private boolean loopThroughDependenciesAddToResultAndConfirmIfAnythingHasChanged
            (ArrayList<Attribute> result,boolean[] wasDependencyUsed  ){
        boolean wasSomethingChanged = false;
        for (int i = 0; i < dependencies.length; i++) {

            if (!wasDependencyUsed[i] && consist(result, dependencies[i].getLeftSide())) {
                addAttributeToTheResult(dependencies[i].getRightSide(), result);
                wasDependencyUsed[i] = true;
                wasSomethingChanged = true;
            }
        }

        return  wasSomethingChanged;
    }

    private static boolean consist(Attribute[] main , Attribute[] candidate){


        boolean[] trueTable = new boolean[candidate.length];

        for (int i = 0; i < candidate.length; i++) {
            for (Attribute m : main) {
                if (m.equals(candidate[i])) {
                    trueTable[i] = true;
                    break;
                }
            }
            if (!trueTable[i]) {
                return false;
            }
        }
        return true;
    }
    private boolean consist(ArrayList<Attribute> main, Attribute[] candidate) {

        boolean[] trueTable = new boolean[candidate.length];

        for (int i = 0; i < candidate.length; i++) {
            for (Attribute m : main) {
                if (m.equals(candidate[i])) {
                    trueTable[i] = true;
                    break;
                }
            }
            if (!trueTable[i]) {
                return false;
            }
        }
        return true;


    }

    static void combinations(Attribute[] schema, int len, int startPosition, Attribute[] result, ArrayList<Attribute[]> possibilites) {
        if (len == 0) {
            Attribute[] tmp = new Attribute[result.length];
            int ind = 0;
            for (Attribute attribute : result) {
                tmp[ind++] = attribute;
            }
            possibilites.add(tmp);

            return;
        }
        for (int i = startPosition; i <= schema.length - len; i++) {
            result[result.length - len] = schema[i];
            combinations(schema, len - 1, i + 1, result, possibilites);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Schema  ").append(Arrays.toString(schema)).append('\n');

        for (FunctionalDependency dept : dependencies) {
            sb.append(dept).append('\n');
        }

        return sb.toString();
    }
}
