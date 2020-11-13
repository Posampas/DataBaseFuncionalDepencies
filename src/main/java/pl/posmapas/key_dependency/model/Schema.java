package pl.posmapas.key_dependency.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Schema {
    private Attribute[] schema;
    private FunctionalDependency[] dependencies = new FunctionalDependency[]{};
    Schema(Attribute[] schema) {
        this.schema = schema;
    }
    private Schema orginSchema = null;


    Attribute getAttribute(String name) {

        for (Attribute s : schema) {
            if (name.equals(s.getName())) {
                return s;
            }
        }
        return null;
    }

    public Attribute[] getAttributes() {
        return schema;
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

    public void setOrginSchema(Schema originSchema) {
        this.orginSchema = originSchema;
    }
    Schema getOrginSchema(){
        return orginSchema;
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

    public NormalForm getNormalForm(){

        if(isRelationInBoyceCoddNormalForm(dependencies,this)){
            return NormalForm.BOYCE_CODD;
        }
        if(isRelationInThirdNormalForm(dependencies,this)){
            return NormalForm.THIRD;
        }
        if(isRelationInSecondNormalForm(dependencies,this)){
            return NormalForm.SECOND;
        }
        return  NormalForm.FIRST;
    }

    public static boolean isRelationInBoyceCoddNormalForm(FunctionalDependency[] dependencies, Schema schema){
        for (FunctionalDependency dependency : dependencies){
            DependencyType dependencyType = FunctionalDependency.dependencyType(dependency, schema);
            if (dependencyType != DependencyType.TRIVIAL && dependencyType != DependencyType.KEY ){
                return false;
            }
        }
        return true;
    }

    public static boolean isRelationInThirdNormalForm(FunctionalDependency[] dependencies, Schema schema){

        for (FunctionalDependency dependency : dependencies){
            DependencyType dependencyType = FunctionalDependency.dependencyType(dependency, schema);
            if (dependencyType != DependencyType.TRIVIAL && dependencyType != DependencyType.KEY
                && dependencyType != DependencyType.KEY_ATTRIBUTE_ON_THE_RIGHT){
                return false;
            }
        }
        return true;
    }


    public static boolean isRelationInSecondNormalForm(FunctionalDependency[] dependencies, Schema schema){

        for (FunctionalDependency dependency : dependencies){
            DependencyType dependencyType = FunctionalDependency.dependencyType(dependency, schema);
            if (dependencyType == DependencyType.PARTIAL){
                return false;
            }
        }
        return true;
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

        return consist(result.toArray(Attribute[]::new), schema);
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

            if (!wasDependencyUsed[i] && consist(result.toArray(new Attribute[0]), dependencies[i].getLeftSide())) {
                addAttributeToTheResult(dependencies[i].getRightSide(), result);
                wasDependencyUsed[i] = true;
                wasSomethingChanged = true;
            }
        }

        return  wasSomethingChanged;
    }

    public  static boolean consist(Attribute[] main , Attribute[] candidate){


        for (Attribute attribute : candidate) {
            boolean isInArray = false;
            for (Attribute m : main) {
                if (m.equals(attribute)) {
                    isInArray = true;
                    break;
                }
            }
            if (!isInArray) {
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

    public String allInfo(){

        StringBuilder sb = new StringBuilder();
        sb.append("Schema  ").append(Arrays.toString(schema));
        if (orginSchema != null){
            sb.append(" <-------------- <Derived form schema : " + Arrays.toString(orginSchema.getAttributes()) + " \n");
        }
        sb.append("Relation is in ").append(getNormalForm()).append(" normal form \n");

        for (FunctionalDependency dept : dependencies) {
            sb.append(dept).append(" ").append(FunctionalDependency.dependencyType(dept,this)).append('\n');
        }

        sb.append("Keys").append('\n');
        int index = 1;
        for (Attribute[] a: findKeys()) {
            sb.append("K").append(index++).append(" = ").append(Arrays.toString(a)).append(", ");
        }

        return sb.toString();
    }
}
