package br.com.doeaqui.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class SlugGeneratorTest {

    private final SlugGenerator slugGenerator = new SlugGenerator();

    @ParameterizedTest
    @CsvSource({
        "Eletrônicos, eletronicos",
        "Home Appliances, home-appliances",
        "Casa & Decoração!, casa-decoracao",
        "  Espaços   Múltiplos  , espacos-multiplos",
        "---Traços--Iniciais---e---Finais---, tracos-iniciais-e-finais",
        "Smartphone 5G Pro, smartphone-5g-pro",
        "Móveis e Escritório, moveis-e-escritorio"
    })
    @DisplayName("Deve gerar slug corretamente para diferentes entradas de texto")
    void shouldGenerateCorrectSlug(String input, String expected) {
        String result = slugGenerator.generate(input);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("Deve retornar string vazia para entrada nula")
    void shouldReturnEmptyStringForNullInput() {
        String result = slugGenerator.generate(null);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar string vazia para entrada vazia ou com espaços")
    void shouldReturnEmptyStringForEmptyOrBlankInput() {
        assertThat(slugGenerator.generate("")).isEmpty();
        assertThat(slugGenerator.generate("   ")).isEmpty();
    }
}
