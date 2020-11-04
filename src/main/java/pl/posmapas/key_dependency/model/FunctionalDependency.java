package pl.posmapas.key_dependency.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FunctionalDependency {
    private Attribute[] leftSide;
    private Attribute[] rightSide;

    private DependencyType dependencyType;

    public FunctionalDependency(Attribute[] leftSide, Attribute[] rightSide) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }

    public Attribute[] getLeftSide() {
        return leftSide;
    }

    public void setLeftSide(Attribute[] leftSide) {
        this.leftSide = leftSide;
    }

    public Attribute[] getRightSide() {
        return rightSide;
    }

    public void setRightSide(Attribute[] rightSide) {
        this.rightSide = rightSide;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FunctionalDependency)) return false;

        FunctionalDependency that = (FunctionalDependency) o;
        return checkIfSameAttributes(rightSide,that.rightSide) && checkIfSameAttributes(leftSide,that.leftSide);


    }

    private boolean checkIfSameAttributes(Attribute[] arr , Attribute[] brr){
        for (Attribute atr: arr) {
            boolean constist = false;
            for (Attribute a : brr){
                if (atr.equals(a)){
                    constist = true;
                    break;
                }
            }
            if (!constist){
                return false;
            }
        }
        return true;
    }

    public static DependencyType dependencyType(FunctionalDependency dependency, Schema schema) {
        ArrayList<Attribute[]> keys = schema.findKeys();
        if ( isDependencyKeyDependency(dependency, keys)){
            return DependencyType.KEY;
        }

        if ( isDependencyTrivial(dependency)){
            return DependencyType.TRIVIAL;
        }

        if (isDependencyKeyAttributeOnTheRight(dependency,keys)){
            return DependencyType.KEY_ATTRIBUTE_ON_THE_RIGHT;
        }

        if(isDependencyPartialDependency(dependency,keys)){
            return DependencyType.PARTIAL;
        }

        return DependencyType.TRANSITIVE;


    }

    public static boolean isDependencyPartialDependency(FunctionalDependency dependency , List<Attribute[]> keys){
        boolean isInSomeKey;

        for (Attribute a :dependency.getLeftSide()) {
            isInSomeKey = false;
            for (Attribute[] key : keys){
                for (Attribute b : key){
                    if (a.equals(b)){
                        isInSomeKey = true;
                        break;
                    }
                }
                if(isInSomeKey){
                    break;
                }
            }
            if (!isInSomeKey){
                return false;
            }
        }
        return true;
    }

    public static boolean isDependencyKeyAttributeOnTheRight(FunctionalDependency dependency, List<Attribute[]> keys){
        for (Attribute a : dependency.getRightSide()){
            for (Attribute[] key : keys ) {
                for (Attribute b : key){
                    if (a.equals(b)){
                        return true;
                    }
                }
            }
        }
        return false;

    }

    public static boolean isDependencyKeyDependency(FunctionalDependency dependency, List<Attribute[]> keys){

        for (Attribute[] key: keys) {

            if (areDepencetisEquals(key,dependency)){
                return true;
            }

        }
        return false;
    }

    private static boolean areDepencetisEquals(Attribute[] key, FunctionalDependency dependency){

        for (Attribute attribute : key) {
            boolean isTheSame = false;
            for (int j = 0; j < dependency.leftSide.length; j++) {
                if (attribute.equals(dependency.leftSide[j])) {
                    isTheSame = true;
                    break;
                }
            }
            if (!isTheSame){
                return false;
            }
        }
        return true;
    }

    public static boolean isDependencyTrivial(FunctionalDependency dependency) {
        for (Attribute a : dependency.getRightSide()) {
            for (Attribute b : dependency.getLeftSide()) {
                if (a.equals(b)) {
                    return true;
                }
            }
        }
        return false;
    }




    @Override
    public int hashCode() {
        int result = Arrays.hashCode(leftSide);
        result = 31 * result + Arrays.hashCode(rightSide);
        return result;
    }

    @Override
    public String toString() {
        return Arrays.toString(leftSide) +
                "--->" + Arrays.toString(rightSide);
    }
}
