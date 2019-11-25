package fr.xgouchet.elmyr.inject.dummy;

import fr.xgouchet.elmyr.annotation.Forgery;
import org.mockito.Mock;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JavaInjectedGenerics {

    @Forgery
    public List<Foo> privateFooList;
    @Forgery
    public Set<Foo> privateFooSet;
    @Forgery
    public Map<Foo, Bar> privateFooMap;
    @Forgery
    public Collection<Foo> privateFooCollection;

}
