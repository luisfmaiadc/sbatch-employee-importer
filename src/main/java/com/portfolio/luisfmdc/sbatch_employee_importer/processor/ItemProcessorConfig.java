package com.portfolio.luisfmdc.sbatch_employee_importer.processor;

import com.portfolio.luisfmdc.sbatch_employee_importer.domain.Funcionario;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.batch.infrastructure.item.validator.BeanValidatingItemProcessor;
import org.springframework.batch.infrastructure.item.validator.ValidatingItemProcessor;
import org.springframework.batch.infrastructure.item.validator.ValidationException;
import org.springframework.batch.infrastructure.item.validator.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Configuration
public class ItemProcessorConfig {

    private final Set<String> funcionariosEmailSet = new HashSet<>();
    private static final BigDecimal SALARIO_LIMITE = new BigDecimal("100000");

    @Bean
    public ItemProcessor<Funcionario, Funcionario> validatorItemProcessor() throws Exception {
        return new CompositeItemProcessorBuilder<Funcionario, Funcionario>()
                .delegates(beanValidatingItemProcessor(), validatingItemProcessor())
                .build();
    }

    private BeanValidatingItemProcessor<Funcionario> beanValidatingItemProcessor() throws Exception {
        BeanValidatingItemProcessor<Funcionario> processor = new BeanValidatingItemProcessor<>();
        processor.setFilter(true);
        processor.afterPropertiesSet();
        return processor;
    }

    private ValidatingItemProcessor<Funcionario> validatingItemProcessor() {
        ValidatingItemProcessor<Funcionario> processor = new ValidatingItemProcessor<>();
        processor.setValidator(validator());
        processor.setFilter(true);
        return processor;
    }

    private Validator<Funcionario> validator() {
        return funcionario -> {
            if (funcionariosEmailSet.contains(funcionario.getEmail())
                    || !isDataAdmissaoValida(funcionario.getDataAdmissao())
                    || !isSalarioValido(funcionario.getSalario())) {
                log.warn("Erro de validação do funcionário: {}", funcionario.getEmail());
                throw new ValidationException("Erro de validação do funcionário: " + funcionario.getEmail());
            }
            funcionariosEmailSet.add(funcionario.getEmail());
        };
    }

    private boolean isDataAdmissaoValida(String dataAdmissaoStr) {
        try {
            LocalDate dataAdmissao = LocalDate.parse(dataAdmissaoStr);
            return !dataAdmissao.isAfter(LocalDate.now());
        } catch (DateTimeException e) {
            return false;
        }
    }

    private boolean isSalarioValido(String salarioStr) {
        try {
            BigDecimal salario = new BigDecimal(salarioStr);
            return salario.compareTo(BigDecimal.ZERO) > 0 && salario.compareTo(SALARIO_LIMITE) <= 0;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}