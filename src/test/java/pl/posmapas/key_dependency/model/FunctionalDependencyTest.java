package pl.posmapas.key_dependency.model;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

class FunctionalDependencyTest {

    @Test
    void testEquals() {

        FunctionalDependency fun1 = new FunctionalDependency(
                new Attribute[]{new Attribute("A")},
                new Attribute[]{new Attribute("B"), new Attribute("C")});


        FunctionalDependency fun2 = new FunctionalDependency(
                new Attribute[]{new Attribute("A")},
                new Attribute[]{new Attribute("C") , new Attribute("B")});


        assertTrue(fun1.equals(fun2));



        FunctionalDependency fun3 = new FunctionalDependency(
                new Attribute[]{new Attribute("D")},
                new Attribute[]{new Attribute("B"), new Attribute("C")});


        FunctionalDependency fun4 = new FunctionalDependency(
                new Attribute[]{new Attribute("A")},
                new Attribute[]{new Attribute("C") , new Attribute("B")});


        assertFalse(fun3.equals(fun4));

    }
}