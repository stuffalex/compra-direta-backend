package br.com.desafio.compradireta.domain.interfaces.dataprovider;

import java.util.List;
import java.util.Optional;

import br.com.desafio.compradireta.domain.entity.entities.CompraDireta;
import br.com.desafio.compradireta.domain.pagination.Pagina;
import br.com.desafio.compradireta.domain.pagination.Paginacao;

public interface ICompraDiretaDataProvider {

    CompraDireta salvar(CompraDireta compraDireta);

    Optional<CompraDireta> buscarPorId(Long id);

    List<CompraDireta> listarTodas();

    Pagina<CompraDireta> listarPaginado(Paginacao paginacao);

    long contarPorAno(int ano);
}
