package fr.xgouchet.elmyr.inject.dummy;

import fr.xgouchet.elmyr.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class JavaInjectedStrings {

    @StringForgery(type = StringForgeryType.HEXADECIMAL)
    public String publicHexaString;

    @StringForgery(type = StringForgeryType.NUMERICAL, size = 42)
    public Set<String> publicNumericalStringSet;

    @RegexForgery("[abc]+")
    public String publicRegex;

    // 110e8400-e29b-11d4-a716-446655440000
    @RegexForgery("\\d{8}-\\d{4}-\\d{4}-\\d{4}-\\d{12}")
    public List<String> publicUUIDList;


    @StringForgery(regex = "[a-z]+@[a-z]+\\.[a-z]{3}")
    public String publicEmail;

}
