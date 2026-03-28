package br.com.doeaqui.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class SlugGenerator {
    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public String generate(String input) {
        if (input == null || input.isBlank()) {
            return "";
        }

        // Substitui espaços por traços
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        
        // Remove acentos e caracteres especiais
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NON_LATIN.matcher(normalized).replaceAll("");
        
        // Normaliza traços múltiplos, remove traços nas pontas e coloca em minúsculo
        return slug.toLowerCase(Locale.ENGLISH)
                   .replaceAll("-{2,}", "-")
                   .replaceAll("^-", "")
                   .replaceAll("-$", "");
    }
}
