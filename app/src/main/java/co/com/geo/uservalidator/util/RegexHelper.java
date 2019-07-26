package co.com.geo.uservalidator.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexHelper {

    /**
     * Expresión regular para email.
     */
    private static String sEmail = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$";


    /**
     * Expresión regular para password.
     * Passwords will contain at least (1) upper case letter
     * Passwords will contain at least (1) lower case letter
     * Passwords will contain at least (1) number or special character
     * Passwords will contain at least (6) characters in length
     * Password maximum length should not be arbitrarily limited
     */
    private static String sPassword = "(?=^.{6,}$)((?=.*\\d)|(?=.*\\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$";

    /**
     * Constructor.
     */
    private RegexHelper() {
    }

    /**
     * Verifica si un texto cumple con el formato de Email.
     *
     * @param sEmailInput Texto de entrada
     * @return Verdadero si el texto de entrada cumple con el formato de Email, falso en caso contrario
     */
    public static boolean isValidEmail(String sEmailInput) {
        return isMatch(sEmailInput, sEmail);
    }

    /**
     * Verifica si un texto cumple con la política de Password.
     *
     * @param sPasswordInput texto de entrada
     * @return verdadero si el texto de entrada cumple con la política de Password, falso en caso contrario
     */
    public static boolean isValidPassword(String sPasswordInput) {
        return isMatch(sPasswordInput, sPassword);
    }

    /**
     * Valida si una cadena de texto cumple con una regla dada.
     * @param s Cadena
     * @param pattern Padrón a comparar
     * @return Verdadero si cumple la validación, falso en otro caso
     */
    private static boolean isMatch(String s, String pattern) {
        try {
            Pattern patt = Pattern.compile(pattern);
            Matcher matcher = patt.matcher(s);
            return matcher.matches();
        } catch (RuntimeException e) {
            return false;
        }
    }

}
