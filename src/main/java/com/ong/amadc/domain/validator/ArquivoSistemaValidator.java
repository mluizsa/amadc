package com.ong.amadc.domain.validator;

import com.ong.amadc.domain.repository.TipoVinculoArquivoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class ArquivoSistemaValidator {

    private final TipoVinculoArquivoRepository tipoVinculoRepository;

    public void validarUpload(MultipartFile arquivo, String tipoVinculoId) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new IllegalArgumentException("O arquivo enviado está vazio ou é inválido.");
        }

        if (!tipoVinculoRepository.existsById(tipoVinculoId)) {
            throw new IllegalArgumentException("Tipo de vínculo '" + tipoVinculoId + "' não é válido no sistema.");
        }
        String contentType = arquivo.getContentType();
        if (contentType != null && !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Apenas arquivos de imagem são permitidos neste módulo.");
        }
    }
}