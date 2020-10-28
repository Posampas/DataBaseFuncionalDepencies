package pl.posmapas.key_dependency.model;

import java.util.Arrays;

public class FunctionalDependency {
    Attribute[] leftSide;
    Attribute[] rightSide;


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
