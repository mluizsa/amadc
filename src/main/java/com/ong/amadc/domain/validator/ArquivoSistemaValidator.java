package com.ong.amadc.domain.validator;

import com.ong.amadc.config.infra.exception.BusinessException;
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

    public void validarUploadPDF(MultipartFile arquivo, String tipoVinculoId) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new IllegalArgumentException("O arquivo enviado não pode estar vazio.");
        }

        String contentType = arquivo.getContentType();

        boolean ehImagem = contentType != null && (
                contentType.equals("image/jpeg") ||
                        contentType.equals("image/png") ||
                        contentType.equals("image/jpg")
        );

        boolean ehPdf = contentType != null && contentType.equals("application/pdf");

        // Permite imagens e PDFs globalmente ou de acordo com a regra do negócio
        if (!ehImagem && !ehPdf) {
            throw new BusinessException("Formato de arquivo inválido. Apenas imagens (JPEG, PNG) e documentos PDF são permitidos.");
        }

        // Validação opcional de tamanho (ex: máximo de 10MB para PDFs, 5MB para imagens)
        long tamanhoMaximo = 10 * 1024 * 1024; // 10 MB
        if (arquivo.getSize() > tamanhoMaximo) {
            throw new BusinessException("O arquivo excede o tamanho máximo permitido de 10MB.");
        }
    }
}