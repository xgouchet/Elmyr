package fr.xgouchet.elmyr.inject.fixture;

import fr.xgouchet.elmyr.annotation.DoubleForgery;
import fr.xgouchet.elmyr.annotation.FloatForgery;
import fr.xgouchet.elmyr.annotation.IntForgery;
import fr.xgouchet.elmyr.annotation.LongForgery;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class JavaInjectedPrimitives {

    @IntForgery(min = 1)
    public int publicInt;

    @IntForgery
    public List<Integer> publicIntList;


    @LongForgery(min = 1)
    public long publicLong;

    @LongForgery
    public Set<Long> publicLongSet;


    @FloatForgery(min = 1)
    public float publicFloat;

    @FloatForgery
    public Collection<Float> publicFloatCollection;


    @DoubleForgery(min = 1)
    public double publicDouble;

    @DoubleForgery
    public List<Set<Double>> publicDoubleSetList;

}
