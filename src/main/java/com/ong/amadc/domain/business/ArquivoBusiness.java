package com.ong.amadc.domain.business;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ArquivoBusiness {

    /**
     * Orquestra a geração do nome normatizado extraindo a extensão diretamente do MultipartFile.
     */
    public String extrairEGerarNomeNormatizado(MultipartFile arquivo,
                                               String nomeIdentificador,
                                               String tipoVinculoId,
                                               int contador) {
        String nomeOriginal = arquivo.getOriginalFilename();

        String extensao = nomeOriginal != null && nomeOriginal.contains(".")
                ? nomeOriginal.substring(nomeOriginal.lastIndexOf("."))
                : ".jpg";

        return gerarNomeNormatizado(nomeIdentificador, tipoVinculoId, extensao, contador);
    }

    public String gerarNomeNormatizado(String nomeIdentificador,
                                       String tipoVinculoId,
                                       String extensaoOriginal,
                                       int contador) {
        String data = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        String hora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));

        String extensao = formatarExtensao(extensaoOriginal);
        String nomeSanitizado = sanitizarNome(nomeIdentificador);

        String sufixo = (contador > 0) ? "_" + contador : "";

        return String.format("AMADC_%s_%s_%s_%s%s%s",
                data, hora, tipoVinculoId.toUpperCase(), nomeSanitizado, sufixo, extensao);
    }

    public void salvarArquivoNoDisco(MultipartFile arquivo, String nomeFinal, String diretorioBase) {
        try {
            Path pastaDestino = Paths.get(diretorioBase).toAbsolutePath().normalize();
            if (!Files.exists(pastaDestino)) {
                Files.createDirectories(pastaDestino);
            }
            Path arquivoDestino = pastaDestino.resolve(nomeFinal);
            Files.copy(arquivo.getInputStream(), arquivoDestino, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Falha de I/O ao gravar o arquivo físico no servidor.", e);
        }
    }

    private String sanitizarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) return "sem_nome";
        return Normalizer.normalize(nome, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9]", "_")
                .replaceAll("_+", "_")
                .replaceAll("^_|_$", "");
    }

    private String formatarExtensao(String extensao) {
        if (extensao == null || extensao.trim().isEmpty()) return ".jpg";
        String ext = extensao.trim().toLowerCase();
        return ext.startsWith(".") ? ext : "." + ext;
    }
}