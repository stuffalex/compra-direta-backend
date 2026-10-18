package br.com.desafio.compradireta.domain.pagination;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

class PaginaTest {

    @Test
    void deveCalcularTotalPaginasCorretamenteTest() {
        Pagina<String> pagina = Pagina.of(List.of("A", "B", "C"), 25, 0, 10);

        assertEquals(3, pagina.getTotalPages());
        assertEquals(25, pagina.getTotalElements());
        assertEquals(0, pagina.getPageNumber());
        assertEquals(10, pagina.getPageSize());
        assertEquals(3, pagina.getContent().size());
    }

    @Test
    void deveMapearConteudoCorretamenteTest() {
        Pagina<Integer> paginaOriginal = Pagina.of(List.of(1, 2, 3), 3, 0, 10);
        Pagina<String> paginaMapeada = paginaOriginal.map(Object::toString);

        assertEquals(List.of("1", "2", "3"), paginaMapeada.getContent());
        assertEquals(3, paginaMapeada.getTotalElements());
        assertEquals(1, paginaMapeada.getTotalPages());
    }

    @Test
    void deveLidarComListaNulaRetornandoVaziaTest() {
        Pagina<String> pagina = Pagina.of(null, 0, 0, 10);

        assertNotNull(pagina.getContent());
        assertTrue(pagina.getContent().isEmpty());
        assertEquals(0, pagina.getTotalPages());
    }
}
