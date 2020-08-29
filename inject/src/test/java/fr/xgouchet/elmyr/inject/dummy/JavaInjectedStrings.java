package fr.xgouchet.elmyr.inject.dummy;

import fr.xgouchet.elmyr.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class JavaInjectedStrings {

    @StringForgery(StringForgeryType.HEXADECIMAL)
    public String publicHexaString;

    @StringForgery(StringForgeryType.NUMERICAL)
    public Set<String> publicNumericalStringSet;

    @RegexForgery("[abc]+")
    public String publicRegex;

    // 110e8400-e29b-11d4-a716-446655440000
    @RegexForgery("\\d{8}-\\d{4}-\\d{4}-\\d{4}-\\d{12}")
    public List<String> publicUUIDList;

}
