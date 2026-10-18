package br.com.desafio.compradireta.domain.usecase.compradireta.listar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.desafio.compradireta.domain.constant.CriterioJulgamento;
import br.com.desafio.compradireta.domain.constant.Modalidade;
import br.com.desafio.compradireta.domain.constant.StatusCompraDireta;
import br.com.desafio.compradireta.domain.entity.entities.CompraDireta;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.ICompraDiretaDataProvider;
import br.com.desafio.compradireta.domain.pagination.Pagina;
import br.com.desafio.compradireta.domain.pagination.Paginacao;
import br.com.desafio.compradireta.domain.usecase.compradireta.dto.CompraDiretaResumoOutput;

@ExtendWith(MockitoExtension.class)
class ListarComprasDiretasUseCaseTest {

    @Mock
    private ICompraDiretaDataProvider compraDiretaDataProvider;

    private ListarComprasDiretasUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = ListarComprasDiretasUseCase.builder()
                .compraDiretaDataProvider(compraDiretaDataProvider)
                .build();
    }

    @Test
    void deveListarTodasAsComprasSemPaginacaoTest() {
        CompraDireta cd = CompraDireta.builder()
                .id(1L)
                .numero("CD-0001/2026")
                .modalidade(Modalidade.DISPENSA_DE_LICITACAO)
                .criterioJulgamento(CriterioJulgamento.MENOR_PRECO)
                .status(StatusCompraDireta.RASCUNHO)
                .build();

        when(compraDiretaDataProvider.listarTodas()).thenReturn(List.of(cd));

        List<CompraDiretaResumoOutput> resultado = useCase.executar();

        assertEquals(1, resultado.size());
        assertEquals("CD-0001/2026", resultado.get(0).getNumero());
        verify(compraDiretaDataProvider).listarTodas();
    }

    @Test
    void deveListarComprasComPaginacaoTest() {
        Paginacao paginacao = Paginacao.of(0, 10);
        CompraDireta cd = CompraDireta.builder()
                .id(2L)
                .numero("CD-0002/2026")
                .modalidade(Modalidade.INEXIGIBILIDADE)
                .criterioJulgamento(CriterioJulgamento.NAO_SE_APLICA)
                .status(StatusCompraDireta.PUBLICADA)
                .build();

        Pagina<CompraDireta> pagina = Pagina.of(List.of(cd), 1, 0, 10);
        when(compraDiretaDataProvider.listarPaginado(paginacao)).thenReturn(pagina);

        Pagina<CompraDiretaResumoOutput> resultado = useCase.executar(paginacao);

        assertEquals(1, resultado.getContent().size());
        assertEquals("CD-0002/2026", resultado.getContent().get(0).getNumero());
        assertEquals(1, resultado.getTotalElements());
        verify(compraDiretaDataProvider).listarPaginado(paginacao);
    }
}
