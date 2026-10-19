package br.com.desafio.compradireta.adapter.entrypoint.controller.compradireta;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.NotNull;
import br.com.desafio.compradireta.domain.pagination.Pagina;
import br.com.desafio.compradireta.domain.pagination.Paginacao;

import br.com.desafio.compradireta.domain.usecase.compradireta.apurar.ApurarVencedorCompraDiretaUseCase;
import br.com.desafio.compradireta.domain.usecase.compradireta.atualizar.AtualizarCompraDiretaInput;
import br.com.desafio.compradireta.domain.usecase.compradireta.atualizar.AtualizarCompraDiretaUseCase;
import br.com.desafio.compradireta.domain.usecase.compradireta.buscar.BuscarCompraDiretaOutput;
import br.com.desafio.compradireta.domain.usecase.compradireta.buscar.BuscarCompraDiretaUseCase;
import br.com.desafio.compradireta.domain.usecase.compradireta.cancelar.CancelarCompraDiretaInput;
import br.com.desafio.compradireta.domain.usecase.compradireta.cancelar.CancelarCompraDiretaUseCase;
import br.com.desafio.compradireta.domain.usecase.compradireta.criar.CriarCompraDiretaInput;
import br.com.desafio.compradireta.domain.usecase.compradireta.criar.CriarCompraDiretaUseCase;
import br.com.desafio.compradireta.domain.usecase.compradireta.dto.CompraDiretaResumoOutput;
import br.com.desafio.compradireta.domain.usecase.compradireta.listar.ListarComprasDiretasUseCase;
import br.com.desafio.compradireta.domain.usecase.compradireta.publicar.PublicarCompraDiretaUseCase;
import br.com.desafio.compradireta.domain.usecase.notificacao.listar.ListarNotificacoesPorCompraUseCase;
import br.com.desafio.compradireta.domain.usecase.notificacao.listar.NotificacaoFornecedorOutput;
import br.com.desafio.compradireta.domain.usecase.proposta.dto.PropostaOutput;
import br.com.desafio.compradireta.domain.usecase.proposta.listar.ListarPropostasPorCompraUseCase;
import br.com.desafio.compradireta.domain.usecase.proposta.registrar.RegistrarPropostaInput;
import br.com.desafio.compradireta.domain.usecase.proposta.registrar.RegistrarPropostaUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/compras-diretas")
@RequiredArgsConstructor
@Tag(name = "Compras Diretas", description = "Cadastro e ciclo de vida de compras diretas")
public class CompraDiretaController {

    private final CriarCompraDiretaUseCase criarCompraDiretaUseCase;
    private final ListarComprasDiretasUseCase listarComprasDiretasUseCase;
    private final BuscarCompraDiretaUseCase buscarCompraDiretaUseCase;
    private final AtualizarCompraDiretaUseCase atualizarCompraDiretaUseCase;
    private final PublicarCompraDiretaUseCase publicarCompraDiretaUseCase;
    private final CancelarCompraDiretaUseCase cancelarCompraDiretaUseCase;
    private final ApurarVencedorCompraDiretaUseCase apurarVencedorCompraDiretaUseCase;
    private final RegistrarPropostaUseCase registrarPropostaUseCase;
    private final ListarPropostasPorCompraUseCase listarPropostasPorCompraUseCase;
    private final ListarNotificacoesPorCompraUseCase listarNotificacoesPorCompraUseCase;

    @PostMapping
    @Operation(summary = "Cria uma nova compra direta em RASCUNHO")
    public ResponseEntity<CompraDiretaResumoOutput> criar(@RequestBody CriarCompraDiretaInput input) {
        return ResponseEntity.status(HttpStatus.CREATED).body(criarCompraDiretaUseCase.executar(input));
    }

    @GetMapping
    @Operation(summary = "Lista compras diretas com suporte opcional a paginação")
    public ResponseEntity<Pagina<CompraDiretaResumoOutput>> listar(
            @RequestParam(required = true) @NotNull Integer page,
            @RequestParam(required = true) @NotNull Integer size) {
        Paginacao paginacao = Paginacao.of(page == null ? 0 : page, size == null ? 20 : size);
        return ResponseEntity.ok(listarComprasDiretasUseCase.executar(paginacao));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca o detalhe de uma compra direta")
    public ResponseEntity<BuscarCompraDiretaOutput> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(buscarCompraDiretaUseCase.executar(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma compra direta em RASCUNHO")
    public ResponseEntity<CompraDiretaResumoOutput> atualizar(@PathVariable Long id,
            @RequestBody AtualizarCompraDiretaInput input) {
        input.setId(id);
        return ResponseEntity.ok(atualizarCompraDiretaUseCase.executar(input));
    }

    @PostMapping("/{id}/publicar")
    @Operation(summary = "Publica a compra direta e notifica os fornecedores convidados")
    public ResponseEntity<CompraDiretaResumoOutput> publicar(@PathVariable Long id) {
        return ResponseEntity.ok(publicarCompraDiretaUseCase.executar(id));
    }

    @PostMapping("/{id}/cancelar")
    @Operation(summary = "Cancela uma compra direta")
    public ResponseEntity<CompraDiretaResumoOutput> cancelar(@PathVariable Long id,
            @RequestBody CancelarCompraDiretaInput input) {
        input.setId(id);
        return ResponseEntity.ok(cancelarCompraDiretaUseCase.executar(input));
    }

    @PostMapping("/{id}/apurar-vencedor")
    @Operation(summary = "Apura o fornecedor vencedor conforme o critério de julgamento da compra")
    public ResponseEntity<CompraDiretaResumoOutput> apurarVencedor(@PathVariable Long id) {
        return ResponseEntity.ok(apurarVencedorCompraDiretaUseCase.executar(id));
    }

    @PostMapping("/{id}/propostas")
    @Operation(summary = "Registra a proposta de um fornecedor convidado")
    public ResponseEntity<PropostaOutput> registrarProposta(@PathVariable Long id,
            @RequestBody RegistrarPropostaInput input) {
        input.setCompraDiretaId(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(registrarPropostaUseCase.executar(input));
    }

    @GetMapping("/{id}/propostas")
    @Operation(summary = "Lista as propostas recebidas para a compra direta com suporte opcional a paginação")
    public ResponseEntity<?> listarPropostas(
            @PathVariable Long id,
            @RequestParam(required = true) @NotNull Integer page,
            @RequestParam(required = true) @NotNull Integer size) {
        if (page != null || size != null) {
            Paginacao paginacao = Paginacao.of(page == null ? 0 : page, size == null ? 20 : size);
            return ResponseEntity.ok(listarPropostasPorCompraUseCase.executar(id, paginacao));
        }
        return ResponseEntity.ok(listarPropostasPorCompraUseCase.executar(id));
    }

    @GetMapping("/{id}/notificacoes")
    @Operation(summary = "Lista as notificações enviadas aos fornecedores desta compra")
    public ResponseEntity<List<NotificacaoFornecedorOutput>> listarNotificacoes(@PathVariable Long id) {
        return ResponseEntity.ok(listarNotificacoesPorCompraUseCase.executar(id));
    }
}
