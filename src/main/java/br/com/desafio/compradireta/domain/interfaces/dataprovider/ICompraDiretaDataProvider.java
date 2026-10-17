package br.com.desafio.compradireta.domain.interfaces.dataprovider;

import java.util.List;
import java.util.Optional;

import br.com.desafio.compradireta.domain.entity.entities.CompraDireta;

public interface ICompraDiretaDataProvider {

    CompraDireta salvar(CompraDireta compraDireta);

    Optional<CompraDireta> buscarPorId(Long id);

    List<CompraDireta> listarTodas();

    long contarPorAno(int ano);
}
